
package motorinwheel.components.suspensionchassisbody;

import java.util.Optional;
import motorinwheel.common.events.AirResistanceEvent;
import motorinwheel.common.events.BrakeWeightEvent;
import motorinwheel.common.events.MotorWeightEvent;
import motorinwheel.common.stateMachine.SingleStateStateMachine;
import motorinwheel.components.wheel.WheelLocationEnum;
import sysmlinjava.annotations.statemachines.Effect;
import sysmlinjava.annotations.statemachines.EffectActivity;
import sysmlinjava.annotations.statemachines.Transition;
import sysmlinjava.statemachine.SysMLEffect;
import sysmlinjava.statemachine.SysMLEffectActivity;
import sysmlinjava.statemachine.SysMLTransition;
import sysmlinjava.statemachine.SysMLTransitionKind;
import sysmlinjava.valuetypes.ForceNewtons;

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

	@EffectActivity
	public SysMLEffectActivity operationalOnAirResistanceTransitionEffectActivity;
	@EffectActivity
	public SysMLEffectActivity operationalOnBrakeWeightTransitionEffectActivity;
	@EffectActivity
	public SysMLEffectActivity operationalOnMotorWeightTransitionEffectActivity;
	@EffectActivity
	public SysMLEffectActivity operationalOnSpeedViewTransitionEffectActivity;

	@Effect
	public SysMLEffect operationalOnAirResistanceTransitionEffect;
	@Effect
	public SysMLEffect operationalOnBrakeWeightTransitionEffect;
	@Effect
	public SysMLEffect operationalOnMotorWeightTransitionEffect;

	public SuspensionChassisBodyStateMachine(SuspensionChassisBody contextBlock)
	{
		super(contextBlock, true, "SuspensionChassisBodyStateMachine");
	}

	@Override
	protected void createEffectActivities()
	{
		super.createEffectActivities();
		operationalOnAirResistanceTransitionEffectActivity = (event, contextBlock) ->
		{
			SuspensionChassisBody suspension = (SuspensionChassisBody)contextBlock.get();
			ForceNewtons airResistance = ((AirResistanceEvent)event.get()).getForce();
			suspension.onAirResistance(airResistance);
		};
		operationalOnBrakeWeightTransitionEffectActivity = (event, contextBlock) ->
		{
			SuspensionChassisBody suspension = (SuspensionChassisBody)contextBlock.get();
			ForceNewtons brakeWeight = ((BrakeWeightEvent)event.get()).getForce();
			WheelLocationEnum location = WheelLocationEnum.valueOf(((BrakeWeightEvent)event.get()).getID().intValue());
			suspension.onBrakeWeight(brakeWeight, location);
		};
		operationalOnMotorWeightTransitionEffectActivity = (event, contextBlock) ->
		{
			SuspensionChassisBody suspension = (SuspensionChassisBody)contextBlock.get();
			ForceNewtons motorWeight = ((MotorWeightEvent)event.get()).getForce();
			WheelLocationEnum location = WheelLocationEnum.valueOf(((MotorWeightEvent)event.get()).getID().intValue());
			suspension.onMotorWeight(motorWeight, location);
		};
	}

	@Override
	protected void createEffects()
	{
		super.createEffects();
		operationalOnAirResistanceTransitionEffect = new SysMLEffect(contextBlock, operationalOnAirResistanceTransitionEffectActivity, "OperationalOnAirResistanceTransition");
		operationalOnBrakeWeightTransitionEffect = new SysMLEffect(contextBlock, operationalOnBrakeWeightTransitionEffectActivity, "OperationalOnBrakeWeightTransition");
		operationalOnMotorWeightTransitionEffect = new SysMLEffect(contextBlock, operationalOnMotorWeightTransitionEffectActivity, "OperationalOnMotorWeightTransition");
	}

	@Override
	protected void createTransitions()
	{
		super.createTransitions();
		operationalOnAirResistanceTransition = new SysMLTransition(contextBlock, operationalState, operationalState, Optional.of(AirResistanceEvent.class), Optional.empty(), Optional.of(operationalOnAirResistanceTransitionEffect),
			"OperationalOnAirResistance", SysMLTransitionKind.internal);
		operationalOnBrakeWeightTransition = new SysMLTransition(contextBlock, operationalState, operationalState, Optional.of(BrakeWeightEvent.class), Optional.empty(), Optional.of(operationalOnBrakeWeightTransitionEffect),
			"OperationalOnBrakeWeight", SysMLTransitionKind.internal);
		operationalOnMotorWeightTransition = new SysMLTransition(contextBlock, operationalState, operationalState, Optional.of(MotorWeightEvent.class), Optional.empty(), Optional.of(operationalOnMotorWeightTransitionEffect),
			"OperationalOnMotorWeight", SysMLTransitionKind.internal);
	}
}
