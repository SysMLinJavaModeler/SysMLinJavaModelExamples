package trafficcontrolsystem;

import java.util.List;
import java.util.Optional;
import sysmlinjava.analysis.statetransitions.StateTransitionTablesTransmitter;
import sysmlinjava.analysis.statetransitions.StateTransitionsDisplay;
import sysmlinjava.analysis.statetransitionstransmitters.StateTransitionsTransmitters;
import sysmlinjava.analysis.timingdiagrams.StatesAxis;
import sysmlinjava.analysis.timingdiagrams.TimeAxis;
import sysmlinjava.analysis.timingdiagrams.TimingDiagramDefinition;
import sysmlinjava.analysis.timingdiagrams.TimingDiagramsDisplay;
import sysmlinjava.analysis.timingdiagrams.TimingDiagramsTransmitter;
import sysmlinjava.annotations.statemachines.ChoicePseudoState;
import sysmlinjava.annotations.statemachines.Effect;
import sysmlinjava.annotations.statemachines.EffectActivity;
import sysmlinjava.annotations.statemachines.Guard;
import sysmlinjava.annotations.statemachines.GuardCondition;
import sysmlinjava.annotations.statemachines.JunctionPseudoState;
import sysmlinjava.annotations.statemachines.State;
import sysmlinjava.annotations.statemachines.StateMachine;
import sysmlinjava.annotations.statemachines.Transition;
import sysmlinjava.statemachine.FinalEvent;
import sysmlinjava.statemachine.FinalTransition;
import sysmlinjava.statemachine.InitialTransition;
import sysmlinjava.statemachine.StateMachineCompletionEvent;
import sysmlinjava.statemachine.SysMLChoicePseudoState;
import sysmlinjava.statemachine.SysMLEffect;
import sysmlinjava.statemachine.SysMLEffectActivity;
import sysmlinjava.statemachine.SysMLGuard;
import sysmlinjava.statemachine.SysMLGuardCondition;
import sysmlinjava.statemachine.SysMLJunctionPseudoState;
import sysmlinjava.statemachine.SysMLState;
import sysmlinjava.statemachine.SysMLStateMachine;
import sysmlinjava.statemachine.SysMLTransition;
import sysmlinjava.statemachine.SysMLTransitionKind;

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
public class TrafficControlSystemStateMachine extends SysMLStateMachine
{
	@ChoicePseudoState
	public SysMLChoicePseudoState initialStateChoice;
	@JunctionPseudoState
	public SysMLJunctionPseudoState finalStateJunction;

	@State
	public SysMLState normalOpsState;
	@State
	public SysMLState emergencyOpsState;

	@GuardCondition
	public SysMLGuardCondition normalGuardCondition;
	@GuardCondition
	public SysMLGuardCondition emergencyGuardCondition;

	@Guard
	public SysMLGuard normalGuard;
	@Guard
	public SysMLGuard emergencyGuard;

	@EffectActivity
	public SysMLEffectActivity normalToEmergencyEffectActivity;
	@EffectActivity
	public SysMLEffectActivity emergencyToNormalEffectActivity;

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
	public SysMLTransition normalJunctionTransition;
	@Transition
	public SysMLTransition emergencyJunctionTransition;
	@Transition
	public FinalTransition finalTransition;

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

	public TrafficControlSystemStateMachine(TrafficControlSystem contextBlock)
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
		result.add(finalStateJunction.identityString());
		result.add(finalState.identityString());
		return result;
	}

	@Override
	protected void createStateCompositeStateMachines()
	{
		mainAt1stNormalOpsStateMachine = new IntersectionSignalSystemNormalStateMachine(((TrafficControlSystem)contextBlock.get()).mainAt1st, "MainAt1stSignalNormalOpsStateMachine", 1L);
		mainAt2ndNormalOpsStateMachine = new IntersectionSignalSystemNormalStateMachine(((TrafficControlSystem)contextBlock.get()).mainAt2nd, "MainAt2ndSignalNormalOpsStateMachine", 2L);
		mainAt3rdNormalOpsStateMachine = new IntersectionSignalSystemNormalStateMachine(((TrafficControlSystem)contextBlock.get()).mainAt3rd, "MainAt3rdSignalNormalOpsStateMachine", 3L);
		mainAt4thNormalOpsStateMachine = new IntersectionSignalSystemNormalStateMachine(((TrafficControlSystem)contextBlock.get()).mainAt4th, "MainAt4thSignalNormalOpsStateMachine", 4L);
		mainAt1stEmergencyOpsStateMachine = new IntersectionSignalSystemEmergencyStateMachine(((TrafficControlSystem)contextBlock.get()).mainAt1st, "MainAt1stSignalEmergencyOpsStateMachine", 1L);
		mainAt2ndEmergencyOpsStateMachine = new IntersectionSignalSystemEmergencyStateMachine(((TrafficControlSystem)contextBlock.get()).mainAt2nd, "MainAt2ndSignalEmergencyOpsStateMachine", 2L);
		mainAt3rdEmergencyOpsStateMachine = new IntersectionSignalSystemEmergencyStateMachine(((TrafficControlSystem)contextBlock.get()).mainAt3rd, "MainAt3rdSignalEmergencyOpsStateMachine", 3L);
		mainAt4thEmergencyOpsStateMachine = new IntersectionSignalSystemEmergencyStateMachine(((TrafficControlSystem)contextBlock.get()).mainAt4th, "MainAt4thSignalEmergencyOpsStateMachine", 4L);
	}

	@Override
	protected void createStates()
	{
		super.createStates();
		normalOpsState = new SysMLState(contextBlock.get(), Optional.empty(), List.of(mainAt1stNormalOpsStateMachine, mainAt2ndNormalOpsStateMachine, mainAt3rdNormalOpsStateMachine, mainAt4thNormalOpsStateMachine), Optional.empty(), "NormalOps");
		emergencyOpsState = new SysMLState(contextBlock.get(), Optional.empty(), List.of(mainAt1stEmergencyOpsStateMachine, mainAt2ndEmergencyOpsStateMachine, mainAt3rdEmergencyOpsStateMachine, mainAt4thEmergencyOpsStateMachine), Optional.empty(), "EmergencyOps");
	}

	@Override
	protected void createPseudoStates()
	{
		super.createPseudoStates();
		initialStateChoice = new SysMLChoicePseudoState(contextBlock, "StartStateChoice");
		finalStateJunction = new SysMLJunctionPseudoState(contextBlock, "StopStateJunction");
	}

	@Override
	protected void createGuardConditions()
	{
		normalGuardCondition = (event, contextBlock) ->
		{
			TrafficControlSystem system = (TrafficControlSystem)contextBlock.get();
			return system.emergencyVehiclePresent.isFalse();
		};
		emergencyGuardCondition = (event, contextBlock) ->
		{
			TrafficControlSystem system = (TrafficControlSystem)contextBlock.get();
			return system.emergencyVehiclePresent.isTrue();
		};
	}

	@Override
	protected void createGuards()
	{
		normalGuard = new SysMLGuard(contextBlock, normalGuardCondition, "EmergencyVehicleAbsent");
		emergencyGuard = new SysMLGuard(contextBlock, emergencyGuardCondition, "EmergencyVehiclePresent");
	}

	@Override
	protected void createEffectActivities()
	{
		normalToEmergencyEffectActivity = (event, contextBlock) ->
		{

		};
		emergencyToNormalEffectActivity = (event, contextBlock) ->
		{

		};
	}

	@Override
	protected void createEffects()
	{
		normalToEmergencyEffect = new SysMLEffect(contextBlock, normalToEmergencyEffectActivity, "ToEmergencyEffect");
		emergencyToNormalEffect = new SysMLEffect(contextBlock, emergencyToNormalEffectActivity, "ToNormalEffect");
	}

	@Override
	protected void createTransitions()
	{
		initialTransition = new InitialTransition(contextBlock, initialState, initialStateChoice, "Initial");
		normalChoiceTransition = new SysMLTransition(contextBlock, initialStateChoice, normalOpsState, Optional.of(normalGuard), Optional.empty(), "NormalStart");
		emergencyChoiceTransition = new SysMLTransition(contextBlock, initialStateChoice, emergencyOpsState, Optional.of(emergencyGuard), Optional.empty(), "EmergencyStart");

		normalToEmergencyTransition = new SysMLTransition(contextBlock, normalOpsState, emergencyOpsState, Optional.of(StateMachineCompletionEvent.class), Optional.empty(), Optional.of(normalToEmergencyEffect), "NormalToEmergency",
			SysMLTransitionKind.external);
		emergencyToNormalTransition = new SysMLTransition(contextBlock, emergencyOpsState, normalOpsState, Optional.of(StateMachineCompletionEvent.class), Optional.empty(), Optional.of(emergencyToNormalEffect), "EmergencyToNormal",
			SysMLTransitionKind.external);

		normalJunctionTransition = new SysMLTransition(contextBlock, normalOpsState, finalStateJunction, Optional.of(FinalEvent.class), Optional.empty(), Optional.empty(), "NormalStop");
		emergencyJunctionTransition = new SysMLTransition(contextBlock, emergencyOpsState, finalStateJunction, Optional.of(FinalEvent.class), Optional.empty(), Optional.empty(), "EmergencyStop");
		finalTransition = new FinalTransition(contextBlock, finalStateJunction, finalState, "Final");
	}

	@Override
	protected void createTransitionsUtility()
	{
		transitionsUtility = Optional.of(new StateTransitionsTransmitters(Optional.of(new StateTransitionTablesTransmitter(StateTransitionsDisplay.udpPort, true)), Optional.of(new TimingDiagramsTransmitter(TimingDiagramsDisplay.udpPort, true))));
	}
}
