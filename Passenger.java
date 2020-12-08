import java.util.*;

/**
 * Defining properties of the passengers
 * @author Shaoyue Liu
 */
public class Passenger {

	private String name;
	private String classPref;
	private String groupName;
	
	
	/**
	 * Constructor for the passenger class
	 * @param name - passenger's name
	 * @param classPref - preferred class
	 * @param group - group member names
	 */
	public Passenger(String name, String classPref, String group) {
		this.name = name;
		this.classPref = classPref;
		this.groupName = group;
	}
	
	/**
	 * Gets the name of the passenger
	 * @return name - of passenger
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the preferred class
	 * @return classPref - preferred class
	 */
	public String getClassPref() {
		return classPref;
	}
	
	/**
	 * Gets the group name
	 * @return groupName - name of the group
	 */
	public String getGroup() {
		return groupName;
	}
}
