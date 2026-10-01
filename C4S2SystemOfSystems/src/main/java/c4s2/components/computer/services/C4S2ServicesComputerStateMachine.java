package c4s2.components.computer.services;

import java.util.Optional;

import c4s2.common.attributetypes.C4S2ServicesComputerStatesEnum;
import sysmlinjava.attributetypes.ElectricalPower;
import sysmlinjava.attributetypes.RReal;
import sysmlinjava.events.SysMLSignalEvent;
import sysmlinjava.javaannotations.statemachines.Effect;
import sysmlinjava.javaannotations.statemachines.EffectActionFunction;
import sysmlinjava.javaannotations.statemachines.Guard;
import sysmlinjava.javaannotations.statemachines.GuardCondition;
import sysmlinjava.javaannotations.statemachines.State;
import sysmlinjava.javaannotations.statemachines.Transition;
import sysmlinjava.states.FinalTransition;
import sysmlinjava.states.InitialTransition;
import sysmlinjava.states.SysMLEffect;
import sysmlinjava.states.SysMLEffectActionFunction;
import sysmlinjava.states.SysMLGuard;
import sysmlinjava.states.SysMLGuardCondition;
import sysmlinjava.states.SysMLState;
import sysmlinjava.states.SysMLStateMachine;
import sysmlinjava.states.SysMLTransition;
import sysmlinjava.states.SysMLTransitionKind;
import sysmlinjavalibrary.common.objects.information.SNMPRequest;
import sysmlinjavalibrary.common.signals.ElectricalPowerSignal;
import sysmlinjavalibrary.common.signals.OnOffSwitchSignal;
import sysmlinjavalibrary.common.signals.SNMPRequestSignal;

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
	@GuardCondition
	private SysMLGuardCondition isControlGuardCondition;

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
	@Guard
	private SysMLGuard isControlGuard;

	@EffectActionFunction
	private SysMLEffectActionFunction initialToPowerOffTransitionEffectActivity;
	@EffectActionFunction
	private SysMLEffectActionFunction powerOffOnPowerSwitchedOnTransitionEffectActivity;
	@EffectActionFunction
	private SysMLEffectActionFunction powerOffToPoweredOnTransitionEffectActivity;
	@EffectActionFunction
	private SysMLEffectActionFunction operationalOnControlTransitionEffectActivity;
	@EffectActionFunction
	private SysMLEffectActionFunction operationalOnControlToPowerOffTransitionEffectActivity;
	@EffectActionFunction
	private SysMLEffectActionFunction operationalOnPowerSwitchedOffTransitionEffectActivity;
	@EffectActionFunction
	private SysMLEffectActionFunction operationalToPowerOffTransitionEffectActivity;

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
		powerOffState = new SysMLState(context, Optional.empty(), Optional.empty(), Optional.empty(), "PowerOff");
		initializingState = new SysMLState(context, Optional.empty(), Optional.empty(), Optional.empty(), "Initializing");
		operationalState = new SysMLState(context, Optional.empty(), Optional.empty(), Optional.empty(), "Operational");
	}

	@Override
	protected void createGuardConditions()
	{
		super.createGuardConditions();
		isSwitchedOnGuardCondition = (event, contextBlock) ->
		{
			return event.isPresent() &&
				event.get() instanceof SysMLSignalEvent &&
				((SysMLSignalEvent)event.get()).signal instanceof OnOffSwitchSignal &&
				((OnOffSwitchSignal)((SysMLSignalEvent)event.get()).signal).onOffSwitch.isOn;
		};
		isSwitchedOffGuardCondition = (event, contextBlock) ->
		{
			return event.isPresent() &&
				event.get() instanceof SysMLSignalEvent &&
				((SysMLSignalEvent)event.get()).signal instanceof OnOffSwitchSignal &&
				!((OnOffSwitchSignal)((SysMLSignalEvent)event.get()).signal).onOffSwitch.isOn;
		};
		isMinPowerGuardCondition = (event, contextBlock) ->
		{
			C4S2ServicesComputer computer = (C4S2ServicesComputer)contextBlock.get();
			return event.isPresent() &&
				event.get() instanceof SysMLSignalEvent &&
				((SysMLSignalEvent)event.get()).signal instanceof ElectricalPowerSignal &&
				((ElectricalPowerSignal)((SysMLSignalEvent)event.get()).signal).power.current.greaterThan(computer.minCurrentIn);
		};
		isControlToPowerOffGuardCondition = (event, contextBlock) ->
		{
			boolean result = false;
			if (event.isPresent() &&
				event.get() instanceof SysMLSignalEvent &&
				((SysMLSignalEvent)event.get()).signal instanceof SNMPRequestSignal)
			{
				SNMPRequest request = ((SNMPRequestSignal)((SysMLSignalEvent)event.get()).signal).request;
				C4S2ServicesComputerStatesEnum state = C4S2ServicesComputerStatesEnum.valueOf(request.mib.getDataStrings().get(1));
				if (state == C4S2ServicesComputerStatesEnum.PowerOff)
					result = true;
			}
			return result;
		};
		isZeroPowerGuardCondition = (event, contextBlock) ->
		{
			return event.isPresent() &&
				event.get() instanceof SysMLSignalEvent &&
				((SysMLSignalEvent)event.get()).signal instanceof ElectricalPowerSignal &&
				((ElectricalPowerSignal)((SysMLSignalEvent)event.get()).signal).power.watts().lessThan(RReal.of(1));
		};
		isControlGuardCondition = (event, contextBlock) ->
		{
			return event.isPresent() &&
				event.get() instanceof SysMLSignalEvent &&
				((SysMLSignalEvent)event.get()).signal instanceof SNMPRequestSignal;
		};
	}

	@Override
	protected void createGuards()
	{
		super.createGuards();
		isSwitchedOnGuard = new SysMLGuard(context, isSwitchedOnGuardCondition, "isSwitchedOn");
		isSwitchedOffGuard = new SysMLGuard(context, isSwitchedOffGuardCondition, "isSwitchedOff");
		isMinPowerGuard = new SysMLGuard(context, isMinPowerGuardCondition, "isMinPower");
		isZeroPowerGuard = new SysMLGuard(context, isZeroPowerGuardCondition, "isZeroPower");
		isControlToPowerOffGuard = new SysMLGuard(context, isControlToPowerOffGuardCondition, "isControlToPowerOff");
		isControlGuard = new SysMLGuard(context, isControlGuardCondition, "isControl");
	}

	@Override
	protected void createEffectActionFunctions()
	{
		super.createEffectActionFunctions();
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
			ElectricalPower power = ((ElectricalPowerSignal)((SysMLSignalEvent)event.get()).signal).power;
			C4S2ServicesComputer computer = (C4S2ServicesComputer)contextBlock.get();
			computer.onElectricalPowerOn(power);
		};
		operationalOnControlTransitionEffectActivity = (event, contextBlock) ->
		{
			SNMPRequest request = ((SNMPRequestSignal)((SysMLSignalEvent)event.get()).signal).request;
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
			ElectricalPower power = ((ElectricalPowerSignal)((SysMLSignalEvent)event.get()).signal).power;
			C4S2ServicesComputer computer = (C4S2ServicesComputer)contextBlock.get();
			computer.onElectricalPowerOff(power);
		};
	}

	@Override
	protected void createEffects()
	{
		super.createEffects();
		initialToPowerOffTransitionEffect = new SysMLEffect(context, initialToPowerOffTransitionEffectActivity, "InitialToPowerOffTransition");
		powerOffOnPowerSwitchedOnTransitionEffect = new SysMLEffect(context, powerOffOnPowerSwitchedOnTransitionEffectActivity, "PowerOffOnPowerSwitchedOnTransition");
		powerOffToInitializingTransitionEffect = new SysMLEffect(context, powerOffToPoweredOnTransitionEffectActivity, "PowerOffToPoweredOnTransition");
		operationalOnControlTransitionEffect = new SysMLEffect(context, operationalOnControlTransitionEffectActivity, "OperationalOnControlTransition");
		operationalOnControlToPowerOffTransitionEffect = new SysMLEffect(context, operationalOnControlToPowerOffTransitionEffectActivity, "OperationalOnControlToPowerOffTransition");
		operationalOnPowerSwitchedOffTransitionEffect = new SysMLEffect(context, operationalOnPowerSwitchedOffTransitionEffectActivity, "OperationalOnPowerSwitchedOffTransition");
		operationalToPowerOffTransitionEffect = new SysMLEffect(context, operationalToPowerOffTransitionEffectActivity, "OperationalToPowerOffTransition");
	}

	@Override
	protected void createTransitions()
	{
		initialToPowerOffTransition = new InitialTransition(context, initialState, powerOffState, initialToPowerOffTransitionEffect, "InitialToPowerOff");

		powerOffOnPowerSwitchedOnTransition = new SysMLTransition(context, powerOffState, powerOffState, Optional.of(SysMLSignalEvent.class), Optional.of(isSwitchedOnGuard), Optional.of(powerOffOnPowerSwitchedOnTransitionEffect),
			"PowerOffOnPowerSwitchedOn", SysMLTransitionKind.internal);

		powerOffToInitializingTransition = new SysMLTransition(context, powerOffState, initializingState, Optional.of(SysMLSignalEvent.class), Optional.of(isMinPowerGuard), Optional.of(powerOffToInitializingTransitionEffect),
			"PowerOffToInitializing", SysMLTransitionKind.external);

		initializingToOperationalTransition = new SysMLTransition(context, initializingState, operationalState, Optional.empty(), Optional.empty(), Optional.empty(), "InitializingToOperational", SysMLTransitionKind.external);

		operationalOnControlToPowerOffTransition = new SysMLTransition(context, operationalState, operationalState, Optional.of(SysMLSignalEvent.class), Optional.of(isControlToPowerOffGuard),
			Optional.of(operationalOnControlToPowerOffTransitionEffect), "OperationalOnControlToPowerOff", SysMLTransitionKind.internal);

		operationalOnPowerSwitchedOffTransition = new SysMLTransition(context, operationalState, operationalState, Optional.of(SysMLSignalEvent.class), Optional.of(isSwitchedOffGuard),
			Optional.of(operationalOnPowerSwitchedOffTransitionEffect), "PowerOffOnPowerSwitchedOn", SysMLTransitionKind.internal);

		operationalOnControlTransition = new SysMLTransition(context, operationalState, operationalState, Optional.of(SysMLSignalEvent.class), Optional.of(isControlGuard), Optional.of(operationalOnControlTransitionEffect), "OperationalOnControl",
			SysMLTransitionKind.internal);

		operationalToPowerOffTransition = new SysMLTransition(context, operationalState, powerOffState, Optional.of(SysMLSignalEvent.class), Optional.of(isZeroPowerGuard), Optional.of(operationalToPowerOffTransitionEffect),
			"OperationalToPowerOff", SysMLTransitionKind.external);

		powerOffToFinalTransition = new FinalTransition(context, powerOffState, finalState, "PowerOffToFinal");
	}
}
