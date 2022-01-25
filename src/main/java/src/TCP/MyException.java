package src.TCP;

public class MyException extends RuntimeException {
	private String error;

	public MyException(String error) {
		this.error = error;
		System.out.println("This is a error...");
	}
	public void getMyMessage() {
		System.err.println("Error: "+ error);
	}
	public String getErrorName() {
		return this.error;
	}
}