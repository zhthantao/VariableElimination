import java.util.Comparator;


public class VariableComparator implements Comparator<Variable>{

	public int compare(Variable v1, Variable v2) {
		return(new Integer(v1.id).compareTo(new Integer(v2.id)));
	}

}
