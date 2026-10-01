package c4s2.common.items.information;

import c4s2.common.attributetypes.TargetDevelopmentAlgorithmsEnum;
import sysmlinjava.attributetypes.InstantMilliseconds;
import sysmlinjava.common.SysMLAnything;
import sysmlinjava.javaannotations.attributes.Attribute;
import sysmlinjava.views.common.StackedProtocolObject;

@SuppressWarnings("javadoc")
public class TargetControl extends SysMLAnything implements StackedProtocolObject
{
	@Attribute
	public TargetDevelopmentAlgorithmsEnum algorithm;
	@Attribute
	public InstantMilliseconds time;

	public TargetControl(TargetDevelopmentAlgorithmsEnum algorithm, InstantMilliseconds time)
	{
		super();
		this.algorithm = algorithm;
		this.time = time;
	}

	public TargetControl(TargetControl copied)
	{
		super(copied);
		this.algorithm = copied.algorithm;
		this.time = new InstantMilliseconds(copied.time);
	}

	public TargetControl()
	{
		this.algorithm = TargetDevelopmentAlgorithmsEnum.Simple;
		this.time = InstantMilliseconds.now();
	}

	public void toAssessing()
	{
		algorithm = TargetDevelopmentAlgorithmsEnum.Complex;
		time = InstantMilliseconds.now();
	}

	public void toFinalized()
	{
		algorithm = TargetDevelopmentAlgorithmsEnum.Simple;
		time = InstantMilliseconds.now();

	}

	@Override
	public String stackNamesString()
	{
		return String.format("%s(state=%s)", getClass().getSimpleName(), algorithm);
	}

	@Override
	public String toString()
	{
		return String.format("TargetControl [algorithm=%s, time=%s]", algorithm, time);
	}
}
