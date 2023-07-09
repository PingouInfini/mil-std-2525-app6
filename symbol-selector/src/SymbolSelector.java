import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
/*
 * Created by JFormDesigner on Sun Jul 09 12:31:09 CEST 2023
 */


/**
 * @author unknown
 */
public class SymbolSelector extends JWindow {

    int COMPONENT_HEIGHT = 100;

//    dialogPane.setBorder(new javax.swing.border.CompoundBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EmptyBorder(
//            0, 0, 0, 0), "Symbole APP6", javax.swing.border.TitledBorder.CENTER, TitledBorder
//            .TOP, new java.awt.Font("Dia\u006cog", java.awt.Font.BOLD, 12), Color.
//            blue), dialogPane.getBorder()));
//            dialogPane.addPropertyChangeListener(e -> {
//        if ("border".equals(e.getPropertyName())) throw new RuntimeException();
//    });

    public SymbolSelector() {
        initComponents();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - unknown
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        titleField = new JTextField();
        behaviourPanel = new JPanel();
        button1 = new JButton();
        button2 = new JButton();
        button3 = new JButton();
        button4 = new JButton();
        backPanel = new JPanel();
        button5 = new JButton();
        symbolPanel = new JPanel();
        button6 = new JButton();
        button7 = new JButton();
        button8 = new JButton();
        button9 = new JButton();
        button10 = new JButton();
        nextPanel = new JPanel();
        button11 = new JButton();
        button12 = new JButton();
        buttonBar = new JPanel();
        okButton = new JButton();
        cancelButton = new JButton();

        //======== this ========
        setAlwaysOnTop(true);
        var contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setBorder ( new javax . swing. border .CompoundBorder ( new javax . swing. border .TitledBorder ( new javax
            . swing. border .EmptyBorder ( 0, 0 ,0 , 0) ,  "JF\u006frmDes\u0069gner \u0045valua\u0074ion" , javax. swing
            .border . TitledBorder. CENTER ,javax . swing. border .TitledBorder . BOTTOM, new java. awt .
            Font ( "D\u0069alog", java .awt . Font. BOLD ,12 ) ,java . awt. Color .red
            ) ,dialogPane. getBorder () ) ); dialogPane. addPropertyChangeListener( new java. beans .PropertyChangeListener ( ){ @Override
            public void propertyChange (java . beans. PropertyChangeEvent e) { if( "\u0062order" .equals ( e. getPropertyName (
            ) ) )throw new RuntimeException( ) ;} } );
            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {
                contentPanel.setLayout(new MigLayout(
                    "fill,insets dialog,hidemode 3,align center center",
                    // columns
                    "[fill]" +
                    "[fill]" +
                    "[fill]" +
                    "[fill]",
                    // rows
                    "[]" +
                    "[]" +
                    "[]"));

                //---- titleField ----
                titleField.setText("Selection du symbole");
                titleField.setHorizontalAlignment(SwingConstants.LEFT);
                titleField.setEditable(false);
                titleField.setBorder(null);
                contentPanel.add(titleField, "cell 0 0");

                //======== behaviourPanel ========
                {
                    behaviourPanel.setMinimumSize(new Dimension(40, 195));
                    behaviourPanel.setMaximumSize(new Dimension(40, 2147483647));
                    behaviourPanel.setLayout(new MigLayout(
                        "fill,hidemode 3,align center center",
                        // columns
                        "[fill]",
                        // rows
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]"));

                    //---- button1 ----
                    button1.setIcon(new ImageIcon(getClass().getResource("/images/1.u.3.1.png")));
                    button1.setMaximumSize(new Dimension(40, 40));
                    button1.setMinimumSize(new Dimension(40, 40));
                    behaviourPanel.add(button1, "cell 0 0");

                    //---- button2 ----
                    button2.setMaximumSize(new Dimension(40, 40));
                    button2.setMinimumSize(new Dimension(40, 40));
                    button2.setIcon(new ImageIcon(getClass().getResource("/images/1.f.3.1.png")));
                    behaviourPanel.add(button2, "cell 0 1");

                    //---- button3 ----
                    button3.setMinimumSize(new Dimension(40, 40));
                    button3.setMaximumSize(new Dimension(40, 40));
                    button3.setIcon(new ImageIcon(getClass().getResource("/images/1.n.3.1.png")));
                    behaviourPanel.add(button3, "cell 0 2");

                    //---- button4 ----
                    button4.setIcon(new ImageIcon(getClass().getResource("/images/1.h.3.1.png")));
                    button4.setMaximumSize(new Dimension(40, 40));
                    button4.setMinimumSize(new Dimension(40, 40));
                    behaviourPanel.add(button4, "cell 0 3");
                }
                contentPanel.add(behaviourPanel, "cell 0 2");

                //======== backPanel ========
                {
                    backPanel.setLayout(new MigLayout(
                        "fill,hidemode 3,align center center",
                        // columns
                        "[fill]",
                        // rows
                        "[]"));

                    //---- button5 ----
                    button5.setText("<");
                    button5.setMaximumSize(new Dimension(78, 100));
                    button5.setMinimumSize(new Dimension(78, 100));
                    backPanel.add(button5, "cell 0 0");
                }
                contentPanel.add(backPanel, "cell 1 2");

                //======== symbolPanel ========
                {
                    symbolPanel.setLayout(new MigLayout(
                        "filly,hidemode 3,align center center",
                        // columns
                        "[fill]",
                        // rows
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]"));

                    //---- button6 ----
                    button6.setText("text");
                    symbolPanel.add(button6, "cell 0 0");

                    //---- button7 ----
                    button7.setText("text");
                    symbolPanel.add(button7, "cell 0 1");

                    //---- button8 ----
                    button8.setText("text");
                    symbolPanel.add(button8, "cell 0 2");

                    //---- button9 ----
                    button9.setText("text");
                    symbolPanel.add(button9, "cell 0 3");

                    //---- button10 ----
                    button10.setText("text");
                    symbolPanel.add(button10, "cell 0 4");
                }
                contentPanel.add(symbolPanel, "cell 2 2");

                //======== nextPanel ========
                {
                    nextPanel.setLayout(new MigLayout(
                        "filly,hidemode 3,align center center",
                        // columns
                        "[fill]",
                        // rows
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]"));

                    //---- button11 ----
                    button11.setText("text");
                    nextPanel.add(button11, "cell 0 0");

                    //---- button12 ----
                    button12.setText("text");
                    nextPanel.add(button12, "cell 0 3");
                }
                contentPanel.add(nextPanel, "cell 3 2");
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);

            //======== buttonBar ========
            {
                buttonBar.setLayout(new MigLayout(
                    "insets dialog,alignx right",
                    // columns
                    "[button,fill]" +
                    "[button,fill]",
                    // rows
                    null));

                //---- okButton ----
                okButton.setText("OK");
                buttonBar.add(okButton, "cell 0 0");

                //---- cancelButton ----
                cancelButton.setText("Cancel");
                buttonBar.add(cancelButton, "cell 1 0");
            }
            dialogPane.add(buttonBar, BorderLayout.SOUTH);
        }
        contentPane.add(dialogPane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - unknown
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JTextField titleField;
    private JPanel behaviourPanel;
    private JButton button1;
    private JButton button2;
    private JButton button3;
    private JButton button4;
    private JPanel backPanel;
    private JButton button5;
    private JPanel symbolPanel;
    private JButton button6;
    private JButton button7;
    private JButton button8;
    private JButton button9;
    private JButton button10;
    private JPanel nextPanel;
    private JButton button11;
    private JButton button12;
    private JPanel buttonBar;
    private JButton okButton;
    private JButton cancelButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    public static void main(String args[]) {
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SymbolSelector().setVisible(true);
            }
        });
    }
}
