package l9g.app.pictureframe.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import javax.imageio.ImageIO;
import l9g.app.pictureframe.App;
import l9g.app.pictureframe.config.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Dr. Thorsten Ludewig <t.ludewig@gmail.com>
 */
public class ImageFactory
{
  final static Logger LOGGER = LoggerFactory.getLogger(ImageFactory.class.
    getName());

  private final static ImageFactory singleton = new ImageFactory();

  private final ArrayList<File> fileList = new ArrayList<>();

  private final Random random = new Random();

  private final static BufferedImage[] imageBuffer = new BufferedImage[2];

  private static int currentImage = 0;

  private ImageFactory()
  {
  }

  private File getRandomFile()
  {
    File file = null;

    if (fileList.size() == 0)
    {
      String picturePath = App.getConfig().getPicturePath();
      File pictureDirectory = new File(picturePath);

      if (pictureDirectory.canRead() && pictureDirectory.isDirectory())
      {
        File[] files = pictureDirectory.listFiles(new FilenameFilter()
        {
          @Override
          public boolean accept(File dir, String name)
          {
            return name.toLowerCase().endsWith(".jpg");
          }
        });
        fileList.addAll(Arrays.asList(files));
        LOGGER.info("{} pictures found.", fileList.size());
      }
      else
      {
        LOGGER.error("Can not read picture path, or not a directory {}",
          picturePath);
        System.exit(-1);
      }
    }

    int numberOfPictures = fileList.size();

    if (numberOfPictures > 0)
    {
      int index = random.nextInt(numberOfPictures);
      file = fileList.get(index);
      fileList.remove(index);
      LOGGER.debug("{} / {}", index, fileList.size());
    }

    return file;
  }

  private BufferedImage getNextImage()
  {
    BufferedImage image = null;

    File imageFile = getRandomFile();

    if (imageFile != null)
    {
      try (InputStream imageInputStream = new FileInputStream(imageFile))
      {
        LOGGER.debug("Reading picture: {}", imageFile.getAbsolutePath());
        image = ImageIO.read(imageInputStream);
      }
      catch (IOException e)
      {
        image = null;
        LOGGER.error("ERROR: reading image: ", e);
      }
    }

    return image;
  }

  private void prepareNextImage()
  {
    LOGGER.debug("prepareNextImage");
    Configuration config = App.getConfig();

    int nextImage = 1 - currentImage;

    BufferedImage image = getNextImage();

    if (image != null)
    {
      imageBuffer[nextImage] = new BufferedImage(
        config.getScreenWidth(), config.getScreenHeight(),
        BufferedImage.TYPE_4BYTE_ABGR);

      Graphics2D g = (Graphics2D) imageBuffer[nextImage].getGraphics();

      g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);
      g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,
        RenderingHints.VALUE_COLOR_RENDER_QUALITY);
      g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
        RenderingHints.VALUE_INTERPOLATION_BICUBIC);
      g.setRenderingHint(RenderingHints.KEY_RENDERING,
        RenderingHints.VALUE_RENDER_QUALITY);

      g.setColor(Color.black);
      g.fillRect(0, 0, config.getScreenWidth(), config.getScreenHeight());

      int iw = image.getWidth();
      int ih = image.getHeight();
      int vw;
      int vh = config.getScreenHeight();
      int x;
      int y = 0;

      float scale = (float) iw / (float) ih;
      vw = (int) ((float) vh * scale);
      x = (config.getScreenWidth() - vw) / 2;

      if (x < 0)
      {
        x = 0;
        vw = config.getScreenWidth();
        scale = (float) ih / (float) iw;
        vh = (int) ((float) vw * scale);
        y = (config.getScreenHeight() - vh) / 2;
      }

      Image scaledImage = image.getScaledInstance(vw, vh, Image.SCALE_SMOOTH);

      g.drawImage(scaledImage, x, y, null);
    }
    else
    {
      LOGGER.info( "Image not available." );
    }
  }

  public static synchronized void update()
  {
    currentImage = 1 - currentImage;
    Thread pictureLoader = new Thread(() ->
    {
      singleton.prepareNextImage();
    }, "pictureLoader");
    pictureLoader.setDaemon(true);
    pictureLoader.start();
  }

  public static synchronized BufferedImage getImage()
  {
    if (imageBuffer[currentImage] == null)
    {
      singleton.prepareNextImage();
      update();
    }

    return imageBuffer[currentImage];
  }

}
