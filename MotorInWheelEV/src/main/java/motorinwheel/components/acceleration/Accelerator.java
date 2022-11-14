package motorinwheel.components.acceleration;

import java.util.Optional;
import motorinwheel.common.ports.energy.AcceleratorPedalForceReceivePort;
import motorinwheel.common.ports.energy.MechanicalForceTransmitPort;
import sysmlinjava.annotations.Constraint;
import sysmlinjava.annotations.Flow;
import sysmlinjava.annotations.FullPort;
import sysmlinjava.annotations.Reception;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.common.SysMLConstraint;
import sysmlinjava.kinds.SysMLFlowDirectionKind;
import sysmlinjava.valuetypes.ForceNewtons;

/**
 * Vehicle component that provides the operator with acceleration control.
 * Function is to translate operator's force on pedal to amount of electrical
 * power to be delivered to the motors-in-wheels.
 * 
 * @author ModelerOne
 *
 */
public class Accelerator extends SysMLBlock
{
	/**
	 * Port for the receipt of operator's force on the accelerator pedal
	 */
	@FullPort
	public AcceleratorPedalForceReceivePort acceleratorPedal;
	/**
	 * Port for the transmission of mechanical force to the electric power supply's
	 * "switch" control
	 */
	@FullPort
	public MechanicalForceTransmitPort electricalPowerControl;

	/**
	 * Flow of force on the accelerator pedal
	 */
	@Flow(direction = SysMLFlowDirectionKind.in)
	public ForceNewtons acceleratorPedalForceIn;
	/**
	 * Flow of force on the power supply switch
	 */
	@Flow(direction = SysMLFlowDirectionKind.out)
	public ForceNewtons electricalPowerControlForceOut;

	/**
	 * Constraint that translates the force on the pedal to force on the switch
	 * (currently force is 1:1, i.e. no "power assist")
	 */
	@Constraint
	public SysMLConstraint electricPowerControlForceCalculation;

	/**
	 * Constructor
	 * 
	 * @param contextBlock block in whose context the accelerator resides
	 * @param name         unique name
	 * @param id           unique ID
	 */
	public Accelerator(SysMLBlock contextBlock, String name, Long id)
	{
		super(contextBlock, name, id);
	}

	/**
	 * Reception for reaction to the receipt of a force on the accelerator pedal
	 * 
	 * @param acceleratorPedalForce force received
	 */
	@Reception
	public void onAcceleratorPedal(ForceNewtons acceleratorPedalForce)
	{
		logger.info(acceleratorPedalForce.toString());
		acceleratorPedalForceIn.value = acceleratorPedalForce.value;
		electricPowerControlForceCalculation.apply();
		electricalPowerControl.transmit(electricalPowerControlForceOut);
	}

	@Override
	protected void createStateMachine()
	{
		stateMachine = Optional.of(new AcceleratorStateMachine(this));
	}

	@Override
	protected void createValues()
	{
	}

	@Override
	protected void createFlows()
	{
		acceleratorPedalForceIn = new ForceNewtons(0);
		electricalPowerControlForceOut = new ForceNewtons(0);
	}

	@Override
	protected void createFullPorts()
	{
		acceleratorPedal = new AcceleratorPedalForceReceivePort(this, this, 0L);
		electricalPowerControl = new MechanicalForceTransmitPort(this, 0L);
	}

	@Override
	protected void createConstraints()
	{
		electricPowerControlForceCalculation = () ->
		{
			electricalPowerControlForceOut.value = acceleratorPedalForceIn.value;
		};
	}
}
