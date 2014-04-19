import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class UI {

	// initialize variable for further use
	private String input;
	private BufferedReader stdin;
	private String message;
	private int start,end;
		
	public UI() {
		// load the main menu
		mainMenu();
	}
	
	private void mainMenu() {
		stdin = new BufferedReader (new InputStreamReader(System.in));
		try {
			System.out.println("Welcome to the Smart Home Monitoring System");
			System.out.println("Please select an option:");
			System.out.println("1. View Log - temperature adjustment");
			System.out.println("2. View Log - Health warnings for day(s)");
			System.out.println("E. Exit");		
			System.out.flush();
			
			input = stdin.readLine().trim();
			
			// based on user input, trigger the function
			switch(input){
			case "1": viewLog();
				break;
			case "2": viewHealthWarnings();
				break;
			case "E": exit();
				break;
			case "e": exit();
				break;
			default: System.out.println("Invalid command");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// always go back to main menu, the ENTER to return is implemented in each of the functions
		mainMenu();
	}
	
	
	/**
	 * This method executes the UI part of the view temperature log command
	 */
	private void viewLog() {
		//implement retrieve function
		
		if(message == null) 
	   	 {
             System.out.println("Log of temperature adjustment and water usage is empty");
	     } else {
             System.out.println(message);
	     }
		
		// return to the main menu
		pressEnterToReturn();
	}
	
	/**
	 * This method executes the UI part of the view temperature log Function
	 */
	public void viewHealthWarnings(){
		
		String temp=null;;
		System.out.println("Please enter a start day:");
        System.out.flush(); // empties buffer, before you input text
        try {
			temp = stdin.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        start = Integer.parseInt(temp);
        
        System.out.println("Please enter a end day:");
        System.out.flush(); // empties buffer, before you input text
        try {
			temp = stdin.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
        end = Integer.parseInt(temp);
		
        //implement retrieve function
        
        
        if(message == null) {
            System.out.println("Log of health warning information is empty");
        	} else {
            System.out.println("Health Information of Warnings issued " +
            		"between <start day> and <end day>:");
        		}
		
		
     // return to the main menu
 		pressEnterToReturn();
	
	}
	
	/**
	 * This method will notify the home manager to shut down, remove subscription, close connection
	 * tells the client to exit, and then exit the whole program itself
	 */
	private void exit() {
		
		//implement shoutdown function to server
		
		System.exit(0);
	}
	
	
	/**
	 * This method is to perform the "Return" function after each function has been executed 
	 */
	private void pressEnterToReturn() {
		try {
			input = stdin.readLine().trim();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// once something is returned, go back to main menu
		mainMenu();
	}
	

	/**
	 * main method
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		UI app= new UI();
	}

}
