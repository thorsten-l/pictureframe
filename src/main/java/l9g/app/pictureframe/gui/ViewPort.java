package l9g.app.pictureframe.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import l9g.app.pictureframe.App;
import l9g.app.pictureframe.config.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Dr. Thorsten Ludewig <t.ludewig@gmail.com>
 */
public class ViewPort extends javax.swing.JPanel implements Runnable
{
  final static Logger LOGGER = LoggerFactory.getLogger(ViewPort.class.
    getName());

  private final Thread painter;

  /**
   * Creates new form ViewPort
   */
  public ViewPort()
  {
    initComponents();
    painter = new Thread(this, "Painter");
    painter.setDaemon(true);
    counter = App.getConfig().getInterval();
  }

  public void start()
  {
    painter.start();
  }

  /**
   * This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents()
  {

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 400, Short.MAX_VALUE)
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 300, Short.MAX_VALUE)
    );
  }// </editor-fold>//GEN-END:initComponents

  @Override
  protected void paintComponent(Graphics g)
  {
    Configuration config = App.getConfig();
    BufferedImage image = ImageFactory.getImage();
    int vh = getSize().height;
    int vw = getSize().width;

    ((Graphics2D) g).setRenderingHint(
      RenderingHints.KEY_ANTIALIASING,
      RenderingHints.VALUE_ANTIALIAS_ON);

    if (image != null && image.getHeight() > 0)
    {
      g.drawImage(image, 0, 0, Color.black, this);
    }
    else
    {
      LOGGER.info("No picture found w={}, h={}", vw, vh);
      g.setColor(Color.black);
      g.fillRect(0, 0, vw, vh);
      g.setColor(Color.red);
      g.drawString("No picture found.", 100, 100);
    }

    if (config.isCountdownShow())
    {
      Graphics2D g2d = (Graphics2D) g;

      g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);
      g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,
        RenderingHints.VALUE_COLOR_RENDER_QUALITY);
      g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
        RenderingHints.VALUE_INTERPOLATION_BICUBIC);
      g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
        RenderingHints.VALUE_RENDER_QUALITY);

      int arc = (int) (360.0 * ((float) counter
        / (float) App.getConfig().getInterval()));
      g.setColor(new Color(0x80, 0x80, 0x80));
      g.fillArc(config.getCountdownX(), config.getCountdownY(), config.
        getCountdownRadius(), config.getCountdownRadius(), 90, arc);
      g.setColor(new Color(0xd0, 0xd0, 0xd0));
      g.drawArc(config.getCountdownX(), config.getCountdownY(), config.
        getCountdownRadius(), config.getCountdownRadius(), 90, arc);
      g.setColor(new Color(0x40, 0x40, 0x40));
      g.drawArc(config.getCountdownX() - 1, config.getCountdownY() - 1, config.
        getCountdownRadius() + 2, config.getCountdownRadius() + 2, 90, arc);
    }
  }

  @Override
  public void run()
  {
    while (true)
    {
      Configuration config = App.getConfig();
      counter--;

      if (counter <= 0)
      {
        counter = config.getInterval();
        ImageFactory.update();
      }

      repaint();

      try
      {
        Thread.sleep(config.getTickDuration());
      }
      catch (InterruptedException ex)
      {
        LOGGER.error("Interrupted painter");
      }
    }
  }

  public void nextPicture()
  {
    counter = 0;
  }

  private long counter = 0;

  // Variables declaration - do not modify//GEN-BEGIN:variables
  // End of variables declaration//GEN-END:variables
}
