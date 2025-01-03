package utils;

import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class LogGenerator {

	public static void generateLog(Button button) {
		System.out.println(button.getText()+" Button Clicked");
	}

	public static void generateLog(Label label) {
		System.out.println(label.getText()+" Label Clicked");
	}

	public static void generateLog(String text) {
		System.out.println(text+" Clicked");
	}

	public static void generateDatabaseLog(String text) {
		System.out.println(text);
	}
}
