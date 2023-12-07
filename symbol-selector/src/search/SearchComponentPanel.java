package search;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SearchComponentPanel extends javax.swing.JPanel {
    private JPopupMenu menu;
    private SearchPanel search;
    private String iconPath;

    public JPanel createSearchPanel(String iconPath) {
        this.iconPath = iconPath;

        JPanel jp = initComponents();
        menu = new JPopupMenu();
        search = new SearchPanel(iconPath);
        menu.setBorder(BorderFactory.createLineBorder(new Color(164, 164, 164)));
        menu.add(search);
        menu.setFocusable(false);
        search.addEventClick(new EventClick() {
            @Override
            public void itemClick(DataSearch data) {
                menu.setVisible(false);
                txtSearch.setText(data.getText());

                System.out.println("Click Item : " + data.getText());
            }

            @Override
            public void itemRemove(Component com, DataSearch data) {
                search.remove(com);
                removeHistory(data.getText());
                menu.setPopupSize(menu.getWidth(), (search.getItemSize() * 35) + 2);
                if (search.getItemSize() == 0) {
                    menu.setVisible(false);
                }
                System.out.println("Remove Item : " + data.getText());
            }
        });
        return jp;
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private JPanel initComponents() {

        txtSearch = new SearchTextField();

        //setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        txtSearch.setPrefixIcon(new javax.swing.ImageIcon(Objects.requireNonNull(getClass().getResource(this.iconPath + "/search.png")))); // NOI18N
        txtSearch.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtSearchMouseClicked(evt);
            }
        });
        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchKeyReleased(evt);
            }
        });

        JPanel jp = new JPanel();

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(jp);
        jp.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 345, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(388, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(439, Short.MAX_VALUE))
        );
        return jp;
    }// </editor-fold>//GEN-END:initComponents

    private void txtSearchMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtSearchMouseClicked
        if (search.getItemSize() > 0) {
            menu.show(txtSearch, 0, txtSearch.getHeight());
        }
    }//GEN-LAST:event_txtSearchMouseClicked

    private void txtSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchKeyReleased
        String text = txtSearch.getText().trim().toLowerCase();
        search.setData(search(text));
        if (search.getItemSize() > 0) {
            //  * 2 top and bot border
            menu.show(txtSearch, 0, txtSearch.getHeight());
            menu.setPopupSize(menu.getWidth(), (search.getItemSize() * 35) + 2);
        } else {
            menu.setVisible(false);
        }
    }//GEN-LAST:event_txtSearchKeyReleased

    private List<DataSearch> search(String search) {
        int limitData = 7;
        List<DataSearch> list = new ArrayList<>();
        String dataTesting[] = {"300 - Rise of an Empire",
                "Cosmic Sin",
                "Deadlock",
                "Deliver Us from Eva",
                "Empire of the Ants",
                "Empire of the Sun",
                "Empire Records",
                "Empire State",
                "Four Good Days",
                "Frozen Fever",
                "Frozen",
                "The Courier",
                "The First Purge",
                "To Olivia",
                "Underworld"};
        for (String d : dataTesting) {
            if (d.toLowerCase().contains(search)) {
                boolean story = isStory(d);
                if (story) {
                    list.add(0, new DataSearch(d, story));
                    //  add or insert to first record
                } else {
                    list.add(new DataSearch(d, story));
                    //  add to last record
                }
                if (list.size() == limitData) {
                    break;
                }
            }
        }
        return list;
    }

    String dataStory[] = {"300 - Rise of an Empire",
            "Empire Records",
            "Empire State",
            "Frozen",
            "The Courier"};

    private void removeHistory(String text) {
        for (int i = 0; i < dataStory.length; i++) {
            String d = dataStory[i];
            if (d.toLowerCase().equals(text.toLowerCase())) {
                dataStory[i] = "";
            }
        }
    }

    private boolean isStory(String text) {
        for (String d : dataStory) {
            if (d.toLowerCase().equals(text.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private SearchTextField txtSearch;
    // End of variables declaration//GEN-END:variables
}