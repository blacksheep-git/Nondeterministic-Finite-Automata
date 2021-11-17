package fa.nfa;

import fa.State;
import fa.dfa.DFA;
import fa.dfa.DFAState;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Nondeterministic Finite Automata
 * @author Quinn Shultz, Mateo Ortegon
 */
public class NFA implements NFAInterface {
    private Set<NFAState> restStates;
    private Set<NFAState> finalStates;
    private Set<Character> alphabet;

    private NFAState startState;

    /**
     * Construct a new Nondeterministic Finite Automata
     */
    public NFA(){
        restStates = new LinkedHashSet<NFAState>();
        finalStates = new LinkedHashSet<NFAState>();
        alphabet = new LinkedHashSet<Character>();
    }

    @Override
    public void addStartState(String name) {
        NFAState s = checkIfExists(name);
        if(s == null){
            s = new NFAState(name);
            addState(s.toString());
        } else {
       	 System.out.println("WARNING: A state with name " + name + " already exists in the NFA");
        }
        startState = s;
    }

    @Override
    public void addState(String name) {
    	NFAState s = checkIfExists(name);
    	 if(s == null){
             s = new NFAState(name);
             restStates.add(s);
         } else {
        	 System.out.println("WARNING: A state with name " + name + " already exists in the NFA");
         }
    }

    @Override
    public void addFinalState(String name) {
    	NFAState s = checkIfExists(name);
    	if(s == null){
            s = new NFAState(name);
            addState(s.toString());
        } else {
       	 System.out.println("WARNING: A state with name " + name + " already exists in the NFA");
        }
    	finalStates.add(s);
    }

    @Override
    public void addTransition(String fromState, char onSymb, String toState) {
    	// TODO: Complete this method
    }

    @Override
    public Set<NFAState> getStates() {
        return restStates;
    }

    @Override
    public Set<NFAState> getFinalStates() {
    	Set<NFAState> ret = new LinkedHashSet<NFAState>();
    	for (NFAState s : restStates) {
    		if (s.isFinal()) {
    			ret.add(s);
    		}
    	}
        return ret;
    }

    @Override
    public State getStartState() {
        return startState;
    }

    @Override
    public Set<Character> getABC() {
    	// TODO: Complete this method
        return null;
    }

    @Override
    public DFA getDFA() {
    	// TODO: Complete this method
        return null;
    }

    public Set<NFAState> getToState(NFAState from, char onSymb) {
    	// TODO: Complete this method
        return null;
    }

    public Set<NFAState> eClosure(NFAState s) {
    	// TODO: Complete this method
        return null;
    }

    /**
     * Check if a state with such name already exists
     * @Author elenasherman - as written in DFA.java
     * @param name
     * @return null if no state exist, or DFAState object otherwise.
     */
    private NFAState checkIfExists(String name){
        NFAState ret = null;
        for(NFAState s : restStates){
            if(s.getName().equals(name)){
                ret = s;
                break;
            }
        }
        return ret;
    }
}
