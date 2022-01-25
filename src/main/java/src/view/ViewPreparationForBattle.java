package src.view;

import src.TCP.client.ControllerClient;
import src.view.panels.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ViewPreparationForBattle extends JFrame {
	
	private ControllerClient connect;
	private ChoosePanel choosePanel = new ChoosePanel();
	private ButtonsPanel buttonsPanel;
	private MyField myField;

	public ViewPreparationForBattle(ControllerClient connect) {
		super("Sea");
		this.connect = connect;
		myField = new MyField(this);
		buttonsPanel = new ButtonsPanel(this);
		this.buttonsPanel.getReady(this);		
		//кнопки
		this.choosePanel.getRemoveAll(this.myField);
		this.choosePanel.getRandom(this.myField);
		this.buttonsPanel.getQuit(connect);
		//панели
		this.add(choosePanel, BorderLayout.EAST);
		this.add(buttonsPanel, BorderLayout.PAGE_END);
		this.add(myField, BorderLayout.LINE_START);
		this.setBounds(0, 0, 700, 600);
		ControllerClient.createFrame(this,700,600,false);
	}

	public void exitRival(boolean mode) {
       if(mode) {
       		JOptionPane.showMessageDialog(null,"You rival exit.", "You rival exit.", JOptionPane.ERROR_MESSAGE);
       }
       this.dispose();
       new MainMenu("Sea Battle");
    }

	public ControllerClient getControllerClient() {
		return this.connect;
	}

    public ChoosePanel getChoosePanel() {
		return this.choosePanel;
	}
	
}