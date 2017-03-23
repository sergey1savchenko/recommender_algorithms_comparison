package general;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.TreeSet;

import Jama.*;
import objectsProcessor.ObjectsReader;
import ufDependencies.*;
import utils.Constants;
import utils.Tools;

public class _SVD_AVRG_GBE {

	static int e = 1000; 														// number of experiments

	static Matrix userItem, UzSz, Vz;
	static HashMap<Integer, TreeSet<Integer>> fu;								// films and their users
	static HashMap<Integer, TreeSet<Integer>> uf;								// users and their films
	static HashMap<Integer, Double> fr;											// films average ratings
	static HashMap<Integer, Double> uad;										// users ratings average deviation
	static double [][] results = new double[e][11];
	static double allFilmsAverageRating;

	public static void main(String[] args) throws IOException, ClassNotFoundException {

		System.out.println("Number of cases to predict: "+e+"...");

		double st, en, real, svdU, svdI, trivial, gbe, svdUError, svdIError, trivialError, gbeError, 
		svdUErrorSum = 0, svdIErrorSum = 0, trivialErrorSum = 0, gbeErrorSum=0;

		st = System.nanoTime();

		userItem = ObjectsReader.get("userItem");
		UzSz = ObjectsReader.get("UzSz");
		Vz = ObjectsReader.get("Vtz").transpose();

		fu = FilmsUsers.getFilmsUsers();
		uf = UsersFilms.getUsersFilms();
		fr = Tools.filmsAverageRatings(userItem);
		uad = Tools.usersAverageDeviation(userItem, fr);

		allFilmsAverageRating = Tools.getAllFilmsAverageRating(fr);

		// test selection
		int n, user, film;
		String [] data = AllExistingRatings.getRatings();
		String temp;

		for (int i = 0; i<e; i++){
			n = (int)Math.floor(Math.random() * data.length);						// random array element
			temp = data[n];															// pair to test
			user = Integer.parseInt(temp.split(",")[0]) - 1;
			film = Integer.parseInt(temp.split(",")[1]) - 1;
			real = Double.parseDouble(temp.split(",")[2]);
			svdU = SVDbyUserSimilarity(user, film);
			svdI = SVDbyItemSimilarity(user, film);
			trivial = getFilmAverageRating(film);
			gbe = globalBaselineEstimate(user, film);
			svdUError = Math.abs(real-svdU);
			svdIError = Math.abs(real-svdI);
			trivialError = Math.abs(real-trivial);
			gbeError = Math.abs(real-gbe);
			double[] tempRow = {real, svdU, svdUError, svdI, svdIError, trivial, trivialError, gbe, gbeError, user, film};
			results[i] = tempRow;
			svdUErrorSum = svdUErrorSum + Math.pow(svdUError, 2);
			svdIErrorSum = svdIErrorSum + Math.pow(svdIError, 2);
			trivialErrorSum = trivialErrorSum + Math.pow(trivialError, 2);
			gbeErrorSum = gbeErrorSum + Math.pow(gbeError, 2);
		}
		Matrix res = new Matrix(results);

		PrintWriter writer = new PrintWriter(Constants.results);
		writer.write(" real U  UE   I  IE  tr  trE gbe gbeE  user  film");
		res.print(writer, 1, 1);

		// RMSE
		double svdURMSE = Math.sqrt((((double)1/e)*svdUErrorSum));
		double svdIRMSE = Math.sqrt((((double)1/e)*svdIErrorSum));
		double trivialRMSE = Math.sqrt((((double)1/e)*trivialErrorSum));
		double gbeRMSE = Math.sqrt((((double)1/e)*gbeErrorSum));

		System.out.println("User-similarity based SVD algorithm RMSE: "+svdURMSE);
		System.out.println("Item-similarity based SVD algorithm RMSE: "+svdIRMSE);
		System.out.println("Trivial prediction by average rating RMSE: "+trivialRMSE);
		System.out.println("Global Baseline Estimate RMSE: "+gbeRMSE);

		writer.write("User-similarity based SVD algorithm RMSE: "+svdURMSE); writer.append('\n');
		writer.write("Item-similarity based SVD algorithm RMSE: "+svdIRMSE); writer.append('\n');
		writer.write("Trivial prediction by average rating RMSE: "+trivialRMSE); writer.append('\n');
		writer.write("Global Baseline Estimate RMSE: "+gbeRMSE);
		writer.close();

		en = System.nanoTime();

		System.out.println("You can see results in 'result.txt' file. Finished in: "+((en-st)/1000000000)+" seconds");
	}

	private static double SVDbyUserSimilarity(int user, int film) throws IOException{
		int [] users = FilmsUsers.getUsersByFilm(fu, film, user);
		int closestUserId = Tools.getClosest(UzSz, users, user);
		if (closestUserId!=(-1)){
			double predictionByUser = userItem.get(closestUserId, film);
			//System.out.println("SVD prediction by User-based similarity: "+predictionByUsers);
			return predictionByUser;
		}else{
			return getFilmAverageRating(film);
		}	
	}

	private static double SVDbyItemSimilarity(int user, int film) throws IOException{
		int [] films = UsersFilms.getFilmsByUser(uf, user, film);
		int closestFilmId = Tools.getClosest(Vz, films, film);
		if (closestFilmId!=(-1)){
			double predictionByItem = userItem.get(user, closestFilmId);
			//System.out.println("SVD prediction by Item-based similarity: "+predictionByItem);
			return predictionByItem;
		}else{
			return getFilmAverageRating(film);
		}
	}

	public static double getFilmAverageRating(int film){
		if(userItem==null)
			try {
				userItem = ObjectsReader.get("userItem");
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
		if (fr==null) fr = Tools.filmsAverageRatings(userItem);
		double trivial = fr.get(film);
		//System.out.println("Trivial prediction by average rating: "+trivial);
		return trivial;
	}

	private static double globalBaselineEstimate(int user, int film) {
		double filmAverage = fr.get(film);
		double filmDifference = allFilmsAverageRating - filmAverage;
		double userDifference = uad.get(user);
		double gbePrediction=0.0;
		if(filmDifference<0){													// Film is better than others in average
			gbePrediction = allFilmsAverageRating + Math.abs(filmDifference);
		} else {																// Film is worse than others in average
			gbePrediction = allFilmsAverageRating - Math.abs(filmDifference);
		}
		if (userDifference<0){ 													// User usually rates less than average rating
			gbePrediction = gbePrediction - userDifference;
		} else {																// User usually rates more than average rating
			gbePrediction = gbePrediction + userDifference;
		}
		//System.out.println("Global Baseline Estimate prediction: "+gbePrediction);
		return gbePrediction;
	}
	
}
