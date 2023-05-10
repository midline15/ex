package exam;

public class UserMain {

	public static void main(String[] args) {
		UserUI userUI = new UserUI();
		UserDao userDao = new UserDao("C:\\users.dat");
		UserServiceInMemory userService = new UserServiceInMemory(userDao.getUsers());

		while (true) {
			int menuId = userUI.menu();
			if (menuId == 1) {
				String email= userUI.inputEmail();
				if(email.equals("q")||email.equals("Q")){
					System.out.println("������");
				}else userUI.regUser(userService.createUser(userUI.createUser(menuId,userService,email)));
			}if(menuId == 2) {
				userUI.printUserList(userService.readUsers());
			}if(menuId == 3){
				String email= userUI.inputEmail();
				if(email.equals("q")||email.equals("Q")){
					System.out.println("������");
				}else userUI.updateUser(userService.updateUser(userService.findIndex(email), userUI.createUser(menuId,userService,email)));
			}if(menuId == 4) {
				String email = userUI.inputEmail();
				if(email.equals("q")||email.equals("Q")){
					System.out.println("������");
				}else userUI.deleteUser(userService.deleteUser(userService.findIndex(email)));
			}if(menuId == 5) {
				userDao.saveUser(userService.readUsers());
				System.out.println("�����մϴ�.");
				break;
			}
		}
	}
}
