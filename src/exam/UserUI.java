package exam;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Iterator;

public class UserUI {
    private BufferedReader br;

    public UserUI() {
        br = new BufferedReader(new InputStreamReader(System.in));
    }

    public int menu() {
        System.out.println("1. ȸ�����");
        System.out.println("2. ȸ�����");
        System.out.println("3. ȸ������");
        System.out.println("4. ȸ������");
        System.out.println("5. ����");
        int menuId = -1;
        try {
            String line = br.readLine();
            menuId = Integer.parseInt(line);
        } catch (Exception e) {
            System.out.println("�ٽ� �Է��ϼ���.");
            ;
        }
        return menuId;
    }

    public User createUser(int menuId,UserService userService,String email) {
        try {
            if(menuId==1) while (true) {
                if (userService.isOnly(email)) break;
                System.out.println("�ߺ��� email�Դϴ�.");
                email = inputEmail();
            }
            System.out.println("�̸��� �Է��ϼ��� : ");
            String name = br.readLine();
            int birthYear = -1;
            while (true) {
                birthYear = inputBirthYear();
                if (birthYear != -1) break;
            }
            return new User(email, name, birthYear);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void regUser(int createUser) {
        if (createUser==1) {
            System.out.println("��ϵǾ����ϴ�.");
        } else System.out.println("�ٽ� �Է��ϼ���.");
    }

    public void printUserList(Iterator<User> readUser) {
        try {
            System.out.printf("%-20s %-20s %-4s\n","Email","�̸�","����⵵");
            System.out.println("==============================================");
            while (readUser.hasNext()) {
                User user = readUser.next();
                System.out.printf("%-20s %-20s %-4d\n",user.getEmail(),user.getName(),user.getBirthYear());
            }
            System.out.println("==============================================");
        } catch (Exception e) {
            System.out.println("��� ����");
        }
    }


    public void updateUser(int updateUser) {
        while (true) {
            if (updateUser==1) {
                System.out.println("���� �Ϸ�");
                break;
            }
            System.out.println("������ ȸ�������� �����ϴ�.");
        }
    }

    public void deleteUser(int deleteUser) {
        while (true) {
            if (deleteUser ==1) {
                System.out.println("���� �Ϸ�");
                break;
            }
            System.out.println("������ ȸ�������� �����ϴ�.");
        }
    }

    public String inputEmail() {
        System.out.println("email�� �Է��ϼ��� : ");
        try {
            return br.readLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public int inputBirthYear() {
        System.out.println("����⵵�� �Է��ϼ��� : ");
        try {
            int birthYear = Integer.parseInt(br.readLine());
            if(birthYear >9999||birthYear<1) throw new Exception("����⵵�� �ùٸ��� �Է��ϼ���.");
            return birthYear;
        } catch (Exception e) {
            System.out.println("����⵵�� �ùٸ��� �Է��ϼ���.");
        }
        return -1;
    }
}
