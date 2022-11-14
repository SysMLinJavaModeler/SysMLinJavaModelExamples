package hflink.systems.gps;

import java.time.LocalTime;
import java.util.Optional;
import hflink.common.objects.GPSMessage;
import hflink.common.ports.GPSMessagingTransmitProtocol;
import sysmlinjava.annotations.FullPort;
import sysmlinjava.annotations.Operation;
import sysmlinjava.annotations.Value;
import sysmlinjava.annotations.events.TimeEvent;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.events.SysMLTimeEvent;
import sysmlinjava.valuetypes.DurationMilliseconds;

/**
 * The {@code GPS} is a SysMLinJava model of the Global Positioning System (GPS)
 * that provides time messages to receiving systems. This is a highly simplified
 * executable model of the US Air Force's GPS for the purposes of simulating
 * receipt of GPS time messages.
 * <p>
 * The {@code GPS} consists of a single full port that represents the interface
 * with the GPS to receive time messages. It is configured to periodically send
 * a time message via the port to all connected ports to simulate the GPS time
 * signal. The time message is transmitted continuously upon invocation of the
 * start() operation and continues until invocation of the stop() operation.
 * <p>
 * Connectors with the {@code GPS}, i.e. connections to receive the GPS time
 * message, are made in other blocks that contain the {@code GPS} as a part.
 * 
 * @author ModelerOne
 *
 */
public class GPS extends SysMLBlock
{
	/**
	 * Port for transmitting GPS messages
	 */
	@FullPort
	public GPSMessagingTransmitProtocol GPSMessaging;

	/**
	 * Duration in seconds of the time until the first GPS message is to be
	 * transmitted indicating the first TDMA time slot begins.
	 */
	@Value
	DurationMilliseconds firstSlot;
	/**
	 * Duration in seconds of the period between GPS message to be transmitted
	 * indicated the start of the next TDMA time slot
	 */
	@Value
	DurationMilliseconds slotPeriod;
	/**
	 * Time event for the occurance of the next GPS message transmission time
	 */
	@TimeEvent
	SysMLTimeEvent timeEvent;

	/**
	 * ID of timer for transmit times
	 */
	public static String timerID = "GPSMessageTime";

	/**
	 * Constructor
	 */
	public GPS()
	{
		super("GPS", 0L);
	}

	/**
	 * Event handler for transmitting GPS messages at transmit times
	 * 
	 * @param time time of the message to be transmitted
	 */
	@Operation
	public void onGPSMessageTime(LocalTime time)
	{
		GPSMessaging.transmit(new GPSMessage(0, time));
	}

	@Override
	public void start()
	{
		super.start();
		stateMachine.get().startTimer(timeEvent);
	}

	@Override
	public void stop()
	{
		stateMachine.get().stopTimer(timerID);
		super.stop();
	}

	@Override
	protected void createStateMachine()
	{
		stateMachine = Optional.of(new GPSStateMachine(this));
	}

	@Override
	protected void createValues()
	{
		firstSlot = DurationMilliseconds.ofSeconds(10);
		slotPeriod = DurationMilliseconds.ofSeconds((int)GPSMessage.secondsPerSlot);
	}

	@Override
	protected void createFullPorts()
	{
		GPSMessaging = new GPSMessagingTransmitProtocol(this, 0L);
	}

	@Override
	protected void createEvents()
	{
		timeEvent = new SysMLTimeEvent(timerID, firstSlot, Optional.of(slotPeriod));
	}
}
