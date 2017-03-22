package ufDependencies;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import utils.Constants;

public class AllExistingRatings {
	
	private static String[] result;
	private static boolean flag = false;
	
	private static void read() throws IOException{
		
		ArrayList <String> ratings = new ArrayList<String>();
		String temp;
		
		FileInputStream fstream = new FileInputStream(Constants.ratings);
	    DataInputStream in = new DataInputStream(fstream);
	    BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
	    
	    while((temp=br.readLine())!=null){
	    	ratings.add(temp);
	    }
	    
	    br.close();
	    in.close();
	    fstream.close();
		
	    result = ratings.stream().toArray(String[]::new);
	    flag = true;
	}
	
	public static String [] getRatings(){
		if (flag==false){
			try {
				read();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return result;
		}
		else{
			return result;
		}
	}
	
}
