
package control;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import entity.Station;

public class StationControl {

	public static boolean success = false;

	public static ArrayList<Station> stationArrayList = new ArrayList<Station>();

	public static void write() {
		ObjectOutputStream oos = null;  
        FileOutputStream fos = null;
		try {
			File csv = new File("stationList");
			fos = new FileOutputStream(csv);  
            oos = new ObjectOutputStream(fos);  
            oos.writeObject(stationArrayList);
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

	@SuppressWarnings("unchecked")
	public static void read(){
		FileInputStream fis = null;  
        ObjectInputStream ois = null; 
		try {
			fis = new FileInputStream("stationList");  
            ois = new ObjectInputStream(fis);
            stationArrayList = (ArrayList<Station>) ois.readObject();  
		} catch (Exception e) {
			e.printStackTrace();
			stationArrayList.add(new Station());
			stationArrayList.add(new Station());
			stationArrayList.add(new Station());
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

	public static int findFreeSlot(int index) {
		int slotNo = -1;
		for (int i = 0; i < Station.getSlotAmount(); i++) {
			if (stationArrayList.get(index).getSlot(i).isHasScooter() == false) {
				slotNo = i;
				break;
			}
		}
		return slotNo;
	}

	public static int findFreeScooter(int index) {
		int slotNo = -1;
		for (int i = 0; i < Station.getSlotAmount(); i++) {
			if (stationArrayList.get(index).getSlot(i).isHasScooter() == true) {
				slotNo = i;
				break;
			}
		}
		return slotNo;
	}

	public static int scanCard(String studentID) {
		if (UserControl.isCurrentUsing(studentID) == true) {
			return 1;// want to return a scooter
		} else if (UserControl.isAbleToBorrow(studentID) == true) {
			return 2; // borrow
		} else {
			return 3;// fire
		}
	}

	public static void userTakeScooter(int index) {
		int s = findFreeScooter(index);
		if (s == -1) {
			System.out.print("no spare scooter");
		} else {
			stationArrayList.get(index).getSlot(s).setLocked(false);
		}
	}

	public static void userReturnSccoter(int index) {
		int s = findFreeSlot(index);
		if (s == -1) {
			System.out.print("no spare slot");
		} else {
			stationArrayList.get(index).getSlot(s).setLocked(false);
		}
	}

	public static boolean isSuccess() {
		return success;
	}

	public static void setSuccess(boolean success) {
		StationControl.success = success;
	}

	public static void reSetAfterBorrow(int index, int i) {
			stationArrayList.get(index).getSlot(i).setHasScooter(false);
			stationArrayList.get(index).getSlot(i).setLocked(true);
	}

	public static void reSetAfterReturn(int index, int i) {
			stationArrayList.get(index).getSlot(i).setHasScooter(true);
			stationArrayList.get(index).getSlot(i).setLocked(true);
	}

	public static int scooterNumberInStation(int index) {
		int amount = 0;
		for (int i = 0; i < Station.getSlotAmount(); i++) {
			if (stationArrayList.get(index).getSlot(i).isHasScooter()) {
				amount++;
			}
		}
		return amount;
	}
}
