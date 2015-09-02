import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;


public class Server implements Runnable
{
	private int port_number;
	private ServerSocket server_socket;
	private Socket client_socket;
	private DataOutputStream output;
	private DataInputStream input;
	private String coordString = "";
	private Point[] coords = new Point[2];
	private ReadCSV restaurantList;
	//private String restaurants;
	
	public Server(int port, ReadCSV csv) throws IOException 
	{
		port_number = port;
		server_socket = new ServerSocket(port_number);
		restaurantList = csv;
	}
	
	public ServerSocket getServerSocket()
	{
		return server_socket;
	}
	
	public void setSocket(HashMap<Integer,Socket> sockets, Integer socketNum)
	{
		client_socket = sockets.get(socketNum);
		try 
		{
			output = new DataOutputStream(client_socket.getOutputStream());
			input = new DataInputStream(client_socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setSocket(Socket client)
	{
		client_socket = client;
		try 
		{
			output = new DataOutputStream(client_socket.getOutputStream());
			input = new DataInputStream(client_socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void listen() throws IOException
	{
		BufferedReader reader = new BufferedReader( 
				 new InputStreamReader(client_socket.getInputStream()) );
		String input_line = reader.readLine();
		System.out.println( "Received from client: " );
		System.out.println( "   " + input_line );
		PrintWriter output = new PrintWriter( client_socket.getOutputStream(), true );
		output.println(input_line);
	}
	
	public void readLocations() throws IOException
	{
		/*Function flow: 
		 * read four UTF strings from client
		 * parse each string and store in coordinate objects
		 * pass the coordinates to function for determining restaurant objects
		 * write the size of the restaurant arraylist to client
		 * write out each object to client
		 */
		//while(true)
		//{
			
			coordString = input.readUTF();
			
			//if(coordString.equals("exit"))
				//break;
			
			String[] coordinatePair = new String[2];
			String[] coordinate = new String[2];
			
			coordinatePair = coordString.split(";");
			
			coordinate = coordinatePair[0].split(",");
			coords[0] = new Point(
					Double.parseDouble(coordinate[0]),Double.parseDouble(coordinate[1]));
			
			coordinate = coordinatePair[1].split(",");
			coords[1] = new Point(
					Double.parseDouble(coordinate[0]),Double.parseDouble(coordinate[1]));
			System.out.println(coords[0].lat + " " + coords[0].lang);
			System.out.println(coords[1].lat + " " + coords[1].lang);
		//}
		//TODO: parse each string in rectangle to obtain 2 integers and make a coordinate object
		//out of them
		//TODO: some algorithm to determine which restaurants to send back
	
			//string result = searchRestaurants(coords);
			//output.writeUTF(result);
			System.out.println("Looking for restaurants");
			ArrayList<Restaurant> viewableRestaurants = 
					restaurantList.getViewableRestaurants(coords[0], coords[1]);
			System.out.println("Found restaurants");
					//restaurantList.getViewableRestaurants(new Point(41.927213,-87.651272), new Point(41.925461,-87.648751));
			String answer1 = "-l;44,29,1";
			String answer2 = "-n;39";
			//writeLocations(answer1);
			
			String result = "";
			if(viewableRestaurants.size() > 50)
			{
				result = "-n;" + Integer.toString(viewableRestaurants.size());
			}
			else
			{
				result = "-l;";
				for(Restaurant r : viewableRestaurants)
				{
					result += r.output();
				}
			}
			System.out.println(result);
			output.writeUTF(result);
			//writeLocations(answer2);
			//System.out.println(result);
	}

	public void writeLocations(String restaurants) throws IOException
	{
		output.writeUTF(restaurants);
	}
	
	public void transmitFile()
	{
		byte[] fileBytes = new byte[1500];
		
		int bytesToWrite = 0;
		
		try 
		{
			FileInputStream fileInput = new FileInputStream(new File("filePath"));
			
			while((bytesToWrite = fileInput.read(fileBytes)) != -1)
			{
				output.write(fileBytes,0,bytesToWrite);
			}
			
			fileInput.close();
			output.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch(IOException f)
		{
			f.printStackTrace();
		}
	}
	
	@Override
	public void run()
	{
		try {
			//listen();
			readLocations();
		} catch (IOException e1) {
			
			e1.printStackTrace();
		}
	}

}
