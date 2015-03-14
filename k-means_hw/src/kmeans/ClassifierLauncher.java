package kmeans;

import java.io.File;
import java.io.FileNotFoundException;

public class ClassifierLauncher {

	private static File inputFile, outputFile;
	
	private static int k = 6;
	
	public static void main(String[] args) {
		inputFile = new File("hw4_data.txt");
		outputFile = new File("hw4_results.txt");
		
		try {
			ExampleClassifier classifier = new ExampleClassifier(inputFile, outputFile);
			
			classifier.performKMeans();
		
			/* create a csv file for each cluster and an overall results file that 
			 * shows which cluster each row was assigned to.	 */
			classifier.createOutputFiles();
			
			// create images for each cluster
			for(int i = 0; i < k; i++) {
				classifier.createClusterImage(i, new File(".//images//cluster_"+ i +".png"));
			}
			
		} catch (FileNotFoundException e) {
			System.err.println("Error constructing ExampleClassifier");
			e.printStackTrace();
		}
	}
}
