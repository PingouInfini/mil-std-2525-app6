package search;

import javax.swing.*;
import java.awt.*;

public class SearchTextField extends JTextField {
    private Icon prefixIcon;

    public SearchTextField() {
        setBorder(javax.swing.BorderFactory.createEmptyBorder(7, 5, 7, 5));
    }

    public void setPrefixIcon(Icon prefixIcon) {
        this.prefixIcon = prefixIcon;
        initBorder();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        paintIcon(g);
        //  paint border
        if (isFocusOwner()) {
            g.setColor(new Color(6, 135, 196));
        } else {
            g.setColor(new Color(76, 181, 195));
        }
        g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
        g.drawRect(1, 1, getWidth() - 3, getHeight() - 3);
    }

    private void paintIcon(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        if (prefixIcon != null) {
            Image prefix = ((ImageIcon) prefixIcon).getImage();
            int y = (getHeight() - prefixIcon.getIconHeight()) / 2;
            g2.drawImage(prefix, 3, y, this);
        }
    }

    private void initBorder() {
        int left = 5;
        int right = 5;
        //  5 is default
        if (prefixIcon != null) {
            //  prefix is left
            left = prefixIcon.getIconWidth() + 5;
        }
        setBorder(javax.swing.BorderFactory.createEmptyBorder(7, left, 7, right));
    }
}
