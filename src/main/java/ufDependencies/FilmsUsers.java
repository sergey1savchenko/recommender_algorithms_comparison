package ufDependencies;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

public class FilmsUsers {

	public static HashMap<Integer, TreeSet<Integer>> getFilmsUsers() throws IOException {

		//		film id      its users
		HashMap<Integer, TreeSet<Integer>> fu = new HashMap<Integer, TreeSet<Integer>>();
		String temp;

		String [] data = AllExistingRatings.getRatings();

		for (int i=0; i<data.length; i++){
			temp = data[i];
			if (fu.containsKey(Integer.parseInt(temp.split(",")[1]))){
				fu.get(Integer.parseInt(temp.split(",")[1])).add(Integer.parseInt(temp.split(",")[0]));
			} else {
				TreeSet<Integer> tmp = new TreeSet<Integer>();
				tmp.add(Integer.parseInt(temp.split(",")[0]));
				fu.put(Integer.parseInt(temp.split(",")[1]), tmp);
			}
		}

		/*	    try{
	    	PrintWriter out = new PrintWriter("pairs.txt" , "UTF-8");
	        Iterator it = fu.entrySet().iterator();
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
		return fu;
	}

	public static int[] getUsersByFilm(HashMap<Integer, TreeSet<Integer>> map, int filmId, int userId) throws IOException {
		ArrayList<Integer> users = new ArrayList<Integer>();
		TreeSet<Integer> all = map.get(filmId+1);
		for(int i : all){
			if((i-1)!=userId){
				users.add(i-1);													// !!! user id in MATRIX
			}
		}
		int[] res = users.stream().mapToInt(i->i).toArray();
		return res;
	}

}
