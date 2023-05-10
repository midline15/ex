package exam;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class UserServiceInMemory implements UserService {
    private List<User> users;

    public UserServiceInMemory() {
        this.users = new ArrayList<>();
    }

    public UserServiceInMemory(List<User> users) {
        this.users = users;
    }

    @Override
    public int createUser(User user) {
        try {
            if (user != null) {
                users.add(user);
                return 1;
            }
        } catch (Exception e) {
        }
        return -1;
    }

    @Override
    public Iterator<User> readUsers() {
        try {
            return users.iterator();
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public int updateUser(int i, User user) {
        if (i >= 0) {
            users.set(i, user);
            return 1;
        }
        return -1;
    }

    @Override
    public int deleteUser(int i) {
        if (i >= 0) {
            users.remove(i);
            return 1;
        }
        return -1;
    }

    @Override
    public int findIndex(String email) {
        int findIndex = -1;
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            if (user.getEmail().equals(email)) {
                findIndex = i;
                break;
            }
        }
        return findIndex;
    }

    @Override
    public boolean isOnly(String email) {
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            if (user.getEmail().equals(email)) {
                return false;
            }
        }
        return true;
    }
}
