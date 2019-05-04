import java.util.Random;

public class test {
	
//	public static void draw(int row, int column){
	
	public static void main(String[] args){
		long seed=3;
		Random random = new Random(seed);
//		random.setSeed(seed);
		
		for (int i=0 ; i<20 ; i++){
			System.out.println(random.nextInt(3)+1+"  seed= "+seed);
			seed = 2*seed + 1;
			
		}
		
	
		
		
	}
}
