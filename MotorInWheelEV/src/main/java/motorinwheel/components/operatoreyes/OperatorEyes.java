
package motorinwheel.components.operatoreyes;

import java.util.Optional;

import motorinwheel.common.ports.information.SpeedValueDisplayReceivePort;
import motorinwheel.systems.operator.Operator;
import sysmlinjava.attributetypes.SpeedKilometersPerHour;
import sysmlinjava.javaannotations.actions.Action;
import sysmlinjava.javaannotations.attributes.Attribute;
import sysmlinjava.javaannotations.ports.Port;
import sysmlinjava.parts.SysMLPart;

/**
 * Model of the operator's viewing of the speed value display (speedometer) of
 * the vehicle. It consists mainly of an interface (port) with the display
 * itself.
 * 
 * @author ModelerOne
 */
public class OperatorEyes extends SysMLPart
{
	/**
	 * Port for the view of the speedometer
	 */
	@Port
	public SpeedValueDisplayReceivePort speedometerView;

	/**
	 * Attribute for the viewed speed value in
	 */
	@Attribute
	public SpeedKilometersPerHour speedViewIn;

	/**
	 * Constructor
	 * 
	 * @param operator operator containing these eyes
	 * @param name     unique name
	 * @param id       unique ID
	 */
	public OperatorEyes(Operator operator, String name, Long id)
	{
		super(Optional.of(operator), name, id);
	}

	/**
	 * Reception for reaction to the view of a new speed value
	 * 
	 * @param currentSpeed value of current speed of the vehicle
	 */
	@Action
	public void onSpeedometerView(SpeedKilometersPerHour currentSpeed)
	{
		logger.info(currentSpeed.toString());
	}

	@Override
	protected void createStateMachine()
	{
		stateMachine = Optional.of(new OperatorEyesStateMachine(this));
	}

	@Override
	protected void createAttributes()
	{
		speedViewIn = new SpeedKilometersPerHour(0);
	}

	@Override
	protected void createPorts()
	{
		speedometerView = new SpeedValueDisplayReceivePort(this, this, 0L);
	}
}
