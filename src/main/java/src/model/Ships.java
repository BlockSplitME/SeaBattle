package src.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Ships implements Serializable{
	private int countDecks;
	private ArrayList<BoxPoint> boxesOfShip;
	private boolean placement;//false - горизонтальная, true - вертикальная

	public Ships(int countDecks){
		this.countDecks = countDecks;
		this.boxesOfShip = new ArrayList<>(countDecks);
	}
	public int getCountDecks(){
		return this.countDecks;
	}
	public ArrayList<BoxPoint> getBoxesOfShip(){
		return this.boxesOfShip;
	}
	public void setAllPicture(Defolt picture){
		for(int i = 0; i < (boxesOfShip.size()-1);i++){
			boxesOfShip.get(i).setPicture(picture);
		}
	}
	public boolean isVert(){
		return this.placement;
	}
	public void touchVert(){
		this.placement = true;
	}
	public boolean isPlace(Ships ship, BoxPoint[][] field){
		int startX = ((ship.getBoxesOfShip().get(0).getX()/Defolt.IMAGE_SIZE)-1);
		if(startX < 0) startX += 1;

		int startY = ((ship.getBoxesOfShip().get(0).getY()/Defolt.IMAGE_SIZE)-1);
		if(startY < 0) startY += 1;
		int tmp = startY;

		int endX = ((ship.getBoxesOfShip().get(ship.getBoxesOfShip().size()-1).getX()/Defolt.IMAGE_SIZE)+1);
		if(endX >= 10) endX -= 1;
		
		int endY = ((ship.getBoxesOfShip().get(ship.getBoxesOfShip().size()-1).getY()/Defolt.IMAGE_SIZE)+1);
		if(endY >= 10) endY -= 1;
		// System.out.print("Start - "+startX+","+startY+"\nEnd - "+endX+","+endY);
		
		for(;startX <= endX;startX++){
			for(startY = tmp ;startY <= endY; startY++){
				// System.out.print("Check - "+startX+","+startY+"\n");
				if(field[startX][startY].getPicture() == Defolt.SHIP){
					return false; 
				}

			}	
		}

		return true;
	}
	public boolean createShip(int x,int y, boolean placement, BoxPoint[][] field){
		if(x < 0 || x > 9 || y < 0 || y > 9) return false;
		this.placement = placement;
		if(placement == true){
			if(y+this.getCountDecks() <= 10){
				for(int i = 0; i < this.getCountDecks(); i++){
					BoxPoint box = new BoxPoint(x*Defolt.IMAGE_SIZE,(y+i)*Defolt.IMAGE_SIZE, Defolt.SHIP);
						boxesOfShip.add(box);
				}
			}
			else{
				if(y-this.getCountDecks() >= 0){
					for(int i = 0; i < this.getCountDecks(); i++){
						BoxPoint box = new BoxPoint(x*Defolt.IMAGE_SIZE,(y - i)*Defolt.IMAGE_SIZE, Defolt.SHIP);
						boxesOfShip.add(0,box);
					}
				}
			}
		}
		else if(placement == false){
			if(x+this.getCountDecks() <= 10){
				for(int i = 0; i < this.getCountDecks(); i++){
					BoxPoint box = new BoxPoint((x + i)*Defolt.IMAGE_SIZE,y*Defolt.IMAGE_SIZE, Defolt.SHIP);
					boxesOfShip.add(box);
				}
			}
			else{
				if(x - this.getCountDecks() >= 0){
					for(int i = 0; i < this.getCountDecks(); i++){
						BoxPoint box = new BoxPoint((x - i)*Defolt.IMAGE_SIZE,y*Defolt.IMAGE_SIZE, Defolt.SHIP);
						boxesOfShip.add(0,box);
					}
				}
			}
		}
		if(this.isPlace(this,field) == false){
		 	boxesOfShip.clear();
		 	return false;
		}
		return true;
	}
	public String toString(){
		StringBuilder str = new StringBuilder();
		str.append(this.getCountDecks()+"\n");
		return str.toString();
	} 
}