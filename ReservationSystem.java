import java.util.*;
import java.io.*;

/**
 * The core system of the reservation system
 * Contains all the prompts for the users
 * Writes output data into file
 * @author Shaoyue Liu
 *
 */
public class ReservationSystem {
	public static void main(String[] args) throws FileNotFoundException {
		ReservationUtilities util = new ReservationUtilities();
		if(args.length == 1) {
			readFile(util, args[0]); //call readfile() method to read input file
		}
		Scanner in = new Scanner(System.in); //Read user input
		String input = "";
		
		while(true) {
			System.out.println("Add [P]assenger, Add [G]roup, [C]ancel Reservations, "
					+ "Print Seating [A]vailability Chart, Print [M]anifest, [Q]uit");
			System.out.println("Please enter a letter shown above: ");
			input = in.nextLine().toUpperCase();
			
			//no input
			if(input.length() == 0) {
				System.out.println("No input detected, please enter a letter shown above:");
			}
			//Add passenger
			else if(input.charAt(0) == 'P') {
				assignPassenger(util, in);
			}
			//Add group
			else if(input.charAt(0) == 'G') {
				addGroup(util, in);
			}
			//Cancel Reservation
			else if(input.charAt(0) == 'C') {
				cancelReservation(util, in);
			}
			//Print seating availability chart
			else if(input.charAt(0) == 'A') {
				System.out.println("Enter First/Economy to check availability:");
				input = in.nextLine();
				if(input.equalsIgnoreCase("First")) {
					System.out.println(util.printAvailability("First"));
				}
				else if(input.equalsIgnoreCase("Economy")) {
					System.out.println(util.printAvailability("Economy"));
				}
				else {
					System.out.println("Invalid input.\n");
				}
			}
			//Print manifest
			else if(input.charAt(0) == 'M') {
				printManifest(util, in);
			}
			//Quit, save to file
			else if(input.charAt(0) == 'Q') {
				//argument exist
				if(args.length == 1) {
					saveToFile(util, args[0]);
				}
				//argument doesn't exist
				else {
					System.out.println("Error detected, unable to save data.");
				}
				System.exit(0);
			}
			//invalid input
			else {
				System.out.println("Please enter a valid input.");
			}
		}
	}
	
	/**
	 * reading file from command line.
	 * @param data - in the file
	 * @param name - of the file
	 */
	public static void readFile(ReservationUtilities data, String name) {
		File f = new File(name);
		
		//check for duplicate file
		if(f.exists()) {
			String pointer;
			String group;
			int seatNumber;
			String[] content = new String[4];
			
			try {
				Scanner fileScanner = new Scanner(f);
				fileScanner.nextLine();
				
				while(fileScanner.hasNextLine()){
					pointer = fileScanner.nextLine();
					content = pointer.split(",");
					seatNumber = Integer.parseInt(content[0].substring(0, 1));
					if(content.length == data.FILE_PARAMETER-1) {
						group = null;
					}
					else {
						group = content[data.FILE_PARAMETER-1];
					}
					//add data to file
					data.updateFromFile(new Passenger(content[2], content[0], group), seatNumber);
				}
			}
			catch(FileNotFoundException e) {
				System.out.println("File not found.");
			}
		}
	}
	
	/**
	 * Prompt users with class and seat preference to assign seat
	 * @param util - of the reservation system
	 * @param scan - user input
	 */
	public static void assignPassenger(ReservationUtilities util, Scanner scan) {
		String name, classPref, seatPref;
		String output = "";
		
		System.out.println("Please enter your name: ");
		name = scan.nextLine();
		//desired class
		do {
			System.out.println("Please enter your desired service class (First/Economy): ");
			classPref = scan.nextLine();
			if (!classPref.equalsIgnoreCase("First") && !classPref.equalsIgnoreCase("Economy")) {
				System.out.println("Please enter a valid service class (First/Economy)");
			}
		}while (!(classPref.substring(0, 1).equalsIgnoreCase("F")) && !(classPref.substring(0, 1).equalsIgnoreCase("E")));
		//desired seat
		while (output.equals("")){
			if(classPref.equalsIgnoreCase("First")) {
				System.out.println("Please enter your seat preference: [W]indow, or [A]isle");
			}
			else if(classPref.equalsIgnoreCase("Economy")) {
				System.out.println("Please enter your seat preference: [W]indow, [A]isle, or [C]enter");
			}
			
			seatPref = scan.nextLine().toUpperCase();
			Passenger p = new Passenger(name, classPref, null); //no group name
			output = util.addPassenger(p, seatPref);
			
			//return message
			if(output.equals("")) {
				System.out.println("Sorry, no seats available for your given preference. Please try a different preference");	
			} else if (output.equals("No more seats.")) {
				System.out.println("Sorry, the service class is full!");
			} else {
				System.out.println("Passenger seated at: " + output + "\n");
			}
		}
	}
	
	/**
	 * Add group to the reservation
	 * @param util - of the reservation system
	 * @param scan - user input
	 */
	public static void addGroup(ReservationUtilities util, Scanner scan) {
		String groupName, classPref, members;
		
		System.out.println("Please enter the group name: ");
		groupName = scan.nextLine();
		System.out.println("Please enter the names of people in the group separated by commas: ");
		members = scan.nextLine();
		
		//Prompt service class
		do {
			System.out.println("Enter a service class: First/Economy: ");
			classPref = scan.nextLine();
			if (!(classPref.substring(0, 1).equalsIgnoreCase("F")) && !(classPref.substring(0, 1).equalsIgnoreCase("E"))) {
				System.out.println("Please enter a valid service class (First/Economy).");
			}
		} while (!(classPref.substring(0, 1).equalsIgnoreCase("F")) && !(classPref.substring(0, 1).equalsIgnoreCase("E")));
		
		//add members of the group
		int groupSize = 1; //At least 1 person has to be in a group
		for(int i = 0; i < members.length(); i++) {
			if(members.charAt(i) == ','){
				groupSize++;
			}
		}
	
		//add all members to an array
		String [] names = new String[groupSize];
		names = members.split(",");
		ArrayList<Passenger> group = new ArrayList<Passenger>();
		//add the group to the arrayList
		for (int i = 0; i < groupSize; i++) {
			names[i] = names[i].trim();
			group.add(new Passenger(names[i], classPref, groupName));
		}
		//add group to reservation system
		String output = util.groupAdd(group);
		if (output != null) {
			System.out.println(output);
		} else {
			System.out.println("Sorry, the service class is full.\n");
		}
	}
	
	/**
	 * Cancel reservation
	 * @param util - reservation program
	 * @param scan - user input
	 */
	public static void cancelReservation(ReservationUtilities util, Scanner scan) {
		String input;
		String seatReserved = "";
		
		//prompt user to cancel individual or group reservation
		System.out.println("Please enter your cancellation type? [I]ndividual or [G]roup? ");
		input = scan.nextLine().toUpperCase();
		
		//individual cancellation
		if (input.charAt(0) == 'I') {
			System.out.println("Enter your name: ");
			input = scan.nextLine();
			seatReserved = util.cancelOne(input);
		}
		//group cancellation
		else if(input.charAt(0) == 'G') {
			
			System.out.println("Enter the name of the group: ");
			input = scan.nextLine();
			seatReserved = util.cancelGroup(input);
		}
		
		//output message to user
		if (seatReserved.equals("Your reservation havs been successfully cancelled.")) {
			System.out.println("Your reservation havs been successfully cancelled for: " + input + ".\n");
		} else {
			System.out.println("Sorry. Cancellation failed.");
		}
	}
	
	/**
	 * Print manifest
	 * @param util - reservation program
	 * @param scan - user input
	 */
	public static void printManifest(ReservationUtilities util, Scanner scan) {
		String classPref;
		//prompt user for class
		System.out.println("Which class' manifest would you like to print? First or Economy? ");
		classPref = scan.nextLine();

		//print manifest for selected class
		System.out.println(util.printManifest(classPref));
	}
	
	/**
	 * Saves all information to file
	 * @param util - reservation program
	 * @param file - to save to
	 */
	public static void saveToFile(ReservationUtilities util, String file) {
		File output = new File(file);
		//save to file
		try {
		PrintWriter pw = new PrintWriter(new FileWriter(file));
		pw.println("First 1-2, Left: A-B, Right: C-D; Economy 10-29, Left: A-C, Right: D-F");
			String flightInformation = util.reservationData();
			pw.print(flightInformation);
			pw.close();
		} 
		catch (IOException e) {
			System.out.println("Error, save failed");
		}
		//message to user
		System.out.println("\nReservation data saved to: " + file);
		}
}
