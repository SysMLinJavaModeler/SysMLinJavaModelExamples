package motorinwheel.components.wheel;

import java.util.Optional;
import motorinwheel.common.signals.BrakeTorqueSignal;
import motorinwheel.common.signals.MechanicalForceSignal;
import motorinwheel.common.signals.MotorTorqueSignal;
import motorinwheel.common.signals.RoadwayForcesSignal;
import motorinwheel.common.stateMachine.SingleStateStateMachine;
import sysmlinjava.attributetypes.ForceNewtons;
import sysmlinjava.attributetypes.TorqueNewtonMeters;
import sysmlinjava.events.SysMLSignalEvent;
import sysmlinjava.javaannotations.statemachines.Effect;
import sysmlinjava.javaannotations.statemachines.EffectActionFunction;
import sysmlinjava.javaannotations.statemachines.Guard;
import sysmlinjava.javaannotations.statemachines.GuardCondition;
import sysmlinjava.javaannotations.statemachines.Transition;
import sysmlinjava.parts.SysMLPart;
import sysmlinjava.states.SysMLEffect;
import sysmlinjava.states.SysMLEffectActionFunction;
import sysmlinjava.states.SysMLGuard;
import sysmlinjava.states.SysMLGuardCondition;
import sysmlinjava.states.SysMLTransition;
import sysmlinjava.states.SysMLTransitionKind;

/**
 * State machine for the Wheel model/simulation. The state machine is a
 * specialization of the {@code SingleStateMachine} consisting mainly of
 * internal state transitions for events on wheel interfaces, i.e. changes to
 * torque from the motor and brake, and changes in the force from the roadway
 * and suspension. See the model of the state machine below for more detail.
 * 
 * @author ModelerOne
 *
 */
public class WheelStateMachine extends SingleStateStateMachine
{
	@Transition
	public SysMLTransition operationalOnMotorTorqueTransition;
	@Transition
	public SysMLTransition operationalOnBrakeTorqueTransition;
	@Transition
	public SysMLTransition operationalOnRoadwayForceTransition;
	@Transition
	public SysMLTransition operationalOnSuspensionForceTransition;

	@GuardCondition
	private SysMLGuardCondition isMotorTorqueGuardCondition;
	@GuardCondition
	private SysMLGuardCondition isBrakeTorqueGuardCondition;
	@GuardCondition
	private SysMLGuardCondition isRoadwayForceGuardCondition;
	@GuardCondition
	private SysMLGuardCondition isSuspensionForceGuardCondition;
	
	@Guard
	private SysMLGuard isMotorTorqueGuard;
	@Guard
	private SysMLGuard isBrakeTorqueGuard;
	@Guard
	private SysMLGuard isRoadwayForceGuard;
	@Guard
	private SysMLGuard isSuspensionForceGuard;
	
	@EffectActionFunction
	public SysMLEffectActionFunction operationalOnMotorTorqueTransitionEffectActivity;
	@EffectActionFunction
	public SysMLEffectActionFunction operationalOnBrakeTorqueTransitionEffectActivity;
	@EffectActionFunction
	public SysMLEffectActionFunction operationalOnRoadwayForceTransitionEffectActivity;
	@EffectActionFunction
	public SysMLEffectActionFunction operationalOnSuspensionForceTransitionEffectActivity;

	@Effect
	public SysMLEffect operationalOnMotorTorqueTransitionEffect;
	@Effect
	public SysMLEffect operationalOnBrakeTorqueTransitionEffect;
	@Effect
	public SysMLEffect operationalOnRoadwayForceTransitionEffect;
	@Effect
	public SysMLEffect operationalOnSuspensionForceTransitionEffect;

	public WheelStateMachine(SysMLPart contextPart)
	{
		super(contextPart, true, "WheelStateMachine");
	}

	@Override
	protected void createStates()
	{
		super.createStates();
	}
	
	@Override
	protected void createGuardConditions()
	{
		super.createGuardConditions();
		isBrakeTorqueGuardCondition = (event, contextPart) ->
		{
			return ((SysMLSignalEvent)event.get()).signal instanceof BrakeTorqueSignal;
		};
		isMotorTorqueGuardCondition = (event, contextPart) ->
		{
			return ((SysMLSignalEvent)event.get()).signal instanceof MotorTorqueSignal;
		};
		isRoadwayForceGuardCondition = (event, contextPart) ->
		{
			return ((SysMLSignalEvent)event.get()).signal instanceof RoadwayForcesSignal;
		};
		isSuspensionForceGuardCondition = (event, contextPart) ->
		{
			return ((SysMLSignalEvent)event.get()).signal instanceof MechanicalForceSignal;
		};
	}

	@Override
	protected void createGuards()
	{
		super.createGuards();
		isBrakeTorqueGuard = new SysMLGuard(context, isBrakeTorqueGuardCondition, "isBrakeTorque");
		isMotorTorqueGuard = new SysMLGuard(context, isMotorTorqueGuardCondition, "isMotorTorque");
		isRoadwayForceGuard = new SysMLGuard(context, isRoadwayForceGuardCondition, "isRoadwayForce");
		isSuspensionForceGuard = new SysMLGuard(context, isSuspensionForceGuardCondition, "isSuspensionForce");
	}

	@Override
	protected void createEffectActionFunctions()
	{
		super.createEffectActionFunctions();
		operationalOnBrakeTorqueTransitionEffectActivity = (event, contextPart) ->
		{
			Wheel wheel = (Wheel)contextPart.get();
			TorqueNewtonMeters brakeTorque = ((BrakeTorqueSignal)((SysMLSignalEvent)event.get()).signal).torque;
			wheel.onBrakeTorque(brakeTorque);
		};
		operationalOnMotorTorqueTransitionEffectActivity = (event, contextPart) ->
		{
			Wheel wheel = (Wheel)contextPart.get();
			TorqueNewtonMeters motorTorque = ((MotorTorqueSignal)((SysMLSignalEvent)event.get()).signal).torque;
			wheel.onMotorTorque(motorTorque);
		};
		operationalOnRoadwayForceTransitionEffectActivity = (event, contextPart) ->
		{
			Wheel wheel = (Wheel)contextPart.get();
			ForceNewtons roadwayForce = ((RoadwayForcesSignal)((SysMLSignalEvent)event.get()).signal).force;
			wheel.onRoadwayForce(roadwayForce);
		};
		operationalOnSuspensionForceTransitionEffectActivity = (event, contextPart) ->
		{
			Wheel wheel = (Wheel)contextPart.get();
			ForceNewtons suspensionForce = ((MechanicalForceSignal)((SysMLSignalEvent)event.get()).signal).force;
			wheel.onSuspensionForce(suspensionForce);
		};
	}

	@Override
	protected void createEffects()
	{
		super.createEffects();
		operationalOnBrakeTorqueTransitionEffect = new SysMLEffect(context, operationalOnBrakeTorqueTransitionEffectActivity, "OperationalOnBrakeTorqueTransition");
		operationalOnMotorTorqueTransitionEffect = new SysMLEffect(context, operationalOnMotorTorqueTransitionEffectActivity, "OperationalOnMotorTorqueTransition");
		operationalOnRoadwayForceTransitionEffect = new SysMLEffect(context, operationalOnRoadwayForceTransitionEffectActivity, "OperationalOnRoadwayForceTransition");
		operationalOnSuspensionForceTransitionEffect = new SysMLEffect(context, operationalOnSuspensionForceTransitionEffectActivity, "OperationalOnSuspensionForceTransition");
	}

	@Override
	protected void createTransitions()
	{
		super.createTransitions();
		operationalOnMotorTorqueTransition = new SysMLTransition(context, operationalState, operationalState, Optional.of(SysMLSignalEvent.class), Optional.of(isMotorTorqueGuard), Optional.of(operationalOnMotorTorqueTransitionEffect),
			"OperationalOnMotorTorque", SysMLTransitionKind.internal);
		operationalOnBrakeTorqueTransition = new SysMLTransition(context, operationalState, operationalState, Optional.of(SysMLSignalEvent.class), Optional.of(isBrakeTorqueGuard), Optional.of(operationalOnBrakeTorqueTransitionEffect),
			"OperationalOnBrakeTorque", SysMLTransitionKind.internal);
		operationalOnRoadwayForceTransition = new SysMLTransition(context, operationalState, operationalState, Optional.of(SysMLSignalEvent.class), Optional.of(isRoadwayForceGuard), Optional.of(operationalOnRoadwayForceTransitionEffect),
			"OperationalOnRoadwayForce", SysMLTransitionKind.internal);
		operationalOnSuspensionForceTransition = new SysMLTransition(context, operationalState, operationalState, Optional.of(SysMLSignalEvent.class), Optional.of(isSuspensionForceGuard), Optional.of(operationalOnSuspensionForceTransitionEffect),
			"OperationalOnSuspensionForce", SysMLTransitionKind.internal);
	}
}
