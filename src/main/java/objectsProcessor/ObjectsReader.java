package objectsProcessor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;

import Jama.Matrix;
import utils.Constants;

public class ObjectsReader {

	private static HashMap <String, Matrix> matrices = new HashMap <String, Matrix>();

	public static Matrix get(String object) throws FileNotFoundException, IOException, ClassNotFoundException{
		if (matrices.containsKey(object)){
			return matrices.get(object);
		} else{
			ObjectInputStream in = new ObjectInputStream (new FileInputStream(Constants.objectsSource+object+".dat"));
			matrices.put(object, (Matrix)in.readObject());
			in.close();
			return matrices.get(object);
		}
	}
}
