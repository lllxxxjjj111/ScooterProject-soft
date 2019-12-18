package entity;
import java.io.Serializable;
public class Station implements Serializable{
	private Slot slot[];
	private static int slotAmount =8;
	
	public Station() {
		this.slot = new Slot[slotAmount];
		for(int i=0;i<slotAmount;i++) {
			if(i<slotAmount/2) {
				slot[i] = new Slot(true,i);
			} else {
				slot[i] = new Slot(false,i);
			}
		}
	}

	public static int getSlotAmount() {
		return slotAmount;
	}
	
	public Slot getSlot(int i) {
		return slot[i];
	}		
	
	public void openSlot(int slotNo) {
		slot[slotNo].setLocked(false);
	}
	
	public void closeSlot(int slotNo) {
		slot[slotNo].setLocked(true);
	}
	
}