import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {
    private JTextField textField;

    public Main() {
        setTitle("IHM Example");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 200);
        setLayout(new FlowLayout());

        textField = new JTextField(10);
        JButton goButton = new JButton("Select an APP6 Icon");
        goButton.addActionListener(e -> showPopup());

        add(textField);
        add(goButton);
    }

    private void showPopup() {
        Node nodes = App6Parser.readFirstColumn(App6Parser.class.getResource("/resources/icon-files-mapping.csv").getFile());

        SymbolSelectorFrame symbolSelector = new SymbolSelectorFrame(nodes);
        symbolSelector.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Main example = new Main();
            example.setVisible(true);
        });
    }
}