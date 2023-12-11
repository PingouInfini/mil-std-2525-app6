import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class ToggleSwitch extends JPanel
{

    private static final long serialVersionUID = -4543250287835773782L;
    private boolean activated = false;
    private Color switchColor = new Color(200, 200, 200);
    private Color buttonColor = new Color(255, 255, 255);
    private Color borderColor = new Color(50, 50, 50);
    private Color activeSwitch = new Color(0, 125, 255);
    private transient BufferedImage bufferedImage;
    private int borderRadius = 10;
    private transient Graphics2D g;

    public ToggleSwitch()
    {
        super();
        setVisible(true);

        addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseReleased(MouseEvent arg0)
            {
                final boolean oldValue = ToggleSwitch.this.activated;
                ToggleSwitch.this.activated = !ToggleSwitch.this.activated;
                firePropertyChange("activated", oldValue, ToggleSwitch.this.activated);
                repaint();
            }
        });

        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setBounds(0, 0, 41, 21);
    }

    @Override
    public void paint(Graphics gr)
    {
        if (this.g == null || this.bufferedImage.getWidth() != getWidth() || this.bufferedImage.getHeight() != getHeight())
        {
            this.bufferedImage = (BufferedImage) createImage(getWidth(), getHeight());
            this.g = (Graphics2D) this.bufferedImage.getGraphics();
            final RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            this.g.setRenderingHints(rh);
        }

        this.g.setColor(this.activated ? this.activeSwitch : this.switchColor);
        this.g.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 5, this.borderRadius);
        this.g.setColor(this.borderColor);
        this.g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 5, this.borderRadius);
        this.g.setColor(this.buttonColor);

        if (this.activated)
        {
            this.g.fillRoundRect(getWidth() / 2, 1, (getWidth() - 1) / 2 - 2, (getHeight() - 1) - 2, this.borderRadius, this.borderRadius);
            this.g.setColor(this.borderColor);
            this.g.drawRoundRect((getWidth() - 1) / 2, 0, (getWidth() - 1) / 2, (getHeight() - 1), this.borderRadius, this.borderRadius);
        }
        else
        {
            this.g.fillRoundRect(1, 1, (getWidth() - 1) / 2 - 2, (getHeight() - 1) - 2, this.borderRadius, this.borderRadius);
            this.g.setColor(this.borderColor);
            this.g.drawRoundRect(0, 0, (getWidth() - 1) / 2, (getHeight() - 1), this.borderRadius, this.borderRadius);
        }

        gr.drawImage(this.bufferedImage, 0, 0, null);
    }

    public boolean isActivated()
    {
        return this.activated;
    }

    public void setActivated(boolean activated)
    {
        this.activated = activated;
    }

    public Color getSwitchColor()
    {
        return this.switchColor;
    }

    /**
     * Unactivated Background Color of switch
     */
    public void setSwitchColor(Color switchColor)
    {
        this.switchColor = switchColor;
    }

    public Color getButtonColor()
    {
        return this.buttonColor;
    }

    /**
     * Switch-Button color
     */
    public void setButtonColor(Color buttonColor)
    {
        this.buttonColor = buttonColor;
    }

    public Color getBorderColor()
    {
        return this.borderColor;
    }

    /**
     * Border-color of whole switch and switch-button
     */
    public void setBorderColor(Color borderColor)
    {
        this.borderColor = borderColor;
    }

    public Color getActiveSwitch()
    {
        return this.activeSwitch;
    }

    public void setActiveSwitch(Color activeSwitch)
    {
        this.activeSwitch = activeSwitch;
    }

    /**
     * @return the borderRadius
     */
    public int getBorderRadius()
    {
        return this.borderRadius;
    }

    /**
     * @param borderRadius the borderRadius to set
     */
    public void setBorderRadius(int borderRadius)
    {
        this.borderRadius = borderRadius;
    }
}