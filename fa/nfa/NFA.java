package fa.nfa;

import fa.State;
import fa.dfa.DFA;
import fa.dfa.DFAState;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Nodeterministic Finite Automata
 * @author Quinn Shultz, Mateo Ortegon
 */
public class NFA implements NFAInterface {
    private Set<NFAState> restStates;
    private Set<NFAState> finalStates;
    private Set<Character> alphabet;

    private NFAState startState;

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
            System.out.println("WARNING: A state with name " + name + " already exists in the DFA");
        }
        startState = s;
    }

    @Override
    public void addState(String name) {

    }

    @Override
    public void addFinalState(String name) {

    }

    @Override
    public void addTransition(String fromState, char onSymb, String toState) {

    }

    @Override
    public Set<? extends State> getStates() {
        return null;
    }

    @Override
    public Set<? extends State> getFinalStates() {
        return null;
    }

    @Override
    public State getStartState() {
        return null;
    }

    @Override
    public Set<Character> getABC() {
        return null;
    }

    @Override
    public DFA getDFA() {
        return null;
    }

    public Set<NFAState> getToState(NFAState from, char onSymb) {

        return null;
    }

    public Set<NFAState> eClosure(NFAState s) {

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
