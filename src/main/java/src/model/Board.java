package src.model;

import java.util.ArrayList;
import java.lang.Math;
import java.io.Serializable;

public class Board implements Serializable {
	private String name;
	private BoxPoint[][] myField;
	private ArrayList<Ships> oneDecksShip;
	private ArrayList<Ships> twoDecksShip;
	private ArrayList<Ships> threeDecksShip;
	private ArrayList<Ships> fourDecksShip;

	public Board() {
		myField = new BoxPoint[Defolt.COLUMNS][Defolt.ROWS];
		oneDecksShip = new ArrayList<>();
		twoDecksShip = new ArrayList<>();
		threeDecksShip = new ArrayList<>();
		fourDecksShip = new ArrayList<>();
	}

	public ArrayList<Ships> getOneDeckShip(){
		return this.oneDecksShip;
	}
	public ArrayList<Ships> getTwoDeckShip(){
		return this.twoDecksShip;
	}
	public ArrayList<Ships> getThreeDeckShip(){
		return this.threeDecksShip;
	}
	public ArrayList<Ships> getFourDeckShip(){
		return this.fourDecksShip;
	}
	public ArrayList<Ships> getAllShips(){
		ArrayList<Ships> tmp  = new ArrayList<>();
		tmp.addAll(oneDecksShip);
		tmp.addAll(twoDecksShip);
		tmp.addAll(threeDecksShip);
		tmp.addAll(fourDecksShip);
		return tmp;
	}

	public BoxPoint[][] getMyField(){
		return this.myField;
	}
	public void setMyField(BoxPoint[][] myField){
		this.myField = myField;
	}
	public void setMyFieldForView(BoxPoint[][] myField) {
		for(int j = 0; j < Defolt.ROWS; j++){
			for(int i = 0; i < Defolt.COLUMNS; i++){
				if(myField[i][j].getPicture() == Defolt.BANGMISS) {
					ModelBoard.setBox(this.myField,myField[i][j]);
				}
			}
		}
	}
	public void setName(String name) {
		this.name = name;
	}
	public String  getName() {
		return this.name;
	}
	public String toString(){
		StringBuilder str = new StringBuilder();
		for(int j = 0; j < Defolt.ROWS; j++){
			for(int i = 0; i < Defolt.COLUMNS; i++){
				if(myField[i][j].getPicture() == Defolt.EMPTY) {
					str.append("empt ");
				} else if(myField[i][j].getPicture() == Defolt.BANGHIT) {
					str.append("bhit ");
				} else if(myField[i][j].getPicture() == Defolt.SHIP) {
					str.append("ship ");
				} else if(myField[i][j].getPicture() == Defolt.BANGMISS) {
					str.append("miss ");
				} else {
					str.append("bhit ");
				}
			}
			str.append("\n");
		}		
		return str.toString();
	}
	public static String parseTo(BoxPoint[][] myField, int xs, int ys, int xe, int ye){
		StringBuilder str = new StringBuilder();
		if(xs < 0) xs = 0;
		if(ys < 0) ys = 0;
		if(xe > 9) xe = 9;
		if(ye > 9) ye = 9;

		for(int j = xs; j <= xe; j++){
			for(int i = ys; i <= ye; i++){
				if(myField[i][j].getPicture() == Defolt.EMPTY) {
					str.append("empt ");
				} else if(myField[i][j].getPicture() == Defolt.BANGHIT) {
					str.append("bhit ");
				} else if(myField[i][j].getPicture() == Defolt.SHIP) {
					str.append("ship ");
				} else if(myField[i][j].getPicture() == Defolt.BANGMISS) {
					str.append("miss ");
				} else {
					str.append("bhit ");
				}
			}
			//str.append("\n");
		}
		//System.out.println(str.toString());		
		return str.toString();
	}
	public static BoxPoint[][] parseFrom(String str) {
		BoxPoint[][] board = new BoxPoint[Defolt.COLUMNS][Defolt.ROWS];
		for(int j = 0; j < Defolt.ROWS; j++){
			for(int i = 0; i < Defolt.COLUMNS; i++){
				char c = str.charAt(i*5 + j*Defolt.COLUMNS*5);
				if(c == 'e') {
					ModelBoard.setBox(board, new BoxPoint(Defolt.IMAGE_SIZE * i, Defolt.IMAGE_SIZE * j,Defolt.EMPTY));
				} else if(c == 's') {
					ModelBoard.setBox(board, new BoxPoint(Defolt.IMAGE_SIZE * i, Defolt.IMAGE_SIZE * j,Defolt.SHIP));
				} else if(c == 'b') {
					ModelBoard.setBox(board, new BoxPoint(Defolt.IMAGE_SIZE * i, Defolt.IMAGE_SIZE * j,Defolt.BANGHIT, true));
				} else if(c == 'm') {
					ModelBoard.setBox(board, new BoxPoint(Defolt.IMAGE_SIZE * i, Defolt.IMAGE_SIZE * j,Defolt.BANGMISS, true));	
				}
			}
		}
		return board;		
	} 
}