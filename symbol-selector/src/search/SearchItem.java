/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package search;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;

public class SearchItem extends javax.swing.JPanel {
    private String iconPath;

    public SearchItem(DataSearch data, String iconPath) {
        this.iconPath = iconPath;
        initComponents();
        setData(data);
    }

    private void setData(DataSearch data) {
        addEventMouse(this);
        addEventMouse(lbText);
        addEventMouse(lbRemove);
        lbText.setText(data.getText());
        lbText.setFont(new Font("Arial Black", Font.BOLD, 8));
        if (data.isInHistory()) {
            lbText.setForeground(new Color(29, 106, 205));
            lbIcon.setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(getClass().getResource(this.iconPath + "/time.png"))));
            lbRemove.setCursor(new Cursor(Cursor.HAND_CURSOR));
        } else {
            lbRemove.setText("");
        }
    }

    private void addEventMouse(Component com) {
        com.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent me) {
                setBackground(new Color(215, 216, 216));
            }

            @Override
            public void mouseExited(MouseEvent me) {
                setBackground(Color.WHITE);
            }

        });
    }

    private ActionListener eventClick;
    private ActionListener eventRemove;

    public void addEvent(ActionListener eventClick, ActionListener eventRemove) {
        this.eventClick = eventClick;
        this.eventRemove = eventRemove;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lbIcon = new javax.swing.JLabel();
        lbText = new javax.swing.JLabel();
        lbRemove = new javax.swing.JLabel();

        setBackground(new Color(255, 255, 255));

        lbIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbIcon.setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(getClass().getResource(this.iconPath + "/search_small.png")))); // NOI18N

        lbText.setFont(new Font("Arial Black", Font.BOLD, 8)); // NOI18N
        lbText.setForeground(new Color(38, 38, 38));
        lbText.setText("Text ...");
        lbText.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                lbTextMouseClicked(evt);
            }
        });

        lbRemove.setForeground(new Color(147, 147, 147));
        lbRemove.setHorizontalAlignment(SwingConstants.CENTER);
        lbRemove.setText("X");
        lbRemove.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                lbRemoveMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(lbIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbText, javax.swing.GroupLayout.DEFAULT_SIZE, 402, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbRemove, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(lbIcon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lbText, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                        .addComponent(lbRemove, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void lbRemoveMouseClicked(MouseEvent evt) {//GEN-FIRST:event_lbRemoveMouseClicked
        if (!lbRemove.getText().trim().equals("")) {
            eventRemove.actionPerformed(null);
        }
    }//GEN-LAST:event_lbRemoveMouseClicked

    private void lbTextMouseClicked(MouseEvent evt) {//GEN-FIRST:event_lbTextMouseClicked
        eventClick.actionPerformed(null);
    }//GEN-LAST:event_lbTextMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lbIcon;
    private javax.swing.JLabel lbRemove;
    private javax.swing.JLabel lbText;
    // End of variables declaration//GEN-END:variables
}
