package motorinwheel.systems.vehicle;

import java.util.Optional;
import sysmlinjava.annotations.statemachines.Guard;
import sysmlinjava.annotations.statemachines.GuardCondition;
import sysmlinjava.annotations.statemachines.OnEnterActivity;
import sysmlinjava.annotations.statemachines.State;
import sysmlinjava.annotations.statemachines.Transition;
import sysmlinjava.statemachine.FinalEvent;
import sysmlinjava.statemachine.FinalTransition;
import sysmlinjava.statemachine.InitialTransition;
import sysmlinjava.statemachine.SysMLGuard;
import sysmlinjava.statemachine.SysMLGuardCondition;
import sysmlinjava.statemachine.SysMLOnEnterActivity;
import sysmlinjava.statemachine.SysMLState;
import sysmlinjava.statemachine.SysMLStateMachine;
import sysmlinjava.statemachine.SysMLTransition;
import sysmlinjava.statemachine.SysMLTransitionKind;

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

	@OnEnterActivity
	public SysMLOnEnterActivity operationalOnEnterActivity;

	@GuardCondition
	public SysMLGuardCondition isNotFinalEventGuardCondition;
	@Guard
	public SysMLGuard isNotFinalEventGuard;

	public VehicleStateMachine(Vehicle vehicle)
	{
		super(Optional.of(vehicle), false, "VehicleStateMachine");
	}

	@Override
	protected void createStateOnEnterActivities()
	{
		super.createStateOnEnterActivities();
		operationalOnEnterActivity = (contextBlock) ->
		{
			((Vehicle)contextBlock.get()).transmitWeights();
		};
	}

	@Override
	protected void createStates()
	{
		super.createStates();
		initializingState = new SysMLState(contextBlock, "Initializing");
		operationalState = new SysMLState(contextBlock, "Operational");
	}

	@Override
	protected void createGuardConditions()
	{
		super.createGuardConditions();
		this.isNotFinalEventGuardCondition = (event, contextBlock) ->
		{
			return event.isPresent() && !(event.get() instanceof FinalEvent);
		};
	}

	@Override
	protected void createGuards()
	{
		super.createGuards();
		isNotFinalEventGuard = new SysMLGuard(contextBlock, isNotFinalEventGuardCondition, "IsNotFinalEvent");
	}

	@Override
	protected void createTransitions()
	{
		initialToInitializingTransition = new InitialTransition(contextBlock, initialState, initializingState, "IntialToInitializing");
		initializingToOperationalTransition = new SysMLTransition(contextBlock, initializingState, operationalState, Optional.empty(), Optional.empty(), Optional.empty(), "InitializingToOperational", SysMLTransitionKind.external);
		operationalToFinalTransition = new FinalTransition(contextBlock, operationalState, finalState, "OperationalToFinal");
	}
}
