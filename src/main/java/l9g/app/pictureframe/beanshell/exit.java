package l9g.app.pictureframe.beanshell;

import bsh.CallStack;
import bsh.Interpreter;

/**
 *
 * @author th
 */
public class exit
{
  public static void invoke(Interpreter env, CallStack callstack)
  {
    quit.invoke(env, callstack);
  }
}
