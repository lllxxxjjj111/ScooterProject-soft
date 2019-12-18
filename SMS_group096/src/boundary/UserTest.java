package boundary;

import control.UserControl;
import entity.User;

//public class UserTest {
//	public static void main(String args[]) {
//		String email[] = new String[5];
//		String id[] = new String[5];
//		String name[] = new String[5];
//		boolean er, ir, dup;
//		email[0] = new String("6325456@qq.com");
//		email[1] = new String("161188101@qmul.ac.uk");
//		email[2] = new String("zd@aa.");
//		email[3] = new String("liuxian@");
//		email[4] = new String("qianxin@qq");
//
//		id[0] = new String("161188100");
//		id[1] = new String("161188101");
//		id[2] = new String("161188102");
//		id[3] = new String("161188102");
//		id[4] = new String("16118810a");
//		
//		name[0] = new String("liming");
//		name[1] = new String("wangling");
//		name[2] = new String("zhaodong");
//		name[3] = new String("liuxian");
//		name[4] = new String("qianxin");
//
//		UserControl userList = new UserControl();
//		
//		for (int i = 0; i < 5; i++) {
//			User user = new User(id[i], name[i] ,email[i]);
//			er = user.isEmailLegal();
//			ir = user.isIDLegal();
//			dup = userList.isDuplication(user.getStudentID());
//			if (er == true && ir == true && dup == true) {
//				userList.register(user);
//				System.out.println(i+"the user has been registered.");
//			}
//			if (er == false) {
//				System.out.println(i+"the user input email is not legal");
//			}
//			if (ir == false) {
//				System.out.println(i+"the user input ID is not legal");
//			} 
//			if(dup == false)
//				System.out.println(i+"the user has been there");
//		}
//
//	}
//	
//
//}
