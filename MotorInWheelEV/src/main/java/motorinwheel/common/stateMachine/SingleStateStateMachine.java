package motorinwheel.common.stateMachine;

import java.util.Optional;
import sysmlinjava.javaannotations.statemachines.State;
import sysmlinjava.javaannotations.statemachines.Transition;
import sysmlinjava.parts.SysMLPart;
import sysmlinjava.states.FinalTransition;
import sysmlinjava.states.InitialTransition;
import sysmlinjava.states.SysMLState;
import sysmlinjava.states.SysMLStateMachine;

/**
 * State machine for parts that operate in a single operational state. The
 * {@code SingleStateStateMachine} starts with a transition from the
 * {@code InitialState} to an {@code operationalState}. Internal transitions
 * within the {@code operationalState} are defined by extensions to the
 * {@code SingleStateStateMachine} to create a specialized state machine that
 * operates in a single state. The {@code SingleStateStateMachine} will
 * transition to the {@code FinalState} if/when a {@code FinalEvent} is
 * received.  See the state machine definition below for details.
 * 
 * @author ModelerOne
 *
 */
public abstract class SingleStateStateMachine extends SysMLStateMachine
{
	/**
	 * State for the single operational state
	 */
	@State
	public SysMLState operationalState;

	/**
	 * Initial transition to the single operational state
	 */
	@Transition
	public InitialTransition initialToOperationalTransition;
	
	
	// @Transition
	// public SysMLTransition operationalOnEventTransition;
	/**
	 * Final transition from the operational state 
	 */
	@Transition
	public FinalTransition operationalToFinalTransition;

	/**
	 * Constructor
	 * @param contextPart part in whose context the state machine executes
	 * @param isAsynchronous true if the state machine is to operate in its own thread of execution, false otherwise
	 * @param name unique name
	 */
	public SingleStateStateMachine(SysMLPart contextPart, boolean isAsynchronous, String name)
	{
		super(Optional.of(contextPart), isAsynchronous, name);
	}

	@Override
	protected void createStates()
	{
		super.createStates();
		operationalState = new SysMLState(context, "Operational");
	}

	@Override
	protected void createTransitions()
	{
		initialToOperationalTransition = new InitialTransition(context, initialState, operationalState, "IntialToInitializing");
		// operationalOnEventTransition = new SysMLTransition(contextPart,
		// operationalState, operationalState, Optional.of(SysMLEvent.class),
		// Optional.of(isNotFinalEventGuard), Optional.empty(), "OperationalOnEvent",
		// SysMLTransitionKind.internal); //Replace this empty effect with instance of
		// effect on your contextPart
		operationalToFinalTransition = new FinalTransition(context, operationalState, finalState, "OperationalToFinal");
	}
}
