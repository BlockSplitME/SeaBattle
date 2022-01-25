package src;

import src.view.MainMenu;

public class MainClient {
	
	public static void main(String[] args) {
		try {
			MainMenu start = new MainMenu("Battle Sea");
		} catch (Exception e) {
			System.out.println("Error");
		}
	}
}
