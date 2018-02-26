package servers;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ray on 2018/2/25.
 */
public class Router implements Runnable{

  String rawRequest;
  Map<String, String> params;

  public Router(String request) {
    this.rawRequest = request;
    params = new HashMap<String, String>();
  }

  public void analyse() {

  }

  public void dispatch() {}

  @Override
  public void run() {

  }
}
