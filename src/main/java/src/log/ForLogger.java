package src.log;

import java.util.logging.*;
import java.io.IOException;
public class ForLogger {
	
	private Logger log;

	public ForLogger(String name) throws IOException {
		log =  Logger.getLogger(name);
		FileHandler file = new FileHandler("src/main/java/src/log/logs/" + name);
		log.addHandler(file);
	}

	public synchronized void info(String str) {
		log.info(str);
	}
	public synchronized void warning(String str) {
		log.warning(str);
	}

}