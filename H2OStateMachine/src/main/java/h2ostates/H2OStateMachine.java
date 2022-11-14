package h2ostates;

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
import sysmlinjava.annotations.statemachines.Guard;
import sysmlinjava.annotations.statemachines.GuardCondition;
import sysmlinjava.annotations.statemachines.JunctionPseudoState;
import sysmlinjava.annotations.statemachines.OnEnterActivity;
import sysmlinjava.annotations.statemachines.State;
import sysmlinjava.annotations.statemachines.Transition;
import sysmlinjava.statemachine.FinalEvent;
import sysmlinjava.statemachine.FinalTransition;
import sysmlinjava.statemachine.InitialTransition;
import sysmlinjava.statemachine.SysMLChoicePseudoState;
import sysmlinjava.statemachine.SysMLGuard;
import sysmlinjava.statemachine.SysMLGuardCondition;
import sysmlinjava.statemachine.SysMLOnEnterActivity;
import sysmlinjava.statemachine.SysMLState;
import sysmlinjava.statemachine.SysMLStateMachine;
import sysmlinjava.statemachine.SysMLTransition;
import sysmlinjava.statemachine.SysMLTransitionKind;
import sysmlinjava.valuetypes.LatentHeatKilojoulesPerKilogram;
import sysmlinjava.valuetypes.TemperatureDegreesC;

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
	 * Activity when entering decomposed state
	 */
	@OnEnterActivity
	public SysMLOnEnterActivity decomposedOnEnterActivity;

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
	 * @param contextBlock H2O block in whose context this state machine executes
	 */
	public H2OStateMachine(H2O contextBlock)
	{
		super(Optional.of(contextBlock), true, "H2OStateMachine");
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
	protected void createStateOnEnterActivities()
	{
		decomposedOnEnterActivity = (contextBlock) ->
		{
			((H2O)contextBlock.get()).acceptEvent(new FinalEvent());
		};
	}

	@Override
	protected void createStates()
	{
		super.createStates();
		ice = new SysMLState(contextBlock, nameIce);
		liquid = new SysMLState(contextBlock, nameLiquid);
		gas = new SysMLState(contextBlock, nameGas);
		decomposed = new SysMLState(contextBlock, Optional.of(decomposedOnEnterActivity), Optional.empty(), Optional.empty(), nameDecomposed);
	}

	@Override
	protected void createPseudoStates()
	{
		super.createPseudoStates();
		choice = new SysMLChoicePseudoState(contextBlock, nameChoice);
	}

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
		initialAsGasGuard = new SysMLGuard(contextBlock, initialAsGasGuardCondition, "isInitialAsGas");
		initialAsLiquidGuard = new SysMLGuard(contextBlock, initialAsLiquidGuardCondition, "isInitialAsLiquid");
		initialAsIceGuard = new SysMLGuard(contextBlock, initialAsIceGuardCondition, "isInitialAsIce");

		gasToLiquidGuard = new SysMLGuard(contextBlock, gasToLiquidGuardCondition, "isGasToLiquid");
		liquidToIceGuard = new SysMLGuard(contextBlock, liquidToIceGuardCondition, "isLiquidToIce");
		iceToLiquidGuard = new SysMLGuard(contextBlock, iceToLiquidGuardCondition, "isIceToLiquid");
		liquidToGasGuard = new SysMLGuard(contextBlock, liquidToGasGuardCondition, "isLiquidToGas");
		gasToDecomposedGuard = new SysMLGuard(contextBlock, gasToDecomposedGuardCondition, "isGasToDecomposed");
	}

	@Override
	protected void createTransitions()
	{
		initialTransition = new InitialTransition(contextBlock, initialState, choice, "initial");
		initialAsGasTransition = new SysMLTransition(contextBlock, choice, gas, Optional.of(initialAsGasGuard), Optional.empty(), "InitialAsGas");
		initialAsLiquidTransition = new SysMLTransition(contextBlock, choice, liquid, Optional.of(initialAsLiquidGuard), Optional.empty(), "InitialAsLiquid");
		initialAsIceTransition = new SysMLTransition(contextBlock, choice, ice, Optional.of(initialAsIceGuard), Optional.empty(), "InitialAsIce");

		gasToLiquidTransition = new SysMLTransition(contextBlock, gas, liquid, Optional.of(TempChangeEvent.class), Optional.of(gasToLiquidGuard), Optional.empty(), "GasToLiquid", SysMLTransitionKind.external);
		liquidToIceTransition = new SysMLTransition(contextBlock, liquid, ice, Optional.of(TempChangeEvent.class), Optional.of(liquidToIceGuard), Optional.empty(), "LiquidToIce", SysMLTransitionKind.external);
		iceToLiquidTransition = new SysMLTransition(contextBlock, ice, liquid, Optional.of(TempChangeEvent.class), Optional.of(iceToLiquidGuard), Optional.empty(), "IceToLiquid", SysMLTransitionKind.external);
		liquidToGasTransition = new SysMLTransition(contextBlock, liquid, gas, Optional.of(TempChangeEvent.class), Optional.of(liquidToGasGuard), Optional.empty(), "LiquidToGas", SysMLTransitionKind.external);
		gasToDecomposedTransition = new SysMLTransition(contextBlock, gas, decomposed, Optional.of(TempChangeEvent.class), Optional.of(gasToDecomposedGuard), Optional.empty(), "GasToDecomposed", SysMLTransitionKind.external);
		decomposedToFinalTransition = new FinalTransition(contextBlock, decomposed, finalState, "DecomposedToFinal");
	}

	@Override
	protected void createTransitionsUtility()
	{
		transitionsUtility = Optional.of(
			new StateTransitionsTransmitters(Optional.of(
				new StateTransitionTablesTransmitter(StateTransitionsDisplay.udpPort, true)), Optional.of(
				new TimingDiagramsTransmitter(TimingDiagramsDisplay.udpPort, true))));
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
