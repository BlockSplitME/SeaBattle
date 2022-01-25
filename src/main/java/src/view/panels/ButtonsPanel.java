package src.view.panels;

import src.TCP.client.ControllerClient;
import java.io.IOException;

import src.view.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ButtonsPanel extends JPanel{
	private String[] nameButtons = new String[]{"History","Save","Quit","Ready!"};
   private JButton[] buttons;
   private JFrame view;

   public ButtonsPanel(ViewBattle view){ 
      this.createButtonsPanel();
     	this.add(buttons[2]);
   }
   public ButtonsPanel(ViewPreparationForBattle view){
   	this.createButtonsPanel();
   	this.add(buttons[2]);
   	this.add(buttons[3]);
   }
   
   public void createButtonsPanel(){
   	this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
     	this.setBounds(40, 450, 290, 100);
      this.view = view;
      buttons = new JButton[4];
      for(int i = 0; i < 4; i++){
      	buttons[i] = new JButton(nameButtons[i]);
      }
   }
   //ready
	public JButton getReady(ViewPreparationForBattle view){
   	this.buttons[3].addActionListener(new StartActListener(view));
    	return this.buttons[3];
   }
   public static class StartActListener implements ActionListener{
		ViewPreparationForBattle view;
		StartActListener(ViewPreparationForBattle e){view = e;}
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				if(view.getControllerClient().ready()) {
					//JOptionPane.showMessageDialog(null,"Start game", "Start game", JOptionPane.ERROR_MESSAGE);
					//view.dispose();
				} else {
					JOptionPane.showMessageDialog(null,"Not all ships", "Not all ships", JOptionPane.ERROR_MESSAGE);
				}
			} catch (IOException error) {

			} catch (ClassNotFoundException classnotfound) {

			}
		}
	} 


	//quit
   public JButton getQuit(ControllerClient c){
   	buttons[2].addActionListener(new QuitActListener(c));
    	return this.buttons[2];
   }
   public static class QuitActListener implements ActionListener{
		ControllerClient connect;
		QuitActListener(ControllerClient e){ connect = e;}
		@Override
		public void actionPerformed(ActionEvent e){
			try {
				connect.breakConnect();
			} catch(IOException error) {
				System.out.println("Server closed.");
			}
		}
	}
}