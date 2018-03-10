package servers;
import controllers.ChatRoomController;

import static spark.Spark.*;

public class SparkHttpServer implements Runnable{

  public void setup() {
   port(3000);
   staticFiles.externalLocation(System.getProperty("user.dir") + "/public");
   path("/chatroom", ()->{
     before("/*", (q ,a) -> {System.out.println("receive call");});
     get("/index", (req , res) -> {
        new ChatRoomController(req, res).index();
        return res.body();
     });
   });

  }

  @Override
  public void run() {
    setup();
    init();
  }
}
