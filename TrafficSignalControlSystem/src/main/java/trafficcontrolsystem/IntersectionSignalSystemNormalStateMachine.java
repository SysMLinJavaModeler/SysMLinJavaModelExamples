package trafficcontrolsystem;

import java.util.List;
import java.util.Optional;

import sysmlinjava.attributetypes.BBoolean;
import sysmlinjava.attributetypes.DirectionDegrees;
import sysmlinjava.attributetypes.DurationMilliseconds;
import sysmlinjava.events.SysMLChangeEvent;
import sysmlinjava.events.SysMLTimeEvent;
import sysmlinjava.javaannotations.statemachines.Effect;
import sysmlinjava.javaannotations.statemachines.EffectActionFunction;
import sysmlinjava.javaannotations.statemachines.EntryActionFunction;
import sysmlinjava.javaannotations.statemachines.Guard;
import sysmlinjava.javaannotations.statemachines.GuardCondition;
import sysmlinjava.javaannotations.statemachines.JunctionPseudoState;
import sysmlinjava.javaannotations.statemachines.State;
import sysmlinjava.javaannotations.statemachines.Transition;
import sysmlinjava.states.FinalTransition;
import sysmlinjava.states.InitialTransition;
import sysmlinjava.states.SysMLEffect;
import sysmlinjava.states.SysMLEffectActionFunction;
import sysmlinjava.states.SysMLEntryActionFunction;
import sysmlinjava.states.SysMLGuard;
import sysmlinjava.states.SysMLGuardCondition;
import sysmlinjava.states.SysMLJunctionPseudoState;
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
 */
public class IntersectionSignalSystemNormalStateMachine extends SysMLStateMachine
{
	@State
	private SysMLState EWGrnNSRed;
	@State
	private SysMLState EWYelNSRed;
	@State
	private SysMLState EWRedNSGrn;
	@State
	private SysMLState EWRedNSYel;

	@JunctionPseudoState
	private SysMLJunctionPseudoState emergencyVehicleOnGreenJunction;
	@JunctionPseudoState
	private SysMLJunctionPseudoState emergencyVehicleOnYellowJunction;

	@EntryActionFunction
	private SysMLEntryActionFunction setTimerForEWGrnNSRed;
	@EntryActionFunction
	private SysMLEntryActionFunction setTimerForEWYelNSRed;
	@EntryActionFunction
	private SysMLEntryActionFunction setTimerForEWRedNSGrn;
	@EntryActionFunction
	private SysMLEntryActionFunction setTimerForEWRedNSYel;

	@GuardCondition
	private SysMLGuardCondition isEWGrnToEWYelTimeNoEVGuardCondition;
	@GuardCondition
	private SysMLGuardCondition isNSGrnToNSYelTimeNoEVGuardCondition;
	@GuardCondition
	private SysMLGuardCondition isEWYelToEWRedTimeNoEVGuardCondition;
	@GuardCondition
	private SysMLGuardCondition isNSYelToNSRedTimeNoEVGuardCondition;
	@GuardCondition
	private SysMLGuardCondition EWYelToEmergencyTimeGuardCondition;
	@GuardCondition
	private SysMLGuardCondition NSYelToEmergencyTimeGuardCondition;
	@GuardCondition
	private SysMLGuardCondition emergencyVehiclePresentGuardCondition;
	@GuardCondition
	private SysMLGuardCondition emergencyVehicleEastboundPresentGuardCondition;
	@GuardCondition
	private SysMLGuardCondition emergencyVehicleWestboundPresentGuardCondition;
	@GuardCondition
	private SysMLGuardCondition emergencyVehicleEastboundTimeGuardCondition;
	@GuardCondition
	private SysMLGuardCondition emergencyVehicleWestboundTimeGuardCondition;

	@Guard
	private SysMLGuard isEWGrnToEWYelTimeNoEVGuard;
	@Guard
	private SysMLGuard isNSGrnToNSYelTimeNoEVGuard;
	@Guard
	private SysMLGuard isEWYelToEWRedTimeNoEVGuard;
	@Guard
	private SysMLGuard isNSYelToNSRedTimeNoEVGuard;
	@Guard
	private SysMLGuard EWYelToEmergencyTimeGuard;
	@Guard
	private SysMLGuard NSYelToEmergencyTimeGuard;
	@Guard
	private SysMLGuard emergencyVehiclePresentGuard;
	@Guard
	private SysMLGuard emergencyVehicleEastboundPresentGuard;
	@Guard
	private SysMLGuard emergencyVehicleWestboundPresentGuard;
	@Guard
	private SysMLGuard emergencyVehicleEastboundTimeGuard;
	@Guard
	private SysMLGuard emergencyVehicleWestboundTimeGuard;

	@EffectActionFunction
	private SysMLEffectActionFunction setEWGrnNSRedEffectAction;
	@EffectActionFunction
	private SysMLEffectActionFunction setEWYelNSRedEffectAction;
	@EffectActionFunction
	private SysMLEffectActionFunction setEWRedNSGreenEffectAction;
	@EffectActionFunction
	private SysMLEffectActionFunction setEWRedNSYelEffectAction;
	@EffectActionFunction
	private SysMLEffectActionFunction setEWRedNSYelEmergencyEffectAction;
	@EffectActionFunction
	private SysMLEffectActionFunction setEmergencyVehiclePresentEffectAction;
	@EffectActionFunction
	private SysMLEffectActionFunction setEmergencyVehicleAbsentEffectAction;

	@Effect
	private SysMLEffect setEWGrnNSRedEffect;
	@Effect
	private SysMLEffect setEWYelNSRedEffect;
	@Effect
	private SysMLEffect setEWRedNSGrnEffect;
	@Effect
	private SysMLEffect setEWRedNSYelEffect;
	@Effect
	private SysMLEffect setEWRedNSYelEmergencyEffect;
	@Effect
	private SysMLEffect setEmergencyVehiclePresentEffect;
	@Effect
	private SysMLEffect setEmergencyVehicleAbsentEffect;

	@Transition
	private InitialTransition initialTransition;
	@Transition
	private SysMLTransition EWGrnToEWYelTimeTransition;
	@Transition
	private SysMLTransition EWYelToEWRedTimeTransition;
	@Transition
	private SysMLTransition NSGrnToNSYelTimeTransition;
	@Transition
	private SysMLTransition NSYelToNSRedTimeTransition;

	@Transition
	private SysMLTransition EWGrnToEastboundEmergencyTransition;
	@Transition
	private SysMLTransition EWGrnToWestboundEmergencyTransition;
	@Transition
	private SysMLTransition NSGrnToNSYelEmergencyTransition;

	@Transition
	private SysMLTransition EWYelEmergencyTransition;
	@Transition
	private SysMLTransition NSYelEmergencyTransition;

	@Transition
	private SysMLTransition EWYelToEastboundEmergencyTimeTransition;
	@Transition
	private SysMLTransition EWYelToWestboundEmergencyTimeTransition;
	@Transition
	private SysMLTransition NSYelToEastboundEmergencyTimeTransition;
	@Transition
	private SysMLTransition NSYelToWestboundEmergencyTimeTransition;

	@Transition
	private SysMLTransition emergencyVehicleOnGreenJunctionToFinalTransition;
	@Transition
	private SysMLTransition emergencyVehicleOnYellowJunctionToFinalTransition;

	final String NSGrnToNSYelTimerID = "NSGrnToNSYelTimerID";
	final String NSYelToEWGrnTimerID = "NSYelToEWGrnTimerID";
	final String EWGrnToEWYelTimerID = "EWGrnToEWYelTimerID";
	final String EWYelToNSGrnTimerID = "EWYelToNSGrnTimerID";
	final String EWYelToFinalTimerID = "EWYelToFinalTimerID";
	final String NSYelToFinalTimerID = "NSYelToFinalTimerID";

	private IntersectionSignalSystem intersection;
	String identity;

	/**
	 * Constructor
	 * 
	 * @param contextSystem system for whish this is the state machine
	 * @param name          unique name
	 * @param id            unique identifier
	 */
	public IntersectionSignalSystemNormalStateMachine(IntersectionSignalSystem contextSystem, String name, Long id)
	{
		super(Optional.of(contextSystem), true, name);
		this.id = id;
		intersection = contextSystem;
		identity = intersection.identityString();
	}

	@Override
	public void start()
	{
		StateTransitionsTransmitters transmitters = (StateTransitionsTransmitters) transitionsUtility.get();
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
		result.add(emergencyVehicleOnGreenJunction.identityString());
		result.add(emergencyVehicleOnYellowJunction.identityString());
		result.add(finalState.identityString());
		return result;
	}

	@SuppressWarnings("unused")
	@Override
	protected void createStateEntryActionFunctions()
	{
		setTimerForEWGrnNSRed = (context) -> startTimer(new SysMLTimeEvent(identity + EWGrnToEWYelTimerID, DurationMilliseconds.of(intersection.EWGrnNSRedDuration),
		Optional.empty()));
		setTimerForEWYelNSRed = (context) -> startTimer(new SysMLTimeEvent(identity + EWYelToNSGrnTimerID, DurationMilliseconds.of(intersection.YellowDuration), Optional.empty()));
		setTimerForEWRedNSGrn = (context) -> startTimer(new SysMLTimeEvent(identity + NSGrnToNSYelTimerID, DurationMilliseconds.of(intersection.EWRedNSGrnDuration),
		Optional.empty()));
		setTimerForEWRedNSYel = (context) -> startTimer(new SysMLTimeEvent(identity + NSYelToEWGrnTimerID, DurationMilliseconds.of(intersection.YellowDuration), Optional.empty()));
	}

	@Override
	protected void createStates()
	{
		super.createStates();
		EWGrnNSRed = new SysMLState(context, Optional.of(setTimerForEWGrnNSRed), Optional.empty(), Optional.empty(), "EWGrnNSRed");
		EWYelNSRed = new SysMLState(context, Optional.of(setTimerForEWYelNSRed), Optional.empty(), Optional.empty(), "EWYelNSRed");
		EWRedNSGrn = new SysMLState(context, Optional.of(setTimerForEWRedNSGrn), Optional.empty(), Optional.empty(), "EWRedNSGrn");
		EWRedNSYel = new SysMLState(context, Optional.of(setTimerForEWRedNSYel), Optional.empty(), Optional.empty(), "EWRedNSYel");
	}

	@Override
	protected void createPseudoStates()
	{
		super.createPseudoStates();
		emergencyVehicleOnGreenJunction = new SysMLJunctionPseudoState(context, "EmergencyVehicleOnGreenJunction");
		emergencyVehicleOnYellowJunction = new SysMLJunctionPseudoState(context, "EmergencyVehicleOnYellowJunction");
	}

	@SuppressWarnings("unused")
	@Override
	protected void createGuardConditions()
	{
		isEWGrnToEWYelTimeNoEVGuardCondition = (event, context) ->
		{
			return ((SysMLTimeEvent) event.get()).timerID.equals(identity + EWGrnToEWYelTimerID) && !intersection.emergencyVehiclePresent.isTrue();
		};
		isNSGrnToNSYelTimeNoEVGuardCondition = (event, context) ->
		{
			return ((SysMLTimeEvent) event.get()).timerID.equals(identity + NSGrnToNSYelTimerID) && !intersection.emergencyVehiclePresent.isTrue();
		};
		isEWYelToEWRedTimeNoEVGuardCondition = (event, context) ->
		{
			return ((SysMLTimeEvent) event.get()).timerID.equals(identity + EWYelToNSGrnTimerID) && !intersection.emergencyVehiclePresent.isTrue();
		};
		isNSYelToNSRedTimeNoEVGuardCondition = (event, context) ->
		{
			return ((SysMLTimeEvent) event.get()).timerID.equals(identity + NSYelToEWGrnTimerID) && !intersection.emergencyVehiclePresent.isTrue();
		};

		EWYelToEmergencyTimeGuardCondition = (event, context) ->
		{
			return ((SysMLTimeEvent) event.get()).timerID.equals(identity + EWYelToFinalTimerID) && intersection.emergencyVehiclePresent.isTrue();
		};
		NSYelToEmergencyTimeGuardCondition = (event, context) ->
		{
			return ((SysMLTimeEvent) event.get()).timerID.equals(identity + NSYelToFinalTimerID) && intersection.emergencyVehiclePresent.isTrue();
		};

		emergencyVehiclePresentGuardCondition = (event, context) ->
		{
			return !((SysMLChangeEvent) event.get()).changeExpression.split("=")[1].equals("none");
		};
		emergencyVehicleEastboundPresentGuardCondition = (event, context) ->
		{
			return ((SysMLChangeEvent) event.get()).changeExpression.split("=")[1].equals("east");
		};
		emergencyVehicleWestboundPresentGuardCondition = (event, context) ->
		{
			return ((SysMLChangeEvent) event.get()).changeExpression.split("=")[1].equals("west");
		};

		emergencyVehicleEastboundTimeGuardCondition = (event, context) ->
		{
			return intersection.emergencyVehiclePresent.isTrue() && intersection.emergencyVehicleDirection.equals(DirectionDegrees.east);
		};
		emergencyVehicleWestboundTimeGuardCondition = (event, context) ->
		{
			return intersection.emergencyVehiclePresent.isTrue() && intersection.emergencyVehicleDirection.equals(DirectionDegrees.west);
		};
	}

	@Override
	protected void createGuards()
	{
		isEWGrnToEWYelTimeNoEVGuard = new SysMLGuard(context, isEWGrnToEWYelTimeNoEVGuardCondition, "isEWGrnToEWYelTime");
		isEWYelToEWRedTimeNoEVGuard = new SysMLGuard(context, isEWYelToEWRedTimeNoEVGuardCondition, "isEWYelToNSGrnTime");
		isNSGrnToNSYelTimeNoEVGuard = new SysMLGuard(context, isNSGrnToNSYelTimeNoEVGuardCondition, "isNSGrnToNSYelTime");
		isNSYelToNSRedTimeNoEVGuard = new SysMLGuard(context, isNSYelToNSRedTimeNoEVGuardCondition, "isNSYelToEWGrnTimeNoEmergency");

		EWYelToEmergencyTimeGuard = new SysMLGuard(context, EWYelToEmergencyTimeGuardCondition, "isEWYelToEmergencyTime");
		NSYelToEmergencyTimeGuard = new SysMLGuard(context, NSYelToEmergencyTimeGuardCondition, "isNSYelToEmergencyTime");

		emergencyVehiclePresentGuard = new SysMLGuard(context, emergencyVehiclePresentGuardCondition, "emergencyVehiclePresent");
		emergencyVehicleWestboundPresentGuard = new SysMLGuard(context, emergencyVehicleWestboundPresentGuardCondition, "emergencyVehicleWestboundPresent");
		emergencyVehicleEastboundPresentGuard = new SysMLGuard(context, emergencyVehicleEastboundPresentGuardCondition, "emergencyVehicleEastboundPresent");
		emergencyVehicleWestboundTimeGuard = new SysMLGuard(context, emergencyVehicleWestboundTimeGuardCondition, "emergencyVehicleWestboundTime");
		emergencyVehicleEastboundTimeGuard = new SysMLGuard(context, emergencyVehicleEastboundTimeGuardCondition, "emergencyVehicleEastboundTime");
	}

	@SuppressWarnings("unused")
	@Override
	protected void createEffectActionFunctions()
	{
		setEWGrnNSRedEffectAction = (event, context) ->
		{
			logger.info(intersection.getName() + " - setEWGrnNSRed");
			IntersectionSignalSystem intersection = (IntersectionSignalSystem) context.get();
			intersection.setEWGrnNSRed();
		};
		setEWYelNSRedEffectAction = (event, context) ->
		{
			logger.info(intersection.getName() + " - setEWYelNSRed");
			IntersectionSignalSystem intersection = (IntersectionSignalSystem) context.get();
			intersection.setEWYelNSRed();
		};
		setEWRedNSGreenEffectAction = (event, context) ->
		{
			logger.info(intersection.getName() + " - setEWRedNSGreen");
			IntersectionSignalSystem intersection = (IntersectionSignalSystem) context.get();
			intersection.setEWRedNSGreen();
		};
		setEWRedNSYelEffectAction = (event, context) ->
		{
			logger.info(intersection.getName() + " - setEWRedNSYel");
			IntersectionSignalSystem intersection = (IntersectionSignalSystem) context.get();
			intersection.setEWRedNSYel();
		};

		setEWRedNSYelEmergencyEffectAction = (event, context) ->
		{
			logger.info(intersection.getName() + " - setEWRedNSYelEmergency");
			IntersectionSignalSystem intersection = (IntersectionSignalSystem) context.get();
			intersection.setEWRedNSYel();
			intersection.emergencyVehiclePresent.setValue(BBoolean.True);
			intersection.emergencyVehicleDirection.setValue(DirectionDegrees.east.value);
		};

		setEmergencyVehiclePresentEffectAction = (event, context) ->
		{
			logger.info(intersection.getName() + " - setEmergencyVehiclePresent");
			IntersectionSignalSystem intersection = (IntersectionSignalSystem) context.get();
			intersection.emergencyVehiclePresent.setValue(BBoolean.True);
			if (((SysMLChangeEvent) event.get()).changeExpression.split("=")[1].equals("west"))
				intersection.emergencyVehicleDirection.setValue(DirectionDegrees.west);
			else if (((SysMLChangeEvent) event.get()).changeExpression.split("=")[1].equals("east"))
				intersection.emergencyVehicleDirection.setValue(DirectionDegrees.east);
		};
		setEmergencyVehicleAbsentEffectAction = (event, context) ->
		{
			logger.info(intersection.getName() + " - setsetEmergencyVehicleAbsent");
			IntersectionSignalSystem intersection = (IntersectionSignalSystem) context.get();
			intersection.emergencyVehiclePresent.setValue(BBoolean.False);
			intersection.emergencyVehicleDirection.setValue(DirectionDegrees.east.value);
		};

	}

	@Override
	protected void createEffects()
	{
		setEWGrnNSRedEffect = new SysMLEffect(context, setEWGrnNSRedEffectAction, "SetEWGrnNSRed");
		setEWYelNSRedEffect = new SysMLEffect(context, setEWYelNSRedEffectAction, "SetEWYelNSRed");
		setEWRedNSGrnEffect = new SysMLEffect(context, setEWRedNSGreenEffectAction, "SetEWRedNSGreen");
		setEWRedNSYelEffect = new SysMLEffect(context, setEWRedNSYelEffectAction, "SetEWRedNSYel");
		setEWRedNSYelEmergencyEffect = new SysMLEffect(context, setEWRedNSYelEmergencyEffectAction, "SetEWRedNSYelEmergency");
		setEmergencyVehiclePresentEffect = new SysMLEffect(context, setEmergencyVehiclePresentEffectAction, "SetEmergencyVehiclePresent");
		setEmergencyVehicleAbsentEffect = new SysMLEffect(context, setEmergencyVehicleAbsentEffectAction, "SetEmergencyVehicleAbsent");
	}

	@Override
	protected void createTransitions()
	{
		initialTransition = new InitialTransition(context, initialState, EWGrnNSRed, setEmergencyVehicleAbsentEffect, "Initial");
		EWGrnToEWYelTimeTransition = new SysMLTransition(context, EWGrnNSRed, EWYelNSRed, Optional.of(SysMLTimeEvent.class), Optional.of(isEWGrnToEWYelTimeNoEVGuard),
		Optional.of(setEWYelNSRedEffect), "EWGrnToEWYel", SysMLTransitionKind.external);
		EWYelToEWRedTimeTransition = new SysMLTransition(context, EWYelNSRed, EWRedNSGrn, Optional.of(SysMLTimeEvent.class), Optional.of(isEWYelToEWRedTimeNoEVGuard),
		Optional.of(setEWRedNSGrnEffect), "EWYelToEWRed", SysMLTransitionKind.external);
		NSGrnToNSYelTimeTransition = new SysMLTransition(context, EWRedNSGrn, EWRedNSYel, Optional.of(SysMLTimeEvent.class), Optional.of(isNSGrnToNSYelTimeNoEVGuard),
		Optional.of(setEWRedNSYelEffect), "NSGrnToNSYel", SysMLTransitionKind.external);
		NSYelToNSRedTimeTransition = new SysMLTransition(context, EWRedNSYel, EWGrnNSRed, Optional.of(SysMLTimeEvent.class), Optional.of(isNSYelToNSRedTimeNoEVGuard),
		Optional.of(setEWGrnNSRedEffect), "NSYelToNSRed", SysMLTransitionKind.external);

		EWGrnToEastboundEmergencyTransition = new SysMLTransition(context, EWGrnNSRed, emergencyVehicleOnGreenJunction, Optional.of(SysMLChangeEvent.class),
		Optional.of(emergencyVehicleEastboundPresentGuard), Optional.of(setEmergencyVehiclePresentEffect), "EWGrnToEastboundEmergency");
		EWGrnToWestboundEmergencyTransition = new SysMLTransition(context, EWGrnNSRed, emergencyVehicleOnGreenJunction, Optional.of(SysMLChangeEvent.class),
		Optional.of(emergencyVehicleWestboundPresentGuard), Optional.of(setEmergencyVehiclePresentEffect), "EWGrnToWestboundEmergency");
		NSGrnToNSYelEmergencyTransition = new SysMLTransition(context, EWRedNSGrn, EWRedNSYel, Optional.of(SysMLChangeEvent.class), Optional.of(emergencyVehiclePresentGuard),
		Optional.of(setEmergencyVehiclePresentEffect), "NSGrnToNSYelEmergency", SysMLTransitionKind.external);

		EWYelEmergencyTransition = new SysMLTransition(context, EWYelNSRed, EWYelNSRed, Optional.of(SysMLChangeEvent.class), Optional.of(emergencyVehiclePresentGuard),
		Optional.of(setEmergencyVehiclePresentEffect), "EWYelEmergency", SysMLTransitionKind.internal);
		NSYelEmergencyTransition = new SysMLTransition(context, EWRedNSYel, EWRedNSYel, Optional.of(SysMLChangeEvent.class), Optional.of(emergencyVehiclePresentGuard),
		Optional.of(setEmergencyVehiclePresentEffect), "NSYelEmergency", SysMLTransitionKind.internal);

		EWYelToEastboundEmergencyTimeTransition = new SysMLTransition(context, EWYelNSRed, emergencyVehicleOnYellowJunction, Optional.of(SysMLTimeEvent.class),
		Optional.of(emergencyVehicleEastboundTimeGuard), Optional.empty(), "EWYelToEastboundEmergency");
		EWYelToWestboundEmergencyTimeTransition = new SysMLTransition(context, EWYelNSRed, emergencyVehicleOnYellowJunction, Optional.of(SysMLTimeEvent.class),
		Optional.of(emergencyVehicleWestboundTimeGuard), Optional.empty(), "EWYelToWestboundEmergency");
		NSYelToEastboundEmergencyTimeTransition = new SysMLTransition(context, EWRedNSYel, emergencyVehicleOnYellowJunction, Optional.of(SysMLTimeEvent.class),
		Optional.of(emergencyVehicleEastboundTimeGuard), Optional.empty(), "NWYelToEastboundEmergency");
		NSYelToWestboundEmergencyTimeTransition = new SysMLTransition(context, EWRedNSYel, emergencyVehicleOnYellowJunction, Optional.of(SysMLTimeEvent.class),
		Optional.of(emergencyVehicleWestboundTimeGuard), Optional.empty(), "NSYelToWestboundEmergency");

		emergencyVehicleOnGreenJunctionToFinalTransition = new FinalTransition(context, emergencyVehicleOnGreenJunction, finalState, "NSYelToFinalJunction");
		emergencyVehicleOnYellowJunctionToFinalTransition = new FinalTransition(context, emergencyVehicleOnYellowJunction, finalState, "NSYelToFinalJunction");
	}

	@Override
	protected void createTransitionsUtility()
	{
		transitionsUtility = Optional.of(new StateTransitionsTransmitters(Optional.of(new StateTransitionTablesTransmitter(StateTransitionsDisplay.udpPort, false)),
		Optional.of(new TimingDiagramsTransmitter(TimingDiagramsDisplay.udpPort, false)), false));
	}
}
