
package src.view.panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ChoosePanel extends JPanel{
    private JRadioButton[] typeShips;
    private String[] nameShips = new String[]{"One deck", "Two decks","Three decks", "Four decks", "Vertical", "Horizontal"};

    private ButtonGroup groupShips = new ButtonGroup();
    private ButtonGroup groupPlace = new ButtonGroup();

    private JButton random = new JButton("Random");
    private JButton removeAll = new JButton("Remove all");
    public ChoosePanel(){
        this.setBounds(450, 140, 500, 600);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        typeShips = new JRadioButton[6];
        for(int i = 0; i < 6; i++){
            typeShips[i] = new JRadioButton();
            if(i == 4 || i == 5) groupPlace.add(typeShips[i]);
            else groupShips.add(typeShips[i]);
        }
        setNameOneDeck(4);
        setNameTwoDeck(3);
        setNameThreeDeck(2);
        setNameFourDeck(1);
        
        this.add(this.random);
        this.add(this.removeAll);
        this.add(this.getShipsPanel());
        this.add(this.getPlacePanel());
    }
    public JPanel getShipsPanel(){
        JPanel ships = new JPanel();
        ships.setBorder(BorderFactory.createTitledBorder("Choose decks:"));
        ships.setLayout(new BoxLayout(ships, BoxLayout.Y_AXIS));
        for(int i = 0;i < 4;i++){
            ships.add(typeShips[i]);
        }
        return ships;
    }
    public JPanel getPlacePanel(){
        JPanel place = new JPanel();
        place.setLayout(new BoxLayout(place, BoxLayout.Y_AXIS));
        place.setBorder(BorderFactory.createTitledBorder("Placement:"));
        typeShips[4].setText(nameShips[4]);
        place.add(typeShips[4]);
        typeShips[5].setText(nameShips[5]);
        place.add(typeShips[5]);
        return place;
    }
    public int getCountDeck(){
        if(typeShips[0].isSelected()) return 1;
        else if(typeShips[1].isSelected()) return 2;
        else if(typeShips[2].isSelected()) return 3;
        else if(typeShips[3].isSelected()) return 4;
        else return 0;
    }   
    public int getPlacement(){
        if(typeShips[4].isSelected()) return 1;
        else if(typeShips[5].isSelected()) return 2;
        else return 0;
    }

    public void setNameOneDeck(int count) {
        String text = nameShips[0] + " - " + count;
        typeShips[0].setText(text);
    }
    public void setNameTwoDeck(int count) {
        String text = nameShips[1] + " - " + count;
        typeShips[1].setText(text);
    }
    public void setNameThreeDeck(int count) {
        String text = nameShips[2] + " - " + count;
        typeShips[2].setText(text);
    }
    public void setNameFourDeck(int count) {
        String text = nameShips[3] + " - " + count;
        typeShips[3].setText(text);
    }
//кнопка случайной расстановки
    public JButton getRandom(MyField myField){
        this.random.addActionListener(new RandomActListener(myField));
        return this.random;
    }
    public static class RandomActListener implements ActionListener{
        MyField random;
        RandomActListener(MyField e){ random = e;}
        @Override
        public void actionPerformed(ActionEvent e){
            this.random.getBoard().getControllerClient().createRandomField();
            this.random.setBoard(this.random.getBoard().getControllerClient().getBoard());
            this.random.callChange();
            this.random.repaint();
        }
    }
//удаление всех кораблей
    public JButton getRemoveAll(MyField myField){
        this.removeAll.addActionListener(new RemoveAllActListener(myField));
        return this.random;
    }
    public static class RemoveAllActListener implements ActionListener{
        MyField remove;
        RemoveAllActListener(MyField e){ remove = e;}
        @Override
        public void actionPerformed(ActionEvent e){
            this.remove.getBoard().getControllerClient().createEmptyField();
            this.remove.setBoard(this.remove.getBoard().getControllerClient().getBoard());
            this.remove.callChange();
            this.remove.repaint();
        }
    }

}