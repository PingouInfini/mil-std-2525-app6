package search;

import event.EventManager;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class SearchComponentPanel extends javax.swing.JPanel {
    private JPopupMenu menu;
    private SearchPanel search;
    private SearchTextField txtSearch;

    private final Map<String, String> searchDataset;
    private Set<String> previousSearch = new TreeSet<>();
    private final String iconPath;

    public SearchComponentPanel(Map<String, String> mapDescriptionHierarchy, Set<String> historiqueRecherche, String iconPath) {
        this.searchDataset = mapDescriptionHierarchy;
        this.iconPath = iconPath;

        if (historiqueRecherche != null)
            this.previousSearch = historiqueRecherche;
        else
            this.previousSearch = new TreeSet<>();
    }

    public JPanel createSearchPanel() {
        JPanel searchPanel = initComponents();
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
                previousSearch.add(data.getText());

                EventManager.getInstance().fireEvent(data.getHierarchy());

                //System.out.println("Click Item : " + data.getText()+ " -> "+data.getHierarchy());
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
        return searchPanel;
    }

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

        JPanel searchPanel = new JPanel();

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(searchPanel);
        searchPanel.setLayout(layout);
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
        return searchPanel;
    }

    private void txtSearchMouseClicked(java.awt.event.MouseEvent evt) {
        if (search.getItemSize() > 0) {
            menu.show(txtSearch, 0, txtSearch.getHeight());
        }
    }

    private void txtSearchKeyReleased(java.awt.event.KeyEvent evt) {
        String text = txtSearch.getText().trim().toLowerCase();
        search.setData(search(text));
        if (search.getItemSize() > 0) {
            //  * 2 top and bot border
            menu.show(txtSearch, 0, txtSearch.getHeight());
            menu.setPopupSize(menu.getWidth(), (search.getItemSize() * 35) + 2);
        } else {
            menu.setVisible(false);
        }
    }

    private List<DataSearch> search(String search) {
        int limitData = 7;
        List<DataSearch> list = new ArrayList<>();

        for (Map.Entry<String, String> entry : searchDataset.entrySet()) {
            String d = entry.getKey();
            if (d.toLowerCase().contains(search)) {
                boolean inHistory = isInHistory(d);
                if (inHistory) {
                    list.add(0, new DataSearch(d, entry.getValue(), inHistory));
                } else {
                    list.add(new DataSearch(d, entry.getValue(), inHistory));
                }
            }
        }
        return list.subList(0, Math.min(list.size(), limitData));
    }

    private void removeHistory(String text) {
        previousSearch.remove(text);
    }

    private boolean isInHistory(String text) {
        return previousSearch.contains(text);
    }
}