package apacheMahout;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.TreeSet;

import ufDependencies.AllExistingRatings;
import utils.Constants;

public class DatasetGenerator {

	private static double [][] toProcess = null;

	public static void create() throws FileNotFoundException{

		toProcess = new double [RecommenderSystemUserBased.e][5];
		String [] data = AllExistingRatings.getRatings();
		int n=0, user, film;
		double real;
		String temp;
		TreeSet<Integer> stringsToSkip = new TreeSet<Integer>();

		for (int i = 0; i<RecommenderSystemUserBased.e; i++){
			while(stringsToSkip.contains(n)){
				n = (int)Math.floor(Math.random() * data.length);						// random array element
			}
			temp = data[n];																// pair to test
			user = Integer.parseInt(temp.split(",")[0]);
			film = Integer.parseInt(temp.split(",")[1]);
			real = Double.parseDouble(temp.split(",")[2]);
			//							prediction error
			double [] tempRow = {real,      0,       0, user, film};
			toProcess[i] = tempRow;
			stringsToSkip.add(n);
		}

		PrintWriter writer = new PrintWriter(Constants.ratingsForMahout);
		for (int i = 0; i<data.length; i++){
			if(!stringsToSkip.contains(i)){
				writer.write(data[i]);
				writer.append('\n');
			}
		}
		writer.close();
	}

	public static double [][] geetSet() throws FileNotFoundException{
		if (toProcess==null){
			create();
			return toProcess;
		}else{
			return toProcess;
		}
	}

}
