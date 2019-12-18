package boundary;
/*
 * Based on SimpleWrite.java provided by Java Sun
 * Updated by Matthew Tang @QMUL 7 May 2017.
 * You need to have RXTX installed with Java
 * Link 1: http://rxtx.qbang.org/wiki/index.php/Main_Page
 * Link 2: http://fizzed.com/oss/rxtx-for-java
 */
import java.io.*;
import control.StationControl;
import control.UsageControl;
import control.UserControl;
import gnu.io.*; 

public class Serial implements Runnable{
    static CommPortIdentifier portId;
    static CommPort com;
    static SerialPort ser;
    static String id;
    static byte n;
    int STATIONNO=0;
    
    public void run() {
        try {
			// TODO: identify the COM port from Windows' control panel
            portId = CommPortIdentifier.getPortIdentifier("COM3");

            com = portId.open("MCS51COM", 2000);
            ser = (SerialPort)com;
			// Baud rate = 9600, Data bits = 8, 1 stop bit, Parity OFF
            ser.setSerialPortParams(9600, SerialPort.DATABITS_8, 
                                    SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
        } catch (Exception e){
            e.printStackTrace(System.out);
        }

		
		// Wait for 1 second if 8051 needs time to initialize
        try { 
            Thread.sleep(1000);
        } catch (InterruptedException e){}
		
        
       
        try {
        	InputStream comIn=ser.getInputStream();
        	OutputStream comOut=ser.getOutputStream();
        	StringBuffer buf=new StringBuffer();
        	int slotNo=-1;
        	while(true) {
        		while(comIn.available()==0);
        		char c=(char)comIn.read();
        		if(c>='0'&&c<='9') {
        			buf.append(c);
        		}
        		else if(c==(char)10) {//DELETE
        			buf.deleteCharAt(buf.length()-1);
        		}
        		else if(c==(char)11) {//COMMIT
        			id=buf.toString();
        			buf=new StringBuffer();
        			System.out.println(id);
        			n=idCheck(id);
        			
        			if(n==0)
        				System.exit(1);
        			else if(n==12||n==13||n==16) {
        				comOut.write(n);
        				continue;
        			}
        			else if(n==17||n==18) {
        				if(n==17) {
        					slotNo=StationControl.findFreeScooter(STATIONNO);
        					if(slotNo==-1) {
        						n=(byte)15;
        						comOut.write(n);
        						continue;
        					}
        					comOut.write(n);
            				comOut.write(slotTrans(slotNo));
            				StationControl.userTakeScooter(STATIONNO);
            				while(comIn.available()==0);
            				char st=(char)comIn.read();
            				if(st=='!') {//slot chosen
            					System.out.println("success");
                    			StationControl.reSetAfterBorrow(STATIONNO, slotNo);
                    			UserControl.startUsing(id);
                    			UserControl.write();
                    			StationControl.write();
                    			UsageControl.write();
                    		}
                    		else if(st=='?') {//timeout
                    			System.out.println("timeout");
                    			StationControl.stationArrayList.get(STATIONNO).getSlot(slotNo).setLocked(true);
                    		}
        				}
        				else if(n==18) {
        					slotNo=StationControl.findFreeSlot(STATIONNO);
        					if(slotNo==-1) {
        						n=(byte)14;
        						comOut.write(n);
        						continue;
        					}
        					comOut.write(n);
            				comOut.write(slotTrans(slotNo));
            				StationControl.userReturnSccoter(STATIONNO);
            				while(comIn.available()==0);
            				char st=(char)comIn.read();
            				if(st=='!') {//slot chosen
            					System.out.println("success");
                    			StationControl.reSetAfterReturn(STATIONNO, slotNo);
                    			UserControl.endUsing(id);
                    			UserControl.write();
                    			StationControl.write();
                    			UsageControl.write();
                    		}
                    		else if(st=='?') {//timeout
                    			System.out.println("timeout");
                    			StationControl.stationArrayList.get(STATIONNO).getSlot(slotNo).setLocked(true);
                    		}
        				}
        				
        			}
        			
        		}
        		
        	}
        	
        }
        catch(Exception e) {
        	e.printStackTrace(System.out);
        }
        ser.close(); 
    }
    
    public static byte idCheck(String id) {
    	
    	//verify the id length
    	if(id.length()!=9)
    		return (byte)12;
    	//verify the id exists
    	if(!UserControl.isDuplication(id)) {//verify the id exists
			return (byte)13;
		}
    	switch(StationControl.scanCard(id)) {
    	case 1:
    		return (byte)18;
    	case 2:
    		return (byte)17;
    	case 3:
    		return (byte)16;
    	}
    	
    	return 0;//error
    }
    
    public static byte slotTrans(int slotNo) {
    	if(slotNo==0)
    		return (byte)0xfe;
    	else if(slotNo==1)
    		return (byte)0xfd;
    	else if(slotNo==2)
    		return (byte)0xfb;
    	else if(slotNo==3)
    		return (byte)0xf7;
    	else if(slotNo==4)
    		return (byte)0xef;
    	else if(slotNo==5)
    		return (byte)0xdf;
    	else if(slotNo==6)
    		return (byte)0xbf;
    	else
    		return (byte)0x7f;
    }
}
