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
import sysmlinjava.annotations.statemachines.Effect;
import sysmlinjava.annotations.statemachines.EffectActivity;
import sysmlinjava.annotations.statemachines.Guard;
import sysmlinjava.annotations.statemachines.GuardCondition;
import sysmlinjava.annotations.statemachines.JunctionPseudoState;
import sysmlinjava.annotations.statemachines.OnEnterActivity;
import sysmlinjava.annotations.statemachines.State;
import sysmlinjava.annotations.statemachines.Transition;
import sysmlinjava.events.SysMLTimeEvent;
import sysmlinjava.statemachine.InitialTransition;
import sysmlinjava.statemachine.SysMLEffect;
import sysmlinjava.statemachine.SysMLEffectActivity;
import sysmlinjava.statemachine.SysMLGuard;
import sysmlinjava.statemachine.SysMLGuardCondition;
import sysmlinjava.statemachine.SysMLJunctionPseudoState;
import sysmlinjava.statemachine.SysMLOnEnterActivity;
import sysmlinjava.statemachine.SysMLState;
import sysmlinjava.statemachine.SysMLStateMachine;
import sysmlinjava.statemachine.SysMLTransition;
import sysmlinjava.statemachine.SysMLTransitionKind;
import sysmlinjava.valuetypes.BBoolean;
import sysmlinjava.valuetypes.DirectionDegrees;
import sysmlinjava.valuetypes.DurationMilliseconds;

/**
 * State machine for the {@code IntersectionSignalSystem} for normal operations.
 * The {@code IntersectionSignalSystemNormalStateMachine} consists of the states
 * for each of the legal signal phases at the intersection, and the legal
 * transitions between these phases. The transitions use guards to determine how
 * to transition in light of whether or not an emergency vehicle is present.
 * Transition effects are used to perform the signal phase changes. See the
 * state machine model below for more details.
 * <p>
 * {@code IntersectionSignalSystemNormalStateMachine} uses the
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
public class IntersectionSignalSystemNormalStateMachine extends SysMLStateMachine
{
	@State
	public SysMLState EWGrnNSRed;
	@State
	public SysMLState EWYelNSRed;
	@State
	public SysMLState EYelWGrnNSRed;
	@State
	public SysMLState EGrnWYelNSRed;
	@State
	public SysMLState EWRedNSGrn;
	@State
	public SysMLState EWRedNSYel;

	@JunctionPseudoState
	public SysMLJunctionPseudoState finalJunction;

	@OnEnterActivity
	public SysMLOnEnterActivity EWGrnNSRedEnterActivity;
	@OnEnterActivity
	public SysMLOnEnterActivity EWYelNSRedEnterActivity;
	@OnEnterActivity
	public SysMLOnEnterActivity EYelWGrnNSRedEnterActivity;
	@OnEnterActivity
	public SysMLOnEnterActivity EGrnWYelNSRedEnterActivity;
	@OnEnterActivity
	public SysMLOnEnterActivity EWRedNSGrnEnterActivity;
	@OnEnterActivity
	public SysMLOnEnterActivity EWRedNSYelEnterActivity;

	@GuardCondition
	public SysMLGuardCondition EWGrnToEWYelGuardCondition;
	@GuardCondition
	public SysMLGuardCondition EWGrnToEYelGuardCondition;
	@GuardCondition
	public SysMLGuardCondition EWGrnToWYelGuardCondition;
	@GuardCondition
	public SysMLGuardCondition NSGrnToNSYelTimedGuardCondition;
	@GuardCondition
	public SysMLGuardCondition NSGrnToNSYelEmergencyGuardCondition;
	@GuardCondition
	public SysMLGuardCondition EWYelToNSGrnGuardCondition;
	@GuardCondition
	public SysMLGuardCondition NSYelToEWGrnGuardCondition;
	@GuardCondition
	public SysMLGuardCondition EWYelToFinalJunctionGuardCondition;
	@GuardCondition
	public SysMLGuardCondition NSYelToFinalJunctionGuardCondition;
	@GuardCondition
	public SysMLGuardCondition EYelToFinalJunctionGuardCondition;
	@GuardCondition
	public SysMLGuardCondition WYelToFinalJunctionGuardCondition;

	@Guard
	public SysMLGuard EWGrnToEWYelGuard;
	@Guard
	public SysMLGuard EWGrnToEYelGuard;
	@Guard
	public SysMLGuard EWGrnToWYelGuard;
	@Guard
	public SysMLGuard NSGrnToNSYelTimedGuard;
	@Guard
	public SysMLGuard NSGrnToNSYelEmergencyGuard;
	@Guard
	public SysMLGuard EWYelToNSGrnGuard;
	@Guard
	public SysMLGuard NSYelToEWGrnGuard;
	@Guard
	public SysMLGuard EWYelToFinalJunctionGuard;
	@Guard
	public SysMLGuard NSYelToFinalJunctionGuard;
	@Guard
	public SysMLGuard EYelToFinalJunctionGuard;
	@Guard
	public SysMLGuard WYelToFinalJunctionGuard;

	@EffectActivity
	public SysMLEffectActivity setEWGrnNSRedEffectActivity;
	@EffectActivity
	public SysMLEffectActivity setEWYelNSRedEffectActivity;
	@EffectActivity
	public SysMLEffectActivity setEYelWGrnNSRedEffectActivity;
	@EffectActivity
	public SysMLEffectActivity setEGrnWYelNSRedEffectActivity;
	@EffectActivity
	public SysMLEffectActivity setEWRedNSGreenEffectActivity;
	@EffectActivity
	public SysMLEffectActivity setEWRedNSYelEffectActivity;
	@EffectActivity
	private SysMLEffectActivity setEmergencyVehiclePresenceEffectActivity;
	@EffectActivity
	private SysMLEffectActivity setEmergencyVehicleAbsenceEffectActivity;

	@Effect
	public SysMLEffect setEWGrnNSRedEffect;
	@Effect
	public SysMLEffect setEWYelNSRedEffect;
	@Effect
	public SysMLEffect setEYelWGrnNSRedEffect;
	@Effect
	public SysMLEffect setEGrnWYelNSRedEffect;
	@Effect
	public SysMLEffect setEWRedNSGreenEffect;
	@Effect
	public SysMLEffect setEWRedNSYelEffect;
	@Effect
	private SysMLEffect setEmergencyVehiclePresenceEffect;
	@Effect
	private SysMLEffect setEmergencyVehicleAbsenceEffect;

	@Transition
	public InitialTransition initialTransition;
	@Transition
	public SysMLTransition EWGrnToEWYelTransition;
	@Transition
	public SysMLTransition EWGrnToEYelTransition;
	@Transition
	public SysMLTransition EWGrnToWYelTransition;
	@Transition
	public SysMLTransition NSGrnToNSYelTimedTransition;
	@Transition
	public SysMLTransition NSGrnToNSYelEmergencyTransition;
	@Transition
	public SysMLTransition EWYelToNSGrnTransition;
	@Transition
	public SysMLTransition NSYelToEWGrnTransition;

	@Transition
	public SysMLTransition EWYelInternalTransition;
	@Transition
	public SysMLTransition NSYelInternalTransition;

	@Transition
	public SysMLTransition EWYelToFinalJunctionTransition;
	@Transition
	public SysMLTransition NSYelToFinalJunctionTransition;
	@Transition
	public SysMLTransition EYelToFinalJunctionTransition;
	@Transition
	public SysMLTransition WYelToFinalJunctionTransition;
	@Transition
	public SysMLTransition finalJunctionToFinalTransition;

	final String EWGrnNSRedTimerID = "EWGrnNSRedTimerID";
	final String EWYelNSRedTimerID = "EWYelNSRedTimerID";
	final String EYelWGrnNSRedTimerID = "EYelWGrnNSRedTimerID";
	final String EGrnWYelNSRedTimerID = "EGrnWYelNSRedTimerID";
	final String EWRedNSGrnTimerID = "EWRedNSGrnTimerID";
	final String EWRedNSYelTimerID = "EWGrnNSRedTimerID";

	private IntersectionSignalSystem intersection;
	private String identity;

	public IntersectionSignalSystemNormalStateMachine(IntersectionSignalSystem contextBlock, String name, Long id)
	{
		super(Optional.of(contextBlock), true, name);
		this.id = id;
		intersection = contextBlock;
		identity = intersection.identityString();
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
		result.add(EWGrnNSRed.identityString());
		result.add(EWYelNSRed.identityString());
		result.add(EWRedNSGrn.identityString());
		result.add(EWRedNSYel.identityString());
		result.add(EYelWGrnNSRed.identityString());
		result.add(EGrnWYelNSRed.identityString());
		result.add(finalJunction.identityString());
		result.add(finalState.identityString());
		return result;
	}

	@Override
	protected void createStateOnEnterActivities()
	{
		   EWGrnNSRedEnterActivity = (contextBlock) -> startTimer(new SysMLTimeEvent(identity +    EWGrnNSRedTimerID, DurationMilliseconds.ofSeconds((int)intersection.EWGrnNSRedDuration.value), Optional.empty()));
		   EWYelNSRedEnterActivity = (contextBlock) -> startTimer(new SysMLTimeEvent(identity +    EWYelNSRedTimerID, DurationMilliseconds.ofSeconds((int)intersection.    YellowDuration.value), Optional.empty()));
		EYelWGrnNSRedEnterActivity = (contextBlock) -> startTimer(new SysMLTimeEvent(identity + EYelWGrnNSRedTimerID, DurationMilliseconds.ofSeconds((int)intersection.    YellowDuration.value), Optional.empty()));
		EGrnWYelNSRedEnterActivity = (contextBlock) -> startTimer(new SysMLTimeEvent(identity + EGrnWYelNSRedTimerID, DurationMilliseconds.ofSeconds((int)intersection.    YellowDuration.value), Optional.empty()));
		   EWRedNSGrnEnterActivity = (contextBlock) -> startTimer(new SysMLTimeEvent(identity +    EWRedNSGrnTimerID, DurationMilliseconds.ofSeconds((int)intersection.EWRedNSGrnDuration.value), Optional.empty()));
		   EWRedNSYelEnterActivity = (contextBlock) -> startTimer(new SysMLTimeEvent(identity +    EWRedNSYelTimerID, DurationMilliseconds.ofSeconds((int)intersection.    YellowDuration.value), Optional.empty()));
	}

	@Override
	protected void createStates()
	{
		super.createStates();
		   EWGrnNSRed = new SysMLState(contextBlock, Optional.of(   EWGrnNSRedEnterActivity), Optional.empty(), Optional.empty(),    "EWGrnNSRed");
		   EWYelNSRed = new SysMLState(contextBlock, Optional.of(   EWYelNSRedEnterActivity), Optional.empty(), Optional.empty(),    "EWYelNSRed");
		EYelWGrnNSRed = new SysMLState(contextBlock, Optional.of(EYelWGrnNSRedEnterActivity), Optional.empty(), Optional.empty(), "EYelWGrnNSRed");
		EGrnWYelNSRed = new SysMLState(contextBlock, Optional.of(EGrnWYelNSRedEnterActivity), Optional.empty(), Optional.empty(), "EGrnWYelNSRed");
		   EWRedNSGrn = new SysMLState(contextBlock, Optional.of(   EWRedNSGrnEnterActivity), Optional.empty(), Optional.empty(),    "EWRedNSGrn");
		   EWRedNSYel = new SysMLState(contextBlock, Optional.of(   EWRedNSYelEnterActivity), Optional.empty(), Optional.empty(),    "EWRedNSYel");
	}

	@Override
	protected void createPseudoStates()
	{
		super.createPseudoStates();
		finalJunction = new SysMLJunctionPseudoState(contextBlock, "FinalJunction");
	}

	@Override
	protected void createGuardConditions()
	{
		EWGrnToEWYelGuardCondition = (event, contextBlock) ->
		{
			boolean isTimerEvent = event.get() instanceof SysMLTimeEvent;
			boolean isID = ((SysMLTimeEvent)event.get()).timerID.equals(identity + EWGrnNSRedTimerID);
			return isTimerEvent && isID;
		};
		EWGrnToEYelGuardCondition = (event, contextBlock) ->
		{
			return event.get() instanceof EmergencyVehicleChangeEvent evChange &&
				evChange.approachDirection.isPresent() &&
				evChange.approachDirection.get() == DirectionDegrees.east;
		};
		EWGrnToWYelGuardCondition = (event, contextBlock) ->
		{
			return event.get() instanceof EmergencyVehicleChangeEvent evChange &&
				evChange.approachDirection.isPresent() &&
				evChange.approachDirection.get() == DirectionDegrees.west;
		};
		NSGrnToNSYelTimedGuardCondition = (event, contextBlock) ->
		{
			boolean isTimerEvent = event.get() instanceof SysMLTimeEvent;
			boolean isID = ((SysMLTimeEvent)event.get()).timerID.equals(identity + EWRedNSGrnTimerID);
			return isTimerEvent && isID;
		};
		NSGrnToNSYelEmergencyGuardCondition = (event, contextBlock) ->
		{
			return event.get() instanceof EmergencyVehicleChangeEvent;
		};
		EWYelToNSGrnGuardCondition = (event, contextBlock) ->
		{
			boolean isTimerEvent = event.get() instanceof SysMLTimeEvent SysMLTimeEvent;
			boolean isID = ((SysMLTimeEvent)event.get()).timerID.equals(identity + EWYelNSRedTimerID);
			boolean evPresent = intersection.emergencyVehiclePresent.isTrue();
			return isTimerEvent && isID && !evPresent;
		};
		NSYelToEWGrnGuardCondition = (event, contextBlock) ->
		{
			boolean isTimerEvent = event.get() instanceof SysMLTimeEvent SysMLTimeEvent;
			boolean isID = ((SysMLTimeEvent)event.get()).timerID.equals(identity + EWRedNSYelTimerID);
			boolean evPresent = intersection.emergencyVehiclePresent.isTrue();
			return isTimerEvent && isID && !evPresent;
		};

		EWYelToFinalJunctionGuardCondition = (event, contextBlock) ->
		{
			boolean isTimerEvent = event.get() instanceof SysMLTimeEvent sysMLTimeEvent;
			boolean isID = ((SysMLTimeEvent)event.get()).timerID.equals(identity + EWYelNSRedTimerID);
			boolean evPresent = intersection.emergencyVehiclePresent.isTrue();
			return isTimerEvent && isID && evPresent;
		};
		NSYelToFinalJunctionGuardCondition = (event, contextBlock) ->
		{
			boolean isTimerEvent = event.get() instanceof SysMLTimeEvent sysMLTimeEvent;
			boolean isID = ((SysMLTimeEvent)event.get()).timerID.equals(identity + EWRedNSYelTimerID);
			boolean evPresent = intersection.emergencyVehiclePresent.isTrue();
			return isTimerEvent && isID && evPresent;
		};
		EYelToFinalJunctionGuardCondition = (event, contextBlock) ->
		{
			boolean isTimerEvent = event.get() instanceof SysMLTimeEvent;
			boolean isID = ((SysMLTimeEvent)event.get()).timerID.equals(identity + EYelWGrnNSRedTimerID);
			return isTimerEvent && isID;
		};
		WYelToFinalJunctionGuardCondition = (event, contextBlock) ->
		{
			boolean isTimerEvent = event.get() instanceof SysMLTimeEvent;
			boolean isID = ((SysMLTimeEvent)event.get()).timerID.equals(identity + EGrnWYelNSRedTimerID);
			return isTimerEvent && isID;
		};
	}

	@Override
	protected void createGuards()
	{
		EWGrnToEWYelGuard = new SysMLGuard(contextBlock, EWGrnToEWYelGuardCondition, "isEWGrnToEWYelTimer");
		EWGrnToEYelGuard = new SysMLGuard(contextBlock, EWGrnToEYelGuardCondition, "isEWGrnToEYelEmergency");
		EWGrnToWYelGuard = new SysMLGuard(contextBlock, EWGrnToWYelGuardCondition, "isEWGrnToWYelEmergency");
		EWYelToNSGrnGuard = new SysMLGuard(contextBlock, EWYelToNSGrnGuardCondition, "isEWYelToNSGrnTimer");
		NSGrnToNSYelTimedGuard = new SysMLGuard(contextBlock, NSGrnToNSYelTimedGuardCondition, "isNSGrnToNSYelTimer");
		NSGrnToNSYelEmergencyGuard = new SysMLGuard(contextBlock, NSGrnToNSYelEmergencyGuardCondition, "isNSGrnToNSYelEmergency");
		NSYelToEWGrnGuard = new SysMLGuard(contextBlock, NSYelToEWGrnGuardCondition, "isNSYelToEWGrnTimerNoEmergency");

		NSYelToFinalJunctionGuard = new SysMLGuard(contextBlock, NSYelToFinalJunctionGuardCondition, "isNSYelToFinalJunctionTimerEmergency");
		EWYelToFinalJunctionGuard = new SysMLGuard(contextBlock, EWYelToFinalJunctionGuardCondition, "isEWYelToFinalJunctionTimerEmergency");
		EYelToFinalJunctionGuard = new SysMLGuard(contextBlock, EYelToFinalJunctionGuardCondition, "isEYelToFinalJunctionTimer");
		WYelToFinalJunctionGuard = new SysMLGuard(contextBlock, WYelToFinalJunctionGuardCondition, "isWYelToFinalJunctionTimer");
	}

	@Override
	protected void createEffectActivities()
	{
		setEWGrnNSRedEffectActivity = (event, contextBlock) ->
		{
			IntersectionSignalSystem intersection = (IntersectionSignalSystem)contextBlock.get();
			intersection.setEWGrnNSRed();
		};
		setEWYelNSRedEffectActivity = (event, contextBlock) ->
		{
			IntersectionSignalSystem intersection = (IntersectionSignalSystem)contextBlock.get();
			intersection.setEWYelNSRed();
		};
		setEYelWGrnNSRedEffectActivity = (event, contextBlock) ->
		{
			IntersectionSignalSystem intersection = (IntersectionSignalSystem)contextBlock.get();
			intersection.setEYelWGrnNSRed();
			if (event.get() instanceof EmergencyVehicleChangeEvent evPresentEvent)
			{
				intersection.emergencyVehiclePresent.setValue(BBoolean.True);
				intersection.emergencyVehicleDirection.setValue(DirectionDegrees.east.value);
			}
		};
		setEGrnWYelNSRedEffectActivity = (event, contextBlock) ->
		{
			IntersectionSignalSystem intersection = (IntersectionSignalSystem)contextBlock.get();
			intersection.setEGrnWYelNSRed();
			if (event.get() instanceof EmergencyVehicleChangeEvent evPresentEvent)
			{
				intersection.emergencyVehiclePresent.setValue(BBoolean.True);
				intersection.emergencyVehicleDirection.setValue(DirectionDegrees.west.value);
			}
		};
		setEWRedNSGreenEffectActivity = (event, contextBlock) ->
		{
			IntersectionSignalSystem intersection = (IntersectionSignalSystem)contextBlock.get();
			intersection.setEWRedNSGreen();
		};
		setEWRedNSYelEffectActivity = (event, contextBlock) ->
		{
			IntersectionSignalSystem intersection = (IntersectionSignalSystem)contextBlock.get();
			intersection.setEWRedNSYel();
			if (event.get() instanceof EmergencyVehicleChangeEvent evPresentEvent)
			{
				intersection.emergencyVehiclePresent.setValue(BBoolean.True);
				intersection.emergencyVehicleDirection.setValue(DirectionDegrees.east.value);
			}
		};
		setEmergencyVehiclePresenceEffectActivity = (event, contextBlock) ->
		{
			IntersectionSignalSystem intersection = (IntersectionSignalSystem)contextBlock.get();
			intersection.emergencyVehiclePresent.setValue(BBoolean.True);
			if (event.get() instanceof EmergencyVehicleChangeEvent evPresentEvent)
				if (evPresentEvent.approachDirection.isPresent())
					intersection.emergencyVehicleDirection.setValue(evPresentEvent.approachDirection.get().value);
		};
		setEmergencyVehicleAbsenceEffectActivity = (event, contextBlock) ->
		{
			IntersectionSignalSystem intersection = (IntersectionSignalSystem)contextBlock.get();
			intersection.emergencyVehiclePresent.setValue(BBoolean.False);
			intersection.emergencyVehicleDirection.setValue(DirectionDegrees.east.value);
		};
	}

	@Override
	protected void createEffects()
	{
		setEWGrnNSRedEffect = new SysMLEffect(contextBlock, setEWGrnNSRedEffectActivity, "SetEWGrnNSRed");
		setEWYelNSRedEffect = new SysMLEffect(contextBlock, setEWYelNSRedEffectActivity, "SetEWYelNSRed");
		setEYelWGrnNSRedEffect = new SysMLEffect(contextBlock, setEYelWGrnNSRedEffectActivity, "SetEYelWGrnNSRed");
		setEGrnWYelNSRedEffect = new SysMLEffect(contextBlock, setEGrnWYelNSRedEffectActivity, "SetEGrnWYelNSRed");
		setEWRedNSGreenEffect = new SysMLEffect(contextBlock, setEWRedNSGreenEffectActivity, "SetEWRedNSGreen");
		setEWRedNSYelEffect = new SysMLEffect(contextBlock, setEWRedNSYelEffectActivity, "SetEWRedNSYel");
		setEmergencyVehiclePresenceEffect = new SysMLEffect(contextBlock, setEmergencyVehiclePresenceEffectActivity, "SetEmergencyVehiclePresent");
		setEmergencyVehicleAbsenceEffect = new SysMLEffect(contextBlock, setEmergencyVehicleAbsenceEffectActivity, "SetEmergencyVehicleAbsent");
	}

	@Override
	protected void createTransitions()
	{
		initialTransition = new InitialTransition(contextBlock, initialState, EWGrnNSRed, setEmergencyVehicleAbsenceEffect, "Initial");
		EWGrnToEWYelTransition = new SysMLTransition(contextBlock, EWGrnNSRed, EWYelNSRed, Optional.of(SysMLTimeEvent.class), Optional.of(EWGrnToEWYelGuard), Optional.of(setEWYelNSRedEffect), "EWGrnToEWYel", SysMLTransitionKind.external);
		EWGrnToEYelTransition = new SysMLTransition(contextBlock, EWGrnNSRed, EYelWGrnNSRed, Optional.of(EmergencyVehicleChangeEvent.class), Optional.of(EWGrnToEYelGuard), Optional.of(setEYelWGrnNSRedEffect), "EWGrnToEYel",
			SysMLTransitionKind.external);
		EWGrnToWYelTransition = new SysMLTransition(contextBlock, EWGrnNSRed, EGrnWYelNSRed, Optional.of(EmergencyVehicleChangeEvent.class), Optional.of(EWGrnToWYelGuard), Optional.of(setEGrnWYelNSRedEffect), "EWGrnToWYel",
			SysMLTransitionKind.external);
		NSGrnToNSYelTimedTransition = new SysMLTransition(contextBlock, EWRedNSGrn, EWRedNSYel, Optional.of(SysMLTimeEvent.class), Optional.of(NSGrnToNSYelTimedGuard), Optional.of(setEWRedNSYelEffect), "NSGrnToNSYelTimed",
			SysMLTransitionKind.external);
		NSGrnToNSYelEmergencyTransition = new SysMLTransition(contextBlock, EWRedNSGrn, EWRedNSYel, Optional.of(EmergencyVehicleChangeEvent.class), Optional.empty(), Optional.of(setEWRedNSYelEffect), "NSGrnToNSYelEmergency",
			SysMLTransitionKind.external);
		EWYelToNSGrnTransition = new SysMLTransition(contextBlock, EWYelNSRed, EWRedNSGrn, Optional.of(SysMLTimeEvent.class), Optional.of(EWYelToNSGrnGuard), Optional.of(setEWRedNSGreenEffect), "EWYelToNSGrn",
			SysMLTransitionKind.external);
		NSYelToEWGrnTransition = new SysMLTransition(contextBlock, EWRedNSYel, EWGrnNSRed, Optional.of(SysMLTimeEvent.class), Optional.of(NSYelToEWGrnGuard), Optional.of(setEWGrnNSRedEffect), "NSYelToEWGrn", SysMLTransitionKind.external);

		EWYelInternalTransition = new SysMLTransition(contextBlock, EWYelNSRed, EWYelNSRed, Optional.of(EmergencyVehicleChangeEvent.class), Optional.empty(), Optional.of(setEmergencyVehiclePresenceEffect), "EWYelInternal",
			SysMLTransitionKind.internal);
		NSYelInternalTransition = new SysMLTransition(contextBlock, EWRedNSYel, EWRedNSYel, Optional.of(EmergencyVehicleChangeEvent.class), Optional.empty(), Optional.of(setEmergencyVehiclePresenceEffect), "NSYelInternal",
			SysMLTransitionKind.internal);

		EWYelToFinalJunctionTransition = new SysMLTransition(contextBlock, EWYelNSRed, finalJunction, Optional.of(SysMLTimeEvent.class), Optional.of(EWYelToFinalJunctionGuard), Optional.empty(), "EWYelToFinalJunction");
		NSYelToFinalJunctionTransition = new SysMLTransition(contextBlock, EWRedNSYel, finalJunction, Optional.of(SysMLTimeEvent.class), Optional.of(NSYelToFinalJunctionGuard), Optional.empty(), "NSYelToFinalJunction");
		EYelToFinalJunctionTransition = new SysMLTransition(contextBlock, EYelWGrnNSRed, finalJunction, Optional.of(SysMLTimeEvent.class), Optional.of(EYelToFinalJunctionGuard), Optional.empty(), "EYelToFinalJunction");
		WYelToFinalJunctionTransition = new SysMLTransition(contextBlock, EGrnWYelNSRed, finalJunction, Optional.of(SysMLTimeEvent.class), Optional.of(WYelToFinalJunctionGuard), Optional.empty(), "WYelToFinalJunction");
		finalJunctionToFinalTransition = new SysMLTransition(contextBlock, finalJunction, finalState, Optional.empty(), Optional.empty(), "FinalJunctionToFinal");
	}

	@Override
	protected void createTransitionsUtility()
	{
		transitionsUtility = Optional.of(new StateTransitionsTransmitters(Optional.of(new StateTransitionTablesTransmitter(StateTransitionsDisplay.udpPort, true)), Optional.of(new TimingDiagramsTransmitter(TimingDiagramsDisplay.udpPort, true))));
	}
}
