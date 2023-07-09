import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

/**
 * @author PingouInfini
 */
public class SymbolSelector extends JDialog {

    // TODO: DEBUT : constantes de DEBUG/test, à supprimer
    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private int NODE_NUMBER = 7;
    // TODO: FIN

    /**
     * Dimensions
     **/
    private int DEFAULT_SPACE = 5;

    // BEHAVIORS
    private int BEHAVIOR_JBUTTON_HEIGHT = 40;
    private int BEHAVIOR_JBUTTON_WIDTH = 40;
    private int BEHAVIOR_PANEL_HEIGHT = 6 * (BEHAVIOR_JBUTTON_HEIGHT + DEFAULT_SPACE);
    private int BEHAVIOR_PANEL_WIDTH = BEHAVIOR_JBUTTON_WIDTH + 10;

    // SYMBOLS
    private int SYMBOL_JBUTTON_HEIGHT = 40;
    private int SYMBO_JBUTTON_WIDTH = 400; // TODO à adapter ?
    private int SYMBO_PANEL_HEIGHT = NODE_NUMBER * SYMBOL_JBUTTON_HEIGHT + (NODE_NUMBER + 2) * DEFAULT_SPACE;
    private int SYMBO_PANEL_WIDTH = SYMBO_JBUTTON_WIDTH + 7;

    // BACK & NEXT
    private int BACK_JBUTTON_HEIGHT = SYMBO_PANEL_HEIGHT - 3 * DEFAULT_SPACE;
    private int BACK_JBUTTON_WIDTH = 40;
    private int BACK_PANEL_HEIGHT = SYMBO_PANEL_HEIGHT;
    private int BACK_PANEL_WIDTH = BACK_JBUTTON_WIDTH + 10;
    private int NEXT_JBUTTON_HEIGHT = SYMBOL_JBUTTON_HEIGHT;
    private int NEXT_JBUTTON_WIDTH = BACK_JBUTTON_WIDTH;
    private int NEXT_PANEL_HEIGHT = SYMBO_PANEL_HEIGHT;
    private int NEXT_PANEL_WIDTH = BACK_JBUTTON_WIDTH;


    private int DIALOG_PANEL_HEIGHT = Math.max(SYMBO_PANEL_HEIGHT, BEHAVIOR_PANEL_HEIGHT) + 50;
    private int DIALOG_PANEL_WIDTH = BEHAVIOR_PANEL_WIDTH + BACK_PANEL_WIDTH + SYMBO_PANEL_WIDTH + NEXT_PANEL_WIDTH
            + 6 * DEFAULT_SPACE;


    public SymbolSelector() {
        initComponents();
    }

    private void initComponents() {
        JPanel dialogPane = new JPanel();
        JPanel contentPanel = new JPanel();
        JPanel behaviourPanel = new JPanel();
        JPanel backPanel = new JPanel();
        JPanel symbolPanel = new JPanel();
        JPanel nextPanel = new JPanel();

        //======== this ========
        setAlwaysOnTop(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setModal(true);
        setResizable(false);
        setTitle("S\u00e9lection du symbole APP6");
        setType(Window.Type.POPUP);
        var contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setBorder(new javax.swing.border.CompoundBorder(new javax.swing.border.TitledBorder(new javax.
                    swing.border.EmptyBorder(0, 0, 0, 0), "", javax.swing.border
                    .TitledBorder.CENTER, javax.swing.border.TitledBorder.BOTTOM, new java.awt.Font("Dialog"
                    , java.awt.Font.BOLD, 12), java.awt.Color.red), dialogPane.getBorder
                    ()));
            dialogPane.addPropertyChangeListener(e -> {
                if ("border".equals(e.getPropertyName())) throw new RuntimeException
                        ();
            });
            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {
                contentPanel.setLayout(new MigLayout(
                        "fill,insets dialog,hidemode 3,align center center",
                        // columns
                        "[40!,center]" +
                                "[40!,fill]" +
                                "[fill]" +
                                "[40!,fill]",
                        // rows
                        "[]"));

                //======== behaviourPanel ========
                {
                    behaviourPanel.setPreferredSize(new Dimension(BEHAVIOR_PANEL_WIDTH, BEHAVIOR_PANEL_HEIGHT));
                    behaviourPanel.setMinimumSize(new Dimension(BEHAVIOR_PANEL_WIDTH, BEHAVIOR_PANEL_HEIGHT));
                    behaviourPanel.setMaximumSize(new Dimension(BEHAVIOR_PANEL_WIDTH, BEHAVIOR_PANEL_HEIGHT));
                    behaviourPanel.setLayout(new MigLayout(
                            "hidemode 3,align center center,gapy " + DEFAULT_SPACE,
                            // columns
                            "[fill]",
                            // rows
                            "[]" +
                                    "[]" +
                                    "[]" +
                                    "[]"));

                    char[] behaviors = {'u', 'f', 'n', 'h'};
                    for (int i = 0; i < behaviors.length; i++) {
                        JButton button = createBehaviorButton(behaviors[i]);
                        behaviourPanel.add(button, "cell 0 " + i);

                    }
                }
                contentPanel.add(behaviourPanel, "cell 0 0");

                //======== backPanel ========
                {
                    backPanel.setPreferredSize(new Dimension(BACK_PANEL_WIDTH, BACK_PANEL_HEIGHT));
                    backPanel.setMinimumSize(new Dimension(BACK_PANEL_WIDTH, BACK_PANEL_HEIGHT));
                    backPanel.setMaximumSize(new Dimension(BACK_PANEL_WIDTH, BACK_PANEL_HEIGHT));
                    backPanel.setLayout(new MigLayout(
                            "fill,hidemode 3,align center center,gapy " + DEFAULT_SPACE,
                            // columns
                            "[fill]",
                            // rows
                            "[]"));

                    //---- button back ----
                    JButton buttonBack = createButton("/images/navigation/left.png", null,
                            BACK_JBUTTON_WIDTH, BACK_JBUTTON_HEIGHT);
                    backPanel.add(buttonBack, "cell 0 0");
                }
                contentPanel.add(backPanel, "cell 1 0");

                //======== symbolPanel ========
                {
                    symbolPanel.setPreferredSize(new Dimension(SYMBO_PANEL_WIDTH, SYMBO_PANEL_HEIGHT));
                    symbolPanel.setMinimumSize(new Dimension(SYMBO_PANEL_WIDTH, SYMBO_PANEL_HEIGHT));
                    symbolPanel.setMaximumSize(new Dimension(SYMBO_PANEL_WIDTH, SYMBO_PANEL_HEIGHT));
                    symbolPanel.setLayout(new MigLayout(
                            "fill,hidemode 3,align left center,gapy " + DEFAULT_SPACE,
                            // columns
                            "[fill]",
                            // rows
                            "[]"));

                    for (int i = 0; i < NODE_NUMBER; i++) {
                        JButton button = createSymbolButton("getHierarchy()", generateRandomWord(i));
                        symbolPanel.add(button, "cell 0 " + i);
                    }
                }
                contentPanel.add(symbolPanel, "cell 2 0");

                //======== nextPanel ========
                {
                    nextPanel.setPreferredSize(new Dimension(NEXT_PANEL_WIDTH, NEXT_PANEL_HEIGHT));
                    nextPanel.setMinimumSize(new Dimension(NEXT_PANEL_WIDTH, NEXT_PANEL_HEIGHT));
                    nextPanel.setMaximumSize(new Dimension(NEXT_PANEL_WIDTH, NEXT_PANEL_HEIGHT));
                    nextPanel.setLayout(new MigLayout(
                            "fill,hidemode 3,align center center,gapy " + DEFAULT_SPACE,
                            // columns
                            "[fill]",
                            // rows
                            "[]"));

                    for (int i = 0; i < NODE_NUMBER; i++) {
                        // TODO: 50% de chance d'avoir un bouton, à remplacer par test "if hasChild"
                        JButton buttonBack;
                        if (Math.random() < 0.5) {
                            buttonBack = createButton("/images/navigation/right.png", null,
                                    NEXT_JBUTTON_WIDTH, NEXT_JBUTTON_HEIGHT);
                            nextPanel.add(buttonBack, "cell 0 " + i);
                        } else {
                            buttonBack = new JButton();
                            buttonBack.setPreferredSize(new Dimension(NEXT_JBUTTON_WIDTH, NEXT_JBUTTON_HEIGHT));
                            buttonBack.setOpaque(false);  // transparent
                            buttonBack.setContentAreaFilled(false);
                            buttonBack.setBorderPainted(false);

                        }
                        nextPanel.add(buttonBack, "cell 0 " + i);
                    }
                }
                contentPanel.add(nextPanel, "cell 3 0");
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);
        }
        contentPane.add(dialogPane, BorderLayout.CENTER);
        setSize(DIALOG_PANEL_WIDTH, DIALOG_PANEL_HEIGHT);
        setLocationRelativeTo(null);
    }

    private JButton createBehaviorButton(char behavior) {
        String iconPath = "/images/behavior/1." + behavior + ".3.1.png";
        return createButton(iconPath, null, BEHAVIOR_JBUTTON_WIDTH, BEHAVIOR_JBUTTON_HEIGHT);
    }

    private JButton createSymbolButton(String hierachy, String description) {
        String iconPath = "/images/behavior/1." + "f" + ".3.1.png";
        // TODO: JButton jButton = createButton(hierachy, description, SYMBO_JBUTTON_WIDTH, SYMBOL_JBUTTON_HEIGHT);
        // TODO et virer le iconPath
        JButton jButton = createButton(iconPath, description, SYMBO_JBUTTON_WIDTH, SYMBOL_JBUTTON_HEIGHT);
        jButton.setHorizontalAlignment(SwingConstants.LEFT);
        return jButton;
    }

    private JButton createButton(String iconPath, String text, int width, int height) {
        JButton jButton = new JButton();
        if (iconPath != null)
            jButton.setIcon(new ImageIcon(getClass().getResource(iconPath)));
        if (text != null && !text.isEmpty())
            jButton.setText(text);
        jButton.setPreferredSize(new Dimension(width, height));
        jButton.setMaximumSize(new Dimension(width, height));
        jButton.setMinimumSize(new Dimension(width, height));
        jButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jButton.setHorizontalAlignment(SwingConstants.CENTER);
        return jButton;
    }

    // TODO: DEBUT methode de DEBUG/test, à supprimer
    private static String generateRandomWord(int i) {
        Random random = new Random();
        StringBuilder word = new StringBuilder();
        word.append(i + 1).append("-");
        int maxLength = random.nextInt(84) + 8;

        while (word.length() < maxLength) {
            int index = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(index);
            word.append(randomChar);
        }

        return word.toString();
    }
    // TODO: FIN

    // TODO: DEBUT MAIN de DEBUG/test, à supprimer
    public static void main(String args[]) {
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new SymbolSelector().setVisible(true));
    }
    // TODO: FIN
}
