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
import sysmlinjava.annotations.statemachines.Transition;
import sysmlinjava.statemachine.InitialTransition;
import sysmlinjava.statemachine.SysMLChoicePseudoState;
import sysmlinjava.statemachine.SysMLEffect;
import sysmlinjava.statemachine.SysMLEffectActivity;
import sysmlinjava.statemachine.SysMLGuard;
import sysmlinjava.statemachine.SysMLGuardCondition;
import sysmlinjava.statemachine.SysMLJunctionPseudoState;
import sysmlinjava.statemachine.SysMLState;
import sysmlinjava.statemachine.SysMLStateMachine;
import sysmlinjava.statemachine.SysMLTransition;
import sysmlinjava.valuetypes.BBoolean;
import sysmlinjava.valuetypes.DirectionDegrees;

/**
 * State machine for the {@code IntersectionSignalSystem} for emergency
 * operations. The {@code IntersectionSignalSystemEmergencyStateMachine}
 * consists of the states for each of the legal signal phases at the
 * intersection, and the legal transitions between these phases. The transitions
 * use guards to determine how to transition in light of whether or not an
 * emergency vehicle is present. Transition effects are used to perform the
 * signal phase changes. See the state machine model below for more details.
 * <p>
 * {@code IntersectionSignalSystemEmergencyStateMachine} uses the
 * {@code StateTransitionsTransmitters} to transmit state transition timing
 * information for the model execution to the {@code TimingDiagramDisplays}. If
 * the display is activated prior to model execution, timing diagram information
 * is transmitted to and received by the timing diagram display. SysMLinJava
 * provides a simple text-based display of the timing diagram information, but
 * highly capable graphical timing diagrams display applications are available
 * commercially. See SysMLinJava.com for more information.
 * 
 * @author ModelerOne
 *
 */
public class IntersectionSignalSystemEmergencyStateMachine extends SysMLStateMachine
{
	@State
	public SysMLState EGrnWRedNSRed;
	@State
	public SysMLState ERedWGrnNSRed;

	@ChoicePseudoState
	public SysMLChoicePseudoState initialStateChoice;
	@JunctionPseudoState
	public SysMLJunctionPseudoState finalStateJunction;

	@GuardCondition
	public SysMLGuardCondition choiceEGrnGuardCondition;
	@GuardCondition
	public SysMLGuardCondition choiceWGrnGuardCondition;
	@GuardCondition
	private SysMLGuardCondition emergencyVehicleAbsenceGuardCondition;

	@Guard
	public SysMLGuard choiceEGrnGuard;
	@Guard
	public SysMLGuard choiceWGrnGuard;
	@Guard
	private SysMLGuard emergencyVehicleAbsenceGuard;

	@EffectActivity
	public SysMLEffectActivity choiceEGrnEffectActivity;
	@EffectActivity
	public SysMLEffectActivity choiceWGrnEffectActivity;
	@EffectActivity
	private SysMLEffectActivity setEmergencyVehicleAbsenceEffectActivity;

	@Effect
	public SysMLEffect choiceEGrnEffect;
	@Effect
	public SysMLEffect choiceWGrnEffect;
	@Effect
	private SysMLEffect setEmergencyVehicleAbsenceEffect;

	@Transition
	public InitialTransition initialToChoice;
	@Transition
	public SysMLTransition initialChoiceToEGrn;
	@Transition
	public SysMLTransition initialChoiceToWGrn;
	@Transition
	public SysMLTransition EGrnToFinalJunction;
	@Transition
	public SysMLTransition WGrnToFinalJunction;
	@Transition
	public SysMLTransition finalJunctionToFinal;

	public IntersectionSignalSystemEmergencyStateMachine(IntersectionSignalSystem contextBlock, String name, Long id)
	{
		super(Optional.of(contextBlock), true, name);
		this.id = id;
	}

	@Override
	public void start()
	{
		StateTransitionsTransmitters transmitters = (StateTransitionsTransmitters)transitionsUtility.get();
		TimeAxis timeAxis = new TimeAxis(600.0, 30.0, 6);
		StatesAxis stateAxis = new StatesAxis(listOfStates());
		transmitters.transmit(new TimingDiagramDefinition(identityString(), stateAxis, timeAxis));
		super.start();
	}

	@Override
	public void stop()
	{
		super.stop();
	}

	@Override
	public List<String> listOfStates()
	{
		List<String> result = super.listOfStates();
		result.add(initialStateChoice.identityString());
		result.add(EGrnWRedNSRed.identityString());
		result.add(ERedWGrnNSRed.identityString());
		result.add(finalStateJunction.identityString());
		result.add(finalState.identityString());
		return result;
	}

	@Override
	protected void createStates()
	{
		super.createStates();
		EGrnWRedNSRed = new SysMLState(contextBlock, "EGrnWRedNSRed");
		ERedWGrnNSRed = new SysMLState(contextBlock, "ERedWGrnNSRed");
	}

	@Override
	protected void createPseudoStates()
	{
		super.createPseudoStates();
		initialStateChoice = new SysMLChoicePseudoState(contextBlock, "initialChoice");
		finalStateJunction = new SysMLJunctionPseudoState(contextBlock, "finalJunction");
	}

	@Override
	protected void createGuardConditions()
	{
		choiceEGrnGuardCondition = (event, contextBlock) ->
		{
			IntersectionSignalSystem intersection = (IntersectionSignalSystem)contextBlock.get();
			return intersection.emergencyVehicleDirection.equalTo(DirectionDegrees.east);
		};
		choiceWGrnGuardCondition = (event, contextBlock) ->
		{
			IntersectionSignalSystem intersection = (IntersectionSignalSystem)contextBlock.get();
			return intersection.emergencyVehicleDirection.equalTo(DirectionDegrees.west);
		};
		emergencyVehicleAbsenceGuardCondition = (event, contextBlock) ->
		{
			return event.get() instanceof EmergencyVehicleChangeEvent evChange && evChange.approachDirection.isEmpty();
		};
	}

	@Override
	protected void createGuards()
	{
		choiceEGrnGuard = new SysMLGuard(contextBlock, choiceEGrnGuardCondition, "EastBoundApproach");
		choiceWGrnGuard = new SysMLGuard(contextBlock, choiceWGrnGuardCondition, "WestBoundApproach");
		emergencyVehicleAbsenceGuard = new SysMLGuard(contextBlock, emergencyVehicleAbsenceGuardCondition, "emergencyVehicleAbsent");
	}

	@Override
	protected void createEffectActivities()
	{
		choiceEGrnEffectActivity = (event, contextBlock) ->
		{
			IntersectionSignalSystem intersection = (IntersectionSignalSystem)contextBlock.get();
			intersection.setEGrnWRedNSRed();
		};
		choiceWGrnEffectActivity = (event, contextBlock) ->
		{
			IntersectionSignalSystem intersection = (IntersectionSignalSystem)contextBlock.get();
			intersection.setERedWGrnNSRed();
		};
		setEmergencyVehicleAbsenceEffectActivity = (event, contextBlock) ->
		{
			IntersectionSignalSystem intersection = (IntersectionSignalSystem)contextBlock.get();
			intersection.emergencyVehiclePresent.setValue(BBoolean.False);
			intersection.emergencyVehicleDirection.setValue(DirectionDegrees.east);
		};
	}

	@Override
	protected void createEffects()
	{
		choiceEGrnEffect = new SysMLEffect(contextBlock, choiceEGrnEffectActivity, "setEGrnWRedNSRed");
		choiceWGrnEffect = new SysMLEffect(contextBlock, choiceWGrnEffectActivity, "setERedWGrnNSRed");
		setEmergencyVehicleAbsenceEffect = new SysMLEffect(contextBlock, setEmergencyVehicleAbsenceEffectActivity, "setEmergencyVehicleAbsent");
	}

	@Override
	protected void createTransitions()
	{
		initialToChoice = new InitialTransition(contextBlock, initialState, initialStateChoice, "InitialToChoice");
		initialChoiceToEGrn = new SysMLTransition(contextBlock, initialStateChoice, EGrnWRedNSRed, Optional.of(choiceEGrnGuard), Optional.of(choiceEGrnEffect), "InitialChoiceToEGrn");
		initialChoiceToWGrn = new SysMLTransition(contextBlock, initialStateChoice, ERedWGrnNSRed, Optional.of(choiceWGrnGuard), Optional.of(choiceWGrnEffect), "InitialChoiceToWGrn");
		EGrnToFinalJunction = new SysMLTransition(contextBlock, EGrnWRedNSRed, finalStateJunction, Optional.of(EmergencyVehicleChangeEvent.class), Optional.of(emergencyVehicleAbsenceGuard), Optional.of(setEmergencyVehicleAbsenceEffect),
			"EGrnToFinalJunction");
		WGrnToFinalJunction = new SysMLTransition(contextBlock, ERedWGrnNSRed, finalStateJunction, Optional.of(EmergencyVehicleChangeEvent.class), Optional.of(emergencyVehicleAbsenceGuard), Optional.of(setEmergencyVehicleAbsenceEffect),
			"WGrnToFinalJunction");
		finalJunctionToFinal = new SysMLTransition(contextBlock, finalStateJunction, finalState, Optional.empty(), Optional.empty(), "finalJunctionToFinal");
	}

	@Override
	protected void createTransitionsUtility()
	{
		transitionsUtility = Optional.of(new StateTransitionsTransmitters(Optional.of(new StateTransitionTablesTransmitter(StateTransitionsDisplay.udpPort, true)), Optional.of(new TimingDiagramsTransmitter(TimingDiagramsDisplay.udpPort, true))));
	}
}
