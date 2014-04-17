import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class UI {

	public void printMenu() {
		System.out.println("Welcome to the Smart" +
				"Home Monitoring System\n"+
				"Please Select an option\n"+
				"1. View Log - Temperature" +
				"adjustment and water usage\n"+
				"2. View Log - Health " +
				"warnings for day(s)\n"+
				"E. exit\n");

	}
	
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		UI app= new UI();
		do {
		app.printMenu();
		BufferedReader in = new BufferedReader(
				new InputStreamReader(System.in));
		System.out.print("Make selection : ");
		String Option = in.readLine();
		if(Option.equals("1"))
		{
			
		}
		else if(Option.equals("2"))
		{
			
		}
		else if(Option.equals("E"))
		{
			System.exit(1);
		}
		else {
			System.out.println("Invalid command");
			app.printMenu();
		}
		}while(true);
	}

}
