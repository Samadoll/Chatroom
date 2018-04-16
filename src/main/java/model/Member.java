package model;

import org.java_websocket.WebSocket;

import java.util.Map;
import java.util.UUID;

/**
 * A basic model for ChatRoom member
 * <p>
 * Created by ray on 2018/2/25.
 */
public class Member {

  private boolean isAdmin;
  private UUID id;
  private String name;
  private String hashedPassWord;

  public Member(String name) {
    this.name = name;
    this.isAdmin = false;
    this.hashedPassWord = "123";
  }

  public String getName() {
    return name;
  }

  public boolean isAdmin() {
    return isAdmin;
  }

  private void setAdmin(boolean status) {
    this.isAdmin = status;
  }

  public boolean comparePassword(String password) {
    return this.hashedPassWord.equals(password);
  }

  public boolean setMemberAsAdmin(Member m) {
    if (this.isAdmin) {
      m.setAdmin(true);
      return true;
    }

    return false;
  }

  // DataBase api to store a new member;
  public void store() {

  }

  public static Member getMember(String name) {
    return null;
  }

  public static boolean verifiedMemberLogin(String name, String password) {
    Member member = Member.getMember(name);
    if (member == null) return false;
    return member.comparePassword(password);
  }
}
