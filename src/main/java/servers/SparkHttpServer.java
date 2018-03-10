package servers;
import controllers.ChatRoomController;

import static spark.Spark.*;

public class SparkHttpServer implements Runnable{

  public void setup() {
   port(3000);
   staticFiles.location("/public");
   path("/chatroom", ()->{
     before("/*", (q ,a) -> {System.out.println("receive call");});
     get("/index", (req , res) -> {
        new ChatRoomController(req, res).index();
        return null;
     });
   });

  }

  @Override
  public void run() {
    init();
    setup();
  }
}
