package general;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.TreeSet;

import Jama.Matrix;
import objectsProcessor.ObjectsReader;
import ufDependencies.AllExistingRatings;
import ufDependencies.FilmsUsers;
import ufDependencies.UsersFilms;
import utils.*;

public class weighedSVD {

	static int e = 1000; 														// number of experiments

	static Matrix userItem, Uz, Vz;
	static HashMap<Integer, TreeSet<Integer>> fu;								// films and their users
	static HashMap<Integer, TreeSet<Integer>> uf;								// users and their films
	static double [][] results = new double[e][7];

	public static void main(String[] args) throws IOException, ClassNotFoundException {

		System.out.println("Number of cases to predict: "+e+"...");

		double st, en, real, wSvdU, wSvdI, wSvdUError, wSvdIError, wSvdUErrorSum = 0, wSvdIErrorSum = 0;

		st = System.nanoTime();

		userItem = ObjectsReader.get("userItem");
		Uz = ObjectsReader.get("Uz");
		Vz = ObjectsReader.get("Vtz").transpose();

		fu = FilmsUsers.getFilmsUsers();
		uf = UsersFilms.getUsersFilms();

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
			wSvdU = weighedSVDbyUserSimilarity(user, film);
			wSvdI = weighedSVDbyItemSimilarity(user, film);
			wSvdUError = Math.abs(real-wSvdU);
			wSvdIError = Math.abs(real-wSvdI);
			double[] tempRow = {real, wSvdU, wSvdUError, wSvdI, wSvdIError, user, film};
			results[i] = tempRow;
			wSvdUErrorSum = wSvdUErrorSum + Math.pow(wSvdUError, 2);
			wSvdIErrorSum = wSvdIErrorSum + Math.pow(wSvdIError, 2);
		}
		Matrix res = new Matrix(results);

		PrintWriter writer = new PrintWriter("resultWeighedSVD.txt");
		writer.write("real wU wUE  wI wIE  user  film");
		res.print(writer, 1, 1);

		// RMSE
		double wSvdURMSE = Math.sqrt((((double)1/e)*wSvdUErrorSum));
		double wSvdIRMSE = Math.sqrt((((double)1/e)*wSvdIErrorSum));

		System.out.println("User-similarity based weighed SVD algorithm RMSE: "+wSvdURMSE);
		System.out.println("Item-similarity based weighed SVD algorithm RMSE: "+wSvdIRMSE);

		writer.write("User-similarity based weighed SVD algorithm RMSE: "+wSvdURMSE); writer.append('\n');
		writer.write("Item-similarity based weighed SVD algorithm RMSE: "+wSvdIRMSE);

		writer.close();

		en = System.nanoTime();

		System.out.println("You can see results in 'result.txt' file. Finished in: "+((en-st)/1000000000)+" seconds");
	}

	private static double weighedSVDbyUserSimilarity(int user, int film) throws IOException{
		int [] users = FilmsUsers.getUsersByFilm(fu, film, user);
		double prediction = Tools.getWeighedSVDbyUser(userItem, Uz, users, user, film);
		if (prediction!=(-1)){
			return prediction;
		}else{
			return _SVD_AVRG_GBE.getFilmAverageRating(film);
		}	
	}

	private static double weighedSVDbyItemSimilarity(int user, int film) throws IOException{
		int [] films = UsersFilms.getFilmsByUser(uf, user, film);
		double prediction = Tools.getWeighedSVDbyItem(userItem, Vz, films, film, user);
		if (prediction!=(-1)){
			return prediction;
		}else{
			return _SVD_AVRG_GBE.getFilmAverageRating(film);
		}
	}
	
}
