package es.ujaen.sv_p4_practica1;
/**
 * 
 * @author Adrián Cotes Ruiz & María Linarejos González Ginés
 * this class must be used by the server for managing the data received from the client and for doing the pertinent
 * actions by the received request action of the client and responding to these message received from the client side
 */
public class Server
{
	/*	Login class that will be use for authentication in the system.
	*	It contains multiples attributes:
	*	user: an attribute that will contain the user name
	*	pass: it'll contain the password of the user
	*	domain_l: it'll will contain the domain in which the user is connecting
	*	port: the port of the domain
	*/
	protected String user = "";
	protected String pass = "";
	protected String domain_l = "";
	protected int port = 0;
	private int receivedId;
	private int receivedSequence;
	protected String message;
	private String receivedMessage;
	protected Message mess = null;
	protected Volt_divider voldi = null;
	protected byte[] buffer_out;
	
	/**
	 * this is the constructor that must be called first for creating the session, it's just for testing
	 * because the real communication between the server and the client must be through message send through the
	 * network
	 * @param user the user login
	 * @param pass the user password
	 */
	public Server(String user, String pass)
	{
		this.user = user;
		this.pass = pass;
	}
	
	/**
	 * constructor for the buffer in (data received from the client)
	 * @param bytedata the message received from the user
	 */
	public Server(byte[] bytedata)
	{
		mess = new Message(bytedata);
	}
	
	/*
	 * NOTES FOR correctLogin AND incorrectLogin METHODS
	 * once the automaticRequestedAction has been called and return an id = 1 (login request), and once on the server
	 * side has been checked that the login is correct or incorrect, the correcLogin or incorrectLogin must be called
	 * on the server side, to obtain the byte array that must be send through the network to the client
	 */
	
	/**
	 * this method is used for responding to the client for a correct login, thats mean that the login data
	 * send by the user is correct
	 * once the automaticRequestedAction has been called and return an id = 1 (login request), and once on the server
	 * side has been checked that the login is correct or incorrect, the correcLogin or incorrectLogin must be called
	 * on the server side, to obtain the byte array that must be send through the network to the client
	 * @return the message that will be send through the network as a byte array
	 */
	public byte[] correctLogin()
	{
		mess.loginResponse(200);
		return mess.toByteArray();
	}
	
	/**
	 * this method is used for responding to the client for a incorrect login, thats mean that the login data
	 * send by the user is incorrect
	 * once the automaticRequestedAction has been called and return an id = 1 (login request), and once on the server
	 * side has been checked that the login is correct or incorrect, the correcLogin or incorrectLogin must be called
	 * on the server side, to obtain the byte array that must be send through the network to the client
	 * @return the message that will be send through the network as a byte array
	 */
	public byte[] incorrectLogin()
	{
		mess.loginResponse(404);
		return mess.toByteArray();
	}
	
	/**
	* this method will be use for an automatic action for a request
	* note that, the login request, can not be an automatic action, it'll save the request message from the client
	* into the message String attribute, so that the server must call to getMessage method to obtain the request login
	* with all the login parameters (user, password, domain_l and port), and then take the content of it before calling
	* to the correctLogin method or incorrectLogin method.
	* if the returned id code is id != 0, then server just need to call to the getBufferOut method to obtain the 
	* byte array of the message that can be send through the net
	* @return the id of the requested action from the client
	*/
	public int automaticRequestedAction()
	{
		receivedMessage = mess.getMessage();
		String[] fields = receivedMessage.split(" ");
		receivedId = Integer.parseInt(fields[0]);
		switch(receivedId)
		{
			case 0:	//logout request
				receivedSequence = Integer.parseInt(fields[1]);
				message = receivedId + " " + receivedSequence + " " + fields[2];
				mess.logoutMessageResponse();
				//buffer out must be here
				buffer_out = mess.toByteArray();
				closeSession();
				break;
			case 1:	//login request message
				/*
				 * for an easier manage of the login request, the received login request message will be save into
				 * the message String attribute, then, if returned id value is id = 1, on the server side it only
				 * necessary to call to getMessage() method
				 */
				receivedSequence = Integer.parseInt(fields[1]);
				user = fields[2];
				pass = fields[3];
				domain_l = fields[4];
				port = Integer.parseInt(fields[5]);
				message = receivedId + " " + receivedSequence + " " + user + " " + pass + " " + domain_l + " " + port;
				break;
			case 2:
				receivedSequence = Integer.parseInt(fields[1]);
				voldi = new Volt_divider(Double.parseDouble(fields[2]), Double.parseDouble(fields[3]), Double.parseDouble(fields[4]), Double.parseDouble(fields[5])); 
				voldi.calc_vcc();
				mess.responseVcc(voldi);
				buffer_out = mess.toByteArray();
				break;
			case 3:
				receivedSequence = Integer.parseInt(fields[1]);
				voldi = new Volt_divider(Double.parseDouble(fields[2]), Double.parseDouble(fields[3]), Double.parseDouble(fields[4]), Double.parseDouble(fields[5])); 
				voldi.calc_vref();
				mess.responseVref(voldi);
				buffer_out = mess.toByteArray();
				break;
			case 4:
				receivedSequence = Integer.parseInt(fields[1]);
				voldi = new Volt_divider(Double.parseDouble(fields[2]), Double.parseDouble(fields[3]), Double.parseDouble(fields[4]), Double.parseDouble(fields[5])); 
				voldi.calc_r1();
				mess.responseR1(voldi);
				buffer_out = mess.toByteArray();
				break;
			case 5:	//Vold_divider request calculation message
				receivedSequence = Integer.parseInt(fields[1]);
				voldi = new Volt_divider(Double.parseDouble(fields[2]), Double.parseDouble(fields[3]), Double.parseDouble(fields[4]), Double.parseDouble(fields[5])); 
				voldi.calc_r2();
				mess.responseR2(voldi);
				buffer_out = mess.toByteArray();
				break;
		}
		return receivedId;
	}
	
	/**
	 * once the user is authenticated on the system, it can be set a domain and a port
	 * for testing purpose
	 * @param domain_l the domain of the server
	 */
	public void setDomain(String domain_l)
	{
		this.domain_l = domain_l;
	}
	/**
	 * once the user is authenticated on the system, it can be set a domain and a port
	 * for testing purpose
	 * @param port the port of the domain
	 */
	public void setPort(int port)
	{
		this.port = port;
	}

	/**
	 * if it's necessary, user can obtain the login data.
	 * just for testing
	 * @return login information
	 */
	public String getUser()
	{
		return this.user;
	}

	/**
	 * if it's necessary, user can obtain the login data.
	 * just for testing
	 * @return login information
	 */
	public String getPass()
	{
		return this.pass;
	}

	/**
	 * if it's necessary, user can obtain the login data.
	 * just for testing
	 * @return login information
	 */
	public String domain_l()
	{
		return domain_l;
	}

	/**
	 * if it's necessary, user can obtain the login data.
	 * just for testing
	 * @return login information
	 */
	public String getMessage()
	{
		return message;
	}
	
	/**
	 * for getting the message created for responding to the client
	 * @return the message for responding to the client, as a byte array
	 */
	public byte[] getBufferOut()
	{
		return buffer_out;
	}

	/**
	 * for closing the session
	 */
	public void closeSession()
	{
		user = "";
		pass = "";
		domain_l = "";
		port = 0;
		receivedId = 0;
		receivedSequence = 0;
		message = null;
		receivedMessage = null;
		mess = null;
		voldi = null;
	}	
}
