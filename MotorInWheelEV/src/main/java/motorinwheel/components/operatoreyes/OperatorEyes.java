
package motorinwheel.components.operatoreyes;

import java.util.Optional;
import motorinwheel.common.ports.information.SpeedValueDisplayReceivePort;
import motorinwheel.systems.operator.Operator;
import sysmlinjava.annotations.Flow;
import sysmlinjava.annotations.FullPort;
import sysmlinjava.annotations.Reception;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.kinds.SysMLFlowDirectionKind;
import sysmlinjava.valuetypes.SpeedKilometersPerHour;

/**
 * Model of the operator's viewing of the speed value display (speedometer) of
 * the vehicle. It consists mainly of an interface (port) with the display
 * itself.
 * 
 * @author ModelerOne
 *
 */
public class OperatorEyes extends SysMLBlock
{
	/**
	 * Port for the view of the speedometer
	 */
	@FullPort
	public SpeedValueDisplayReceivePort speedometerView;

	/**
	 * Flow of the viewed speed value in
	 */
	@Flow(direction = SysMLFlowDirectionKind.in)
	public SpeedKilometersPerHour speedViewIn;

	public OperatorEyes(Operator operator, String name, Long id)
	{
		super(operator, name, id);
	}

	/**
	 * Reception for reaction to the view of a new speed value
	 * 
	 * @param currentSpeed value of current speed of the vehicle
	 */
	@Reception
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
	protected void createFlows()
	{
		speedViewIn = new SpeedKilometersPerHour(0);
	}

	@Override
	protected void createFullPorts()
	{
		speedometerView = new SpeedValueDisplayReceivePort(this, this, 0L);
	}
}
