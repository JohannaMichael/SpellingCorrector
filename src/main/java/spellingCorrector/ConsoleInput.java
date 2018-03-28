package spellingCorrector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleInput {

private BufferedReader in;
	
	public ConsoleInput() {
		in = new BufferedReader(new InputStreamReader(System.in));
	}
	
	public String input() {
		try {
			return in.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "exit!";
	}

	public void close() {
		try {
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
