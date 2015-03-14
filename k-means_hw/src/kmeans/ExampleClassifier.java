package kmeans;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import drawing.CategoryImageBuilder;

public class ExampleClassifier {

	private List<Double[]> data;
	private List<Integer> classificationResults;
	
	private static final int k = 6;
	
	private Scanner scan;
	private PrintWriter print;
	private File outputFile;
	
	public ExampleClassifier(File dataFile, File outputFile) throws FileNotFoundException {
		
		if(dataFile.exists()) {
			 scan = new Scanner(new FileReader(dataFile.toString()));
		} else {
			System.err.println("Invalid data filename. Terminating");
			System.exit(1);
		}
		
		FileDataExtractor extractor = new FileDataExtractor();
		data = extractor.getFileData(scan);
		
		scan.close();
		
		this.outputFile = outputFile;
	}
	
	public void performKMeans() {
		Kmeans km = new Kmeans(data, k);
		km.performKMeans();
		
		this.classificationResults =  km.getAssignments();
	}
	
	public void createOutputFiles() throws FileNotFoundException {
		print = new PrintWriter(outputFile);
		
		// print all results
		printResultsFile();
		print.close();
		
		// print the 6 csv files
		for(int i = 0; i < k; i++) {
			print = new PrintWriter(".//csv//cluster_" + i + ".csv");
			printClusterData(i);
			print.close();
		}
	}
	
	private void printResultsFile() {
		for(int i = 0; i < data.size(); i++) {
			print.println(i + ", " + classificationResults.get(i));
		}
	}
	
	// Print the data for a single cluster to a csv file
	private void printClusterData(int cluster) {
		for(int i = 0; i < data.size(); i++) {
			if(cluster == classificationResults.get(i)) {
				Double[] rowData = data.get(i);
				
				for(int j = 0; j < rowData.length; j++) {
					print.print(rowData[j]);
					
					if(j < rowData.length-1) {
						print.print(",");
					}
				}
				
				print.println();
			}
		}
	}
	
	// create an image that contains all the lines in a single cluster
	public void createClusterImage(int cluster, File imageFile) {
		List<Double[]> categoryRows = new ArrayList<Double[]>();
		
		for(int i = 0; i < data.size(); i++) {
			if(cluster == classificationResults.get(i)) {
				categoryRows.add(data.get(i));
			}
		}
		
		CategoryImageBuilder imageBuilder = new CategoryImageBuilder(categoryRows);
		imageBuilder.createCategoryImage();
		
		try {
			imageBuilder.savePngImage(imageFile);
		} catch (Exception e) {
			System.out.println("Error creating image for cluster: " + cluster);
		}
	}
}
