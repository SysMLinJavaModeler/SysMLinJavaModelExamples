package hflink.tests;

import java.util.Optional;

import sysmlinjava.attributetypes.DurationSeconds;
import sysmlinjava.attributetypes.RReal;
import sysmlinjava.probability.SysMLUniformProbabilityDistribution;

@SuppressWarnings("javadoc")
public class DurationSecondsRandomUniform extends DurationSeconds
{
	private static final long serialVersionUID = -3105055554710597969L;

	public DurationSecondsRandomUniform(RReal value)
	{
		super(value);
	}

	@Override
	protected void createProbabilityDistribution()
	{
		probabilityDistribution = Optional.of(new SysMLUniformProbabilityDistribution(new RReal(1.0), new RReal(5.0)));
	}
}
