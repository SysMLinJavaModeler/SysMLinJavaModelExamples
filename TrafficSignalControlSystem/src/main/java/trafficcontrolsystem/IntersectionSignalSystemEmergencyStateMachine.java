package trafficcontrolsystem;

import java.util.List;
import java.util.Optional;

import sysmlinjava.attributetypes.BBoolean;
import sysmlinjava.attributetypes.DirectionDegrees;
import sysmlinjava.attributetypes.DurationMilliseconds;
import sysmlinjava.events.SysMLChangeEvent;
import sysmlinjava.events.SysMLTimeEvent;
import sysmlinjava.javaannotations.statemachines.ChoicePseudoState;
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
import sysmlinjava.states.SysMLChoicePseudoState;
import sysmlinjava.states.SysMLEffect;
import sysmlinjava.states.SysMLEffectActionFunction;
import sysmlinjava.states.SysMLEntryActionFunction;
import sysmlinjava.states.SysMLGuard;
import sysmlinjava.states.SysMLGuardCondition;
import sysmlinjava.states.SysMLJunctionPseudoState;
import sysmlinjava.states.SysMLState;
import sysmlinjava.states.SysMLStateMachine;
import sysmlinjava.states.SysMLTransition;
import sysmlinjava.views.statetransitionstables.StateTransitionTablesTransmitter;
import sysmlinjava.views.statetransitionstables.StateTransitionsDisplay;
import sysmlinjava.views.statetransitionstables.StateTransitionsTransmitters;
import sysmlinjava.views.timingdiagrams.StatesAxis;
import sysmlinjava.views.timingdiagrams.TimeAxis;
import sysmlinjava.views.timingdiagrams.TimingDiagramDefinition;
import sysmlinjava.views.timingdiagrams.TimingDiagramsDisplay;
import sysmlinjava.views.timingdiagrams.TimingDiagramsTransmitter;

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
@SuppressWarnings("javadoc")
public class IntersectionSignalSystemEmergencyStateMachine extends SysMLStateMachine
{
	@State
	private SysMLState EYelWGrnNSRed;
	@State
	private SysMLState EGrnWYelNSRed;
	@State
	public SysMLState EGrnWRedNSRed;
	@State
	public SysMLState ERedWGrnNSRed;

	@EntryActionFunction
	private SysMLEntryActionFunction setTimerForEYelWGrnNSRed;
	@EntryActionFunction
	private SysMLEntryActionFunction setTimerForEGrnWYelNSRed;
	
	@ChoicePseudoState
	public SysMLChoicePseudoState emergencyVehicleDirectionChoice;
	@JunctionPseudoState
	public SysMLJunctionPseudoState emergencyVehicleDepartedJunction;

	@GuardCondition
	public SysMLGuardCondition eastboundEmergencyVehiclePresentCondition;
	@GuardCondition
	public SysMLGuardCondition westboundEmergencyVehiclePresentCondition;
	@GuardCondition
	private SysMLGuardCondition emergencyVehicleAbsentCondition;

	@Guard
	public SysMLGuard eastboundEmergencyVehiclePresent;
	@Guard
	public SysMLGuard westboundEmergencyVehiclePresent;
	@Guard
	private SysMLGuard emergencyVehicleAbsent;

	@EffectActionFunction
	public SysMLEffectActionFunction setToEastboundEmergencyVehicleAction;
	@EffectActionFunction
	public SysMLEffectActionFunction setToWestboundEmergencyVehicleAction;
	@EffectActionFunction
	private SysMLEffectActionFunction setEmergencyVehicleAbsentAction;

	@Effect
	public SysMLEffect setToEastboundEmergencyVehicle;
	@Effect
	public SysMLEffect setToWestboundEmergencyVehicle;
	@Effect
	private SysMLEffect setEmergencyVehicleAbsent;

	@Transition
	public InitialTransition initialToEmergencyVehicleDirectionChoice;
	@Transition
	public SysMLTransition eastboundEmergencyVehicleChoice;
	@Transition
	public SysMLTransition westboundEmergencyVehicleChoice;
	@Transition
	public SysMLTransition eastboundEmergencyVehicleDepartedJunction;
	@Transition
	public SysMLTransition westboundEmergencyVehicleDepartedJunction;
	@Transition
	public SysMLTransition emergencyVehicleDepartedJunctionToFinal;

	final String WYelToWRedTimerID="WYelToWRedTimerID";
	final String EYelToERedTimerID="EYelToERedTimerID";

	private IntersectionSignalSystem intersection;
	String identity;
	
	public IntersectionSignalSystemEmergencyStateMachine(IntersectionSignalSystem contextSystem, String name, Long id)
	{
		super(Optional.of(contextSystem), true, name);
		this.id = id;
		this.intersection = contextSystem;
		this.identity = intersection.identityString();
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
		result.add(emergencyVehicleDirectionChoice.identityString());
		result.add(EYelWGrnNSRed.identityString());
		result.add(EGrnWYelNSRed.identityString());
		result.add(EGrnWRedNSRed.identityString());
		result.add(ERedWGrnNSRed.identityString());
		result.add(emergencyVehicleDepartedJunction.identityString());
		result.add(finalState.identityString());
		return result;
	}

	@SuppressWarnings("unused")
	@Override
	protected void createStateEntryActionFunctions()
	{
		setTimerForEYelWGrnNSRed = (contextSystem) -> startTimer(new SysMLTimeEvent(identity + EYelToERedTimerID, DurationMilliseconds.ofSeconds((int)intersection.YellowDuration.value), Optional.empty()));
		setTimerForEGrnWYelNSRed = (contextSystem) -> startTimer(new SysMLTimeEvent(identity + WYelToWRedTimerID, DurationMilliseconds.ofSeconds((int)intersection.YellowDuration.value), Optional.empty()));
	}

	@Override
	protected void createStates()
	{
		super.createStates();
		EYelWGrnNSRed = new SysMLState(context, "EYelWGrnNSRed");
		EGrnWYelNSRed = new SysMLState(context, "EGrnWYelNSRed");
		EGrnWRedNSRed = new SysMLState(context, "EGrnWRedNSRed");
		ERedWGrnNSRed = new SysMLState(context, "ERedWGrnNSRed");
	}

	@Override
	protected void createPseudoStates()
	{
		super.createPseudoStates();
		emergencyVehicleDirectionChoice = new SysMLChoicePseudoState(context, "initialChoice");
		emergencyVehicleDepartedJunction = new SysMLJunctionPseudoState(context, "finalJunction");
	}

	@SuppressWarnings("unused")
	@Override
	protected void createGuardConditions()
	{
		eastboundEmergencyVehiclePresentCondition = (event, contextSystem) ->
		{
			IntersectionSignalSystem intersection = (IntersectionSignalSystem)contextSystem.get();
			return intersection.emergencyVehicleDirection.equalTo(DirectionDegrees.east);
		};
		westboundEmergencyVehiclePresentCondition = (event, contextSystem) ->
		{
			IntersectionSignalSystem intersection = (IntersectionSignalSystem)contextSystem.get();
			return intersection.emergencyVehicleDirection.equalTo(DirectionDegrees.west);
		};
		emergencyVehicleAbsentCondition = (event, contextSystem) ->
		{
			return ((SysMLChangeEvent)event.get()).changeExpression.split("=")[1].equals("none");
		};
	}

	@Override
	protected void createGuards()
	{
		eastboundEmergencyVehiclePresent = new SysMLGuard(context, eastboundEmergencyVehiclePresentCondition, "EastBoundEmergencyVehiclPresent");
		westboundEmergencyVehiclePresent = new SysMLGuard(context, westboundEmergencyVehiclePresentCondition, "WestBoundEmergencyVehiclPresent");
		emergencyVehicleAbsent = new SysMLGuard(context, emergencyVehicleAbsentCondition, "emergencyVehicleAbsent");
	}

	@SuppressWarnings("unused")
	@Override
	protected void createEffectActionFunctions()
	{
		setToEastboundEmergencyVehicleAction = (event, contextSystem) ->
		{
			logger.info(intersection.getName() + " - setToEastboundEmergencyVehicle");
			IntersectionSignalSystem intersection = (IntersectionSignalSystem)contextSystem.get();
			intersection.setEGrnWRedNSRed();
		};
		setToWestboundEmergencyVehicleAction = (event, contextSystem) ->
		{
			logger.info(intersection.getName() + " - setToWestboundEmergencyVehicle");
			IntersectionSignalSystem intersection = (IntersectionSignalSystem)contextSystem.get();
			intersection.setERedWGrnNSRed();
		};
		setEmergencyVehicleAbsentAction = (event, contextSystem) ->
		{
			logger.info(intersection.getName() + " - setEmergencyVehicleAbsent");
			IntersectionSignalSystem intersection = (IntersectionSignalSystem)contextSystem.get();
			intersection.emergencyVehiclePresent.setValue(BBoolean.False);
			intersection.emergencyVehicleDirection.setValue(DirectionDegrees.east);
		};
	}

	@Override
	protected void createEffects()
	{
		setToEastboundEmergencyVehicle = new SysMLEffect(context, setToEastboundEmergencyVehicleAction, "setEGrnWRedNSRed");
		setToWestboundEmergencyVehicle = new SysMLEffect(context, setToWestboundEmergencyVehicleAction, "setERedWGrnNSRed");
		setEmergencyVehicleAbsent = new SysMLEffect(context, setEmergencyVehicleAbsentAction, "setEmergencyVehicleAbsent");
	}

	@Override
	protected void createTransitions()
	{
		initialToEmergencyVehicleDirectionChoice = new InitialTransition(context, initialState, emergencyVehicleDirectionChoice, "InitialToChoice");
		eastboundEmergencyVehicleChoice = new SysMLTransition(context, emergencyVehicleDirectionChoice, EGrnWRedNSRed, Optional.of(eastboundEmergencyVehiclePresent), Optional.of(setToEastboundEmergencyVehicle), "InitialChoiceToEGrn");
		westboundEmergencyVehicleChoice = new SysMLTransition(context, emergencyVehicleDirectionChoice, ERedWGrnNSRed, Optional.of(westboundEmergencyVehiclePresent), Optional.of(setToWestboundEmergencyVehicle), "InitialChoiceToWGrn");
		eastboundEmergencyVehicleDepartedJunction = new SysMLTransition(context, EGrnWRedNSRed, emergencyVehicleDepartedJunction, Optional.of(SysMLChangeEvent.class), Optional.of(emergencyVehicleAbsent), Optional.of(setEmergencyVehicleAbsent), "EGrnToFinalJunction");
		westboundEmergencyVehicleDepartedJunction = new SysMLTransition(context, ERedWGrnNSRed, emergencyVehicleDepartedJunction, Optional.of(SysMLChangeEvent.class), Optional.of(emergencyVehicleAbsent), Optional.of(setEmergencyVehicleAbsent), "WGrnToFinalJunction");
		emergencyVehicleDepartedJunctionToFinal = new FinalTransition(context, emergencyVehicleDepartedJunction, finalState, "emergencyVehicleDepartedJunctionToFinal");
	}

	@Override
	protected void createTransitionsUtility()
	{
		transitionsUtility = Optional.of(new StateTransitionsTransmitters(Optional.of(new StateTransitionTablesTransmitter(StateTransitionsDisplay.udpPort, false)), Optional.of(new TimingDiagramsTransmitter(TimingDiagramsDisplay.udpPort, false)), false));
	}
}
