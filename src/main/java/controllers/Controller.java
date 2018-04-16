package controllers;

import com.google.common.io.Files;
import org.json.JSONObject;
import servers.SparkHttpServer;
import spark.Request;
import spark.Response;

import java.io.File;
import java.nio.charset.Charset;

public abstract class Controller {

  Request request;
  Response response;
  SparkHttpServer httpServer;

  public Controller(Request req, Response res, SparkHttpServer httpServer) {
    this.request = req;
    this.response = res;
    this.httpServer = httpServer;
  }

  private void sendFile(String path) {
    try {
      String root = System.getProperty("user.dir");
      File htmlFile = new File(root+ path);
      response.type("text/html");
      String htmlString = Files.asCharSource(htmlFile, Charset.forName("UTF-8")).read();
      response.body(htmlString);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void render(String format, Object target, int ...status) {
    if (status.length == 0) {
      response.status(200);
    } else {
      response.status(status[0]);
    }
    switch (format) {
      case "html":
        sendFile((String) target);
        break;
      case "json":
        JSONObject json = (JSONObject) target;
        response.type("json");
        response.body(target.toString());
        break;
      case "text":
        response.type("text/html");
        response.body((String) target);
        break;
    }
  }
}
