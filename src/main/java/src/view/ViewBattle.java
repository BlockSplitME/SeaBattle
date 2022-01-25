package src.view;

import src.TCP.client.ControllerClient;
import src.view.panels.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ViewBattle extends JFrame {
    private ButtonsPanel buttonsPanel;
    private MyField myField;
    private EnemyField enemyField;
    private ControllerClient connect;
    
    public ViewBattle(ControllerClient connect){
	    super("Battle");
        this.connect = connect;
        
        this.createViewFrame();
        this.setBounds(0, 0, 1000, 600);
        ControllerClient.createFrame(this,1000,600,false);
    }
    
    public void createViewFrame(){
        enemyField = new EnemyField(this);
        myField = new MyField(this);
        buttonsPanel = new ButtonsPanel(this);
        enemyField.setBounds(500,40,400,400); 
        
        this.buttonsPanel.getQuit(connect);
        this.add(myField);
        this.add(enemyField);
        this.add(buttonsPanel);
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

    public void repaintEnemy() {
        this.enemyField.repaint();
    }

    public void repaintMy() {
        this.myField.setBoard(connect.getBoard());
        this.myField.repaint();
    }

    public EnemyField getEnemyField() {
        return this.enemyField;
    }

    public MyField getMyField() {
        return this.myField;
    }
    
}