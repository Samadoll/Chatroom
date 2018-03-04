package controllers;

import java.net.Socket;
import java.util.Map;

public abstract class Controller implements Runnable{

  private Map<String,String> params;
  private Socket client;

  public Controller(Map<String,String> params, Socket socket){
    this.params = params;
    this.client = client;
  }
}
