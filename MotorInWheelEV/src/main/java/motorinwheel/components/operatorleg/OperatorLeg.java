
package motorinwheel.components.operatorleg;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Optional;
import motorinwheel.common.ports.energy.AcceleratorPedalForceTransmitPort;
import motorinwheel.common.ports.energy.BrakePedalForceTransmitPort;
import motorinwheel.systems.operator.Operator;
import sysmlinjava.annotations.Flow;
import sysmlinjava.annotations.FullPort;
import sysmlinjava.annotations.Reception;
import sysmlinjava.annotations.Value;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.kinds.SysMLFlowDirectionKind;
import sysmlinjava.statemachine.FinalEvent;
import sysmlinjava.valuetypes.DurationMilliseconds;
import sysmlinjava.valuetypes.ForceNewtons;
import sysmlinjava.valuetypes.IInteger;

/**
 * Model of the operator's pressing on the accelerator or decelorator (brake)
 * pedals of the vehicle. It consists mainly of a interfaces (ports) with the
 * pedals themselves.
 * 
 * @author ModelerOne
 *
 */
public class OperatorLeg extends SysMLBlock
{
	/**
	 * Port for the brake pedal
	 */
	@FullPort
	public BrakePedalForceTransmitPort brakePedal;
	/**
	 * Port for the accelerator pedal
	 */
	@FullPort
	public AcceleratorPedalForceTransmitPort acceleratorPedal;

	/**
	 * Flow (out) of the force on the brake pedal
	 */
	@Flow(direction = SysMLFlowDirectionKind.out)
	public ForceNewtons brakePedalForceOut;
	/**
	 * Flow (out) of the force on the accelerator pedal
	 */
	@Flow(direction = SysMLFlowDirectionKind.out)
	public ForceNewtons acceleratorPedalForceOut;

	/**
	 * Current count of the actions (braking or accelerating) taken
	 */
	@Value
	public IInteger actionsCount;

	/**
	 * ID of the timer for operator actions
	 */
	public static final String timerID = "OperatorSpeedUpdateTimer";
	/**
	 * Definition of the operator actions to be taken
	 */
	public OperatorActions operatorActions;
	/**
	 * Iterator into the set of operator action to be taken
	 */
	public ListIterator<OperatorAction> operatorActionsIterator;

	/**
	 * Constructor
	 * 
	 * @param operator operator in whose context this part resides
	 * @param name     unique name
	 * @param id       unique ID
	 */
	public OperatorLeg(Operator operator, String name, Long id)
	{
		super(operator, name, id);
		operatorActions = new OperatorActions();
		operatorActionsIterator = operatorActions.listIterator();
	}

	/**
	 * Last/prvious action taken by the operator
	 */
	private OperatorAction prevAction = new OperatorAction(Duration.ZERO, Optional.empty(), Optional.of(new ForceNewtons(1)));

	/**
	 * Reaction to occurance of timer to perform next operator action, i.e. update
	 * the speed
	 */
	@Reception
	public void onSpeedUpdateTime()
	{
		actionsCount.increment();
		if (operatorActionsIterator.hasNext())
		{
			OperatorAction action = operatorActionsIterator.next();
			logger.info(action.toString());
			if (action.acceleratorPedalForce.isPresent())
			{
				if (prevAction.brakePedalForce.isPresent())
				{
					brakePedalForceOut.value = 0;
					brakePedal.transmit(brakePedalForceOut);
				}
				acceleratorPedalForceOut.value = action.acceleratorPedalForce.get().value;
				acceleratorPedal.transmit(acceleratorPedalForceOut);
			}
			else if (action.brakePedalForce.isPresent())
			{
				if (prevAction.acceleratorPedalForce.isPresent())
				{
					acceleratorPedalForceOut.value = 0;
					acceleratorPedal.transmit(acceleratorPedalForceOut);
				}
				brakePedalForceOut.value = action.brakePedalForce.get().value;
				brakePedal.transmit(brakePedalForceOut);
			}
			prevAction = action;
		}
		else
		{
			logger.info("Operator actions done");
			stateMachine.get().stopTimer(timerID);
			acceptEvent(new FinalEvent());
		}
	}

	@Override
	public void start()
	{
		super.start();
		DurationMilliseconds initialDelay = DurationMilliseconds.ofSeconds(10);
		DurationMilliseconds period = DurationMilliseconds.ofSeconds(3);
		stateMachine.get().startTimer(OperatorLeg.timerID, initialDelay, period);
	}

	@Override
	public void stop()
	{
		stateMachine.get().stopTimer(OperatorLeg.timerID);
		super.stop();
	}

	@Override
	protected void createStateMachine()
	{
		stateMachine = Optional.of(new OperatorLegStateMachine(this));
	}

	@Override
	protected void createValues()
	{
		actionsCount = new IInteger(0);
	}

	@Override
	protected void createFlows()
	{
		acceleratorPedalForceOut = new ForceNewtons(0);
		brakePedalForceOut = new ForceNewtons(0);
	}

	@Override
	protected void createFullPorts()
	{
		acceleratorPedal = new AcceleratorPedalForceTransmitPort(this, 0L);
		brakePedal = new BrakePedalForceTransmitPort(this, 0L);
	}

	/**
	 * List of actions to be taken by the operator to stimulate the vehicle
	 * behavior, i.e. the model execution
	 * 
	 * @author ModelerOne
	 *
	 */
	public class OperatorActions extends ArrayList<OperatorAction>
	{
		private static final long serialVersionUID = -3540355495329553695L;
		public static final String operatorActionsFilename = "OperatorActions.txt";

		/**
		 * Constructor that loads the action definitions from a file
		 */
		public OperatorActions()
		{
			try
			{
				Path path = Path.of(this.getClass().getResource("/" + operatorActionsFilename).toURI());
				try (BufferedReader lineReader = new BufferedReader(new FileReader(path.toFile())))
				{
					String nextLine = lineReader.readLine();
					while (nextLine != null)
					{
						String[] tokens = nextLine.split(",");
						if (tokens.length == 3)
						{
							Duration timeOffset = Duration.ofSeconds(Long.valueOf(tokens[0]));
							Boolean isAccelerate = tokens[1].equalsIgnoreCase("Accel") ? true : false;
							ForceNewtons forceNewtons = new ForceNewtons(Double.valueOf(tokens[2]));
							if (isAccelerate)
								add(new OperatorAction(timeOffset, Optional.of(forceNewtons), Optional.empty()));
							else
								add(new OperatorAction(timeOffset, Optional.empty(), Optional.of(forceNewtons)));
						}
						nextLine = lineReader.readLine();
					}
				} catch (FileNotFoundException e)
				{
					e.printStackTrace();
				} catch (IOException e)
				{
					e.printStackTrace();
				}
			} catch (URISyntaxException e1)
			{
				e1.printStackTrace();
			}
		}
	}

	/**
	 * Definition of an operator action to be performed at a specified time
	 * 
	 * @author ModelerOne
	 *
	 */
	public class OperatorAction
	{
		/**
		 * Time of the action
		 */
		Duration timeOffset;
		/**
		 * Optional force to be applied to accelerator
		 */
		Optional<ForceNewtons> acceleratorPedalForce;
		/**
		 * Optional force to be applied to brake
		 */
		Optional<ForceNewtons> brakePedalForce;

		/**
		 * Constructor
		 * 
		 * @param timeOffset            time of the action
		 * @param acceleratorPedalForce optional force to be applied to accelerator
		 * @param brakePedalForce       optional force to be applied to brake
		 */
		public OperatorAction(Duration timeOffset, Optional<ForceNewtons> acceleratorPedalForce, Optional<ForceNewtons> brakePedalForce)
		{
			super();
			this.timeOffset = timeOffset;
			this.acceleratorPedalForce = acceleratorPedalForce;
			this.brakePedalForce = brakePedalForce;
		}

		@Override
		public String toString()
		{
			return String.format("OperatorAction [timeOffset=%s, acceleratorPedalForce=%s, brakePedalForce=%s]", timeOffset, acceleratorPedalForce, brakePedalForce);
		}
	}
}
