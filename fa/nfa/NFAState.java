package fa.nfa;

import fa.State;

/**
 * A NFA State.
 * @author Quinn Shultz, Mateo Ortegon
 */
public class NFAState extends State {
	
	private String name;
	
	/**
	 * Creates a state that has a unique name.
	 * @param name - the name of the state
	 */
    public NFAState(String name){
    	this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
