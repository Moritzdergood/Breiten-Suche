import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GRAPH graph = new BAHNHOEFE().bahnhofsGraphErstellen();
            JFrame fenster = new JFrame("Bahnhofssuche");
            fenster.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            fenster.setContentPane(inhaltErstellen(graph));
            fenster.pack();
            fenster.setLocationRelativeTo(null);
            fenster.setVisible(true);
        });
    }

    static JPanel inhaltErstellen(GRAPH graph) {
        String[] bahnhoefe = graph.bahnhofsnamenGeben();
        JComboBox<String> start = new JComboBox<>(bahnhoefe);
        JComboBox<String> ziel = new JComboBox<>(bahnhoefe);
        start.setSelectedItem("München");
        ziel.setSelectedItem("Hamburg");

        JButton suchen = new JButton("Route suchen");
        JPanel auswahl = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        JLabel startLabel = new JLabel("Start");
        startLabel.setLabelFor(start);
        JLabel zielLabel = new JLabel("Ziel");
        zielLabel.setLabelFor(ziel);
        auswahl.add(startLabel);
        auswahl.add(start);
        auswahl.add(zielLabel);
        auswahl.add(ziel);
        auswahl.add(suchen);

        JTextArea ergebnis = new JTextArea(14, 52);
        ergebnis.setEditable(false);
        ergebnis.setLineWrap(true);
        ergebnis.setWrapStyleWord(true);
        ergebnis.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextArea matrix = new JTextArea(graph.adjaText());
        matrix.setEditable(false);
        matrix.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        matrix.setTabSize(4);
        matrix.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTabbedPane ansichten = new JTabbedPane();
        ansichten.addTab("Suchergebnis", new JScrollPane(ergebnis));
        ansichten.addTab("Adjazenzmatrix", new JScrollPane(matrix));

        suchen.addActionListener(ereignis -> {
            ergebnis.setText(graph.breitensucheText(
                    (String) start.getSelectedItem(), (String) ziel.getSelectedItem()));
            ergebnis.setCaretPosition(0);
            ansichten.setSelectedIndex(0);
        });
        suchen.doClick(0);

        JPanel inhalt = new JPanel(new BorderLayout(0, 8));
        inhalt.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        inhalt.add(auswahl, BorderLayout.NORTH);
        inhalt.add(ansichten, BorderLayout.CENTER);
        return inhalt;
    }
}
