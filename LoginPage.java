import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.*;


public class LoginPage implements ActionListener{ 
	
	private static JFrame frame = new JFrame();
	private static JLabel userLabel;				//sets private variables for all JLabels, JButtons, & JTextFields
	private static JTextField userText;
	private static JLabel passLabel;
	private static JTextField passText;
	private static JPasswordField passwordField;
	private static JButton b;
	private static JLabel loginStatus;
	private static JButton forgot;
	private static JLabel lastAccess;
	private static JLabel dbStatus;
	private static boolean containsCredentials;
	
	private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu/MM/dd HH:mm:ss");
	private static LocalDateTime now = LocalDateTime.now();
	private static String access = dtf.format(now);
	
	//=================================================================================
	
	LoginPage(){ 
		
		frame.setSize(500,200);									//create frame
		frame.setTitle("SIGN IN TO PROJECT");					//add title
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);						//centers frame on screen
		
		JPanel loginPanel = new JPanel();
		loginPanel.setBackground(new Color(119, 136, 153));
		
		frame.add(loginPanel);
		
		loginPanel.setLayout(null);
		
		userLabel = new JLabel("User");
		userLabel.setFont(new Font("Sans Serif", Font.PLAIN, 11));
		userLabel.setBounds(175, 20, 80, 25);
		loginPanel.add(userLabel);
		//userLabel.setHorizontalAlignment(JLabel.CENTER);
		
		userText = new JTextField(20);
		userText.setBounds(170, 40, 160, 25);
		loginPanel.add(userText);
		//userText.setHorizontalAlignment(JLabel.CENTER);
		
		passLabel = new JLabel("Password");
		passLabel.setFont(new Font("Sans Serif", Font.PLAIN, 11));
		passLabel.setBounds(175, 60, 80, 25);
		loginPanel.add(passLabel);
		
		/*
		
		passText = new JTextField(20);
		passText.setBounds(170, 80, 160, 25);
		loginPanel.add(passText);
		passText.addKeyListener(new KeyAdapter() {

			  public void keyPressed(KeyEvent a) {
			    if (a.getKeyCode()==KeyEvent.VK_ENTER){
			    	LoginPage.buttonAction();
			    }
			  }
			});
			
			*/
		
		passwordField = new JPasswordField(20);
		passwordField.setFont(new Font("Sans Serif", Font.PLAIN, 11));
		passwordField.setBounds(170, 80, 160, 25);
		loginPanel.add(passwordField);
		passwordField.addKeyListener(new KeyAdapter() {

			  public void keyPressed(KeyEvent a) {
			    if (a.getKeyCode()==KeyEvent.VK_ENTER){
			    	LoginPage.buttonAction();
			    }
			  }
			});
		
		b = new JButton("NEXT");
		b.setFont(new Font("Sans Serif",Font.PLAIN, 12));
		b.setBounds(210, 110, 80, 25);
		b.addActionListener(this);
		loginPanel.add(b);
		
		loginStatus = new JLabel("");
		loginStatus.setBounds(5, 130, 300, 25);
		loginPanel.add(loginStatus);
		
		
//		forgot = new JButton("new user/forgot password");
//		forgot.setFont(new Font("Sans Serif", Font.ITALIC, 11));
//		forgot.setBounds(350, 150, 150, 25);
//		loginPanel.add(forgot);
		
		
		lastAccess = new JLabel("Current Time: " + access);
		lastAccess.setFont(new Font("Sans Serif", Font.PLAIN, 10));
		lastAccess.setBounds(320, 145, 250, 25);
		loginPanel.add(lastAccess);
		
		dbStatus = new JLabel("Database connected!");
		dbStatus.setFont(new Font("Sans Serif", Font.PLAIN, 10));
		dbStatus.setBounds(380, 132, 250, 25);
		loginPanel.add(dbStatus);
		String url = "jdbc:mysql://localhost:3306/javabase";
		String username = "java";
		String password = "password";
		boolean status = false;
		try (Connection connection = DriverManager.getConnection(url, username, password)) {
		    //System.out.println("Database connected!");
		    status = true;
		} catch (SQLException e) {
		    throw new IllegalStateException("Cannot connect the database!", e);
		}
		if(status == true) {
			dbStatus.setForeground(Color.GREEN);
		}
		
		
		
		
		frame.setVisible(true);	

	}  
	//=================================================================================

	@Override
	public void actionPerformed(ActionEvent e) {		
		buttonAction();
		
	} 
	
	//=================================================================================
	
	private static boolean buttonAction() {
		
		String user = userText.getText();					//gets text from user and password
		char[] ts = passwordField.getPassword();
		String pass = new String(ts);
		
		
		String url = "jdbc:mysql://localhost:3306/javabase";
		String username = "java";
		String password = "password";

		try (Connection connection = DriverManager.getConnection(url, username, password)) {

            PreparedStatement st = (PreparedStatement) connection
                .prepareStatement("Select username, password from login where username=? and password=?");

            st.setString(1, user);
            st.setString(2, pass);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
            	loginSuccess();
            }
            else {
            	loginFail();
            }
		} catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

		/* 			
		 * Previous method of comparing usernames and passwords from .txt file
		 * 
		try {
			BufferedReader reader = new BufferedReader(new FileReader("user:pass.txt")); //BufferReader to look through list of credentials
			String line;
			boolean containsCredentials = false;
			while((line = reader.readLine()) != null && containsCredentials == false) {	//will look through all lines until null
				if(line.startsWith(user) && line.endsWith(password)) {					//once line contains both user & password:
					loginSuccess();
				}
			}
			if(containsCredentials == false) {											//if false clear text
				loginFail();
			}
			reader.close();
		} 
		catch (IOException f) {
			f.printStackTrace();
		}
		*/
		return true;
		
	}
	
	//=================================================================================
	
	private static void loginSuccess() {
		
		String user = userText.getText();
		
		Timer timer = new Timer(1000, new ActionListener() {
			public void actionPerformed(ActionEvent arg1) {
				loginStatus.setForeground(Color.black);
				loginStatus.setText("Loading Application...");	
			}
		});
		
		Timer intialization = new Timer(2000, new ActionListener() {	//creates timer to announce initialization after 1.5secs
			  @Override
			  public void actionPerformed(ActionEvent arg0) {
				  MainWindow newWindow = new MainWindow();
				  frame.dispose();
			  }
			});
		
		containsCredentials = true;											//1. end while loop
		loginStatus.setForeground(new Color(0, 204, 0));
		loginStatus.setText("Login Successful");							//2. Login Successful
		timer.start();														//3. start first timer
		timer.setRepeats(false);
		intialization.start(); 												//4. start initialization timer			
		intialization.setRepeats(false); 									//do not repeat
		
		access = dtf.format(now);
		//System.out.println("Log time is: " + access);
		AccessLog(user, access);							//*******ACCESS LOG ON**********
		
	}
	
	//=================================================================================
	
	private static void loginFail() {
		userText.setText("");
		passwordField.setText("");
		loginStatus.setForeground(new Color (148, 49, 38));
		loginStatus.setText("Login Unsuccessful, please try again");			//prompt to try again
		
	}
	
	//=================================================================================

	private static void AccessLog(String user, String accessDate) {						//for login systems, being able to see who logged in and when is important
		try {
			File file =new File("accesslog.txt");
			BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));	
			writer.write("\nLast login: " + user + " || " + accessDate);				//appends user and date to file
			writer.write("\n==================================");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//=================================================================================

	
}


