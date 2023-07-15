import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author PingouInfini
 */
public class SymbolSelectorFrame extends JDialog  {

    private JPanel dialogPane = new JPanel();

    /* Init Dimensions */
    private int NODE_NUMBER = 7;
    private int DEFAULT_SPACE = 5;

    // BEHAVIOURS
    private int BEHAVIOUR_JBUTTON_HEIGHT = 40;
    private int BEHAVIOUR_JBUTTON_WIDTH = 40;
    private int BEHAVIOUR_PANEL_HEIGHT = 6 * (BEHAVIOUR_JBUTTON_HEIGHT + DEFAULT_SPACE);
    private int BEHAVIOUR_PANEL_WIDTH = BEHAVIOUR_JBUTTON_WIDTH + 10;

    // SYMBOLS
    private int SYMBOL_JBUTTON_HEIGHT = 40;
    private int SYMBO_JBUTTON_WIDTH = 400;
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


    private int DIALOG_PANEL_HEIGHT = Math.max(SYMBO_PANEL_HEIGHT, BEHAVIOUR_PANEL_HEIGHT) + 50;
    private int DIALOG_PANEL_WIDTH = BEHAVIOUR_PANEL_WIDTH + BACK_PANEL_WIDTH + SYMBO_PANEL_WIDTH + NEXT_PANEL_WIDTH
            + 6 * DEFAULT_SPACE;

    private String BEHAVIOUR = "h";


    public SymbolSelectorFrame(Node node) {
        adaptSize(node.getChildren().size());
        initMainPanel();
        initComponents(node);
    }

    /**
     * Adapts component size. Only the values affected by the number of nodes are modified
     */
    private void adaptSize(int size) {
        NODE_NUMBER = size;

        SYMBO_PANEL_HEIGHT = NODE_NUMBER * SYMBOL_JBUTTON_HEIGHT + (NODE_NUMBER + 2) * DEFAULT_SPACE;

        BACK_JBUTTON_HEIGHT = SYMBO_PANEL_HEIGHT - 3 * DEFAULT_SPACE;
        BACK_PANEL_HEIGHT = SYMBO_PANEL_HEIGHT;
        NEXT_PANEL_HEIGHT = SYMBO_PANEL_HEIGHT;


        DIALOG_PANEL_HEIGHT = Math.max(SYMBO_PANEL_HEIGHT, BEHAVIOUR_PANEL_HEIGHT) + 50;
    }


    private void initMainPanel() {
        setAlwaysOnTop(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setModal(true);
        setResizable(false);
        setTitle("Sélection du symbole APP6");
        setType(Window.Type.POPUP);

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
    }


    private void initComponents(Node node) {
        JPanel contentPanel = new JPanel();
        JPanel behaviourPanel = new JPanel();
        JPanel backPanel = new JPanel();
        JPanel symbolPanel = new JPanel();
        JPanel nextPanel = new JPanel();

        //======== this ========
        var contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

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
                behaviourPanel.setPreferredSize(new Dimension(BEHAVIOUR_PANEL_WIDTH, BEHAVIOUR_PANEL_HEIGHT));
                behaviourPanel.setMinimumSize(new Dimension(BEHAVIOUR_PANEL_WIDTH, BEHAVIOUR_PANEL_HEIGHT));
                behaviourPanel.setMaximumSize(new Dimension(BEHAVIOUR_PANEL_WIDTH, BEHAVIOUR_PANEL_HEIGHT));
                behaviourPanel.setLayout(new MigLayout(
                        "hidemode 3,align center center,gapy " + DEFAULT_SPACE,
                        // columns
                        "[fill]",
                        // rows
                        "[]" +
                                "[]" +
                                "[]" +
                                "[]"));

                char[] behaviours = {'u', 'f', 'n', 'h'};
                for (int i = 0; i < behaviours.length; i++) {
                    JButton button = createBehaviourButton(behaviours[i]);
                    button.putClientProperty("behaviour", behaviours[i]);
                    button.addActionListener(e -> {
                        this.BEHAVIOUR = String.valueOf( ((JButton) e.getSource()).getClientProperty("behaviour"));
                        JPanel dialogPane = getDialogPane();
                        dialogPane.removeAll();
                        adaptSize(node.getChildren().size());
                        initComponents(node);
                        validate();
                    });
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
                if (!node.getHierarchy().isBlank()) {
                    JButton buttonBack = createButton("/images/navigation/left.png", null,
                            BACK_JBUTTON_WIDTH, BACK_JBUTTON_HEIGHT);

                    addListenerNavigation(buttonBack, node.getParent());
                    backPanel.add(buttonBack, "cell 0 0");
                }
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
                    String hierarchy = node.getChildren().get(i).getHierarchy();
                    String name = node.getChildren().get(i).getHierarchy()+" - "+node.getChildren().get(i).getName();
                    JButton button = createSymbolButton(hierarchy, name);
                    button.setFont(button.getFont().deriveFont(button.getFont().getStyle() & Font.BOLD, button.getFont().getSize() - 3f));

                    button.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            // TODO
                            // Code à exécuter lors du clic sur le bouton
                            //JOptionPane.showMessageDialog(null, "Le bouton a été cliqué !");

                        }
                    });
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
                    JButton buttonNext;
                    // If the node displayed has children
                    if (node.getChildren().get(i).getChildren().size() > 0) {
                        buttonNext = createButton("/images/navigation/right.png", null,
                                NEXT_JBUTTON_WIDTH, NEXT_JBUTTON_HEIGHT);
                        addListenerNavigation(buttonNext, node.getChildren().get(i));
                    } else {
                        buttonNext = new JButton();
                        buttonNext.setPreferredSize(new Dimension(NEXT_JBUTTON_WIDTH, NEXT_JBUTTON_HEIGHT));
                        buttonNext.setOpaque(false);  // transparent
                        buttonNext.setContentAreaFilled(false);
                        buttonNext.setBorderPainted(false);
                    }
                    nextPanel.add(buttonNext, "cell 0 " + i);
                }
            }
            contentPanel.add(nextPanel, "cell 3 0");
        }
        dialogPane.add(contentPanel, BorderLayout.CENTER);

        contentPane.add(dialogPane, BorderLayout.CENTER);
        setSize(DIALOG_PANEL_WIDTH, DIALOG_PANEL_HEIGHT);
        setLocationRelativeTo(null);
    }

    private JButton createBehaviourButton(char behaviour) {
        String iconPath = "/images/behaviour/1." + behaviour + ".3.1.png";
        return createButton(iconPath, null, BEHAVIOUR_JBUTTON_WIDTH, BEHAVIOUR_JBUTTON_HEIGHT);
    }

    private JButton createSymbolButton(String hierachy, String description) {
        String iconPath = "/images/icons/" + hierachy.replace("X", BEHAVIOUR) + ".png";
        JButton jButton = createButton(iconPath, description, SYMBO_JBUTTON_WIDTH, SYMBOL_JBUTTON_HEIGHT);
        jButton.setHorizontalAlignment(SwingConstants.LEFT);
        return jButton;
    }

    private JButton createButton(String iconPath, String text, int width, int height) {
        JButton jButton = new JButton();
        if (iconPath != null)
            try {
                jButton.setIcon(new ImageIcon(getClass().getResource(iconPath)));
            } catch (Exception e) {
                jButton.setIcon(new ImageIcon(getClass().getResource("/images/navigation/notfound.png")));
            }
        if (text != null && !text.isEmpty())
            jButton.setText(text);
        jButton.setPreferredSize(new Dimension(width, height));
        jButton.setMaximumSize(new Dimension(width, height));
        jButton.setMinimumSize(new Dimension(width, height));
        jButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jButton.setHorizontalAlignment(SwingConstants.CENTER);
        return jButton;
    }

    private void addListenerNavigation(final JButton jbouton, final Node node)
    {
        jbouton.addActionListener(e -> {
            JPanel dialogPane = getDialogPane();
            dialogPane.removeAll();

            adaptSize(node.getChildren().size());
            initComponents(node);
            validate();
        });
    }

    private JPanel getDialogPane() {
        return dialogPane;
    }
}
