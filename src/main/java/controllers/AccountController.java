package controllers;

import model.Member;
import org.json.JSONException;
import org.json.JSONObject;
import servers.SparkHttpServer;
import spark.Request;
import spark.Response;

import java.util.UUID;

public class AccountController extends Controller {

  public AccountController(Request req, Response res, SparkHttpServer server) {
    super(req, res, server);
  }

  public void login() {
    try {
      JSONObject loginParams = new JSONObject(request.body());
      String account = loginParams.getString("account");
      String password = loginParams.getString("password");
      if (account.isEmpty() || password.isEmpty()) {
        render("json", new JSONObject().put("status", "failed"));
      } else if (Member.verifiedMemberLogin(account, password)) {
        response.cookie("session_id", UUID.randomUUID().toString(), 1);
        render("json", new JSONObject().put("status", "success"));
      } else {
        render("json", new JSONObject().put("status", "failed"));;
      }
    } catch (JSONException e) {
      render("text", "{status: error}", 500);
    }
  }

  public void register() {
    Request r = this.request;
  }
}
