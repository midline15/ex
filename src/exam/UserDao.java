package exam;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class UserDao {
	private String filename;
	public UserDao(String filename) {
		this.filename = filename;
	}
	public void saveUser(Iterator<User> users) {
		List<User> serialUsers = new ArrayList<>();
		while(users.hasNext()){
			User user = users.next();
			serialUsers.add(user);
		}
		try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))){
			out.writeObject(serialUsers);
		}catch(Exception e) {e.printStackTrace();}
	}
	
	public List<User> getUsers(){
		File file = new File(filename);
		if(!file.exists()) {return new ArrayList<>();}

		List<User> users = null;
		try(ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))){
			users = (List<User>)in.readObject();
		}catch(Exception e) {e.printStackTrace();}
		return users;
	}
	
}
