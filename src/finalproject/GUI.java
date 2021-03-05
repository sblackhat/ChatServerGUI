package finalproject;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.sql.SQLException;
import java.util.StringTokenizer;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import java.awt.Font;
import javax.swing.JPasswordField;

public class GUI {

	private JFrame frame;
	private JTextField portField;
	private static JTextField messageScreen;
	private JTextField ipField;
	private JPasswordField passwordField;
	private JTextField userField;
	private InetAddress ip;
	private String userName;
	private String password;
	private int port;
	private DBManager db;
	private Server server;
	private static JTextField clientScreen;
	private String fullDialog;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI() {
		initialize();
	}
	
	private boolean userInputChecker() {
		if(userField.getText().isBlank() || portField.getText().isBlank() 
				|| passwordField.getText().isBlank() || ipField.getText().isBlank()) {
			return false;
		}else { return true;}
	}
	
	private void setUpDB() {
		//new DBManager("benito_ferrarogutierrez","ER854","vlab2020.networking.edu.pl","15212");
		db = new DBManager(userField.getText(),passwordField.getText(),
				ipField.getText(),portField.getText());
		try {
			db.startDBConnection();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(frame,
				    "Cannot conect to the DB with the provided credentials.",
				    "Connection error",
				    JOptionPane.WARNING_MESSAGE);
		}
		
	}
	
	public static void addClientScreen(String client) {
		clientScreen.setText(clientScreen.getText() + "\n" + client);
	}

	public static void addmessageScreen(String message) {
		messageScreen.setText(messageScreen.getText() + "\n" + message);
	}
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 586, 401);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(userInputChecker()) {
					setUpDB();
					server = new Server(7878);
					JOptionPane.showMessageDialog(frame,
						    "Database started on port 7878",
						    "Database Started",
						    JOptionPane.DEFAULT_OPTION);
					StartServer task = new StartServer(server);
					task.execute();
				}else {
					JOptionPane.showMessageDialog(frame,
						    "You should fill the empty fields to start the server.",
						    "Empty Fields",
						    JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		btnStart.setBounds(32, 47, 81, 19);
		frame.getContentPane().add(btnStart);
		
		JButton btnStop = new JButton("Stop");
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					DBManager.stop();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnStop.setBounds(32, 15, 81, 19);
		frame.getContentPane().add(btnStop);
		
		portField = new JTextField();
		portField.setBounds(222, 47, 65, 19);
		frame.getContentPane().add(portField);
		portField.setColumns(10);
		
		JLabel lblPort = new JLabel("Port");
		lblPort.setBounds(147, 49, 40, 15);
		frame.getContentPane().add(lblPort);
		
		JLabel lblClientList = new JLabel("Client List");
		lblClientList.setBounds(12, 79, 112, 15);
		frame.getContentPane().add(lblClientList);
		
		JButton btnAll = new JButton("All");
		btnAll.setFont(new Font("Dialog", Font.BOLD, 11));
		btnAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				messageScreen.setText(fullDialog);
			}
		});
		btnAll.setBounds(243, 168, 59, 19);
		frame.getContentPane().add(btnAll);
		
		JButton btnNewButton = new JButton("Select");
		btnNewButton.setFont(new Font("Dialog", Font.BOLD, 11));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String userName= JOptionPane.showInputDialog("Write the username you want to filter the messages: ");
				fullDialog = messageScreen.getText();
				System.out.println(fullDialog);
				StringBuilder sb = new StringBuilder();
				StringTokenizer st = new StringTokenizer(fullDialog);
				String token;
				 while (st.hasMoreTokens()) {
					 token = st.nextToken();
					 if(token.startsWith("[" + userName)) {
						 sb.append(token+" " + st.nextToken() + " ");
					 }
			     }
				messageScreen.setText(sb.toString());
			}
		});
		btnNewButton.setBounds(236, 199, 74, 19);
		frame.getContentPane().add(btnNewButton);
		
		messageScreen = new JTextField();
		messageScreen.setBounds(320, 105, 239, 248);
		frame.getContentPane().add(messageScreen);
		messageScreen.setColumns(10);
		
		ipField = new JTextField();
		ipField.setBounds(222, 15, 114, 19);
		frame.getContentPane().add(ipField);
		ipField.setColumns(10);
		
		JLabel lblDbIp = new JLabel("DB URL");
		lblDbIp.setBounds(147, 17, 70, 15);
		frame.getContentPane().add(lblDbIp);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(428, 46, 121, 19);
		frame.getContentPane().add(passwordField);
		
		JLabel lblNewLabel = new JLabel("Password");
		lblNewLabel.setBounds(366, 49, 70, 15);
		frame.getContentPane().add(lblNewLabel);
		
		userField = new JTextField();
		userField.setBounds(428, 14, 121, 19);
		frame.getContentPane().add(userField);
		userField.setColumns(10);
		
		JLabel lblUsername = new JLabel("Username");
		lblUsername.setBounds(366, 17, 81, 15);
		frame.getContentPane().add(lblUsername);
		
		JLabel lblNewLabel_1 = new JLabel("Messages");
		lblNewLabel_1.setBounds(320, 87, 65, 15);
		frame.getContentPane().add(lblNewLabel_1);
		
		clientScreen = new JTextField();
		clientScreen.setBounds(12, 105, 217, 246);
		frame.getContentPane().add(clientScreen);
		clientScreen.setColumns(10);
	}
}
