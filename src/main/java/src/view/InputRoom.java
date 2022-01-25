package src.view;

import src.TCP.MyException;
import src.TCP.client.ControllerClient;
import src.log.ForLogger;

import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.lang.ClassNotFoundException;

public class InputRoom extends JFrame implements ActionListener {
	private ForLogger log;
	private int room;
	private JFrame forClosing;
	JTextField text;
	JButton buttonCr;
	JButton buttonConnect;
	JLabel label;
	public InputRoom(String title, JFrame e){
		super(title);
		this.forClosing = e;
		try {
			log = new ForLogger("Client");
		} catch(NullPointerException npe) {
			System.out.println("Error logger.");
		} catch(IOException io) {
			System.out.println("Error logger.");
		}
		this.creating();
	}
	private void creating(){
		text = new JTextField(16);
		buttonCr = new JButton("Create");
		buttonConnect = new JButton("Connect");

		label = new JLabel("Enter 4 numb:");
		JPanel panel = new JPanel();
		JPanel forButtons = new JPanel(new FlowLayout(FlowLayout.CENTER));

		text.setText("");

		buttonCr.addActionListener(this);
		buttonConnect.addActionListener(this);
		

		forButtons.add(buttonCr);
		forButtons.add(buttonConnect);
		panel.add(label);
		panel.add(text);
		panel.add(forButtons);
		this.add(panel);
		this.setSize(300, 150);
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		this.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		String s = e.getActionCommand();
		if(text.getText().length() != 4){
			label.setText("Error! Enter 4 numb.");
			text.setText("");
			return;
		}
		try {
			this.room = Integer.parseInt(text.getText());
		} catch(NumberFormatException wrongEnter) {
			log.info("Wrong: "+room);
			this.room = 0;
		}
		try{
			if (s.equals("Create")) {
				new ControllerClient(1, room, log);
				this.dispose();
				this.forClosing.dispose();
				log.info("Enter to create Done!");
			}
		} catch(IOException error){
			log.info(error.getMessage());
			label.setText("Server closed");
			text.setText("");
		} catch(MyException not) {
			not.getMyMessage();
			label.setText(not.getErrorName());
			text.setText(""); 
		} catch(ClassNotFoundException err) {
			log.info(err.getMessage());
			label.setText("Error! Enter 4 numb.");
			text.setText("");
		}
		try {
			if(s.equals("Connect")) {
				new ControllerClient(2, room, log);
				this.dispose();
				this.forClosing.dispose();
				log.info("Done!");
			}
		} catch(ClassNotFoundException err) {
			log.info(err.getMessage());
			label.setText("Error! Enter 4 numb.");
			text.setText("");
		} catch(MyException not) {
			not.getMyMessage();
			label.setText(not.getErrorName());
			text.setText(""); 
		} catch(IOException error){
			log.info(error.getMessage());
			label.setText("Server closed");
			text.setText("");
		}
	}
}