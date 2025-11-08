package OracLib;

import java.util.ArrayList;
import java.util.List;

public class UserProfiles {
    private List<UserData> users;

    public UserProfiles() {
        users = new ArrayList<>();
    }

    public List<UserData> getUsers() {
        return users;
    }

    public void setUsers(List<UserData> users) {
        this.users = users;
    }

    public void addUser(UserData user) {
        users.add(user);
    }

    public int searchUser(String username) {
        for (int i = 0; i < users.size(); i++) {
            UserData user = users.get(i);
            if (user != null && username.equals(user.getUsername())) {
                return i;
            }
        }
        return -1;
    }

    public UserData getUser(int index) {
        if (index >= 0 && index < users.size()) {
            return users.get(index);
        }
        return null;
    }
}
