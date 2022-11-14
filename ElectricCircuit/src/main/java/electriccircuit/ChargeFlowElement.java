package electriccircuit;

import java.util.Optional;
import sysmlinjava.annotations.Flow;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.ports.SysMLProxyPort;

/**
 * {@code ChargeFlowElement} is the SysMLinJava representation of a pin on an
 * electrical component of the {@code ElectricCircuit}. The model is a
 * SysMLinJava implementation of the capacitor model described in "SysML
 * Extension for Physical Interaction and Signal Flow Simulation", Object
 * Management Group, Inc., 2018. The pin is modeled as a SysML proxy port with a
 * single flow value for the charge flowing through the pin.
 * 
 * @author ModelerOne
 *
 */
public class ChargeFlowElement extends SysMLProxyPort
{
	/**
	 * Flow value, in terms of current and voltage, of the charge moving through the
	 * pin/port
	 */
	@Flow
	FlowingCharge cF;

	/**
	 * Constructor
	 * 
	 * @param contextBlock             block of which this proxy port is a property
	 *                                 of
	 * @param implementingContextBlock optional block which implements the interface
	 *                                 represented by the proxy port
	 * @param id                       index into an array of proxy ports for this
	 *                                 port, 0 if single port
	 */
	public ChargeFlowElement(SysMLBlock contextBlock, Optional<? extends SysMLBlock> implementingContextBlock, Long id)
	{
		super(contextBlock, implementingContextBlock, id);
	}

	@Override
	protected void createFlows()
	{
		cF = new FlowingCharge(0, 0);
	}
}
