package fr.pingouinfini;

import fr.pingouinfini.symbology.*;
import fr.pingouinfini.view.Item;
import net.miginfocom.swing.MigLayout;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.ImageTranscoder;
import org.w3c.dom.svg.SVGDocument;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;


/**
 * @author pingouinfini
 */
public class SymbolSelectorFrame extends JPanel {

    private static MilitarySymbol milSym = MilitarySymbolFactory.createSymbol("10-0-6-10-2-0-13-121102-09-51");
    private SymbolSetEntityModifierTree entityModifierTree;

    private static JFormattedTextField formattedTextField1;

    private JComboBox<Item> entityComboBox;
    private JComboBox<Item> entityTypeComboBox;
    private JComboBox<Item> entitySubTypeComboBox;
    private JComboBox<Item> sectorOneModifierComboBox;
    private JComboBox<Item> sectorTwoModifierComboBox;
    private JPanel panel15;


    private JPanel svgSymbolPanel;

    public SymbolSelectorFrame() {
        initComponents();
    }

    private void initComponents() {
        JDialog symbolSelectorFrame = new JDialog();
        JPanel leftSideSelectorFrame = new JPanel();
        JPanel rightSideSelectorFrame = new JPanel();
        JPanel affiliationPanel = new JPanel();
        formattedTextField1 = new JFormattedTextField();
        JTabbedPane tabbedPane1 = new JTabbedPane();
        JPanel symbolParts = new JPanel();
        entityComboBox = new JComboBox<>();
        entityTypeComboBox = new JComboBox<>();
        entitySubTypeComboBox = new JComboBox<>();
        sectorOneModifierComboBox = new JComboBox<>();
        sectorTwoModifierComboBox = new JComboBox<>();
        JPanel statusPanel = new JPanel();
        panel15 = new JPanel();


        svgSymbolPanel = new JPanel();

        //======== symbolSelectorFrame ========
        {
            symbolSelectorFrame.setAlwaysOnTop(true);
            symbolSelectorFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            symbolSelectorFrame.setResizable(true); // TODO: keep this ?
            symbolSelectorFrame.setTitle("Sélection du symbole APP6");
            symbolSelectorFrame.setType(Window.Type.POPUP);
            //symbolSelectorFrame.setModal(true);

            Container symbolSelectorContentPane = symbolSelectorFrame.getContentPane();
            symbolSelectorContentPane.setLayout(new GridLayout(1, 2));

            //======== leftSideSelectorFrame ========
            {
                leftSideSelectorFrame.setBorder(new javax.swing.border.CompoundBorder(new javax.swing.border.TitledBorder(new javax
                        .swing.border.EmptyBorder(0, 0, 0, 0), "", javax.swing
                        .border.TitledBorder.CENTER, javax.swing.border.TitledBorder.BOTTOM, new java.awt.
                        Font("Dialog", java.awt.Font.BOLD, 12), java.awt.Color.red
                ), leftSideSelectorFrame.getBorder()));
                leftSideSelectorFrame.addPropertyChangeListener(e -> {
                    if ("border".equals(e.getPropertyName(
                    ))) throw new RuntimeException();
                });
                leftSideSelectorFrame.setLayout(new MigLayout(
                        "insets 0,hidemode 3,gap 0 0",
                        // columns
                        "[grow,fill]",
                        // rows
                        "[grow,fill]" +
                                "[grow,fill]" +
                                "[grow,fill]"));

                //======== affiliationPanel ========
                {
                    affiliationPanel.setBorder(new TitledBorder("Affiliation"));
                    affiliationPanel.setLayout(new FlowLayout());

                    for (StandardEntityTwos standardEntityTwos : StandardEntityTwos.values()) {

                        JButton button = new JButton();
                        button.setText(standardEntityTwos.name());
                        button.addActionListener(e -> {
                            milSym.setStandardEntity(StandardEntityTwos.getEnum(standardEntityTwos.getSidcPart()));
                            updateSvgPanel();
                            formattedTextField1.setText(formatSIDC(milSym.toString()));
                        });
                        affiliationPanel.add(button);
                    }
                }
                leftSideSelectorFrame.add(affiliationPanel, "cell 0 0,height 110:110:100");

                formattedTextField1.setText(formatSIDC(milSym.toString()));
                leftSideSelectorFrame.add(formattedTextField1, "cell 0 1");

                //======== tabbedPane1 ========
                {

                    //======== symbolParts ========
                    {
                        symbolParts.setLayout(new MigLayout(
                                "fillx,insets 0,hidemode 3,gap 5 5",
                                // columns
                                "[fill]",
                                // rows
                                "[fill]" +
                                        "[fill]" +
                                        "[fill]" +
                                        "[fill]" +
                                        "[fill]" +
                                        "[fill]" +
                                        "[fill]" +
                                        "[fill]" +
                                        "[fill]" +
                                        "[fill]" +
                                        "[fill]"));

                        //---- contextComboBox ----
                        JComboBox<Item> contextComboBox = initCombobox("Context", StandardEntityOnes.class, milSym.getStandardEntityOne().getSidcPart());
                        contextComboBox.addItemListener(e -> {
                            if (e.getStateChange() == ItemEvent.SELECTED) {
                                milSym.setStandardEntityOne(StandardEntityOnes.getEnum(getIdFromComboBox(contextComboBox)));
                                updateSvgPanel();
                                formattedTextField1.setText(formatSIDC(milSym.toString()));
                            }
                        });
                        symbolParts.add(contextComboBox, "cell 0 0");

                        //---- symbolSetComboBox ----
                        JComboBox<Item> symbolSetComboBox = initCombobox("Symbol set", SymbolSets.class, milSym.getSymbolSet().getSidcPart());
                        updateEntities(getIdFromComboBox(symbolSetComboBox));
                        symbolSetComboBox.addItemListener(e -> {
                            if (e.getStateChange() == ItemEvent.SELECTED) {
                                updateEntities(getIdFromComboBox(symbolSetComboBox));
                                milSym.setSymbolSet(SymbolSets.getEnum(getIdFromComboBox(symbolSetComboBox)));
                                updateSvgPanel();
                                formattedTextField1.setText(formatSIDC(milSym.toString()));
                            }
                        });
                        symbolParts.add(symbolSetComboBox, "cell 0 1");

                        //---- HQTFComboBox ----
                        JComboBox<Item> HQTFComboBox = initCombobox("Headquarters/Task force/Dummy", HQTFDummy.class, milSym.getHqTFDummy().getSidcPart());
                        HQTFComboBox.setSelectedIndex(Integer.parseInt(milSym.getHqTFDummy().getSidcPart()));
                        HQTFComboBox.addItemListener(e -> {
                            if (e.getStateChange() == ItemEvent.SELECTED) {
                                milSym.setHqTFDummy(HQTFDummy.getEnum(getIdFromComboBox(HQTFComboBox)));
                                updateSvgPanel();
                                formattedTextField1.setText(formatSIDC(milSym.toString()));
                            }
                        });
                        symbolParts.add(HQTFComboBox, "cell 0 2");

                        //---- echelonComboBox ----
                        JComboBox<Item> echelonComboBox = initCombobox("Echelon/Mobility/Towed array", EchelonAmplifiers.class, milSym.getAmplifier().getSidcPart());
                        addValuesCombobox(echelonComboBox, EquipmentMobilityAmplifiers.class);
                        addValuesCombobox(echelonComboBox, NavalTowedArrayAmplifiers.class);
                        echelonComboBox.addItemListener(e -> {
                            if (e.getStateChange() == ItemEvent.SELECTED) {
                                updateAmplifier(echelonComboBox);
                                updateSvgPanel();
                                formattedTextField1.setText(formatSIDC(milSym.toString()));
                            }
                        });
                        symbolParts.add(echelonComboBox, "cell 0 3");

                        //---- entityComboBox ----
                        entityComboBox.setBorder(new TitledBorder("Entity"));
                        setComboBoxSelectedIndexFromId(entityComboBox, milSym.getEntity().getName());
                        updateEntityTypes(getIdFromComboBox(entityComboBox));
                        entityComboBox.addItemListener(e -> {
                            if (e.getStateChange() == ItemEvent.SELECTED) {
                                updateEntityTypes(getIdFromComboBox(entityComboBox));
                                milSym.setEntity(new Entity(getIdFromComboBox(entityComboBox), getIdFromComboBox(entityComboBox) + getIdFromComboBox(entityTypeComboBox)+ getIdFromComboBox(entitySubTypeComboBox)));
                                updateSvgPanel();
                                formattedTextField1.setText(formatSIDC(milSym.toString()));
                            }
                        });
                        symbolParts.add(entityComboBox, "cell 0 4");

                        //---- entityTypeComboBox ----
                        entityTypeComboBox.setBorder(new TitledBorder("Entity type"));
                        setComboBoxSelectedIndexFromId(entityTypeComboBox, milSym.getEntityType().getName());
                        updateEntitySubtypes(getIdFromComboBox(entityComboBox), getIdFromComboBox(entityTypeComboBox));
                        entityTypeComboBox.addItemListener(e -> {
                            if (e.getStateChange() == ItemEvent.SELECTED) {
                                updateEntitySubtypes(getIdFromComboBox(entityComboBox), getIdFromComboBox(entityTypeComboBox));
                                milSym.setEntityType(new EntityType(getIdFromComboBox(entityTypeComboBox), getIdFromComboBox(entityComboBox) + getIdFromComboBox(entityTypeComboBox)+ getIdFromComboBox(entitySubTypeComboBox)));
                                updateSvgPanel();
                                formattedTextField1.setText(formatSIDC(milSym.toString()));
                            }
                        });
                        symbolParts.add(entityTypeComboBox, "cell 0 5");

                        //---- entitySubTypeComboBox ----
                        entitySubTypeComboBox.setBorder(new TitledBorder("Entity subtype"));
                        setComboBoxSelectedIndexFromId(entitySubTypeComboBox, milSym.getEntitySubType().getName());
                        entitySubTypeComboBox.addItemListener(e -> {
                            if (e.getStateChange() == ItemEvent.SELECTED) {
                                milSym.setEntitySubType(new EntitySubType(getIdFromComboBox(entitySubTypeComboBox), getIdFromComboBox(entityComboBox) + getIdFromComboBox(entityTypeComboBox)+ getIdFromComboBox(entitySubTypeComboBox)));
                                updateSvgPanel();
                                formattedTextField1.setText(formatSIDC(milSym.toString()));
                            }
                        });
                        symbolParts.add(entitySubTypeComboBox, "cell 0 6");

                        //---- sectorOneModifierComboBox ----
                        sectorOneModifierComboBox.setBorder(new TitledBorder("Sector one modifier"));
                        setComboBoxSelectedIndexFromId(sectorOneModifierComboBox, milSym.getSectorOneModifier().getName());
                        sectorOneModifierComboBox.addItemListener(e -> {
                            if (e.getStateChange() == ItemEvent.SELECTED) {
                                milSym.setSectorOneModifier(new Modifier(getIdFromComboBox(sectorOneModifierComboBox), getIdFromComboBox(sectorOneModifierComboBox)));
                                updateSvgPanel();
                                formattedTextField1.setText(formatSIDC(milSym.toString()));
                            }

                        });
                        symbolParts.add(sectorOneModifierComboBox, "cell 0 7");

                        //---- comboBox14 ----
                        sectorTwoModifierComboBox.setBorder(new TitledBorder("Sector two modifier"));
                        setComboBoxSelectedIndexFromId(sectorTwoModifierComboBox, milSym.getSectorTwoModifier().getName());
                        sectorTwoModifierComboBox.addItemListener(e -> {
                            if (e.getStateChange() == ItemEvent.SELECTED) {
                                milSym.setSectorTwoModifier(new Modifier(getIdFromComboBox(sectorTwoModifierComboBox), getIdFromComboBox(sectorTwoModifierComboBox)));
                                updateSvgPanel();
                                formattedTextField1.setText(formatSIDC(milSym.toString()));
                            }
                        });
                        symbolParts.add(sectorTwoModifierComboBox, "cell 0 8");

                        //======== panel14 ========
                        {
                            statusPanel.setBorder(new TitledBorder("Status"));
                            statusPanel.setBackground(new Color(235, 234, 234));
                            statusPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

                            for (Status status : Status.values()) {
                                JButton button = new JButton();
                                button.setText(status.name());
                                button.addActionListener(e -> {
                                    milSym.setStatusAmplifier(Status.getEnum(status.getSidcPart()));
                                    milSym.setStatusAmplifierMode(StatusAmplifierModes.Alternate);
                                    updateSvgPanel();
                                    formattedTextField1.setText(formatSIDC(milSym.toString()));
                                });
                                statusPanel.add(button);
                            }
                        }
                        symbolParts.add(statusPanel, "cell 0 9");

                        //======== panel15 ========
                        {
                            panel15.setBorder(new TitledBorder("Reinforced or Reduced"));
                            panel15.setLayout(new FlowLayout(FlowLayout.LEFT));

                            for (Reinforcer reinforcer : Reinforcer.values()) {
                                JButton button = new JButton();
                                button.setText(reinforcer.name());
                                panel15.add(button);
                            }
                        }
                        symbolParts.add(panel15, "cell 0 10");
                    }
                    tabbedPane1.addTab("Symbol", symbolParts);
                }
                leftSideSelectorFrame.add(tabbedPane1, "cell 0 2");
            }
            symbolSelectorContentPane.add(leftSideSelectorFrame);

            //======== panel11 ========
            {
                rightSideSelectorFrame.setLayout(new MigLayout(
                        "insets 0,hidemode 3,gap 0 0",
                        // columns
                        "[grow,fill]",
                        // rows
                        "[grow,fill]" +
                                "[grow,fill]" +
                                "[grow,fill]"));
            }

            /*********************/


            updateSvgPanel();


            rightSideSelectorFrame.add(svgSymbolPanel, "cell 0 0");


            symbolSelectorContentPane.add(rightSideSelectorFrame);
            symbolSelectorFrame.pack();
            symbolSelectorFrame.setLocationRelativeTo(symbolSelectorFrame.getOwner());
            symbolSelectorFrame.setVisible(true);
        }
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    private void updateAmplifier(JComboBox<Item> echelonComboBox) {
        char[] c = ((Item) Objects.requireNonNull(echelonComboBox.getSelectedItem())).getId().toCharArray();
        switch (c[0]) {
            case '1', '2' -> milSym.setAmplifier(EchelonAmplifiers.getEnum(getIdFromComboBox(echelonComboBox)));
            case '3', '4', '5' -> milSym.setAmplifier(EquipmentMobilityAmplifiers.getEnum(getIdFromComboBox(echelonComboBox)));
            case '6' -> milSym.setAmplifier(NavalTowedArrayAmplifiers.getEnum(getIdFromComboBox(echelonComboBox)));
        }
    }


    public static byte[] convertSvgToPng(String svgData) throws IOException, TranscoderException {
        // Créer une instance de transcodeur d'image
        ImageTranscoder transcoder = new ImageTranscoder() {
            @Override
            public BufferedImage createImage(int width, int height) {
                return new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            }

            @Override
            public void writeImage(BufferedImage img, TranscoderOutput output) {
                // Convertir l'image en format PNG et la stocker dans le flux de sortie
                try {
                    ImageIO.write(img, "png", output.getOutputStream());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        // Configurer l'entrée du transcodeur avec la chaîne SVG
        TranscoderInput input = new TranscoderInput(new ByteArrayInputStream(svgData.getBytes()));

        // Configurer la sortie du transcodeur avec un flux de sortie ByteArrayOutputStream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TranscoderOutput output = new TranscoderOutput(outputStream);

        // Effectuer la conversion
        transcoder.transcode(input, output);

        // Récupérer les données de l'image PNG convertie
        return outputStream.toByteArray();
    }

    private void updateSvgPanel() {
        svgSymbolPanel.removeAll();

        SVGDocument d = SvgFactory.createSymbolSvg(milSym);
        String svgData = SvgFactory.getSvgRawFromDocument(d);

        try {
            byte[] pngData = convertSvgToPng(svgData);

            // Créer une ImageIcon à partir des données PNG
            ImageIcon icon = new ImageIcon(pngData);

            // Créer un JLabel pour afficher l'ImageIcon
            JLabel label = new JLabel(icon);

            // Ajouter le JLabel au JPanel
            svgSymbolPanel.add(label);

        } catch (IOException | TranscoderException e) {
            e.printStackTrace();
        }
        svgSymbolPanel.validate();
    }

    private String getIdFromComboBox(JComboBox<Item> comboBox) {
        String returned = "";
        try {
            returned = ((Item) Objects.requireNonNull(comboBox.getSelectedItem())).getId();
        } catch (Exception e) {
            return "00";
        }
        return returned;
    }

    /**
     * When the SymbolSet value is changed, the entity combobox values are initialized,
     * as are the sectorOneModifier and sectorTwoModifier comboboxes.
     * We keep the previous values in the comboboxes if they still exist in the new values,
     * otherwise we take the first version (or empty if no value).
     * @param symbolSetId symbolSetId
     */
    private void updateEntities(String symbolSetId) {
        // init entityModifierTree from selected SymbolSet
        SymbolSets symbolSets = SymbolSets.getEnum(symbolSetId);
        entityModifierTree = MilitarySymbolFactory.createSymbolSetEntityModifierTree(symbolSets);

        // current value and preparation of the default value
        String currentEntityId = getIdFromComboBox(entityComboBox);
        String currentSectorOneModifierId = getIdFromComboBox(sectorOneModifierComboBox);
        String currentSectorTwoModifierId = getIdFromComboBox(sectorTwoModifierComboBox);
        Entity futureEntity = null;
        Modifier futureModifierOne = new Modifier("00","00");
        Modifier futurModifierTwo = new Modifier("00","00");
        if(!entityModifierTree.getSectorModifierOnes().isEmpty())
            futureModifierOne = entityModifierTree.getSectorModifierOnes().iterator().next();
        if(!entityModifierTree.getSectorModifierTwos().isEmpty())
            futurModifierTwo = entityModifierTree.getSectorModifierTwos().iterator().next();
        if(entityModifierTree.getEntities().iterator().hasNext())
            futureEntity = entityModifierTree.getEntities().iterator().next();
        else // no entity for the selected symbolset
            return;

        // Clear comboboxes
        entityComboBox.removeAllItems();
        entityTypeComboBox.removeAllItems();
        entitySubTypeComboBox.removeAllItems();
        sectorOneModifierComboBox.removeAllItems();
        sectorTwoModifierComboBox.removeAllItems();


        for (Entity entity : entityModifierTree.getEntities()) {
            entityComboBox.addItem(new Item(entity.getIdentifier().substring(0, 2), entity.getName()));

            if (entity.getIdentifier().substring(0, 2).equals(currentEntityId))
                futureEntity = entity;
        }
        entityComboBox.getModel().setSelectedItem(new Item(futureEntity.getIdentifier().substring(0, 2), futureEntity.getName()));

        for (Modifier modifierOne : entityModifierTree.getSectorModifierOnes()) {
            sectorOneModifierComboBox.addItem(new Item(modifierOne.getIdentifier().substring(0, 2), modifierOne.getName()));

            if (modifierOne.getIdentifier().substring(0, 2).equals(currentSectorOneModifierId))
                futureModifierOne = modifierOne;
        }
        sectorOneModifierComboBox.getModel().setSelectedItem(new Item(futureModifierOne.getIdentifier().substring(0, 2), futureModifierOne.getName()));


        for (Modifier modifierTwo : entityModifierTree.getSectorModifierTwos()) {
            sectorTwoModifierComboBox.addItem(new Item(modifierTwo.getIdentifier().substring(0, 2), modifierTwo.getName()));

            if (modifierTwo.getIdentifier().substring(0, 2).equals(currentSectorTwoModifierId))
                futurModifierTwo = modifierTwo;
        }
        sectorTwoModifierComboBox.getModel().setSelectedItem(new Item(futurModifierTwo.getIdentifier().substring(0, 2), futurModifierTwo.getName()));
    }

    private void updateEntityTypes(String entityId) {
        String currentEntityTypeId = getIdFromComboBox(entityTypeComboBox);

        EntityType futureEntityType = null;
        if (!entityModifierTree.getEntityTypesFromEntityId(entityId).isEmpty()) {
            futureEntityType = entityModifierTree.getEntityTypesFromEntityId(entityId).get(0);
        }
        // No type for the selected entity
        else
            return;

        entityTypeComboBox.removeAllItems();
        for (EntityType entityType : entityModifierTree.getEntityTypesFromEntityId(entityId)) {
            entityTypeComboBox.addItem(new Item(entityType.getIdentifier().substring(2, 4), entityType.getName()));

            if (entityType.getIdentifier().substring(0, 2).equals(currentEntityTypeId))
                futureEntityType = entityType;
        }
        entityTypeComboBox.getModel().setSelectedItem(new Item(futureEntityType.getIdentifier().substring(0, 2), futureEntityType.getName()));
    }

    private void updateEntitySubtypes(String entityId, String entityTypeId) {
        String currentEntitySubTypeId = getIdFromComboBox(entitySubTypeComboBox);

        EntitySubType futureEntitySubType;
        if (!entityModifierTree.getEntitySubTypesFromEntityAndEntityTypeId(entityId, entityTypeId).isEmpty()) {
            futureEntitySubType = entityModifierTree.getEntitySubTypesFromEntityAndEntityTypeId(entityId, entityTypeId).get(0);
        }
        // No subtype for the selected entity
        else
            return;

        entitySubTypeComboBox.removeAllItems();
        for (EntitySubType entitySubType : entityModifierTree.getEntitySubTypesFromEntityAndEntityTypeId(entityId, entityTypeId)) {
            entitySubTypeComboBox.addItem(new Item(entitySubType.getIdentifier().substring(4, 6), entitySubType.getName()));

            if (entitySubType.getIdentifier().substring(0, 2).equals(currentEntitySubTypeId)) {
                futureEntitySubType = entitySubType;
            }
        }
        entitySubTypeComboBox.getModel().setSelectedItem(new Item(futureEntitySubType.getIdentifier().substring(0, 2), futureEntitySubType.getName()));
    }


    private static <T extends Enum<T> & Amplifier> JComboBox<Item> initCombobox(String title, Class<T> enumClass, String defaultPartId) {
        JComboBox<Item> comboBox = new JComboBox<>();
        comboBox.setBorder(new TitledBorder(title));

        T enumFirstValue = Arrays.stream(enumClass.getEnumConstants()).iterator().next();
        Item defaultItem = new Item(enumFirstValue.getSidcPart(), enumFirstValue.toString());

        // Init with enum values
        for (T enumConstant : enumClass.getEnumConstants()) {
            comboBox.addItem(new Item(enumConstant.getSidcPart(), enumConstant.toString()));

            if (enumConstant.getSidcPart().equals(defaultPartId))
                defaultItem = new Item(enumConstant.getSidcPart(), enumConstant.toString());
        }

        // Set default value
        comboBox.getModel().setSelectedItem(defaultItem);

        return comboBox;
    }

    private void setComboBoxSelectedIndexFromId(JComboBox<Item> comboBox, String selectedId) {
        for(int i = 0 ; i < comboBox.getItemCount(); i++) {
            if(selectedId.equals(comboBox.getItemAt(i).getId()))
                comboBox.setSelectedIndex(i);
        }
    }

    private static <T extends Enum<T> & Amplifier> void addValuesCombobox(JComboBox<Item> comboBox, Class<T> enumClass) {
        for (T enumConstant : enumClass.getEnumConstants()) {
            comboBox.addItem(new Item(enumConstant.getSidcPart(), enumConstant.toString()));
        }
    }


    private static String formatSIDC(String sidc) {
        // Vérifier si la longueur de la chaîne SIDC est correcte
        if (sidc.length() != 20) {
            throw new IllegalArgumentException("La longueur de la chaîne SIDC doit être de 20 caractères.");
        }

        // Formater la chaîne SIDC en ajoutant des tirets aux positions spécifiées
        StringBuilder formattedSidc = new StringBuilder();
        int[] dashPositions = {2, 3, 4, 6, 7, 8, 10, 16, 18}; // Positions des tirets
        for (int i = 0; i < sidc.length(); i++) {
            formattedSidc.append(sidc.charAt(i));
            // Ajouter un tiret si la position actuelle est dans le tableau des positions des tirets
            if (contains(dashPositions, i + 1)) {
                formattedSidc.append("-");
            }
        }
        System.out.println(formattedSidc.toString());

        return formattedSidc.toString();
    }

    private static boolean contains(int[] array, int value) {
        for (int num : array) {
            if (num == value) {
                return true;
            }
        }
        return false;
    }
}