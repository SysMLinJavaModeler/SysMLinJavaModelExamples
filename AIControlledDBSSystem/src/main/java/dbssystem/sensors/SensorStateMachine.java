package dbssystem.sensors;

import java.util.Optional;

import sysmlinjava.events.SysMLSignalEvent;
import sysmlinjava.javaannotations.statemachines.Effect;
import sysmlinjava.javaannotations.statemachines.EffectActionFunction;
import sysmlinjava.javaannotations.statemachines.Guard;
import sysmlinjava.javaannotations.statemachines.GuardCondition;
import sysmlinjava.javaannotations.statemachines.State;
import sysmlinjava.javaannotations.statemachines.Transition;
import sysmlinjava.parts.SysMLPart;
import sysmlinjava.states.FinalTransition;
import sysmlinjava.states.InitialTransition;
import sysmlinjava.states.SysMLEffect;
import sysmlinjava.states.SysMLEffectActionFunction;
import sysmlinjava.states.SysMLGuard;
import sysmlinjava.states.SysMLGuardCondition;
import sysmlinjava.states.SysMLState;
import sysmlinjava.states.SysMLStateMachine;
import sysmlinjava.states.SysMLTransition;
import sysmlinjava.states.SysMLTransitionKind;

/**
 * Abstract state machine for a common sensor. The state machine consists of
 * only two states - one to initialize the sensor and one to repeatedly handle
 * input/receipt of sensed values.
 * 
 * @author ModelerOne
 */
public abstract class SensorStateMachine extends SysMLStateMachine
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
	protected SysMLTransition operationalOnSensedValueSignalTransition;
	@Transition
	protected SysMLTransition operationalToFinalTransition;

	@GuardCondition
	protected SysMLGuardCondition isSensedValueGuardCondition;

	@Guard
	protected SysMLGuard isSensedValueGuard;

	@EffectActionFunction
	protected SysMLEffectActionFunction onSensedValueSignalEffectActivity;

	@Effect
	protected SysMLEffect onSensedValueSignalEffect;

	/**
	 * Constructor
	 * 
	 * @param contextSensor sensor in whose context this state machine is to operate
	 * @param name          unique name
	 */
	public SensorStateMachine(SysMLPart contextSensor, String name)
	{
		super(Optional.of(contextSensor), true, name);
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
		// Guard conditions to be created in method override, e.g.
		// isSensedValueGuardCondition = (event, contextBlock) -> {return ...};
	}

	@Override
	protected void createGuards()
	{
		isSensedValueGuard = new SysMLGuard(context, isSensedValueGuardCondition, "isMotionSignal");
	}

	@Override
	protected void createEffectActionFunctions()
	{
		// Effect activities to be created in method override, e.g.
		// onSensedValueSignalEffectActivity = (event, contextBlock) -> {...};
	}

	@Override
	protected void createEffects()
	{
		super.createEffects();
		onSensedValueSignalEffect = new SysMLEffect(context, onSensedValueSignalEffectActivity, "onSensedValueChange");
	}

	@Override
	protected void createTransitions()
	{
		initialToInitializingTransition = new InitialTransition(context, initialState, initializingState, "InitialToInitializing");
		initializingToOperationalTransition = new SysMLTransition(context, initializingState, operationalState, "InitializingToOperational");
		operationalOnSensedValueSignalTransition = new SysMLTransition(context, operationalState, operationalState, Optional.of(SysMLSignalEvent.class),
		Optional.of(isSensedValueGuard), Optional.of(onSensedValueSignalEffect), "OperationalOnSensedValueSignal", SysMLTransitionKind.internal);
		operationalToFinalTransition = new FinalTransition(context, operationalState, finalState, "OperationalToFinal");
	}
}
