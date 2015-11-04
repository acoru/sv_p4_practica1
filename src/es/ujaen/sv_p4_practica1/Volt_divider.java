package es.ujaen.sv_p4_practica1;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;


/**
 * 
 * @author Adrián Cotes Ruiz & María Linarejos González Ginés
 * it is the data class, it's mean that it'll contain the actions that will be implemented on the server
 * for been requested by the client
 */
public class Volt_divider
{
	protected double vcc = 0, vref = 0, r1 = 0, r2 = 0;

	/**
	 * the main constructor, for setting the initial values into the attributes of the class
	 * @param vcc the Vcc tension
	 * @param vref the Vref tension
	 * @param r1 the first resistor
	 * @param r2 the second resistor
	 */
	public Volt_divider(double vcc, double vref, double r1, double r2)
	{
		this.vcc = vcc;
		this.vref = vref;
		this.r1 = r1;
		this.r2 = r2;
	}

	/**
	 * method used by the server for calculate the Vcc from a request of the client
	 */
	public void calc_vcc()
	{
		vcc = vref * ((r1 + r2) / r2);
	}

	/**
	 * method used by the server for calculate the Vref from a request of the client
	 */
	public void calc_vref()
	{
		vref = vcc * (r2 / (r1 + r2));
	}

	/**
	 * method used by the server for calculate the R1 from a request of the client
	 */
	public void calc_r1()
	{
		r1 = (vcc / vref - 1) * r2;
	}

	/**
	 * method used by the server for calculate the R2 from a request of the client
	 */
	public void calc_r2()
	{
		r2 = (r1 / (vcc / vref - 1));
	}

	/**
	 * for setting a value to the Vcc attribute, for testing purpose
	 * @param vcc the value of Vcc to set
	 */
	public void set_vcc(double vcc)
	{
		this.vcc = vcc;
	}

	/**
	 * for setting a value to the Vref attribute, for testing purpose
	 * @param vcc the value of Vref to set
	 */
	public void set_vref(double vref)
	{
		this.vref = vref;
	}

	/**
	 * for setting a value to the R1 attribute, for testing purpose
	 * @param vcc the value of R1 to set
	 */
	public void set_r1(double r1)
	{
		this.r1 = r1;
	}

	/**
	 * for setting a value to the R2 attribute, for testing purpose
	 * @param vcc the value of R2 to set
	 */
	public void set_r2(double r2)
	{
		this.r2 = r2;
	}

	/**
	 * just for getting the value of Vcc
	 * @return to obtain the Vcc value
	 */
	public double get_vcc()
	{
		return vcc;
	}

	/**
	 * just for getting the value of Vref
	 * @return to obtain the Vref value
	 */
	public double get_vref()
	{
		return vref;
	}

	/**
	 * just for getting the value of R1
	 * @return to obtain the R1 value
	 */
	public double get_r1()
	{
		return r1;
	}

	/**
	 * just for getting the value of R2
	 * @return to obtain the R2 value
	 */
	public double get_r2()
	{
		return r2;
	}

	/**
	 * taking the values received from the network connection
	 * this method is used for reading the content received from the network
	 * it's similar to a buffer in
	 * @param dis a byte array input that contains the Volt_divider values
	 */
	public Volt_divider(DataInputStream dis)
	{
		try
		{
			//setting the content of the DataInputStream into the attributes of the class
			vcc=dis.readDouble();
			vref=dis.readDouble();
			r1=dis.readDouble();
			r2=dis.readDouble();
		}
		catch(IOException e)
		{
			vcc=0.0;
			vref=0.0;
			r1=0.0;
			r2=0.0;

			e.printStackTrace();
		}
	}

	
	/**
	 * casting the content of the attributes into a array of bytes (DataOutputStream) (writing the content of the attributes into
	 * the DataOutStream, it's similar to a buffer out)
	 * @param dos the buffer out in which the values of the Volt_divider attributes will be written
	 */
	public void toByteArray (DataOutputStream dos)
	{
		try
		{
			dos.writeDouble(vcc);
			dos.writeDouble(vref);
			dos.writeDouble(r1);
			dos.writeDouble(r2);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * putting the attributes values into a string value (returning as String value)
	 */
	public String toString()
	{
		return vcc + " " + vref + " " + r1 + " " + r2;
	}
}	
