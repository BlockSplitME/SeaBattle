package src.model;

import java.util.ArrayList;
import java.lang.Math;
import java.io.Serializable;

public class ModelBoard implements Serializable{
	
	public static BoxPoint getBox(BoxPoint[][] field,int x, int y){
		if(x < 0 || x > 9 || y < 0 || y > 9) 
			return null;
		return field[x][y];
	}
	public static void setBox(BoxPoint[][] field, BoxPoint box){
		int x = box.getX() / Defolt.IMAGE_SIZE;
		int y = box.getY() / Defolt.IMAGE_SIZE;
		field[x][y] = box;
	}
	
	public static Ships searchShip(BoxPoint box, Board field){
		for(Ships ship : field.getAllShips()){
			for(BoxPoint newBox : ship.getBoxesOfShip()){
				if(box.equalsXY(newBox) == true) return ship;
			}
		}
		return null;
	}
//---------------------------------------Создание поля
	public final  static void createEmptyField(Board field){
		//if(field.getOneDeckShip() != null) {
			field.getOneDeckShip().clear();
		//}
		//if(field.getTwoDeckShip() != null) {
			field.getTwoDeckShip().clear();
		//}
		//if(field.getThreeDeckShip() != null) {
			field.getThreeDeckShip().clear();
		//}
		//if(field.getFourDeckShip() != null) {
			field.getFourDeckShip().clear();
		//}

		field.setMyField(new BoxPoint[Defolt.COLUMNS][Defolt.ROWS]);
		for (int i = 0; i < Defolt.ROWS; i++) {
            for (int j = 0; j < Defolt.COLUMNS; j++) {
                setBox(field.getMyField(), new BoxPoint(Defolt.IMAGE_SIZE * i, Defolt.IMAGE_SIZE * j,Defolt.EMPTY));
            }
        }

	}
	public static void createRandomField(Board field){
		createEmptyField(field);
		
		ArrayList<Integer> array = new ArrayList<>();
		for(int i = 0; i < 100; i++) array.add(i);

		while(field.getAllShips().size() <= 10){
			int rand = (int)(Math.random()*(array.size()-1));
			boolean palace = (rand >= 50) ? true : false;
			
			if(field.getFourDeckShip().size() < 1){
				Ships ship4 = new Ships(4);
				if(ship4.createShip(array.get(rand)%10,array.get(rand)/10,true,field.getMyField())){
					addShip(ship4, field);
				}
				array.remove(rand);
				continue;		
			}
			if(field.getThreeDeckShip().size() < 2){
				Ships ship3 = new Ships(3);
				if(ship3.createShip(array.get(rand)%10,array.get(rand)/10,palace,field.getMyField())){
					addShip(ship3, field);
				}
				array.remove(rand);
				continue;
			}
			if(field.getTwoDeckShip().size() < 3){
				Ships ship2 = new Ships(2);
				if(ship2.createShip(array.get(rand)%10,array.get(rand)/10,palace,field.getMyField())) {
					addShip(ship2, field);
				} 
				array.remove(rand);
				continue;
			}
			if(field.getOneDeckShip().size() < 4){
				Ships ship1 = new Ships(1);
				if(ship1.createShip(array.get(rand)%10,array.get(rand)/10,palace,field.getMyField())){
					addShip(ship1,field);
				}
				array.remove(rand);
				
			}
			else{break;}
		}
		//System.out.print("\n \n \n");
	}
//---------------------------------------Взаимодействие с кораблями------------------------------
	public static boolean touchShip(BoxPoint box, Board field){
		int count = 0;
		Ships tmp = searchShip(box, field);
		if(tmp == null) return false;
		for(BoxPoint newBox: tmp.getBoxesOfShip()){
			if(box.equalsXY(newBox)) //System.out.print(tmp.getBoxesOfShip().get(count));
				tmp.getBoxesOfShip().get(count).doTouch();   
			count++;
		}
		return true;
	}
	public static void isShipDead(BoxPoint box, Board field){
		Ships tmp = searchShip(box, field);
		if(tmp == null) return;
		for(BoxPoint newBox: tmp.getBoxesOfShip()){
			if(newBox.getIsTouched() == false){
				return ;
			}
		}
		aroundBang(tmp,field);
		removeShip(searchShip(box, field),field);
	}

	public static void aroundBang(Ships ship, Board field){
		int xStart = ship.getBoxesOfShip().get(0).getX() /Defolt.IMAGE_SIZE;
		int yStart = ship.getBoxesOfShip().get(0).getY() /Defolt.IMAGE_SIZE;
		int xEnd = ship.getBoxesOfShip().get(ship.getCountDecks()-1).getX() /Defolt.IMAGE_SIZE;
		int yEnd = ship.getBoxesOfShip().get(ship.getCountDecks()-1).getY() /Defolt.IMAGE_SIZE;
		BoxPoint[][] myField = field.getMyField();
		for(int i = (xStart-1); i <= (xEnd+1);i++){
			for(int j = (yStart-1); j <= (yEnd+1);j++){
				if(i < 0 || i > 9 || j < 0 || j > 9) continue;
				if(myField[i][j].getIsTouched()) continue;
				myField[i][j].setPicture(Defolt.BANGMISS);
				myField[i][j].doTouch();
			}
		}
		field.setMyField(myField);

	}
	public static String aroundBangForView(int xs, int ys, int xe, int ye, Board field){
		int xStart = xs /Defolt.IMAGE_SIZE;
		int yStart = ys /Defolt.IMAGE_SIZE;
		int xEnd = xe /Defolt.IMAGE_SIZE;
		int yEnd = ye /Defolt.IMAGE_SIZE;
		BoxPoint[][] myField = field.getMyField();
		StringBuilder str = new StringBuilder();
		for(int i = (xStart-1); i <= (xEnd+1);i++){
			for(int j = (yStart-1); j <= (yEnd+1);j++){
				if(i < 0 || i > 9 || j < 0 || j > 9) continue;
				if(myField[i][j].getIsTouched()) continue;
				str.append(i);str.append(j);
			}
		}
		return str.toString();

	}
	public static void addShip(Ships ship, Board field){
		int count = ship.getCountDecks();
		switch(count){
			case 1:{
				if (field.getOneDeckShip().size() < 4) {
                    field.getOneDeckShip().add(ship);
                    for (BoxPoint box : ship.getBoxesOfShip()) {
                    	//System.out.print("----------------------\n"+ box.getX()+","+box.getY() +"\n" );
                        setBox(field.getMyField(), box);
                    }
                } //исключение("Перебор однопалубных. Максимально возможно - 4.");
                break;
            }
            case 2:{
            	if (field.getTwoDeckShip().size() < 3) {
                    field.getTwoDeckShip().add(ship);
                    for (BoxPoint box : ship.getBoxesOfShip()) {
                        setBox(field.getMyField(), box);
                    }
                } //исключение("Перебор однопалубных. Максимально возможно - 4.");
                break;
            }
            case 3:{
            	if (field.getThreeDeckShip().size() < 2) {
                    field.getThreeDeckShip().add(ship);
                    for (BoxPoint box : ship.getBoxesOfShip()) {
                        setBox(field.getMyField(), box);
                    }
                } //исключение("Перебор однопалубных. Максимально возможно - 4.");
                break;
            }
            case 4:{
            	if (field.getFourDeckShip().size() < 1) {
                    field.getFourDeckShip().add(ship);
                    for (BoxPoint box : ship.getBoxesOfShip()) {
                        setBox(field.getMyField(), box);
                    }
                } //исключение("Перебор однопалубных. Максимально возможно - 4.");
                break;
            }
		}	
	}
	public static void removeShip(Ships ship, Board field){
		 if (field.getOneDeckShip().contains(ship)) {
            for (BoxPoint box : ship.getBoxesOfShip()) {
                if(box.getIsTouched()){
                	box.setPicture(Defolt.BANGHIT);
               	}
               	else{
               		box.setPicture(Defolt.EMPTY);
               		setBox(field.getMyField(), box);
               	}
                            	
                field.getOneDeckShip().remove(ship);
            }
        } else if (field.getTwoDeckShip().contains(ship)) {
            for (BoxPoint box : ship.getBoxesOfShip()) {
                if(box.getIsTouched()){
                	box.setPicture(Defolt.BANGHIT);
               	}
               	else{
               		box.setPicture(Defolt.EMPTY);
               		setBox(field.getMyField(), box);
               	}
                 
                field.getTwoDeckShip().remove(ship);
            }
        } else if (field.getThreeDeckShip().contains(ship)) {
            for (BoxPoint box : ship.getBoxesOfShip()) {
                if(box.getIsTouched()){
                	box.setPicture(Defolt.BANGHIT);
               	}
               	else{
               		box.setPicture(Defolt.EMPTY);
               		setBox(field.getMyField(), box);
               	}
               	
                field.getThreeDeckShip().remove(ship);
            }
        } else if (field.getFourDeckShip().contains(ship)) {
            for (BoxPoint box : ship.getBoxesOfShip()) {
                if(box.getIsTouched()){
                	box.setPicture(Defolt.BANGHIT);
               	}
               	else{
               		box.setPicture(Defolt.EMPTY);
               		setBox(field.getMyField(), box);
               	}
               	
                field.getFourDeckShip().remove(ship);
            }
        }
	}
}