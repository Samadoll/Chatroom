package model;

/**
 * A basic model for ChatRoom member
 * <p>
 * Created by ray on 2018/2/25.
 */
public class Member {

  private boolean isAdmin;
  private String name;
  private String HashedPassWord;

  public Member(String name) {
    this.name = name;
    this.isAdmin = false;
    this.HashedPassWord = "123";
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
}
