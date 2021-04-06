package l9g.app.pictureframe.gui;

import java.awt.event.KeyEvent;
import l9g.app.pictureframe.App;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Dr. Thorsten Ludewig <t.ludewig@gmail.com>
 */
public class BaseFrame extends javax.swing.JFrame
{
  final static Logger LOGGER = LoggerFactory.getLogger(BaseFrame.class.
    getName());

  /**
   * Creates new form BaseFrame
   */
  public BaseFrame()
  {
    initComponents();
    initialize();
  }

  public final void initialize()
  {
    viewPort = new ViewPort();
    viewPort.setBackground(new java.awt.Color(117, 192, 255));
    viewPort.setVerifyInputWhenFocusTarget(false);
    getContentPane().add(viewPort, java.awt.BorderLayout.CENTER);
    pack();
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

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    addMouseListener(new java.awt.event.MouseAdapter()
    {
      public void mouseClicked(java.awt.event.MouseEvent evt)
      {
        formMouseClicked(evt);
      }
    });
    addWindowListener(new java.awt.event.WindowAdapter()
    {
      public void windowOpened(java.awt.event.WindowEvent evt)
      {
        formWindowOpened(evt);
      }
    });
    addKeyListener(new java.awt.event.KeyAdapter()
    {
      public void keyPressed(java.awt.event.KeyEvent evt)
      {
        formKeyPressed(evt);
      }
    });

    pack();
  }// </editor-fold>//GEN-END:initComponents


  private void formMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_formMouseClicked
  {//GEN-HEADEREND:event_formMouseClicked
    LOGGER.info("System exit by mouse click x={} y={}", evt.getX(), evt.getY());
    System.exit(0);
  }//GEN-LAST:event_formMouseClicked

  private void formWindowOpened(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowOpened
  {//GEN-HEADEREND:event_formWindowOpened
    LOGGER.info("Window opened w={}, h={}", getSize().width, getSize().height);
    viewPort.start();
  }//GEN-LAST:event_formWindowOpened

  private void formKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_formKeyPressed
  {//GEN-HEADEREND:event_formKeyPressed
    if ( evt.getKeyCode() == KeyEvent.VK_ESCAPE )
    {
      LOGGER.info( "System exit by ESC key" );
      System.exit(0);
    }
    if ( evt.getKeyCode() == KeyEvent.VK_N )
    {
       viewPort.nextPicture();
    }    
  }//GEN-LAST:event_formKeyPressed

  // Variables declaration - do not modify//GEN-BEGIN:variables
  // End of variables declaration//GEN-END:variables
  private ViewPort viewPort;
}
