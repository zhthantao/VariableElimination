import java.util.HashMap;
import java.util.Vector;


public class Factor {

	Vector<Variable> variables;
	Vector<Double> table;
	
	public Factor() {
		this.variables = new Vector<Variable>();
		this.table = new Vector<Double>();
	}
	
	public Factor(Factor f) {
		this.variables = f.variables;
		this.table = f.table;
	}
	
	public void addVariable(Variable v) {
		if(!this.variables.contains(v)) {
			Vector<Double> table = new Vector<Double>();
			this.variables.add(v);
			int size = this.table.size();
			if(size == 0)
				size = 1;
			for(int i = 0; i <  v.d * size; i++)
				table.add(1d);
			this.table = table;
		}
	}
	
	public HashMap<Integer, Integer> getValuesFromIndex(int index) {
		HashMap<Integer, Integer> values = new HashMap<Integer, Integer>();
		int product = this.table.size();
		int remainder, q, blockSize;
		for(Variable v : this.variables) {
			blockSize = product / v.d;
			q = index / blockSize;
			values.put(v.id, q);
			remainder = index % blockSize;
			index = remainder;
			product = blockSize;
		}
		return values;
	}
	
	public int getIndexFromValues(HashMap<Integer, Integer> values) {
		int index = 0;
		int product = this.table.size();
		int blockSize;
		for(Variable v : this.variables) {
			blockSize = product / v.d;
			index += values.get(v.id) * blockSize;
			product = blockSize;
		}
		return index;
	}
	
	public void sumout(Variable vout) {
		System.out.println("eliminating v:" + vout.id);
		for(Variable v : this.variables) {
			System.out.println("Variable: " + v.id);
		}
		Vector<Double> table = new Vector<Double>();
		int product = this.table.size();
		int blockSize;
		double sum;
		for(Variable v : this.variables) {
			blockSize = product / v.d;
			if(v.equals(vout)) {
				for(int i = 0; i < blockSize; i++) {
					sum = 0;
					for(int j = 0; j < v.d; j++) {
						sum += this.table.get(i + j * blockSize);
					}
					table.add(sum);
					System.out.println("Sum:" + sum);
				}
				break;
			}
			else
				product = blockSize;
		}
		this.variables.remove(vout);

		System.out.println("ITable:");
		for(Double d : this.table) {
			System.out.println(d);
		}
		System.out.println("Table:");
		for(Double d : table) {
			System.out.println(d);
		}
		this.table = table;
	}
}
