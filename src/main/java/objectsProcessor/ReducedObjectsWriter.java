package objectsProcessor;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import Jama.Matrix;
import utils.Constants;

public class ReducedObjectsWriter {
	
	private static int z = Constants.reductionLevel;
	
	public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException {
		
		Matrix userItem, U, S, Vt;
		
		userItem = ObjectsReader.get("userItem");
		U = ObjectsReader.get("U");
		S = ObjectsReader.get("S");
		Vt = ObjectsReader.get("Vt");
		
		int m = userItem.getRowDimension();
	    int n = userItem.getColumnDimension();
	    
	    Matrix Uz = ObjectsWriter.matrixSubset(U, m, z);
	    Matrix Sz = ObjectsWriter.matrixSubset(S, z, z);
	    Matrix Vtz = ObjectsWriter.matrixSubset(Vt, z, n);
	    Matrix UzSz = Uz.times(Sz);
	    
	    ObjectOutputStream 
		out = new ObjectOutputStream(new FileOutputStream(Constants.objectsSource+"Uz.dat", false)); out.writeObject(Uz); out.close();
		out = new ObjectOutputStream(new FileOutputStream(Constants.objectsSource+"Sz.dat", false)); out.writeObject(Sz); out.close();
		out = new ObjectOutputStream(new FileOutputStream(Constants.objectsSource+"Vtz.dat", false)); out.writeObject(Vtz); out.close();
		out = new ObjectOutputStream(new FileOutputStream(Constants.objectsSource+"UzSz.dat", false)); out.writeObject(UzSz); out.close();
	}
}
