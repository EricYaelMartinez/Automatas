import javax.swing.*;
import java.awt.*;

public class TapePanel extends JPanel {
    private String tape;
    private int headPosition;
    private String currentState;

    public TapePanel(String tape) {
        this.tape = tape;
        this.headPosition = 0; // El cabezal empieza al principio
        this.currentState = "q0";
        setPreferredSize(new Dimension(1000, 150));
        setBackground(Color.BLACK);
    }

    public void updateTape(String tape, int headPosition, String currentState) {
        this.tape = tape;
        this.headPosition = headPosition;
        this.currentState = currentState;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Monospaced", Font.PLAIN, 30));

        int x = 50;
        int y = 50;
        int boxSize = 40;

        // Dibujar la cinta
        for (int i = 0; i < tape.length(); i++) {
            if (i == headPosition) {
                g.setColor(Color.GREEN); // Color del cabezal
                g.drawRect(x - 2, y - 2, boxSize + 4, boxSize + 4);
            } else {
                g.setColor(Color.WHITE);
            }
            g.drawRect(x, y, boxSize, boxSize); // Dibujar cuadro de la cinta
            g.drawString(String.valueOf(tape.charAt(i)), x + 10, y + 30); // Dibujar símbolo de la cinta
            x += boxSize;
        }

        // Dibujar el estado actual encima del cabezal
        g.setColor(Color.RED);
        g.drawString("V", 50 + headPosition * boxSize, y - 10);
        g.drawString(currentState, 50 + headPosition * boxSize, y - 30);
    }
}
