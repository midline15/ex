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
        System.out.println("1. 회원등록");
        System.out.println("2. 회원목록");
        System.out.println("3. 회원수정");
        System.out.println("4. 회원삭제");
        System.out.println("5. 종료");
        int menuId = -1;
        try {
            String line = br.readLine();
            menuId = Integer.parseInt(line);
        } catch (Exception e) {
            System.out.println("다시 입력하세요.");
            ;
        }
        return menuId;
    }

    public User createUser(int menuId,UserService userService,String email) {
        try {
            if(menuId==1) while (true) {
                if (userService.isOnly(email)) break;
                System.out.println("중복된 email입니다.");
                email = inputEmail();
            }
            System.out.println("이름을 입력하세요 : ");
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
            System.out.println("등록되었습니다.");
        } else System.out.println("다시 입력하세요.");
    }

    public void printUserList(Iterator<User> readUser) {
        try {
            System.out.printf("%-20s %-20s %-4s\n","Email","이름","출생년도");
            System.out.println("==============================================");
            while (readUser.hasNext()) {
                User user = readUser.next();
                System.out.printf("%-20s %-20s %-4d\n",user.getEmail(),user.getName(),user.getBirthYear());
            }
            System.out.println("==============================================");
        } catch (Exception e) {
            System.out.println("목록 없음");
        }
    }


    public void updateUser(int updateUser) {
        while (true) {
            if (updateUser==1) {
                System.out.println("수정 완료");
                break;
            }
            System.out.println("수정할 회원정보가 없습니다.");
        }
    }

    public void deleteUser(int deleteUser) {
        while (true) {
            if (deleteUser ==1) {
                System.out.println("삭제 완료");
                break;
            }
            System.out.println("삭제할 회원정보가 없습니다.");
        }
    }

    public String inputEmail() {
        System.out.println("email을 입력하세요 : ");
        try {
            return br.readLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public int inputBirthYear() {
        System.out.println("출생년도를 입력하세요 : ");
        try {
            int birthYear = Integer.parseInt(br.readLine());
            if(birthYear >9999||birthYear<1) throw new Exception("출생년도를 올바르게 입력하세요.");
            return birthYear;
        } catch (Exception e) {
            System.out.println("출생년도를 올바르게 입력하세요.");
        }
        return -1;
    }
}
