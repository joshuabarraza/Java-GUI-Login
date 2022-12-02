import java.awt.*;
import java.awt.event.*;
import java.io.*;


import javax.swing.*;
import javax.swing.border.Border;

public class MainWindow implements ActionListener{
	
	
	private JFrame frame = new JFrame();;
	private JLayeredPane mainPanel;
	private JLabel label;
	private JButton lightMode;
	private JButton darkMode;
	private JLabel toggle;
	private JButton program1;
	private JButton program2;
	private JButton program3;
	
	//=================================================================================

	MainWindow() {
		
		frame.setSize(1000,800);
		frame.setTitle("Application");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		
		
		mainPanel = new JLayeredPane();
		mainPanel.setVisible(true);
		mainPanel.setLayout(null);
		mainPanel.setOpaque(true);
		frame.add(mainPanel);
		
		
		label = new JLabel("Welcome To My Project Center!");
		label.setBounds(0,0,1000,100);
		label.setBackground(Color.LIGHT_GRAY);
		label.setOpaque(true);
		label.setFont(new Font("Sans Serif", Font.PLAIN, 25));
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setVerticalAlignment(JLabel.CENTER);
		mainPanel.add(label, Integer.valueOf(0));
		
		toggle = new JLabel(" Light      Dark ");
		toggle.setFont(new Font("Sans Serif", Font.PLAIN, 12));
		Border blackline = BorderFactory.createLineBorder(Color.black);
		toggle.setBounds(900,10,90,15);
//		toggle.setBackground(Color.BLUE);
//		toggle.setForeground(Color.BLUE);
		toggle.setBorder(blackline);
		mainPanel.add(toggle, Integer.valueOf(1));
		
		lightMode = new JButton("||||");
		lightMode.setBounds(901,11,45,13);
		lightMode.setVisible(false);
		//lightMode.setSelected(true);
		lightMode.addActionListener(new ActionListener(){
			@Override
	        public void actionPerformed( ActionEvent evt ) {
				mainPanel.setBackground(Color.WHITE);
				lightMode.setVisible(false);
				darkMode.setVisible(true);
	        }
			});
		darkMode = new JButton("||||");
		darkMode.setBounds(945,11,44,13);
		frame.getRootPane().setDefaultButton(darkMode);
		darkMode.addActionListener(new ActionListener(){
			@Override
	        public void actionPerformed( ActionEvent evt ) {
				mainPanel.setBackground(new Color(51,51,51));
				darkMode.setVisible(false);
				lightMode.setVisible(true);
	        }
			});
		
		
		mainPanel.add(lightMode, Integer.valueOf(2));
		mainPanel.add(darkMode, Integer.valueOf(3));
		
		
		program1 = new JButton("");
		program1.setBounds(100,100,250,100);
		program1.addActionListener(new ActionListener() {
		public void actionPerformed( ActionEvent evt ) {
			WeightNBal weightNBal = new WeightNBal();
        }
		});
		mainPanel.add(program1);
		
		
		program2 = new JButton("");
		program2.setBounds(375,100,250,100);
		mainPanel.add(program2);
		
		program3 = new JButton("");
		program3.setBounds(650,100,250,100);
		mainPanel.add(program3);
		
		
		frame.setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {		
		
	}


}
