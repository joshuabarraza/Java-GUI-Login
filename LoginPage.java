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
	private static JLabel currTime;
	private static JLabel dbStatus;
	private static boolean containsCredentials;
	
	private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu/MM/dd HH:mm:ss");	//dtf finds current date & time
	private static LocalDateTime now = LocalDateTime.now();
	private static String access = dtf.format(now);							//converts dtf into String
	
	//=================================================================================
	
	LoginPage(){ 
		
		frame.setSize(500,200);							//Create JFrame "frame"
		frame.setTitle("SIGN IN TO PROJECT");						//Add title
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);				//Quit program when exiting frame
		frame.setResizable(false);							//User cannot resize frame
		frame.setLocationRelativeTo(null);						//Centers frame on screen
		
		
		JPanel loginPanel = new JPanel();					//Create JPanel "loginPanel"
		loginPanel.setBackground(new Color(119, 136, 153));				//Customize background color
		frame.add(loginPanel);								//Add JPanel to frame
		loginPanel.setLayout(null);
		
		
		userLabel = new JLabel("User");						//First JLabel "user"
		userLabel.setFont(new Font("Sans Serif", Font.PLAIN, 11));			//Setting font
		userLabel.setBounds(175, 20, 80, 25);						//Setting location & size of JLabel
		loginPanel.add(userLabel);							//Adding to JPanel
		
		
		userText = new JTextField(20);						//Create JTextField for user to type username
		userText.setBounds(170, 40, 160, 25);						//Setting location just below JLabel
		loginPanel.add(userText);							//Adding to JPanel

		
		passLabel = new JLabel("Password");					//Second JLabel "password"
		passLabel.setFont(new Font("Sans Serif", Font.PLAIN, 11));			
		passLabel.setBounds(175, 60, 80, 25);						
		loginPanel.add(passLabel);							

		
		passwordField = new JPasswordField(20);					//Create JPasswordField for user to type password 
											//(will automatically hide input text)
		passwordField.setBounds(170, 80, 160, 25);
		loginPanel.add(passwordField);
		passwordField.addKeyListener(new KeyAdapter() {					//Adding KeyListener so that "Enter" key will cue JButton

			  public void keyPressed(KeyEvent a) {
			    if (a.getKeyCode()==KeyEvent.VK_ENTER){
			    	LoginPage.buttonAction();
			    }
			  }
			});
		
		
		b = new JButton("NEXT");						//Adding JButton "next" that will login user
		b.setFont(new Font("Sans Serif",Font.PLAIN, 12));
		b.setBounds(210, 110, 80, 25);
		b.addActionListener(this);							//Creates ActionListener for JButton that will verify
		loginPanel.add(b);								//credentials and open main program
		
		
		loginStatus = new JLabel("");						//Adding JLabel that will give user feedback about credential validity
		loginStatus.setBounds(5, 130, 300, 25);						//JLabel will be empty until button is clicked
		loginPanel.add(loginStatus);
		
		
		currTime = new JLabel("Current Time: " + access);			//Adding JLabel that will display current date & time
		currTime.setFont(new Font("Sans Serif", Font.PLAIN, 10));
		currTime.setBounds(320, 145, 250, 25);
		loginPanel.add(currTime);
		
		dbStatus = new JLabel("Database connected!");				//Adding JLabel that confirms connection to SQL Database
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
		return true;	
	}
	
	//Without separate database you can also use BufferedReader to compare usernames and passwords from .txt file in the same project folder
		/* 			
		 * 
		 * 
		 * try {
		 *	  BufferedReader reader = new BufferedReader(new FileReader("user:pass.txt")); 		//BufferReader to look through list of credentials
		 *	  String line;
		 *	  boolean containsCredentials = false;
		 *	  while((line = reader.readLine()) != null && containsCredentials == false) {		//will look through all lines until null
		 *		  if(line.startsWith(user) && line.endsWith(password)) {			//once line contains both user & password -> SUCCESS
		 *			  loginSuccess();
		 *		  }
		 *	  }
		 *	  if(containsCredentials == false) {							//if false clear text
		 *		loginFail();
		 *	  }
		 *	  reader.close();
		 * } 
		 * catch (IOException f) {
		 *	f.printStackTrace();
		 * }
		 */
	
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
		
		containsCredentials = true;							//1. end while loop
		loginStatus.setForeground(new Color(0, 204, 0));
		loginStatus.setText("Login Successful");					//2. Login Successful
		timer.start();									//3. start first timer
		timer.setRepeats(false);
		intialization.start(); 								//4. start initialization timer			
		intialization.setRepeats(false); 						//do not repeat
		
		access = dtf.format(now);
		//System.out.println("Log time is: " + access);
		AccessLog(user, access);							//*******ACCESS LOG ON**********
		
	}
	
	//=================================================================================
	
	private static void loginFail() {						//if credentials do not match:
		userText.setText("");								//set username and password fields blank
		passwordField.setText("");
		loginStatus.setForeground(new Color (148, 49, 38));
		loginStatus.setText("Login Unsuccessful, please try again");			//JLabel will appear & prompt user to try again
		
	}
	
	//=================================================================================

	private static void AccessLog(String user, String accessDate) {					//for login systems, being able to see who logged in and when is important
		try {
			File file =new File("accesslog.txt");							//access.txt is used to write data
			BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));	
			writer.write("\nLast login: " + user + " || " + accessDate);				//appends the user and date/time to text
			writer.write("\n==================================");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//=================================================================================

	
}


