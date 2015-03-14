package kmeans;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Kmeans {
	
	private List<Double[]> data;
	private List<Double[]> centroids;
	private List<Integer> clusterAssignments;	
	private int k;
	
	private static final int NUM_COLS = 60;
	
	public Kmeans(List<Double[]> data, int k) {
		this.data = data;
		this.k = k;
		this.centroids = new ArrayList<Double[]>();
	}
	
	public void performKMeans() {
		boolean redistributionComplete = false;
		int redistributionCount;
		
		clusterAssignments = new ArrayList<Integer>(data.size());
		for(int i = 0; i < data.size(); i++) {
			clusterAssignments.add(0);		
		}
		
		// select initial cluster means (centroids)
		selectInitialCentroids();
		
		/* Until no distribution occurs do the following:
		 * 1) Assign each object to the cluster based on closest proximity
		 * 2) Update the cluster means for each cluster */
		while(!redistributionComplete) {
			redistributionCount = 0;			
			
			// assign each row to the nearest centroid's cluster
			for(int i = 0; i < data.size(); i++) {
				int newNearest = calcNearestCluster(data.get(i));
				
				if(newNearest != clusterAssignments.get(i)) {
					clusterAssignments.set(i, newNearest);
					redistributionCount++;
				}
			}
			
			if(redistributionCount == 0) {
				redistributionComplete = true;
			} else {
				computeNewCentroids();
			}
		}
	}
	
	/* sets arbitrary middle elements in data to
	 * be the centroids for the first pass */
	private void selectInitialCentroids() {
		Random rand = new Random();
		List<Integer> currCentroids = new ArrayList<Integer>();
		int newCentroid;
		
		for(int i = 0; i < k; i++) {			
			do {
				newCentroid = rand.nextInt() & 599;
			} while(currCentroids.contains(newCentroid));
			
			centroids.add(data.get(i));
		}
	}

	// find the cluster that is closest to the given row
	private int calcNearestCluster(Double[] example) {
		double[] distances = new double[k];
		Double[] currentCentroid;
		
		// find the distance for each centroid
		for(int i = 0; i < k; i++) {
			currentCentroid = centroids.get(i);
			
			// compare each element to get the distance
			distances[i] = getEuclideanDistance(example, currentCentroid);
		}
		
		return findMinDistanceIndex(distances);		
	}
	
	// returns the index of the smallest double
	private int findMinDistanceIndex(double[] distances)  {
		int min = 0;
		
		for(int i = 0; i < distances.length-1; i++) {
			if(distances[i+1] < distances[i]) {
				min = i+1;
			}
		}
		
		return min;
	}
	
	// returns the euclidean distance between two lines
	private double getEuclideanDistance(Double[] vector1, Double[] vector2) {
		double totalDist = 0;
		
		if(vector1.length != vector2.length) {
			System.err.println("Vectors must be the same length");
			return 0;
		}
		
		// add the square of the difference
		for(int i = 0; i < NUM_COLS; i++) {
			totalDist += (vector1[i] - vector2[i]) * (vector1[i] - vector2[i]);
		}
		
		// return the square root of the result
		return Math.sqrt(totalDist);
	}
	
	// get the cluster assignments
	public List<Integer> getAssignments() {
		return this.clusterAssignments;
	}
	
	// generates new centroids after a pass
	public void computeNewCentroids() {		
		
		// create new centroids
		createNewCentroids();
		
		Double[] currRow, currCentroid;
		int clusterAssign, currClusterCount;
		for(int i = 0; i < data.size(); i++) {
			currRow = data.get(i);
			clusterAssign = clusterAssignments.get(i);
			currCentroid = centroids.get(clusterAssign);
			
			// add the current row's values into the centroid
			for(int j = 0; j < currRow.length; j++) {
				currCentroid[j] += currRow[j];
			}			
		}
		
		int[] centroidCounts = getClusterCounts();
		
		// compute the mean by dividing each centroid's values by the
		// number of elements clustered to it.
		for(int i = 0; i < k; i++) {
			currCentroid = centroids.get(i);
			currClusterCount = centroidCounts[i];
			
			for(int j = 0; j < currCentroid.length; j++) {
				currCentroid[j] = currCentroid[j] / currClusterCount;
			}
		}
	}
	
	// creates fresh centroid holders
	private void createNewCentroids() {
		Double[] newCentroid;
		
		for(int i = 0; i < k; i++) {			
			newCentroid = new Double[NUM_COLS];
			
			// initialize double objects
			for(int j = 0; j < NUM_COLS; j++) {
				newCentroid[j] = new Double(0.0);
			}
			
			centroids.add(newCentroid);
		}
	}
	
	// counts the number of items assigned to a cluster 
	public int[] getClusterCounts() {
		int[] counts = new int[k];
		
		for(int i = 0; i < clusterAssignments.size(); i++) {
			counts[clusterAssignments.get(i)]++;
		}
		
		return counts;
	}
	
	// test method
	public void printCentroids() {
		Double[] currCentroid;
		
		for(int i = 0; i < k; i++) {
			currCentroid = centroids.get(i);
			
			for(int j = 0; j < currCentroid.length; j++) {
				System.out.printf("%2f ", currCentroid[j]);
			}
			System.out.println();
		}
		System.out.println();
	}
}
