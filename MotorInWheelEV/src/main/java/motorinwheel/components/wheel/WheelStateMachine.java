package motorinwheel.components.wheel;

import java.util.Optional;
import motorinwheel.common.events.MotorTorqueEvent;
import motorinwheel.common.events.BrakeTorqueEvent;
import motorinwheel.common.events.MechanicalForceEvent;
import motorinwheel.common.events.RoadwayForcesEvent;
import motorinwheel.common.stateMachine.SingleStateStateMachine;
import sysmlinjava.annotations.statemachines.Effect;
import sysmlinjava.annotations.statemachines.EffectActivity;
import sysmlinjava.annotations.statemachines.Transition;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.statemachine.SysMLEffect;
import sysmlinjava.statemachine.SysMLEffectActivity;
import sysmlinjava.statemachine.SysMLTransition;
import sysmlinjava.statemachine.SysMLTransitionKind;
import sysmlinjava.valuetypes.ForceNewtons;
import sysmlinjava.valuetypes.TorqueNewtonMeters;

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

	@EffectActivity
	public SysMLEffectActivity operationalOnMotorTorqueTransitionEffectActivity;
	@EffectActivity
	public SysMLEffectActivity operationalOnBrakeTorqueTransitionEffectActivity;
	@EffectActivity
	public SysMLEffectActivity operationalOnRoadwayForceTransitionEffectActivity;
	@EffectActivity
	public SysMLEffectActivity operationalOnSuspensionForceTransitionEffectActivity;

	@Effect
	public SysMLEffect operationalOnMotorTorqueTransitionEffect;
	@Effect
	public SysMLEffect operationalOnBrakeTorqueTransitionEffect;
	@Effect
	public SysMLEffect operationalOnRoadwayForceTransitionEffect;
	@Effect
	public SysMLEffect operationalOnSuspensionForceTransitionEffect;

	public WheelStateMachine(SysMLBlock contextBlock)
	{
		super(contextBlock, true, "WheelStateMachine");
	}

	@Override
	protected void createStates()
	{
		super.createStates();
	}

	@Override
	protected void createEffectActivities()
	{
		super.createEffectActivities();
		this.operationalOnBrakeTorqueTransitionEffectActivity = (event, contextBlock) ->
		{
			Wheel wheel = (Wheel)contextBlock.get();
			TorqueNewtonMeters brakeTorque = ((BrakeTorqueEvent)event.get()).getTorque();
			wheel.onBrakeTorque(brakeTorque);
		};
		this.operationalOnMotorTorqueTransitionEffectActivity = (event, contextBlock) ->
		{
			Wheel wheel = (Wheel)contextBlock.get();
			TorqueNewtonMeters motorTorque = ((MotorTorqueEvent)event.get()).getTorque();
			wheel.onMotorTorque(motorTorque);
		};
		this.operationalOnRoadwayForceTransitionEffectActivity = (event, contextBlock) ->
		{
			Wheel wheel = (Wheel)contextBlock.get();
			ForceNewtons roadwayForce = ((RoadwayForcesEvent)event.get()).getForce();
			wheel.onRoadwayForce(roadwayForce);
		};
		this.operationalOnSuspensionForceTransitionEffectActivity = (event, contextBlock) ->
		{
			Wheel wheel = (Wheel)contextBlock.get();
			ForceNewtons suspensionForce = ((MechanicalForceEvent)event.get()).getForce();
			wheel.onSuspensionForce(suspensionForce);
		};
	}

	@Override
	protected void createEffects()
	{
		super.createEffects();
		this.operationalOnBrakeTorqueTransitionEffect = new SysMLEffect(contextBlock, operationalOnBrakeTorqueTransitionEffectActivity, "OperationalOnBrakeTorqueTransition");
		this.operationalOnMotorTorqueTransitionEffect = new SysMLEffect(contextBlock, operationalOnMotorTorqueTransitionEffectActivity, "OperationalOnMotorTorqueTransition");
		this.operationalOnRoadwayForceTransitionEffect = new SysMLEffect(contextBlock, operationalOnRoadwayForceTransitionEffectActivity, "OperationalOnRoadwayForceTransition");
		this.operationalOnSuspensionForceTransitionEffect = new SysMLEffect(contextBlock, operationalOnSuspensionForceTransitionEffectActivity, "OperationalOnSuspensionForceTransition");
	}

	@Override
	protected void createTransitions()
	{
		super.createTransitions();
		operationalOnMotorTorqueTransition = new SysMLTransition(contextBlock, operationalState, operationalState, Optional.of(MotorTorqueEvent.class), Optional.empty(), Optional.of(operationalOnMotorTorqueTransitionEffect),
			"OperationalOnMotorTorque", SysMLTransitionKind.internal);
		operationalOnBrakeTorqueTransition = new SysMLTransition(contextBlock, operationalState, operationalState, Optional.of(BrakeTorqueEvent.class), Optional.empty(), Optional.of(operationalOnBrakeTorqueTransitionEffect),
			"OperationalOnBrakeTorque", SysMLTransitionKind.internal);
		operationalOnRoadwayForceTransition = new SysMLTransition(contextBlock, operationalState, operationalState, Optional.of(RoadwayForcesEvent.class), Optional.empty(), Optional.of(operationalOnRoadwayForceTransitionEffect),
			"OperationalOnRoadwayForce", SysMLTransitionKind.internal);
		operationalOnSuspensionForceTransition = new SysMLTransition(contextBlock, operationalState, operationalState, Optional.of(MechanicalForceEvent.class), Optional.empty(), Optional.of(operationalOnSuspensionForceTransitionEffect),
			"OperationalOnSuspensionForce", SysMLTransitionKind.internal);
	}
}
