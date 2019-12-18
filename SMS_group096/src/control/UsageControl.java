package control;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

import entity.Usage;

public class UsageControl {
	
	public static ArrayList<Usage> usageArrayList = new ArrayList<Usage>();

	/**
	 * the method to update the day usage of each user
	 * @param studentID
	 * @param date
	 * @param time
	 */
	public static void updateUsage(String studentID, int date, int time) {
		boolean has = false;
		for (int i = 0; i < usageArrayList.size(); i++) {
			if ((usageArrayList.get(i).getStudentID().equals(studentID)) && (usageArrayList.get(i).getDate() == date)) {
				usageArrayList.get(i).setDayUsage(time);
				has = true;
				break;
			}
		}
		if (!has) {
			Usage usage = new Usage(studentID, date, time);
			usageArrayList.add(usage);
		}

	}

	/**
	 * the method to remove the usage record 7 days ago
	 */
	public static void removeUsage() {
		Calendar now = Calendar.getInstance();
		int today = now.get(Calendar.DAY_OF_YEAR);
		for (int i = 0; i < usageArrayList.size(); i++) {
			if (today - usageArrayList.get(i).getDate() > 7) {
				usageArrayList.remove(i);
			}
		}
	}

	/**
	 * the method to search the record of usage
	 * @param studentID
	 * @return
	 */
	public static int searchID(String studentID) {
		for (int i = 0; i < usageArrayList.size(); i++) {
			if (usageArrayList.get(i).getStudentID().equals(studentID)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * the method to read file
	 */
	@SuppressWarnings("unchecked")
	public static void read(){
		FileInputStream fis = null;  
        ObjectInputStream ois = null; 
		try {
			fis = new FileInputStream("usageList");  
            ois = new ObjectInputStream(fis);
            usageArrayList = (ArrayList<Usage>) ois.readObject();  
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
			File csv = new File("usageList");
			fos = new FileOutputStream(csv);  
            oos = new ObjectOutputStream(fos);  
            oos.writeObject(usageArrayList);
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

}
