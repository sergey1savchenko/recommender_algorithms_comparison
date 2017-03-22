package utils;

/* ratings.dat
   dataset includes 1,000,209 ratings of 3,952 movies by 6,040 users
   SVD rank = 3663 ---> 1000
   
   ratingsMahout.dat
   dataset includes 100,004 ratings of 671 movies by 9,066 users
   SVD rank = 671 ----> 500
*/

public class Constants {
	public static final int filmsNumber = 3952;			// 671
	public static final int usersNumber = 6040;			// 9066
	public static final int reductionLevel = 1000;		// 500
	
	public static final String ratings = "data/ratings.dat";
//	public static final String ratings = "data/ratingsSmall.dat";
	
	public static final String objectsSource = "objects/";
//	public static final String objectsSource = "objects/objectsSmall/";
	
	public static final String ratingsForMahout = "data/ratingsMahout.dat";
	public static final String results = "result.txt";
	public static final String resultsMahoutUserBased = "resultMahoutUserBased.txt";
	public static final String resultsMahoutItemBased = "resultMahoutItemBased.txt";
}