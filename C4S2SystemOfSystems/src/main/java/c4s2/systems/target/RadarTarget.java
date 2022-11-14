package c4s2.systems.target;

import c4s2.common.objects.information.RadarSignalReturn;
import c4s2.common.objects.information.RadarSignalTransmission;
import c4s2.common.ports.information.RadarSignalReturnTransmitProtocol;
import c4s2.common.valueTypes.RadarReturnSignatureEnum;
import c4s2.common.ports.information.RadarSignalReceiver;
import sysmlinjava.annotations.FullPort;
import sysmlinjava.annotations.Operation;
import sysmlinjava.annotations.Reception;
import sysmlinjava.annotations.Value;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.valuetypes.PointGeospatial;

/**
 * The RadarTarget is a SysMLinJava model of a basic radar target. The radar
 * target has a port to receive radar signal transmissions and a port to return
 * (reflect) the received transmissions. It also has a geoposition and a radar
 * signature, i.e. a reflected radar signal that can be identified as a certain
 * type of target, e.g. large armored vehicle, small wheeled vehicle, structure,
 * or destroyed object. The RadarTarget should be specialized for one of the
 * signatures the radar is capable of identifying when used for a
 * model/simulation of a real world radar system.
 * <p>
 * The RadarTarget also has a single event reception method, i.e. one for
 * receiving the radar signal transmission. The method simply determines if the
 * signal's scanning area includes the current location of the radar target and,
 * if so, returns the signal with its signature embedded in the signal. This
 * simulates a radar system's ability to transmit a signal, receive its
 * reflection, determine from the returned signal the target's location and its
 * signature. Specializations of the RadarTarget can override this method to
 * customize the target's return of the radar scan.
 * 
 * @author ModelerOne
 *
 */
public class RadarTarget extends SysMLBlock
{
	/**
	 * Port representing the "receiver" of a radar signal, i.e. the targets surface.
	 */
	@FullPort
	public RadarSignalReceiver radarSignalReceiver;
	/**
	 * Port representing the reflection of the received radar signal, i.e. the
	 * signal return.
	 */
	@FullPort
	public RadarSignalReturnTransmitProtocol radarSignalReturn;

	/**
	 * Value for the signature that is embedded in the radar signal returned
	 * (reflected back) to the radar
	 */
	@Value
	public RadarReturnSignatureEnum signature;
	/**
	 * Value for the target's current (geospatial) position. Specializations of the
	 * RadarTarget should set this value if/when it changes its position, i.e. is a
	 * moving target.
	 */
	@Value
	public PointGeospatial currentPosition;

	/**
	 * Constructor
	 */
	public RadarTarget()
	{
		super();
	}

	/**
	 * Receives the radar signal transmission to determine if the target is within
	 * the radar's scan area and, if so, returns (reflects) the radar signal with
	 * the embedded signature and location of the target.
	 * 
	 * @param transmission the radar scanning signal transmitted to the target.
	 */
	@Reception
	public void receiveRadarSignalTransmission(RadarSignalTransmission transmission)
	{
		boolean detected = transmission.includes(currentPosition);
		if (detected)
			returnRadarSignalTransmission(transmission);
	}

	/**
	 * Overridable operation that returns (reflects) the specifed radar signal
	 * transmission with a radar signal return indicating the target's signature and
	 * position. signal. The {@code RadarSignalReturn} simulates the information
	 * that would be contained in the waveform that is received by the radar from
	 * the target reflection.
	 * 
	 * @param transmission the radar scanning signal transmitted to and received by
	 *                     the target
	 */
	@Operation
	public void returnRadarSignalTransmission(RadarSignalTransmission transmission)
	{
		radarSignalReturn.transmit(new RadarSignalReturn(new PointGeospatial(currentPosition), signature));
	}

	@Override
	protected void createFullPorts()
	{
		radarSignalReceiver = new RadarSignalReceiver(this, 0L);
		radarSignalReturn = new RadarSignalReturnTransmitProtocol(this, 0L);
	}

	@Override
	protected void createValues()
	{
		signature = RadarReturnSignatureEnum.unknown;
		currentPosition = new PointGeospatial();
	}
}
