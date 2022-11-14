package electriccircuit;

import static java.lang.Math.PI;
import static java.lang.Math.sin;
import static java.lang.Math.cos;
import sysmlinjava.analysis.bom.annotations.BOMLineItemValue;
import sysmlinjava.annotations.Operation;
import sysmlinjava.annotations.Value;
import sysmlinjava.comments.SysMLComment;
import sysmlinjava.valuetypes.CurrentAmps;
import sysmlinjava.valuetypes.DurationSeconds;

/**
 * {@code VoltageSource} is the SysMLinJava model of an voltage source component
 * of the {@code ElectricCircuit} system. The model is a SysMLinJava
 * implementation of the voltage source described in "SysML Extension for
 * Physical Interaction and Signal Flow Simulation", Object Management Group,
 * Inc., 2018. The {@code VoltageSource} is a {@code TwoPinElectricalComponent}
 * characterized by its steady-state AC voltage. It has two pins, a positive and
 * a negative, that interface with other circuit components.
 * <p>
 * The {@code VoltageSource} model is as specified by its constraints which are
 * declared in the {@code VoltageSourceConstraint} block. The
 * {@code VoltageSourceConstraint} block is used to validate/verify the voltage
 * source model's execution.
 * 
 * @see <a href="http://www.omg.org/spec/SysPhS/1.0/PDF">SysML Extension for
 *      Physical Interaction and Signal Flow Simulation</a>
 * 
 * @see electriccircuit.VoltageSource
 * 
 * @author ModelerOne
 *
 */
public class VoltageSource extends TwoPinElectricalComponent
{
	/**
	 * Value for the voltage being input to the circuit by the source
	 */
	@Value
	public Voltage input;
	/**
	 * Value for the amplitude of the voltage input
	 */
	@BOMLineItemValue
	@Value
	public Voltage amplitude;
	/**
	 * Value of the derivative of the voltage being input to the circuit by the
	 * source
	 */
	@Value
	public Voltage dvdt;
	/**
	 * Value for the maximum current that can be output to the circuit from the source
	 */
	@BOMLineItemValue
	@Value
	public CurrentAmps maxCurrent;

	/**
	 * Constructor
	 * 
	 * @param name       unique name of the voltage source
	 */
	public VoltageSource(String name)
	{
		super(name);
	}

	/**
	 * Updates (recalculates) the new voltage input for the specified time. Whereas
	 * the voltage input follows a simple sinusoidal waveform, the new voltage is
	 * simply a calculation of the amplitude multiplied by the current time value of
	 * the sin function.
	 * 
	 * @param time time (into the model execution) of the new voltage
	 */
	@Operation
	public void updateVin(DurationSeconds time)
	{
		input.setValue(amplitude.value * sin(2.0 * PI * time.value));
		dvdt.setValue(amplitude.value * cos(2.0 * PI * time.value));
		this.time.value = time.value;
	}

	/**
	 * Updates (recalculates) the new current input, as well as other derived
	 * properties of, the voltage source for the specified current in the circuit.
	 * 
	 * @param i current in the circuit that is driven by the voltage source
	 */
	@Operation
	public void updateIThru(Current i)
	{
		vDrop.setValue(input);
		iThru.setValue(i);
		p.cF.i.setValue(i);
		p.cF.v.setValue(input);
		n.cF.i.setValue(i);
		n.cF.v.setValue(0);
		time.setValue(time);
	}

	@Override
	protected void createValues()
	{
		super.createValues();
		amplitude = new Voltage(220.0);
		input = new Voltage(0);
		dvdt = new Voltage(0);
		maxCurrent = new CurrentAmps(20.0);
		time = new DurationSeconds(0);
	}

	@Override
	protected void createComments()
	{
		super.createComments();
		comment = new SysMLComment("Description:Voltage source/power supply for circuit\nSource:AcePowerSupplies.com");
	}
}