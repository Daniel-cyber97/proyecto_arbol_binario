package com.ejemplo.id3;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.*;
import java.util.*;

import weka.core.Instance;
import weka.core.Instances;

public class TreeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Iniciando TreeServlet...");

        InputStream arffStream = getServletContext().getResourceAsStream("/WEB-INF/clima.arff");

        if (arffStream == null) {
            System.out.println("ERROR: No se pudo encontrar el archivo clima.arff");
            req.setAttribute("tree", "<p style='color:red;'>Error: archivo .arff no encontrado.</p>");
            req.getRequestDispatcher("index.jsp").forward(req, resp);
            return;
        }

        // Carga los datos del archivo ARFF
        Instances data = new Instances(new InputStreamReader(arffStream));
        // Definir el índice de la clase (la última columna)
        data.setClassIndex(data.numAttributes() - 1);

        List<Map<String, String>> datos = new ArrayList<>();
        List<String> atributos = new ArrayList<>();

        // Guardar los nombres de los atributos excepto la clase
        for (int i = 0; i < data.numAttributes() - 1; i++) {
            atributos.add(data.attribute(i).name());
        }

        // Recorremos cada instancia para crear un mapa atributo->valor en String
        for (Instance inst : data) {
            Map<String, String> fila = new HashMap<>();
            for (String attr : atributos) {
                int attrIndex = data.attribute(attr).index();
                // Si el atributo es nominal, obtenemos stringValue
                if (data.attribute(attr).isNominal()) {
                    fila.put(attr, inst.stringValue(attrIndex));
                } else {
                    // Si es numérico, convertimos el valor numérico a cadena
                    fila.put(attr, String.valueOf(inst.value(attrIndex)));
                }
            }
            // La clase (label) siempre nominal en este ejemplo, se extrae como string
            fila.put("label", inst.stringValue(data.classAttribute()));
            datos.add(fila);
        }

        // Construcción del árbol con tu implementación ID3Algorithm
        ID3Algorithm id3 = new ID3Algorithm();
        ID3Algorithm.Node root = id3.buildTree(datos, atributos);

        // Generar el árbol como texto HTML para mostrarlo en la página
        String treeHtml = id3.printTree(root, "");
        System.out.println("Árbol generado:\n" + treeHtml);

        // Pasar el árbol a la JSP
        req.setAttribute("tree", treeHtml);
        req.getRequestDispatcher("index.jsp").forward(req, resp);
    }
}
