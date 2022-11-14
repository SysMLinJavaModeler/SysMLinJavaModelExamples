package c4s2.common.objects.information;

import c4s2.common.valueTypes.RadarReturnSignatureEnum;
import sysmlinjava.annotations.Attribute;
import sysmlinjava.common.SysMLClass;
import sysmlinjava.valuetypes.PointGeospatial;

public class RadarReport extends SysMLClass
{
	@Attribute
	public PointGeospatial position;
	@Attribute
	public RadarReturnSignatureEnum type;
}
