package fr.pingouinfini;

import fr.pingouinfini.symbology.*;
import fr.pingouinfini.symbology.StandardEntityOnes;
import fr.pingouinfini.symbology.StandardEntityTwos;
import fr.pingouinfini.symbology.StatusAmplifierModes;
import net.miginfocom.swing.MigLayout;
import org.apache.batik.swing.JSVGCanvas;
import org.w3c.dom.svg.SVGDocument;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Hello world!
 */
public class App {




    public static void main(String[] args) {
        SymbolSelectorFrame symbolSelectorFrame = new SymbolSelectorFrame();

//        // Tested against symbols generated from https://sidc.milsymb.net/#/APP6
//        MilitarySymbolFactory.setStatusAmplifierMode(StatusAmplifierModes.Alternate);
//        //MilitarySymbol milSym = MilitarySymbolFactory.createSymbol("10-0-0-20-2-4-61-120501-03-01");
//        //MilitarySymbol milSym = MilitarySymbolFactory.createSymbol("10-0-3-10-0-0-16-120300-00-00");
////        MilitarySymbol milSym = MilitarySymbolFactory.createSymbol("10-2-3-36-0-0-00-110100-00-00");
//
//        MilitarySymbol milSym = new MilitarySymbol();
//        milSym.setContext(StandardEntityOnes.Simulation);
//        milSym.setStandardEntity(StandardEntityTwos.Friend);
//        milSym.setSymbolSet(SymbolSets.Space);
//        milSym.setStatusAmplifier(Status.Present);
//        milSym.setStatusAmplifierMode(StatusAmplifierModes.Alternate);
//        milSym.setHqTFDummy(HQTFDummy.NotApplicable);
//        milSym.setAmplifier(EchelonAmplifiers.Battalion);
//        milSym.setEntity(new Entity("13", "130000"));
//        milSym.setEntityType(new EntityType("00", "130000"));
//        milSym.setEntitySubType(new EntitySubType("00", "110300"));
//        milSym.setSectorOneModifier(new Modifier("06", "06"));
//        milSym.setSectorTwoModifier(new Modifier("02", "02"));
//
//        //SymbolSetEntityModifierTree h = MilitarySymbolFactory.createSymbolSetEntityModifierTree(SymbolSets.LandEquipment);
//        //System.out.println(h);
//
//        System.out.println("SIDC: " + milSym);
//        showSymbol(milSym);
    }

    public static void showSymbol(MilitarySymbol milSym) {
        JFrame f = new JFrame("SIDC: " + milSym);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        JSVGCanvas canvas = new JSVGCanvas();
        SVGDocument d = SvgFactory.createSymbolSvg(milSym);

        System.out.println("SVG : \n " + SvgFactory.getSvgRawFromDocument(d)); //TODO : Only for DEBUG, remove me

        canvas.setSVGDocument(d);
        canvas.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1.0;
        c.gridwidth = 3;
        c.anchor = GridBagConstraints.PAGE_START;
        c.fill = GridBagConstraints.BOTH;
        panel.add(canvas, c);
        panel.setBackground(Color.DARK_GRAY);

        JTextField sidcTextField = new JTextField();
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        c.weightx = 0.6;
        c.anchor = GridBagConstraints.PAGE_END;
        panel.add(sidcTextField, c);

        JButton showSymbolButton = new JButton("Show symbol");
        c.gridx = 2;
        c.gridy = 1;
        c.gridwidth = GridBagConstraints.REMAINDER;

        c.weightx = 0.4;
        panel.add(showSymbolButton, c);

        f.setSize(612, 792);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.getContentPane().add(canvas);
        f.setVisible(true);
    }
}
