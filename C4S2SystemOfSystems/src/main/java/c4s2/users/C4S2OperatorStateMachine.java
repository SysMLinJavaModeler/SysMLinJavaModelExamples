
package c4s2.users;

import java.util.Optional;

import c4s2.common.attributetypes.C4S2OperatorStatesEnum;
import c4s2.common.items.information.OperatorRadarMonitorView;
import c4s2.common.items.information.OperatorStrikeMonitorView;
import c4s2.common.items.information.OperatorSystemMonitorView;
import c4s2.common.items.information.OperatorTargetMonitorView;
import c4s2.common.signals.OperatorControlSignal;
import c4s2.common.signals.OperatorRadarMonitorViewSignal;
import c4s2.common.signals.OperatorStrikeMonitorViewSignal;
import c4s2.common.signals.OperatorSystemMonitorViewSignal;
import c4s2.common.signals.OperatorTargetMonitorViewSignal;
import sysmlinjava.attributetypes.DurationMilliseconds;
import sysmlinjava.events.SysMLSignalEvent;
import sysmlinjava.events.SysMLTimeEvent;
import sysmlinjava.javaannotations.statemachines.Effect;
import sysmlinjava.javaannotations.statemachines.EffectActionFunction;
import sysmlinjava.javaannotations.statemachines.Guard;
import sysmlinjava.javaannotations.statemachines.GuardCondition;
import sysmlinjava.javaannotations.statemachines.EntryActionFunction;
import sysmlinjava.javaannotations.statemachines.ExitActionFunction;
import sysmlinjava.javaannotations.statemachines.State;
import sysmlinjava.javaannotations.statemachines.Transition;
import sysmlinjava.states.FinalTransition;
import sysmlinjava.states.InitialTransition;
import sysmlinjava.states.SysMLEffect;
import sysmlinjava.states.SysMLEffectActionFunction;
import sysmlinjava.states.SysMLEntryActionFunction;
import sysmlinjava.states.SysMLExitActionFunction;
import sysmlinjava.states.SysMLGuard;
import sysmlinjava.states.SysMLGuardCondition;
import sysmlinjava.states.SysMLState;
import sysmlinjava.states.SysMLStateMachine;
import sysmlinjava.states.SysMLTransition;
import sysmlinjava.states.SysMLTransitionKind;

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

	@EntryActionFunction
	public SysMLEntryActionFunction initializingOnEnterAction;
	@EntryActionFunction
	public SysMLEntryActionFunction configuringOnEnterAction;
	@EntryActionFunction
	public SysMLEntryActionFunction findFixTrackTargetingOnEnterAction;
	@EntryActionFunction
	public SysMLEntryActionFunction engagingOnEnterAction;
	@EntryActionFunction
	public SysMLEntryActionFunction assessingOnEnterAction;
	@EntryActionFunction
	public SysMLEntryActionFunction finalizingOnEnterAction;

	@ExitActionFunction
	public SysMLExitActionFunction configuringOnExitAction;
	@ExitActionFunction
	public SysMLExitActionFunction findFixTrackTargetingOnExitAction;
	@ExitActionFunction
	public SysMLExitActionFunction engagingOnExitAction;
	@ExitActionFunction
	public SysMLExitActionFunction assessingOnExitAction;
	@ExitActionFunction
	public SysMLExitActionFunction finalizingOnExitAction;

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
	@GuardCondition
	public SysMLGuardCondition isSystemMonitorGuardCondition;
	@GuardCondition
	public SysMLGuardCondition isRadarMonitorGuardCondition;
	@GuardCondition
	public SysMLGuardCondition isStrikeMonitorGuardCondition;
	@GuardCondition
	public SysMLGuardCondition isTargetMonitorGuardCondition;

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
	@Guard
	public SysMLGuard isSystemMonitorGuard;
	@Guard
	public SysMLGuard isRadarMonitorGuard;
	@Guard
	public SysMLGuard isStrikeMonitorGuard;
	@Guard
	public SysMLGuard isTargetMonitorGuard;

	@EffectActionFunction
	public SysMLEffectActionFunction onSystemMonitorWhileConfiguringEffectAction;
	@EffectActionFunction
	public SysMLEffectActionFunction onSystemMonitorWhileF2T2ingEffectAction;
	@EffectActionFunction
	public SysMLEffectActionFunction onSystemMonitorWhileEngagingEffectAction;
	@EffectActionFunction
	public SysMLEffectActionFunction onSystemMonitorWhileAssessingEffectAction;
	@EffectActionFunction
	public SysMLEffectActionFunction onSystemMonitorWhileFinalizingEffectAction;
	@EffectActionFunction
	public SysMLEffectActionFunction onRadarMonitorWhileConfiguringEffectAction;
	@EffectActionFunction
	public SysMLEffectActionFunction onRadarMonitorWhileF2T2ingEffectAction;
	@EffectActionFunction
	public SysMLEffectActionFunction onRadarMonitorWhileEngagingEffectAction;
	@EffectActionFunction
	public SysMLEffectActionFunction onRadarMonitorWhileAssessingEffectAction;
	@EffectActionFunction
	public SysMLEffectActionFunction onRadarMonitorWhileFinalizingEffectAction;
	@EffectActionFunction
	public SysMLEffectActionFunction onStrikeMonitorWhileConfiguringEffectAction;
	@EffectActionFunction
	public SysMLEffectActionFunction onStrikeMonitorWhileF2T2ingEffectAction;
	@EffectActionFunction
	public SysMLEffectActionFunction onStrikeMonitorWhileEngagingEffectAction;
	@EffectActionFunction
	public SysMLEffectActionFunction onStrikeMonitorWhileAssessingEffectAction;
	@EffectActionFunction
	public SysMLEffectActionFunction onStrikeMonitorWhileFinalizingEffectAction;
	@EffectActionFunction
	public SysMLEffectActionFunction onTargetMonitorWhileConfiguringEffectAction;
	@EffectActionFunction
	public SysMLEffectActionFunction onTargetMonitorWhileF2T2ingEffectAction;
	@EffectActionFunction
	public SysMLEffectActionFunction onTargetMonitorWhileEngagingEffectAction;
	@EffectActionFunction
	public SysMLEffectActionFunction onTargetMonitorWhileAssessingEffectAction;
	@EffectActionFunction
	public SysMLEffectActionFunction onTargetMonitorWhileFinalizingEffectAction;
	@EffectActionFunction
	public SysMLEffectActionFunction onConfigurationTimerWhileConfiguringEffectAction;

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
	protected void createStateEntryActionFunctions()
	{
		super.createStateEntryActionFunctions();
		initializingOnEnterAction = (context) -> ((C4S2Operator)context.get()).initialize();
		configuringOnEnterAction = (context) -> ((C4S2Operator)context.get()).configure();
		findFixTrackTargetingOnEnterAction = (context) -> ((C4S2Operator)context.get()).findFixTrackTarget();
		engagingOnEnterAction = (context) -> ((C4S2Operator)context.get()).engage();
		assessingOnEnterAction = (context) -> ((C4S2Operator)context.get()).assess();
		finalizingOnEnterAction = (context) -> ((C4S2Operator)context.get()).finalize();
	}

	@Override
	protected void createStateExitActionFunctions()
	{
		super.createStateExitActionFunctions();
		configuringOnExitAction = (context) -> ((C4S2Operator)context.get()).concludeConfigure();
		findFixTrackTargetingOnExitAction = (context) -> ((C4S2Operator)context.get()).concludeFindFixTrackTarget();
		engagingOnExitAction = (context) -> ((C4S2Operator)context.get()).concludeEngage();
		assessingOnExitAction = (context) -> ((C4S2Operator)context.get()).concludeAssess();
		finalizingOnExitAction = (context) -> ((C4S2Operator)context.get()).concludeFinalize();
	}

	@Override
	protected void createStates()
	{
		super.createStates();
		initializingState = new SysMLState(context, Optional.of(initializingOnEnterAction), Optional.empty(), Optional.empty(), "Initializing");
		configuringState = new SysMLState(context, Optional.of(configuringOnEnterAction), Optional.empty(), Optional.of(configuringOnExitAction), "Configuring");
		findFixTrackTargetingState = new SysMLState(context, Optional.of(findFixTrackTargetingOnEnterAction), Optional.empty(), Optional.of(findFixTrackTargetingOnExitAction), "FindFixTrackTargeting");
		engagingState = new SysMLState(context, Optional.of(engagingOnEnterAction), Optional.empty(), Optional.of(engagingOnExitAction), "Engaging");
		assessingState = new SysMLState(context, Optional.of(assessingOnEnterAction), Optional.empty(), Optional.of(assessingOnExitAction), "Assessing");
		finalizingState = new SysMLState(context, Optional.of(finalizingOnEnterAction), Optional.empty(), Optional.of(finalizingOnExitAction), "Finalizing");
	}

	@Override
	protected void createGuardConditions()
	{
		super.createGuardConditions();
		isControlToConfigureGuardCondition = (event, context) ->
		{
			return event.isPresent() && event.get() instanceof SysMLSignalEvent && ((SysMLSignalEvent)event.get()).signal instanceof OperatorControlSignal
				&& ((OperatorControlSignal)((SysMLSignalEvent)event.get()).signal).control.state == C4S2OperatorStatesEnum.Configuring;
		};
		isControlToFindFixTrackTargetGuardCondition = (event, context) ->
		{
			return event.isPresent() && event.get() instanceof SysMLSignalEvent && ((SysMLSignalEvent)event.get()).signal instanceof OperatorControlSignal
				&& ((OperatorControlSignal)((SysMLSignalEvent)event.get()).signal).control.state == C4S2OperatorStatesEnum.FindingFixingTrackingTargeting;
		};
		isControlToEngageGuardCondition = (event, context) ->
		{
			return event.isPresent() && event.get() instanceof SysMLSignalEvent && ((SysMLSignalEvent)event.get()).signal instanceof OperatorControlSignal
				&& ((OperatorControlSignal)((SysMLSignalEvent)event.get()).signal).control.state == C4S2OperatorStatesEnum.Engaging;
		};
		isControlToAssessGuardCondition = (event, context) ->
		{
			return event.isPresent() && event.get() instanceof SysMLSignalEvent && ((SysMLSignalEvent)event.get()).signal instanceof OperatorControlSignal
				&& ((OperatorControlSignal)((SysMLSignalEvent)event.get()).signal).control.state == C4S2OperatorStatesEnum.Assessing;
		};
		isControlToFinalizingGuardCondition = (event, context) ->
		{
			return event.isPresent() && event.get() instanceof SysMLSignalEvent && ((SysMLSignalEvent)event.get()).signal instanceof OperatorControlSignal
				&& ((OperatorControlSignal)((SysMLSignalEvent)event.get()).signal).control.state == C4S2OperatorStatesEnum.Finalizing;
		};
		isSystemMonitorGuardCondition = (event, context) ->
		{
			return event.isPresent() && event.get() instanceof SysMLSignalEvent && ((SysMLSignalEvent)event.get()).signal instanceof OperatorSystemMonitorViewSignal;
		};
		isRadarMonitorGuardCondition = (event, context) ->
		{
			return event.isPresent() && event.get() instanceof SysMLSignalEvent && ((SysMLSignalEvent)event.get()).signal instanceof OperatorRadarMonitorViewSignal;
		};
		isStrikeMonitorGuardCondition = (event, context) ->
		{
			return event.isPresent() && event.get() instanceof SysMLSignalEvent && ((SysMLSignalEvent)event.get()).signal instanceof OperatorStrikeMonitorViewSignal;
		};
		isTargetMonitorGuardCondition = (event, context) ->
		{
			return event.isPresent() && event.get() instanceof SysMLSignalEvent && ((SysMLSignalEvent)event.get()).signal instanceof OperatorTargetMonitorViewSignal;
		};
	}

	@Override
	protected void createGuards()
	{
		super.createGuards();
		isControlToConfigureGuard = new SysMLGuard(context, isControlToConfigureGuardCondition, "isControlToConfigure");
		isControlToFindFixTrackTargetGuard = new SysMLGuard(context, isControlToFindFixTrackTargetGuardCondition, "isControlToFindFixTrackTarget");
		isControlToEngageGuard = new SysMLGuard(context, isControlToEngageGuardCondition, "isControlToEngage");
		isControlToAssessGuard = new SysMLGuard(context, isControlToAssessGuardCondition, "isControlToAssess");
		isControlToFinalizingGuard = new SysMLGuard(context, isControlToFinalizingGuardCondition, "isControlToFinalizing");
		isSystemMonitorGuard = new SysMLGuard(context, isSystemMonitorGuardCondition, "isSystemMonitorGuardCondition");
		isRadarMonitorGuard = new SysMLGuard(context, isRadarMonitorGuardCondition, "isRadarMonitorGuardCondition");
		isStrikeMonitorGuard = new SysMLGuard(context, isStrikeMonitorGuardCondition, "isStrikeMonitorGuardCondition");
		isTargetMonitorGuard = new SysMLGuard(context, isTargetMonitorGuardCondition, "isTargetMonitorGuardCondition");
	}

	@Override
	protected void createEffectActionFunctions()
	{
		super.createEffectActionFunctions();
		onSystemMonitorWhileConfiguringEffectAction = (event, context) ->
		{
			C4S2Operator operator = (C4S2Operator)context.get();
			OperatorSystemMonitorView monitorView = ((OperatorSystemMonitorViewSignal)((SysMLSignalEvent)event.get()).signal).monitorView;
			operator.onSystemMonitorViewDuringConfiguration(monitorView);
		};
		onSystemMonitorWhileF2T2ingEffectAction = (event, context) ->
		{
			C4S2Operator operator = (C4S2Operator)context.get();
			OperatorSystemMonitorView monitorView = ((OperatorSystemMonitorViewSignal)((SysMLSignalEvent)event.get()).signal).monitorView;
			operator.onSystemMonitorViewDuringF2T2ing(monitorView);
		};
		onSystemMonitorWhileEngagingEffectAction = (event, context) ->
		{
			C4S2Operator operator = (C4S2Operator)context.get();
			OperatorSystemMonitorView monitorView = ((OperatorSystemMonitorViewSignal)((SysMLSignalEvent)event.get()).signal).monitorView;
			operator.onSystemMonitorViewDuringEngaging(monitorView);
		};
		onSystemMonitorWhileAssessingEffectAction = (event, context) ->
		{
			C4S2Operator operator = (C4S2Operator)context.get();
			OperatorSystemMonitorView monitorView = ((OperatorSystemMonitorViewSignal)((SysMLSignalEvent)event.get()).signal).monitorView;
			operator.onSystemMonitorViewDuringAssessing(monitorView);
		};
		onSystemMonitorWhileFinalizingEffectAction = (event, context) ->
		{
			C4S2Operator operator = (C4S2Operator)context.get();
			OperatorSystemMonitorView monitorView = ((OperatorSystemMonitorViewSignal)((SysMLSignalEvent)event.get()).signal).monitorView;
			operator.onSystemMonitorViewDuringFinalizing(monitorView);
		};

		onRadarMonitorWhileConfiguringEffectAction = (event, context) ->
		{
			C4S2Operator operator = (C4S2Operator)context.get();
			OperatorRadarMonitorView monitorView = ((OperatorRadarMonitorViewSignal)((SysMLSignalEvent)event.get()).signal).monitorView;
			operator.onRadarMonitorViewDuringConfiguration(monitorView);
		};
		onRadarMonitorWhileF2T2ingEffectAction = (event, context) ->
		{
			C4S2Operator operator = (C4S2Operator)context.get();
			OperatorRadarMonitorView monitorView = ((OperatorRadarMonitorViewSignal)((SysMLSignalEvent)event.get()).signal).monitorView;
			operator.onRadarMonitorViewDuringF2T2ing(monitorView);
		};
		onRadarMonitorWhileEngagingEffectAction = (event, context) ->
		{
			C4S2Operator operator = (C4S2Operator)context.get();
			OperatorRadarMonitorView monitorView = ((OperatorRadarMonitorViewSignal)((SysMLSignalEvent)event.get()).signal).monitorView;
			operator.onRadarMonitorViewDuringEngaging(monitorView);
		};
		onRadarMonitorWhileAssessingEffectAction = (event, context) ->
		{
			C4S2Operator operator = (C4S2Operator)context.get();
			OperatorRadarMonitorView monitorView = ((OperatorRadarMonitorViewSignal)((SysMLSignalEvent)event.get()).signal).monitorView;
			operator.onRadarMonitorViewDuringAssessing(monitorView);
		};
		onRadarMonitorWhileFinalizingEffectAction = (event, context) ->
		{
			C4S2Operator operator = (C4S2Operator)context.get();
			OperatorRadarMonitorView monitorView = ((OperatorRadarMonitorViewSignal)((SysMLSignalEvent)event.get()).signal).monitorView;
			operator.onRadarMonitorViewDuringFinalizing(monitorView);
		};

		onStrikeMonitorWhileConfiguringEffectAction = (event, context) ->
		{
			C4S2Operator operator = (C4S2Operator)context.get();
			OperatorStrikeMonitorView monitorView = ((OperatorStrikeMonitorViewSignal)((SysMLSignalEvent)event.get()).signal).monitorView;
			operator.onStrikeMonitorViewDuringConfiguration(monitorView);
		};
		onStrikeMonitorWhileF2T2ingEffectAction = (event, context) ->
		{
			C4S2Operator operator = (C4S2Operator)context.get();
			OperatorStrikeMonitorView monitorView = ((OperatorStrikeMonitorViewSignal)((SysMLSignalEvent)event.get()).signal).monitorView;
			operator.onStrikeMonitorViewDuringF2T2ing(monitorView);
		};
		onStrikeMonitorWhileEngagingEffectAction = (event, context) ->
		{
			C4S2Operator operator = (C4S2Operator)context.get();
			OperatorStrikeMonitorView monitorView = ((OperatorStrikeMonitorViewSignal)((SysMLSignalEvent)event.get()).signal).monitorView;
			operator.onStrikeMonitorViewDuringEngaging(monitorView);
		};
		onStrikeMonitorWhileAssessingEffectAction = (event, context) ->
		{
			C4S2Operator operator = (C4S2Operator)context.get();
			OperatorStrikeMonitorView monitorView = ((OperatorStrikeMonitorViewSignal)((SysMLSignalEvent)event.get()).signal).monitorView;
			operator.onStrikeMonitorViewDuringAssessing(monitorView);
		};
		onStrikeMonitorWhileFinalizingEffectAction = (event, context) ->
		{
			C4S2Operator operator = (C4S2Operator)context.get();
			OperatorStrikeMonitorView monitorView = ((OperatorStrikeMonitorViewSignal)((SysMLSignalEvent)event.get()).signal).monitorView;
			operator.onStrikeMonitorViewDuringFinalizing(monitorView);
		};

		onTargetMonitorWhileConfiguringEffectAction = (event, context) ->
		{
			C4S2Operator operator = (C4S2Operator)context.get();
			OperatorTargetMonitorView monitorView = ((OperatorTargetMonitorViewSignal)((SysMLSignalEvent)event.get()).signal).monitorView;
			operator.onTargetMonitorViewDuringConfiguration(monitorView);
		};
		onTargetMonitorWhileF2T2ingEffectAction = (event, context) ->
		{
			C4S2Operator operator = (C4S2Operator)context.get();
			OperatorTargetMonitorView monitorView = ((OperatorTargetMonitorViewSignal)((SysMLSignalEvent)event.get()).signal).monitorView;
			operator.onTargetMonitorViewDuringF2T2ing(monitorView);
		};
		onTargetMonitorWhileEngagingEffectAction = (event, context) ->
		{
			C4S2Operator operator = (C4S2Operator)context.get();
			OperatorTargetMonitorView monitorView = ((OperatorTargetMonitorViewSignal)((SysMLSignalEvent)event.get()).signal).monitorView;
			operator.onTargetMonitorViewDuringEngaging(monitorView);
		};
		onTargetMonitorWhileAssessingEffectAction = (event, context) ->
		{
			C4S2Operator operator = (C4S2Operator)context.get();
			OperatorTargetMonitorView monitorView = ((OperatorTargetMonitorViewSignal)((SysMLSignalEvent)event.get()).signal).monitorView;
			operator.onTargetMonitorViewDuringAssessing(monitorView);
		};
		onTargetMonitorWhileFinalizingEffectAction = (event, context) ->
		{
			C4S2Operator operator = (C4S2Operator)context.get();
			OperatorTargetMonitorView monitorView = ((OperatorTargetMonitorViewSignal)((SysMLSignalEvent)event.get()).signal).monitorView;
			operator.onTargetMonitorViewDuringFinalizing(monitorView);
		};

		onConfigurationTimerWhileConfiguringEffectAction = (event, context) ->
		{
			C4S2Operator operator = (C4S2Operator)context.get();
			operator.onMaxConfigurationTime();
		};
	}

	@Override
	protected void createEffects()
	{
		super.createEffects();
		onSystemMonitorWhileConfiguringEffect = new SysMLEffect(context, onSystemMonitorWhileConfiguringEffectAction, "OnSystemMonitorWhileConfiguring");
		onSystemMonitorWhileF2T2ingEffect = new SysMLEffect(context, onSystemMonitorWhileF2T2ingEffectAction, "OnSystemMonitorWhileF2T2ing");
		onSystemMonitorWhileEngagingEffect = new SysMLEffect(context, onSystemMonitorWhileEngagingEffectAction, "OnSystemMonitorWhileEngaging");
		onSystemMonitorWhileAssessingEffect = new SysMLEffect(context, onSystemMonitorWhileAssessingEffectAction, "OnSystemMonitorWhileAssessing");
		onSystemMonitorWhileFinalizingEffect = new SysMLEffect(context, onSystemMonitorWhileFinalizingEffectAction, "OnSystemMonitorWhileFinalizing");
		onRadarMonitorWhileConfiguringEffect = new SysMLEffect(context, onRadarMonitorWhileConfiguringEffectAction, "OnRadarMonitorWhileConfiguring");
		onRadarMonitorWhileF2T2ingEffect = new SysMLEffect(context, onRadarMonitorWhileF2T2ingEffectAction, "OnRadarMonitorWhileF2T2ing");
		onRadarMonitorWhileEngagingEffect = new SysMLEffect(context, onRadarMonitorWhileEngagingEffectAction, "OnRadarMonitorWhileEngaging");
		onRadarMonitorWhileAssessingEffect = new SysMLEffect(context, onRadarMonitorWhileAssessingEffectAction, "OnRadarMonitorWhileAssessing");
		onRadarMonitorWhileFinalizingEffect = new SysMLEffect(context, onRadarMonitorWhileFinalizingEffectAction, "OnRadarMonitorWhileFinalizing");
		onStrikeMonitorWhileConfiguringEffect = new SysMLEffect(context, onStrikeMonitorWhileConfiguringEffectAction, "OnStrikeMonitorWhileConfiguring");
		onStrikeMonitorWhileF2T2ingEffect = new SysMLEffect(context, onStrikeMonitorWhileF2T2ingEffectAction, "OnStrikeMonitorWhileF2T2ing");
		onStrikeMonitorWhileEngagingEffect = new SysMLEffect(context, onStrikeMonitorWhileEngagingEffectAction, "OnStrikeMonitorWhileEngaging");
		onStrikeMonitorWhileAssessingEffect = new SysMLEffect(context, onStrikeMonitorWhileAssessingEffectAction, "OnStrikeMonitorWhileAssessing");
		onStrikeMonitorWhileFinalizingEffect = new SysMLEffect(context, onStrikeMonitorWhileFinalizingEffectAction, "OnStrikeMonitorWhileFinalizing");
		onTargetMonitorWhileConfiguringEffect = new SysMLEffect(context, onTargetMonitorWhileConfiguringEffectAction, "OnTargetMonitorWhileConfiguring");
		onTargetMonitorWhileF2T2ingEffect = new SysMLEffect(context, onTargetMonitorWhileF2T2ingEffectAction, "OnTargetMonitorWhileF2T2ing");
		onTargetMonitorWhileEngagingEffect = new SysMLEffect(context, onTargetMonitorWhileEngagingEffectAction, "OnTargetMonitorWhileEngaging");
		onTargetMonitorWhileAssessingEffect = new SysMLEffect(context, onTargetMonitorWhileAssessingEffectAction, "OnTargetMonitorWhileAssessing");
		onTargetMonitorWhileFinalizingEffect = new SysMLEffect(context, onTargetMonitorWhileFinalizingEffectAction, "OnTargetMonitorWhileFinalizing");
		onConfigurationTimerWhileConfiguringEffect = new SysMLEffect(context, onConfigurationTimerWhileConfiguringEffectAction, "onConfigurationTimerWhileConfiguring");
	}

	@Override
	protected void createTransitions()
	{
		initialToInitializingTransition = new InitialTransition(context, initialState, initializingState, "InitialToInitializing");
		initializingToConfiguringTransition = new SysMLTransition(context, initializingState, configuringState, Optional.of(SysMLSignalEvent.class), Optional.of(isControlToConfigureGuard), Optional.empty(), "InitializingToConfiguring",
			SysMLTransitionKind.external);
		configuringToFindFixTrackTargetingTransition = new SysMLTransition(context, configuringState, findFixTrackTargetingState, Optional.of(SysMLSignalEvent.class), Optional.of(isControlToFindFixTrackTargetGuard), Optional.empty(),
			"ConfiguringToFixTrackTargetinging", SysMLTransitionKind.external);
		findFixTrackTargetingToEngagingTransition = new SysMLTransition(context, findFixTrackTargetingState, engagingState, Optional.of(SysMLSignalEvent.class), Optional.of(isControlToEngageGuard), Optional.empty(),
			"FixTrackTargetingingToTrackingTargeting", SysMLTransitionKind.external);
		engagingToAssessingTransition = new SysMLTransition(context, engagingState, assessingState, Optional.of(SysMLSignalEvent.class), Optional.of(isControlToAssessGuard), Optional.empty(), "StrikingToAssessing",
			SysMLTransitionKind.external);
		assessingToFinalizingTransition = new SysMLTransition(context, assessingState, finalizingState, Optional.of(SysMLSignalEvent.class), Optional.of(isControlToFinalizingGuard), Optional.empty(), "AssessingToFinalizing",
			SysMLTransitionKind.external);
		finalizingToFinalTransition = new FinalTransition(context, finalizingState, finalState, "FinalizingToFinal");

		onSystemMonitorWhileConfiguringTransition = new SysMLTransition(context, configuringState, configuringState, Optional.of(SysMLSignalEvent.class), Optional.of(isSystemMonitorGuard), Optional.of(onSystemMonitorWhileConfiguringEffect),
			"OnSystemMonitoringWhileConfiguring", SysMLTransitionKind.internal);
		onSystemMonitorWhileF2T2ingTransition = new SysMLTransition(context, findFixTrackTargetingState, findFixTrackTargetingState, Optional.of(SysMLSignalEvent.class), Optional.of(isSystemMonitorGuard),
			Optional.of(onSystemMonitorWhileF2T2ingEffect), "OnSystemMonitoringWhileF2T2", SysMLTransitionKind.internal);
		onSystemMonitorWhileEngagingTransition = new SysMLTransition(context, engagingState, engagingState, Optional.of(SysMLSignalEvent.class), Optional.of(isSystemMonitorGuard), Optional.of(onSystemMonitorWhileEngagingEffect),
			"OnSystemMonitoringWhileEngaging", SysMLTransitionKind.internal);
		onSystemMonitorWhileAssessingTransition = new SysMLTransition(context, assessingState, assessingState, Optional.of(SysMLSignalEvent.class), Optional.of(isSystemMonitorGuard), Optional.of(onSystemMonitorWhileAssessingEffect),
			"OnSystemMonitoringWhileAssessing", SysMLTransitionKind.internal);
		onSystemMonitorWhileFinalizingTransition = new SysMLTransition(context, finalizingState, finalizingState, Optional.of(SysMLSignalEvent.class), Optional.of(isSystemMonitorGuard), Optional.of(onSystemMonitorWhileFinalizingEffect),
			"OnSystemMonitoringWhileFinalizing", SysMLTransitionKind.internal);

		onRadarMonitorWhileConfiguringTransition = new SysMLTransition(context, configuringState, configuringState, Optional.of(SysMLSignalEvent.class), Optional.of(isRadarMonitorGuard), Optional.of(onRadarMonitorWhileConfiguringEffect),
			"OnRadarMonitoringWhileConfiguring", SysMLTransitionKind.internal);
		onRadarMonitorWhileF2T2ingTransition = new SysMLTransition(context, findFixTrackTargetingState, findFixTrackTargetingState, Optional.of(SysMLSignalEvent.class), Optional.of(isRadarMonitorGuard),
			Optional.of(onRadarMonitorWhileF2T2ingEffect), "OnRadarMonitoringWhileF2T2", SysMLTransitionKind.internal);
		onRadarMonitorWhileEngagingTransition = new SysMLTransition(context, engagingState, engagingState, Optional.of(SysMLSignalEvent.class), Optional.of(isRadarMonitorGuard), Optional.of(onRadarMonitorWhileEngagingEffect),
			"OnRadarMonitoringWhileEngaging", SysMLTransitionKind.internal);
		onRadarMonitorWhileAssessingTransition = new SysMLTransition(context, assessingState, assessingState, Optional.of(SysMLSignalEvent.class), Optional.of(isRadarMonitorGuard), Optional.of(onRadarMonitorWhileAssessingEffect),
			"OnRadarMonitoringWhileAssessing", SysMLTransitionKind.internal);
		onRadarMonitorWhileFinalizingTransition = new SysMLTransition(context, finalizingState, finalizingState, Optional.of(SysMLSignalEvent.class), Optional.of(isRadarMonitorGuard), Optional.of(onRadarMonitorWhileFinalizingEffect),
			"OnRadarMonitoringWhileFinalizing", SysMLTransitionKind.internal);

		onStrikeMonitorWhileConfiguringTransition = new SysMLTransition(context, configuringState, configuringState, Optional.of(SysMLSignalEvent.class), Optional.of(isStrikeMonitorGuard), Optional.of(onStrikeMonitorWhileConfiguringEffect),
			"OnStrikeMonitoringWhileConfiguring", SysMLTransitionKind.internal);
		onStrikeMonitorWhileF2T2ingTransition = new SysMLTransition(context, findFixTrackTargetingState, findFixTrackTargetingState, Optional.of(SysMLSignalEvent.class), Optional.of(isStrikeMonitorGuard),
			Optional.of(onStrikeMonitorWhileF2T2ingEffect), "OnStrikeMonitoringWhileF2T2", SysMLTransitionKind.internal);
		onStrikeMonitorWhileEngagingTransition = new SysMLTransition(context, engagingState, engagingState, Optional.of(SysMLSignalEvent.class), Optional.of(isStrikeMonitorGuard), Optional.of(onStrikeMonitorWhileEngagingEffect),
			"OnStrikeMonitoringWhileEngaging", SysMLTransitionKind.internal);
		onStrikeMonitorWhileAssessingTransition = new SysMLTransition(context, assessingState, assessingState, Optional.of(SysMLSignalEvent.class), Optional.of(isStrikeMonitorGuard), Optional.of(onStrikeMonitorWhileAssessingEffect),
			"OnStrikeMonitoringWhileAssessing", SysMLTransitionKind.internal);
		onStrikeMonitorWhileFinalizingTransition = new SysMLTransition(context, finalizingState, finalizingState, Optional.of(SysMLSignalEvent.class), Optional.of(isStrikeMonitorGuard), Optional.of(onStrikeMonitorWhileFinalizingEffect),
			"OnStrikeMonitoringWhileFinalizing", SysMLTransitionKind.internal);

		onTargetMonitorWhileConfiguringTransition = new SysMLTransition(context, configuringState, configuringState, Optional.of(SysMLSignalEvent.class), Optional.of(isTargetMonitorGuard), Optional.of(onTargetMonitorWhileConfiguringEffect),
			"OnTargetMonitoringWhileConfiguring", SysMLTransitionKind.internal);
		onTargetMonitorWhileF2T2ingTransition = new SysMLTransition(context, findFixTrackTargetingState, findFixTrackTargetingState, Optional.of(SysMLSignalEvent.class), Optional.of(isTargetMonitorGuard),
			Optional.of(onTargetMonitorWhileF2T2ingEffect), "OnTargetMonitoringWhileF2T2", SysMLTransitionKind.internal);
		onTargetMonitorWhileEngagingTransition = new SysMLTransition(context, engagingState, engagingState, Optional.of(SysMLSignalEvent.class), Optional.of(isTargetMonitorGuard), Optional.of(onTargetMonitorWhileEngagingEffect),
			"OnTargetMonitoringWhileEngaging", SysMLTransitionKind.internal);
		onTargetMonitorWhileAssessingTransition = new SysMLTransition(context, assessingState, assessingState, Optional.of(SysMLSignalEvent.class), Optional.of(isTargetMonitorGuard), Optional.of(onTargetMonitorWhileAssessingEffect),
			"OnTargetMonitoringWhileAssessing", SysMLTransitionKind.internal);
		onTargetMonitorWhileFinalizingTransition = new SysMLTransition(context, finalizingState, finalizingState, Optional.of(SysMLSignalEvent.class), Optional.of(isTargetMonitorGuard), Optional.of(onTargetMonitorWhileFinalizingEffect),
			"OnTargetMonitoringWhileFinalizing", SysMLTransitionKind.internal);

		onConfurationTimerWhileConfiguringTransition = new SysMLTransition(context, configuringState, finalState, Optional.of(SysMLTimeEvent.class), Optional.empty(), Optional.of(onConfigurationTimerWhileConfiguringEffect),
			"OnConfigurationTimeringWhileConfiguring", SysMLTransitionKind.external);
	}
}
