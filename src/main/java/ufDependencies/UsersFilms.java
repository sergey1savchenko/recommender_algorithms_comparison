package ufDependencies;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

public class UsersFilms {

	public static HashMap<Integer, TreeSet<Integer>> getUsersFilms() throws IOException {

		//		user id      his films
		HashMap<Integer, TreeSet<Integer>> uf = new HashMap<Integer, TreeSet<Integer>>();
		String temp;

		String [] data = AllExistingRatings.getRatings();

		for (int i=0; i<data.length; i++){
			temp = data[i];
			if (uf.containsKey(Integer.parseInt(temp.split(",")[0]))){
				uf.get(Integer.parseInt(temp.split(",")[0])).add(Integer.parseInt(temp.split(",")[1]));
			} else {
				TreeSet<Integer> tmp = new TreeSet<Integer>();
				tmp.add(Integer.parseInt(temp.split(",")[1]));
				uf.put(Integer.parseInt(temp.split(",")[0]), tmp);
			}
		}

		/*	    try{
	    	PrintWriter out = new PrintWriter("pairs.txt" , "UTF-8");
	        Iterator it = uf.entrySet().iterator();
	        while (it.hasNext()) {
	        	Map.Entry pair = (Map.Entry)it.next();
	        	out.write((pair.getKey().toString()));
	        	out.write(" : ");
	        	Iterator itSet = ((TreeSet<Integer>) pair.getValue()).iterator();
	        	while (itSet.hasNext()){
	        		out.write(itSet.next().toString());
	        		out.write(", ");
	        	}
	        	out.append('\n');
	        }
	        out.flush();
        }catch(IOException ex){
        	System.out.println(ex.getMessage());
        }
		 */
		return uf;
	}

	public static int[] getFilmsByUser(HashMap<Integer, TreeSet<Integer>> map, int userId, int filmId) throws IOException {
		ArrayList<Integer> films = new ArrayList<Integer>();
		TreeSet<Integer> all = map.get(userId+1);
		for(int i : all){
			if((i-1)!=filmId){
				films.add(i-1);													// !!! film id in MATRIX
			}
		}
		int[] res = films.stream().mapToInt(i->i).toArray();
		return res;
	}

}
