package finalproject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import oracle.jdbc.pool.OracleDataSource;

public class DBManager {
	private final String BASE_URL = "jdbc:oracle:thin:@";
	private String hostName;
	private String jdbcUrl = null;
	private String userName = null;
	private String password = null;
	private String port = null;
	private static Connection conn;

	DBManager(String userName, String password, String hostName, String port) {
		this.userName = userName;
		this.password = password;
		this.hostName = hostName;
		this.port = port;
	}
	
	DBManager(){
		
	}
	
	public String getURL() {
		return jdbcUrl;
	}

	private void buildURL() {
		StringBuilder sb = new StringBuilder(BASE_URL);
		sb.append(hostName).append(":").append(port).append(":XE");
		jdbcUrl = sb.toString();
	}

	// Starts the connection with the oracle SQL server
	public void startDBConnection() throws SQLException {
		buildURL();
		OracleDataSource ds;
		ds = new OracleDataSource();
		ds.setURL(jdbcUrl);
		System.out.println(jdbcUrl);
		conn = ds.getConnection(userName, password);

	}
	
	private String getCurrentTime() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		return dtf.format(now);
	}

	// Inserts the user in the DB
	public void insertUser(String userName) throws SQLException {
		StringBuilder sb = new StringBuilder("INSERT INTO \"BENITO_FERRAROGUTIERREZ\".\"USERS\" VALUES ('");
		sb.append(userName).append("' , TO_DATE('").append(getCurrentTime())
		.append("','YYYY-MM-DD HH24:MI:SS'))"); //Date format in SQL
		execute(sb.toString());
	}
	
	public void insertMessage(String userName, String message) throws SQLException {
		StringBuilder sb = new StringBuilder("INSERT INTO \"BENITO_FERRAROGUTIERREZ\".\"MESSAGES\" VALUES ('");
		sb.append(userName).append("' , '").append(message).append("' , ").append("TO_DATE('").append(getCurrentTime())
		.append("','YYYY-MM-DD HH24:MI:SS'))");
		System.out.println(sb.toString());
		execute(sb.toString());
	}
	
	public boolean getUser(String username) throws SQLException {
		StringBuilder query = new StringBuilder(
				"SELECT * FROM \"BENITO_FERRAROGUTIERREZ\".\"USERS\" WHERE USERNAME='");
		query.append(username).append("'");
		ResultSet rset = executeQuery(query.toString());
		if(rset.next()) { return true;}
		return false;
	}
	
	private void execute(String query) throws SQLException {
		Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, 
                ResultSet.CONCUR_READ_ONLY);
		int m = stmt.executeUpdate(query);
		/*if (m==1)
		    System.out.println("inserted successfully : "+query);
		else
		    System.out.println("insertion failed");*/
	}
	
	private ResultSet executeQuery(String query) throws SQLException{
		Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, 
                ResultSet.CONCUR_READ_ONLY);
		return stmt.executeQuery(query);
	}
	
	public void deleteUser(String user) throws SQLException {
		StringBuilder sb = new StringBuilder("DELETE \"BENITO_FERRAROGUTIERREZ\".\"USERS\" WHERE USERNAME = '");
		sb.append(user).append("'");
		//System.out.println(sb.toString());
		execute(sb.toString());
	}
	
	
	public ArrayList<Username> getUsers() throws SQLException{
		String query = "SELECT * FROM \"BENITO_FERRAROGUTIERREZ\".\"USERS\" ORDER BY USERNAME";
		ResultSet rset = executeQuery(query);
		ArrayList<Username> users = new ArrayList<Username>();
		while (rset.next()) {
			users.add(new Username(rset.getString("USERNAME"),
					rset.getString("REGISTRATION_DATE")));
			}
		return users;
	}
	
	public ArrayList<Message> getMessages() throws SQLException{
		String query = "SELECT * FROM \"BENITO_FERRAROGUTIERREZ\".\"MESSAGES\" ORDER BY CREATION_DATE DESC";
		ResultSet rset = executeQuery(query);
		ArrayList<Message> messages = new ArrayList<Message>();
		//int i = 0;
		while (rset.next()) {
		    
			messages.add(new Message(rset.getString("USERNAME"),
					rset.getString("CREATION_DATE"),
					rset.getString("MESSAGE")));
			/*System.out.print(messages.get(i).getUsername() + 
					messages.get(i).getText() + messages.get(i).getDate());
			i++;*/
			}
		return messages;
	}
	
	public static void stop() throws SQLException {
		conn.close(); 
	}

	/*public static void main(String args[]) {
		DBManager db = new DBManager("benito_ferrarogutierrez","ER854","vlab2020.networking.edu.pl","15212");
		try {
			db.startDBConnection();
			//db.insertMessage("Jonh", "HELLO!");
			//db.getMessages();
			//db.deleteUser("Jonh");
			//db.insertUser("Jonh");
			//db.getUsers();
			//System.out.println("Sucess");
			db.getUser("Jonh");
			db.getUser("Mandi");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		
	}*/

}
