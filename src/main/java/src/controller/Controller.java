package src.controller;

import src.model.*;
import java.util.ArrayList;
import java.lang.Math;
import java.io.Serializable;

public class Controller implements Serializable{
	private Board player1;
	private Board player2;
	private boolean movePlayer1;
//--------------------------------Создание--------------------------------------
	public Controller() {
		movePlayer1 = true;
	}
	public void connectFirstPlayer(Board p) {
		p.setName("first");
		this.player1 = p;
	}
	public void connectSecondPlayer(Board p) {
		p.setName("second");
		this.player2 = p;
	}
	public Board getPlayer(String name){
		if(player1.getName().equals(name)) return player1;
		return player2;
	}
	public boolean getMove(){
		return this.movePlayer1;
	}
	public boolean canDoMove(Board p){
		if(!(isWin().equals("0"))){
			return false;
		}
		if(p.getName().equals(player1.getName())){
			if(movePlayer1) return true;
		}
		else{
			if(movePlayer1 == false) return true;
		}
		return false;
	}
	public void addBot(){
		this.player2 = new Board();
		player2.setName("bot");
		ModelBoard.createRandomField(player2);
	}
	public Board getFirst(){
		return this.player1;
	}
	public Board getSecond(){
		return this.player2;
	}
	public Board getRival(Board p){
		if(this.getFirst().getName().equals(p.getName())) return this.getSecond();
		else return this.getFirst();
	}
//--------------------------------Игра--------------------------------------	
	public BoxPoint doMove(int x, int y, Board p){
    	if(this.canDoMove(p)== false) return null;
    	if(x < 0 || x > 9 || y < 0 || y > 9) return null;
    	BoxPoint box = this.shot(x,y,p);
    	if(box == null) return null;
    	//выстрел сделан
    	if(ModelBoard.touchShip(box, this.getRival(p)) == false){
    		//не попал по кораблю
			if(this.getRival(p).getName().equals("bot")){
				//если второй игрок бот
				while(true){
					BoxPoint newBox = this.finishingOff(this.getRival(p));
					if(newBox == null) continue;
					if(ModelBoard.touchShip(newBox,p) == false) break;
					ModelBoard.isShipDead(newBox, p);
				}
			}
			else{
				if(p.getName().equals(player1.getName())){ movePlayer1 = false;}
				else {movePlayer1 = true;}
    		}
    	}
    	ModelBoard.isShipDead(box, this.getRival(p));
    	
    	//System.out.println(box);
    	//System.out.println(this.getRival(p));
    	
    	return box;
	}
	public String isWin(){
		if(player1.getAllShips().size() == 0) return player2.getName();
		else if(player2.getAllShips().size() == 0) return player1.getName();
		else return "0";
	}
	
    public BoxPoint shot(int x, int y, Board p){//кто стреляет
		BoxPoint tmpBox  = new BoxPoint(x*Defolt.IMAGE_SIZE, y*Defolt.IMAGE_SIZE,Defolt.BANGHIT,true);
		for(BoxPoint[] b : this.getRival(p).getMyField()){
			for(BoxPoint box: b){
				if(box.equalsXY(tmpBox)){
					if(box.getIsTouched()){return null;}
					Defolt search = box.getPicture();
					switch(search){
						case EMPTY:{
							BoxPoint forSet = new BoxPoint(Defolt.IMAGE_SIZE * x, Defolt.IMAGE_SIZE * y,Defolt.BANGMISS,true);
							ModelBoard.setBox(this.getRival(p).getMyField(),forSet);
							return forSet;
						}
						case SHIP:{
							ModelBoard.setBox(this.getRival(p).getMyField(),tmpBox);
							return tmpBox;
						}
					}
				}
			}	
		}
		return null;
	}
	public BoxPoint randomShot(Board p){
		ArrayList<Integer> array = new ArrayList<>();
		for(int i = 0; i < Defolt.COLUMNS;i++){
			for(int j = 0; j < Defolt.ROWS; j++){
				if(this.getRival(p).getMyField()[i][j].getIsTouched() == false){
					array.add( (this.getRival(p).getMyField()[i][j].getX()/Defolt.IMAGE_SIZE) + (this.getRival(p).getMyField()[i][j].getY()*10/Defolt.IMAGE_SIZE));
				}
			}
		}
		int rand = (int)(Math.random() * (array.size()-1));
		//System.out.print(rand);
		return (this.shot(array.get(rand)%10,array.get(rand)/10,p));
		
	}
	public BoxPoint finishingOff(Board p){
		for(BoxPoint[] b: this.getRival(p).getMyField()){
			for(BoxPoint box : b){
				if(box.getPicture() != Defolt.EMPTY && box.getPicture() != Defolt.BANGMISS && box.getPicture() != Defolt.SHIP){
					//System.out.print(box.getX()/Defolt.IMAGE_SIZE +"-"+box.getY()/Defolt.IMAGE_SIZE);
					int count = 0;
					Ships ship = ModelBoard.searchShip(box,player1);
					if(ship == null){
						continue;
					}
					int rand = (int)(Math.random()*4);
					for(BoxPoint newBox: ship.getBoxesOfShip()){
						if(newBox.getIsTouched()) count++;
					}
					int y = box.getY() / Defolt.IMAGE_SIZE;
					int x = box.getX() / Defolt.IMAGE_SIZE; 
					if(count == 1){
						switch(rand){
							case 0:{
								if((x+1) >= 10) rand++;
								else{
									return this.shot(x+1,y,p);
								}
							}
							case 1:{
								if((x-1) < 0){
									return this.shot(x+1,y,p);
								}
								else{
									return this.shot(x-1,y,p);
								}
							}
							case 2:{
								if((y+1) >= 10){
									rand++;
								}
								else{
									return this.shot(x,y+1,p);
								}
							}
							case 3:{
								if((y-1) < 0){
									return this.shot(x,y+1,p);
									
								}
								else{
									return this.shot(x,y-1,p);
									
								}
							}
						}
					}
					else{
						int i=1;
						if(ship.isVert()){

							while((y+i) < 10){
								
								BoxPoint check = ModelBoard.getBox(this.getRival(p).getMyField(),x,y+i);
								if(check.getPicture() == Defolt.BANGMISS && check.getIsTouched()) break;
								box = this.shot(x,y+i,p);
								if(box != null) return box;
								i++;
							}
							i=1;
							while((y-i) >= 0){
								box = this.shot(x,y-i,p);
								if(box != null) return box;
								i++;
							}
						}
						else{
							while((x+i) < 10){
								
								BoxPoint check = ModelBoard.getBox(this.getRival(p).getMyField(),x+i,y);
								if(check.getPicture() == Defolt.BANGMISS && check.getIsTouched()) break;
								box = this.shot(x+i,y,p);
								if(box != null) return box;
								i++;
							}
							i=1;
							while((x-i) >= 0){
								box = this.shot(x-i,y,p);
								if(box != null) return box;
								i++;
							}
						}
					}
					
				}
			}
		}
		return this.randomShot(p);
	}
}