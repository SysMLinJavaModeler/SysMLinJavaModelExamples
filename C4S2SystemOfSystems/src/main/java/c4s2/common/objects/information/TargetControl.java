package c4s2.common.objects.information;

import c4s2.common.valueTypes.TargetDevelopmentAlgorithmsEnum;
import sysmlinjava.analysis.common.StackedProtocolObject;
import sysmlinjava.annotations.Attribute;
import sysmlinjava.common.SysMLClass;
import sysmlinjava.valuetypes.InstantMilliseconds;

public class TargetControl extends SysMLClass implements StackedProtocolObject
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
