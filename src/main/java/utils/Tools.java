package utils;

import java.util.HashMap;
import java.util.Map.Entry;

import Jama.Matrix;

public class Tools {

	public static int getClosest(Matrix x, int [] items, int toCompareWithId){
		HashMap<Integer, Double> distances = getDistances(x, items, toCompareWithId);
		if (items.length>0){
			Entry<Integer, Double> min = null;
			for (Entry<Integer, Double> entry : distances.entrySet()) {
				if (min == null || min.getValue() > entry.getValue()) {
					min = entry;
				}
			}
			return min.getKey();
		}else {
			return -1;
		}
	}

	public static double getWeighedSVDbyUser(Matrix userItem, Matrix Uz, int[] users, int user, int film) {
		if (users.length>0){

			HashMap<Integer, Double> distances = getDistances(Uz, users, user);
			Entry<Integer, Double> max = null;
			for (Entry<Integer, Double> entry : distances.entrySet()) {
				if (max == null || max.getValue() < entry.getValue()) {
					max = entry;
				}
			}
			double maxDistance = max.getValue();
			double temp = 0.0;
			double counter = 0.0;

			for (Entry<Integer, Double> entry : distances.entrySet()) {
				temp = temp + ( (double)userItem.get(entry.getKey(), film) * entry.getValue()/maxDistance);
				counter = counter + 1.0;
			}
			return temp/counter;
		}else {
			return -1;
		}
	}

	public static double getWeighedSVDbyItem(Matrix userItem, Matrix Vz, int[] films, int film, int user) {
		if (films.length>0){

			HashMap<Integer, Double> distances = getDistances(Vz, films, film);
			Entry<Integer, Double> max = null;
			for (Entry<Integer, Double> entry : distances.entrySet()) {
				if (max == null || max.getValue() < entry.getValue()) {
					max = entry;
				}
			}
			double maxDistance = max.getValue();

			double temp = 0.0;
			double counter = 0.0;

			for (Entry<Integer, Double> entry : distances.entrySet()) {
				temp = temp + ( (double)userItem.get(user, entry.getKey()) * entry.getValue()/maxDistance);
				counter = counter + 1.0;
			}
			return temp/counter;
		}else {
			return -1;
		}
	}

	public static HashMap<Integer, Double> getDistances(Matrix x, int [] items, int toCompareWithId){
		HashMap<Integer, Double> distances = new HashMap<Integer, Double>();
		double dist;

		for (int i = 0; i<items.length; i++){
			dist = getEuclideanDistance(  x.getArray()[items[i]]  ,  x.getArray()[toCompareWithId])   ;
			//dist = cosineSimilarity(  x.getArray()[items[i]]  ,  x.getArray()[toCompareWithId])   ;
			distances.put(items[i], dist);
		}
		return distances;
	}

	public static double getEuclideanDistance(double[] a, double[] b) {
		double x=0, s;

		for(int i=0; i<a.length; x+=s*s){
			s = a[i] - b[i++];
		}
		return Math.sqrt(x);
	}

	public static double cosineSimilarity(double[] vectorA, double[] vectorB) {
		double dotProduct = 0.0;
		double normA = 0.0;
		double normB = 0.0;
		for (int i = 0; i < vectorA.length; i++) {
			dotProduct += vectorA[i] * vectorB[i];
			normA += Math.pow(vectorA[i], 2);
			normB += Math.pow(vectorB[i], 2);
		}   
		return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
	}

	public static HashMap<Integer, Double> filmsAverageRatings(Matrix userItem){
		HashMap<Integer, Double> filmsAverageRatings = new HashMap<Integer, Double>();
		double temp=0.0;
		double counter=0.0;

		for(int f=0; f<Constants.filmsNumber; f++){
			for(int u=0; u<Constants.usersNumber; u++){
				if(userItem.get(u, f)!=0.0){
					temp = temp + userItem.get(u, f);
					counter = counter + 1;
				}
			}
			double a = (temp / counter);
			if(a != a){																		// a is NaN
				filmsAverageRatings.put(f, 3.0);
			}else{
				filmsAverageRatings.put(f, a);
			}

			temp = 0.0;
			counter = 0.0;
		}

		return filmsAverageRatings;
	}

	public static HashMap <Integer, Double> usersAverageDeviation(Matrix userItem, HashMap<Integer, Double> filmsAverageRatings) {
		HashMap <Integer, Double> usersAverageDeviation = new HashMap<Integer, Double>();
		double temp=0.0;
		double counter=0.0;

		for (int u=0; u<Constants.usersNumber; u++){
			for (int f=0; f<Constants.filmsNumber; f++){
				if(userItem.get(u, f)>0.0){
					temp = temp + (userItem.get(u, f) - filmsAverageRatings.get(f));
					counter++;
				}
			}
			usersAverageDeviation.put(u, (temp / counter));
			temp = 0.0;
			counter = 0.0;
		}

		return usersAverageDeviation;
	}

	public static double getAllFilmsAverageRating(HashMap<Integer, Double> fr) {
		double temp = 0;

		for(int f = 0; f<Constants.filmsNumber; f++) {
			temp = temp + fr.get(f).doubleValue();
		}

		double res = temp / (double)Constants.filmsNumber;
		return res;
	}

}
