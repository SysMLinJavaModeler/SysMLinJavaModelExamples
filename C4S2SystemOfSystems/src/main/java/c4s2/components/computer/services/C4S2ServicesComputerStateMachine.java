package c4s2.components.computer.services;

import java.util.Optional;
import c4s2.common.valueTypes.C4S2ServicesComputerStatesEnum;
import sysmlinjava.annotations.statemachines.Effect;
import sysmlinjava.annotations.statemachines.EffectActivity;
import sysmlinjava.annotations.statemachines.Guard;
import sysmlinjava.annotations.statemachines.GuardCondition;
import sysmlinjava.annotations.statemachines.State;
import sysmlinjava.annotations.statemachines.Transition;
import sysmlinjava.statemachine.FinalTransition;
import sysmlinjava.statemachine.InitialTransition;
import sysmlinjava.statemachine.SysMLEffect;
import sysmlinjava.statemachine.SysMLEffectActivity;
import sysmlinjava.statemachine.SysMLGuard;
import sysmlinjava.statemachine.SysMLGuardCondition;
import sysmlinjava.statemachine.SysMLState;
import sysmlinjava.statemachine.SysMLStateMachine;
import sysmlinjava.statemachine.SysMLTransition;
import sysmlinjava.statemachine.SysMLTransitionKind;
import sysmlinjava.valuetypes.ElectricalPower;
import sysmlinjava.valuetypes.RReal;
import sysmlinjavalibrary.common.events.ElectricalPowerEvent;
import sysmlinjavalibrary.common.events.OnOffSwitchEvent;
import sysmlinjavalibrary.common.events.SNMPRequestEvent;
import sysmlinjavalibrary.common.objects.information.OnOffSwitch;
import sysmlinjavalibrary.common.objects.information.SNMPRequest;

/**
 * The {@code C4S2ServicesComputerStateMachine} is the SysMLinJava model of the
 * state machine for the C4S2 Services Computer that provides a host for all
 * radar, strike, target, and systems services. The state machine has 3 states -
 * power off, initializing, and operational. See the state machine's states and
 * transitions for definition of this behavior.
 * 
 * @author ModelerOne
 *
 * @see C4S2ServicesComputer
 */
public class C4S2ServicesComputerStateMachine extends SysMLStateMachine
{
	@State
	private SysMLState powerOffState;
	@State
	private SysMLState initializingState;
	@State
	private SysMLState operationalState;

	@Transition
	private InitialTransition initialToPowerOffTransition;
	@Transition
	private SysMLTransition powerOffOnPowerSwitchedOnTransition;
	@Transition
	private SysMLTransition powerOffToInitializingTransition;
	@Transition
	private SysMLTransition initializingToOperationalTransition;
	@Transition
	private SysMLTransition operationalOnControlTransition;
	@Transition
	private SysMLTransition operationalOnControlToPowerOffTransition;
	@Transition
	private SysMLTransition operationalOnPowerSwitchedOffTransition;
	@Transition
	private SysMLTransition operationalToPowerOffTransition;
	@Transition
	private SysMLTransition powerOffToFinalTransition;

	@GuardCondition
	private SysMLGuardCondition isSwitchedOnGuardCondition;
	@GuardCondition
	private SysMLGuardCondition isSwitchedOffGuardCondition;
	@GuardCondition
	private SysMLGuardCondition isMinPowerGuardCondition;
	@GuardCondition
	private SysMLGuardCondition isControlToPowerOffGuardCondition;
	@GuardCondition
	private SysMLGuardCondition isZeroPowerGuardCondition;

	@Guard
	private SysMLGuard isSwitchedOnGuard;
	@Guard
	private SysMLGuard isSwitchedOffGuard;
	@Guard
	private SysMLGuard isMinPowerGuard;
	@Guard
	private SysMLGuard isControlToPowerOffGuard;
	@Guard
	private SysMLGuard isZeroPowerGuard;

	@EffectActivity
	private SysMLEffectActivity initialToPowerOffTransitionEffectActivity;
	@EffectActivity
	private SysMLEffectActivity powerOffOnPowerSwitchedOnTransitionEffectActivity;
	@EffectActivity
	private SysMLEffectActivity powerOffToPoweredOnTransitionEffectActivity;
	@EffectActivity
	private SysMLEffectActivity operationalOnControlTransitionEffectActivity;
	@EffectActivity
	private SysMLEffectActivity operationalOnControlToPowerOffTransitionEffectActivity;
	@EffectActivity
	private SysMLEffectActivity operationalOnPowerSwitchedOffTransitionEffectActivity;
	@EffectActivity
	private SysMLEffectActivity operationalToPowerOffTransitionEffectActivity;

	@Effect
	private SysMLEffect initialToPowerOffTransitionEffect;
	@Effect
	private SysMLEffect powerOffOnPowerSwitchedOnTransitionEffect;
	@Effect
	private SysMLEffect powerOffToInitializingTransitionEffect;
	@Effect
	private SysMLEffect operationalOnControlTransitionEffect;
	@Effect
	public SysMLEffect operationalOnControlToPowerOffTransitionEffect;
	@Effect
	public SysMLEffect operationalOnPowerSwitchedOffTransitionEffect;
	@Effect
	private SysMLEffect operationalToPowerOffTransitionEffect;

	public C4S2ServicesComputerStateMachine(C4S2ServicesComputer c4S2ServicesComputer)
	{
		super(Optional.of(c4S2ServicesComputer), true, "C4S2ServicesComputerStateMachine");
	}

	@Override
	protected void createStates()
	{
		super.createStates();
		powerOffState = new SysMLState(contextBlock, Optional.empty(), Optional.empty(), Optional.empty(), "PowerOff");
		initializingState = new SysMLState(contextBlock, Optional.empty(), Optional.empty(), Optional.empty(), "Initializing");
		operationalState = new SysMLState(contextBlock, Optional.empty(), Optional.empty(), Optional.empty(), "Operational");
	}

	@Override
	protected void createGuardConditions()
	{
		super.createGuardConditions();
		isSwitchedOnGuardCondition = (event, contextBlock) ->
		{
			OnOffSwitch onOffSwitch = ((OnOffSwitchEvent)event.get()).getOnOffSwitch();
			return onOffSwitch.isOn;
		};
		isSwitchedOffGuardCondition = (event, contextBlock) ->
		{
			OnOffSwitch onOffSwitch = ((OnOffSwitchEvent)event.get()).getOnOffSwitch();
			return !onOffSwitch.isOn;
		};
		isMinPowerGuardCondition = (event, contextBlock) ->
		{
			ElectricalPower power = ((ElectricalPowerEvent)event.get()).getPower();
			C4S2ServicesComputer computer = (C4S2ServicesComputer)contextBlock.get();
			return power.current.greaterThan(computer.minCurrentIn);
		};
		isControlToPowerOffGuardCondition = (event, contextBlock) ->
		{
			boolean result = false;
			SNMPRequest request = ((SNMPRequestEvent)event.get()).getRequest();
			C4S2ServicesComputerStatesEnum state = C4S2ServicesComputerStatesEnum.valueOf(request.mib.getDataStrings().get(1));
			if (state == C4S2ServicesComputerStatesEnum.PowerOff)
				result = true;
			return result;
		};
		isZeroPowerGuardCondition = (event, contextBlock) ->
		{
			ElectricalPower power = ((ElectricalPowerEvent)event.get()).getPower();
			return power.watts().lessThan(RReal.of(1));
		};
	}

	@Override
	protected void createGuards()
	{
		super.createGuards();
		isSwitchedOnGuard = new SysMLGuard(contextBlock, isSwitchedOnGuardCondition, "isSwitchedOn");
		isSwitchedOffGuard = new SysMLGuard(contextBlock, isSwitchedOffGuardCondition, "isSwitchedOff");
		isMinPowerGuard = new SysMLGuard(contextBlock, isMinPowerGuardCondition, "isMinPower");
		isZeroPowerGuard = new SysMLGuard(contextBlock, isZeroPowerGuardCondition, "isZeroPower");
		isControlToPowerOffGuard = new SysMLGuard(contextBlock, isControlToPowerOffGuardCondition, "isControlToPowerOff");
	}

	@Override
	protected void createEffectActivities()
	{
		super.createEffectActivities();
		initialToPowerOffTransitionEffectActivity = (event, contextBlock) ->
		{
			C4S2ServicesComputer computer = (C4S2ServicesComputer)contextBlock.get();
			computer.initialize();
		};
		powerOffOnPowerSwitchedOnTransitionEffectActivity = (event, contextBlock) ->
		{
			C4S2ServicesComputer computer = (C4S2ServicesComputer)contextBlock.get();
			computer.onSwitchToPowerOn();
		};
		powerOffToPoweredOnTransitionEffectActivity = (event, contextBlock) ->
		{
			ElectricalPower power = ((ElectricalPowerEvent)event.get()).getPower();
			C4S2ServicesComputer computer = (C4S2ServicesComputer)contextBlock.get();
			computer.onElectricalPowerOn(power);
		};
		operationalOnControlTransitionEffectActivity = (event, contextBlock) ->
		{
			SNMPRequestEvent requestEvent = (SNMPRequestEvent)event.get();
			SNMPRequest request = (SNMPRequest)requestEvent.getRequest();
			C4S2ServicesComputer computer = (C4S2ServicesComputer)contextBlock.get();
			computer.onSNMPRequest(request);
		};
		operationalOnControlToPowerOffTransitionEffectActivity = (event, contextBlock) ->
		{
			C4S2ServicesComputer computer = (C4S2ServicesComputer)contextBlock.get();
			computer.onSNMPRequestToPowerOff();
		};
		operationalOnPowerSwitchedOffTransitionEffectActivity = (event, contextBlock) ->
		{
			C4S2ServicesComputer computer = (C4S2ServicesComputer)contextBlock.get();
			computer.onSwitchToPowerOff();
		};
		operationalToPowerOffTransitionEffectActivity = (event, contextBlock) ->
		{
			ElectricalPower power = ((ElectricalPowerEvent)event.get()).getPower();
			C4S2ServicesComputer computer = (C4S2ServicesComputer)contextBlock.get();
			computer.onElectricalPowerOff(power);
		};
	}

	@Override
	protected void createEffects()
	{
		super.createEffects();
		initialToPowerOffTransitionEffect = new SysMLEffect(contextBlock, initialToPowerOffTransitionEffectActivity, "InitialToPowerOffTransition");
		powerOffOnPowerSwitchedOnTransitionEffect = new SysMLEffect(contextBlock, powerOffOnPowerSwitchedOnTransitionEffectActivity, "PowerOffOnPowerSwitchedOnTransition");
		powerOffToInitializingTransitionEffect = new SysMLEffect(contextBlock, powerOffToPoweredOnTransitionEffectActivity, "PowerOffToPoweredOnTransition");
		operationalOnControlTransitionEffect = new SysMLEffect(contextBlock, operationalOnControlTransitionEffectActivity, "OperationalOnControlTransition");
		operationalOnControlToPowerOffTransitionEffect = new SysMLEffect(contextBlock, operationalOnControlToPowerOffTransitionEffectActivity, "OperationalOnControlToPowerOffTransition");
		operationalOnPowerSwitchedOffTransitionEffect = new SysMLEffect(contextBlock, operationalOnPowerSwitchedOffTransitionEffectActivity, "OperationalOnPowerSwitchedOffTransition");
		operationalToPowerOffTransitionEffect = new SysMLEffect(contextBlock, operationalToPowerOffTransitionEffectActivity, "OperationalToPowerOffTransition");
	}

	@Override
	protected void createTransitions()
	{
		initialToPowerOffTransition = new InitialTransition(contextBlock, initialState, powerOffState, initialToPowerOffTransitionEffect, "InitialToPowerOff");

		powerOffOnPowerSwitchedOnTransition = new SysMLTransition(contextBlock, powerOffState, powerOffState, Optional.of(OnOffSwitchEvent.class), Optional.of(isSwitchedOnGuard), Optional.of(powerOffOnPowerSwitchedOnTransitionEffect),
			"PowerOffOnPowerSwitchedOn", SysMLTransitionKind.internal);

		powerOffToInitializingTransition = new SysMLTransition(contextBlock, powerOffState, initializingState, Optional.of(ElectricalPowerEvent.class), Optional.of(isMinPowerGuard), Optional.of(powerOffToInitializingTransitionEffect),
			"PowerOffToInitializing", SysMLTransitionKind.external);

		initializingToOperationalTransition = new SysMLTransition(contextBlock, initializingState, operationalState, Optional.empty(), Optional.empty(), Optional.empty(), "InitializingToOperational", SysMLTransitionKind.external);

		operationalOnControlToPowerOffTransition = new SysMLTransition(contextBlock, operationalState, operationalState, Optional.of(SNMPRequestEvent.class), Optional.of(isControlToPowerOffGuard),
			Optional.of(operationalOnControlToPowerOffTransitionEffect), "OperationalOnControlToPowerOff", SysMLTransitionKind.internal);

		operationalOnPowerSwitchedOffTransition = new SysMLTransition(contextBlock, operationalState, operationalState, Optional.of(OnOffSwitchEvent.class), Optional.of(isSwitchedOffGuard),
			Optional.of(operationalOnPowerSwitchedOffTransitionEffect), "PowerOffOnPowerSwitchedOn", SysMLTransitionKind.internal);

		operationalOnControlTransition = new SysMLTransition(contextBlock, operationalState, operationalState, Optional.of(SNMPRequestEvent.class), Optional.empty(), Optional.of(operationalOnControlTransitionEffect), "OperationalOnControl",
			SysMLTransitionKind.internal);

		operationalToPowerOffTransition = new SysMLTransition(contextBlock, operationalState, powerOffState, Optional.of(ElectricalPowerEvent.class), Optional.of(isZeroPowerGuard), Optional.of(operationalToPowerOffTransitionEffect),
			"OperationalToPowerOff", SysMLTransitionKind.external);

		powerOffToFinalTransition = new FinalTransition(contextBlock, powerOffState, finalState, "PowerOffToFinal");
	}
}
