package es.ujaen.sv_p4_practica1;
/**
 * 
 * @author Adrián Cotes Ruiz & María Linarejos González Ginés
 * a interface for specifying the methods and variables that must be include in the client application
 */
public interface Client 
{
	String user = "";
	String password = "";
	String domain_l = "";
	int port = 0;
	Message mess = null;
	Volt_divider voldi = null;
	String message = "";	//the message which will be send to the server
	String receivedMessage = "";	//the message received from the server
	double Vcc = 0, Vref = 0, r1 = 0, r2 = 0;
	int voldiType = 0;	//for specifying the Volt_divider action
	
	/**
	 * client must have a method for creating and sending a login message
	 * NOTES: when the client use this method, it will use the Message class for creating the login message
	 * and send it to the server, this login code should be used in a client application part in which later, it will
	 * use the receivedMessage method for getting the response that has been sent by the server, and then, do the
	 * following actions, if it is correct, continue, if it is not correct, abort operation
	 * NOTE: this method is just for creating and sending the message, the previous note is just a recommendation
	 * of how this method should be used
	 * @param user the login user name
	 * @param password the login password
	 * @param domain_l the domain in which the user is connecting
	 * @param port the port of the domain
	 */
	public static void login(String user, String password, String domain_l, int port)
	{	
	}
	
	/**
	 * for requesting a logout action to the server
	 * the same as before, it will only create and send the logout message to the server, once it has been sent,
	 * it is necessary to use the receievedMessage method for getting the logout response from the server.
	 */
	public static void logout()
	{
	}
	
	/**
	 * for requesting a Volt_divider action (example, calculate Vref)
	 * the same as the other two methods, just for creating and sending a Volt_divider action request, for example, for
	 * calculating Vref, once the message has been sent to the server, it is necessary to use the receivedMessage method
	 * for getting the response from the server, and then, show the content received to the user of the application
	 * @param voldiType the type of Volt_divider calculation
	 * @param vd a Volt_divider object for the calculation
	 */
	public static void requestVoltDividerAction(int voldiType, Volt_divider vd)
	{		
	}
	
	/**
	 * for getting the response of the server
	 * it will take the message from the server as a byte array, and then, it will return this message that has been
	 * received as a string message
	 * @param bytedata the buffer in, for taking the response from the server
	 * @return returning the received message as a simple string
	 */
	public static String receivedMessage(byte[] bytedata)
	{
		return receivedMessage;
	}
}