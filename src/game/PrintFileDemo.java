package game;
import java.util.Scanner;
import java.io.File;

public class PrintFileDemo {
		public static void main(String [] args) {
			try {
				Scanner fileScan = new Scanner (new File("textFile.txt"));
				while(fileScan.hasNextLine()) {
					String line = fileScan.nextLine();
					System.out.println(line);
					}
					fileScan.close();
				} catch(Exception ex) {
					ex.printStackTrace();
			}
		}
	}


