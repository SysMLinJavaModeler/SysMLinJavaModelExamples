package c4s2.platforms;

import java.util.Optional;
import c4s2.common.events.HeatTransferEvent;
import sysmlinjava.annotations.statemachines.Effect;
import sysmlinjava.annotations.statemachines.EffectActivity;
import sysmlinjava.annotations.statemachines.State;
import sysmlinjava.annotations.statemachines.Transition;
import sysmlinjava.statemachine.FinalEvent;
import sysmlinjava.statemachine.InitialTransition;
import sysmlinjava.statemachine.SysMLEffect;
import sysmlinjava.statemachine.SysMLEffectActivity;
import sysmlinjava.statemachine.SysMLState;
import sysmlinjava.statemachine.SysMLStateMachine;
import sysmlinjava.statemachine.SysMLTransition;
import sysmlinjava.statemachine.SysMLTransitionKind;
import sysmlinjava.valuetypes.ElectricalPower;
import sysmlinjava.valuetypes.ForceNewtons;
import sysmlinjava.valuetypes.IInteger;
import sysmlinjavalibrary.common.events.ConvectiveHeatEvent;
import sysmlinjavalibrary.common.events.ElectricalPowerEvent;
import sysmlinjavalibrary.common.events.MechanicalForceEvent;
import sysmlinjavalibrary.common.objects.energy.thermal.ConvectiveHeat;

/**
 * The {@code C4PlatformStateMachine} is the SysMLinJava model of the state
 * machine for a command/control/computers/Communications (C4) platform that
 * provides SWAP-C support for the C4S2System. The state machine has 2 states -
 * initializing and operational. See the state machine's states and transitions
 * for definition of this behavior.
 * 
 * @author ModelerOne
 *
 *         Known users:
 * @see C4Platform
 */
public class C4PlatformStateMachine extends SysMLStateMachine
{
	@State
	public SysMLState initializingState;
	@State
	public SysMLState operationalState;

	@Transition
	public InitialTransition initialToInitializingTransition;
	@Transition
	public SysMLTransition initializingToOperationalTransition;
	@Transition
	public SysMLTransition operationalOnPowerSinkTransition;
	@Transition
	public SysMLTransition operationalOnHeatTransferTransition;
	@Transition
	public SysMLTransition operationalOnRackMountTransition;
	@Transition
	public SysMLTransition operationalToFinalTransition;

	@EffectActivity
	public SysMLEffectActivity operationalOnPowerSinkTransitionEffectActivity;
	@EffectActivity
	public SysMLEffectActivity operationalOnHeatTransferTransitionEffectActivity;
	@EffectActivity
	public SysMLEffectActivity operationalOnRackMountTransitionEffectActivity;

	@Effect
	public SysMLEffect operationalOnPowerSinkTransitionEffect;
	@Effect
	public SysMLEffect operationalOnHeatTransferTransitionEffect;
	@Effect
	public SysMLEffect operationalOnRackMountTransitionEffect;

	public C4PlatformStateMachine(C4Platform c4Platform)
	{
		super(Optional.of(c4Platform), true, "C4PlatformStateMachine");
	}

	@Override
	protected void createStates()
	{
		super.createStates();
		initializingState = new SysMLState(contextBlock, "Initializing");
		operationalState = new SysMLState(contextBlock, "Operational");
	}

	@Override
	protected void createEffectActivities()
	{
		super.createEffectActivities();
		operationalOnPowerSinkTransitionEffectActivity = (event, contextBlock) ->
		{
			C4Platform platform = (C4Platform)contextBlock.get();
			ElectricalPower powerRequest = ((ElectricalPowerEvent)event.get()).getPower();
			platform.onPowerSink(powerRequest);
		};
		operationalOnHeatTransferTransitionEffectActivity = (event, contextBlock) ->
		{
			C4Platform platform = (C4Platform)contextBlock.get();
			ConvectiveHeat heat = ((ConvectiveHeatEvent)event.get()).getHeat();
			platform.onHeatSource(heat);
		};
		operationalOnRackMountTransitionEffectActivity = (event, contextBlock) ->
		{
			C4Platform platform = (C4Platform)contextBlock.get();
			ForceNewtons weight = ((MechanicalForceEvent)event.get()).getForce();
			IInteger mountPointIndex = ((MechanicalForceEvent)event.get()).getID();
			platform.onWeightSource(weight, mountPointIndex);
		};
	}

	@Override
	protected void createEffects()
	{
		operationalOnPowerSinkTransitionEffect = new SysMLEffect(contextBlock, operationalOnPowerSinkTransitionEffectActivity, "OperationalOnPowerSinkTransition");
		operationalOnHeatTransferTransitionEffect = new SysMLEffect(contextBlock, operationalOnHeatTransferTransitionEffectActivity, "OperationalOnHeatTransferTransition");
		operationalOnRackMountTransitionEffect = new SysMLEffect(contextBlock, operationalOnRackMountTransitionEffectActivity, "OperationalOnRackMountTransition");
	}

	@Override
	protected void createTransitions()
	{
		initialToInitializingTransition = new InitialTransition(contextBlock, initialState, initializingState, "InitialToInitializing");
		initializingToOperationalTransition = new SysMLTransition(contextBlock, initializingState, operationalState, Optional.empty(), Optional.empty(), Optional.empty(), "InitializingToOperations", SysMLTransitionKind.external);
		operationalOnPowerSinkTransition = new SysMLTransition(contextBlock, operationalState, operationalState, Optional.of(ElectricalPowerEvent.class), Optional.empty(), Optional.of(operationalOnPowerSinkTransitionEffect),
			"OperationalOnPowerSink", SysMLTransitionKind.internal);
		operationalOnHeatTransferTransition = new SysMLTransition(contextBlock, operationalState, operationalState, Optional.of(HeatTransferEvent.class), Optional.empty(), Optional.of(operationalOnHeatTransferTransitionEffect),
			"OperationalOnHeatTransfer", SysMLTransitionKind.internal);
		operationalOnRackMountTransition = new SysMLTransition(contextBlock, operationalState, operationalState, Optional.of(MechanicalForceEvent.class), Optional.empty(), Optional.of(operationalOnRackMountTransitionEffect),
			"OperationalOnRackMount", SysMLTransitionKind.internal);
		operationalToFinalTransition = new SysMLTransition(contextBlock, operationalState, finalState, Optional.of(FinalEvent.class), Optional.empty(), Optional.empty(), "OperationalToFinal", SysMLTransitionKind.external);
	}
}
