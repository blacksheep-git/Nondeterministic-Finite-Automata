package fa.nfa;

import fa.State;
import java.util.HashMap;
import java.util.Set;

/**
 * A NFA State.
 * @author Quinn Shultz, Mateo Ortegon
 */
public class NFAState extends State {
    boolean isFinalState;
    HashMap<Character, Set<NFAState>> transitions; //key value transitions from this NFAState

	/**
	 * Constructs a state that with name
	 * @param name - the name of the state
	 */
    public NFAState(String name){
    	this.name = name;
        this.isFinalState = false;
        this.transitions = new HashMap<>();
    }

    public void setAsFinalState(){
        isFinalState = true;
    }

}
