import event.EventManager;
import event.EventType;
import event.Observer;
import model.ExtractedData;
import model.NodeAPP6;
import net.miginfocom.swing.MigLayout;
import search.SearchComponentPanel;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.Serial;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @author PingouInfini
 */
public class SymbolSelectorFrame extends JDialog implements Observer {
    @Serial
    private static final long serialVersionUID = -1108393012654115976L;

    private static final String FRENCH_LANGUAGE = "FR";
    private static final String ENGLISH_LANGUAGE = "EN";

    private final JPanel dialogPane = new JPanel();
    private final int FONT_SIZE = 10;

    /* Init Dimensions */
    private int nodeNumber;
    private static final int DEFAULT_SPACE = 5;

    // BEHAVIOURS
    private static final int BEHAVIOUR_JBUTTON_HEIGHT = 50;
    private static final int BEHAVIOUR_JBUTTON_WIDTH = SymbolSelectorFrame.BEHAVIOUR_JBUTTON_HEIGHT;
    private static final int BEHAVIOUR_PANEL_HEIGHT = 6
            * (SymbolSelectorFrame.BEHAVIOUR_JBUTTON_HEIGHT + SymbolSelectorFrame.DEFAULT_SPACE);
    private static final int BEHAVIOUR_PANEL_WIDTH = SymbolSelectorFrame.BEHAVIOUR_JBUTTON_WIDTH + 10;

    // SYMBOLS
    private static final int SYMBOL_JBUTTON_HEIGHT = SymbolSelectorFrame.BEHAVIOUR_JBUTTON_HEIGHT;
    private static final int SYMBO_JBUTTON_WIDTH = 400;
    private int symboPanelHeight;
    private static final int SYMBO_PANEL_WIDTH = SymbolSelectorFrame.SYMBO_JBUTTON_WIDTH + 7;

    // BACK & NEXT
    private int backJButtonHeight;
    private static final int BACK_JBUTTON_WIDTH = SymbolSelectorFrame.BEHAVIOUR_JBUTTON_WIDTH;
    private int backPanelHeight;
    private static final int BACK_PANEL_WIDTH = SymbolSelectorFrame.BACK_JBUTTON_WIDTH + 10;
    private static final int NEXT_JBUTTON_HEIGHT = SymbolSelectorFrame.SYMBOL_JBUTTON_HEIGHT;
    private static final int NEXT_JBUTTON_WIDTH = SymbolSelectorFrame.BACK_JBUTTON_WIDTH;
    private int nextPanelHeight;
    private static final int NEXT_PANEL_WIDTH = SymbolSelectorFrame.BACK_JBUTTON_WIDTH + 10;

    private int dialogPanelHeight;
    private static final int DIALOG_PANEL_WIDTH = SymbolSelectorFrame.BEHAVIOUR_PANEL_WIDTH + SymbolSelectorFrame.BACK_PANEL_WIDTH
            + SymbolSelectorFrame.SYMBO_PANEL_WIDTH + SymbolSelectorFrame.NEXT_PANEL_WIDTH + 6 * SymbolSelectorFrame.DEFAULT_SPACE + 20;

    private String behaviour = "h";
    private String language = ENGLISH_LANGUAGE;
    private boolean presumed = false;

    private final ExtractedData extractedData;
    private final Set<String> historiqueRecherche;
    private String startSymbole;
    private final String iconeDirectoryPath;

    public SymbolSelectorFrame(ExtractedData extractedData, Set<String> historiqueRecherche, String startSymbole, String iconeDirectoryPath) {
        this.extractedData = extractedData;
        this.historiqueRecherche = historiqueRecherche;
        this.startSymbole = startSymbole;
        this.iconeDirectoryPath = iconeDirectoryPath;

        NodeAPP6 startnode = extractedData.getNode();

        if (startSymbole != null && startSymbole.length() > 4) {
            final char c = startSymbole.charAt(2);
            startSymbole = startSymbole.replace(c, 'X');
            startnode = extractedData.getNode().getParentNodeByHierarchy(startSymbole);

            if (startnode == null) {
                startnode = extractedData.getNode();
            }

            if ("fhun".contains(String.valueOf(c).toLowerCase())) {
                this.behaviour = String.valueOf(c).toLowerCase();
            }
            if ("spa".contains(String.valueOf(c).toLowerCase())) {
                this.behaviour = String.valueOf(c).toLowerCase();
                this.presumed = true;
            }
        }

        adaptSize(startnode.getChildren().size());
        initMainPanel();
        initComponents(startnode);
    }

    /**
     * Adapts component size. Only the values affected by the number of nodes are modified
     */
    private void adaptSize(int size) {
        this.nodeNumber = size;

        this.symboPanelHeight = this.nodeNumber * SymbolSelectorFrame.SYMBOL_JBUTTON_HEIGHT
                + (this.nodeNumber + 2) * SymbolSelectorFrame.DEFAULT_SPACE;

        this.backJButtonHeight = this.symboPanelHeight - 3 * SymbolSelectorFrame.DEFAULT_SPACE;
        this.backPanelHeight = this.symboPanelHeight;
        this.nextPanelHeight = this.symboPanelHeight;

        this.dialogPanelHeight = Math.max(this.symboPanelHeight, SymbolSelectorFrame.BEHAVIOUR_PANEL_HEIGHT) + 100;
    }

    private void initMainPanel() {
        setAlwaysOnTop(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        setTitle("Sélection du symbole APP6");
        setType(Window.Type.POPUP);
        setModal(true);

        this.dialogPane.setBorder(
                new javax.swing.border.CompoundBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EmptyBorder(0, 0, 0, 0),
                        "", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.BOTTOM,
                        new java.awt.Font("Dialog", java.awt.Font.BOLD, FONT_SIZE), java.awt.Color.red), this.dialogPane.getBorder()));
        this.dialogPane.addPropertyChangeListener(e -> {
            if ("border".equals(e.getPropertyName()))
                throw new RuntimeException();
        });
        this.dialogPane.setLayout(new BorderLayout());
    }

    private void initComponents(NodeAPP6 node) {
        final JPanel contentPanel = new JPanel();
        final JPanel languagePanel = new JPanel();
        final JPanel searchPanel = new JPanel();
        final JPanel behaviourPanel = new JPanel();
        final JPanel backPanel = new JPanel();
        final JPanel symbolPanel = new JPanel();
        final JPanel nextPanel = new JPanel();
        final JPanel blankPanel = createBlankPanel();

        final Container container = getContentPane();
        container.setLayout(new BorderLayout());

        contentPanel.setLayout(new MigLayout("fill,insets dialog,hidemode 3,align center center",
                // columns
                "[10!]" //
                        + "[" + (SymbolSelectorFrame.BEHAVIOUR_PANEL_WIDTH) + "!,center]" //
                        + "[" + (SymbolSelectorFrame.BACK_PANEL_WIDTH - SymbolSelectorFrame.DEFAULT_SPACE) + "!,fill]" //
                        + "[fill]" //
                        + "[" + (SymbolSelectorFrame.NEXT_PANEL_WIDTH + SymbolSelectorFrame.DEFAULT_SPACE) + "!,fill]" //
                        + "[10!]",
                // rows
                "[]" //
                        + "[]"));

        initLanguagePanel(node, languagePanel);
        contentPanel.add(languagePanel, "cell 3 0 2 1,alignx trailing center,grow 0 0");

        initSearchPanel(searchPanel);
        contentPanel.add(searchPanel, "cell 1 0 4 1, gapy 10");
        EventManager.getInstance().subscribe(EventType.UI_REDRAW, this);

        initBehaviourPanel(node, behaviourPanel, blankPanel);
        contentPanel.add(behaviourPanel, "cell 1 1");

        initBackPanel(node, backPanel);
        contentPanel.add(backPanel, "cell 2 1");

        initSymbolPanel(node, symbolPanel);
        contentPanel.add(symbolPanel, "cell 3 1");

        initNextPanel(node, nextPanel);
        contentPanel.add(nextPanel, "cell 4 1");

        this.dialogPane.add(contentPanel, BorderLayout.CENTER);

        container.add(this.dialogPane, BorderLayout.CENTER);
        setSize(SymbolSelectorFrame.DIALOG_PANEL_WIDTH, this.dialogPanelHeight);
    }

    private void setPanelSize(JPanel jpanel, int WIDTH, int HEIGHT) {
        jpanel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        jpanel.setMinimumSize(new Dimension(WIDTH, HEIGHT));
        jpanel.setMaximumSize(new Dimension(WIDTH, HEIGHT));
    }

    private void initLanguagePanel(NodeAPP6 node, final JPanel languagePanel) {
        setPanelSize(languagePanel, NEXT_PANEL_WIDTH * 2, DEFAULT_SPACE * 4);
        languagePanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        // ---- textAreaEN ----
        final JLabel usEnglishFlag = new JLabel();
        usEnglishFlag.setIcon(getImageIconFromPath("/images/navigation/usenglish_flag.png", 20, 15));
        languagePanel.add(usEnglishFlag);

        // ---- ToggleSwitch ----
        final ToggleSwitch ts = createToggleSwitch(!this.language.equals("EN"));
        ts.addPropertyChangeListener(evt -> {
            if (evt.getPropertyName().equals("activated")) {
                final boolean activated = (boolean) evt.getNewValue();
                this.language = activated ? FRENCH_LANGUAGE : ENGLISH_LANGUAGE;
                this.extractedData.setMapDescriptionHierarchy(
                        switchSearchDataset(this.extractedData.getMapDescriptionHierarchy()));

                redraw(node);
            }
        });

        languagePanel.add(ts);

        // ---- textAreaFR ----
        final JLabel frenchFlag = new JLabel();
        frenchFlag.setIcon(getImageIconFromPath("/images/navigation/french_flag.png", 20, 15));
        languagePanel.add(frenchFlag);
    }

    private void initSearchPanel(JPanel searchPanel) {
        setPanelSize(searchPanel, 350, DEFAULT_SPACE * 8);
        searchPanel.setLayout(new MigLayout("fill,hidemode 3,aligny center",
                // columns
                "[fill]",
                // rows
                "[]"));

        setPanelSize(searchPanel, 375, 40);
        searchPanel.add(new SearchComponentPanel(this.extractedData.getMapDescriptionHierarchy(),
                this.historiqueRecherche, "/images/navigation/").createSearchPanel());
    }

    private void initBehaviourPanel(NodeAPP6 node, final JPanel behaviourPanel, JPanel blankPanel) {
        setPanelSize(behaviourPanel, BEHAVIOUR_PANEL_WIDTH, BEHAVIOUR_PANEL_HEIGHT);
        behaviourPanel.setLayout(new MigLayout("hidemode 3,align center center,gapy 0"
                + SymbolSelectorFrame.DEFAULT_SPACE,
                // columns
                "[fill]",
                // rows
                "[]" + "[]" + "[]" + "[]" + "[]" + "[]" + "[]" + "[]"));

        final char[] behaviorProved = {'u', 'f', 'n', 'h'};
        final char[] behaviorPresumed = {'p', 'a', 'n', 's'};

        // ---- ToggleSwitch ----
        final ToggleSwitch ts2 = createToggleSwitch(this.presumed);
        ts2.addPropertyChangeListener(evt -> {
            if (evt.getPropertyName().equals("activated")) {
                final boolean activated = (boolean) evt.getNewValue();
                if (activated) {
                    this.presumed = true;
                    this.behaviour = String.valueOf(behaviorPresumed[new String(behaviorProved).indexOf(this.behaviour)]);
                } else {
                    this.presumed = false;
                    this.behaviour = String.valueOf(behaviorProved[new String(behaviorPresumed).indexOf(this.behaviour)]);
                }
                redraw(node);
            }
        });
        behaviourPanel.add(createDisabledJTextArea(language.equals("EN") ? "PRESUMED" : "PRÉSUMÉ"), "cell 0 0");
        behaviourPanel.add(ts2, "cell 0 1");
        behaviourPanel.add(blankPanel, "cell 0 2");

        char[] behaviours;
        if (!this.presumed) {
            behaviours = new char[]{'u', 'f', 'n', 'h'};
        } else {
            behaviours = new char[]{'p', 'a', 'n', 's'};
        }

        for (int i = 0; i < behaviours.length; i++) {
            final JButton button = createBehaviourButton(behaviours[i]);
            button.putClientProperty("behaviour", behaviours[i]);
            button.addActionListener(e -> {
                this.behaviour = String.valueOf(((JButton) e.getSource()).getClientProperty("behaviour"));
                redraw(node);
            });
            behaviourPanel.add(button, "cell 0 " + (i + 3));
        }

        for (int j = 4; j < 7; j++)
            behaviourPanel.add(blankPanel, "cell 0 " + (behaviours.length + j));

    }

    private void initBackPanel(NodeAPP6 node, final JPanel backPanel) {
        setPanelSize(backPanel, BACK_PANEL_WIDTH, this.backPanelHeight);
        backPanel.setLayout(new MigLayout("fill,hidemode 3,align center center,gapy " + SymbolSelectorFrame.DEFAULT_SPACE,
                // columns
                "[fill]",
                // rows
                "[]"));

        // ---- button back ----
        if (!node.getHierarchy().isEmpty()) {
            final JButton buttonBack = createButton(
                    "/images/navigation/left.png", null,
                    SymbolSelectorFrame.BACK_JBUTTON_WIDTH, this.backJButtonHeight);

            addListenerNavigation(buttonBack, node.getParent());
            backPanel.add(buttonBack, "cell 0 0");
        }
    }

    private void initSymbolPanel(NodeAPP6 node, final JPanel symbolPanel) {
        setPanelSize(symbolPanel, SYMBO_PANEL_WIDTH, this.symboPanelHeight);
        symbolPanel.setLayout(new MigLayout("fill,hidemode 3,align left center,gapy " + SymbolSelectorFrame.DEFAULT_SPACE,
                // columns
                "[fill]",
                // rows
                "[]"));

        for (int i = 0; i < this.nodeNumber; i++) {
            final String hierarchy = node.getChildren().get(i).getHierarchy();
            String name = switch (this.language) {
                case ENGLISH_LANGUAGE -> node.getChildren().get(i).getName();
                case FRENCH_LANGUAGE -> node.getChildren().get(i).getNameFR();
                default -> "";
            };

            final JButton button = createSymbolButton(hierarchy, name);
            button.setFont(button.getFont().deriveFont(button.getFont().getStyle() & Font.BOLD, FONT_SIZE));
            button.setEnabled(node.getChildren().get(i).getSymbolCode() != null);

            button.addActionListener(e -> {
                // Code executé lors du clic sur le bouton
                firePropertyChange("selectedSymbol", "", hierarchy.replace("X", SymbolSelectorFrame.this.behaviour));
            });

            if (this.startSymbole.equals(hierarchy)) {
                Border compoundBorder = new CompoundBorder(new LineBorder(Color.GREEN, 5),
                        new EmptyBorder(0, 12, 0, 0));
                button.setBorder(compoundBorder);
            }

            symbolPanel.add(button, "cell 0 " + i);
        }
    }

    private void initNextPanel(NodeAPP6 node, final JPanel nextPanel) {
        setPanelSize(nextPanel, NEXT_PANEL_WIDTH, this.nextPanelHeight);
        nextPanel.setLayout(new MigLayout("fill,hidemode 3,align center center,gapy " + SymbolSelectorFrame.DEFAULT_SPACE,
                // columns
                "[fill]",
                // rows
                "[]"));

        for (int i = 0; i < this.nodeNumber; i++) {
            JButton buttonNext;
            // If the node displayed has children
            if (!node.getChildren().get(i).getChildren().isEmpty()) {
                buttonNext = createButton(
                        "/images/navigation/right.png", null,
                        SymbolSelectorFrame.NEXT_JBUTTON_WIDTH, SymbolSelectorFrame.NEXT_JBUTTON_HEIGHT);
                addListenerNavigation(buttonNext, node.getChildren().get(i));
            } else {
                buttonNext = new JButton();
                buttonNext.setPreferredSize(new Dimension(SymbolSelectorFrame.NEXT_JBUTTON_WIDTH, SymbolSelectorFrame.NEXT_JBUTTON_HEIGHT));
                buttonNext.setOpaque(false); // transparent
                buttonNext.setContentAreaFilled(false);
                buttonNext.setBorderPainted(false);
            }
            nextPanel.add(buttonNext, "cell 0 " + i);
        }
    }

    private ToggleSwitch createToggleSwitch(boolean activated) {
        final ToggleSwitch toggleSwitch = new ToggleSwitch();
        toggleSwitch.setPreferredSize(new Dimension(NEXT_PANEL_WIDTH / 2, DEFAULT_SPACE * 2));
        toggleSwitch.setMinimumSize(new Dimension(NEXT_PANEL_WIDTH / 2, DEFAULT_SPACE * 2));
        toggleSwitch.setMaximumSize(new Dimension(NEXT_PANEL_WIDTH / 2, DEFAULT_SPACE * 2));
        toggleSwitch.setActivated(activated);

        return toggleSwitch;
    }

    private JPanel createBlankPanel() {
        final JPanel jPanel = new JPanel();
        setPanelSize(jPanel, BEHAVIOUR_PANEL_WIDTH, DEFAULT_SPACE * 3);
        return jPanel;
    }

    private JTextArea createDisabledJTextArea(String text) {
        final JTextArea textArea = new JTextArea();
        textArea.setText(text);
        textArea.setEnabled(false);
        textArea.setBackground(null);
        textArea.setFont(new Font("Arial Black", Font.BOLD, 8));
        return textArea;
    }

    private JButton createBehaviourButton(char behaviour) {
        String iconPath = "/images/behaviour/1." + behaviour + ".3.1.png";
        return createButton(iconPath, null, SymbolSelectorFrame.BEHAVIOUR_JBUTTON_WIDTH, SymbolSelectorFrame.BEHAVIOUR_JBUTTON_HEIGHT);
    }

    private JButton createSymbolButton(String hierarchy, String description) {
        final String iconPath = "/images/icons/" +//
                "/" +//
                hierarchy.replace("X", this.behaviour) +//
                ".png";

        final JButton jButton = createButton(iconPath, description, SymbolSelectorFrame.SYMBO_JBUTTON_WIDTH,
                SymbolSelectorFrame.SYMBOL_JBUTTON_HEIGHT);
        jButton.setHorizontalAlignment(SwingConstants.LEFT);

        return jButton;
    }

    private JButton createButton(String iconPath, String text, int width, int height) {
        final JButton jButton = new JButton();
        jButton.setIcon(getImageIconFromPath(iconPath));

        if (text != null && !text.isEmpty())
            jButton.setText(text);

        jButton.setPreferredSize(new Dimension(width, height));
        jButton.setMaximumSize(new Dimension(width, height));
        jButton.setMinimumSize(new Dimension(width, height));
        jButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jButton.setHorizontalAlignment(SwingConstants.CENTER);
        jButton.setRequestFocusEnabled(false);
        return jButton;
    }

    private ImageIcon getImageIconFromPath(String iconPath) {
        return getImageIconFromPath(iconPath, BEHAVIOUR_JBUTTON_WIDTH - 10, BEHAVIOUR_JBUTTON_HEIGHT - 10);
    }

    private ImageIcon getImageIconFromPath(String iconPath, int width, int height) {
        if (iconPath != null) try {
            Image image = new ImageIcon(Objects.requireNonNull(getClass().getResource(iconPath))).getImage();
            return new ImageIcon(image.getScaledInstance(width, height, Image.SCALE_SMOOTH));
        } catch (Exception ignored) {
        }
        return new ImageIcon(Objects.requireNonNull(getClass().getResource("/images/navigation/notfound.png")));
    }

    private void addListenerNavigation(final JButton jbouton, final NodeAPP6 node) {
        jbouton.addActionListener(e -> redraw(node));
    }

    private void redraw(NodeAPP6 node) {
        final JPanel panel = getDialogPane();
        panel.removeAll();
        adaptSize(node.getChildren().size());
        initComponents(node);
        validate();
    }

    private Map<String, String> switchSearchDataset(Map<String, String> mapDescriptionHierarchy) {
        Map<String, String> switchedMap = new HashMap<>();

        for (Map.Entry<String, String> entry : mapDescriptionHierarchy.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            // Search for the "/" in the key
            int index = key.indexOf(" / ");

            // If "/" is found, swap the left and right parts
            if (index != -1) {
                String leftPart = key.substring(0, index).trim();
                String rightPart = key.substring(index + 3).trim();
                String switchedKey = rightPart + " / " + leftPart;
                switchedMap.put(switchedKey, value);
            } else {
                // If "/" is not found, keep the original key
                switchedMap.put(key, value);
            }
        }
        return switchedMap;
    }

    private JPanel getDialogPane() {
        return this.dialogPane;
    }

    @Override
    public void update(String hierarchy) {
        this.startSymbole = hierarchy;
        redraw(this.extractedData.getNode().getParentNodeByHierarchy(hierarchy));
    }
}
