import java.io.BufferedReader;
import java.io.InputStreamReader;


public class CoordinateTest {
	
	public static void main(String[] args) {		
		try
		{	
			CoordinateSocket client= new CoordinateSocket(7000);
			client.transmit();
			//client.echo(message);
			System.out.println( "Done...exiting..." );
		}
		catch ( Exception e )
		{
			System.out.println( e.getMessage() );
		}
	}

}
