<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Árbol de Decisión ID3</title>
    <style>
        /* Reset básico */
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Poppins', sans-serif;
            background: linear-gradient(135deg, #e0f7fa, #ffffff);
            color: #333;
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 40px 20px;
        }

        .card {
            background-color: #ffffff;
            max-width: 900px;
            width: 100%;
            padding: 40px;
            border-radius: 16px;
            box-shadow: 0 12px 30px rgba(0, 0, 0, 0.1);
            animation: fadeIn 0.6s ease;
        }

        h2 {
            text-align: center;
            color: #0d47a1;
            font-size: 2.2em;
            margin-bottom: 30px;
        }

        .error {
            color: #d32f2f;
            text-align: center;
            font-weight: bold;
            font-size: 1.2em;
        }

        .tree-output {
            background-color: #f1f8e9;
            color: #2e7d32;
            padding: 20px;
            border-left: 6px solid #81c784;
            border-radius: 12px;
            font-family: 'Courier New', Courier, monospace;
            white-space: pre-wrap;
            overflow-x: auto;
            font-size: 1em;
            line-height: 1.6;
        }

        /* Animación suave de entrada */
        @keyframes fadeIn {
            from {
                opacity: 0;
                transform: translateY(20px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }

        /* Responsive */
        @media (max-width: 768px) {
            .card {
                padding: 25px;
            }

            h2 {
                font-size: 1.5em;
            }

            .tree-output {
                font-size: 0.95em;
            }
        }
    </style>
</head>
<body>
    <div class="card">
        <h2>Árbol de Decisión Generado (ID3)</h2>
        <%
            String tree = (String) request.getAttribute("tree");
            if (tree == null) {
        %>
            <p class="error">No se pudo generar el árbol.</p>
        <%
            } else {
        %>
            <div class="tree-output"><%= tree %></div>
        <%
            }
        %>
    </div>
</body>
</html>
