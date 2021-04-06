package l9g.app.pictureframe;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author Thorsten Ludewig <t.ludewig@ostfalia.de>
 */
@ToString
public class UpsObject
{
  public UpsObject( String name, String oid )
  {
    this.name = name;
    this.oid = oid;
  }
  
  @Getter
  private final String name;
  
  @Getter
  private final String oid;
  
  @Getter
  @Setter
  private int value;
}
