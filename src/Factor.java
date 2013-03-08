/**@author: Ranjit Kumar Parvathaneni
 * @created: 25th February 2013
 * @name: Factor
 */
import java.util.HashMap;
import java.util.ArrayList;


public class Factor {

	ArrayList<Variable> variables;
	ArrayList<LogNumber> table;
	
	public Factor() {
		this.variables = new ArrayList<Variable>();
		this.table = new ArrayList<LogNumber>();
	}
	
	public Factor(Factor f) {
		this.variables = f.variables;
		this.table = f.table;
	}
	
	public void addVariable(Variable v) {
		if(!this.variables.contains(v)) {
			ArrayList<LogNumber> table = new ArrayList<LogNumber>();
			this.variables.add(v);
			int size = this.table.size();
			if(size == 0)
				size = 1;
			for(int i = 0; i <  v.d * size; i++)
				table.add(new LogNumber(1d, false));
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
}
