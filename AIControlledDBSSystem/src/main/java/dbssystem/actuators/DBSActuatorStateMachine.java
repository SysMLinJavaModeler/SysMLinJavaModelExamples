package dbssystem.actuators;

import java.util.Optional;

import dbssystem.common.DBSControlSignal;
import sysmlinjava.events.SysMLSignalEvent;
import sysmlinjava.javaannotations.statemachines.Effect;
import sysmlinjava.javaannotations.statemachines.EffectActionFunction;
import sysmlinjava.javaannotations.statemachines.State;
import sysmlinjava.javaannotations.statemachines.Transition;
import sysmlinjava.states.FinalTransition;
import sysmlinjava.states.InitialTransition;
import sysmlinjava.states.SysMLEffect;
import sysmlinjava.states.SysMLEffectActionFunction;
import sysmlinjava.states.SysMLState;
import sysmlinjava.states.SysMLStateMachine;
import sysmlinjava.states.SysMLTransition;
import sysmlinjava.states.SysMLTransitionKind;

/**
 * State machine for the DBSActuator. The state machine consists of only two
 * states - one to initialize the actuator and one to repeatedly handle receipts
 * of control values.
 * 
 * @author ModelerOne
 */
public class DBSActuatorStateMachine extends SysMLStateMachine
{
	@State
	protected SysMLState initializingState;
	@State
	protected SysMLState operationalState;

	@Transition
	protected InitialTransition initialToInitializingTransition;
	@Transition
	protected SysMLTransition initializingToOperationalTransition;
	@Transition
	protected SysMLTransition operationalOnDBSControlTransition;
	@Transition
	protected SysMLTransition operationalToFinalTransition;

	@EffectActionFunction
	protected SysMLEffectActionFunction onDBSControlEffectAction;

	@Effect
	protected SysMLEffect onDBSControlEffect;

	/**
	 * Constructor
	 * 
	 * @param contextPart part in whose context the port executes, i.e. the DBS
	 *                    actuator
	 */
	public DBSActuatorStateMachine(DBSActuator contextPart)
	{
		super(Optional.of(contextPart), true, "DBSActuatorStateMachine");
	}

	@Override
	protected void createStates()
	{
		super.createStates();
		initializingState = new SysMLState(context, "Initializing");
		operationalState = new SysMLState(context, "Operational");
	}

	@Override
	protected void createEffectActionFunctions()
	{
		onDBSControlEffectAction = (event, contextBlock) ->
		{
			DBSActuator actuator = (DBSActuator) contextBlock.get();
			SysMLSignalEvent controlEvent = (SysMLSignalEvent) event.get();
			DBSControlSignal dbsControlSignal = (DBSControlSignal) controlEvent.signal;
			actuator.onDBSControl(dbsControlSignal.control);
		};
	}

	@Override
	protected void createEffects()
	{
		super.createEffects();
		onDBSControlEffect = new SysMLEffect(context, onDBSControlEffectAction, "onDBSControl");
	}

	@Override
	protected void createTransitions()
	{
		initialToInitializingTransition = new InitialTransition(context, initialState, initializingState, "InitialToInitializing");
		initializingToOperationalTransition = new SysMLTransition(context, initializingState, operationalState, "InitializingToOperational");
		operationalOnDBSControlTransition = new SysMLTransition(context, operationalState, operationalState, Optional.of(SysMLSignalEvent.class), Optional.empty(),
		Optional.of(onDBSControlEffect), "OperationalOnDBSControl", SysMLTransitionKind.internal);
		operationalToFinalTransition = new FinalTransition(context, operationalState, finalState, "OperationalToFinal");
	}
}
