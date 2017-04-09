package apacheMahout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;

import Jama.Matrix;
import utils.Constants;

public class RecommenderSystemItemBased {
	
	static int e = 5000; 														// number of experiments
	
	public static void main(String[] args){
		
		System.out.println("Number of cases to predict: "+e+"...");
		double st, en;
		
		st = System.nanoTime();
		
		int tasteExceptions = 0, nanExceptions = 0;
		
		DataModel model = null;
		ItemSimilarity similarity = null;
		try {
			DatasetGenerator.create();
			model = new FileDataModel(new File(utils.Constants.ratingsForMahout));
			similarity = new PearsonCorrelationSimilarity(model);
		} catch (IOException | TasteException e) {
			e.printStackTrace();
		}
		

		GenericItemBasedRecommender recommender = new GenericItemBasedRecommender(model, similarity);
		
		int user, film;
		Double real, prediction, error, errorSum = 0.0, RMSE;

		double[][] toProcess = null;
		try {
			toProcess = DatasetGenerator.geetSet();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		for (int i=0; i<e; i++){
			user = (int)toProcess[i][3];
			film = (int)toProcess[i][4];

			try{
				prediction = (double)recommender.estimatePreference(user, film);
			}catch(TasteException e1){
				prediction = general._SVD_AVRG_GBE.getFilmAverageRating((film-1));
				real = toProcess[i][0];
				toProcess[i][1] = prediction;
				error = Math.abs(real-prediction);
				toProcess[i][2] = error;
				errorSum = errorSum + Math.pow((error), 2);
				tasteExceptions++;
			}
			if(prediction.isNaN()){
				prediction = general._SVD_AVRG_GBE.getFilmAverageRating((film-1));
				nanExceptions++;
			}
			real = toProcess[i][0];
			toProcess[i][1] = prediction;
			error = Math.abs(real-prediction);
			toProcess[i][2] = error;
			errorSum = errorSum + Math.pow((error), 2);
		}

		Matrix res = new Matrix(toProcess);

		PrintWriter writer = null;
		try {
			writer = new PrintWriter(Constants.resultsMahoutItemBased);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		writer.write("real pred err   user   film");
		res.print(writer, 1, 1);

		RMSE = Math.sqrt((((double)1/e)*errorSum));

		System.out.println("Number of taste exceptions: "+tasteExceptions);
		System.out.println("Number of NaN exceptions: "+nanExceptions);
		System.out.println("Item-similarity based Mahout predictions RMSE: "+RMSE);
		writer.write("Item-similarity based Mahout predictions RMSE: "+RMSE);
		writer.close();
		
		en = System.nanoTime();
		
		System.out.println("You can see results in 'resultMahoutItemBased.txt' file. Finished in: "+((en-st)/1000000000)+" seconds");
		
	}

}
