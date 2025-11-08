package OracLib;

import java.util.ArrayList;

class UserData {
    private int index;
    private String username;
    private String password;
    private String profilePic;
    private String userBio;
    private String age;
    private String book1;
    private String book2;
    private String book3;
    private String book4;
    private String book5;

    public UserData(String username, String password, String profilePic, String userBio, String age, String book1, String book2, String book3, String book4, String book5) {
        this.username = username;
        this.password = password;
        this.profilePic = profilePic;
        this.userBio = userBio;
        this.age = age;
        this.book1 = book1;
        this.book2 = book2;
        this.book3 = book3;
        this.book4 = book4;
        this.book5 = book5;
    }
    public void setUserIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void setUsername(String username) {this.username = username;}

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {this.password = password;}

    public String getPassword() {
        return password;
    }

    public void setProfilePic(String profilePic) {this.profilePic = profilePic;}

    public String getProfilePic() {
        return profilePic;
    }

    public void setUserBio(String userBio) {this.userBio = userBio;}

    public String getUserBio() {
        return userBio;
    }

    public void setAge(String age) {this.age = age;}

    public String getAge() {
        return age;
    }

    public void setBook1(String book1) {this.book1 = book1;}

    public String getBook1() {
        return book1;
    }

    public void setBook2(String book2) {this.book2 = book2;}

    public String getBook2() {
        return book2;
    }

    public void setBook3(String book3) {this.book3 = book3;}

    public String getBook3() {
        return book3;
    }

    public void setBook4(String book4) {this.book4 = book4;}

    public String getBook4() {
        return book4;
    }

    public void setBook5(String book5) {this.book5 = book5;}

    public String getBook5() {
        return book5;
    }
}
