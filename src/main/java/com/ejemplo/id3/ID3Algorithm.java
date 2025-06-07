package com.ejemplo.id3;

import java.util.*;

public class ID3Algorithm {

    public static class Node {
        String atributo; // nombre del atributo (nodo de decisión)
        String valor;    // valor que llevó a este nodo desde su padre
        String label;    // si es hoja, el valor de clase
        List<Node> hijos = new ArrayList<>();

        public boolean esHoja() {
            return label != null;
        }
    }

    public Node buildTree(List<Map<String, String>> datos, List<String> atributos) {
        // Caso base: todos tienen la misma clase
        if (mismosValores(datos)) {
            Node hoja = new Node();
            hoja.label = datos.get(0).get("label");
            return hoja;
        }

        // Caso base: no hay más atributos para dividir
        if (atributos.isEmpty()) {
            Node hoja = new Node();
            hoja.label = claseMayoritaria(datos);
            return hoja;
        }

        // Elegir el mejor atributo (con mayor ganancia)
        String mejorAtributo = seleccionarMejorAtributo(datos, atributos);

        Node nodo = new Node();
        nodo.atributo = mejorAtributo;

        Set<String> valores = obtenerValores(datos, mejorAtributo);
        for (String val : valores) {
            List<Map<String, String>> subconjunto = filtrarPorValor(datos, mejorAtributo, val);
            if (subconjunto.isEmpty()) {
                Node hoja = new Node();
                hoja.valor = val;
                hoja.label = claseMayoritaria(datos);
                nodo.hijos.add(hoja);
            } else {
                List<String> nuevosAtributos = new ArrayList<>(atributos);
                nuevosAtributos.remove(mejorAtributo);
                Node hijo = buildTree(subconjunto, nuevosAtributos);
                hijo.valor = val;
                nodo.hijos.add(hijo);
            }
        }

        return nodo;
    }

    public String printTree(Node nodo, String indentacion) {
        StringBuilder sb = new StringBuilder();

        if (nodo.esHoja()) {
            sb.append(indentacion).append("→ ").append(nodo.label).append("\n");
        } else {
            for (Node hijo : nodo.hijos) {
                sb.append(indentacion).append(nodo.atributo)
                  .append(" = ").append(hijo.valor).append(":\n")
                  .append(printTree(hijo, indentacion + "   "));
            }
        }

        return sb.toString();
    }

    private boolean mismosValores(List<Map<String, String>> datos) {
        String clase = datos.get(0).get("label");
        for (Map<String, String> fila : datos) {
            if (!fila.get("label").equals(clase)) return false;
        }
        return true;
    }

    private String claseMayoritaria(List<Map<String, String>> datos) {
        Map<String, Integer> conteo = new HashMap<>();
        for (Map<String, String> fila : datos) {
            String label = fila.get("label");
            conteo.put(label, conteo.getOrDefault(label, 0) + 1);
        }
        return Collections.max(conteo.entrySet(), Map.Entry.comparingByValue()).getKey();
    }

    private String seleccionarMejorAtributo(List<Map<String, String>> datos, List<String> atributos) {
        double mejorGanancia = Double.NEGATIVE_INFINITY;
        String mejor = null;
        for (String atributo : atributos) {
            double ganancia = calcularGanancia(datos, atributo);
            if (ganancia > mejorGanancia) {
                mejorGanancia = ganancia;
                mejor = atributo;
            }
        }
        return mejor;
    }

    private double calcularGanancia(List<Map<String, String>> datos, String atributo) {
        double entropiaTotal = calcularEntropia(datos);
        Map<String, List<Map<String, String>>> particiones = new HashMap<>();
        for (Map<String, String> fila : datos) {
            String valor = fila.get(atributo);
            particiones.computeIfAbsent(valor, k -> new ArrayList<>()).add(fila);
        }

        double entropiaEsperada = 0.0;
        for (List<Map<String, String>> grupo : particiones.values()) {
            double peso = (double) grupo.size() / datos.size();
            entropiaEsperada += peso * calcularEntropia(grupo);
        }

        return entropiaTotal - entropiaEsperada;
    }

    private double calcularEntropia(List<Map<String, String>> datos) {
        Map<String, Integer> conteo = new HashMap<>();
        for (Map<String, String> fila : datos) {
            String clase = fila.get("label");
            conteo.put(clase, conteo.getOrDefault(clase, 0) + 1);
        }

        double entropia = 0.0;
        for (int freq : conteo.values()) {
            double p = (double) freq / datos.size();
            entropia -= p * (Math.log(p) / Math.log(2));
        }

        return entropia;
    }

    private Set<String> obtenerValores(List<Map<String, String>> datos, String atributo) {
        Set<String> valores = new HashSet<>();
        for (Map<String, String> fila : datos) {
            valores.add(fila.get(atributo));
        }
        return valores;
    }

    private List<Map<String, String>> filtrarPorValor(List<Map<String, String>> datos, String atributo, String valor) {
        List<Map<String, String>> filtrado = new ArrayList<>();
        for (Map<String, String> fila : datos) {
            if (fila.get(atributo).equals(valor)) {
                filtrado.add(fila);
            }
        }
        return filtrado;
    }
}
