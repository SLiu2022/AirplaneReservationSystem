import java.util.*;

/**
 * Defining properties of the seats
 * @author Shaoyue Liu
 */
public class Seat {

	private Passenger passenger;
	private String seatNum;
	private String seatLocation; //window, center, or aisle
	
	/**
	 * Constructor for the Seat class
	 * Initialize seats without passenger
	 * @param number - seat number
	 * @param classPref - class preferred
	 */
	public Seat (String number, String classPref) {
		this.passenger = null;
		this.seatNum = number;
		
		//Economy Class
		if(classPref.equalsIgnoreCase("E")) {
			if (number.contains ("D") || number.contains("C")) {		
				this.seatLocation = "A"; //Aisle seat - base on the given diagram in the assignment page
			}
			else if (number.contains("A") || number.contains("F")) {
				this.seatLocation = "W"; //Window seat 
			} 
			else {
				this.seatLocation = "C"; //Center seat
			}
		}
		//First Class
		else if(classPref.equalsIgnoreCase("F")) {
			if (number.contains("D") || number.contains("A")) {
				this.seatLocation = "W"; //Window seat - base on the given diagram in the assignment page
			} 
			else {
				this.seatLocation = "A"; //Aisle Seat
			}
		}
	}
	
	/**
	 * Gets the seat number
	 * @return seatNum - seat number
	 */
	public String getSeatNumber() {
		return seatNum;
	}
	
	/**
	 * Gets the seat location
	 * @return seatLocation
	 */
	public String getSeatLocation() {
		return seatLocation;
	}
	
	/**
	 * Gets the assigned passenger
	 * @return passenger - assigned to seat
	 */
	public Passenger getPassenger() {
		return passenger;
	}
	
	/**
	 * Assign passenger 
	 * @param assigned - passenger that is assigned to the seat
	 */
	public void assignPassenger(Passenger assigned) {
		passenger = assigned;
	}
}