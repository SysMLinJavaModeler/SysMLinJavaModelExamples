package c4s2.systems.radar;

import java.util.Optional;
import c4s2.common.messages.RadarSystemMonitorMessage;
import c4s2.common.objects.information.RadarSignalReturn;
import c4s2.common.objects.information.RadarSignalTransmission;
import c4s2.common.objects.information.RadarSystemControl;
import c4s2.common.objects.information.RadarSystemMonitor;
import c4s2.common.ports.information.C4S2MessagingProtocol;
import c4s2.common.ports.information.RadarSignalReturnReceiveProtocol;
import c4s2.common.ports.information.RadarSignalTransmitProtocol;
import c4s2.common.valueTypes.RadarSystemStatesEnum;
import sysmlinjava.annotations.FullPort;
import sysmlinjava.annotations.Operation;
import sysmlinjava.annotations.Reception;
import sysmlinjava.annotations.Value;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.valuetypes.DirectionDegrees;
import sysmlinjava.valuetypes.DirectionRadians;
import sysmlinjava.valuetypes.DistanceMeters;
import sysmlinjava.valuetypes.DurationMilliseconds;
import sysmlinjava.valuetypes.FrequencyHertz;
import sysmlinjava.valuetypes.InstantMilliseconds;
import sysmlinjava.valuetypes.LatitudeDegrees;
import sysmlinjava.valuetypes.LongitudeDegrees;
import sysmlinjava.valuetypes.PointGeospatial;
import sysmlinjava.valuetypes.PowerWatts;
import sysmlinjava.valuetypes.ProbabilityPercent;

/**
 * The {@code RadarSystem} is a SysMLinJava model of a simple radar system that
 * transmits a radar signal over a scanning area and receives return
 * (reflection) signals from {@code RadarTarget}s identifying the targets
 * geoposition and signature (type).
 * <p>
 * The {@code RadarSystem} has a port for the messaging protocol used for
 * messaging with the {@code C4S2System} and ports for transmitting the radar
 * scanning signal and for receiving the radar signal return (reflection) from
 * the {@code RadarTarget}.
 * <p>
 * The radar system is stationary and is located at a position 1 mile south of
 * the center of its scanning area which is at lat/lon 42N, 88W. Its scanning
 * area is a radial section with left right half angles of 22.5 degrees for a
 * total section of 45 degrees. The scan section's half distance from the scan
 * center is 0.25 kilometers for a total scan area depth of 0.5 kilometres. The scan
 * signal is at a frequency of 2.7GHz and a power of 300 kW. These radar values
 * reflect a radar that scans relatively small areas and detects detailed target
 * signatures.
 * 
 * @author ModelerOne
 *
 */
public class RadarSystem extends SysMLBlock
{
	/**
	 * Port representin the messaging protocol used to communicate with the
	 * C4S2System
	 */
	@FullPort
	public C4S2MessagingProtocol messaging;
	/**
	 * Port representing the radar signal transmitter
	 */
	@FullPort
	public RadarSignalTransmitProtocol radarSignalTransmitter;
	/**
	 * Port representing the radar signal return (reflection) receiver
	 */
	@FullPort
	public RadarSignalReturnReceiveProtocol radarSignalReturnReceiver;

	/**
	 * Value for the half distance of the scan area radial section
	 */
	@Value
	public DistanceMeters scanHalfDistance;
	/**
	 * Value for the geoposition of the radar system (derived from above values)
	 */
	@Value
	public PointGeospatial systemPosition;
	/**
	 * Value for the geopostion of the scan center (derived from above values)
	 */
	@Value
	public PointGeospatial scanCenterPosition;
	/**
	 * Value for the scan half angle in radians (derived from above values)
	 */
	@Value
	public DirectionRadians scanHalfAngle;
	/**
	 * Value for the radar signal frequency
	 */
	@Value
	public FrequencyHertz scanFrequencyHertz;
	/**
	 * Value for the radar signal power
	 */
	@Value
	public PowerWatts scanPowerWatts;
	/**
	 * Value for the time interval between scans during F2T2 operations
	 */
	@Value
	public DurationMilliseconds f2t2ScanIntervalDuration;
	/**
	 * Value for the time interval between scans during EA operations
	 */
	@Value
	public DurationMilliseconds eaScanIntervalDuration;

	/**
	 * Value for the current state of the radar system
	 */
	@Value
	public RadarSystemStatesEnum currentState;
	/**
	 * Value for the current monitoring data for the radar system
	 */
	@Value
	public RadarSystemMonitor currentMonitor;
	/**
	 * Value for the current control data for the radar system
	 */
	@Value
	public RadarSystemControl currentControl;
	/**
	 * Value for the current radar signal transmission
	 */
	@Value
	public Optional<RadarSignalTransmission> currentTransmission;
	/**
	 * Value for the current/lastest radar signal return
	 */
	@Value
	public Optional<RadarSignalReturn> currentReturn;
	/**
	 * Value for the confidence level in data from the current return
	 */
	@Value
	public Optional<ProbabilityPercent> confidence;

	/**
	 * Name of the timer for scans during F2T2 operations
	 */
	public final static String f2t2ScanTimerID = "F2T2ScanTime";
	/**
	 * Name of the timer for scans during EA operations
	 */
	public final static String eaScanTimerID = "EAScanTime";

	/**
	 * Constructor
	 */
	public RadarSystem()
	{
		super();
	}

	/**
	 * Initializes the radar system by setting the state, and configuring a default
	 * transmission, control, and monitor data
	 */
	@Operation
	public void initialize()
	{
		logger.info("intializing...");
		currentState = RadarSystemStatesEnum.Initializing;
		currentTransmission = Optional.of(new RadarSignalTransmission(systemPosition, scanCenterPosition, scanHalfDistance, scanHalfAngle, scanFrequencyHertz, scanPowerWatts, InstantMilliseconds.now()));
		currentControl = new RadarSystemControl(currentState, currentTransmission, InstantMilliseconds.now());
		currentMonitor = new RadarSystemMonitor(currentState, currentTransmission, currentReturn, confidence, InstantMilliseconds.now());
	}

	/**
	 * Reacts to receipt of a control to transition to Idle state. Sets state to
	 * Idle, set the transmission IAW the control, and sets the monitor data to be
	 * IAW the control data before publishing the monitor via the messaging
	 * protocol.
	 * 
	 * @param control received control data for the system
	 */
	@Reception
	public void onControlToIdle(RadarSystemControl control)
	{
		logger.info(control.toString());
		currentControl.state = RadarSystemStatesEnum.Idle;
		currentControl.transmission = control.transmission;
		currentMonitor.currentState = RadarSystemStatesEnum.Idle;
		currentMonitor.radarSignalTransmission = currentControl.transmission;
		messaging.transmit(new RadarSystemMonitorMessage(new RadarSystemMonitor(currentMonitor)));
	}

	/**
	 * Reacts to receipt of a control to transition to F2T2 scanning state. Sets
	 * state to F2T2 scanning, set the transmission IAW the control, and sets the
	 * monitor data to be IAW the control data before publishing the monitor via the
	 * messaging protocol.
	 * 
	 * @param control received control data for the system
	 */
	@Reception
	public void onControlToF2T2Scanning(RadarSystemControl control)
	{
		logger.info(control.toString());
		currentControl.state = RadarSystemStatesEnum.F2T2Scanning;
		currentMonitor.currentState = RadarSystemStatesEnum.F2T2Scanning;
		if (control.transmission.isPresent())
		{
			currentControl.transmission = Optional.of(control.transmission.get());
			RadarSignalTransmission transmission = control.transmission.get();
			scanCenterPosition.setValue(transmission.scanCenterPosition);
			systemPosition.setValue(transmission.systemPosition);
			scanHalfDistance.setValue(transmission.scanHalfDistance.value);
			scanHalfAngle.setValue(transmission.scanHalfAngle.value);
			scanFrequencyHertz.setValue(transmission.scanFrequency.value);
			scanPowerWatts.setValue(transmission.scanPower.value);
			currentMonitor.radarSignalTransmission = Optional.of(control.transmission.get());
		}
		messaging.transmit(new RadarSystemMonitorMessage(new RadarSystemMonitor(currentMonitor)));
	}

	/**
	 * Starts the F2T2 scanning by setting the current radar transmissions to be IAW
	 * with F2T2 values, and then simply starting the timer that invokes each scan.
	 */
	@Operation
	public void startF2T2Scanning()
	{
		currentTransmission = Optional.of(new RadarSignalTransmission(systemPosition, scanCenterPosition, scanHalfDistance, scanHalfAngle, scanFrequencyHertz, scanPowerWatts, InstantMilliseconds.now()));
		currentReturn = Optional.empty();
	}

	/**
	 * Performs the next F2T2 scan by transmitting the radar signal via the radar
	 * signal transmitter
	 */
	@Operation
	public void performNextF2T2Scan()
	{
		currentTransmission.get().scanStartTime = InstantMilliseconds.now();
		radarSignalTransmitter.transmit(currentTransmission.get());
	}

	/**
	 * Reacts to the receipt of a radar signal return (reflection) by saving the
	 * return signal, storing it in the current monitor data, and publishing the
	 * monitor via the messaging protocol.
	 * 
	 * @param radarSignalReturn the current radar signal return
	 */
	@Reception
	public void onF2T2RadarSignalReturn(RadarSignalReturn radarSignalReturn)
	{
		logger.info(radarSignalReturn.toString());
		currentReturn = Optional.of(radarSignalReturn);
		currentMonitor.radarSignalReturn = currentReturn;
		messaging.transmit(new RadarSystemMonitorMessage(new RadarSystemMonitor(currentMonitor)));
	}

	/**
	 * Stops the F2T3 scanning (currently no operation).
	 */
	@Operation
	public void stopF2T2Scanning()
	{
	}

	/**
	 * Reacts to receipt of a control to transition to EA scanning state. Sets state
	 * to EA scanning, set the transmission IAW the control, and sets the monitor
	 * data to be IAW the control data before publishing the monitor via the
	 * messaging protocol.
	 * 
	 * @param control received control data for the system
	 */
	@Reception
	public void onControlToEAScanning(RadarSystemControl control)
	{
		logger.info(control.toString());
		currentControl.state = RadarSystemStatesEnum.EAScanning;
		currentMonitor.currentState = RadarSystemStatesEnum.EAScanning;
		if (currentControl.transmission.isPresent())
		{
			currentControl.transmission = Optional.of(control.transmission.get());
			RadarSignalTransmission transmission = control.transmission.get();
			scanCenterPosition.setValue(transmission.scanCenterPosition);
			systemPosition.setValue(transmission.systemPosition);
			scanHalfDistance.setValue(transmission.scanHalfDistance.value);
			scanHalfAngle.setValue(transmission.scanHalfAngle.value);
			scanFrequencyHertz.setValue(transmission.scanFrequency.value);
			scanPowerWatts.setValue(transmission.scanPower.value);
			currentMonitor.radarSignalTransmission = Optional.of(control.transmission.get());
		}
		messaging.transmit(new RadarSystemMonitorMessage(new RadarSystemMonitor(currentMonitor)));
	}

	/**
	 * Starts the EA scanning by setting the current radar transmissions to be IAW
	 * with EA values, and then simply starting the timer that invokes each scan.
	 */
	@Operation
	public void startEAScanning()
	{
		currentTransmission = Optional.of(new RadarSignalTransmission(systemPosition, scanCenterPosition, scanHalfDistance, scanHalfAngle, scanFrequencyHertz, scanPowerWatts, InstantMilliseconds.now()));
		currentReturn = Optional.empty();
	}

	/**
	 * Performs the next EA scan by transmitting the radar signal via the radar
	 * signal transmitter
	 */
	@Operation
	public void performNextEAScan()
	{
		currentTransmission.get().scanStartTime = InstantMilliseconds.now();
		radarSignalTransmitter.transmit(currentTransmission.get());
	}

	/**
	 * Reacts to the receipt of a radar signal return (reflection) by saving the
	 * return signal, storing it in the current monitor data, and publishing the
	 * monitor via the messaging protocol.
	 * 
	 * @param radarSignalReturn the current radar signal return
	 */
	@Reception
	public void onEARadarSignalReturn(RadarSignalReturn radarSignalReturn)
	{
		logger.info(radarSignalReturn.toString());
		currentReturn = Optional.of(radarSignalReturn);
		currentMonitor.radarSignalReturn = currentReturn;
		messaging.transmit(new RadarSystemMonitorMessage(new RadarSystemMonitor(currentMonitor)));
	}

	/**
	 * Stops the EA scanning (currently empty operation).
	 */
	@Operation
	public void stopEAScanning()
	{
	}

	/**
	 * Reacts to receipt of a control to transition to Detached state. It first
	 * sends a "zero-power" transmission to the target vehicle to indicate the
	 * simulation is completed. It then sets the system state to Detached, sets the
	 * transmission IAW the control, and sets the monitor data to be essentially
	 * empty IAW being detached before publishing the monitor via the messaging
	 * protocol.
	 * 
	 * @param control received control data for the system
	 */
	@Reception
	public void onControlToDetach(RadarSystemControl control)
	{
		logger.info(control.toString());
		currentTransmission.get().scanPower.value = 0;
		radarSignalTransmitter.transmit(currentTransmission.get());
		currentControl.state = control.state;
		currentControl.transmission = control.transmission;
		currentControl.time = control.time;
		currentMonitor.currentState = control.state;
		currentMonitor.radarSignalTransmission = Optional.empty();
		currentMonitor.radarSignalReturn = Optional.empty();
		currentMonitor.confidence = Optional.empty();
		currentMonitor.time = InstantMilliseconds.now();
		messaging.transmit(new RadarSystemMonitorMessage(new RadarSystemMonitor(currentMonitor)));
	}

	@Override
	protected void createStateMachine()
	{
		this.stateMachine = Optional.of(new RadarSystemStateMachine(this));
	}

	@Override
	protected void createValues()
	{
		scanCenterPosition = new PointGeospatial(new LatitudeDegrees(45.0), new LongitudeDegrees(105.0));
		systemPosition = scanCenterPosition.movedTo(DirectionRadians.south, new DistanceMeters(1000));
		scanHalfDistance = new DistanceMeters(250);
		scanHalfAngle = new DirectionRadians(new DirectionDegrees(22.5));
		scanFrequencyHertz = new FrequencyHertz(2_700_000_000.0);
		scanPowerWatts = new PowerWatts(300_000.0);
		f2t2ScanIntervalDuration = new DurationMilliseconds(1500);
		eaScanIntervalDuration = new DurationMilliseconds(1500);
		currentState = RadarSystemStatesEnum.Initial;
		currentTransmission = Optional.empty();
		currentReturn = Optional.empty();
		confidence = Optional.empty();
		
		InstantMilliseconds now = InstantMilliseconds.now();
		currentControl = new RadarSystemControl(currentState, currentTransmission, now);
		currentMonitor = new RadarSystemMonitor(currentState, currentTransmission, currentReturn, confidence, now);
	}

	@Override
	protected void createFullPorts()
	{
		super.createFullPorts();
		messaging = new C4S2MessagingProtocol(this, 0L, "RadarSystemMessaging");
		radarSignalReturnReceiver = new RadarSignalReturnReceiveProtocol(this, 0L);
		radarSignalTransmitter = new RadarSignalTransmitProtocol(this, 0L);
	}
}
