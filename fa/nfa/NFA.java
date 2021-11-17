package fa.nfa;

import fa.State;
import fa.dfa.DFA;
import fa.dfa.DFAState;

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

    private NFAState startState; //save reference so don't have to look for it later
    private final char e = 'e'; //empty transition char

    /**
     * Construct a new Nondeterministic Finite Automata
     */
    public NFA(){
        restStates = new LinkedHashSet<NFAState>();
        finalStates = new LinkedHashSet<NFAState>();
        alphabet = new LinkedHashSet<Character>();
        allStates = new LinkedHashSet<NFAState>();
    }

    /**
     * TODO
     * @param
     * @return
     */
    @Override
    public void addStartState(String name) {
        NFAState state = checkIfExists(name,allStates);
        if(state == null){ //if state doesn't exist in allStates collection then create a new NFAState object and add
            state = new NFAState(name);
            allStates.add(state);
        }
        state.setAsStartState();
        startState = state; // save reference to start state
    }

    /**
     * TODO
     * @param
     * @return
     */
    @Override
    public void addState(String name) {
        NFAState state = new NFAState(name);
        if(state == null){ //if state doesn't exist in allStates collection then create a new NFAState object and add
            state = new NFAState(name);
        }
        restStates.add(state);
        allStates.add(state);
    }

    /**
     * TODO
     * @param
     * @return
     */
    @Override
    public void addFinalState(String name) {
        NFAState state = new NFAState(name);
        state.setAsFinalState();
        allStates.add(state);
        finalStates.add(state);
    }

    /**
     * TODO
     * @param
     * @param
     * @param
     * @return
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
     * TODO
     * @return
     */
    @Override
    public Set<NFAState> getStates() {
        return restStates;
    }

    /**
     * TODO
     * @return
     */
    @Override
    public Set<NFAState> getFinalStates() {
        return finalStates;
    }

    /**
     * TODO
     * @return
     */
    @Override
    public State getStartState() {
        return startState;
    }

    /**
     * TODO
     * @return
     */
    @Override
    public Set<Character> getABC() {
        return alphabet;
    }

    /**
     * TODO
     * @return
     */
    @Override
    public DFA getDFA() { //TODO: RE-RWITE
        /* Initialize new DFA */
        DFA dfa = new DFA();

        /* Keep track of visited states */
        Map<Set<NFAState>, String> visitedStates = new LinkedHashMap<>(); //Set(key) state(value) pair

        /* Get the closure of the NFA's start state*/
        Set<NFAState> states = eClosure(startState);

        /* Add to visited sates set */
        visitedStates.put(states, states.toString());

        LinkedList<Set<NFAState>> queue = new LinkedList<>();

        /* Adds the set of states to the end of the queue */
        queue.add(states);

        /* Sets the start state of the DFS */
        dfa.addStartState(visitedStates.get(states));

        while(!queue.isEmpty()){
            /* Queue based working of a linked list - Retrieves and removes the
             * head (first element) of this list */
            states = queue.poll();

            for (char c : alphabet) {
                LinkedHashSet<NFAState> temp = new LinkedHashSet<>();
                for (NFAState s : states) {
                    /* Adds all of the elements from 'st.getTo(c)' to temp */
                    temp.addAll(getToState(s,c));
                }
                LinkedHashSet<NFAState> temp1 = new LinkedHashSet<>();
                for(NFAState st : temp){
                    temp1.addAll(eClosure(st));
                }
                if(!visitedStates.containsKey(temp1)){
                    visitedStates.put(temp1, temp1.toString());
                    queue.add(temp1);

                    //figure out if list contains any final states yet
                    boolean hasFinal = false;
                    for(NFAState s: temp1){
                        if(finalStates.contains(s)){
                            hasFinal = true;
                            break;
                        }
                    }

                    //if list does contain final states
                    if(hasFinal){
                        dfa.addFinalState(visitedStates.get(temp1)); //
                    }else{
                        dfa.addState(visitedStates.get(temp1));
                    }
                }

                /* Add transitions to the DFA */
                dfa.addTransition(visitedStates.get(states), c, visitedStates.get(temp1));
            }
        }
        return dfa;
    }

    /**
     * TODO
     * @param fromState
     * @param onSymb
     * @return toStates
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
     * TODO
     * @param state
     * @return Set<NFAState>
     */
    public Set<NFAState> eClosure(NFAState state){
        return search(new LinkedHashSet<>(), state);
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

    /**
     * TODO
     * @param hashSet
     * @param st
     * @return
     */
    private Set<NFAState> search(LinkedHashSet<NFAState> hashSet, NFAState st){ //TODO: RE-RWITE
        LinkedHashSet<NFAState> visitedStates = hashSet;
        LinkedHashSet<NFAState> eClosureSet = new LinkedHashSet<>();

        eClosureSet.add(st);
        /* As long as there exists a state to go to on an empty transition */
        if(!getToState(st,e).isEmpty() && !visitedStates.contains(st)){
            visitedStates.add(st);
            for(NFAState nfa : getToState(st,e)){
                eClosureSet.addAll(search(visitedStates, nfa));
            }
        }
        return eClosureSet;
    }
}
