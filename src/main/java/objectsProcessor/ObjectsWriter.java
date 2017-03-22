package objectsProcessor;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import Jama.Matrix;
import Jama.SingularValueDecomposition;
import utils.Constants;
import utils.UserItemMatrix;

public class ObjectsWriter {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		
		Matrix userItem = UserItemMatrix.createUserItemMatrix(Constants.usersNumber, Constants.filmsNumber);

		System.out.println("A = U * S * V^t");
		SingularValueDecomposition svd = userItem.svd();
		Matrix U = svd.getU();
		Matrix S = svd.getS();
		Matrix Vt = svd.getV().transpose();
		
		int m = userItem.getRowDimension();
	    int n = userItem.getColumnDimension();
	    int k = S.rank();
	    System.out.println("S rank is: "+k);
	    
	    Matrix Uk = matrixSubset(U, m, k);
	    Matrix Sk = matrixSubset(S, k, k);
	    Matrix Vtk = matrixSubset(Vt, k, n);
	    Matrix UkSk = Uk.times(Sk);
		
	    ObjectOutputStream 
		out = new ObjectOutputStream(new FileOutputStream(Constants.objectsSource+"userItem.dat", false)); out.writeObject(userItem); out.close();
		out = new ObjectOutputStream(new FileOutputStream(Constants.objectsSource+"U.dat", false)); out.writeObject(U); out.close();
		out = new ObjectOutputStream(new FileOutputStream(Constants.objectsSource+"S.dat", false)); out.writeObject(S); out.close();
		out = new ObjectOutputStream(new FileOutputStream(Constants.objectsSource+"Vt.dat", false)); out.writeObject(Vt); out.close();
		out = new ObjectOutputStream(new FileOutputStream(Constants.objectsSource+"Uk.dat", false)); out.writeObject(Uk); out.close();
		out = new ObjectOutputStream(new FileOutputStream(Constants.objectsSource+"Sk.dat", false)); out.writeObject(Sk); out.close();
		out = new ObjectOutputStream(new FileOutputStream(Constants.objectsSource+"oVtk.dat", false)); out.writeObject(Vtk); out.close();
		out = new ObjectOutputStream(new FileOutputStream(Constants.objectsSource+"UkSk.dat", false)); out.writeObject(UkSk); out.close();
	}
	
	protected static Matrix matrixSubset(Matrix orig, int m, int k) {
      Matrix newMatrix = new Matrix(m, k);
      for(int i = 0; i < m; i++) {
         for(int j = 0; j < k; j++) {
            newMatrix.set(i, j, orig.get(i, j));
         }
      }
      return newMatrix;
	}

}
