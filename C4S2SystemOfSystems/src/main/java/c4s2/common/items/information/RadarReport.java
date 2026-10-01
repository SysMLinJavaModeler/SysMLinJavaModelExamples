package c4s2.common.items.information;

import c4s2.common.attributetypes.RadarReturnSignatureEnum;
import sysmlinjava.attributetypes.PointGeospatial;
import sysmlinjava.common.SysMLAnything;
import sysmlinjava.javaannotations.attributes.Attribute;

@SuppressWarnings("javadoc")
public class RadarReport extends SysMLAnything
{
	@Attribute
	public PointGeospatial position;
	@Attribute
	public RadarReturnSignatureEnum type;
}
