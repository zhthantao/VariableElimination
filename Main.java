import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;


public class Main {
	
	public static void main(String[] args) {;
		
		GM gm = new GM();
		String inputFile = args[0];
		String evidFile = args[1];
		
		gm.read(inputFile, evidFile);	//read the data
		gm.instantiate();	//instantiate the evidence
		
		int maxClusterSize = 2;
		/* Elimination */
		ArrayList<Factor> factors;
		
		for(Variable v : gm.variables) {
			System.out.println("order: " + v.id + " size:" + v.neighbours.size());
		}
		
		Variable v;
		while(!gm.order.isEmpty()) {
			v = gm.order.poll();
			factors = new ArrayList<Factor>();
			for(Factor f : gm.factors) {
				if(f.variables.contains(v)) {
					factors.add(f);
					for(Variable v1 : f.variables)
						System.out.print(v1.id + " ");
					System.out.println();
				}
			}
			
			if(factors.size() > 0) {
			
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
				gm.sumout(newFactor, v);
				if(newFactor.variables.size() + 1 > maxClusterSize)
					maxClusterSize = newFactor.variables.size() + 1;
				gm.factors.add(newFactor);
			}
		}
		System.out.println(gm.factors.get(0).table.get(0));
		System.out.println(maxClusterSize);
	}
}
