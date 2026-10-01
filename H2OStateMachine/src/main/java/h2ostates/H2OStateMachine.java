package h2ostates;

import java.util.List;
import java.util.Optional;

import sysmlinjava.attributetypes.LatentHeatKilojoulesPerKilogram;
import sysmlinjava.attributetypes.TemperatureDegreesC;
import sysmlinjava.events.SysMLChangeEvent;
import sysmlinjava.javaannotations.statemachines.EntryActionFunction;
import sysmlinjava.javaannotations.statemachines.Guard;
import sysmlinjava.javaannotations.statemachines.GuardCondition;
import sysmlinjava.javaannotations.statemachines.JunctionPseudoState;
import sysmlinjava.javaannotations.statemachines.State;
import sysmlinjava.javaannotations.statemachines.Transition;
import sysmlinjava.states.FinalEvent;
import sysmlinjava.states.FinalTransition;
import sysmlinjava.states.InitialTransition;
import sysmlinjava.states.SysMLChoicePseudoState;
import sysmlinjava.states.SysMLEntryActionFunction;
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
 * {@code H2OStateMachine} is a SysMLinJava example model that demonstrates how
 * to define a SysML state machine using the SysMLinJava API. It models a
 * continuous process of heating and cooling an amount of water to exist in all
 * of its possible states, i.e. ice, liquid, gas, and decomposed - the "final"
 * state. Modelers should recognize the state machine as being compliant with
 * the state machine definition in SysML.
 * <p>
 * The {@code H2OStateMachine} is a SysMLinJava implementation of the SysML
 * state machine for H2O found in the book "A Practical Guide to SysML - The
 * Systems Modeling Language 3rd edition" by Sanford Friedenthal, et al; Object
 * Management Group; Morgan Kaufman publisher; copyright 2015.
 * 
 * @author ModelerOne
 *
 */
public class H2OStateMachine extends SysMLStateMachine
{
	/**
	 * Junction pseudo-state for deciding initial state of H2)
	 */
	@JunctionPseudoState
	public SysMLChoicePseudoState choice;
	/**
	 * State for H2O as gas
	 */
	@State
	public SysMLState gas;
	/**
	 * State for H2O as liquid
	 */
	@State
	public SysMLState liquid;
	/**
	 * State for H2O as ice
	 */
	@State
	public SysMLState ice;
	/**
	 * State for H2O as decomposed into atoms
	 */
	@State
	public SysMLState decomposed;

	/**
	 * Action when entering decomposed state
	 */
	@EntryActionFunction
	public SysMLEntryActionFunction decomposedOnEnterAction;

	/**
	 * Transition from initial state to choice of H2O start state
	 */
	@Transition
	public InitialTransition initialTransition;
	/**
	 * Transition to start state as gas
	 */
	@Transition
	public SysMLTransition initialAsGasTransition;
	/**
	 * Transition to start state as liquid
	 */
	@Transition
	public SysMLTransition initialAsLiquidTransition;
	/**
	 * Transition to start state as ice
	 */
	@Transition
	public SysMLTransition initialAsIceTransition;
	/**
	 * Transition from gas to liquid
	 */
	@Transition
	public SysMLTransition gasToLiquidTransition;
	/**
	 * Transition from liquid to ice
	 */
	@Transition
	public SysMLTransition liquidToIceTransition;
	/**
	 * Transition from ice to liquid
	 */
	@Transition
	public SysMLTransition iceToLiquidTransition;
	/**
	 * Transition from liquid to gas
	 */
	@Transition
	public SysMLTransition liquidToGasTransition;
	/**
	 * Transition from gas to decomposed
	 */
	@Transition
	public SysMLTransition gasToDecomposedTransition;
	/**
	 * Transition from decomposed to final state
	 */
	@Transition
	public SysMLTransition decomposedToFinalTransition;

	/**
	 * Guard condition for the H2O to start in the gas state
	 */
	@GuardCondition
	public SysMLGuardCondition initialAsGasGuardCondition;
	/**
	 * Guard condition for the H2O to start in the liquie state
	 */
	@GuardCondition
	public SysMLGuardCondition initialAsLiquidGuardCondition;
	/**
	 * Guard condition for the H2O to start in the ice state
	 */
	@GuardCondition
	public SysMLGuardCondition initialAsIceGuardCondition;
	/**
	 * Guard condition for the H2O to transition from the gas to liquid state
	 */
	@GuardCondition
	public SysMLGuardCondition gasToLiquidGuardCondition;
	/**
	 * Guard condition for the H2O to transition from the liquid to ice state
	 */
	@GuardCondition
	public SysMLGuardCondition liquidToIceGuardCondition;
	/**
	 * Guard condition for the H2O to transition from the ice to liquid state
	 */
	@GuardCondition
	public SysMLGuardCondition iceToLiquidGuardCondition;
	/**
	 * Guard condition for the H2O to transition from the liquid to gas state
	 */
	@GuardCondition
	public SysMLGuardCondition liquidToGasGuardCondition;
	/**
	 * Guard condition for the H2O to transition from the gas to decomposed state
	 */
	@GuardCondition
	public SysMLGuardCondition gasToDecomposedGuardCondition;

	/**
	 * Guard containing the condition for the H2O to start in the gas state
	 */
	@Guard
	public SysMLGuard initialAsGasGuard;
	/**
	 * Guard containing the condition for the H2O to start in the liquie state
	 */
	@Guard
	public SysMLGuard initialAsLiquidGuard;
	/**
	 * Guard containing the condition for the H2O to start in the ice state
	 */
	@Guard
	public SysMLGuard initialAsIceGuard;
	/**
	 * Guard containing the condition for the H2O to transition from the gas to
	 * liquid state
	 */
	@Guard
	public SysMLGuard gasToLiquidGuard;
	/**
	 * Guard containing the condition for the H2O to transition from the liquid to
	 * ice state
	 */
	@Guard
	public SysMLGuard liquidToIceGuard;
	/**
	 * Guard containing the condition for the H2O to transition from the ice to
	 * liquid state
	 */
	@Guard
	public SysMLGuard iceToLiquidGuard;
	/**
	 * Guard containing the condition for the H2O to transition from the liquid to
	 * gas state
	 */
	@Guard
	public SysMLGuard liquidToGasGuard;
	/**
	 * Guard containing the condition for the H2O to transition from the gas to
	 * decomposed state
	 */
	@Guard
	public SysMLGuard gasToDecomposedGuard;

	/**
	 * Constructor
	 * 
	 * @param contextPart H2O part in whose context this state machine executes
	 */
	public H2OStateMachine(H2O contextPart)
	{
		super(Optional.of(contextPart), true, "H2OStateMachine");
	}

	@Override
	public void start()
	{
		StatesAxis statesAxis = new StatesAxis(List.of(nameInitial, nameChoice, nameIce, nameLiquid, nameGas, nameDecomposed, nameFinal));
		TimeAxis timeAxis = new TimeAxis(12, 10, 10);
		TimingDiagramDefinition definition = new TimingDiagramDefinition(identityString(), statesAxis, timeAxis);
		((StateTransitionsTransmitters)transitionsUtility.get()).transmit(definition);
		super.start();
	}

	@Override
	public void stop()
	{
		((StateTransitionsTransmitters)transitionsUtility.get()).stop();
		super.stop();
	}

	@Override
	protected void createStateEntryActionFunctions()
	{
		decomposedOnEnterAction = (contextBlock) ->
		{
			((H2O)contextBlock.get()).acceptEvent(new FinalEvent());
		};
	}

	@Override
	protected void createStates()
	{
		super.createStates();
		ice = new SysMLState(context, nameIce);
		liquid = new SysMLState(context, nameLiquid);
		gas = new SysMLState(context, nameGas);
		decomposed = new SysMLState(context, Optional.of(decomposedOnEnterAction), Optional.empty(), Optional.empty(), nameDecomposed);
	}

	@Override
	protected void createPseudoStates()
	{
		super.createPseudoStates();
		choice = new SysMLChoicePseudoState(context, nameChoice);
	}

	@SuppressWarnings("unused")
	@Override
	protected void createGuardConditions()
	{
		initialAsGasGuardCondition = (event, contextBlock) ->
		{
			TemperatureDegreesC temp = ((H2O)contextBlock.get()).temp;
			TemperatureDegreesC gasTemp = (((H2O)contextBlock.get()).gasTemp);
			return temp.greaterThanOrEqualTo(gasTemp);
		};
		initialAsLiquidGuardCondition = (event, contextBlock) ->
		{
			TemperatureDegreesC temp = ((H2O)contextBlock.get()).temp;
			TemperatureDegreesC gasTemp = (((H2O)contextBlock.get()).gasTemp);
			TemperatureDegreesC iceTemp = (((H2O)contextBlock.get()).iceTemp);
			return temp.greaterThan(iceTemp) && temp.lessThan(gasTemp);
		};
		initialAsIceGuardCondition = (event, contextBlock) ->
		{
			TemperatureDegreesC temp = ((H2O)contextBlock.get()).temp;
			TemperatureDegreesC iceTemp = (((H2O)contextBlock.get()).iceTemp);
			return temp.lessThanOrEqualTo(iceTemp);
		};

		gasToLiquidGuardCondition = (event, contextBlock) ->
		{
			TemperatureDegreesC temp = ((H2O)contextBlock.get()).temp;
			TemperatureDegreesC gasTemp = (((H2O)contextBlock.get()).gasTemp);
			return temp.lessThan(gasTemp);
		};
		liquidToIceGuardCondition = (event, contextBlock) ->
		{
			TemperatureDegreesC temp = ((H2O)contextBlock.get()).temp;
			TemperatureDegreesC iceTemp = (((H2O)contextBlock.get()).iceTemp);
			return temp.lessThanOrEqualTo(iceTemp);
		};
		iceToLiquidGuardCondition = (event, contextBlock) ->
		{
			TemperatureDegreesC temp = ((H2O)contextBlock.get()).temp;
			TemperatureDegreesC iceTemp = ((H2O)contextBlock.get()).iceTemp;
			LatentHeatKilojoulesPerKilogram latentHeat = ((H2O)contextBlock.get()).latentHeat;
			LatentHeatKilojoulesPerKilogram minLatentHeat = ((H2O)contextBlock.get()).minLatentHeatCondensation;
			return temp.greaterThan(iceTemp) && latentHeat.greaterThanOrEqualTo(minLatentHeat);
		};
		liquidToGasGuardCondition = (event, contextBlock) ->
		{
			TemperatureDegreesC temp = ((H2O)contextBlock.get()).temp;
			TemperatureDegreesC gasTemp = ((H2O)contextBlock.get()).gasTemp;
			LatentHeatKilojoulesPerKilogram latentHeat = ((H2O)contextBlock.get()).latentHeat;
			LatentHeatKilojoulesPerKilogram minLatentHeat = ((H2O)contextBlock.get()).minLatentHeatEvaporation;
			return temp.greaterThanOrEqualTo(gasTemp) && latentHeat.greaterThanOrEqualTo(minLatentHeat);
		};
		gasToDecomposedGuardCondition = (event, contextBlock) ->
		{
			TemperatureDegreesC temp = ((H2O)contextBlock.get()).temp;
			TemperatureDegreesC maxGasTemp = ((H2O)contextBlock.get()).decomposedTemp;
			return temp.greaterThanOrEqualTo(maxGasTemp);
		};
	}

	@Override
	protected void createGuards()
	{
		initialAsGasGuard = new SysMLGuard(context, initialAsGasGuardCondition, "isInitialAsGas");
		initialAsLiquidGuard = new SysMLGuard(context, initialAsLiquidGuardCondition, "isInitialAsLiquid");
		initialAsIceGuard = new SysMLGuard(context, initialAsIceGuardCondition, "isInitialAsIce");

		gasToLiquidGuard = new SysMLGuard(context, gasToLiquidGuardCondition, "isGasToLiquid");
		liquidToIceGuard = new SysMLGuard(context, liquidToIceGuardCondition, "isLiquidToIce");
		iceToLiquidGuard = new SysMLGuard(context, iceToLiquidGuardCondition, "isIceToLiquid");
		liquidToGasGuard = new SysMLGuard(context, liquidToGasGuardCondition, "isLiquidToGas");
		gasToDecomposedGuard = new SysMLGuard(context, gasToDecomposedGuardCondition, "isGasToDecomposed");
	}

	@Override
	protected void createTransitions()
	{
		initialTransition = new InitialTransition(context, initialState, choice, "initial");
		initialAsGasTransition = new SysMLTransition(context, choice, gas, Optional.of(initialAsGasGuard), Optional.empty(), "InitialAsGas");
		initialAsLiquidTransition = new SysMLTransition(context, choice, liquid, Optional.of(initialAsLiquidGuard), Optional.empty(), "InitialAsLiquid");
		initialAsIceTransition = new SysMLTransition(context, choice, ice, Optional.of(initialAsIceGuard), Optional.empty(), "InitialAsIce");

		gasToLiquidTransition = new SysMLTransition(context, gas, liquid, Optional.of(SysMLChangeEvent.class), Optional.of(gasToLiquidGuard), Optional.empty(), "GasToLiquid", SysMLTransitionKind.external);
		liquidToIceTransition = new SysMLTransition(context, liquid, ice, Optional.of(SysMLChangeEvent.class), Optional.of(liquidToIceGuard), Optional.empty(), "LiquidToIce", SysMLTransitionKind.external);
		iceToLiquidTransition = new SysMLTransition(context, ice, liquid, Optional.of(SysMLChangeEvent.class), Optional.of(iceToLiquidGuard), Optional.empty(), "IceToLiquid", SysMLTransitionKind.external);
		liquidToGasTransition = new SysMLTransition(context, liquid, gas, Optional.of(SysMLChangeEvent.class), Optional.of(liquidToGasGuard), Optional.empty(), "LiquidToGas", SysMLTransitionKind.external);
		gasToDecomposedTransition = new SysMLTransition(context, gas, decomposed, Optional.of(SysMLChangeEvent.class), Optional.of(gasToDecomposedGuard), Optional.empty(), "GasToDecomposed", SysMLTransitionKind.external);
		decomposedToFinalTransition = new FinalTransition(context, decomposed, finalState, "DecomposedToFinal");
	}

	@Override
	protected void createTransitionsUtility()
	{
		transitionsUtility = Optional
			.of(new StateTransitionsTransmitters(Optional.of(new StateTransitionTablesTransmitter(StateTransitionsDisplay.udpPort, false)), Optional.of(new TimingDiagramsTransmitter(TimingDiagramsDisplay.udpPort, false)), false));
	}

	/**
	 * State name used in timing diagram
	 */
	private static final String nameInitial = "Initial";
	/**
	 * State name used in timing diagram
	 */
	private static final String nameChoice = "StartChoice";
	/**
	 * State name used in timing diagram
	 */
	private static final String nameIce = "Ice";
	/**
	 * State name used in timing diagram
	 */
	private static final String nameLiquid = "Liquid";
	/**
	 * State name used in timing diagram
	 */
	private static final String nameGas = "Gas";
	/**
	 * State name used in timing diagram
	 */
	private static final String nameDecomposed = "Decomposed";
	/**
	 * State name used in timing diagram
	 */
	private static final String nameFinal = "Final";
}
