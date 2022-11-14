package hflink.components.switchrouter;

import java.util.Optional;
import hflink.common.events.IPPacketEvent;
import hflink.common.objects.IPPacket;
import sysmlinjava.annotations.statemachines.Effect;
import sysmlinjava.annotations.statemachines.EffectActivity;
import sysmlinjava.annotations.statemachines.State;
import sysmlinjava.annotations.statemachines.Transition;
import sysmlinjava.statemachine.InitialTransition;
import sysmlinjava.statemachine.SysMLEffect;
import sysmlinjava.statemachine.SysMLEffectActivity;
import sysmlinjava.statemachine.SysMLState;
import sysmlinjava.statemachine.SysMLStateMachine;
import sysmlinjava.statemachine.SysMLTransition;
import sysmlinjava.statemachine.SysMLTransitionKind;

/**
 * State machine for the Ethernet Switch/IP Router model/simulation. The state
 * machine consists of states to initialize and operate, with the key transition
 * being the one that occurs upon receipt of an IP Packet to be routed to the
 * applicable ethernet port. See the model of the state machine below for more
 * detail.
 * 
 * @author ModelerOne
 *
 */
public class EthernetSwitchIPRouterStateMachine extends SysMLStateMachine
{
	@State
	private SysMLState initializingState;
	@State
	private SysMLState operationalState;

	@Transition
	private InitialTransition initialToInitializingTransition;
	@Transition
	private SysMLTransition initializingToOperationalTransition;
	@Transition
	private SysMLTransition operationalOnIPPacketTransition;
	@Transition
	private SysMLTransition operationalToFinalTransition;

	@EffectActivity
	private SysMLEffectActivity onIPPacketEventEffectActivity;
	@Effect
	private SysMLEffect onIPPacketEventEffect;

	public EthernetSwitchIPRouterStateMachine(EthernetSwitchIPRouter ethernetSwitch)
	{
		super(Optional.of(ethernetSwitch), true, "EthernetSwitchIPRouterStateMachine");
	}

	@Override
	protected void createStates()
	{
		super.createStates();
		initializingState = new SysMLState(contextBlock, "Initializing");
		operationalState = new SysMLState(contextBlock, "Operational");
	}

	@Override
	protected void createTransitions()
	{
		initialToInitializingTransition = new InitialTransition(contextBlock, initialState, initializingState, "InitialToInitializing");
		initializingToOperationalTransition = new SysMLTransition(contextBlock, initializingState, operationalState, "InitializingToOperational");
		operationalOnIPPacketTransition = new SysMLTransition(contextBlock, operationalState, operationalState, Optional.of(IPPacketEvent.class), Optional.empty(), Optional.of(onIPPacketEventEffect), "OperationalOnIPPacket",
			SysMLTransitionKind.internal);
		operationalToFinalTransition = new SysMLTransition(contextBlock, operationalState, finalState, Optional.of(IPPacketEvent.class), Optional.empty(), Optional.empty(), "OperationalToFinal", SysMLTransitionKind.external);
	}

	@Override
	protected void createEffectActivities()
	{
		super.createEffectActivities();
		onIPPacketEventEffectActivity = (event, contextBlock) ->
		{
			if (event.get() instanceof IPPacketEvent)
			{
				IPPacket packet = ((IPPacketEvent)event.get()).getPacket();
				EthernetSwitchIPRouter switchRouter = (EthernetSwitchIPRouter)contextBlock.get();
				switchRouter.routeIPPacket(packet);
			}
		};
	}

	@Override
	protected void createEffects()
	{
		super.createEffects();
		this.onIPPacketEventEffect = new SysMLEffect(contextBlock, onIPPacketEventEffectActivity, "onIPPacket");
	}
}
