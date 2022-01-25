package src.model;

import java.io.Serializable;

public class BoxPoint implements Serializable{
	private int x;
	private int y;
	private Defolt picture;
	private boolean isToched = false;

	public BoxPoint(int x, int y, Defolt picture){
		this.x = x;
		this.y = y;
		this.picture = picture;
	}
	public BoxPoint(int x,int y, Defolt picture, Boolean touch){
		this.x = x;
		this.y = y;
		this.picture = picture;
		this.isToched = touch;
	}	
	public int getX(){
		return this.x;
	}
	public int getY(){
		return this.y;
	}
	public void setX(int x){
		this.x = x;
	}
	public void setY(int y){
		this.y = y;
	}
	public void setPicture(Defolt picture){
		this.picture = picture;;
	}
	public Defolt getPicture(){
		return this.picture;
	}
	public void doTouch(){
		this.isToched = true;
	}
	//изменения
	public void unTouched(){
		this.isToched = false;
	}
	public boolean getIsTouched(){
	 	return this.isToched;
	}
	public boolean equalsXY(BoxPoint box){
		if(this.getX() == box.getX() && this.getY() == box.getY()){
			return true;
		}
		return false;
	}
	public boolean equals(BoxPoint box){
		if(this.getX() == box.getX() && this.getY() == box.getY() && 
		this.getIsTouched() == box.getIsTouched() && 
		this.getPicture() == box.getPicture()){ return true;}
		return false;
	}
	public String toString(){
		StringBuilder str = new StringBuilder();
		str.append(" " + this.getX()/Defolt.IMAGE_SIZE +"-"+this.getY()/Defolt.IMAGE_SIZE+"\n");
		str.append(" " + this.picture + " " + isToched);
		return str.toString();
	} 
}