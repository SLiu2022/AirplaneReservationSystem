import java.util.*;

/**
 * 
 * Has all the utilities for the reservation system
 * @author Shaoyue Liu
 */
public class ReservationUtilities {

	private static ArrayList<Seat> firstSeats;
	private static ArrayList<Seat> economySeats;
	//First Class has 2 Rows and 4 columns
	private static final int First_Rows = 2;
	private static final int First_Columns = 4;
	private static int First_Seat_Count = (First_Rows * First_Columns);
	private int firstSeatRemain = First_Seat_Count;
	//Economy Class has 20 rows and 6 columns
	private static final int Economy_Rows = 20;
	private static final int Economy_Columns = 6;
	private static int Economy_Seat_Count = (Economy_Rows * Economy_Columns);
	private int economySeatRemain = Economy_Seat_Count;
	//Passengers have 4 parameters: [P]assenger, Name, Class, Seat Preference
	public static int FILE_PARAMETER = 4;
	
	/**
	 * Constructor for plane class
	 */
	public ReservationUtilities() {
		firstSeats = initializeFirstClass();
		economySeats = initializeEconomyClass();
	}

	/**
	 * This method will initialize the First Class seats
	 * by filling up the arraylist with the corresponding
	 * seat numbers.
	 * @return firstSeats - arraylist for First Class seats
	 */
	private ArrayList<Seat> initializeFirstClass() {
		firstSeats = new ArrayList<Seat>();
		String seat = ""; //Seat Ex: 1A, 1B, 1C, 1D...2D
		
		//Initialize Seats
		for(int i = 1; i <= First_Rows; i++) {
			for(int j = 1; j <= First_Columns; j++) {
				seat += i;
				if(j == 1) {
					seat += "A";
				}
				else if(j == 2) {
					seat += "B";
				}
				else if(j == 3) {
					seat += "C";
				}
				else if(j == 4) {
					seat += "D";
				}
				firstSeats.add(new Seat(seat, "F"));
				seat = "";
			}
		}
		return firstSeats;
	}

	/**
	 * This method will initialize the Economy Class seats
	 * by filling up the arraylist with the corresponding
	 * seat numbers.
	 * @return firstSeats - arraylist for First Class seats
	 */
	private ArrayList<Seat> initializeEconomyClass() {
		economySeats = new ArrayList<Seat>();
		String seat = ""; //Seat Ex: 10A, 10B, 10C, 10D...29F
		int economyStart = 10; //economy starts at row 10
		//Initialize Seats
		for(int i = economyStart; i <= Economy_Rows + economyStart; i++) {
			for(int j = 1; j <= Economy_Columns; j++) {
				seat += i;
				if(j == 1) {
					seat += "A";
				}
				else if (j == 2) {
					seat += "B";
				}
				else if (j == 3) {
					seat += "C";
				}
				else if (j == 4) {
					seat += "D";
				}
				else if (j == 5) {
					seat += "E";
				}
				else if (j == 6) {
					seat += "F";
				}
				economySeats.add(new Seat(seat, "E"));
				seat = ""; //reset the loop
			}
		}
		return economySeats;
	}
	
	
	/**
	 * Add passenger from file and update seat count
	 * @param p - passenger
	 * @param seat - seat number
	 */
	public void updateFromFile(Passenger p, int seat) {
		if(p.getClassPref().equalsIgnoreCase("F") || p.getClassPref().equalsIgnoreCase("First")) {
			firstSeats.get(seat).assignPassenger(p);
			firstSeatRemain--;
		}
		else if(p.getClassPref().equalsIgnoreCase("E") || p.getClassPref().equalsIgnoreCase("Economy")) {
			economySeats.get(seat).assignPassenger(p);
			economySeatRemain--;
		}
	}
	
	/**
	 * Add passenger to the selected seat
	 * @param p - passenger
	 * @param seatPref - preferred seat
	 * @return - return the assigned seat/no seat available
	 */
	public String addPassenger(Passenger p, String seatPref) {
		//Handle First Class
		if(p.getClassPref().equalsIgnoreCase("First")) {
			//Check for remaining seats
			if(firstSeatRemain == 0) {
				return "No more seats.";
			}
			//loop to find and assign the first available seat to the passenger
			//Seat must meet the passenger's preference
			for(int i = 0; i < First_Seat_Count; i++) {
				//Check for seat availability and location
				if(firstSeats.get(i).getPassenger() == null && firstSeats.get(i).getSeatLocation().equalsIgnoreCase(seatPref)) {
					firstSeatRemain--; //reduce remaining seat by 1
						firstSeats.get(i).assignPassenger(p); //assign seat to passenger
						//return message
						return "Congratulation, Your Seat Number Is: " + firstSeats.get(i).getSeatNumber();
				}
			}
			//if no seats are available message
			return "Sorry, no seat available for your preference.";
		//Handle economy class
		} else if(p.getClassPref().equalsIgnoreCase("Economy")) {
			//Check for remaining seats
			if(economySeatRemain == 0) {
				return "No more seats.";
			}
			//loop to find and assign the first available seat to the passenger
			//Seat must meet the passenger's preference
			for(int i = 0; i < Economy_Seat_Count; i++) {
				//Check for seat availability and location
				if(economySeats.get(i).getPassenger() == null && economySeats.get(i).getSeatLocation().equals(seatPref)) {
						economySeatRemain--; //reduce remaining seat by 1
						economySeats.get(i).assignPassenger(p); //assign seat to passenger
						//return message
						return "Congratulation, Your Seat Number Is: " + economySeats.get(i).getSeatNumber();
				}
			}
			//if no seats are available message
			return "Sorry, no seat available for your preference.";
		}
		//If the input is neither first/economy class
		return "Sorry input error, please enter First/Economy class";
	}
	
	/**
	 * Add a group to the selected seats
	 * @param groupMembers - arraylist of passengers in the group
	 * @return groupSeats - string that contains the name and seat number
	 */
	public String groupAdd(ArrayList<Passenger> groupMembers) {
		ArrayList<Integer> list = new ArrayList<Integer>();
		String groupLocation = "";
		int adjacent = 0, max = 0;
		//Check if the group size > remaining seats
		//Handle first class
		if(groupMembers.get(0).getClassPref().equalsIgnoreCase("First") &&
				firstSeatRemain < groupMembers.size()) {
			return "Sorry, not enough available seats in the First Class.";
		} 
		//handle economy class
		else if (groupMembers.get(0).getClassPref().equalsIgnoreCase("Economy") &&
			economySeatRemain < groupMembers.size()) {
				return "Sorry, not enough available seats in the Economy Class.";
		}
		
		//Handle first class seating
		if(groupMembers.get(0).getClassPref().equalsIgnoreCase("First")) {
			//loop through group members
			for(int i = groupMembers.size(); i > 0; i--) {
				//loop through seats
				for(int j = 0; j < First_Seat_Count; j++) {
					//Check for seat availability
					if((j == First_Seat_Count -1) || ((j % First_Columns) == 0)){
						if (adjacent > max) {
							max = adjacent;
							//seat from the front the of the row
							for (int k = max; k >= 1; k--) 
								list.add(Math.abs(k-j));
						}
						//assign passenger to seat as long as the list is not empty
						if(max >= i) {
							while(!list.isEmpty() && !groupMembers.isEmpty()) {
								//Assign seat to passenger
								firstSeats.get(list.get(0)).assignPassenger(groupMembers.get(0));
								//Display passenger and assign seat
								groupLocation += (groupMembers.get(0).getName() + " is assigned to seat: " 
												  + firstSeats.get(list.get(0)).getSeatNumber() + "\n");
								//remove passenger from waiting list
								groupMembers.remove(0);
								list.remove(0);
							}
						}
						list.clear();
						adjacent = 0;
						max = 0;
					}
					
					//Check for adjacent seats
					if(firstSeats.get(j).getPassenger() != null) {
						if(adjacent > max) {
							list.clear();
							max = adjacent;
							for(int k = 1; k <= max; k++) {
								list.add(j-k);
							}
						}
						adjacent = 0; 
					} else {
						adjacent++;
						}
				}
			}
		}
		//handle economy class seating
		else if(groupMembers.get(0).getClassPref().equalsIgnoreCase("Economy")) {
			//loop through group members
			for(int i = groupMembers.size(); i > 0; i--) {
				//loop through seats
				for(int j = 0; j < Economy_Seat_Count; j++) {
					//Check for seat availability
					if((j == Economy_Seat_Count -1) || ((j % Economy_Columns) == 0)){
						if (adjacent > max) {
							max = adjacent;
							//seat from the front the of the row
							for (int k = max; k >= 1; k--) 
								list.add(Math.abs(k-j));
						}
						//assign passenger to seat as long as the list is not empty
						if(max >= i) {
							while(!list.isEmpty() && !groupMembers.isEmpty()) {
								//Assign seat to passenger
								firstSeats.get(list.get(0)).assignPassenger(groupMembers.get(0));
								//Display passenger and assign seat
								groupLocation += (groupMembers.get(0).getName() + "is assigned to seat: " 
												  + firstSeats.get(list.get(0)).getSeatNumber() + "\n");
								//remove passenger from waiting list
								groupMembers.remove(0);
								list.remove(0);
							}
						}
						list.clear();
						adjacent = 0;
						max = 0;
					}
					
					//Check for adjacent seats
					if(economySeats.get(j).getPassenger() != null) {
						if(adjacent > max) {
							list.clear();
							max = adjacent;
							for(int k = 1; k <= max; k++) {
								list.add(j-k);
							}
						}
						adjacent = 0; 
					} else {
						adjacent++;
						}
				}
			}
		}
		return groupLocation;
	}
	
	/**
	 * Cancel reservation for a single passenger
	 * @param name - name of the passenger
	 * @return output- message to user that indicates the reservation is canceled or not
	 */
	public String cancelOne(String name) {
		boolean seatReserved = false;
		String output = "";
		//Check first class for passenger name
		for (int x = 0; x < First_Seat_Count; x++) {
			if (firstSeats.get(x).getPassenger() != null) {
				if (name.equalsIgnoreCase(firstSeats.get(x).getPassenger().getName())) {
					firstSeatRemain++; //update remaining seats
					firstSeats.get(x).assignPassenger(null); //empty the seat for future passenger
					seatReserved = true;
				}
			}
		}
		
		//Check economy class for passenger name
				for (int x = 0; x < Economy_Seat_Count; x++) {
					if (economySeats.get(x).getPassenger() != null) {
						if (name.equalsIgnoreCase(economySeats.get(x).getPassenger().getName())) {
							economySeatRemain++; //update remaining seats
							economySeats.get(x).assignPassenger(null); //empty the seat for future passenger
							seatReserved = true;
						}
					}
				}
				
		//output message to user
		if(seatReserved) {
			output = "Your reservation havs been successfully cancelled.";
		}
		else {
			output = "Reservation not found.";
		}
		return output;
	}
	
	/**
	 * Cancel reservation for a group
	 * @param groupName - name of the group
	 * @return output - message to user that indicates the reservation is canceled or not
	 */
	public String cancelGroup(String groupName) {
		boolean seatReserved = false;
		String output = "";
		
		//Check first class for group name
		for(int i = 0; i < First_Seat_Count; i++) {
			if (firstSeats.get(i).getPassenger() != null) {
				if (groupName.equalsIgnoreCase(firstSeats.get(i).getPassenger().getGroup())) {
					firstSeatRemain++; //update remaining seats
					firstSeats.get(i).assignPassenger(null);//empty the seat for future passenger
					seatReserved = true;
				}
			}
		}
		
		//Check economy class for group name
		for(int i = 0; i < Economy_Seat_Count; i++) {
			if (economySeats.get(i).getPassenger() != null) {
				if (groupName.equalsIgnoreCase(economySeats.get(i).getPassenger().getGroup())) {
						economySeatRemain++; //update remaining seats
						economySeats.get(i).assignPassenger(null);//empty the seat for future passenger
						seatReserved = true;
				}
			}
		}
		
		//output message to user
		if(seatReserved) {
			output = "Your reservation havs been canceled successfully.";
		}
		else {
			output = "Reservation not found.";
		}
		return output;
	}
	
	/**
	 * Prints the availability chart of the desired class for the user
	 * @param classPref - user's input
	 * @return output - availability chart of the class
	 */
	public String printAvailability(String classPref) {
		boolean seatAvailable = false;
		String output = "";
		int seatNum = 0;
		//First class availability chart
		if(classPref.equalsIgnoreCase("First")) {
			output = "First: ";
			for (int i = 1; i <= First_Rows; i++) {
				seatAvailable = false;
				//find available seats
				for (int j = 1; j <= First_Columns; j++) {
					if (firstSeats.get(seatNum).getPassenger() == null)	{
						//available seat found
						if (seatAvailable == false) {	
							output += ("\n" + i + ": ");
							seatAvailable = true;
						} else {	
							output += ",";
						}
						//add to availability chart
						output += firstSeats.get(seatNum).getSeatNumber().charAt(1);
					}
					seatNum++;
				}
			}
		}
		//Economy class availability chart
		else if(classPref.equalsIgnoreCase("Economy")) {
			output = "Economy: ";
			for (int i = 10; i <= Economy_Rows+10; i++) {
				seatAvailable = false;
				//find available seats
				for (int j = 1; j <= Economy_Columns; j++) {
					if (economySeats.get(seatNum).getPassenger() == null)	{
						//available seat found
						if (seatAvailable == false) {	
							output += ("\n" + i + ": ");
							seatAvailable = true;
						} else {	
							output += ",";
						}
						//add to availability chart
						output += economySeats.get(seatNum).getSeatNumber().charAt(2);
					}
					seatNum++;
				}
			}
			output += "\n";
		}
		return output;
	}
	
	/**
	 * Create manifest of current passenger in the selected class
	 * @param classPref - user input
	 * @return manifest - contain all passengers in the class
	 */
	public String printManifest(String classPref) {
		String manifest = "";
		
		//Print first class manifest
		if(classPref.equalsIgnoreCase("First")) {
			manifest += "First Class Manifest\n";
			//loop to obtain all passengers
			for(int x = 0; x < First_Seat_Count; x++) {
				if(firstSeats.get(x).getPassenger() != null) {
					manifest += firstSeats.get(x).getSeatNumber() + ": ";
					manifest += firstSeats.get(x).getPassenger().getName() + "\n";
				}
			}
		}
		//Print economy class manifest
		else if(classPref.equalsIgnoreCase("Economy")) {
			manifest += "Economy Class Manifest\n";
			//loop to obtain all passengers
			for(int x = 0; x < First_Seat_Count; x++) {
				if(economySeats.get(x).getPassenger() != null) {
					manifest += economySeats.get(x).getSeatNumber() + ": ";
					manifest += economySeats.get(x).getPassenger().getName() + "\n";
				}
			}
		}
		return manifest;
	}
	
	/**
	 * Reservation information to be outputted to file
	 * @return output - contains reservation information
	 */
	public String reservationData() {
		String output = "";
		//First class information
		for (int x = 0; x < First_Seat_Count; x++) {		
			if (firstSeats.get(x).getPassenger() != null) {
				output += (firstSeats.get(x).getSeatNumber() + ", ");
					if (firstSeats.get(x).getPassenger().getGroup() == null) {
						output += ("I");		
					} else {
						output += ("G, ");
						output += (firstSeats.get(x).getPassenger().getGroup());
					}			
						output += (", " + firstSeats.get(x).getPassenger().getName());
						output += "\n";
					}
				}
		//loop over all the economy class to get the information
		for (int x = 0; x < Economy_Seat_Count; x++) {
			if (economySeats.get(x).getPassenger() != null) {
				output += (economySeats.get(x).getSeatNumber() + ", ");	
				if (economySeats.get(x).getPassenger().getGroup() == null) {		
					output += ("I");
				} else {		
					output += ("G, ");
					output += (economySeats.get(x).getPassenger().getGroup());
				}	
					output += (", " + economySeats.get(x).getPassenger().getName());
					output += "\n";
			}
		}

		return output;
	}	
}



