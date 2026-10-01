package hflink.common.ports;

import hflink.common.items.DataLinkFrame;
import hflink.common.items.GPSMessage;
import hflink.common.items.TDMASlot;
import sysmlinjava.actions.SysMLAction;
import sysmlinjava.actions.SysMLActionFunction;
import sysmlinjava.annotations.SysMLHyperlink;
import sysmlinjava.attributetypes.DurationSeconds;
import sysmlinjava.attributetypes.IInteger;
import sysmlinjava.attributetypes.InstantMilliseconds;
import sysmlinjava.attributetypes.QueueFIFO;
import sysmlinjava.common.SysMLAnything;
import sysmlinjava.javaannotations.actions.Action;
import sysmlinjava.javaannotations.actions.ActionFunction;
import sysmlinjava.javaannotations.annotations.Hyperlink;
import sysmlinjava.javaannotations.attributes.Attribute;
import sysmlinjava.javaannotations.requirements.RequirementAttribute;
import sysmlinjava.javaannotations.requirements.RequirementFunction;
import sysmlinjava.javaannotations.requirements.RequirementSpecificationLink;
import sysmlinjava.ports.SysMLPort;
import sysmlinjava.states.StateBehaviorContext;

/**
 * Port representation of the TDMA transmit protocol. The protocol
 * models/simulates common implementations of the TDMA, i.e. signals to be
 * transmitted are buffered until a GPS time is received that corresponds to the
 * time of the TDMA slot that is assigned to this protocol instance. Upon
 * receipt of the instance's slot time, the protocol proceeds to transmit the
 * signal using the connected server port, which in the case of the HF
 * datalink's Modem-Radio is presumed to be the PSK protocol port.
 * 
 * @author ModelerOne
 */
public class TDMATransmitProtocol extends SysMLPort
{
	/**
	 * Attribute for the ID of the TDMA slot assigned to this protocol instance
	 */
	@RequirementAttribute
	@Attribute
	public IInteger tdmaSlotID;

	/**
	 * Attribute of the time the protocol received an object to transmit and started
	 * its wait for its assigned TDMA slot to occur
	 */
	@Attribute
	public InstantMilliseconds startedWaitTime;
	/**
	 * Attribute of the time waited for its assigned TDMA slot to occur (used for
	 * parameteric analysis performed by {@code TDMAWaitTimeFrequencyAnalysisCase})
	 * 
	 * @see hflink.analysis.TDMAWaitTimeFrequencyAnalysisCase
	 */
	@RequirementAttribute
	@Attribute
	public DurationSeconds timeWaited;

	/**
	 * Attribute for buffer of objects to be transmitted in the TDMA slots when they
	 * occur
	 */
	@Attribute
	public QueueFIFO<TDMASlot> tdmaSlotBuffer;

	/**
	 * Functional interface for the function that, if slot time occurred, performs
	 * the actual transmission of TDMA slot data.
	 */
	@FunctionalInterface
	public interface TransmitSlotActionFunction extends SysMLActionFunction
	{
		/**
		 * If slot time occurred, performs the actual transmission of TDMA slot data
		 * 
		 * @param gpsMessage GPS message received
		 */
		public void perform(GPSMessage gpsMessage);
	}

	/**
	 * Function that, if slot time occurred, performs the actual transmission of
	 * TDMA slot data.
	 */
	@ActionFunction
	public TransmitSlotActionFunction transmitSlotActionFunction;

	/**
	 * Action that contains the function that, if slot time occurred, performs the
	 * actual transmission of TDMA slot data.
	 */
	@Action
	public SysMLAction transmitSlotAction;

	/**
	 * Constructor
	 * 
	 * @param context block in whose context this port exists
	 * @param name    unique name
	 */
	public TDMATransmitProtocol(StateBehaviorContext context, String name)
	{
		super(context, 0L, name);
		tdmaSlotBuffer = new QueueFIFO<>();
	}

	/**
	 * Comment specifying the standard/specification of the TDMA transmit protocol
	 */
	@RequirementSpecificationLink
	@Hyperlink
	public SysMLHyperlink protocolStandard;

	/**
	 * Commences the transmission of the specified object by encapsulating it in a
	 * TDMASlot object, adding it to the slot buffer for eventual transmission upon
	 * the occurence of the TDMA slot for this protocol instance.
	 */
	@Override
	public void transmit(SysMLAnything object)
	{
		if (!connectedPortsServers.isEmpty())
		{
			TDMASlot slot = (TDMASlot) serverObjectFor(object);
			tdmaSlotBuffer.add(slot);
			startedWaitTime = InstantMilliseconds.now();
		}
	}

	/**
	 * Action for the receipt of a GPSMessage with the current time. If the time is
	 * in accordance with the TDMA slot assigned to this protocol instance, then the
	 * next TDMASlot object is removed from the buffer and transmitted via the
	 * server protocols connected to this protocol, presumably the PSK protocols of
	 * the context Modem-Radio.
	 * 
	 * @param gpsMessage message received from GPS with a time reading
	 */
	@RequirementFunction
	@Action
	public void onGPSMessage(GPSMessage gpsMessage)
	{
		if (transmitSlotAction.function instanceof TransmitSlotActionFunction function)
			function.perform(gpsMessage);
	}

	@Override
	protected SysMLAnything serverObjectFor(SysMLAnything clientObject)
	{
		SysMLAnything result = null;
		if (clientObject instanceof DataLinkFrame)
		{
			DataLinkFrame frame = (DataLinkFrame) clientObject;
			result = new TDMASlot((int) tdmaSlotID.value, frame);
		}
		else
			logger.warning("unexpected client object type: " + clientObject.getClass().getSimpleName());
		return result;
	}

	@Override
	protected void createActionFunctions()
	{
		transmitSlotActionFunction = (gpsMessage) ->
		{
			if (gpsMessage.slotID() == tdmaSlotID.value)
			{
				TDMASlot slot = tdmaSlotBuffer.poll();
				if (slot != null)
				{
					DurationSeconds deltaTime = InstantMilliseconds.now().subtracted(startedWaitTime);

					try
					{
						timeWaited.setValue(deltaTime);
					} catch (Exception e)
					{
						e.printStackTrace();
					}

					connectedPortsServers.forEach(serverPort ->
					{
						serverPort.transmit(slot);

						// delay to simulate time to transmit frame at 96 kilobits/sec (12kbytes/sec)
						DurationSeconds delayTime = DurationSeconds.of(((slot.data.sizeBytes() / (96_000 / 8))));
						delay(delayTime.value);
					});
				}
			}
		};
	}

	@Override
	protected void createActions()
	{
		transmitSlotAction = new SysMLAction(transmitSlotActionFunction, "slotAction", 0L);
	}

	@Override
	protected void createAttributes()
	{
		tdmaSlotID = new IInteger(0);
		startedWaitTime = new InstantMilliseconds(0);
		timeWaited = new DurationSeconds(0);
	}

	@Override
	protected void createSupportingInformationLinks()
	{
		protocolStandard = new SysMLHyperlink("IRS for Time-Division-Multiple-Access Transmit Protocol", "file://IRS for Time-Division-Multiple-Access Transmit Protocol");
	}
}
