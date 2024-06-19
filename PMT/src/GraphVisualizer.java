import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.swing_viewer.SwingViewer;
import org.graphstream.ui.swing_viewer.DefaultView;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class GraphVisualizer {
    private final Graph graph;
    private final SwingViewer viewer;
    private final JPanel viewPanel;

    public GraphVisualizer(Map<String, String[]> transitions, String initialState, String finalState) {
        // Configurar la propiedad del sistema para GraphStream
        System.setProperty("org.graphstream.ui", "swing");

        graph = new SingleGraph("Turing Machine");

        // Estilo del grafo
        graph.setAttribute("ui.stylesheet", 
            "graph { padding: 30px; }" +
            "node { size: 20px, 20px; text-size: 10px; text-color: white; }" +
            "edge { text-size: 14px; text-color: red; }"
        );

        // Add nodes
        for (String state : new String[]{"q0", "q1", "q2", "q3", "q4"}) {
            Node node = graph.addNode(state);
            node.setAttribute("ui.label", state);
        }

        // Add edges
        for (Map.Entry<String, String[]> entry : transitions.entrySet()) {
            String fromState = entry.getKey().split(",")[0];
            String toState = entry.getValue()[0];
            String label = entry.getKey().split(",")[1] + " / " + entry.getValue()[1] + ", " + entry.getValue()[2];
            Edge edge = graph.addEdge(fromState + toState + label, fromState, toState, true);
            edge.setAttribute("ui.label", label);
        }

        graph.getNode(initialState).setAttribute("ui.style", "fill-color: green;");
        graph.getNode(finalState).setAttribute("ui.style", "fill-color: red;");

        viewer = new SwingViewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        viewer.enableAutoLayout();

        DefaultView view = (DefaultView) viewer.addDefaultView(false); // False for a Swing view
        view.setPreferredSize(new Dimension(800, 600));

        viewPanel = new JPanel(new BorderLayout());
        viewPanel.add(view, BorderLayout.CENTER);
        viewPanel.setPreferredSize(new Dimension(800, 600));
    }

    public void updateGraph(String currentState) {
        for (Node node : graph) {
            node.setAttribute("ui.style", "fill-color: black;");
        }
        graph.getNode(currentState).setAttribute("ui.style", "fill-color: blue;");
    }

    public JPanel getViewPanel() {
        return viewPanel;
    }
}
