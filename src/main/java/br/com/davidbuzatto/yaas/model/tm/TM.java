/*
 * Copyright (C) 2023 Prof. Dr. David Buzatto
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package br.com.davidbuzatto.yaas.model.tm;

import br.com.davidbuzatto.yaas.gui.tm.TMSimulationStep;
import br.com.davidbuzatto.yaas.model.AbstractGeometricForm;
import br.com.davidbuzatto.yaas.util.CharacterConstants;
import br.com.davidbuzatto.yaas.util.Utils;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Turing Machine representation and algorithms.
 * TODO update
 * 
 * @author Prof. Dr. David Buzatto
 */
public class TM extends AbstractGeometricForm implements Cloneable {
    
    private transient static final boolean DEBUG = Boolean.parseBoolean( 
            Utils.getMavenModel().getProperties().getProperty( "debugAlgorithms" ) );
    
    private List<TMState> states;
    private List<TMTransition> transitions;
    private TMState initialState;
    private TMType type;
    private char stackStartingSymbol;
    
    private transient TMID rootId;
    private transient List<TMID> ids;
    private transient static int buildTreeLevel;
    private transient static final String levelString = "  ";
    private transient boolean accepted;
    private transient boolean acceptForSimulation;
    private transient TMID firstAcceptedId;
    
    // cache control
    private boolean alphabetUpToDate;
    private boolean stackAlphabetUpToDate;
    private boolean deltaUpToDate;
    
    private Set<Character> alphabet;
    private Set<Character> stackAlphabet;
    private Map<TMState, List<TMTransition>> delta;
    
    private boolean transitionControlPointsVisible;
    
    public TM() {
        this( CharacterConstants.STACK_STARTING_SYMBOL );
    }
    
    public TM( char startingSymbol ) {
        this.states = new ArrayList<>();
        this.transitions = new ArrayList<>();
        this.stackStartingSymbol = startingSymbol;
        this.type = TMType.EMPTY;
    }
    
    public boolean accepts( String str, TMAcceptanceType acceptanceType ) {
        return accepts( str, acceptanceType, null );
    }
    
    // TODO implement
    public boolean accepts( String str, TMAcceptanceType acceptanceType, List<TMSimulationStep> simulationSteps ) {
        
        accepted = false;
        acceptForSimulation = false;
        firstAcceptedId = null;
        
        if ( simulationSteps != null ) {
            acceptForSimulation = true;
        }
        
        if ( canExecute() ) {
            
            Map<TMState, List<TMTransition>> delta = getDelta();
            Deque<Character> stack = new ArrayDeque<>();
            stack.push( stackStartingSymbol );
            
            rootId = new TMID( initialState, str, stack, null, Color.BLACK );
            
            buildTreeLevel = 0;
            ids = new ArrayList<>();
            buildIDTree( rootId, delta, acceptanceType );
            
            if ( firstAcceptedId != null ) {
                
                List<TMID> pathIds = new ArrayList<>();
                
                TMID current = firstAcceptedId;
                while ( current != null ) {
                    pathIds.add( current );
                    current = current.getParent();
                }
                
                for ( int i = pathIds.size()-1; i >= 0; i-- ) {
                    simulationSteps.add( new TMSimulationStep( pathIds.get( i ) ) );
                }
                
            }
            
        }
        
        return accepted;
        
    }
    
    // TODO implement
    private void buildIDTree( TMID node, Map<TMState, List<TMTransition>> delta, TMAcceptanceType acceptanceType ) {
        
        if ( node.getString().isEmpty() ) {
            
            if ( acceptanceType == TMAcceptanceType.FINAL_STATE && node.getState().isFinal() ) {
                
                node.setAcceptedByFinalState( true );
                accepted = true;
                
                // finds only one solution path
                if ( acceptForSimulation ) {
                    firstAcceptedId = node;
                    ids.add( node );
                    return;
                }
                
            } else if ( acceptanceType == TMAcceptanceType.STOP && node.getStack().isEmpty() ) {
                
                node.setAcceptedByEmptyStack( true );
                accepted = true;
                
                if ( acceptForSimulation ) {
                    firstAcceptedId = node;
                    ids.add( node );
                    return;
                }
                
            }
            
        }
        
        ids.add( node );
        buildTreeLevel++;
        
        // found the first leaf when is collecting data for simulation
        if ( firstAcceptedId != null ) {
            return;
        }
        
        if ( DEBUG ) {
            System.out.println( levelString.repeat( buildTreeLevel ) + "Processing: " + node );
        }

        String string = node.getString();

        for ( TMTransition t : delta.get( node.getState() ) ) {

            if ( DEBUG ) {
                System.out.println( levelString.repeat( buildTreeLevel ) + "  Transition: " + t );
            }

            for ( TMOperation o : t.getOperations() ) {

                if ( DEBUG ) {
                    System.out.println( levelString.repeat( buildTreeLevel ) + "    Operation: " + o );
                }

                // matches symbol
                if ( !string.isEmpty() && o.getSymbol() == string.charAt( 0 ) ) {

                    if ( DEBUG ) {
                        System.out.println( levelString.repeat( buildTreeLevel ) + "      Matches symbol: " + o.getSymbol() );
                    }

                    Deque<Character> stack = Utils.cloneCharacterStack( node.getStack() );

                    // matches stack top
                    if ( !stack.isEmpty() && o.getTop() == stack.peek() ) {

                        if ( DEBUG ) {
                            System.out.println( levelString.repeat( buildTreeLevel ) + "      Matches stack top: " + o.getTop() );
                        }

                        // consumes the input symbol
                        String newString = string.substring( 1 );

                        // updates the stack
                        processStack( o, stack );

                        TMID newId = new TMID( t.getTargetState(), 
                                newString, stack, o, t.getStrokeColor() );
                        node.addChild( newId );

                        buildIDTree( newId, delta, acceptanceType );

                        // empty stack?
                    } else if ( o.getTop() == CharacterConstants.EMPTY_STRING && stack.isEmpty() ) {

                        if ( DEBUG ) {
                            System.out.println( levelString.repeat( buildTreeLevel ) + "      Matches empty stack" );
                        }

                        String newString = string.substring( 1 );
                        processStack( o, stack );

                        TMID newId = new TMID( t.getTargetState(),
                                newString, stack, o, t.getStrokeColor() );
                        node.addChild( newId );

                        buildIDTree( newId, delta, acceptanceType );

                    }

                    // empty transition 
                } else if ( o.getSymbol() == CharacterConstants.EMPTY_STRING ) {

                    if ( DEBUG ) {
                        System.out.println( levelString.repeat( buildTreeLevel ) + "      Empty transition" );
                    }

                    Deque<Character> stack = Utils.cloneCharacterStack( node.getStack() );

                    // matches stack top
                    if ( !stack.isEmpty() && o.getTop() == stack.peek() ) {

                        if ( DEBUG ) {
                            System.out.println( levelString.repeat( buildTreeLevel ) + "      Matches stack top: " + o.getTop() );
                        }

                        // don't consume any symbol

                        // updates the stack
                        processStack( o, stack );

                        TMID newId = new TMID( t.getTargetState(), 
                                string, stack, o, t.getStrokeColor() );
                        node.addChild( newId );

                        buildIDTree( newId, delta, acceptanceType );

                        // empty stack?
                    } else if ( o.getTop() == CharacterConstants.EMPTY_STRING && stack.isEmpty() ) {

                        if ( DEBUG ) {
                            System.out.println( levelString.repeat( buildTreeLevel ) + "      Matches empty stack" );
                        }

                        String newString = string.substring( 1 );
                        processStack( o, stack );

                        TMID newId = new TMID( t.getTargetState(), 
                                newString, stack, o, t.getStrokeColor() );
                        node.addChild( newId );

                        buildIDTree( newId, delta, acceptanceType );

                    }

                }

            }

        }
        
        buildTreeLevel--;
        
    }
    
    // TODO remove?
    private void processStack( TMOperation op, Deque<Character> stack ) {
        
        switch ( op.getType() ) {
            case DO_NOTHING:
                break;
            case POP:
                stack.pop();
                break;
            case PUSH:
                for ( Character s : op.getSymbolsToPush() ) {
                    stack.push( s );
                }
                break;
            case REPLACE:
                stack.pop();
                for ( Character s : op.getSymbolsToPush() ) {
                    stack.push( s );
                }
                break;
        }
        
    }

    public boolean canExecute() {
        return initialState != null;
    }
    
    @Override
    public void draw( Graphics2D g2d ) {
        
        g2d = (Graphics2D) g2d.create();
        
        int maxX = 0;
        int maxY = 0;
        
        for ( TMTransition t : transitions ) {
            t.draw( g2d );
        }
        
        for ( TMState s : states ) {
            s.draw( g2d );
            if ( maxX < s.getX1() + s.getRadius() ) {
                maxX = s.getX1() + s.getRadius();
            }
            if ( maxY < s.getY1() + s.getRadius() ) {
                maxY = s.getY1() + s.getRadius();
            }
        }
        
        width = maxX + 100;
        height = maxY + 100;
        
        for ( TMTransition t : transitions ) {
            t.drawLabel( g2d );
        }
        
        g2d.dispose();
        
    }

    @Override
    public boolean intersects( int x, int y ) {
        return false;
    }

    @Override
    public void move( int xAmount, int yAmount ) {
        for ( TMTransition t : transitions ) {
            t.move( xAmount, yAmount );
        }
        for ( TMState s : states ) {
            s.move( xAmount, yAmount );
        }
    }
    
    public TMState getStateAt( int x, int y ) {
        
        for ( int i = states.size()-1; i >= 0; i-- ) {
            TMState s = states.get( i );
            if ( s.intersects( x, y ) ) {
                s.setSelected( true );
                return s;
            }
        }
        
        return null;
        
    }
    
    public TMTransition getTransitionAt( int x, int y ) {
        
        for ( int i = transitions.size()-1; i >= 0; i-- ) {
            TMTransition t = transitions.get( i );
            if ( t.intersects( x, y ) ) {
                t.setSelected( true );
                return t;
            }
        }
        
        return null;
        
    }
    
    public void deselectAll() {
        for ( TMTransition t : transitions ) {
            t.setSelected( false );
        }
        for ( TMState s : states ) {
            s.setSelected( false );
        }
    }
    
    public void addState( TMState state ) {
        
        if ( state != null ) {
            
            states.add( state );
            
            if ( state.isInitial() ) {
                initialState = state;
            }
            
        }
        
        markAllCachesAsObsolete();
        updateType();
        
    }
    
    public void addTransition( TMTransition transition ) {
        
        if ( transition != null ) {
            
            TMTransition tf = null;
            
            for ( TMTransition t : transitions ) {
                if ( t.getOriginState() == transition.getOriginState() && 
                        t.getTargetState() == transition.getTargetState() ) {
                    tf = t;
                    break;
                }
            }
            
            if ( tf == null ) {
                transition.setControlPointsVisible( transitionControlPointsVisible );
                transitions.add( transition );
            } else {
                tf.addOperations( transition.getOperations() );
            }
            
        }
        
        updateTransitions();
        markAllCachesAsObsolete();
        updateType();
        
    }
    
    public void updateTransitions() {
        for ( TMTransition t : transitions ) {
            t.updateStartAndEndPoints();
        }
    }
    
    public void resetTransitionsTransformations() {
        for ( TMTransition t : transitions ) {
            t.resetTransformations();
        }
    }
    
    public void draggTransitions( MouseEvent evt ) {
        for ( TMTransition t : transitions ) {
            t.mouseDragged( evt );
        }
    }
    
    public void setTransitionsControlPointsVisible( boolean visible ) {
        transitionControlPointsVisible = visible;
        for ( TMTransition t : transitions ) {
            t.setControlPointsVisible( visible );
        }
    }
    
    public void mouseHoverStatesAndTransitions( int x, int y ) {
        for ( TMTransition t : transitions ) {
            t.mouseHover( x, y );
        }
        for ( TMState s : states ) {
            s.mouseHover( x, y );
        }
    }

    public void resetStatesColor() {
        for ( TMState s : states ) {
            s.resetStrokeColor();
        }
    }
    
    public void resetTransitionsColor() {
        for ( TMTransition t : transitions ) {
            t.resetStrokeColor();
        }
    }
    
    public void setInitialState( TMState initialState ) {
        
        if ( this.initialState != null ) {
            this.initialState.setInitial( false );
        }
        
        this.initialState = initialState;
        
        markAllCachesAsObsolete();
        updateType();
        
    }

    public TMState getInitialState() {
        return initialState;
    }
    
    public TMType getType() {
        return type;
    }
    
    public void removeState( TMState state ) {
        
        if ( initialState == state ) {
            initialState = null;
        }
        
        states.remove( state );
        
        List<TMTransition> ts = new ArrayList<>();
        for ( TMTransition t : transitions ) {
            if ( t.getOriginState() == state || t.getTargetState() == state ) {
                ts.add( t );
            }
        }
        
        for ( TMTransition t : ts ) {
            transitions.remove( t );
        }
        
        markAllCachesAsObsolete();
        updateType();
        
    }
    
    public void removeTransition( TMTransition transition ) {
        transitions.remove( transition );
        markAllCachesAsObsolete();
        updateType();
    }
    
    // TODO update
    public void updateType() {
        
        if ( states.isEmpty() ) {
            type = TMType.EMPTY;
            return;
        }
        
        Map<String, Integer> counts = new HashMap<>();
        Map<String, String> mirror = new HashMap<>();
        boolean epsilon = false;
        boolean nondeterminism = false;
        
        String sepLeft = "-<|";
        String sepRight = "|>-";
        String lookEp = String.format( "%s%c%s", sepLeft,
                CharacterConstants.EMPTY_STRING, sepRight );
        
        for ( TMTransition t : transitions ) {
            
            for ( TMOperation o : t.getOperations() ) {
                
                String k = String.format( "%s-%s%c%s-%c", 
                        t.getOriginState(), sepLeft, 
                        o.getSymbol(), sepRight, o.getTop() );
                String kEp = String.format( "%s-%s%c%s-%c", 
                        t.getOriginState(), sepLeft, 
                        CharacterConstants.EMPTY_STRING, sepRight, o.getTop() );
                
                Integer v = counts.get( k );
                counts.put( k, v == null ? 1 : v+1 );
                
                if ( !counts.containsKey( kEp ) ) {
                    counts.put( kEp, 0 );
                }
                
                mirror.put( k, kEp );
                mirror.put( kEp, k );
                
            }
            
        }
        
        for ( Map.Entry<String, Integer> e : counts.entrySet() ) {
            
            if ( e.getValue() > 1 ) {
                nondeterminism = true;
                break;
            }
            
            String k1 = e.getKey();
            String k2 = mirror.get( k1 );
            
            if ( !k1.contains( lookEp ) && k2 != null ) {
                Integer c = counts.get( k2 );
                if ( c != null && c > 0 ) {
                    epsilon = true;
                }
            }
            
        }
        
        if ( nondeterminism || epsilon ) {
            type = TMType.TM;
        } else {
            type = TMType.NTM;
        }
        
    }
    
    public String getFormalDefinition() {
        
        String def = String.format("T = { Q, %c, %c, %c, %s, %c, F }\n",
                CharacterConstants.CAPITAL_SIGMA,
                CharacterConstants.CAPITAL_GAMMA,
                CharacterConstants.SMALL_DELTA, 
                initialState.toString(),
                stackStartingSymbol );
        def += getStatesString() + "\n";
        def += getAlphabetString() + "\n";
        def += getStackAlphabetString() + "\n";
        def += getFinalStatesString();
        
        return def;
        
    }
    
    // TODO update
    public Set<Character> getAlphabet() {
        
        if ( alphabet == null || !alphabetUpToDate ) {
            
            alphabetUpToDate = true;
            alphabet = new TreeSet<>();
        
            for ( TMTransition t : transitions ) {
                for ( TMOperation o : t.getOperations() ) {
                    if ( o.getSymbol() != CharacterConstants.EMPTY_STRING ) {
                        alphabet.add( o.getSymbol() );
                    }
                }
            }
        
        }
        
        return alphabet;
        
    }
    
    // TODO update
    public Set<Character> getStackAlphabet() {
        
        if ( stackAlphabet == null || !stackAlphabetUpToDate ) {
            
            stackAlphabetUpToDate = true;
            
            stackAlphabet = new TreeSet<>();
            stackAlphabet.add( stackStartingSymbol );
        
            for ( TMTransition t : transitions ) {
                for ( TMOperation o : t.getOperations() ) {
                    if ( o.getTop() != CharacterConstants.EMPTY_STRING ) {
                        stackAlphabet.add( o.getTop() );
                    }
                    stackAlphabet.addAll( o.getSymbolsToPush() );
                }
            }
        
        }
        
        return stackAlphabet;
        
    }
    
    // TODO update
    public Map<TMState, List<TMTransition>> getDelta() {
        
        if ( delta == null || !deltaUpToDate ) {
            
            deltaUpToDate = true;
            
            delta = new TreeMap<>();
            for ( TMState s : states ) {
                delta.put( s, new ArrayList<>() );
            }

            for ( TMTransition t : transitions ) {
                List<TMTransition> ts = delta.get( t.getOriginState() );
                ts.add( t );
            }
            
        }
        
        return delta;
        
    }
    
    private String getStatesString() {
        
        String str = "";
        
        List<String> ss = new ArrayList<>();
        for ( TMState s : states ) {
            ss.add( s.toString() );
        }
        
        Collections.sort( ss );
        boolean first = true;
        
        str += "Q = { ";
        for ( String s : ss ) {
            if ( !first ) {
                str += ", ";
            }
            str += s;
            first = false;
        }
        str += " }";
        
        return str;
        
    }
    
    private String getFinalStatesString() {
        
        String str = "";
        
        List<String> ss = new ArrayList<>();
        for ( TMState s : states ) {
            if ( s._final ) {
                ss.add( s.toString() );
            }
        }
        
        Collections.sort( ss );
        boolean first = true;
        
        str += "F = { ";
        for ( String s : ss ) {
            if ( !first ) {
                str += ", ";
            }
            str += s;
            first = false;
        }
        str += " }";
        
        return str;
        
    }
    
    public List<TMState> getFinalStates() {
        
        List<TMState> acStates = new ArrayList<>();
        
        for ( TMState s : states ) {
            if ( s.isFinal() ) {
                acStates.add( s );
            }
        }
        
        return acStates;
        
    }
    
    private String getAlphabetString() {
        
        String str = "";
        
        Set<Character> alf = getAlphabet();
        
        boolean first = true;
        
        str += CharacterConstants.CAPITAL_SIGMA + " = { ";
        for ( Character c : alf ) {
            if ( !first ) {
                str += ", ";
            }
            str += c;
            first = false;
        }
        str += " }";
        
        return str;
        
    }
    
    private String getStackAlphabetString() {
        
        String str = "";
        
        Set<Character> alf = getStackAlphabet();
        
        boolean first = true;
        
        str += CharacterConstants.CAPITAL_GAMMA + " = { ";
        for ( Character c : alf ) {
            if ( !first ) {
                str += ", ";
            }
            str += c;
            first = false;
        }
        str += " }";
        
        return str;
        
    }

    public List<TMState> getStates() {
        return states;
    }

    public List<TMTransition> getTransitions() {
        return transitions;
    }

    public char getStackStartingSymbol() {
        return stackStartingSymbol;
    }
    
    public void setStackStartingSymbol( char stackStartingSymbol ) {
        this.stackStartingSymbol = stackStartingSymbol;
        markAllCachesAsObsolete();
    }

    public TMID getRootId() {
        return rootId;
    }

    public List<TMID> getIds() {
        return ids;
    }
    
    public void deactivateAllStatesInSimulation() {
        for ( TMState s : states ) {
            s.setActiveInSimulation( false );
        }
    }
    
    public void markAllCachesAsObsolete() {
        alphabetUpToDate = false;
        stackAlphabetUpToDate = false;
        deltaUpToDate = false;
    }
    
    public void merge( TM fa ) {
        
        for ( TMState s : fa.getStates() ) {
            addState( s );
        }
        
        for ( TMTransition t : fa.getTransitions() ) {
            addTransition( t );
        }
        
    }
    
    public String generateCode() {
        
        updateType();
        String className = getClass().getSimpleName();
        String modelName = "tm";
        
        switch ( type ) {
            case EMPTY:
                modelName = "tm";
                break;
            case TM:
                modelName = "tm";
                break;
            case NTM:
                modelName = "ntm";
                break;
        }
        
        String template =
                """
                public static %s create%s() {
                
                    %s %s = new %s();
                
                    // states
                %s
                
                    // transitions
                %s
                
                    return %s;
                
                }""";
        
        StringBuilder stBuilderInst = new StringBuilder();
        boolean first = true;
        for ( TMState s : this.states ) {
            if ( !first ) {
                stBuilderInst.append( "\n\n" );
            }
            stBuilderInst.append( s.generateCode( modelName ) );
            first = false;
        }
        
        StringBuilder tBuilder = new StringBuilder();
        first = true;
        for ( TMTransition t : this.transitions ) {
            if ( !first ) {
                tBuilder.append( "\n\n" );
            }
            tBuilder.append( t.generateCode( this, modelName ) );
            first = false;
        }
        
        return String.format( template, 
                className, modelName.toUpperCase(), 
                className, modelName, className, 
                stBuilderInst.toString(), 
                tBuilder.toString(),
                modelName );
        
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode( this.states );
        hash = 23 * hash + Objects.hashCode( this.transitions );
        hash = 23 * hash + Objects.hashCode( this.initialState );
        return hash;
    }

    @Override
    public boolean equals( Object obj ) {
        if ( this == obj ) {
            return true;
        }
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        final TM other = (TM) obj;
        if ( !Objects.equals( this.states, other.states ) ) {
            return false;
        }
        if ( !Objects.equals( this.transitions, other.transitions ) ) {
            return false;
        }
        return Objects.equals( this.initialState, other.initialState );
    }
    
    @Override
    @SuppressWarnings( "unchecked" )
    public Object clone() throws CloneNotSupportedException {
        
        TM c = (TM) super.clone();
        Map<TMState, TMState> ref = new HashMap<>();
    
        c.states = new ArrayList<>();
        for ( TMState s : states ) {
            TMState n = (TMState) s.clone();
            c.addState( n );
            ref.put( s, n );
        }
        
        c.transitions = new ArrayList<>();
        for ( TMTransition t : transitions ) {
            TMTransition n = (TMTransition) t.clone();
            n.setOriginState( ref.get( t.getOriginState() ) );
            n.setTargetState( ref.get( t.getTargetState() ) );
            c.addTransition( n );
        }
        
        // c.initialState = null;  <- c.addState() resolves it accordingly
        // c.type = null;          <- c.updateType() resolves it accordingly
        
        c.stackStartingSymbol = stackStartingSymbol;
        
        c.alphabetUpToDate = false;
        c.stackAlphabetUpToDate = false;
        c.deltaUpToDate = false;

        c.alphabet = null;
        c.stackAlphabet = null;
        c.delta = null;

        c.transitionControlPointsVisible = false;
        
        c.updateType();
        return c;
        
    }
    
}