package controllers;

import com.google.common.io.Files;
import spark.Request;
import spark.Response;

import java.io.File;
import java.nio.charset.Charset;

public abstract class Controller {

  Request request;
  Response response;

  public Controller(Request req, Response res) {
    this.request = req;
    this.response = res;
  }

  private void sendFile(String path) {
    try {
      File htmlFile = new File(path);
      response.type("text/html");
      response.status(200);
      String htmlString = Files.asCharSource(htmlFile, Charset.forName("UTF-8")).read();
      response.body(htmlString);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void render(String format, String target) {
    switch (format) {
      case "html":
        sendFile(target);
        break;
      case "json":
        break;
    }
  }
}
