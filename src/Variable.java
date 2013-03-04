import java.util.HashSet;


public class Variable {
	
	int d;	//no of values for this value
	boolean isEvidence;
	int id; //id for this variable
	HashSet<Variable> neighbours;
	int sortIndex;
	
	public Variable(int d, int id) {
		this.d = d;
		this.id = id;
		this.neighbours = new HashSet<Variable>();
	}
	
	public boolean equals(Variable v) {
		return this.id == v.id;
	}

}
