package motorinwheel.components.acceleration;

import java.util.Optional;

import motorinwheel.common.ports.energy.AcceleratorPedalForceReceivePort;
import motorinwheel.common.ports.energy.MechanicalForceTransmitPort;
import sysmlinjava.attributetypes.ForceNewtons;
import sysmlinjava.javaannotations.actions.Action;
import sysmlinjava.javaannotations.actions.Calculation;
import sysmlinjava.javaannotations.attributes.Attribute;
import sysmlinjava.javaannotations.ports.Port;
import sysmlinjava.parts.SysMLPart;

/**
 * Vehicle component that provides the operator with acceleration control.
 * Function is to translate operator's force on pedal to amount of electrical
 * power to be delivered to the motors-in-wheels.
 * 
 * @author ModelerOne
 *
 */
public class Accelerator extends SysMLPart
{
	/**
	 * Port for the receipt of operator's force on the accelerator pedal
	 */
	@Port
	public AcceleratorPedalForceReceivePort acceleratorPedal;
	/**
	 * Port for the transmission of mechanical force to the electric power supply's
	 * "switch" control
	 */
	@Port
	public MechanicalForceTransmitPort electricalPowerControl;

	/**
	 * Flow of force on the accelerator pedal
	 */
	@Attribute
	public ForceNewtons acceleratorPedalForceIn;
	/**
	 * Flow of force on the power supply switch
	 */
	@Attribute
	public ForceNewtons electricalPowerControlForceOut;

	/**
	 * Constructor
	 * 
	 * @param contextPart part in whose context the accelerator resides
	 * @param name         unique name
	 * @param id           unique ID
	 */
	public Accelerator(SysMLPart contextPart, String name, Long id)
	{
		super(Optional.of(contextPart), name, id);
	}

	/**
	 * Reception for reaction to the receipt of a force on the accelerator pedal
	 * 
	 * @param acceleratorPedalForce force received
	 */
	@Action
	public void onAcceleratorPedal(ForceNewtons acceleratorPedalForce)
	{
		acceleratorPedalForceIn.value = acceleratorPedalForce.value;
		calculateElectricalPowerControlForce();
		electricalPowerControl.transmit(electricalPowerControlForceOut);
	}

	/**
	 * Calculation of the force on the pedal to force on the switch
	 * (currently force is 1:1, i.e. no "power assist")
	 */
	@Calculation
	private void calculateElectricalPowerControlForce()
	{
		electricalPowerControlForceOut.value = acceleratorPedalForceIn.value;
	}

	@Override
	protected void createStateMachine()
	{
		stateMachine = Optional.of(new AcceleratorStateMachine(this));
	}

	@Override
	protected void createAttributes()
	{
		acceleratorPedalForceIn = new ForceNewtons(0);
		electricalPowerControlForceOut = new ForceNewtons(0);
	}

	@Override
	protected void createPorts()
	{
		acceleratorPedal = new AcceleratorPedalForceReceivePort(this, this, 0L);
		electricalPowerControl = new MechanicalForceTransmitPort(this, 0L);
	}
}
