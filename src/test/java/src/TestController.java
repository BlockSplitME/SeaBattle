package src;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import src.model.*;
import src.controller.*;


public class TestController {

//-----------------test----------------	
	@Test
	public void testMove1() {//ход за пределы
		Controller game = new Controller();
		Board board = new Board();
		ModelBoard.createRandomField(board);
		game.connectFirstPlayer(board);
		game.addBot();
		assertEquals(null,game.doMove(-1,0,game.getFirst()));
	}
	@Test 
	public void testMove2() {//второй ход если попал
		Controller game = new Controller();
		Board board = new Board();
		ModelBoard.createEmptyField(board);
		game.connectFirstPlayer(board);
		Ships ship = new Ships(2);
		Board boardtwo = new Board();
		ModelBoard.createEmptyField(boardtwo);
		ship.createShip(0,0,false,boardtwo.getMyField());
		game.connectSecondPlayer(boardtwo);
		ModelBoard.addShip(ship,game.getSecond());
		game.doMove(0,0,game.getFirst());
		assertEquals(null, game.doMove(3,3,game.getFirst()));
	}
	@Test
	public void testMove3() {//второй ход если не попал
		Controller game = new Controller();
		Board board = new Board();
		ModelBoard.createEmptyField(board);
		game.connectFirstPlayer(board);
		Ships ship = new Ships(2);
		Board boardtwo = new Board();
		ModelBoard.createEmptyField(boardtwo);
		ship.createShip(0,0,false,boardtwo.getMyField());
		game.connectSecondPlayer(boardtwo);
		ModelBoard.addShip(ship,game.getSecond());
		game.doMove(5,5,game.getFirst());
		assertEquals(null, game.doMove(3,3,game.getFirst()));
	}
	@Test
	public void testMove4() {//ход если уже победил
		Controller game = new Controller();
		Board board = new Board();
		ModelBoard.createRandomField(board);
		game.connectFirstPlayer(board);
		Ships ship = new Ships(1);
		Board boardtwo = new Board();
		ModelBoard.createEmptyField(boardtwo);
		ship.createShip(0,0,false,boardtwo.getMyField());
		game.connectSecondPlayer(boardtwo);
		ModelBoard.addShip(ship,game.getSecond());
		game.doMove(0,0,game.getFirst());
		assertEquals(null, game.doMove(3,3,game.getFirst()));
	}
	@Test 
	public void testMove5() {//ход по одной клетке
		Controller game = new Controller();
		Board board = new Board();
		ModelBoard.createRandomField(board);
		game.connectFirstPlayer(board);
		game.addBot();
		game.doMove(0,0,game.getFirst());
		assertEquals(null,game.doMove(0,0,game.getFirst()));
	}
	@Test
	public void addShip1() {//вне поля
		Controller game = new Controller();
		Board board = new Board();
		Ships ship = new Ships(1);
		assertEquals(false, ship.createShip(-2,0,false,board.getMyField()));
	}
	@Test
	public void addShip2() {//мешает другой корабль
		Controller game = new Controller();
		Board board = new Board();
		Ships ship1 = new Ships(1);
		Ships ship2 = new Ships(1);
		ModelBoard.createEmptyField(board);
		ship1.createShip(0,0,false,board.getMyField());
		ModelBoard.addShip(ship1,board);
		assertEquals(false, ship2.createShip(1,0,false, board.getMyField()));
	}
}