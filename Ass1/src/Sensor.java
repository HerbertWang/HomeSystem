import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class Sensor {

	private static String sensorTypeInput, fileNameInput;
	private String[] values;
	private String fileName, sensorType, line, value, tempSensorMode, userName;
	private int period;
	private FileReader fr;
	private BufferedReader br;
	private boolean active;
	
	public Sensor(String sensorTypeInput, String fileNameInput){
		this.sensorType = sensorTypeInput;
		this.fileName = fileNameInput;
		try {
			fr = new FileReader(fileName);
			br = new BufferedReader(fr);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		System.out.println("the Sensor has been set up");
	}
	
	/**
	 * This method will activate the sensor
	 */
	private void activateSensor(){
		active = true;
		Thread sensorTask = new SensorTask();
		sensorTask.start();
	}
	
	/**
	 * This class defines the main task that will be executed periodically
	 */
	private final class SensorTask extends Thread {

		/**
		 * This is the main execution of the thread
		 */
		public void run() {
			
			// if period of last line = 0, then it's time to read the next line
			if (period <= 0) {
				// reads the next line. If reached the end of file, start over on the same file
				try {
					if((line = br.readLine()) == null) {
						fr.close();
						fr = new FileReader(fileName);
						br = new BufferedReader(fr);
						line = br.readLine();
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
						
				// parse the line. from format, first is value, second field is timeTicker
				values = line.split(",");
				value = values[0];
				System.out.println(value);
				period = Integer.parseInt(values[1]);
				System.out.println(period);
			}
							
			// decrement period
			period--;
		}
	}
	
	/**
	 * return sensor type
	 */
	public String getSensorType() {
		return this.sensorType;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Sensor app=new Sensor("Temperature","Temperature.txt");
		app.activateSensor();
		 while(app.active){
         }
	}
	
	public void exit(){	
		// exit the entire program
		System.exit(0);
	}
	
}
