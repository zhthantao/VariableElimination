import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.ArrayList;


public class GM {

	String type;
	int numVariables;
	ArrayList<Factor> factors;
	ArrayList<Variable> variables;
	PriorityQueue<Variable> order;

	public GM() {
		this.variables = new ArrayList<Variable>();
		this.factors = new ArrayList<Factor>();
		this.order = new PriorityQueue<Variable>(100, new Comparator<Variable>(){
			public int compare(Variable o1, Variable o2) {
				return(new Integer(o1.neighbours.size()).compareTo(new Integer(o2.neighbours.size())));
			}
		});
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
			
			//add neighbours
			addNeighbours();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		try {
			Scanner s = new Scanner(new File(evidFile));
			int numEvidences = s.nextInt();
			Variable v;
			for(int i = 0; i < numEvidences; i++) {
				v = this.variables.get(s.nextInt());
				v.isEvidence = true;
				v.value = s.nextInt();
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
		for(Variable v : this.variables) {
			if(v.isEvidence) {
				for(Factor f : this.factors) {
					if(f.variables.contains(v)) {
						index = f.variables.indexOf(v);
						product = 1;
						for(int i = f.variables.size() - 1; i >= 0; i--) {
							if(i < index) break;
							product *= f.variables.get(i).d;
						}
						//remove from table
						tableSize = f.table.size();
						for(int j = tableSize - 1; j >= 0; j--) {
							if((j % product)/ (product /v.d) != v.value) {
								f.table.remove(j);
							}
						}
						f.variables.remove(index);
					}
				}
			}
		}
	}

	/*
	 * Product
	 */
	public Factor product(Factor f1, Factor f2) {
		Factor newFactor = new Factor();

		//get the variable for newFactor.
		for(Variable v2 : f2.variables) {
			for(Variable v1 : f1.variables) {
				newFactor.addVariable(v1);
			}
			if(!newFactor.variables.contains(v2))
				newFactor.addVariable(v2);
		}

		Collections.sort(newFactor.variables, new Comparator<Variable>(){
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
		System.out.println();
		return newFactor;
	}
	
	
	public void sumout(Factor f, Variable vout) {
		System.out.println("eliminating v:" + vout.id);
		for(Variable v : f.variables) {
			System.out.print(v.id + " ");
		}
		System.out.println();
		
//		System.out.println("ITable:" + this.table.size());
//		for(Double d : this.table) {
//			System.out.println(d);
//		}
		
		ArrayList<Double> table = new ArrayList<Double>();
		int size = f.table.size();
		int product = size;
		double sum;
		for(Variable v : f.variables) {
			product = product / v.d;
			if(v.equals(vout)) {
				for(int i = 0; i < size / v.d; i++) {
					sum = 0;
					for(int j = 0; j < v.d; j++) {
						sum += f.table.get(i + j * product);
					}
					table.add(sum);
				}
				break;
			}
		}
		f.variables.remove(vout);
		
		//add fill edge
		addFillEdges(vout);

		for(Variable v : f.variables) {
			System.out.print(v.id + " ");
		}
		System.out.println();

//		System.out.println();
//		System.out.println("Table:" + table.size());
//		for(Double d : table) {
//			System.out.println(d);
//		}
		System.out.println();
		f.table = table;
	}
	
	private void addNeighbours() {
		int size;
		Variable current, runner;
		for(Factor f : this.factors) {
			size = f.variables.size();
			for(int i = 0; i < size - 1; i++) {
				current = f.variables.get(i);
				for(int j = i + 1; j < size; j++) {
					runner = f.variables.get(j);
					if(!current.neighbours.contains(runner))
						current.neighbours.add(runner);
					if(!runner.neighbours.contains(current))
						runner.neighbours.add(current);
				}
			}
		}
		for(Variable v : this.variables) {
			if(v.isEvidence == false) {
				order.add(v);
			}
		}
	}
	
	private void addFillEdges(Variable v) {
		int size = v.neighbours.size();
		Variable current, runner;
		for(int i = 0; i < size - 1; i++) {
			current = v.neighbours.get(i);
			order.remove(current);
			for(int j = i + 1; j < size; j++) {
				runner = v.neighbours.get(j);
				if(!current.neighbours.contains(runner) && !runner.neighbours.contains(current)) {
					System.out.println("fill edge added:" + current.id + " " + runner.id);
					current.neighbours.add(runner);
					runner.neighbours.add(current);
				}
			}
			order.add(current);
		}
		System.out.println("end of filledge");
	}
}
