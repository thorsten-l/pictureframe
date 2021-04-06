package l9g.app.pictureframe.config;

//~--- JDK imports ------------------------------------------------------------
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author Thorsten Ludewig <t.ludewig@gmail.de>
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@ToString
public class Configuration
{   
  @Getter
  @Setter
  private String picturePath;

  @Getter
  @Setter
  private String timezone;

  @Getter
  @Setter
  private long interval;

  @Getter
  @Setter
  private int tickDuration;

  @Getter
  @Setter
  private int consolePort;

  @Getter
  @Setter
  private int screenWidth;
  
  @Getter
  @Setter
  private int screenHeight;

  @Getter
  @Setter
  private boolean countdownShow;
  
  @Getter
  @Setter
  private int countdownX;

  @Getter
  @Setter
  private int countdownY;

  @Getter
  @Setter
  private int countdownRadius; 
}
