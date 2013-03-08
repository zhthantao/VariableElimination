/**@author: Ranjit Kumar Parvathaneni
 * @created: 25th February 2013
 * @name: Variable
 */

import java.util.ArrayList;


public class Variable {
	
	int d;	//no of values for this value
	int id; //id of the variable
	boolean isEvidence;	//is evidence or not
	int value;	//evidence value
	ArrayList<Variable> neighbours;	//variable connected by an edge
	
	public Variable(int d, int id) {
		this.d = d;
		this.id = id;
		this.isEvidence = false;
		this.neighbours = new ArrayList<Variable>();
	}
	
	public boolean equals(Variable v) {
		return this.id == v.id;
	}

}
