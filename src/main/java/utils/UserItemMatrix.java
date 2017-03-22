package utils;

import java.io.IOException;

import Jama.Matrix;

public class UserItemMatrix {
	
	public static Matrix createUserItemMatrix(int users, int films) throws IOException {
		
		//								 users films
		double [][] values = new double [users][films];
		
		String [] data = ufDependencies.AllExistingRatings.getRatings();
	    
	    for(String temp : data){
	    	//	  |			User ID					      ||		Film ID					    |    |				rating			     |
	    	values[Integer.parseInt(temp.split(",")[0])-1][Integer.parseInt(temp.split(",")[1])-1] = Double.parseDouble(temp.split(",")[2]);
	    }
	    
	    Matrix userItem = new Matrix(values);
	      
	    return userItem;
	}
}