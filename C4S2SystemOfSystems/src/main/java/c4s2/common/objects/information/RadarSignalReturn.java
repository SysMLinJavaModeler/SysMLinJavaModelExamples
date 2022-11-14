package c4s2.common.objects.information;

import c4s2.common.valueTypes.RadarReturnSignatureEnum;
import sysmlinjava.analysis.common.StackedProtocolObject;
import sysmlinjava.annotations.Attribute;
import sysmlinjava.common.SysMLClass;
import sysmlinjava.valuetypes.PointGeospatial;

public class RadarSignalReturn extends SysMLClass implements StackedProtocolObject
{
	@Attribute
	public PointGeospatial position;
	@Attribute
	public RadarReturnSignatureEnum signature;

	public RadarSignalReturn(PointGeospatial position, RadarReturnSignatureEnum signature)
	{
		super();
		this.position = position;
		this.signature = signature;
	}

	/**
	 * Conxtructor for deep copy of this
	 * 
	 * @param copied instance to be copied
	 */
	public RadarSignalReturn(RadarSignalReturn copied)
	{
		super(copied);
		this.position = new PointGeospatial(copied.position);
		this.signature = copied.signature;
	}

	public RadarSignalReturn()
	{
		this.position = new PointGeospatial();
		this.signature = RadarReturnSignatureEnum.unknown;
	}

	@Override
	public String stackNamesString()
	{
		return String.format("%s(signature=%s)", getClass().getSimpleName(), signature);
	}

	@Override
	public String toString()
	{
		return String.format("RadarSignalReturn [position=%s, signature=%s]", position, signature);
	}
}
