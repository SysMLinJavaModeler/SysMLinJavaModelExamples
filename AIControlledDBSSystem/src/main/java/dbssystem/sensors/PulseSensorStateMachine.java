package dbssystem.sensors;

import dbssystem.common.PressureSignalSignal;
import dbssystem.common.TremorPresenceSignal;
import sysmlinjava.events.SysMLSignalEvent;

/**
 * State machine for the pulse sensor. As a specialized
 * {@code SensorStateMachine}, the {@code PulstSensorStateMachine} consists of
 * only two states - one to initialize the sensor and one to repeatedly handle
 * receipts of pressure signals from the patient as well as tremor presence
 * signals from the tremor sensor. The unique guard and effect needed for this
 * sensor are specified.
 * 
 * @author ModelerOne
 */
public class PulseSensorStateMachine extends SensorStateMachine
{
	/**
	 * Constructor
	 * 
	 * @param contextSensor
	 */
	public PulseSensorStateMachine(PulseSensor contextSensor)
	{
		super(contextSensor, "PulseSensorStateMachine");
	}

	@Override
	protected void createGuardConditions()
	{
		isSensedValueGuardCondition = (event, contextSensor) ->
		{
			return contextSensor.isPresent() &&
				contextSensor.get() instanceof PulseSensor &&
				event.isPresent() &&
				event.get() instanceof SysMLSignalEvent signalEvent &&
				(signalEvent.signal instanceof PressureSignalSignal || signalEvent.signal instanceof TremorPresenceSignal);
		};
	}

	@Override
	protected void createEffectActionFunctions()
	{
		onSensedValueSignalEffectActivity = (event, contextSensor) ->
		{
			if (event.isPresent())
			{
				if (contextSensor.isPresent() && contextSensor.get() instanceof PulseSensor sensor)
				{
					if (event.get() instanceof SysMLSignalEvent signalEvent)
					{
						if (signalEvent.signal instanceof PressureSignalSignal pressureSignalSignal)
							sensor.onPressureSignal(pressureSignalSignal.value);
						else if (signalEvent.signal instanceof TremorPresenceSignal tremorPresenceSignal)
							sensor.onTremorPresence(tremorPresenceSignal.isPresent);
					}
					else
						logger.warning("unrecognized signal event: " + event.get().getClass().getSimpleName() + ", i.e. not a SysMLSignalEvent");
				}
				else
					logger.warning("missing PulseSensor context");
			}
			else
				logger.warning("missing sensed value signal event");
		};
	}

}
