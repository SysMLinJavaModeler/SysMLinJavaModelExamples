package electriccircuit;

import java.util.Optional;

import sysmlinjava.javaannotations.attributes.Attribute;
import sysmlinjava.ports.SysMLProxyPort;
import sysmlinjava.states.StateBehaviorContext;

/**
 * {@code ChargeFlowElement} is the SysMLinJava representation of a pin on an
 * electrical component of the {@code ElectricCircuit}. The model is a
 * SysMLinJava implementation of the capacitor model described in "SysML
 * Extension for Physical Interaction and Signal Flow Simulation", Object
 * Management Group, Inc., 2018. The pin is modeled as a SysML proxy port with a
 * single flow value for the charge flowing through the pin.
 * 
 * @see <a href="https://www.omg.org/spec/SysPhS/1.0/PDF">SysML Extension for
 *      Physical Interaction and Signal Flow Simulation</a>
 * 
 * @author ModelerOne
 *
 */
public class ChargeFlowElement extends SysMLProxyPort
{
	/**
	 * Attribute for flowing CurrentAmps and voltage, i.e. the charge moving through the
	 * port
	 */
	@Attribute
	FlowingCharge cF;

	/**
	 * Constructor
	 * 
	 * @param context             part or port of which this proxy port is a
	 *                            property of
	 * @param implementingContext optional part or port which implements the
	 *                            interface represented by the proxy port
	 * @param id                  index into an array of proxy ports for this port,
	 *                            0 if single port
	 */
	public ChargeFlowElement(StateBehaviorContext context, Optional<StateBehaviorContext> implementingContext, Long id)
	{
		super(context, implementingContext, "ChargeFlowElement", id);
	}

	@Override
	protected void createAttributes()
	{
		cF = new FlowingCharge(0, 0);
	}
}
