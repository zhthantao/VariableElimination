import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Vector;


public class GM {

	String type;
	int numVariables;
	Vector<Factor> factors;
	Vector<Variable> variables;
	Vector<Evidence> Evidences;
	
	public GM() {
		this.variables = new Vector<Variable>();
		this.factors = new Vector<Factor>();
		this.Evidences = new Vector<Evidence>();
	}
	
	/*
	 * read from the input files
	 */
	public void read(String inputFile, String evidFile) {
		
		try {
			Scanner s = new Scanner(new File(inputFile));
			
			//read data
			this.type = s.next();
			this.numVariables = s.nextInt();
			
			//get variables
			for(int i = 0; i < this.numVariables; i++) {
				this.variables.add(new Variable(s.nextInt(), i));
			}
			//get factors
			int numFactors = s.nextInt();
			for (int i = 0; i < numFactors; i++) {
				this.factors.add(new Factor());
			}
			
			//variables in factors
			int numVarsForFactor = 0;
			for(int i = 0; i < this.factors.size(); i++) {
				numVarsForFactor = s.nextInt();
				for (int j = 0; j < numVarsForFactor; j++) {
					this.factors.get(i).variables.add(this.variables.get(s.nextInt()));
				}
			}
			
			//table in factors
			int tableSize = 0;
			for(int i = 0; i < this.factors.size(); i++) {
				tableSize = s.nextInt();
				for(int j = 0; j < tableSize; j++) {
					this.factors.get(i).table.add(s.nextDouble());
				}
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		try {
			Scanner s = new Scanner(new File(evidFile));
			int numEvidences = s.nextInt();
			Variable v;
			for(int i = 0; i < numEvidences; i++) {
				v = this.variables.get(s.nextInt());
				this.Evidences.add(new Evidence(v, s.nextInt()));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Instantiate
	 */
	public void instantiate() {
		int product;
		int index;
		int tableSize;
	
		//remove evidence from factors
		for(Evidence e : this.Evidences) {
			for(Factor f : this.factors) {
				if(f.variables.contains(e.variable)) {
					index = f.variables.indexOf(e.variable);
					product = 1;
					for(int i = f.variables.size() - 1; i >= 0; i--) {
						if(i < index) break;
						product *= f.variables.get(i).d;
					}
					//remove from table
					tableSize = f.table.size();
					for(int j = tableSize - 1; j >= 0; j--) {
						if((j % product)/ (product /e.variable.d) != e.value) {
							f.table.remove(j);
						}
					}
					f.variables.remove(index);
				}
			}		
		}
	}
	
	/*
	 * get the order using min-degree
	 */
	public void order() {
		int factorSize;
		for(Factor f : this.factors) {
			factorSize = f.variables.size();
			for(int i = 0; i < factorSize - 1; i++) {
				for(int j = i + 1; j < factorSize; j++) {
					f.variables.get(i).neighbours.add(f.variables.get(j));
					f.variables.get(j).neighbours.add(f.variables.get(i));
				}
			}
		}
		
		//sort variables vector. Results in min-degree order
		Collections.sort(this.variables, new Comparator<Variable>() {
			public int compare(Variable a, Variable b) {
				return(new Integer(a.neighbours.size()).compareTo(new Integer(b.neighbours.size())));
			}
		});		
	}
	
	/*
	 * Product
	 */
	public Factor product(Factor f1, Factor f2) {
		Factor newFactor = new Factor();
		
		//get the variable for newFactor.
		Vector<Variable> intersection = new Vector<Variable>();
		for(Variable v2 : f2.variables) {
			for(Variable v1 : f1.variables) {
				newFactor.addVariable(v1);
			}
			newFactor.addVariable(v2);
		}
		
		Collections.sort(newFactor.variables, new Comparator<Variable>() {
			public int compare(Variable v1, Variable v2) {
				return(new Integer(v1.id).compareTo(new Integer(v2.id)));
			}
		});
		
		HashMap<Integer, Integer> values = new HashMap<Integer, Integer>();
		double value1, value2, product;
		for(int i = 0; i < newFactor.table.size(); i++) {
			values = newFactor.getValuesFromIndex(i);
			value1 = f1.table.get(f1.getIndexFromValues(values));
			value2 = f2.table.get(f2.getIndexFromValues(values));
			product = value1 * value2;
			newFactor.table.set(i, product);
		}
		return newFactor;
	}
	
	/*
	 * sumout
	 */
	public void sumout() {
		
	}
	
	public void print() {
		for(Factor f : this.factors) {
			System.out.println("ID:");
			for(Variable v : f.variables) {
				System.out.println(v.id);
			}
			System.out.println("Pr:");
			for(Double d : f.table) {
				System.out.println(d);
			}
		}
	}
	
	public void printEvidence() {
		for(Evidence e : this.Evidences) {
			System.out.println(e.value);
		}
	}
	
}
