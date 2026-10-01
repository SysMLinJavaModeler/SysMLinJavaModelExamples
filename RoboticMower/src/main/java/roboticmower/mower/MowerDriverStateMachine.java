package roboticmower.mower;

import java.util.Optional;
import roboticmower.info.WheelsControl;
import roboticmower.signals.WheelsControlSignal;
import sysmlinjava.events.SysMLSignalEvent;
import sysmlinjava.javaannotations.statemachines.Effect;
import sysmlinjava.javaannotations.statemachines.EffectActionFunction;
import sysmlinjava.javaannotations.statemachines.EntryActionFunction;
import sysmlinjava.javaannotations.statemachines.State;
import sysmlinjava.javaannotations.statemachines.Transition;
import sysmlinjava.states.FinalTransition;
import sysmlinjava.states.InitialTransition;
import sysmlinjava.states.SysMLEffect;
import sysmlinjava.states.SysMLEffectActionFunction;
import sysmlinjava.states.SysMLEntryActionFunction;
import sysmlinjava.states.SysMLState;
import sysmlinjava.states.SysMLStateMachine;
import sysmlinjava.states.SysMLTransition;
import sysmlinjava.states.SysMLTransitionKind;
import sysmlinjava.views.statetransitionstables.StateTransitionTablesTransmitter;
import sysmlinjava.views.statetransitionstables.StateTransitionsDisplay;
import sysmlinjava.views.statetransitionstables.StateTransitionsTransmitters;

/**
 * State machine behavior of the Mower Driver. The state machine consists of
 * only two states - initializing and operating. While in the operating state,
 * the state machine performs internal transitions whose effects are to respond
 * to wheel control events.
 * 
 * @author ModelerOne
 *
 * @see roboticmower.mower.MowerDriver
 */
public class MowerDriverStateMachine extends SysMLStateMachine
{
	/**
	 * State for initializing the subsystem
	 */
	@State
	SysMLState initializing;
	/**
	 * State for normal operation, i.e. responding to wheel control events
	 */
	@State
	SysMLState operating;

	/**
	 * Transition from initial to initializing state
	 */
	@Transition
	InitialTransition initialToInitializing;
	/**
	 * Transition from initializing to operating state
	 */
	@Transition
	SysMLTransition initializingToOperating;
	/**
	 * Transition within operating state for wheels control event
	 */
	@Transition
	SysMLTransition operatingOnWheelsControl;
	/**
	 * Transition from operating to final state
	 */
	@Transition
	FinalTransition controllingToFinal;

	/**
	 * Action performed upon entering the initializing state
	 */
	@EntryActionFunction
	SysMLEntryActionFunction initializingOnEnterAction;

	/**
	 * Action performed as the effect of internal transition upon wheels control
	 * event
	 */
	@EffectActionFunction
	SysMLEffectActionFunction operatingOnWheelsControlEffectAction;

	/**
	 * Effect of internal transition upon wheels control event
	 */
	@Effect
	SysMLEffect operatingOnWheelsControlEffect;

	/**
	 * Constructor
	 * 
	 * @param contextBlock block in whose context this state machine is to execute.
	 */
	public MowerDriverStateMachine(MowerDriver contextBlock)
	{
		super(Optional.of(contextBlock), true, "MowerDriverStateMachine");
	}

	@Override
	protected void createStateEntryActionFunctions()
	{
		initializingOnEnterAction = (contextBlock) ->
		{
			MowerDriver subsystem = (MowerDriver)contextBlock.get();
			subsystem.initialize();
		};
	}

	@Override
	protected void createStates()
	{
		super.createStates();
		initializing = new SysMLState(context, Optional.of(initializingOnEnterAction), Optional.empty(), Optional.empty(), "Initializing");
		operating = new SysMLState(context, "Operating");
	}

	@Override
	protected void createEffectActionFunctions()
	{
		operatingOnWheelsControlEffectAction = (event, contextBlock) ->
		{
			MowerDriver subsystem = (MowerDriver)contextBlock.get();
			WheelsControl control = ((WheelsControlSignal)((SysMLSignalEvent)event.get()).signal).control;
			subsystem.onWheelsControl(control);
		};
	}

	@Override
	protected void createEffects()
	{
		operatingOnWheelsControlEffect = new SysMLEffect(context, operatingOnWheelsControlEffectAction, "OnWheelsControlEffect");
	}

	@Override
	protected void createTransitions()
	{
		initialToInitializing = new InitialTransition(context, initialState, initializing, "InitialToInitializing");
		initializingToOperating = new SysMLTransition(context, initializing, operating, Optional.empty(), Optional.empty(), Optional.empty(), "InitializingToOperating", SysMLTransitionKind.external);
		operatingOnWheelsControl = new SysMLTransition(context, operating, operating, Optional.of(SysMLSignalEvent.class), Optional.empty(), Optional.of(operatingOnWheelsControlEffect), "OperatingOnWheelsControl",
			SysMLTransitionKind.internal);
		controllingToFinal = new FinalTransition(context, operating, finalState, "OperatingToFinal");
	}

	@Override
	protected void createTransitionsUtility()
	{
		transitionsUtility = Optional.of(new StateTransitionsTransmitters(Optional.of(new StateTransitionTablesTransmitter(StateTransitionsDisplay.udpPort, false)), Optional.empty(), false));
	}
}
