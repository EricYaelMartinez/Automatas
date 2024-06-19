import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class TuringMachine {
    private String tape;
    private int head;
    private String currentState;
    private final String initialState;
    private final String finalState;
    private final Map<String, String[]> transitions;
    private final TapePanel tapePanel;
    private final GraphVisualizer graphVisualizer;
    private final JTextArea transitionTextArea;
    private Timer timer;

    public TuringMachine(String tape, String initialState, String finalState, Map<String, String[]> transitions, TapePanel tapePanel, GraphVisualizer graphVisualizer, JTextArea transitionTextArea) {
        this.tape = tape;
        this.head = 0; // El cabezal empieza al principio
        this.currentState = initialState;
        this.initialState = initialState;
        this.finalState = finalState;
        this.transitions = transitions;
        this.tapePanel = tapePanel;
        this.graphVisualizer = graphVisualizer;
        this.transitionTextArea = transitionTextArea;
        this.tapePanel.updateTape(tape, head, currentState);
    }

    private void printTape() {
        System.out.println("Tape: " + tape);
        System.out.println("Head: " + " ".repeat(head) + "^");
    }

    private void step() {
        char symbol = tape.charAt(head);
        String key = currentState + "," + symbol;
        if (transitions.containsKey(key)) {
            String[] transition = transitions.get(key);
            String newState = transition[0];
            char writeSymbol = transition[1].charAt(0);
            char direction = transition[2].charAt(0);
            String transitionText = "δ: (" + currentState + ", " + symbol + ") -> (" + newState + ", " + writeSymbol + ", " + direction + ")";
            System.out.println(transitionText);
            transitionTextArea.append(transitionText + "\n"); // Actualizar el JTextArea con la transición
            tape = tape.substring(0, head) + writeSymbol + tape.substring(head + 1);
            currentState = newState;
            if (direction == 'R') {
                head++;
                if (head == tape.length()) {
                    tape += "B";
                }
            } else if (direction == 'L') {
                if (head == 0) {
                    tape = "B" + tape;
                } else {
                    head--;
                }
            }
            tapePanel.updateTape(tape, head, currentState); // Actualizar la animación
            graphVisualizer.updateGraph(currentState); // Actualizar el grafo
        }
    }

    private void countOnesAndShowResult() {
        int count = 0;
        for (char c : tape.toCharArray()) {
            if (c == '1') {
                count++;
            }
        }
        JOptionPane.showMessageDialog(null, "La suma es: " + count, "Resultado", JOptionPane.INFORMATION_MESSAGE);
    }

    public void run() {
        transitionTextArea.append("Cadena de entrada: " + tape + "\n"); // Mostrar la cadena de entrada
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!currentState.equals(finalState)) {
                    step();
                } else {
                    timer.stop();
                    printTape();
                    countOnesAndShowResult();
                }
            }
        });
        timer.start();
    }

    private static JTable createTransitionTable(DefaultTableModel tableModel) {
        String[] columnNames = {"Estados", "0", "1", "B"};
        String[][] data = {
            {"q0", "", "q1,1,R", ""},
            {"q1", "q2,1,R", "q1,1,R", ""},
            {"q2", "", "q2,1,R", "q3,B,L"},
            {"q3", "", "q4,B,R", ""},
            {"q4", "", "", ""}
        };

        for (String[] row : data) {
            tableModel.addRow(row);
        }

        JTable table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        return table;
    }

    public static void main(String[] args) {
        Map<String, String[]> transitions = new HashMap<>();
        transitions.put("q0,1", new String[]{"q1", "1", "R"});
        transitions.put("q1,1", new String[]{"q1", "1", "R"});
        transitions.put("q1,0", new String[]{"q2", "1", "R"});
        transitions.put("q2,1", new String[]{"q2", "1", "R"});
        transitions.put("q2,B", new String[]{"q3", "B", "L"});
        transitions.put("q3,1", new String[]{"q4", "B", "R"});

        String initialState = "q0";
        String finalState = "q4";

        // Solicitar la cadena de entrada utilizando un JOptionPane
        String inputTape = JOptionPane.showInputDialog(null, "Introduce la cadena de entrada:");

        // Validar que el usuario no haya cancelado el diálogo
        if (inputTape == null) {
            System.exit(0);
        }

        // Preparar la interfaz gráfica
        JFrame frame = new JFrame("Turing Machine");
        frame.setLayout(new BorderLayout());

        TapePanel tapePanel = new TapePanel(inputTape);
        frame.add(tapePanel, BorderLayout.NORTH);

        GraphVisualizer graphVisualizer = new GraphVisualizer(transitions, initialState, finalState);
        frame.add(graphVisualizer.getViewPanel(), BorderLayout.CENTER);

        JTextArea transitionTextArea = new JTextArea();
        transitionTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(transitionTextArea);
        scrollPane.setPreferredSize(new Dimension(800, 200));
        frame.add(scrollPane, BorderLayout.SOUTH);

        // Configurar la tabla de transiciones
        String[] columnNames = {"Estados", "0", "1", "B"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable transitionTable = createTransitionTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(transitionTable);
        tableScrollPane.setPreferredSize(new Dimension(400, 200));
        frame.add(tableScrollPane, BorderLayout.EAST);

        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Crear y ejecutar la máquina de Turing
        TuringMachine tm = new TuringMachine(inputTape, initialState, finalState, transitions, tapePanel, graphVisualizer, transitionTextArea);
        tm.run();
    }
}
