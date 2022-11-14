package c4s2.users;

import java.util.Optional;
import c4s2.common.events.C4S2OperatorControlEvent;
import c4s2.common.objects.information.OperatorControl;
import c4s2.common.objects.information.OperatorRadarControlView;
import c4s2.common.objects.information.OperatorRadarMonitorView;
import c4s2.common.objects.information.OperatorStrikeControlView;
import c4s2.common.objects.information.OperatorStrikeMonitorView;
import c4s2.common.objects.information.OperatorSystemControlView;
import c4s2.common.objects.information.OperatorSystemMonitorView;
import c4s2.common.objects.information.OperatorTargetControlView;
import c4s2.common.objects.information.OperatorTargetMonitorView;
import c4s2.common.objects.information.RadarControl;
import c4s2.common.objects.information.RadarMonitor;
import c4s2.common.objects.information.RadarSignalTransmission;
import c4s2.common.objects.information.StrikeControl;
import c4s2.common.objects.information.StrikeMonitor;
import c4s2.common.objects.information.C4S2SystemControl;
import c4s2.common.objects.information.SystemMonitor;
import c4s2.common.objects.information.TargetControl;
import c4s2.common.objects.information.TargetMonitor;
import c4s2.common.ports.information.OperatorControlTransmitProtocol;
import c4s2.common.ports.information.OperatorMonitorViewProtocol;
import c4s2.common.valueTypes.C4S2OperatorServicesComputerStatesEnum;
import c4s2.common.valueTypes.C4S2OperatorStatesEnum;
import c4s2.common.valueTypes.C4S2ServicesComputerStatesEnum;
import c4s2.common.valueTypes.RadarReturnSignatureEnum;
import c4s2.common.valueTypes.RadarSystemStatesEnum;
import c4s2.common.valueTypes.ServiceStatesEnum;
import c4s2.common.valueTypes.StrikeSystemStatesEnum;
import c4s2.common.valueTypes.TargetDevelopmentAlgorithmsEnum;
import sysmlinjava.annotations.FullPort;
import sysmlinjava.annotations.Operation;
import sysmlinjava.annotations.Part;
import sysmlinjava.annotations.Reception;
import sysmlinjava.annotations.Value;
import sysmlinjava.annotations.comments.Problem;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.comments.SysMLProblem;
import sysmlinjava.statemachine.FinalEvent;
import sysmlinjava.valuetypes.BBoolean;
import static sysmlinjava.valuetypes.BBoolean.*;
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
import sysmlinjavalibrary.common.objects.information.OnOffSwitch;
import sysmlinjavalibrary.common.ports.energy.mechanical.MechanicalOnOffSwitchContact;
import sysmlinjavalibrary.components.communications.common.objects.EthernetSwitchIPRouterStatesEnum;
import sysmlinjavalibrary.components.communications.common.objects.SIPRNetRouterStatesEnum;

/**
 * SysMLinJava model and simulation of the operator of the
 * Command/Control/Surveillance/Strike (C4S2) System. The operator could be
 * human, automated, or some combination of both. Its behavior is assumed to be
 * in accordance with the operator's state machine as specified by the
 * {@code C4S2OperatorStateMachine}.
 * <p>
 * The {@code C4S2Operator} uses a set of views of information (the user
 * interface) to monitor and control the {@code C4S2System} and it's attached
 * systems - the {@code RadarSystem} and the {@code StrikeSystem}. Together, the
 * {@code C4S2Operator}, {@code C4S2System}, {@code RadarSystem}, and
 * {@code StrikeSystem} can execute the standard military F2T2EA operation, i.e
 * to find, fix, track, target, engage, and assess a target.
 * <p>
 * The {@code C4S2Operator} includes a port for receiving the monitoring
 * information (radar, target, strike, and system monitor views) and a port for
 * submitting the control information (radar, target, strike, and system control
 * views). Each of the monitor and control views are realized as parts of the
 * block. It also has ports for the power on/off switches for the components of
 * the {@code C4S2System}.
 * <p>
 * Values of the {@code C4S2Operator} include details about the radar
 * surveillance, i.e. the scanning area, power, and frequency of the radar
 * signal; and other details about confidence in the target detection, and
 * system configuration information.
 * <p>
 * The {@code C4S2Operator} first initializes (powers on) the systems and then
 * configures the {@code C4S2System}, {@code RadarSystem}, and
 * {@code StrikeSytem} to perform the F2T2EA operation. After configuration
 * completes, the {@code RadarSystem} is used to F2T2 the target. Once the
 * target is F2T2'd, the {@code StrikeSystem} is sent to engage (E) the target.
 * After engagment is completed, the {@code RadarSystem} is set to assess (A)
 * that the target has been destroyed. After the assessment, the
 * {@code C4S2Operator} finalizes the systems by detaching the radar and strike
 * systems and powering down the {@code C4S2System}. The {@code C4S2Operator}
 * then terminates itself thereby ending the simulation.
 * 
 * @author ModelerOne
 *
 */
public class C4S2Operator extends SysMLBlock
{
	/**
	 * Port representing the input of all views of monitoring information
	 */
	@FullPort
	public OperatorMonitorViewProtocol monitorViewPort;
	/**
	 * Port representing the output of all views of controling information
	 */
	@FullPort
	public OperatorControlTransmitProtocol controlViewPort;
	/**
	 * Port representing the power on/off switch for the ethernet switch/IP router
	 */
	@FullPort
	public MechanicalOnOffSwitchContact onOffSwitchEthernetSwitchIPRouter;
	/**
	 * Port representing the power on/off switch for the SIPRNet router
	 */
	@FullPort
	public MechanicalOnOffSwitchContact onOffSwitchSIPRNetRouter;
	/**
	 * Port representing the power on/off switch for C4S2 services computer
	 */
	@FullPort
	public MechanicalOnOffSwitchContact onOffSwitchC4S2ServicesComputer;
	/**
	 * Port representing the power on/off switch for C4S2 operator services computer
	 */
	@FullPort
	public MechanicalOnOffSwitchContact onOffSwitchOperatorServicesComputer;

	/**
	 * Part representing the view of monitoring information for the C4S2 system
	 */
	@Part
	public OperatorSystemMonitorView systemMonitorView;
	/**
	 * Part representing the view of monitoring information for the radar system
	 */
	@Part
	public OperatorRadarMonitorView radarMonitorView;
	/**
	 * Part representing the view of monitoring information for the targeting system
	 * (the target service)
	 */
	@Part
	public OperatorTargetMonitorView targetMonitorView;
	/**
	 * Part representing the view of monitoring information for the strike system
	 */
	@Part
	public OperatorStrikeMonitorView strikeMonitorView;
	/**
	 * Part representing the view of controling information for the C4S2 system
	 */
	@Part
	public OperatorSystemControlView systemControlView;
	/**
	 * Part representing the view of controling information for the radar system
	 */
	@Part
	public OperatorRadarControlView radarControlView;
	/**
	 * Part representing the view of controling information for the targting system
	 * (target service)
	 */
	@Part
	public OperatorTargetControlView targetControlView;
	/**
	 * Part representing the view of controling information for the strike system
	 */
	@Part
	public OperatorStrikeControlView strikeControlView;

	/**
	 * Value for the geoposition of the radar system (derived from above values)
	 */
	@Value
	public PointGeospatial radarSystemPosition;
	/**
	 * Value for the geopostion of the scan center (derived from above values)
	 */
	@Value
	public PointGeospatial scanCenterPosition;
	/**
	 * Value for the half distance of the scan area radial section
	 */
	@Value
	public DistanceMeters scanHalfDistance;
	/**
	 * Value for the scan half angle in radians (derived from above values)
	 */
	@Value
	public DirectionRadians scanHalfAngle;

	/**
	 * Frequency of the radar scanning signal
	 */
	@Value
	public FrequencyHertz scanFrequencyHertz;
	/**
	 * Power of the radar scanning signal
	 */
	@Value
	public PowerWatts scanPowerWatts;
	/**
	 * Time of the last scan
	 */
	@Value
	public InstantMilliseconds scanTime;
	/**
	 * Minimum confidence value for the target to be considered "found"
	 */
	@Value
	public ProbabilityPercent minConfidenceTargetFound;
	/**
	 * Minimum confidence value for the target to be considered "tracked"
	 */
	@Value
	public ProbabilityPercent minConfidenceTargetTracked;
	/**
	 * Time at which the configuration of all systems was started
	 */
	@Value
	public InstantMilliseconds configurationStartTime;
	/**
	 * Maximum time in which all systems should be configured
	 */
	@Value
	public DurationMilliseconds maxConfigurationDuration;
	/**
	 * Indication that the C4S2 system is ready for the next state of operation
	 */
	@Value
	public BBoolean systemGo;
	/**
	 * Indication that the radar system is ready for the next state of operation
	 */
	@Value
	public BBoolean radarGo;
	/**
	 * Indication that the strike system is ready for the next state of operation
	 */
	@Value
	public BBoolean strikeGo;
	/**
	 * Indication that the targeting system is ready for the next state of operation
	 */
	@Value
	public BBoolean targetGo;

	/**
	 * Problem comment for limitation of only single attempt to configure all
	 * systems
	 */
	@Problem
	public SysMLProblem singleConfigurationAttempt;

	public C4S2Operator()
	{
		super();
	}

	/**
	 * Initialize all systems by powering them on. If/when all are ready, go to next
	 * state - configuring
	 */
	@Operation
	void initialize()
	{
		logger.info("initializing...");
		powerOnSystems();
		delay(4.0);
		gotoNextState(C4S2OperatorStatesEnum.Configuring);
	}

	/**
	 * Configures all systems by setting and sending them controling information.
	 * Puts the CC4S2 system into operational mode, puts the radar into idle mode,
	 * puts the strike system into standby mode, and commands the targeting system
	 * to use the "simple" tracking algorithm. Also sets a timer to max
	 * configuration time in case of failure.
	 */
	@Operation
	void configure()
	{
		logger.info("configuring...");
		configureSystem();
		configureRadar();
		configureStrike();
		configureTarget();
		configurationStartTime = InstantMilliseconds.now();
		((C4S2OperatorStateMachine)stateMachine.get()).startConfigurationTimer(maxConfigurationDuration);
	}

	/**
	 * Invokes the find/fix/track/target (F2T2) operation by putting the radar into
	 * F2T2 scanning mode.
	 */
	@Operation
	void findFixTrackTarget()
	{
		logger.info("findFixTrackTargeting...");
		radarGo = targetGo = False;
		radarControlView.control.toF2T2ing();
		controlViewPort.transmit(new OperatorRadarControlView(radarControlView));
	}

	/**
	 * Invokes the engage (E) operation by putting the strike system into striking
	 * mode.
	 */
	@Operation
	void engage()
	{
		logger.info("engaging...");
		strikeGo = False;
		strikeControlView.control.toEngaging();
		controlViewPort.transmit(strikeControlView.copy());
	}

	/**
	 * Invokes the assess (A) operation by returning the strike system to base,
	 * putting the targeting system into complex mode, and putting radar into EA
	 * scanning mode.
	 */
	@Operation
	void assess()
	{
		logger.info("assessing...");
		radarGo = strikeGo = targetGo = False;

		strikeControlView.control.toAssessing();
		controlViewPort.transmit(strikeControlView.copy());

		targetControlView.control.toAssessing();
		controlViewPort.transmit(new OperatorTargetControlView(targetControlView));

		radarControlView.control.toAssessing();
		controlViewPort.transmit(new OperatorRadarControlView(radarControlView));
	}

	/**
	 * Invokes finalization of all systems by detaching the radar and strike systems
	 * from this operation, stops the targeting system, and, after these systems are
	 * finalized, powering off the C4S2 system.
	 */
	@Operation
	public void finalize()
	{
		logger.info("finalizing...");
		systemGo = radarGo = strikeGo = targetGo = False;
		finalizeRadar();
		finalizeStrike();
		finalizeTarget();
		delay(5);
		finalizeSystem();
	}

	/**
	 * Reacts to update of the view of C4S2 system monitoring information during
	 * configuration mode by checking if the system is in accordance with it current
	 * configuration control and, if so, checking to see if all other systems are
	 * configured correctly and, if so, invokes transition to
	 * finding/fixing/tracking/targeting mode.
	 * 
	 * @param monitorView view of the system monitoring information
	 */
	@Reception
	public void onSystemMonitorViewDuringConfiguration(OperatorSystemMonitorView monitorView)
	{
		logger.info(monitorView.toString());
		systemMonitorView = monitorView;
		if (systemMonitorView.monitor.isIAW(systemControlView.control))
		{
			systemGo = True;
			if (allSystemsGo())
				gotoNextState(C4S2OperatorStatesEnum.FindingFixingTrackingTargeting);
		}
		else
		{
			systemGo = False;
			logger.warning("system monitor not (yet) IAW control" + systemMonitorView.monitor.toString());
		}
	}

	/**
	 * Reacts to update of the view of radar system monitoring information during
	 * configuration mode by checking if the system is in accordance with it current
	 * configuration control and, if so, checking to see if all other systems are
	 * configured correctly and, if so, invokes transition to
	 * finding/fixing/tracking/targeting mode.
	 * 
	 * @param monitorView view of the radar system monitoring information
	 */
	@Reception
	public void onRadarMonitorViewDuringConfiguration(OperatorRadarMonitorView monitorView)
	{
		logger.info(monitorView.toString());
		radarMonitorView = monitorView;
		if (radarMonitorView.monitor.isIAW(radarControlView.control))
		{
			radarGo = True;
			if (allSystemsGo())
				gotoNextState(C4S2OperatorStatesEnum.FindingFixingTrackingTargeting);
		}
		else
		{
			radarGo = False;
			logger.warning("radar monitor not IAW control" + radarControlView.control.toString());
		}
	}

	/**
	 * Reacts to update of the view of strike system monitoring information during
	 * configuration mode by checking if the system is in accordance with it current
	 * configuration control and, if so, checking to see if all other systems are
	 * configured correctly and, if so, invokes transition to
	 * finding/fixing/tracking/targeting mode.
	 * 
	 * @param monitorView view of the strike system monitoring information
	 */
	@Reception
	public void onStrikeMonitorViewDuringConfiguration(OperatorStrikeMonitorView monitorView)
	{
		logger.info(monitorView.toString());
		strikeMonitorView = monitorView;
		if (strikeMonitorView.monitor.isIAW(strikeControlView.control))
		{
			strikeGo = True;
			if (allSystemsGo())
				gotoNextState(C4S2OperatorStatesEnum.FindingFixingTrackingTargeting);
		}
		else
		{
			strikeGo = False;
			logger.warning("strike monitor not IAW control" + strikeControlView.control.toString());
		}

	}

	/**
	 * Reacts to update of the view of targeting system monitoring information
	 * during configuration mode by checking if the system is in accordance with it
	 * current configuration control and, if so, checking to see if all other
	 * systems are configured correctly and, if so, invokes transition to
	 * finding/fixing/tracking/targeting mode.
	 * 
	 * @param monitorView view of the targeting system monitoring information
	 */
	@Reception
	public void onTargetMonitorViewDuringConfiguration(OperatorTargetMonitorView monitorView)
	{
		logger.info(monitorView.toString());
		targetMonitorView = monitorView;
		if (targetMonitorView.monitor.isIAW(targetControlView.control))
		{
			targetGo = True;
			if (allSystemsGo())
				gotoNextState(C4S2OperatorStatesEnum.FindingFixingTrackingTargeting);
		}
		else
		{
			targetGo = False;
			logger.warning("target monitor not IAW control" + targetControlView.control.toString());
		}
	}

	/**
	 * Reacts to the occurence of the max configuration time without all systems
	 * being correctly configured. Currently, this behavior is limited to log
	 * notification only.
	 */
	@Reception
	public void onMaxConfigurationTime()
	{
		logger.severe(String.format("Systems configuration timed out: systemGo=%s, radarGo=%s, strikeGo=%s, targetGo=%s", systemGo, radarGo, strikeGo, targetGo));
	}

	/**
	 * Reacts to update of the view of C4S2 system monitoring information during
	 * F2T2 mode by checking if the system is in accordance with it current
	 * configuration control and, if so, indicating such amongst the systems to be
	 * ready. before checking to see if all other
	 * 
	 * @param monitorView view of the C4S2 system monitoring information
	 */
	@Reception
	public void onSystemMonitorViewDuringF2T2ing(OperatorSystemMonitorView monitorView)
	{
		logger.info(monitorView.toString());
		systemMonitorView = monitorView;
		if (systemMonitorView.monitor.isIAW(systemControlView.control))
			systemGo = True;
		else
		{
			systemGo = False;
			logger.warning("system monitor not IAW control" + systemControlView.control.toString());
		}
	}

	/**
	 * Reacts to update of the view of radar system monitoring information during
	 * F2T2 mode by checking if the system is in accordance with it current
	 * configuration control and, if so, checking to see if all other systems are
	 * configured/operating correctly and, if so, invokes transition to engaging
	 * mode.
	 * 
	 * @param monitorView view of the radar system monitoring information
	 */
	@Reception
	public void onRadarMonitorViewDuringF2T2ing(OperatorRadarMonitorView monitorView)
	{
		logger.info(monitorView.toString());
		radarMonitorView = monitorView;
		if (radarMonitorView.monitor.isIAW(radarControlView.control))
		{
			radarGo = True;
			if (allSystemsGo())
				gotoNextState(C4S2OperatorStatesEnum.Engaging);
		}
		else
		{
			radarGo = False;
			logger.warning("radar monitor not IAW control" + radarControlView.control.toString());
		}
	}

	/**
	 * Reacts to update of the view of strike system monitoring information during
	 * F2T2 mode by checking if the system is in accordance with it current
	 * configuration control and, if so, checking to see if all other systems are
	 * configured/operating correctly and, if so, indicating such amongst the
	 * systems to be ready.
	 * 
	 * @param monitorView view of the strike system monitoring information
	 */
	@Reception
	public void onStrikeMonitorViewDuringF2T2ing(OperatorStrikeMonitorView monitorView)
	{
		logger.info(monitorView.toString());
		strikeMonitorView = monitorView;
		if (strikeMonitorView.monitor.isIAW(strikeControlView.control))
			strikeGo = True;
		else
		{
			strikeGo = False;
			logger.warning("strike monitor not IAW control" + radarControlView.control.toString());
		}
	}

	@Reception
	public void onTargetMonitorViewDuringF2T2ing(OperatorTargetMonitorView monitorView)
	{
		logger.info(monitorView.toString());
		targetMonitorView = monitorView;
		if (targetMonitorView.monitor.isIAW(targetControlView.control))
		{
			if (targetMonitorView.monitor.signature.equals(RadarReturnSignatureEnum.vehicleLargeArmored))
			{
				targetGo = True;
				if (allSystemsGo())
					gotoNextState(C4S2OperatorStatesEnum.Engaging);
			}
		}
		else
		{
			targetGo = False;
			logger.warning("target monitor not IAW control" + targetControlView.control.toString());
		}
	}

	@Reception
	public void onSystemMonitorViewDuringEngaging(OperatorSystemMonitorView monitorView)
	{
		logger.info(monitorView.toString());
		systemMonitorView = monitorView;
		if (systemMonitorView.monitor.isIAW(systemControlView.control))
			systemGo = True;
		else
		{
			systemGo = False;
			logger.warning("system monitor not IAW control" + systemControlView.control.toString());
		}
	}

	@Reception
	public void onRadarMonitorViewDuringEngaging(OperatorRadarMonitorView monitorView)
	{
		logger.info(monitorView.toString());
		radarMonitorView = monitorView;
		if (radarMonitorView.monitor.isIAW(radarControlView.control))
			radarGo = True;
		else
		{
			radarGo = False;
			logger.warning("radar monitor not IAW control" + radarControlView.control.toString());
		}
	}

	@Reception
	public void onStrikeMonitorViewDuringEngaging(OperatorStrikeMonitorView monitorView)
	{
		logger.info(monitorView.toString());
		strikeMonitorView = monitorView;
		if (strikeMonitorView.monitor.isIAW(strikeControlView.control))
		{
			if (strikeMonitorView.monitor.confirmTargetEngaged.isPresent() && strikeMonitorView.monitor.confirmTargetEngaged.get().isTrue())
			{
				strikeGo = True;
				if (allSystemsGo())
					gotoNextState(C4S2OperatorStatesEnum.Assessing);
			}
		}
		else
		{
			strikeGo = False;
			logger.warning("strike monitor not IAW control" + strikeControlView.control.toString());
		}
	}

	@Reception
	public void onTargetMonitorViewDuringEngaging(OperatorTargetMonitorView monitorView)
	{
		logger.info(monitorView.toString());
		targetMonitorView = monitorView;
		if (targetMonitorView.monitor.isIAW(targetControlView.control))
			targetGo = True;
		else
		{
			targetGo = False;
			logger.warning("target monitor not IAW control" + targetControlView.control.toString());
		}
	}

	@Reception
	public void onSystemMonitorViewDuringAssessing(OperatorSystemMonitorView monitorView)
	{
		logger.info(monitorView.toString());
		systemMonitorView = monitorView;
		if (systemMonitorView.monitor.isIAW(systemControlView.control))
		{
			systemGo = True;
			if (allSystemsGo())
				gotoNextState(C4S2OperatorStatesEnum.Finalizing);
		}
		else
			systemGo = False;
	}

	@Reception
	public void onRadarMonitorViewDuringAssessing(OperatorRadarMonitorView monitorView)
	{
		logger.info(monitorView.toString());
		radarMonitorView = monitorView;
		if (radarMonitorView.monitor.isIAW(radarControlView.control))
		{
			if (radarAssessed())
				radarGo = True;
			if (allSystemsGo())
				gotoNextState(C4S2OperatorStatesEnum.Finalizing);
		}
		else
			radarGo = False;
	}

	@Reception
	public void onStrikeMonitorViewDuringAssessing(OperatorStrikeMonitorView monitorView)
	{
		logger.info(monitorView.toString());
		strikeMonitorView = monitorView;
		if (strikeReturned())
		{
			strikeGo = True;
			if (allSystemsGo())
				gotoNextState(C4S2OperatorStatesEnum.Finalizing);
		}
		else
			strikeGo = False;
	}

	@Reception
	public void onTargetMonitorViewDuringAssessing(OperatorTargetMonitorView monitorView)
	{
		logger.info(monitorView.toString());
		targetMonitorView = monitorView;
		if (targetMonitorView.monitor.isIAW(targetControlView.control))
		{
			if (targetAssessed())
				targetGo = True;
			if (allSystemsGo())
				gotoNextState(C4S2OperatorStatesEnum.Finalizing);
		}
		else
			targetGo = False;
	}

	@Reception
	public void onSystemMonitorViewDuringFinalizing(OperatorSystemMonitorView monitorView)
	{
		logger.info(monitorView.toString());
		systemMonitorView = monitorView;
		if (systemMonitorView.monitor.isIAW(systemControlView.control))
		{
			if (allSystemsGo())
				gotoNextState(C4S2OperatorStatesEnum.Final);
		}
		else
			systemGo = False;
	}

	@Reception
	public void onRadarMonitorViewDuringFinalizing(OperatorRadarMonitorView monitorView)
	{
		logger.info(monitorView.toString());
		radarMonitorView = monitorView;
		if (radarMonitorView.monitor.isIAW(radarControlView.control))
		{
			if (allSystemsGo())
				gotoNextState(C4S2OperatorStatesEnum.Final);
		}
		else
			radarGo = False;
	}

	@Reception
	public void onStrikeMonitorViewDuringFinalizing(OperatorStrikeMonitorView monitorView)
	{
		logger.info(monitorView.toString());
		strikeMonitorView = monitorView;
		if (strikeMonitorView.monitor.isIAW(strikeControlView.control))
		{
			if (allSystemsGo())
				gotoNextState(C4S2OperatorStatesEnum.Final);
		}
		else
		{
			strikeGo = False;
		}
	}

	@Reception
	public void onTargetMonitorViewDuringFinalizing(OperatorTargetMonitorView monitorView)
	{
		logger.info(monitorView.toString());
		targetMonitorView = monitorView;
		if (targetMonitorView.monitor.isIAW(targetControlView.control))
		{
			if (allSystemsGo())
				gotoNextState(C4S2OperatorStatesEnum.Final);
		}
		else
			targetGo = False;
	}

	/**
	 * Submits an event to the state machine to transition to the next state of the
	 * operator.
	 * 
	 * @param nextState next state to transition to
	 */
	@Operation
	private void gotoNextState(C4S2OperatorStatesEnum nextState)
	{
		if (nextState != C4S2OperatorStatesEnum.Final)
			acceptEvent(new C4S2OperatorControlEvent(new OperatorControl(nextState)));
		else
			acceptEvent(new FinalEvent());
	}

	/**
	 * Returns true if all systems are a "go", i.e are ready to perform the next
	 * activities of the current F2T2EA operation.
	 * 
	 * @return true if all systems are ready to perform next activity, false
	 *         otherwise.
	 */
	@Operation
	private boolean allSystemsGo()
	{
		return systemGo.isTrue() && radarGo.isTrue() && strikeGo.isTrue() && targetGo.isTrue();
	}

	/**
	 * Configures the C4S2System by setting the system control view with the states
	 * of each of the systems and components to "operational" and submitting the
	 * controls.
	 */
	@Operation
	private void configureSystem()
	{
		systemControlView.control.toConfigured();
		controlViewPort.transmit(systemControlView.copy());
	}

	/**
	 * Configures the RadarSystem by setting the radar control view to specify 1)
	 * the radar's geospatial position, 2) the radar signal to be used for scanning,
	 * 3) the radar's state as "idle" (ready), and then submitting the control.
	 */
	@Operation
	private void configureRadar()
	{
		RadarSignalTransmission signalTransmission = new RadarSignalTransmission(radarSystemPosition, scanCenterPosition, scanHalfDistance, scanHalfAngle, scanFrequencyHertz, scanPowerWatts, scanTime);
		radarControlView.control.toConfigured(signalTransmission);
		controlViewPort.transmit(new OperatorRadarControlView(radarControlView));
	}

	/**
	 * Configures the targetting software by setting the target control view to
	 * specify use of the "simple" algorithm for target development, and then
	 * submitting the control.
	 */
	@Operation
	private void configureTarget()
	{
		targetControlView.control.algorithm = TargetDevelopmentAlgorithmsEnum.Simple;
		controlViewPort.transmit(new OperatorTargetControlView(targetControlView));
	}

	/**
	 * Configures the strike system by setting the strike control view to specify 1)
	 * a state of standing by, 2) a strike ordnance of "large" type and unarmed, 3)
	 * its geospatial position of the base as colocated with the radar system's
	 * location, and then submitting the control.
	 */
	@Operation
	private void configureStrike()
	{
		strikeControlView.control.toConfigured(radarSystemPosition);
		controlViewPort.transmit(strikeControlView.copy());
	}

	/**
	 * Returns a true if the current radar monitor indicates a signal return has
	 * been received and the return has a destroyed vehicle signature, false
	 * otherwise.
	 * 
	 * @return whether the radar has assessed the target was destroyed.
	 */
	@Operation
	private boolean radarAssessed()
	{
		boolean assessed = radarMonitorView.monitor.radarSignalReturn.isPresent() && radarMonitorView.monitor.radarSignalReturn.get().signature == RadarReturnSignatureEnum.vehicleDestroyed;
		logger.info(String.format("assessed=%s", assessed));
		return assessed;
	}

	/**
	 * Returns a true if the current target monitor indicates its target has been
	 * destroyed by virtue of the vehicle having stopped moving, false otherwise.
	 * 
	 * @return whether the target has been assessed as destroyed.
	 */
	@Operation
	private boolean targetAssessed()
	{
		return targetMonitorView.monitor.signature == RadarReturnSignatureEnum.vehicleDestroyed && targetMonitorView.monitor.isStopProjected();
	}

	/**
	 * Returns true if the current strike monitor indicates the strike system has
	 * returned to its base, false otherwise.
	 * 
	 * @return whether the strike system has returned to base.
	 */
	@Operation
	private boolean strikeReturned()
	{
		return strikeMonitorView.monitor.state == StrikeSystemStatesEnum.returned;
	}

	/**
	 * Finalizes the strike system by setting the strike control view to specify
	 * "detached" or final state and submitting the control.
	 */
	@Operation
	private void finalizeStrike()
	{
		strikeControlView.control.toFinalized();
		controlViewPort.transmit(strikeControlView);
	}

	/**
	 * Finalizes the targeting software by setting the target control view to
	 * specify a "final" state and submitting the control.
	 */
	@Operation
	private void finalizeTarget()
	{
		targetControlView.control.toFinalized();
		controlViewPort.transmit(targetControlView);
	}

	/**
	 * Finalizes the radar system by setting the radar control view to specify
	 * "detached" or final state and submitting the control.
	 */
	@Operation
	private void finalizeRadar()
	{
		radarControlView.control.toFinalized();
		controlViewPort.transmit(radarControlView);
	}

	/**
	 * Finalizes the C4S2 system by setting the system control view to specify
	 * "final" state and submitting the control.
	 */
	@Operation
	private void finalizeSystem()
	{
		systemControlView.control.toFinalized();
		controlViewPort.transmit(systemControlView);
	}

	/**
	 * Concludes the configuration phase of the operation by simply stopping the
	 * configuration timer.
	 */
	@Operation
	void concludeConfigure()
	{
		logger.info("concluding configuration...");
		((C4S2OperatorStateMachine)stateMachine.get()).stopConfigurationTimer();
	}

	/**
	 * Concludes the F2T2 phase of the operation by simply logging the event.
	 */
	@Operation
	void concludeFindFixTrackTarget()
	{
		logger.info("concluding F2T2...");
	}

	/**
	 * Concludes the Engage phase of the operation by simply logging the event.
	 */
	@Operation
	void concludeEngage()
	{
		logger.info("concluding engagement...");
	}

	/**
	 * Concludes the Assess phase of the operation by simply logging the event.
	 */
	@Operation
	void concludeAssess()
	{
		logger.info("concluding assessment...");
		radarControlView.control.toIdled();
		controlViewPort.transmit(new OperatorRadarControlView(radarControlView));
	}

	public void concludeFinalize()
	{
		logger.info("concluding finalization...");
	}

	/**
	 * Powers on the C4S2 system by switching each components on/off switch to "on".
	 */
	@Operation
	private void powerOnSystems()
	{
		OnOffSwitch switchedOn = new OnOffSwitch(true);
		onOffSwitchC4S2ServicesComputer.transmit(switchedOn);
		onOffSwitchOperatorServicesComputer.transmit(switchedOn);
		onOffSwitchEthernetSwitchIPRouter.transmit(switchedOn);
		onOffSwitchSIPRNetRouter.transmit(switchedOn);
	}

	@Override
	protected void createStateMachine()
	{
		stateMachine = Optional.of(new C4S2OperatorStateMachine(this));
	}

	@Override
	protected void createValues()
	{
		super.createValues();
		scanCenterPosition = new PointGeospatial(new LatitudeDegrees(45.0), new LongitudeDegrees(105.0));
		radarSystemPosition = scanCenterPosition.movedTo(DirectionRadians.south, new DistanceMeters(1000));
		scanHalfDistance = new DistanceMeters(250);
		scanHalfAngle = new DirectionRadians(new DirectionDegrees(22.5));
		scanFrequencyHertz = new FrequencyHertz(2_700_000_000.0);
		scanPowerWatts = new PowerWatts(300_000.0);
		scanTime = InstantMilliseconds.now();
		minConfidenceTargetFound = new ProbabilityPercent(85);
		minConfidenceTargetTracked = new ProbabilityPercent(95);
		maxConfigurationDuration = new DurationMilliseconds(60_000);
		configurationStartTime = InstantMilliseconds.MAX;
		systemGo = False;
		radarGo = False;
		strikeGo = False;
		targetGo = False;
	}

	@Override
	protected void createParts()
	{
		radarControlView = new OperatorRadarControlView(new RadarControl(RadarSystemStatesEnum.Idle, Optional.empty(), InstantMilliseconds.now()));
		radarMonitorView = new OperatorRadarMonitorView(new RadarMonitor(RadarSystemStatesEnum.Idle, Optional.empty(), Optional.empty(), Optional.empty(), InstantMilliseconds.now()));
		systemControlView = new OperatorSystemControlView(new C4S2SystemControl(C4S2ServicesComputerStatesEnum.Initializing, C4S2OperatorServicesComputerStatesEnum.Initializing, EthernetSwitchIPRouterStatesEnum.Initializing,
			SIPRNetRouterStatesEnum.Initializing, ServiceStatesEnum.initial, ServiceStatesEnum.initial, ServiceStatesEnum.initial, ServiceStatesEnum.initial, ServiceStatesEnum.initial, InstantMilliseconds.now()));
		systemMonitorView = new OperatorSystemMonitorView(new SystemMonitor(C4S2ServicesComputerStatesEnum.Initializing, C4S2OperatorServicesComputerStatesEnum.Initializing, EthernetSwitchIPRouterStatesEnum.Initializing,
			SIPRNetRouterStatesEnum.Initializing, ServiceStatesEnum.initial, ServiceStatesEnum.initial, ServiceStatesEnum.initial, ServiceStatesEnum.initial, ServiceStatesEnum.initial, InstantMilliseconds.now()));
		targetControlView = new OperatorTargetControlView(new TargetControl(TargetDevelopmentAlgorithmsEnum.Simple, InstantMilliseconds.now()));
		targetMonitorView = new OperatorTargetMonitorView(new TargetMonitor());
		strikeControlView = new OperatorStrikeControlView(new StrikeControl(StrikeSystemStatesEnum.initializing, Optional.empty(), Optional.empty(), InstantMilliseconds.now()));
		strikeMonitorView = new OperatorStrikeMonitorView(new StrikeMonitor(StrikeSystemStatesEnum.initializing, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), InstantMilliseconds.now()));
	}

	@Override
	protected void createFullPorts()
	{
		super.createFullPorts();
		monitorViewPort = new OperatorMonitorViewProtocol(this, 0L);
		controlViewPort = new OperatorControlTransmitProtocol(this, 0L);
		onOffSwitchC4S2ServicesComputer = new MechanicalOnOffSwitchContact(this, 0L);
		onOffSwitchOperatorServicesComputer = new MechanicalOnOffSwitchContact(this, 0L);
		onOffSwitchEthernetSwitchIPRouter = new MechanicalOnOffSwitchContact(this, 0L);
		onOffSwitchSIPRNetRouter = new MechanicalOnOffSwitchContact(this, 0L);
	}

	@Override
	protected void createProblems()
	{
		singleConfigurationAttempt = new SysMLProblem(
			"Operator currently limited to only one attempt to successfully configure systems before going operational, but should be allowed to perform multiple (2 or 3?) attempts before failing.");
	}
}
