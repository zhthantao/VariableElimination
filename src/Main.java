import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;


public class Main {

	public static void main(String[] args) {
		String inputFile = args[0];
		String evidFile = args[1];
		GM gm = new GM();
		gm.read(inputFile, evidFile);
		gm.instantiate();
		gm.order();
		
		//Elimination
		Vector<Factor> factors;
		for(Variable v : gm.variables) {
			factors = new Vector<Factor>();
			for(Factor f : gm.factors) {
				if(f.variables.contains(v))
					factors.add(f);
			}
			
			Collections.sort(factors, new Comparator<Factor>(){
				public int compare(Factor f1, Factor f2) {
					return(new Integer(f1.variables.size()).compareTo(new Integer(f2.variables.size())));
				}
			});
			
			//multiply all the factors
			Factor f1  = factors.get(0);
			Factor f2;
			Factor newFactor = new Factor(f1);
			gm.factors.remove(f1);
			for(int i = 1; i < factors.size(); i++) {
				f2 = factors.get(i);
				newFactor = gm.product(newFactor, f2);
				gm.factors.remove(f2);
			}
			
			//summation
			newFactor.sumout(v);			
			gm.factors.add(newFactor);
			
					
		}
		System.out.println(gm.factors.get(0).table.size());
	}
}
