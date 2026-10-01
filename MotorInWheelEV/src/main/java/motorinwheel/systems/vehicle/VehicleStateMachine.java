package motorinwheel.systems.vehicle;

import java.util.Optional;
import sysmlinjava.javaannotations.statemachines.Guard;
import sysmlinjava.javaannotations.statemachines.GuardCondition;
import sysmlinjava.javaannotations.statemachines.EntryActionFunction;
import sysmlinjava.javaannotations.statemachines.State;
import sysmlinjava.javaannotations.statemachines.Transition;
import sysmlinjava.states.FinalEvent;
import sysmlinjava.states.FinalTransition;
import sysmlinjava.states.InitialTransition;
import sysmlinjava.states.SysMLEntryActionFunction;
import sysmlinjava.states.SysMLGuard;
import sysmlinjava.states.SysMLGuardCondition;
import sysmlinjava.states.SysMLState;
import sysmlinjava.states.SysMLStateMachine;
import sysmlinjava.states.SysMLTransition;
import sysmlinjava.states.SysMLTransitionKind;

/**
 * State machine for the Vehicle model/simulation. The state machine consists of
 * an initialization and operational state with transitions only for moving
 * between these states, i.e. as a parts container, there is no significant
 * behavior performed by the vehicle. See the model of the state machine below
 * for more detail.
 * 
 * @author ModelerOne
 *
 */
public class VehicleStateMachine extends SysMLStateMachine
{
	@State
	public SysMLState initializingState;
	@State
	public SysMLState operationalState;

	@Transition
	public InitialTransition initialToInitializingTransition;
	@Transition
	public SysMLTransition initializingToOperationalTransition;
	@Transition
	public SysMLTransition operationalToFinalTransition;

	@EntryActionFunction
	public SysMLEntryActionFunction operationalOnEnterActivity;

	@GuardCondition
	public SysMLGuardCondition isNotFinalEventGuardCondition;
	@Guard
	public SysMLGuard isNotFinalEventGuard;

	public VehicleStateMachine(Vehicle vehicle)
	{
		super(Optional.of(vehicle), false, "VehicleStateMachine");
	}

	@Override
	protected void createStateEntryActionFunctions()
	{
		super.createStateEntryActionFunctions();
		operationalOnEnterActivity = (contextPart) ->
		{
			((Vehicle)contextPart.get()).transmitWeights();
		};
	}

	@Override
	protected void createStates()
	{
		super.createStates();
		initializingState = new SysMLState(context, "Initializing");
		operationalState = new SysMLState(context, "Operational");
	}

	@Override
	protected void createGuardConditions()
	{
		super.createGuardConditions();
		this.isNotFinalEventGuardCondition = (event, contextPart) ->
		{
			return event.isPresent() && !(event.get() instanceof FinalEvent);
		};
	}

	@Override
	protected void createGuards()
	{
		super.createGuards();
		isNotFinalEventGuard = new SysMLGuard(context, isNotFinalEventGuardCondition, "IsNotFinalEvent");
	}

	@Override
	protected void createTransitions()
	{
		initialToInitializingTransition = new InitialTransition(context, initialState, initializingState, "IntialToInitializing");
		initializingToOperationalTransition = new SysMLTransition(context, initializingState, operationalState, Optional.empty(), Optional.empty(), Optional.empty(), "InitializingToOperational", SysMLTransitionKind.external);
		operationalToFinalTransition = new FinalTransition(context, operationalState, finalState, "OperationalToFinal");
	}
}
