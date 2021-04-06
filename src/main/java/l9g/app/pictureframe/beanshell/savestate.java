package l9g.app.pictureframe.beanshell;

import bsh.CallStack;
import bsh.Interpreter;
import l9g.app.pictureframe.App;

/**
 *
 * @author th
 */
public class savestate
{
  public static void invoke(Interpreter env, CallStack callstack)
  {
    env.getOut().println("save current golbal statistics to save.xml");
    App.saveState();
  }
}
