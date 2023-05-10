package exam;

import java.util.Iterator;

public interface UserService {
    int createUser(User user);
    Iterator<User> readUsers();
    int updateUser(int i,User user);
    int deleteUser(int i);
    int findIndex(String email);
    boolean isOnly(String email);
}
