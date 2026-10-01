package h2ostates;

import java.util.Optional;

import sysmlinjava.attributetypes.LatentHeatKilojoulesPerKilogram;
import sysmlinjava.attributetypes.MassKilograms;
import sysmlinjava.attributetypes.TemperatureDegreesC;
import sysmlinjava.javaannotations.attributes.Attribute;
import sysmlinjava.parts.SysMLPart;

/**
 * {@code H2O} is a SysMLinJava example of a continuous process modeled by a SysML state
 * machine. The process is simply the heating and cooling of an amount of water
 * into its physical states, i.e. the ice, liquid, gas, and decomposed (final)
 * states. The {@code H2OStateMachine} maintains the state of the H2O based on the
 * current temperature and the applied latent heat, transitioning between the
 * states as the temperature and applied latent heat reaches a "change of state"
 * level.
 * <p>
 * The {@code H2O} model demonstrates how to model a state machine in SysMLinJava, i.e.
 * using the SysMLinJava API to declare the states and transitions that comprise
 * the state machine. The example also illustrates how to use the SysMLinJava
 * test and test case elements to test a system. In this H2O example, the
 * {@code H2OStatesTest} performs the testing of the H2O example.
 * <p>
 * The {@code H2OStateMachine} is a SysMLinJava implementation of the SysML state
 * machine for H2O found in the book "A Practical Guide to SysML - The Systems
 * Modeling Language 3rd edition" by Sanford Friedenthal, et al; Object
 * Management Group; Morgan Kaufman publisher; copyright 2015.
 * 
 * @author ModelerOne
 *
 */
public class H2O extends SysMLPart
{
	/**
	 * Temperature of the H2O
	 */
	@Attribute
	public TemperatureDegreesC temp;
	/**
	 * Temperature of the H2O at which it becomes a gas
	 */
	@Attribute
	public TemperatureDegreesC gasTemp;
	/**
	 * Temperature of the H2O at which it becomes ice
	 */
	@Attribute
	public TemperatureDegreesC iceTemp;
	/**
	 * Temperature of the H2O at which it decomposes (H2O molecule breaks up)
	 */
	@Attribute
	public TemperatureDegreesC decomposedTemp;
	/**
	 * Mass of the H2O
	 */
	@Attribute
	public MassKilograms mass;
	/**
	 * Current latent heat
	 */
	@Attribute
	public LatentHeatKilojoulesPerKilogram latentHeat;
	/**
	 * Minimum latent heat to evaporate the H2O
	 */
	@Attribute
	public LatentHeatKilojoulesPerKilogram minLatentHeatEvaporation;
	/**
	 * Minimum latent heat to liquify the H2O
	 */
	@Attribute
	public LatentHeatKilojoulesPerKilogram minLatentHeatCondensation;

	/**
	 * Constructor
	 */
	public H2O()
	{
		super();
	}

	@Override
	protected void createStateMachine()
	{
		stateMachine = Optional.of(new H2OStateMachine(this));
	}

	@Override
	protected void createAttributes()
	{
		temp = new TemperatureDegreesC(-10);
		gasTemp = new TemperatureDegreesC(100);
		iceTemp = new TemperatureDegreesC(0);
		decomposedTemp = new TemperatureDegreesC(2000);
		mass = new MassKilograms(10);
		latentHeat = new LatentHeatKilojoulesPerKilogram(2265);
		minLatentHeatEvaporation = LatentHeatKilojoulesPerKilogram.waterVaporizationLH;
		minLatentHeatCondensation = LatentHeatKilojoulesPerKilogram.waterCondensationLH;
	}
}
