package electriccircuit;

import java.util.Optional;
import sysmlinjava.analysis.bom.annotations.BOMLineItemComment;
import sysmlinjava.annotations.Flow;
import sysmlinjava.annotations.ProxyPort;
import sysmlinjava.annotations.Value;
import sysmlinjava.annotations.comments.Comment;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.comments.SysMLComment;
import sysmlinjava.valuetypes.DurationSeconds;

/**
 * {@code TwoPinElectricalComponent} is the SysMLinJava model of a common
 * two-pin, positive and negative, electrical component such as a resistor,
 * capacitor, or inductor, etc. as part of
 * the {@code ElectricCircuit} system. The {@code TwoPinElectricalComponent} is specialized/extended for each of these types of components.
 * <p>
 * The {@code TwoPinElectricalComponent} model is as specified by its constraints which are
 * declared in the {@code BinaryElectricalComponentConstraint} block. The
 * {@code BinaryElectricalComponentConstraint} block is used to validate/verify the {@code TwoPinElectricalComponent} 
 * model's execution.
 * 
 * @see <a href="http://www.omg.org/spec/SysPhS/1.0/PDF">SysML Extension for
 *      Physical Interaction and Signal Flow Simulation</a>
 * 
 * @see electriccircuit.TwoPinElectricalComponent
 * 
 * @author ModelerOne
 *
 */
public class TwoPinElectricalComponent extends SysMLBlock
{
	/**
	 * Port that represents the negative pin and its voltage and current flow into
	 * the component
	 */
	@ProxyPort
	ChargeFlowElement n;
	/**
	 * Port that represents the positive pin and its voltage and current flow into
	 * the component
	 */
	@ProxyPort
	ChargeFlowElement p;

	/**
	 * Flow of current through the component
	 */
	@Flow
	Current iThru;
	/**
	 * Voltage across the component
	 */
	@Value
	Voltage vDrop;
	/**
	 * Time of the current values of the component
	 */
	@Value
	DurationSeconds time;

	/**
	 * Comment(s) on the resistor for use in a BOM 
	 */
	@BOMLineItemComment
	@Comment
	public SysMLComment comment;

	/**
	 * Constructor
	 * 
	 * @param name unique name of the component
	 */
	public TwoPinElectricalComponent(String name)
	{
		super(name, 0L);
	}

	@Override
	protected void createValues()
	{
		vDrop = new Voltage(0);
		time = new DurationSeconds(0);
	}

	@Override
	protected void createFlows()
	{
		iThru = new Current(0);
	}

	@Override
	protected void createProxyPorts()
	{
		p = new ChargeFlowElement(this, Optional.of(this), 0L);
		n = new ChargeFlowElement(this, Optional.of(this), 1L);
	}
}
