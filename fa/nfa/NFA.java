package fa.nfa;

import fa.State;
import fa.dfa.DFA;

import java.util.*;

/**
 * Nondeterministic Finite Automata
 * @author Quinn Shultz, Mateo Ortegon
 */
public class NFA implements NFAInterface {
    private final Set<NFAState> restStates;
    private final Set<NFAState> finalStates;
    private final Set<NFAState> allStates;
    private final Set<Character> alphabet;
    private final LinkedHashSet<NFAState> eClosure;
    private final LinkedHashSet<NFAState> searchedStates;

    private NFAState startState; //save reference so don't have to look for it later
    private final char e = 'e'; //empty transition char

    /**
     * Construct Nondeterministic Finite Automata
     */
    public NFA(){
        restStates = new LinkedHashSet<>();
        finalStates = new LinkedHashSet<>();
        alphabet = new LinkedHashSet<>();
        allStates = new LinkedHashSet<>();
        eClosure = new LinkedHashSet<>();
        searchedStates = new LinkedHashSet<>();
    }

    /**
     * (non-Javadoc)
     * @see fa.nfa.NFAInterface#addStartState(String)
     */
    @Override
    public void addStartState(String name) {
        NFAState state = checkIfExists(name,allStates);
        if(state == null){ //if state doesn't exist in allStates collection then create a new NFAState object and add
            state = new NFAState(name);
            allStates.add(state);
        }
        startState = state; // save reference to start state
    }

    /**
     * (non-Javadoc)
     * @see fa.nfa.NFAInterface#addState(String)
     */
    @Override
    public void addState(String name) {
        NFAState state = checkIfExists(name,allStates);
        if(state == null){ //if state doesn't exist in allStates collection then create a new NFAState object and add
            state = new NFAState(name);
        }
        restStates.add(state);
        allStates.add(state);
    }

    /**
     * (non-Javadoc)
     * @see fa.nfa.NFAInterface#addFinalState(String)
     */
    @Override
    public void addFinalState(String name) {
        NFAState state = new NFAState(name);
        state.setAsFinalState();
        allStates.add(state);
        finalStates.add(state);
    }

    /**
     * (non-Javadoc)
     * @see fa.nfa.NFAInterface#addTransition(String, char, String)
     */
    @Override
    public void addTransition(String from, char onSymb, String to) {
        NFAState fromState = checkIfExists(from, allStates);
        NFAState toState = checkIfExists(to, allStates);

        if(fromState.transitions.containsKey(onSymb)){ //if the transition symbol already exists for the fromState, just link the toState to that symbol
            fromState.transitions.get(onSymb).add(toState);
        } else{ //if the transition does not exist for the fromState, then create a new linked set of transitions, and link the symbol to the new list
            Set<NFAState> transitionStates = new LinkedHashSet<>();
            transitionStates.add(toState);
            fromState.transitions.put(onSymb, transitionStates);
        }

        if(!alphabet.contains(onSymb) && onSymb != e){ //if the non-empty transition symbol is not already in the alphabet, then add it
            alphabet.add(onSymb);
        }
    }

    /**
     * (non-Javadoc)
     * @see fa.nfa.NFAInterface#getStates()
     */
    @Override
    public Set<NFAState> getStates() {
        return restStates;
    }

    /**
     * (non-Javadoc)
     * @see fa.nfa.NFAInterface#getFinalStates()
     */
    @Override
    public Set<NFAState> getFinalStates() {
        return finalStates;
    }

    /**
     * (non-Javadoc)
     * @see fa.nfa.NFAInterface#getStartState()
     */
    @Override
    public State getStartState() {
        return startState;
    }

    /**
     * (non-Javadoc)
     * @see fa.nfa.NFAInterface#getABC()
     */
    @Override
    public Set<Character> getABC() {
        return alphabet;
    }

    /**
     * (non-Javadoc)
     * @see fa.nfa.NFAInterface#getDFA()
     */
    @Override
    public DFA getDFA() {
        DFA dfa = new DFA();

        Set<Set<NFAState>> statesSet = new LinkedHashSet<>(); //Set(key) state(value) pair
        Set<NFAState> nfaEClosure = eClosure(this.startState); //first set is eClosure from startState
        statesSet.add(nfaEClosure);

        Queue<Set<NFAState>> queue = new LinkedList<>();
        queue.add(nfaEClosure); //stack to push and pop nfaEClosures

        dfa.addStartState(nfaEClosure.toString()); //get string from first eClosure to add as start state on DFA

        while(!queue.isEmpty()){ //keep looping while still have items on stack
            //begin popping LIFO
            nfaEClosure = queue.poll();

            for (char c : alphabet) { //for each char on alphabet find all the possible transitions for each set
                LinkedHashSet<NFAState> temp = new LinkedHashSet<>();

                //store all toStates from each state in nfaEClosure over symbol c
                for (NFAState state : nfaEClosure) {
                    temp.addAll(getToState(state,c));
                }

                HashSet<NFAState> dfaSet = new HashSet<>();

                for(NFAState state : temp){
                    dfaSet.addAll(eClosure(state));
                }

                if(!statesSet.contains(dfaSet)){
                    statesSet.add(dfaSet);
                    queue.add(dfaSet);

                    //figure out if list contains any final states yet
                    boolean hasFinal = false;
                    for(NFAState state: dfaSet){
                        if(finalStates.contains(state)){
                            hasFinal = true;
                            break;
                        }
                    }

                    //if list does contain final states
                    if(hasFinal){
                        dfa.addFinalState(dfaSet.toString()); //
                    }else{
                        dfa.addState(dfaSet.toString());
                    }
                }

                //add transitions to DFA
                String fromState = nfaEClosure.toString();
                String toState = dfaSet.toString();
                dfa.addTransition(fromState, c, toState);
            }
        }
        return dfa;
    }

    /**
     * (non-Javadoc)
     * @see fa.nfa.NFAInterface#getToState(NFAState, char)
     */
    public Set<NFAState> getToState(NFAState fromState, char onSymb) {
        Set<NFAState> toStates;

        if(fromState.transitions.containsKey(onSymb)){ //if onSymb is in the fromSate's transition map
            toStates = fromState.transitions.get(onSymb); //get the list of states transitioned over onSymb from fromState
        }else{
            toStates = new LinkedHashSet<>();
        }
        return toStates;
    }

    /**
     * (non-Javadoc)
     * @see fa.nfa.NFAInterface#eClosure(NFAState)
     */
    public Set<NFAState> eClosure(NFAState currentState){
        eClosure.add(currentState);
        
        if(!getToState(currentState,e).isEmpty() && !searchedStates.contains(currentState)){ //if there are still valid transitions to be made from this state
                                                                                    // and this state hasn't been visited yet
            searchedStates.add(currentState);
            for(NFAState state : getToState(currentState,e)){ //loop for each state that can be transitioned to from current state over e
                Set<NFAState> tempEClosure = eClosure(state); //get the eCLosure from this state
                eClosure.addAll(tempEClosure);
            }
        }

        //create temp copy of eClosure
        LinkedHashSet<NFAState> temp = new LinkedHashSet<>(eClosure);

        //clear lists for future searches
        eClosure.clear();
        searchedStates.clear();

        return temp;
    }

    /**
     * Check if a state with such name already exists
     * @param name name of state
     * @param list list of states
     * @return null if no state exist, or DFAState object otherwise.
     */
    private NFAState checkIfExists(String name, Set<NFAState> list){
        NFAState ret = null;
        for(NFAState s : list){
            if(s.getName().equals(name)){
                ret = s;
                break;
            }
        }
        return ret;
    }

}
