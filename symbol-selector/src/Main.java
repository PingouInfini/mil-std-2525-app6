import model.ExtractedData;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

public class Main extends JFrame {

    public Main() {
        setTitle("IHM Example");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 200);
        setLayout(new FlowLayout());

        JTextField textField = new JTextField(10);
        JButton goButton = new JButton("Select an APP6 Icon");
        goButton.addActionListener(e -> showPopup());

        add(textField);
        add(goButton);
    }

    private void showPopup() {
        ExtractedData extractedData = App6Parser.readColumns(Objects.
                requireNonNull(App6Parser.class.getResource("/resources/icon-files-mapping.csv")).getFile());

        Set<String> historiqueRecherche = new TreeSet<>();
        historiqueRecherche.add("INFANTRY / INFANTERIE");
        historiqueRecherche.add("ARMOR TRACK / PISTE DE BLINDE");

        SymbolSelectorFrame symbolSelector = new SymbolSelectorFrame(extractedData, historiqueRecherche, "1.X.4.1.3", null);
        symbolSelector.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Main example = new Main();
            example.setVisible(true);
        });
    }
}