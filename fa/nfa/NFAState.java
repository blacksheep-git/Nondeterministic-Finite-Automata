package fa.nfa;

import fa.State;
import java.util.HashMap;
import java.util.Set;

/**
 * A NFA State.
 * @author Quinn Shultz, Mateo Ortegon
 */
public class NFAState extends State {
	boolean isStartState;
    boolean isFinalState;
    HashMap<Character, Set<NFAState>> transitions; //key value transitions from this NFAState

	/**
	 * Creates a state that has a unique name.
	 * @param name - the name of the state
	 */
    public NFAState(String name){
    	this.name = name;
    	this.isStartState = false;
        this.isFinalState = false;
        this.transitions = new HashMap<Character, Set<NFAState>>();
    }

    @Override
    public String toString() {
        return name;
    }

    public void setAsStartState(){
        isStartState = true;
    }

    public boolean isStartState() {
        return this.isStartState;
    }

    public void setAsFinalState(){
        isFinalState = true;
    }

    public boolean isFinalState() {
        return this.isFinalState;
    }
}
