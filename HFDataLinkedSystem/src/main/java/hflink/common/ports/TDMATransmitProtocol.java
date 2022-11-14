package hflink.common.ports;

import hflink.common.objects.DataLinkFrame;
import hflink.common.objects.GPSMessage;
import hflink.common.objects.TDMASlot;
import sysmlinjava.annotations.Reception;
import sysmlinjava.annotations.Value;
import sysmlinjava.annotations.comments.Hyperlink;
import sysmlinjava.annotations.requirements.RequirementSpecificationLink;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.comments.SysMLHyperlink;
import sysmlinjava.common.SysMLClass;
import sysmlinjava.ports.SysMLFullPort;
import sysmlinjava.valuetypes.DurationSeconds;
import sysmlinjava.valuetypes.IInteger;
import sysmlinjava.valuetypes.InstantMilliseconds;
import sysmlinjava.valuetypes.QueueFIFO;

/**
 * Full port for the TDMA transmit protocol. The protocol models/simulates
 * common implementations of the TDMA, i.e. signals to be transmitted are
 * buffered until a GPS time is received that corresponds to the time of the
 * TDMA slot that is assigned to this protocol instance. Upon receipt of the
 * instance's slot time, the protocol proceeds to transmit the signal using the
 * connected server port, which in the case of the HF datalink's Modem-Radio is
 * presumed to be the PSK protocol port.
 * 
 * @author ModelerOne
 *
 */
public class TDMATransmitProtocol extends SysMLFullPort
{
	/**
	 * Value for the ID of the TDMA slot assigned to this protocol instance
	 */
	@Value
	public IInteger tdmaSlotID;

	/**
	 * Value of the time the protocol received an object to transmit and started its
	 * wait for its assigned TDMA slot to occur
	 */
	@Value
	public InstantMilliseconds startedWaitTime;
	/**
	 * Value of the time waited for its assigned TDMA slot to occur (used for
	 * parameteric analysis performed by
	 * {@code TDMAWaitTimeFrequencyConstraintBlock})
	 */
	@Value
	public DurationSeconds timeWaited;

	/**
	 * Value for buffer of objects to be transmitted in the TDMA slots when they occur
	 */
	@Value
	public QueueFIFO<TDMASlot> tdmaSlotBuffer;

	/**
	 * Constructor
	 * 
	 * @param contextBlock block in whose context this port exists
	 * @param name         unique name
	 */
	public TDMATransmitProtocol(SysMLBlock contextBlock, String name)
	{
		super(contextBlock, 0L, name);
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
	public void transmit(SysMLClass object)
	{
		if (!connectedPortsServers.isEmpty())
		{
			TDMASlot slot = (TDMASlot)serverObjectFor(object);
			tdmaSlotBuffer.add(slot);
			startedWaitTime = InstantMilliseconds.now();
		}
	}

	/**
	 * Reception for reaction to the receipt of a GPSMessage with the current time.
	 * If the time is in accordance with the TDMA slot assigned to this protocol
	 * instance, then the next TDMASlot object is removed from the buffer and
	 * transmitted via the server protocols connected to this protocol, presumably
	 * the PSK protocols of the context Modem-Radio.
	 * 
	 * @param gpsMessage message received from GPS with a time reading
	 */
	@Reception
	public void onGPSMessage(GPSMessage gpsMessage)
	{
		if (gpsMessage.slotID() == tdmaSlotID.value)
		{
			TDMASlot slot = tdmaSlotBuffer.poll();
			if(slot != null)
			{
				DurationSeconds deltaTime = InstantMilliseconds.now().subtracted(startedWaitTime);
				
				try {
				timeWaited.setValue(deltaTime);
				}catch(Exception e) {e.printStackTrace();}
				
				connectedPortsServers.forEach(serverPort ->
				{
					serverPort.transmit(slot);
	
					// delay to simulate time to transmit frame at 96 kilobits/sec (12kbytes/sec)
					DurationSeconds delayTime = DurationSeconds.of((long)((((DataLinkFrame)slot.data).sizeBytes() / (96_000 / 8))));
					delay(delayTime.value);
				});
			}
		}
	}

	@Override
	protected SysMLClass serverObjectFor(SysMLClass clientObject)
	{
		SysMLClass result = null;
		if (clientObject instanceof DataLinkFrame)
		{
			DataLinkFrame frame = (DataLinkFrame)clientObject;
			result = new TDMASlot((int)tdmaSlotID.value, frame);
		}
		else
			logger.warning("unexpected client object type: " + clientObject.getClass().getSimpleName());
		return result;
	}

	@Override
	protected void createValues()
	{
		tdmaSlotID = new IInteger(0);
		startedWaitTime = new InstantMilliseconds(0);
		timeWaited = new DurationSeconds(0);
	}

	@Override
	protected void createHyperlinks()
	{
		protocolStandard = new SysMLHyperlink("IRS for Time-Division-Multiple-Access Transmit Protocol", "file://IRS for Time-Division-Multiple-Access Transmit Protocol");
	}
}
