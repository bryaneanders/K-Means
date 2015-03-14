package kmeans;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileDataExtractor {
	
	private static final int NUM_COLS = 60;
	
	public FileDataExtractor() {}
	
	public List<Double[]> getFileData(Scanner scan) {
		ArrayList<Double[]> fileData = new ArrayList<Double[]>();
		Double[] lineDoubles;
		String[] doubleStrings;
		
		// get the numbers from each line
		while(scan.hasNextLine()) {
			doubleStrings = scan.nextLine().trim().split("\\s+");
			lineDoubles = new Double[NUM_COLS];
			
			// parse out each double value
			for(int i = 0; i < doubleStrings.length; i++) {
				lineDoubles[i] = Double.parseDouble(doubleStrings[i]);
			}
			
			// add the numbers to the list
			fileData.add(lineDoubles);
		}
		
		return fileData;
	}

}
