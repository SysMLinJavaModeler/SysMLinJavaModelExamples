
package motorinwheel.systems.operator;

import motorinwheel.components.operatoreyes.OperatorEyes;
import motorinwheel.components.operatorleg.OperatorLeg;
import motorinwheel.domain.MotorInWheelEVDomain;
import sysmlinjava.annotations.Part;
import sysmlinjava.blocks.SysMLBlock;

/**
 * The operator of the vehicle. Function is to operate the vehicle, using eyes
 * and leg to monitor and control its speed. The {@code Operator} is simply a
 * parts container with no real behavior as the (@Code OoperatorEyes} and (@Code
 * OoperatorLeg} actually perform the operator actions that drive the model
 * execution.
 * 
 * @author ModelerOne
 *
 */
public class Operator extends SysMLBlock
{
	/**
	 * Part for the operator's eyes that monitor speed
	 */
	@Part
	public OperatorEyes eyes;
	/**
	 * Part for the operator's leg that controls the accelerator and brake pedals
	 */
	@Part
	public OperatorLeg leg;

	/**
	 * Constructor
	 * 
	 * @param domain domain of which the operator is a part
	 * @param name   unique name
	 * @param id     unique ID
	 */
	public Operator(MotorInWheelEVDomain domain, String name, long id)
	{
		super(domain, name, id);
	}

	@Override
	protected void createParts()
	{
		eyes = new OperatorEyes(this, "OperatorEyes", (long)0);
		leg = new OperatorLeg(this, "OperatorLeg", (long)0);
	}

	@Override
	public void start()
	{
		eyes.start();
		leg.start();
	}

	@Override
	public void stop()
	{
		eyes.stop();
		leg.stop();
	}
}
