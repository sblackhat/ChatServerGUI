package finalproject;
 
import java.io.*;
import java.net.*;
import java.sql.SQLException;
import java.util.*;
 

public class UserThread extends Thread {
    private Socket socket;
    private Server server;
    private PrintWriter writer;
    private DBManager db;
    private static int counter = 1;
 
    public UserThread(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }
 
    public void run() {
        try {
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
 
            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);
            db = new DBManager();
            
            printUsers();
 
            String userName = reader.readLine();
            server.addUserName(userName);
            
            //Register username
            try {
            	if(!db.getUser(userName)) {
            		db.insertUser(userName);
            		//Print on the GUI
            		GUI.addClientScreen("Client - "+counter +": " +  userName);
            		counter++;
            	}
            }catch(SQLException e) {
            	System.out.println(e.getMessage());
            }
 
            String serverMessage = "New user connected: " + userName;
            server.broadcast(serverMessage, this);
 
            String clientMessage;
 
            do {
                clientMessage = reader.readLine();
                serverMessage = "[" + userName + "]: " + clientMessage;
                
                server.broadcast(serverMessage, this);
                //Register the message in the DB
                try {
                	db.insertMessage(userName, clientMessage);
                	//Print on GUI
                	GUI.addmessageScreen(serverMessage);
                 }catch(SQLException e) {
                	System.out.println(e.getMessage());
                }
 
            } while (!clientMessage.equals("@disconect"));
 
            server.removeUser(userName, this);
            socket.close();
 
            serverMessage = userName + " has quitted.";
            server.broadcast(serverMessage, this);
 
        } catch (IOException ex) {
            System.out.println("Error in UserThread: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
 
    /**
     * Sends a list of online users to the newly connected user.
     */
    void printUsers() {
        if (server.hasUsers()) {
            writer.println("Connected users: " + server.getUserNames());
        } else {
            writer.println("No other users connected");
        }
        writer.println("Enter the username: " + "\n" + "To exit send @disconect");
    }
 
    /**
     * Sends a message to the client.
     */
    void sendMessage(String message) {
        writer.println(message);
    }
}