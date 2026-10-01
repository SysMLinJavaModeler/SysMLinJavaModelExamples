package c4s2.platforms;

import java.util.Optional;
import sysmlinjava.attributetypes.ElectricalPower;
import sysmlinjava.events.SysMLSignalEvent;
import sysmlinjava.javaannotations.statemachines.Effect;
import sysmlinjava.javaannotations.statemachines.EffectActionFunction;
import sysmlinjava.javaannotations.statemachines.Guard;
import sysmlinjava.javaannotations.statemachines.GuardCondition;
import sysmlinjava.javaannotations.statemachines.State;
import sysmlinjava.javaannotations.statemachines.Transition;
import sysmlinjava.states.FinalEvent;
import sysmlinjava.states.InitialTransition;
import sysmlinjava.states.SysMLEffect;
import sysmlinjava.states.SysMLEffectActionFunction;
import sysmlinjava.states.SysMLGuard;
import sysmlinjava.states.SysMLGuardCondition;
import sysmlinjava.states.SysMLState;
import sysmlinjava.states.SysMLStateMachine;
import sysmlinjava.states.SysMLTransition;
import sysmlinjava.states.SysMLTransitionKind;
import sysmlinjavalibrary.common.objects.energy.thermal.ConvectiveHeat;
import sysmlinjavalibrary.common.signals.ConvectiveHeatSignal;
import sysmlinjavalibrary.common.signals.ElectricalPowerSignal;
import sysmlinjavalibrary.common.signals.HeatSignal;
import sysmlinjavalibrary.common.signals.MechanicalForceSignal;

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

	@GuardCondition
	public SysMLGuardCondition isHeatTransferGuardCondition;
	@GuardCondition
	public SysMLGuardCondition isMechanicalForceGuardCondition;
	@GuardCondition
	public SysMLGuardCondition isPowerSinkGuardCondition;
	
	@Guard
	public SysMLGuard isHeatTransferGuard;
	@Guard
	public SysMLGuard isMechanicalForceGuard;
	@Guard
	public SysMLGuard isPowerSinkGuard;
	
	@EffectActionFunction
	public SysMLEffectActionFunction operationalOnPowerSinkTransitionEffectActivity;
	@EffectActionFunction
	public SysMLEffectActionFunction operationalOnHeatTransferTransitionEffectActivity;
	@EffectActionFunction
	public SysMLEffectActionFunction operationalOnRackMountTransitionEffectActivity;

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
		initializingState = new SysMLState(context, "Initializing");
		operationalState = new SysMLState(context, "Operational");
	}

	@Override
	protected void createGuardConditions()
	{
		isHeatTransferGuardCondition = (event, contextBlock) ->
		{
			return event.isPresent() &&
				event.get() instanceof SysMLSignalEvent &&
				((SysMLSignalEvent)event.get()).signal instanceof HeatSignal;
		};
		isMechanicalForceGuardCondition = (event, contextBlock) ->
		{
			return event.isPresent() &&
				event.get() instanceof SysMLSignalEvent &&
				((SysMLSignalEvent)event.get()).signal instanceof MechanicalForceSignal;
		};
		isPowerSinkGuardCondition = (event, contextBlock) ->
		{
			return event.isPresent() &&
				event.get() instanceof SysMLSignalEvent &&
				((SysMLSignalEvent)event.get()).signal instanceof ElectricalPowerSignal;
		};
	}

	@Override
	protected void createGuards()
	{
		isHeatTransferGuard = new SysMLGuard(context, isHeatTransferGuardCondition, "isHeatTransfer");
		isMechanicalForceGuard = new SysMLGuard(context, isMechanicalForceGuardCondition, "isMechanicalForce");
		isPowerSinkGuard = new SysMLGuard(context, isPowerSinkGuardCondition, "isPowerSink");
	}

	@Override
	protected void createEffectActionFunctions()
	{
		super.createEffectActionFunctions();
		operationalOnPowerSinkTransitionEffectActivity = (event, contextBlock) ->
		{
			C4Platform platform = (C4Platform)contextBlock.get();
			ElectricalPower powerRequest = ((ElectricalPowerSignal)((SysMLSignalEvent)event.get()).signal).power;
			platform.onPowerSink(powerRequest);
		};
		operationalOnHeatTransferTransitionEffectActivity = (event, contextBlock) ->
		{
			C4Platform platform = (C4Platform)contextBlock.get();
			ConvectiveHeat heat = ((ConvectiveHeatSignal)((SysMLSignalEvent)event.get()).signal).heat;
			platform.onHeatSource(heat);
		};
		operationalOnRackMountTransitionEffectActivity = (event, contextBlock) ->
		{
			C4Platform platform = (C4Platform)contextBlock.get();
			MechanicalForceSignal forceSignal = (MechanicalForceSignal)((SysMLSignalEvent)event.get()).signal;
			platform.onWeightSource(forceSignal.force, forceSignal.id);
		};
	}

	@Override
	protected void createEffects()
	{
		operationalOnPowerSinkTransitionEffect = new SysMLEffect(context, operationalOnPowerSinkTransitionEffectActivity, "OperationalOnPowerSinkTransition");
		operationalOnHeatTransferTransitionEffect = new SysMLEffect(context, operationalOnHeatTransferTransitionEffectActivity, "OperationalOnHeatTransferTransition");
		operationalOnRackMountTransitionEffect = new SysMLEffect(context, operationalOnRackMountTransitionEffectActivity, "OperationalOnRackMountTransition");
	}

	@Override
	protected void createTransitions()
	{
		initialToInitializingTransition = new InitialTransition(context, initialState, initializingState, "InitialToInitializing");
		initializingToOperationalTransition = new SysMLTransition(context, initializingState, operationalState, Optional.empty(), Optional.empty(), Optional.empty(), "InitializingToOperations", SysMLTransitionKind.external);
		operationalOnPowerSinkTransition = new SysMLTransition(context, operationalState, operationalState, Optional.of(SysMLSignalEvent.class), Optional.of(isPowerSinkGuard), Optional.of(operationalOnPowerSinkTransitionEffect),
			"OperationalOnPowerSink", SysMLTransitionKind.internal);
		operationalOnHeatTransferTransition = new SysMLTransition(context, operationalState, operationalState, Optional.of(SysMLSignalEvent.class), Optional.of(isHeatTransferGuard), Optional.of(operationalOnHeatTransferTransitionEffect),
			"OperationalOnHeatTransfer", SysMLTransitionKind.internal);
		operationalOnRackMountTransition = new SysMLTransition(context, operationalState, operationalState, Optional.of(SysMLSignalEvent.class), Optional.of(isMechanicalForceGuard), Optional.of(operationalOnRackMountTransitionEffect),
			"OperationalOnRackMount", SysMLTransitionKind.internal);
		operationalToFinalTransition = new SysMLTransition(context, operationalState, finalState, Optional.of(FinalEvent.class), Optional.empty(), Optional.empty(), "OperationalToFinal", SysMLTransitionKind.external);
	}
}
