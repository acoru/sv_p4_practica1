package es.ujaen.sv_p4_practica1;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * 
 * @author Adrián Cotes Ruiz & María Linarejos González Ginés
 * Message class for defining the way the information must be sent from the server to the client and vice versa
 * It contains methods for creating request message and for creating response message, and two methods for buffer in
 * and buffer out
 */
public class Message
{
	//static integer sequence for managing a global sequence number for all the object of this Message class
	protected static int sequence;
	protected int id;	//it'll identify the type of message
	private int receivedId;
	protected int receivedSequence;
	protected String message = "";
	protected Volt_divider voldi = null;
	private String receivedData = "";
	private String login, password, domain_l;
	private int port;

	/**
	 * create a message login request, it must be the first client message
	 * it's the main constructor for a client that want to start the communication
	 * @param login the user for the login
	 * @param pass the password for the login
	 * @param domail_l the domain to connect
	 * @param port the port of the domain
	 */
	public Message(String login, String pass, String domail_l, int port)
	{
		id = 1;
		message = id + " " + sequence + " " + login + " " + pass + " " + domail_l + " " + port;
		sequence++;
	}
	
	/**
	 * message response for a login, server will send an OK message to the client
	 * this message is use as the server message constructor, it must be the first server message
	 * the response integer must be a 200 for "LOGIN OK" or 404 for "INVALID LOGIN" response, it must be implemented
	 * on server side
	 * @param response this parameter is set from the server side, 200 for "LOGIN OK" and 404 for "INVALID LOGIN"
	 */
	public void loginResponse(int response)
	{
		id = 100;
		message = id + " " + receivedSequence + " " + response;
		sequence++;
	}
	
	/**
	 * logout request message from the client side
	 */
	public void logoutMessageRequest()
	{
		id = 0;
		message = id + " " + sequence + " LOGOUT";
		sequence++;
	}
	
	/**
	 * logout message response from the server side
	 */
	public void logoutMessageResponse()
	{
		id = 100;
		message = id + " " + sequence + " BYE";
		sequence++;
	}

	/**
	 * for creating a message for requesting the value of Vcc (to be calculated by the server)
	 * @param vd a Volt_divider object
	 */
	public void requestVcc(Volt_divider vd)
	{
		id = 2;
		message = id + " " + sequence + " " + vd.toString();
		sequence++;
	}
	
	/**
	 * for creating the response to the request of Vcc from the client by the server
	 * @param vd a Volt_divider object
	 */
	public void responseVcc(Volt_divider vd)
	{
		id = 2;
		message = id + " " + receivedSequence + " " + vd.toString();
	}
	
	/**
	 * for creating a message for requesting the value of Vref (to be calculated by the server)
	 * @param vd a Volt_divider object
	 */
	public void requestVref(Volt_divider vd)
	{
		id = 3;
		message = id + " " + sequence + " " + vd.toString();
		sequence++;
	}
	
	/**
	 * for creating the response to the request of Vref from the client by the server
	 * @param vd a Volt_divider object
	 */
	public void responseVref(Volt_divider vd)
	{
		id = 3;
		message = id + " " + receivedSequence + " " + vd.toString();
	}
	
	/**
	 * for creating a message for requesting the value of R1 (to be calculated by the server)
	 * @param vd a Volt_divider object
	 */
	public void requestR1(Volt_divider vd)
	{
		id = 4;
		message = id + " " + sequence + " " + vd.toString();
		sequence++;
	}
	
	/**
	 * for creating the response to the request of R1 from the client by the server
	 * @param vd a Volt_divider object
	 */
	public void responseR1(Volt_divider vd)
	{
		id = 4;
		message = id + " " + receivedSequence + " " + vd.toString();
	}
	
	/**
	 * for creating a message for requesting the value of R2 (to be calculated by the server)
	 * @param vd a Volt_divider object
	 */
	public void requestR2(Volt_divider vd)
	{
		id = 5;
		message = id + " " + sequence + " " + vd.toString();
		sequence++;
	}
	
	/**
	 * for creating the response to the request of R2 from the client by the server
	 * @param vd a Volt_divider object
	 */
	public void responseR2(Volt_divider vd)
	{
		id = 5;
		message = id + " " + receivedSequence + " " + vd.toString();
	}
	
	/**
	 * creating a "buffer out" method, it'll be used for sending
	 * the data through the net to the client or the server
	 * @return the message that must be send as a byte array for sending it through the network
	 */
	public byte[] toByteArray()
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream(24);
		DataOutputStream dos = new DataOutputStream(bos);

		try
		{
			//just take the content of the message string and put into the DataOutputStream
			dos.writeChars(message);
			//for returning the message string as a byte array
			return bos.toByteArray();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	/**
	* it works as a buffer in, the String data is the received data from the net
	* it'll split the data variable and will get the values for setting inside
	* the receivedSecuence attribute and a Volt_divider "voldi" attribute
	* message request from the client id between 1 and 99
	* message response from the server from 101 to 199
	* example: if a user request the login, it will be used the id = 1, then,
	* the server will response to this request from the client with an id = 101
	* it'll be necessary a later use of the getMessage method for taking the content of the message string
	* id = 0 for logout request and id = 100 for logout response
	* @param bytedata the message received from the network that has been send from the client or the server
	*/
	public Message(byte[] bytedata)
	{
		//converting the input byte array into a string for later use
		ByteArrayInputStream bais = new ByteArrayInputStream(bytedata);
		BufferedReader in = new BufferedReader(new InputStreamReader(bais));
		try
		{
			receivedData = in.readLine();
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
		
		String[] fields = receivedData.split(" ");
		receivedId = Integer.parseInt(fields[0]);
		switch(receivedId)
		{
			case 0:	//logout request
				receivedSequence = Integer.parseInt(fields[1]);
				message = receivedId + " " + receivedSequence + " " + fields[2];
				break;
			case 1:	//login request message
				receivedSequence = Integer.parseInt(fields[1]);
				login = fields[2];
				password = fields[3];
				domain_l = fields[4];
				port = Integer.parseInt(fields[5]);
				message = receivedId + " " + receivedSequence + " " + login + " " + password + " " + domain_l + " " + port;
				break;
			case 2:
			case 3:
			case 4:
			case 5:	//Vold_divider request calculation message
				receivedSequence = Integer.parseInt(fields[1]);
				voldi = new Volt_divider(Double.parseDouble(fields[2]), Double.parseDouble(fields[3]), Double.parseDouble(fields[4]), Double.parseDouble(fields[5])); 
				message = receivedId + " " + receivedSequence + " " + voldi.toString();
				break;
			case 100:	//logout message response
				receivedSequence = Integer.parseInt(fields[1]);
				message = receivedId + " " + receivedSequence + fields[2];
				break;
			case 101:	//login message response
				receivedSequence = Integer.parseInt(fields[1]);
				message = receivedId + " " + receivedSequence + " " + fields[2];
				break;
			case 102:
			case 103:
			case 104:
			case 105:	//Volt_divider response calculation message
				receivedSequence = Integer.parseInt(fields[1]);
				voldi = new Volt_divider(Double.parseDouble(fields[2]), Double.parseDouble(fields[3]), Double.parseDouble(fields[4]), Double.parseDouble(fields[5])); 
				message = receivedId + " " + receivedSequence + " " + voldi.toString();
				break;
		}
	}
	
	/**
	 * just for getting the message
	 * NOTE: this method must be use every time after a buffer in message for getting the content of the message
	 * @return the message that has been received and converted into a simple String
	 */
	public String getMessage()
	{
		return message;
	}

	/**
	 * just for setting a message, just for testing purpose
	 * @param a String message, for testing purpose
	 */
	public void setMessage(String message)
	{
		this.message = message;
	}
}