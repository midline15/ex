package exam;

import java.io.Serializable;

public class User implements Serializable {
	private static final long serialVersionUID = 1L;
	private String email;
	private String name;
	private int birthYear;
	public User() {};
	public User(String email, String name, int birthYear) {
		this.email = email;
		this.name = name;
		this.birthYear = birthYear;
	}
	public String getEmail(){
		return email;
	}
	public String getName() {
		return name;
	}
	public int getBirthYear() {
		return birthYear;
	}
}
