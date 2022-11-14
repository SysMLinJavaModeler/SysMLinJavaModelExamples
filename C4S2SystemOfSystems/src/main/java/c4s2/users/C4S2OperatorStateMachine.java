
package c4s2.users;

import java.util.Optional;
import c4s2.common.events.C4S2OperatorControlEvent;
import c4s2.common.events.OperatorRadarMonitorViewEvent;
import c4s2.common.events.OperatorStrikeMonitorViewEvent;
import c4s2.common.events.OperatorSystemMonitorViewEvent;
import c4s2.common.events.OperatorTargetMonitorViewEvent;
import c4s2.common.objects.information.OperatorRadarMonitorView;
import c4s2.common.objects.information.OperatorStrikeMonitorView;
import c4s2.common.objects.information.OperatorSystemMonitorView;
import c4s2.common.objects.information.OperatorTargetMonitorView;
import c4s2.common.valueTypes.C4S2OperatorStatesEnum;
import sysmlinjava.annotations.statemachines.Effect;
import sysmlinjava.annotations.statemachines.EffectActivity;
import sysmlinjava.annotations.statemachines.Guard;
import sysmlinjava.annotations.statemachines.GuardCondition;
import sysmlinjava.annotations.statemachines.OnEnterActivity;
import sysmlinjava.annotations.statemachines.OnExitActivity;
import sysmlinjava.annotations.statemachines.State;
import sysmlinjava.annotations.statemachines.Transition;
import sysmlinjava.events.SysMLTimeEvent;
import sysmlinjava.statemachine.FinalTransition;
import sysmlinjava.statemachine.InitialTransition;
import sysmlinjava.statemachine.SysMLEffect;
import sysmlinjava.statemachine.SysMLEffectActivity;
import sysmlinjava.statemachine.SysMLGuard;
import sysmlinjava.statemachine.SysMLGuardCondition;
import sysmlinjava.statemachine.SysMLOnEnterActivity;
import sysmlinjava.statemachine.SysMLOnExitActivity;
import sysmlinjava.statemachine.SysMLState;
import sysmlinjava.statemachine.SysMLStateMachine;
import sysmlinjava.statemachine.SysMLTransition;
import sysmlinjava.statemachine.SysMLTransitionKind;
import sysmlinjava.valuetypes.DurationMilliseconds;

/**
 * The {@code C4S2OperatorStateMachine} is the SysMLinJava model of the state
 * machine for a C4S2 system operator - human or machine. The state machine has
 * 6 states that essentially implement the find/fix/track/target/engage/assess
 * (F2T2EA) process. The states are initializing, configuring,
 * find/fix/track/Target (F2T2), engage (E), assess (A) and finalizing. See the
 * state machine's states and transitions for definition of this behavior.
 * 
 * @author ModelerOne
 *
 *         Known users:
 * @see c4s2.users.C4S2Operator
 */
public class C4S2OperatorStateMachine extends SysMLStateMachine
{
	@State
	public SysMLState initializingState;
	@State
	public SysMLState configuringState;
	@State
	public SysMLState findFixTrackTargetingState;
	@State
	public SysMLState engagingState;
	@State
	public SysMLState assessingState;
	@State
	public SysMLState finalizingState;

	@OnEnterActivity
	public SysMLOnEnterActivity initializingOnEnterActivity;
	@OnEnterActivity
	public SysMLOnEnterActivity configuringOnEnterActivity;
	@OnEnterActivity
	public SysMLOnEnterActivity findFixTrackTargetingOnEnterActivity;
	@OnEnterActivity
	public SysMLOnEnterActivity engagingOnEnterActivity;
	@OnEnterActivity
	public SysMLOnEnterActivity assessingOnEnterActivity;
	@OnEnterActivity
	public SysMLOnEnterActivity finalizingOnEnterActivity;

	@OnExitActivity
	public SysMLOnExitActivity configuringOnExitActivity;
	@OnExitActivity
	public SysMLOnExitActivity findFixTrackTargetingOnExitActivity;
	@OnExitActivity
	public SysMLOnExitActivity engagingOnExitActivity;
	@OnExitActivity
	public SysMLOnExitActivity assessingOnExitActivity;
	@OnExitActivity
	public SysMLOnExitActivity finalizingOnExitActivity;

	@Transition
	public InitialTransition initialToInitializingTransition;
	@Transition
	public SysMLTransition initializingToConfiguringTransition;
	@Transition
	public SysMLTransition configuringToFindFixTrackTargetingTransition;
	@Transition
	public SysMLTransition findFixTrackTargetingToEngagingTransition;
	@Transition
	public SysMLTransition engagingToAssessingTransition;
	@Transition
	public SysMLTransition assessingToFinalizingTransition;
	@Transition
	public FinalTransition finalizingToFinalTransition;
	@Transition
	public SysMLTransition onConfurationTimerWhileConfiguringTransition;
	@Transition
	public SysMLTransition onSystemMonitorWhileConfiguringTransition;
	@Transition
	public SysMLTransition onSystemMonitorWhileF2T2ingTransition;
	@Transition
	public SysMLTransition onSystemMonitorWhileEngagingTransition;
	@Transition
	public SysMLTransition onSystemMonitorWhileAssessingTransition;
	@Transition
	public SysMLTransition onSystemMonitorWhileFinalizingTransition;
	@Transition
	public SysMLTransition onRadarMonitorWhileConfiguringTransition;
	@Transition
	public SysMLTransition onRadarMonitorWhileF2T2ingTransition;
	@Transition
	public SysMLTransition onRadarMonitorWhileEngagingTransition;
	@Transition
	public SysMLTransition onRadarMonitorWhileAssessingTransition;
	@Transition
	public SysMLTransition onRadarMonitorWhileFinalizingTransition;
	@Transition
	public SysMLTransition onStrikeMonitorWhileConfiguringTransition;
	@Transition
	public SysMLTransition onStrikeMonitorWhileF2T2ingTransition;
	@Transition
	public SysMLTransition onStrikeMonitorWhileEngagingTransition;
	@Transition
	public SysMLTransition onStrikeMonitorWhileAssessingTransition;
	@Transition
	public SysMLTransition onStrikeMonitorWhileFinalizingTransition;
	@Transition
	public SysMLTransition onTargetMonitorWhileConfiguringTransition;
	@Transition
	public SysMLTransition onTargetMonitorWhileF2T2ingTransition;
	@Transition
	public SysMLTransition onTargetMonitorWhileEngagingTransition;
	@Transition
	public SysMLTransition onTargetMonitorWhileAssessingTransition;
	@Transition
	public SysMLTransition onTargetMonitorWhileFinalizingTransition;

	@GuardCondition
	public SysMLGuardCondition isControlToConfigureGuardCondition;
	@GuardCondition
	public SysMLGuardCondition isControlToFindFixTrackTargetGuardCondition;
	@GuardCondition
	public SysMLGuardCondition isControlToEngageGuardCondition;
	@GuardCondition
	public SysMLGuardCondition isControlToAssessGuardCondition;
	@GuardCondition
	public SysMLGuardCondition isControlToFinalizingGuardCondition;

	@Guard
	public SysMLGuard isControlToConfigureGuard;
	@Guard
	public SysMLGuard isControlToFindFixTrackTargetGuard;
	@Guard
	public SysMLGuard isControlToEngageGuard;
	@Guard
	public SysMLGuard isControlToAssessGuard;
	@Guard
	public SysMLGuard isControlToFinalizingGuard;

	@EffectActivity
	public SysMLEffectActivity onSystemMonitorWhileConfiguringEffectActivity;
	@EffectActivity
	public SysMLEffectActivity onSystemMonitorWhileF2T2ingEffectActivity;
	@EffectActivity
	public SysMLEffectActivity onSystemMonitorWhileEngagingEffectActivity;
	@EffectActivity
	public SysMLEffectActivity onSystemMonitorWhileAssessingEffectActivity;
	@EffectActivity
	public SysMLEffectActivity onSystemMonitorWhileFinalizingEffectActivity;
	@EffectActivity
	public SysMLEffectActivity onRadarMonitorWhileConfiguringEffectActivity;
	@EffectActivity
	public SysMLEffectActivity onRadarMonitorWhileF2T2ingEffectActivity;
	@EffectActivity
	public SysMLEffectActivity onRadarMonitorWhileEngagingEffectActivity;
	@EffectActivity
	public SysMLEffectActivity onRadarMonitorWhileAssessingEffectActivity;
	@EffectActivity
	public SysMLEffectActivity onRadarMonitorWhileFinalizingEffectActivity;
	@EffectActivity
	public SysMLEffectActivity onStrikeMonitorWhileConfiguringEffectActivity;
	@EffectActivity
	public SysMLEffectActivity onStrikeMonitorWhileF2T2ingEffectActivity;
	@EffectActivity
	public SysMLEffectActivity onStrikeMonitorWhileEngagingEffectActivity;
	@EffectActivity
	public SysMLEffectActivity onStrikeMonitorWhileAssessingEffectActivity;
	@EffectActivity
	public SysMLEffectActivity onStrikeMonitorWhileFinalizingEffectActivity;
	@EffectActivity
	public SysMLEffectActivity onTargetMonitorWhileConfiguringEffectActivity;
	@EffectActivity
	public SysMLEffectActivity onTargetMonitorWhileF2T2ingEffectActivity;
	@EffectActivity
	public SysMLEffectActivity onTargetMonitorWhileEngagingEffectActivity;
	@EffectActivity
	public SysMLEffectActivity onTargetMonitorWhileAssessingEffectActivity;
	@EffectActivity
	public SysMLEffectActivity onTargetMonitorWhileFinalizingEffectActivity;
	@EffectActivity
	public SysMLEffectActivity onConfigurationTimerWhileConfiguringEffectActivity;
	@Effect
	public SysMLEffect onSystemMonitorWhileConfiguringEffect;
	@Effect
	public SysMLEffect onSystemMonitorWhileF2T2ingEffect;
	@Effect
	public SysMLEffect onSystemMonitorWhileEngagingEffect;
	@Effect
	public SysMLEffect onSystemMonitorWhileAssessingEffect;
	@Effect
	public SysMLEffect onSystemMonitorWhileFinalizingEffect;
	@Effect
	public SysMLEffect onRadarMonitorWhileConfiguringEffect;
	@Effect
	public SysMLEffect onRadarMonitorWhileF2T2ingEffect;
	@Effect
	public SysMLEffect onRadarMonitorWhileEngagingEffect;
	@Effect
	public SysMLEffect onRadarMonitorWhileAssessingEffect;
	@Effect
	public SysMLEffect onRadarMonitorWhileFinalizingEffect;
	@Effect
	public SysMLEffect onStrikeMonitorWhileConfiguringEffect;
	@Effect
	public SysMLEffect onStrikeMonitorWhileF2T2ingEffect;
	@Effect
	public SysMLEffect onStrikeMonitorWhileEngagingEffect;
	@Effect
	public SysMLEffect onStrikeMonitorWhileAssessingEffect;
	@Effect
	public SysMLEffect onStrikeMonitorWhileFinalizingEffect;
	@Effect
	public SysMLEffect onTargetMonitorWhileConfiguringEffect;
	@Effect
	public SysMLEffect onTargetMonitorWhileF2T2ingEffect;
	@Effect
	public SysMLEffect onTargetMonitorWhileEngagingEffect;
	@Effect
	public SysMLEffect onTargetMonitorWhileAssessingEffect;
	@Effect
	public SysMLEffect onTargetMonitorWhileFinalizingEffect;

	@Effect
	public SysMLEffect onConfigurationTimerWhileConfiguringEffect;
	public static final String maxConfigurationTimerID = "maxConfigurationTimerID";

	public C4S2OperatorStateMachine(C4S2Operator c4s2Operator)
	{
		super(Optional.of(c4s2Operator), true, "C4S2OperatorStateMachine");
	}

	public void startConfigurationTimer(DurationMilliseconds maxConfigurationDuration)
	{
		startTimer(maxConfigurationTimerID, maxConfigurationDuration, DurationMilliseconds.ZERO);
	}

	public void stopConfigurationTimer()
	{
		stopTimer(maxConfigurationTimerID);
	}

	@Override
	protected void createStateOnEnterActivities()
	{
		super.createStateOnEnterActivities();
		initializingOnEnterActivity = (contextBlock) -> ((C4S2Operator)contextBlock.get()).initialize();
		configuringOnEnterActivity = (contextBlock) -> ((C4S2Operator)contextBlock.get()).configure();
		findFixTrackTargetingOnEnterActivity = (contextBlock) -> ((C4S2Operator)contextBlock.get()).findFixTrackTarget();
		engagingOnEnterActivity = (contextBlock) -> ((C4S2Operator)contextBlock.get()).engage();
		assessingOnEnterActivity = (contextBlock) -> ((C4S2Operator)contextBlock.get()).assess();
		finalizingOnEnterActivity = (contextBlock) -> ((C4S2Operator)contextBlock.get()).finalize();
	}

	@Override
	protected void createStateOnExitActivities()
	{
		super.createStateOnExitActivities();
		configuringOnExitActivity = (contextBlock) -> ((C4S2Operator)contextBlock.get()).concludeConfigure();
		findFixTrackTargetingOnExitActivity = (contextBlock) -> ((C4S2Operator)contextBlock.get()).concludeFindFixTrackTarget();
		engagingOnExitActivity = (contextBlock) -> ((C4S2Operator)contextBlock.get()).concludeEngage();
		assessingOnExitActivity = (contextBlock) -> ((C4S2Operator)contextBlock.get()).concludeAssess();
		finalizingOnExitActivity = (contextBlock) -> ((C4S2Operator)contextBlock.get()).concludeFinalize();
	}

	@Override
	protected void createStates()
	{
		super.createStates();
		initializingState = new SysMLState(contextBlock, Optional.of(initializingOnEnterActivity), Optional.empty(), Optional.empty(), "Initializing");
		configuringState = new SysMLState(contextBlock, Optional.of(configuringOnEnterActivity), Optional.empty(), Optional.of(configuringOnExitActivity), "Configuring");
		findFixTrackTargetingState = new SysMLState(contextBlock, Optional.of(findFixTrackTargetingOnEnterActivity), Optional.empty(), Optional.of(findFixTrackTargetingOnExitActivity), "FindFixTrackTargeting");
		engagingState = new SysMLState(contextBlock, Optional.of(engagingOnEnterActivity), Optional.empty(), Optional.of(engagingOnExitActivity), "Engaging");
		assessingState = new SysMLState(contextBlock, Optional.of(assessingOnEnterActivity), Optional.empty(), Optional.of(assessingOnExitActivity), "Assessing");
		finalizingState = new SysMLState(contextBlock, Optional.of(finalizingOnEnterActivity), Optional.empty(), Optional.of(finalizingOnExitActivity), "Finalizing");
	}

	@Override
	protected void createGuardConditions()
	{
		super.createGuardConditions();
		isControlToConfigureGuardCondition = (event, contextBlock) ->
		{
			return ((C4S2OperatorControlEvent)event.get()).getControl().state == C4S2OperatorStatesEnum.Configuring;
		};
		isControlToFindFixTrackTargetGuardCondition = (event, contextBlock) ->
		{
			return ((C4S2OperatorControlEvent)event.get()).getControl().state == C4S2OperatorStatesEnum.FindingFixingTrackingTargeting;
		};
		isControlToEngageGuardCondition = (event, contextBlock) ->
		{
			return ((C4S2OperatorControlEvent)event.get()).getControl().state == C4S2OperatorStatesEnum.Engaging;
		};
		isControlToAssessGuardCondition = (event, contextBlock) ->
		{
			return ((C4S2OperatorControlEvent)event.get()).getControl().state == C4S2OperatorStatesEnum.Assessing;
		};
		isControlToFinalizingGuardCondition = (event, contextBlock) ->
		{
			return ((C4S2OperatorControlEvent)event.get()).getControl().state == C4S2OperatorStatesEnum.Finalizing;
		};
	}

	@Override
	protected void createGuards()
	{
		super.createGuards();
		isControlToConfigureGuard = new SysMLGuard(contextBlock, isControlToConfigureGuardCondition, "isControlToConfigure");
		isControlToFindFixTrackTargetGuard = new SysMLGuard(contextBlock, isControlToFindFixTrackTargetGuardCondition, "isControlToFindFixTrackTarget");
		isControlToEngageGuard = new SysMLGuard(contextBlock, isControlToEngageGuardCondition, "isControlToEngage");
		isControlToAssessGuard = new SysMLGuard(contextBlock, isControlToAssessGuardCondition, "isControlToAssess");
		isControlToFinalizingGuard = new SysMLGuard(contextBlock, isControlToFinalizingGuardCondition, "isControlToFinalizing");
	}

	@Override
	protected void createEffectActivities()
	{
		super.createEffectActivities();
		onSystemMonitorWhileConfiguringEffectActivity = (event, contextBlock) ->
		{
			C4S2Operator operator = (C4S2Operator)contextBlock.get();
			OperatorSystemMonitorView monitorView = ((OperatorSystemMonitorViewEvent)event.get()).getMonitorView();
			operator.onSystemMonitorViewDuringConfiguration(monitorView);
		};
		onSystemMonitorWhileF2T2ingEffectActivity = (event, contextBlock) ->
		{
			C4S2Operator operator = (C4S2Operator)contextBlock.get();
			OperatorSystemMonitorView monitorView = ((OperatorSystemMonitorViewEvent)event.get()).getMonitorView();
			operator.onSystemMonitorViewDuringF2T2ing(monitorView);
		};
		onSystemMonitorWhileEngagingEffectActivity = (event, contextBlock) ->
		{
			C4S2Operator operator = (C4S2Operator)contextBlock.get();
			OperatorSystemMonitorView monitorView = ((OperatorSystemMonitorViewEvent)event.get()).getMonitorView();
			operator.onSystemMonitorViewDuringEngaging(monitorView);
		};
		onSystemMonitorWhileAssessingEffectActivity = (event, contextBlock) ->
		{
			C4S2Operator operator = (C4S2Operator)contextBlock.get();
			OperatorSystemMonitorView monitorView = ((OperatorSystemMonitorViewEvent)event.get()).getMonitorView();
			operator.onSystemMonitorViewDuringAssessing(monitorView);
		};
		onSystemMonitorWhileFinalizingEffectActivity = (event, contextBlock) ->
		{
			C4S2Operator operator = (C4S2Operator)contextBlock.get();
			OperatorSystemMonitorView monitorView = ((OperatorSystemMonitorViewEvent)event.get()).getMonitorView();
			operator.onSystemMonitorViewDuringFinalizing(monitorView);
		};

		onRadarMonitorWhileConfiguringEffectActivity = (event, contextBlock) ->
		{
			C4S2Operator operator = (C4S2Operator)contextBlock.get();
			OperatorRadarMonitorView monitorView = ((OperatorRadarMonitorViewEvent)event.get()).getMonitorView();
			operator.onRadarMonitorViewDuringConfiguration(monitorView);
		};
		onRadarMonitorWhileF2T2ingEffectActivity = (event, contextBlock) ->
		{
			C4S2Operator operator = (C4S2Operator)contextBlock.get();
			OperatorRadarMonitorView monitorView = ((OperatorRadarMonitorViewEvent)event.get()).getMonitorView();
			operator.onRadarMonitorViewDuringF2T2ing(monitorView);
		};
		onRadarMonitorWhileEngagingEffectActivity = (event, contextBlock) ->
		{
			C4S2Operator operator = (C4S2Operator)contextBlock.get();
			OperatorRadarMonitorView monitorView = ((OperatorRadarMonitorViewEvent)event.get()).getMonitorView();
			operator.onRadarMonitorViewDuringEngaging(monitorView);
		};
		onRadarMonitorWhileAssessingEffectActivity = (event, contextBlock) ->
		{
			C4S2Operator operator = (C4S2Operator)contextBlock.get();
			OperatorRadarMonitorView monitorView = ((OperatorRadarMonitorViewEvent)event.get()).getMonitorView();
			operator.onRadarMonitorViewDuringAssessing(monitorView);
		};
		onRadarMonitorWhileFinalizingEffectActivity = (event, contextBlock) ->
		{
			C4S2Operator operator = (C4S2Operator)contextBlock.get();
			OperatorRadarMonitorView monitorView = ((OperatorRadarMonitorViewEvent)event.get()).getMonitorView();
			operator.onRadarMonitorViewDuringFinalizing(monitorView);
		};

		onStrikeMonitorWhileConfiguringEffectActivity = (event, contextBlock) ->
		{
			C4S2Operator operator = (C4S2Operator)contextBlock.get();
			OperatorStrikeMonitorView monitorView = ((OperatorStrikeMonitorViewEvent)event.get()).getMonitorView();
			operator.onStrikeMonitorViewDuringConfiguration(monitorView);
		};
		onStrikeMonitorWhileF2T2ingEffectActivity = (event, contextBlock) ->
		{
			C4S2Operator operator = (C4S2Operator)contextBlock.get();
			OperatorStrikeMonitorView monitorView = ((OperatorStrikeMonitorViewEvent)event.get()).getMonitorView();
			operator.onStrikeMonitorViewDuringF2T2ing(monitorView);
		};
		onStrikeMonitorWhileEngagingEffectActivity = (event, contextBlock) ->
		{
			C4S2Operator operator = (C4S2Operator)contextBlock.get();
			OperatorStrikeMonitorView monitorView = ((OperatorStrikeMonitorViewEvent)event.get()).getMonitorView();
			operator.onStrikeMonitorViewDuringEngaging(monitorView);
		};
		onStrikeMonitorWhileAssessingEffectActivity = (event, contextBlock) ->
		{
			C4S2Operator operator = (C4S2Operator)contextBlock.get();
			OperatorStrikeMonitorView monitorView = ((OperatorStrikeMonitorViewEvent)event.get()).getMonitorView();
			operator.onStrikeMonitorViewDuringAssessing(monitorView);
		};
		onStrikeMonitorWhileFinalizingEffectActivity = (event, contextBlock) ->
		{
			C4S2Operator operator = (C4S2Operator)contextBlock.get();
			OperatorStrikeMonitorView monitorView = ((OperatorStrikeMonitorViewEvent)event.get()).getMonitorView();
			operator.onStrikeMonitorViewDuringFinalizing(monitorView);
		};

		onTargetMonitorWhileConfiguringEffectActivity = (event, contextBlock) ->
		{
			C4S2Operator operator = (C4S2Operator)contextBlock.get();
			OperatorTargetMonitorView monitorView = ((OperatorTargetMonitorViewEvent)event.get()).getMonitorView();
			operator.onTargetMonitorViewDuringConfiguration(monitorView);
		};
		onTargetMonitorWhileF2T2ingEffectActivity = (event, contextBlock) ->
		{
			C4S2Operator operator = (C4S2Operator)contextBlock.get();
			OperatorTargetMonitorView monitorView = ((OperatorTargetMonitorViewEvent)event.get()).getMonitorView();
			operator.onTargetMonitorViewDuringF2T2ing(monitorView);
		};
		onTargetMonitorWhileEngagingEffectActivity = (event, contextBlock) ->
		{
			C4S2Operator operator = (C4S2Operator)contextBlock.get();
			OperatorTargetMonitorView monitorView = ((OperatorTargetMonitorViewEvent)event.get()).getMonitorView();
			operator.onTargetMonitorViewDuringEngaging(monitorView);
		};
		onTargetMonitorWhileAssessingEffectActivity = (event, contextBlock) ->
		{
			C4S2Operator operator = (C4S2Operator)contextBlock.get();
			OperatorTargetMonitorView monitorView = ((OperatorTargetMonitorViewEvent)event.get()).getMonitorView();
			operator.onTargetMonitorViewDuringAssessing(monitorView);
		};
		onTargetMonitorWhileFinalizingEffectActivity = (event, contextBlock) ->
		{
			C4S2Operator operator = (C4S2Operator)contextBlock.get();
			OperatorTargetMonitorView monitorView = ((OperatorTargetMonitorViewEvent)event.get()).getMonitorView();
			operator.onTargetMonitorViewDuringFinalizing(monitorView);
		};

		onConfigurationTimerWhileConfiguringEffectActivity = (event, contextBlock) ->
		{
			C4S2Operator operator = (C4S2Operator)contextBlock.get();
			operator.onMaxConfigurationTime();
		};
	}

	@Override
	protected void createEffects()
	{
		super.createEffects();
		onSystemMonitorWhileConfiguringEffect = new SysMLEffect(contextBlock, onSystemMonitorWhileConfiguringEffectActivity, "OnSystemMonitorWhileConfiguring");
		onSystemMonitorWhileF2T2ingEffect = new SysMLEffect(contextBlock, onSystemMonitorWhileF2T2ingEffectActivity, "OnSystemMonitorWhileF2T2ing");
		onSystemMonitorWhileEngagingEffect = new SysMLEffect(contextBlock, onSystemMonitorWhileEngagingEffectActivity, "OnSystemMonitorWhileEngaging");
		onSystemMonitorWhileAssessingEffect = new SysMLEffect(contextBlock, onSystemMonitorWhileAssessingEffectActivity, "OnSystemMonitorWhileAssessing");
		onSystemMonitorWhileFinalizingEffect = new SysMLEffect(contextBlock, onSystemMonitorWhileFinalizingEffectActivity, "OnSystemMonitorWhileFinalizing");
		onRadarMonitorWhileConfiguringEffect = new SysMLEffect(contextBlock, onRadarMonitorWhileConfiguringEffectActivity, "OnRadarMonitorWhileConfiguring");
		onRadarMonitorWhileF2T2ingEffect = new SysMLEffect(contextBlock, onRadarMonitorWhileF2T2ingEffectActivity, "OnRadarMonitorWhileF2T2ing");
		onRadarMonitorWhileEngagingEffect = new SysMLEffect(contextBlock, onRadarMonitorWhileEngagingEffectActivity, "OnRadarMonitorWhileEngaging");
		onRadarMonitorWhileAssessingEffect = new SysMLEffect(contextBlock, onRadarMonitorWhileAssessingEffectActivity, "OnRadarMonitorWhileAssessing");
		onRadarMonitorWhileFinalizingEffect = new SysMLEffect(contextBlock, onRadarMonitorWhileFinalizingEffectActivity, "OnRadarMonitorWhileFinalizing");
		onStrikeMonitorWhileConfiguringEffect = new SysMLEffect(contextBlock, onStrikeMonitorWhileConfiguringEffectActivity, "OnStrikeMonitorWhileConfiguring");
		onStrikeMonitorWhileF2T2ingEffect = new SysMLEffect(contextBlock, onStrikeMonitorWhileF2T2ingEffectActivity, "OnStrikeMonitorWhileF2T2ing");
		onStrikeMonitorWhileEngagingEffect = new SysMLEffect(contextBlock, onStrikeMonitorWhileEngagingEffectActivity, "OnStrikeMonitorWhileEngaging");
		onStrikeMonitorWhileAssessingEffect = new SysMLEffect(contextBlock, onStrikeMonitorWhileAssessingEffectActivity, "OnStrikeMonitorWhileAssessing");
		onStrikeMonitorWhileFinalizingEffect = new SysMLEffect(contextBlock, onStrikeMonitorWhileFinalizingEffectActivity, "OnStrikeMonitorWhileFinalizing");
		onTargetMonitorWhileConfiguringEffect = new SysMLEffect(contextBlock, onTargetMonitorWhileConfiguringEffectActivity, "OnTargetMonitorWhileConfiguring");
		onTargetMonitorWhileF2T2ingEffect = new SysMLEffect(contextBlock, onTargetMonitorWhileF2T2ingEffectActivity, "OnTargetMonitorWhileF2T2ing");
		onTargetMonitorWhileEngagingEffect = new SysMLEffect(contextBlock, onTargetMonitorWhileEngagingEffectActivity, "OnTargetMonitorWhileEngaging");
		onTargetMonitorWhileAssessingEffect = new SysMLEffect(contextBlock, onTargetMonitorWhileAssessingEffectActivity, "OnTargetMonitorWhileAssessing");
		onTargetMonitorWhileFinalizingEffect = new SysMLEffect(contextBlock, onTargetMonitorWhileFinalizingEffectActivity, "OnTargetMonitorWhileFinalizing");
		onConfigurationTimerWhileConfiguringEffect = new SysMLEffect(contextBlock, onConfigurationTimerWhileConfiguringEffectActivity, "onConfigurationTimerWhileConfiguring");
	}

	@Override
	protected void createTransitions()
	{
		initialToInitializingTransition = new InitialTransition(contextBlock, initialState, initializingState, "InitialToInitializing");
		initializingToConfiguringTransition = new SysMLTransition(contextBlock, initializingState, configuringState, Optional.of(C4S2OperatorControlEvent.class), Optional.of(isControlToConfigureGuard), Optional.empty(),
			"InitializingToConfiguring", SysMLTransitionKind.external);
		configuringToFindFixTrackTargetingTransition = new SysMLTransition(contextBlock, configuringState, findFixTrackTargetingState, Optional.of(C4S2OperatorControlEvent.class), Optional.of(isControlToFindFixTrackTargetGuard),
			Optional.empty(), "ConfiguringToFixTrackTargetinging", SysMLTransitionKind.external);
		findFixTrackTargetingToEngagingTransition = new SysMLTransition(contextBlock, findFixTrackTargetingState, engagingState, Optional.of(C4S2OperatorControlEvent.class), Optional.of(isControlToEngageGuard), Optional.empty(),
			"FixTrackTargetingingToTrackingTargeting", SysMLTransitionKind.external);
		engagingToAssessingTransition = new SysMLTransition(contextBlock, engagingState, assessingState, Optional.of(C4S2OperatorControlEvent.class), Optional.of(isControlToAssessGuard), Optional.empty(), "StrikingToAssessing",
			SysMLTransitionKind.external);
		assessingToFinalizingTransition = new SysMLTransition(contextBlock, assessingState, finalizingState, Optional.of(C4S2OperatorControlEvent.class), Optional.of(isControlToFinalizingGuard), Optional.empty(), "AssessingToFinalizing",
			SysMLTransitionKind.external);
		finalizingToFinalTransition = new FinalTransition(contextBlock, finalizingState, finalState, "FinalizingToFinal");

		onSystemMonitorWhileConfiguringTransition = new SysMLTransition(contextBlock, configuringState, configuringState, Optional.of(OperatorSystemMonitorViewEvent.class), Optional.empty(),
			Optional.of(onSystemMonitorWhileConfiguringEffect), "OnSystemMonitoringWhileConfiguring", SysMLTransitionKind.internal);
		onSystemMonitorWhileF2T2ingTransition = new SysMLTransition(contextBlock, findFixTrackTargetingState, findFixTrackTargetingState, Optional.of(OperatorSystemMonitorViewEvent.class), Optional.empty(),
			Optional.of(onSystemMonitorWhileF2T2ingEffect), "OnSystemMonitoringWhileF2T2", SysMLTransitionKind.internal);
		onSystemMonitorWhileEngagingTransition = new SysMLTransition(contextBlock, engagingState, engagingState, Optional.of(OperatorSystemMonitorViewEvent.class), Optional.empty(), Optional.of(onSystemMonitorWhileEngagingEffect),
			"OnSystemMonitoringWhileEngaging", SysMLTransitionKind.internal);
		onSystemMonitorWhileAssessingTransition = new SysMLTransition(contextBlock, assessingState, assessingState, Optional.of(OperatorSystemMonitorViewEvent.class), Optional.empty(), Optional.of(onSystemMonitorWhileAssessingEffect),
			"OnSystemMonitoringWhileAssessing", SysMLTransitionKind.internal);
		onSystemMonitorWhileFinalizingTransition = new SysMLTransition(contextBlock, finalizingState, finalizingState, Optional.of(OperatorSystemMonitorViewEvent.class), Optional.empty(), Optional.of(onSystemMonitorWhileFinalizingEffect),
			"OnSystemMonitoringWhileFinalizing", SysMLTransitionKind.internal);

		onRadarMonitorWhileConfiguringTransition = new SysMLTransition(contextBlock, configuringState, configuringState, Optional.of(OperatorRadarMonitorViewEvent.class), Optional.empty(), Optional.of(onRadarMonitorWhileConfiguringEffect),
			"OnRadarMonitoringWhileConfiguring", SysMLTransitionKind.internal);
		onRadarMonitorWhileF2T2ingTransition = new SysMLTransition(contextBlock, findFixTrackTargetingState, findFixTrackTargetingState, Optional.of(OperatorRadarMonitorViewEvent.class), Optional.empty(),
			Optional.of(onRadarMonitorWhileF2T2ingEffect), "OnRadarMonitoringWhileF2T2", SysMLTransitionKind.internal);
		onRadarMonitorWhileEngagingTransition = new SysMLTransition(contextBlock, engagingState, engagingState, Optional.of(OperatorRadarMonitorViewEvent.class), Optional.empty(), Optional.of(onRadarMonitorWhileEngagingEffect),
			"OnRadarMonitoringWhileEngaging", SysMLTransitionKind.internal);
		onRadarMonitorWhileAssessingTransition = new SysMLTransition(contextBlock, assessingState, assessingState, Optional.of(OperatorRadarMonitorViewEvent.class), Optional.empty(), Optional.of(onRadarMonitorWhileAssessingEffect),
			"OnRadarMonitoringWhileAssessing", SysMLTransitionKind.internal);
		onRadarMonitorWhileFinalizingTransition = new SysMLTransition(contextBlock, finalizingState, finalizingState, Optional.of(OperatorRadarMonitorViewEvent.class), Optional.empty(), Optional.of(onRadarMonitorWhileFinalizingEffect),
			"OnRadarMonitoringWhileFinalizing", SysMLTransitionKind.internal);

		onStrikeMonitorWhileConfiguringTransition = new SysMLTransition(contextBlock, configuringState, configuringState, Optional.of(OperatorStrikeMonitorViewEvent.class), Optional.empty(),
			Optional.of(onStrikeMonitorWhileConfiguringEffect), "OnStrikeMonitoringWhileConfiguring", SysMLTransitionKind.internal);
		onStrikeMonitorWhileF2T2ingTransition = new SysMLTransition(contextBlock, findFixTrackTargetingState, findFixTrackTargetingState, Optional.of(OperatorStrikeMonitorViewEvent.class), Optional.empty(),
			Optional.of(onStrikeMonitorWhileF2T2ingEffect), "OnStrikeMonitoringWhileF2T2", SysMLTransitionKind.internal);
		onStrikeMonitorWhileEngagingTransition = new SysMLTransition(contextBlock, engagingState, engagingState, Optional.of(OperatorStrikeMonitorViewEvent.class), Optional.empty(), Optional.of(onStrikeMonitorWhileEngagingEffect),
			"OnStrikeMonitoringWhileEngaging", SysMLTransitionKind.internal);
		onStrikeMonitorWhileAssessingTransition = new SysMLTransition(contextBlock, assessingState, assessingState, Optional.of(OperatorStrikeMonitorViewEvent.class), Optional.empty(), Optional.of(onStrikeMonitorWhileAssessingEffect),
			"OnStrikeMonitoringWhileAssessing", SysMLTransitionKind.internal);
		onStrikeMonitorWhileFinalizingTransition = new SysMLTransition(contextBlock, finalizingState, finalizingState, Optional.of(OperatorStrikeMonitorViewEvent.class), Optional.empty(), Optional.of(onStrikeMonitorWhileFinalizingEffect),
			"OnStrikeMonitoringWhileFinalizing", SysMLTransitionKind.internal);

		onTargetMonitorWhileConfiguringTransition = new SysMLTransition(contextBlock, configuringState, configuringState, Optional.of(OperatorTargetMonitorViewEvent.class), Optional.empty(),
			Optional.of(onTargetMonitorWhileConfiguringEffect), "OnTargetMonitoringWhileConfiguring", SysMLTransitionKind.internal);
		onTargetMonitorWhileF2T2ingTransition = new SysMLTransition(contextBlock, findFixTrackTargetingState, findFixTrackTargetingState, Optional.of(OperatorTargetMonitorViewEvent.class), Optional.empty(),
			Optional.of(onTargetMonitorWhileF2T2ingEffect), "OnTargetMonitoringWhileF2T2", SysMLTransitionKind.internal);
		onTargetMonitorWhileEngagingTransition = new SysMLTransition(contextBlock, engagingState, engagingState, Optional.of(OperatorTargetMonitorViewEvent.class), Optional.empty(), Optional.of(onTargetMonitorWhileEngagingEffect),
			"OnTargetMonitoringWhileEngaging", SysMLTransitionKind.internal);
		onTargetMonitorWhileAssessingTransition = new SysMLTransition(contextBlock, assessingState, assessingState, Optional.of(OperatorTargetMonitorViewEvent.class), Optional.empty(), Optional.of(onTargetMonitorWhileAssessingEffect),
			"OnTargetMonitoringWhileAssessing", SysMLTransitionKind.internal);
		onTargetMonitorWhileFinalizingTransition = new SysMLTransition(contextBlock, finalizingState, finalizingState, Optional.of(OperatorTargetMonitorViewEvent.class), Optional.empty(), Optional.of(onTargetMonitorWhileFinalizingEffect),
			"OnTargetMonitoringWhileFinalizing", SysMLTransitionKind.internal);

		onConfurationTimerWhileConfiguringTransition = new SysMLTransition(contextBlock, configuringState, finalState, Optional.of(SysMLTimeEvent.class), Optional.empty(), Optional.of(onConfigurationTimerWhileConfiguringEffect),
			"OnConfigurationTimeringWhileConfiguring", SysMLTransitionKind.external);
	}
}
