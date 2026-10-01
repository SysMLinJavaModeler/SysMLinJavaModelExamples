
package motorinwheel.components.suspensionchassisbody;

import java.util.Optional;
import motorinwheel.common.signals.AirResistanceSignal;
import motorinwheel.common.signals.MechanicalForceSignal;
import motorinwheel.common.stateMachine.SingleStateStateMachine;
import motorinwheel.components.wheel.WheelLocationEnum;
import sysmlinjava.attributetypes.ForceNewtons;
import sysmlinjava.events.SysMLSignalEvent;
import sysmlinjava.javaannotations.statemachines.Effect;
import sysmlinjava.javaannotations.statemachines.EffectActionFunction;
import sysmlinjava.javaannotations.statemachines.Guard;
import sysmlinjava.javaannotations.statemachines.GuardCondition;
import sysmlinjava.javaannotations.statemachines.Transition;
import sysmlinjava.states.SysMLEffect;
import sysmlinjava.states.SysMLEffectActionFunction;
import sysmlinjava.states.SysMLGuard;
import sysmlinjava.states.SysMLGuardCondition;
import sysmlinjava.states.SysMLTransition;
import sysmlinjava.states.SysMLTransitionKind;

/**
 * State machine for the Suspension/Chassis/Body model/simulation. The state
 * machine is a specialization of the {@code SingleStateMachine} consisting
 * mainly of internal state transitions for events on the power supply's
 * interface, i.e. initiating the weights of the motor and brake mounted on the
 * suspension, and changes to the air resistance forces on the body of the
 * vehicle. See the model of the state machine below for more detail.
 * 
 * @author ModelerOne
 *
 */
public class SuspensionChassisBodyStateMachine extends SingleStateStateMachine
{
	@Transition
	public SysMLTransition operationalOnAirResistanceTransition;
	@Transition
	public SysMLTransition operationalOnBrakeWeightTransition;
	@Transition
	public SysMLTransition operationalOnMotorWeightTransition;

	@GuardCondition
	private SysMLGuardCondition isAirResistanceGuardCondition;
	@GuardCondition
	private SysMLGuardCondition isBrakeWeightGuardCondition;
	@GuardCondition
	private SysMLGuardCondition isMotorWeightGuardCondition;
	
	@Guard
	private SysMLGuard isAirResistanceGuard;
	@Guard
	private SysMLGuard isBrakeWeightGuard;
	@Guard
	private SysMLGuard isMotorWeightGuard;
		
	@EffectActionFunction
	public SysMLEffectActionFunction operationalOnAirResistanceTransitionEffectActivity;
	@EffectActionFunction
	public SysMLEffectActionFunction operationalOnBrakeWeightTransitionEffectActivity;
	@EffectActionFunction
	public SysMLEffectActionFunction operationalOnMotorWeightTransitionEffectActivity;
	@EffectActionFunction
	public SysMLEffectActionFunction operationalOnSpeedViewTransitionEffectActivity;

	@Effect
	public SysMLEffect operationalOnAirResistanceTransitionEffect;
	@Effect
	public SysMLEffect operationalOnBrakeWeightTransitionEffect;
	@Effect
	public SysMLEffect operationalOnMotorWeightTransitionEffect;

	public SuspensionChassisBodyStateMachine(SuspensionChassisBody contextPart)
	{
		super(contextPart, true, "SuspensionChassisBodyStateMachine");
	}

	
	@Override
	protected void createGuardConditions()
	{
		super.createGuardConditions();
		isAirResistanceGuardCondition = (event, contextPart) ->
		{
			return ((SysMLSignalEvent)event.get()).signal instanceof AirResistanceSignal;
		};
		isBrakeWeightGuardCondition = (event, contextPart) ->
		{
			return ((SysMLSignalEvent)event.get()).signal instanceof MechanicalForceSignal;
		};
		isMotorWeightGuardCondition = (event, contextPart) ->
		{
			return ((SysMLSignalEvent)event.get()).signal instanceof MechanicalForceSignal;
		};
	}

	
	@Override
	protected void createGuards()
	{
		super.createGuards();
		isAirResistanceGuard = new SysMLGuard(context, isAirResistanceGuardCondition, "isAirResistance");
		isBrakeWeightGuard = new SysMLGuard(context, isBrakeWeightGuardCondition, "isBrakeWeight");
		isMotorWeightGuard = new SysMLGuard(context, isMotorWeightGuardCondition, "isMotorWeight");
	}


	@Override
	protected void createEffectActionFunctions()
	{
		super.createEffectActionFunctions();
		operationalOnAirResistanceTransitionEffectActivity = (event, contextPart) ->
		{
			SuspensionChassisBody suspension = (SuspensionChassisBody)contextPart.get();
			ForceNewtons airResistance = ((AirResistanceSignal)((SysMLSignalEvent)event.get()).signal).force;
			suspension.onAirResistance(airResistance);
		};
		operationalOnBrakeWeightTransitionEffectActivity = (event, contextPart) ->
		{
			SuspensionChassisBody suspension = (SuspensionChassisBody)contextPart.get();
			ForceNewtons brakeWeight = ((MechanicalForceSignal)((SysMLSignalEvent)event.get()).signal).force;
			WheelLocationEnum location = WheelLocationEnum.valueOf(((MechanicalForceSignal)((SysMLSignalEvent)event.get()).signal).id.intValue());
			suspension.onBrakeWeight(brakeWeight, location);
		};
		operationalOnMotorWeightTransitionEffectActivity = (event, contextPart) ->
		{
			SuspensionChassisBody suspension = (SuspensionChassisBody)contextPart.get();
			ForceNewtons motorWeight = ((MechanicalForceSignal)((SysMLSignalEvent)event.get()).signal).force;
			WheelLocationEnum location = WheelLocationEnum.valueOf(((MechanicalForceSignal)((SysMLSignalEvent)event.get()).signal).id.intValue());
			suspension.onMotorWeight(motorWeight, location);
		};
	}

	@Override
	protected void createEffects()
	{
		super.createEffects();
		operationalOnAirResistanceTransitionEffect = new SysMLEffect(context, operationalOnAirResistanceTransitionEffectActivity, "OperationalOnAirResistanceTransition");
		operationalOnBrakeWeightTransitionEffect = new SysMLEffect(context, operationalOnBrakeWeightTransitionEffectActivity, "OperationalOnBrakeWeightTransition");
		operationalOnMotorWeightTransitionEffect = new SysMLEffect(context, operationalOnMotorWeightTransitionEffectActivity, "OperationalOnMotorWeightTransition");
	}

	@Override
	protected void createTransitions()
	{
		super.createTransitions();
		operationalOnAirResistanceTransition = new SysMLTransition(context, operationalState, operationalState, Optional.of(SysMLSignalEvent.class), Optional.of(isAirResistanceGuard), Optional.of(operationalOnAirResistanceTransitionEffect),
			"OperationalOnAirResistance", SysMLTransitionKind.internal);
		operationalOnBrakeWeightTransition = new SysMLTransition(context, operationalState, operationalState, Optional.of(SysMLSignalEvent.class), Optional.of(isBrakeWeightGuard), Optional.of(operationalOnBrakeWeightTransitionEffect),
			"OperationalOnBrakeWeight", SysMLTransitionKind.internal);
		operationalOnMotorWeightTransition = new SysMLTransition(context, operationalState, operationalState, Optional.of(SysMLSignalEvent.class), Optional.of(isMotorWeightGuard), Optional.of(operationalOnMotorWeightTransitionEffect),
			"OperationalOnMotorWeight", SysMLTransitionKind.internal);
	}
}
