/**
 * EchoClientSocket.java
 * Demonstrates a simple echo client
 */

/**
 * @author Daryl
 *
 */

import java.net.*;
import java.io.*;
import java.util.*;

public class EatSafeClient {
    
public static String coordinates;
static Socket client_socket;

	public static void main(String[] argv) throws IOException
	{
           coordinates = "41.947308,-87.649489;41.945477,-87.646131";
    
		client_socket= new Socket("207.181.245.105", 7000 );
		sendCoordinates();
                readCoordinates();
           
	}
        
        public static void sendCoordinates()
        {
            try{
            DataOutputStream DataOutput = new DataOutputStream(client_socket.getOutputStream()); 
                    DataOutput.writeUTF(coordinates);
            }
            catch(Exception e)
            {
                System.err.println(e);
            }
                
        }
        
        public static void readCoordinates()
        {
            try{
                DataInputStream DataInput = new DataInputStream(client_socket.getInputStream());
                String input;
                String[] inputStore = new String[100];
                String[] inputStore2 = new String[100];
                int i=0;
                int j=0;
        
            
                input = DataInput.readUTF();
            
            if (input.charAt(1) == 'n')
            {
                input = input.substring(3);
                System.out.println("Restaurants in this area " +input);
            }
            else if (input.charAt(1) == 'l')
            {
               input = input.substring(3);
               for(String inputParts: input.split(";"))
               {
                   inputStore[i]=inputParts;
                   for(String tempString: inputStore[i].split(","))
                   {
                        inputStore2[j] = tempString;            
                        j++;
                   }
                   
                   i++;
               }
               
               
               
                for(int k=0; k<inputStore2.length;)
                {
                    System.out.println("Longitude: " +inputStore2[k]);
                    k++;
                    System.out.println("Latitude: " +inputStore2[k]);
                    k++;
                        if("1".equals(inputStore2[k]))
                        {
                            System.out.println("Passed food inspection");
                        }
                        else
                        {
                            System.out.println("Failed food inspection");
                        }
                    k++;
                    System.out.println("Name: " +inputStore2[k]);     
                    k++;
                    System.out.println("Date: " +inputStore2[k]);
                    System.out.println("---------------------------------------");
                    k++;
                }
            }
            
            
            
            DataInput.close();
            }
            catch(Exception e)
            {
                System.err.println(e);
            }
        }
}
