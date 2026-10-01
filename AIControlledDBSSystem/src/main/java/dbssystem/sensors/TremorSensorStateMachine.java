
package dbssystem.sensors;

import dbssystem.common.MotionSignalSignal;
import sysmlinjava.events.SysMLSignalEvent;

/**
 * State machine for the TremorSensor. As a specialized
 * {@code SensorStateMachine}, the {@code TremorSensorStateMachine} consists of
 * only two states - one to initialize the sensor and one to repeatedly handle
 * receipts of motion signals from the patient. The unique guard and effect
 * needed for this sensor are specified.
 * 
 * @author ModelerOne
 */
public class TremorSensorStateMachine extends SensorStateMachine
{
	/**
	 * Constructor
	 * 
	 * @param contextSensor sensor in whose context this state machine executes
	 */
	public TremorSensorStateMachine(TremorSensor contextSensor)
	{
		super(contextSensor, "TremorSensorStateMachine");
	}

	@Override
	protected void createGuardConditions()
	{
		isSensedValueGuardCondition = (event, contextSensor) ->
		{
			return contextSensor.isPresent() &&
				contextSensor.get() instanceof TremorSensor &&
				event.isPresent() &&
				event.get() instanceof SysMLSignalEvent signalEvent &&
				(signalEvent.signal instanceof MotionSignalSignal);
		};
	}

	@Override
	protected void createEffectActionFunctions()
	{
		onSensedValueSignalEffectActivity = (event, contextSensor) ->
		{
			if (event.isPresent())
			{
				if (contextSensor.isPresent() && contextSensor.get() instanceof TremorSensor sensor)
				{
					if (event.get() instanceof SysMLSignalEvent signalEvent)
					{
						if (signalEvent.signal instanceof MotionSignalSignal motionSignalSignal)
							sensor.onMotion(motionSignalSignal.value);
					}
					else
						logger.warning("unrecognized signal event: " + event.get().getClass().getSimpleName() + ", i.e. not a SysMLSignalEvent");
				}
				else
					logger.warning("missing TremorSensor context");
			}
			else
				logger.warning("missing sensed value signal event");
		};
	}
}
