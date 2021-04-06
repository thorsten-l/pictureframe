package l9g.app.pictureframe.beanshell;

import bsh.CallStack;
import bsh.Interpreter;
import l9g.app.pictureframe.App;

/**
 *
 * @author th
 */
public class buildinfo
{
  public static void invoke(Interpreter env, CallStack callstack)
  {
    App.buildInfo(env.getOut());
  }
}

