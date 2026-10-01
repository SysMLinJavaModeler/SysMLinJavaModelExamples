package trafficcontrolsystem;

import java.util.List;
import java.util.Optional;

import sysmlinjava.events.SysMLChangeEvent;
import sysmlinjava.events.SysMLCompletionEvent;
import sysmlinjava.javaannotations.statemachines.ChoicePseudoState;
import sysmlinjava.javaannotations.statemachines.Effect;
import sysmlinjava.javaannotations.statemachines.EffectActionFunction;
import sysmlinjava.javaannotations.statemachines.Guard;
import sysmlinjava.javaannotations.statemachines.GuardCondition;
import sysmlinjava.javaannotations.statemachines.State;
import sysmlinjava.javaannotations.statemachines.StateMachine;
import sysmlinjava.javaannotations.statemachines.Transition;
import sysmlinjava.states.FinalTransition;
import sysmlinjava.states.InitialTransition;
import sysmlinjava.states.SysMLChoicePseudoState;
import sysmlinjava.states.SysMLEffect;
import sysmlinjava.states.SysMLEffectActionFunction;
import sysmlinjava.states.SysMLGuard;
import sysmlinjava.states.SysMLGuardCondition;
import sysmlinjava.states.SysMLState;
import sysmlinjava.states.SysMLStateMachine;
import sysmlinjava.states.SysMLTransition;
import sysmlinjava.states.SysMLTransitionKind;
import sysmlinjava.views.statetransitionstables.StateTransitionTablesTransmitter;
import sysmlinjava.views.statetransitionstables.StateTransitionsDisplay;
import sysmlinjava.views.statetransitionstables.StateTransitionsTransmitters;
import sysmlinjava.views.timingdiagrams.StatesAxis;
import sysmlinjava.views.timingdiagrams.TimeAxis;
import sysmlinjava.views.timingdiagrams.TimingDiagramDefinition;
import sysmlinjava.views.timingdiagrams.TimingDiagramsDisplay;
import sysmlinjava.views.timingdiagrams.TimingDiagramsTransmitter;

/**
 * State machine for the {@code TrafficControlSystem}. The
 * {@code TrafficControlSystemStateMachine} consists of a choice pseudo-state to
 * determine whether to start operation in normal operations or emergency
 * operations mode, and one state each for operating in normal or emergency
 * mode. The state machine also contains sub-state machines for the behavior of
 * each of the individual intersection's signal systems - one for normal
 * operations and one for emergency operations. These normal and emergency
 * operations intersection state machines operate as concurrent sub-state
 * machines in the normal and emergency operations states, respectively, of the
 * {@code TrafficControlSystemStateMachine}. See the state machine model below
 * for more details.
 * <p>
 * {@code TrafficControlSystemStateMachine} uses the
 * {@code StateTransitionsTransmitters} to transmit state transition timing
 * information to specified {@code TimingDiagramDisplays} if active. If the
 * display is activated prior to model execution, timing diagram information for
 * the model execution is transmitted to the timing diagram display. SysMLinJava
 * provides a simple text-based display of the timing diagram information, but
 * highly capable graphical timing diagrams display applications are available
 * commercially. See SysMLinJava.com for more information.
 * 
 * @author ModelerOne
 *
 */
@SuppressWarnings("javadoc")
public class TrafficSignalControlSystemStateMachine extends SysMLStateMachine
{
	@ChoicePseudoState
	public SysMLChoicePseudoState initialStateChoice;

	@State
	public SysMLState normalOpsState;
	@State
	public SysMLState emergencyOpsState;

	@GuardCondition
	public SysMLGuardCondition emergencyVehicleAbsentGuardCondition;
	@GuardCondition
	public SysMLGuardCondition emergencyVehiclePresentGuardCondition;
	@GuardCondition
	public SysMLGuardCondition emergencyVehicleArrivesGuardCondition;
	@GuardCondition
	public SysMLGuardCondition emergencyVehicleDepartsGuardCondition;

	@Guard
	public SysMLGuard emergencyVehicleAbsentGuard;
	@Guard
	public SysMLGuard emergencyVehiclePresentGuard;
	@Guard
	public SysMLGuard emergencyVehicleArrivesGuard;
	@Guard
	public SysMLGuard emergencyVehicleDepartsGuard;

	@EffectActionFunction
	public SysMLEffectActionFunction normalToEmergencyEffectAction;
	@EffectActionFunction
	public SysMLEffectActionFunction emergencyToNormalEffectAction;

	@Effect
	public SysMLEffect normalToEmergencyEffect;
	@Effect
	public SysMLEffect emergencyToNormalEffect;

	@Transition
	public InitialTransition initialTransition;
	@Transition
	public SysMLTransition normalChoiceTransition;
	@Transition
	public SysMLTransition emergencyChoiceTransition;
	@Transition
	public SysMLTransition normalToEmergencyTransition;
	@Transition
	public SysMLTransition emergencyToNormalTransition;
	@Transition
	public FinalTransition normalToFinalTransition;
	@Transition
	public FinalTransition emergencyToFinalTransition;

	@StateMachine
	public IntersectionSignalSystemNormalStateMachine mainAt1stNormalOpsStateMachine;
	@StateMachine
	public IntersectionSignalSystemNormalStateMachine mainAt2ndNormalOpsStateMachine;
	@StateMachine
	public IntersectionSignalSystemNormalStateMachine mainAt3rdNormalOpsStateMachine;
	@StateMachine
	public IntersectionSignalSystemNormalStateMachine mainAt4thNormalOpsStateMachine;
	@StateMachine
	public IntersectionSignalSystemEmergencyStateMachine mainAt1stEmergencyOpsStateMachine;
	@StateMachine
	public IntersectionSignalSystemEmergencyStateMachine mainAt2ndEmergencyOpsStateMachine;
	@StateMachine
	public IntersectionSignalSystemEmergencyStateMachine mainAt3rdEmergencyOpsStateMachine;
	@StateMachine
	public IntersectionSignalSystemEmergencyStateMachine mainAt4thEmergencyOpsStateMachine;

	public TrafficSignalControlSystemStateMachine(TrafficSignalControlSystem contextBlock)
	{
		super(Optional.of(contextBlock), true, "TrafficControlSystemStateMachine");
	}

	@Override
	public void start()
	{
		TimeAxis timeAxis = new TimeAxis(600.0, 30.0, 6);
		StatesAxis stateAxis = new StatesAxis(listOfStates());
		StateTransitionsTransmitters transmitters = (StateTransitionsTransmitters)transitionsUtility.get();
		transmitters.transmit(new TimingDiagramDefinition(identityString(), stateAxis, timeAxis));
		super.start();
	}

	@Override
	public void stop()
	{
		((StateTransitionsTransmitters)transitionsUtility.get()).stop();
		super.stop();
	}

	@Override
	public List<String> listOfStates()
	{
		List<String> result = super.listOfStates();
		result.add(initialStateChoice.identityString());
		result.add(normalOpsState.identityString());
		result.add(emergencyOpsState.identityString());
		result.add(finalState.identityString());
		return result;
	}

	@Override
	protected void createStateCompositeStateMachines()
	{
		mainAt1stNormalOpsStateMachine = new IntersectionSignalSystemNormalStateMachine(((TrafficSignalControlSystem)context.get()).mainAt1st, "MainAt1stSignalNormalOpsStateMachine", 1L);
		mainAt2ndNormalOpsStateMachine = new IntersectionSignalSystemNormalStateMachine(((TrafficSignalControlSystem)context.get()).mainAt2nd, "MainAt2ndSignalNormalOpsStateMachine", 2L);
		mainAt3rdNormalOpsStateMachine = new IntersectionSignalSystemNormalStateMachine(((TrafficSignalControlSystem)context.get()).mainAt3rd, "MainAt3rdSignalNormalOpsStateMachine", 3L);
		mainAt4thNormalOpsStateMachine = new IntersectionSignalSystemNormalStateMachine(((TrafficSignalControlSystem)context.get()).mainAt4th, "MainAt4thSignalNormalOpsStateMachine", 4L);
		mainAt1stEmergencyOpsStateMachine = new IntersectionSignalSystemEmergencyStateMachine(((TrafficSignalControlSystem)context.get()).mainAt1st, "MainAt1stSignalEmergencyOpsStateMachine", 1L);
		mainAt2ndEmergencyOpsStateMachine = new IntersectionSignalSystemEmergencyStateMachine(((TrafficSignalControlSystem)context.get()).mainAt2nd, "MainAt2ndSignalEmergencyOpsStateMachine", 2L);
		mainAt3rdEmergencyOpsStateMachine = new IntersectionSignalSystemEmergencyStateMachine(((TrafficSignalControlSystem)context.get()).mainAt3rd, "MainAt3rdSignalEmergencyOpsStateMachine", 3L);
		mainAt4thEmergencyOpsStateMachine = new IntersectionSignalSystemEmergencyStateMachine(((TrafficSignalControlSystem)context.get()).mainAt4th, "MainAt4thSignalEmergencyOpsStateMachine", 4L);
	}

	@Override
	protected void createStates()
	{
		super.createStates();
		normalOpsState = new SysMLState(context.get(), Optional.empty(), List.of(mainAt1stNormalOpsStateMachine, mainAt2ndNormalOpsStateMachine, mainAt3rdNormalOpsStateMachine, mainAt4thNormalOpsStateMachine), Optional.empty(), "NormalOps");
		emergencyOpsState = new SysMLState(context.get(), Optional.empty(), List.of(mainAt1stEmergencyOpsStateMachine, mainAt2ndEmergencyOpsStateMachine, mainAt3rdEmergencyOpsStateMachine, mainAt4thEmergencyOpsStateMachine), Optional.empty(), "EmergencyOps");
	}

	@Override
	protected void createPseudoStates()
	{
		super.createPseudoStates();
		initialStateChoice = new SysMLChoicePseudoState(context, "StartStateChoice");
	}

	@SuppressWarnings("unused")
	@Override
	protected void createGuardConditions()
	{
		emergencyVehicleAbsentGuardCondition = (event, context) ->
		{
			TrafficSignalControlSystem system = (TrafficSignalControlSystem)context.get();
			return system.emergencyVehiclePresent.isFalse();
		};
		emergencyVehiclePresentGuardCondition = (event, context) ->
		{
			TrafficSignalControlSystem system = (TrafficSignalControlSystem)context.get();
			return system.emergencyVehiclePresent.isTrue();
		};
		emergencyVehicleArrivesGuardCondition = (event, context) ->
		{
			return !((SysMLChangeEvent)event.get()).changeExpression.split("=")[1].equals("none");
		};
		emergencyVehicleDepartsGuardCondition = (event, context) ->
		{
			return ((SysMLChangeEvent)event.get()).changeExpression.split("=")[1].equals("none");
		};
	}

	@Override
	protected void createGuards()
	{
		emergencyVehicleAbsentGuard = new SysMLGuard(context, emergencyVehicleAbsentGuardCondition, "EmergencyVehiclAbsent");
		emergencyVehiclePresentGuard = new SysMLGuard(context, emergencyVehiclePresentGuardCondition, "EmergencyVehiclePresent");
		emergencyVehicleArrivesGuard = new SysMLGuard(context, emergencyVehicleArrivesGuardCondition, "EmergencyVehicleArrives");
		emergencyVehicleDepartsGuard = new SysMLGuard(context, emergencyVehicleDepartsGuardCondition, "EmergencyVehicleDeparts");
	}

	@SuppressWarnings("unused")
	@Override
	protected void createEffectActionFunctions()
	{
		normalToEmergencyEffectAction = (event, context) ->
		{
			((TrafficSignalControlSystem)context.get()).emergencyVehiclePresent.setValue(true);
		};
		emergencyToNormalEffectAction = (event, context) ->
		{
			((TrafficSignalControlSystem)context.get()).emergencyVehiclePresent.setValue(false);
		};
	}

	@Override
	protected void createEffects()
	{
		normalToEmergencyEffect = new SysMLEffect(context, normalToEmergencyEffectAction, "ToEmergencyEffect");
		emergencyToNormalEffect = new SysMLEffect(context, emergencyToNormalEffectAction, "ToNormalEffect");
	}

	@Override
	protected void createTransitions()
	{
		initialTransition = new InitialTransition(context, initialState, initialStateChoice, "Initial");
		normalChoiceTransition = new SysMLTransition(context, initialStateChoice, normalOpsState, Optional.of(emergencyVehicleAbsentGuard), Optional.empty(), "NormalChoice");
		emergencyChoiceTransition = new SysMLTransition(context, initialStateChoice, emergencyOpsState, Optional.of(emergencyVehiclePresentGuard), Optional.empty(), "EmergencyChoice");

		normalToEmergencyTransition = new SysMLTransition(context, normalOpsState, emergencyOpsState, Optional.of(SysMLCompletionEvent.class), Optional.empty(), Optional.of(normalToEmergencyEffect), "NormalToEmergency", SysMLTransitionKind.external);
		emergencyToNormalTransition = new SysMLTransition(context, emergencyOpsState, normalOpsState, Optional.of(SysMLCompletionEvent.class), Optional.empty(), Optional.of(emergencyToNormalEffect), "EmergencyToNormal", SysMLTransitionKind.external);

		normalToFinalTransition = new FinalTransition(context, normalOpsState, finalState, "NormalToFinal");
		emergencyToFinalTransition = new FinalTransition(context, emergencyOpsState, finalState, "EmergencyToFinal");
	}

	@Override
	protected void createTransitionsUtility()
	{
		transitionsUtility = Optional.of(new StateTransitionsTransmitters(Optional.of(new StateTransitionTablesTransmitter(StateTransitionsDisplay.udpPort, false)), Optional.of(new TimingDiagramsTransmitter(TimingDiagramsDisplay.udpPort, false)), false));
	}
}
