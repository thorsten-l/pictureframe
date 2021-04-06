package l9g.app.pictureframe;

import java.awt.AWTException;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Robot;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.TimeZone;
import java.util.logging.Level;
import javax.swing.JFrame;
import javax.xml.bind.JAXB;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import l9g.app.pictureframe.config.Configuration;
import l9g.app.pictureframe.gui.BaseFrame;

/**
 *
 * @author Thorsten Ludewig <t.ludewig@ostfalia.de>
 */
public class App
{
  private static final String CONFIGURATION = "config.xml";

  final static Logger LOGGER = LoggerFactory.getLogger(App.class.
    getName());

  @Getter
  public static Configuration config;

  public static void buildInfo(PrintStream out)
  {
    BuildProperties build = BuildProperties.getInstance();
    out.println("Project Name    : " + build.getProjectName());
    out.println("Project Version : " + build.getProjectVersion());
    out.println("Build Timestamp : " + build.getTimestamp());
    out.flush();
  }

  public static void readConfiguration()
  {
    LOGGER.info("reading configuration file config.xml 2");

    try
    {
      Configuration c = null;
      File configFile = new File(CONFIGURATION);

      LOGGER.info("Config file: {}", configFile.getAbsolutePath());

      if (configFile.exists() && configFile.canRead())
      {
        c = JAXB.unmarshal(new FileReader(configFile), Configuration.class);

        LOGGER.debug("new config <{}>", c);

        if (c != null)
        {
          LOGGER.info("setting config");
          config = c;
        }
      }
      else
      {
        LOGGER.info("Can NOT read config file");
      }

      if (config == null)
      {
        LOGGER.error("config file NOT found");
        System.exit(2);
      }
    }
    catch (Exception e)
    {
      LOGGER.error("Configuratione file config.xml not found ", e);
      System.exit(2);
    }
  }

  public static void saveState()
  {
   
  }

  private static void setVsyncRequested(JFrame f, boolean b)
  {
    try
    {
      Class<?> tmpClass
              = Class.forName("com.sun.java.swing.SwingUtilities3");
      Method tmpMethod
              = tmpClass.getMethod( "setVsyncRequested", Container.class, boolean.class );
      tmpMethod.invoke(tmpClass, f, Boolean.valueOf(b));
      LOGGER.info( "VSync requested" );
    } catch (Throwable ignore)
    {
      LOGGER.error("Warning: Error while requesting vsync: " + ignore);
    }
  }

  public static void main(String[] args) throws Exception
  {
    BuildProperties build = BuildProperties.getInstance();
    LOGGER.info("Project Name    : " + build.getProjectName());
    LOGGER.info("Project Version : " + build.getProjectVersion());
    LOGGER.info("Build Timestamp : " + build.getTimestamp());
    
    if ( args.length == 1 && args[0].equals("--create-config"))
    {
      Configuration config = new Configuration();
      config.setPicturePath(".");
      config.setTimezone("Europe/Berlin");
      config.setInterval(60);
      config.setTickDuration(1000);
      config.setConsolePort(8640);
      
      GraphicsDevice device
              = GraphicsEnvironment.getLocalGraphicsEnvironment()
                      .getScreenDevices()[0];

      Rectangle screenSize = device.getDefaultConfiguration().getBounds();
      
      config.setScreenWidth(screenSize.width);
      config.setScreenHeight(screenSize.height);
      
      config.setCountdownShow(true);
      config.setCountdownRadius(32);
      config.setCountdownX(config.getScreenWidth()-48);
      config.setCountdownY(config.getScreenHeight()-48);
      
      LOGGER.info( "Writing config.xml");
      LOGGER.info( "{}", config );
      
      JAXB.marshal(config, new FileWriter(CONFIGURATION));
      
      System.exit(0);
    }
    
    Runtime.getRuntime().addShutdownHook(new Thread()
    {
      @Override
      public void run()
      {
        LOGGER.info("Shutdown Hook is running !");
        saveState();
      }
    });

    readConfiguration();
    LOGGER.debug(config.toString());

    new Console(config.getConsolePort()).start();

    String timezone = config.getTimezone();

    if (timezone != null && timezone.trim().length() > 0)
    {
      TimeZone.setDefault(TimeZone.getTimeZone(timezone));
    }

    java.awt.EventQueue.invokeLater(() ->
    {
      BaseFrame baseFrame = new BaseFrame();

      GraphicsDevice device
              = GraphicsEnvironment.getLocalGraphicsEnvironment()
                      .getScreenDevices()[0];

      baseFrame.setAlwaysOnTop(true);
      baseFrame.setAutoRequestFocus(true);

      Rectangle screenSize = device.getDefaultConfiguration().getBounds();
      Dimension appDimension = new Dimension(screenSize.width, screenSize.height);
        
      LOGGER.info("Screen size = " + screenSize);

      baseFrame.setPreferredSize(appDimension);
      baseFrame.setMinimumSize(appDimension);
      baseFrame.setVisible(true);
      device.setFullScreenWindow(baseFrame);
      setVsyncRequested(baseFrame, true);
      
      try
      {
        Robot robot = new Robot();
        robot.mouseMove(screenSize.width, screenSize.height);
      }
      catch (AWTException ex)
      {
        java.util.logging.Logger.getLogger(App.class.getName()).
          log(Level.SEVERE, null, ex);
      }
    });
  }
}
