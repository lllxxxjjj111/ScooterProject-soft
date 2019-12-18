package control;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import entity.Usage;
import entity.User;

public class UserControl {

	public static ArrayList<User> userArrayList = new ArrayList<User>();

	/**
	 * the method to verify the legality of inputed ID
	 * 
	 * @param studentID
	 * @return
	 */
	public static boolean isIDLegal(String studentID) {
		if (studentID.length() == 9) {
			try {
				Integer.valueOf(studentID);
				return true;
			} catch (Exception ex) {
			}
		}
		return false;
	}

	/**
	 * the method to verify the legality of inputed email address
	 * 
	 * @param email
	 * @return
	 */
	public static boolean isEmailLegal(String email) {
		// email address must in the form of xxx@xxx.xxx,
		// "[a-z0-9A-Z]+(.[a-z0-9A-z]{1,})?@[a-z0-9A-Z]+(.[a-z0-9A-z]{1,})?"
		String REGEX = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
		Pattern p = Pattern.compile(REGEX);
		Matcher matcher = p.matcher(email);
		return matcher.matches();
	}

	/**
	 * the method to check if the user is in the system
	 * 
	 * @param id
	 * @return
	 */
	public static boolean isDuplication(String id) {
		for (int i = 0; i < userArrayList.size(); i++) {
			if (userArrayList.get(i).getStudentID().equals(id)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * the method to register the user to the system
	 * 
	 * @param user
	 */
	public static void register(User user) {
		userArrayList.add(user);
		System.out.println(user);
	}

	/**
	 * the method to read file
	 */
	@SuppressWarnings("unchecked")
	public static void read(){
		FileInputStream fis = null;  
        ObjectInputStream ois = null; 
		try {
			fis = new FileInputStream("userList");  
            ois = new ObjectInputStream(fis);
            userArrayList = (ArrayList<User>)ois.readObject();  
		} catch (Exception e) {
			e.printStackTrace();
		} finally {  
            if (fis != null) {  
                try {  
                    fis.close();  
                } catch (IOException e1) {  
                    e1.printStackTrace();  
                }  
            }  
            if (ois != null) {  
                try {  
                    ois.close();  
                } catch (IOException e2) {  
                    e2.printStackTrace();  
                }  
            }  
        }  
	}

	/**
	 * the method to write file
	 */
	public static void write() {
		ObjectOutputStream oos = null;  
        FileOutputStream fos = null;
		try {
			File csv = new File("userList");
			fos = new FileOutputStream(csv);  
            oos = new ObjectOutputStream(fos);  
            oos.writeObject(userArrayList);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {  
            if (oos != null) {  
                try {  
                    oos.close();  
                } catch (IOException e1) {  
                    e1.printStackTrace();  
                }  
            }  
            if (fos != null) {  
                try {  
                    fos.close();  
                } catch (IOException e2) {  
                    e2.printStackTrace();  
                }  
            }  
        }  
	}

	/**
	 * the method to get the index of the student ID in the system
	 * 
	 * @param studentID
	 * @return
	 */
	public static int searchID(String studentID) {
		for (int i = 0; i < userArrayList.size(); i++) {
			if (userArrayList.get(i).getStudentID().equals(studentID)) {
				return i;
			}
		}
		return 0;
	}

	/**
	 * the method to record the time of using staring and set the user's status as
	 * using scooter
	 * 
	 * @param studentID
	 */
	public static void startUsing(String studentID) {
		int i = searchID(studentID);
		Calendar cld = Calendar.getInstance();
		userArrayList.get(i).setStatus(1);// 1 means using now
		userArrayList.get(i).setCld(cld);
	}

	/**
	 * the method to stop using scooter and check if need to be banned
	 * 
	 * @param studentID
	 */
	public static void endUsing(String studentID) {

		int[] time = usingTime(studentID);
		if (time[0] > 30 || time[1] > 120) {
			ban(studentID);
		}
	}

	/**
	 * the method to calculate the using time
	 * 
	 * @param studentID
	 * @return
	 */
	public static int[] usingTime(String studentID) {
		int i = searchID(studentID);
		int[] time = new int[2];
		Calendar now = Calendar.getInstance();
		int day = now.get(Calendar.DAY_OF_YEAR);
		Calendar last = userArrayList.get(i).getCld();
		int minute = (now.get(Calendar.YEAR) - last.get(Calendar.YEAR)) * 525600
				+ (now.get(Calendar.DAY_OF_YEAR) - last.get(Calendar.DAY_OF_YEAR)) * 1440
				+ (now.get(Calendar.HOUR_OF_DAY) - last.get(Calendar.HOUR_OF_DAY)) * 60
				+ (now.get(Calendar.MINUTE) - last.get(Calendar.MINUTE));
	//	System.out.println(minute);
		userArrayList.get(i).setStatus(2);// 2 means no using

		time[0] = minute;
		for (int j = 0; j < UsageControl.usageArrayList.size(); j++) {
			if ((UsageControl.usageArrayList.get(j).getStudentID().equals(studentID))
					&& (UsageControl.usageArrayList.get(j).getDate() == day)) {
				time[1] = minute + UsageControl.usageArrayList.get(j).getDayUsage();
				break;
			}
		}

		UsageControl.updateUsage(studentID, day, time[1]);
		return time;
	}

	/**
	 * the method to ban the user who hasn't payed the fine
	 * 
	 * @param studentID
	 */
	public static void ban(String studentID) {
		int i = searchID(studentID);
		if (i >= 0) {
			userArrayList.get(i).setFine(true);
			System.out.println("you are fined");
		} else
			System.out.println("something wrong");
	}

	/**
	 * the method to check if the user is using a scooter
	 * 
	 * @param studentID
	 * @return
	 */
	public static boolean isCurrentUsing(String studentID) {
		int i = searchID(studentID);
		if (userArrayList.get(i).getStatus() == 1) {
			return true;
		} else
			return false;
	}

	/**
	 * the method to check if the user can borrow a scooter not using and not having
	 * fine
	 * 
	 * @param studentID
	 * @return
	 */
	public static boolean isAbleToBorrow(String studentID) {
		int i = searchID(studentID);
		if (userArrayList.get(i).getStatus() == 2 && userArrayList.get(i).isFine() == false) {
			return true;
		} else
			return false;
	}

	/**
	 * the method to lift the ban of user after paying fine
	 * 
	 * @param studentID
	 */
	public static void unBan(String studentID) {
		int i = searchID(studentID);
		if (i >= 0) {
			userArrayList.get(i).setFine(false);
		} else
			System.out.println("something wrong");
	}
}
