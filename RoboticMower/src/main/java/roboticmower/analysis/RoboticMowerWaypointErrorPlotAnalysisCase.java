package roboticmower.analysis;

import java.util.Optional;

import sysmlinjava.views.common.Axis;
import sysmlinjava.views.scatterplots.ScatterPlotAnalysisCase;
import sysmlinjava.views.scatterplots.ScatterPlotDefinition;

/**
 * Scatter plot analysis case for the plotting (analysis) of the error in
 * planned versus actual waypoints of the Robotic Mower. It extends the basic
 * {@code ScatterPlotAnalysisCase} with a simple retrieval of the XY difference
 * between planned and actual waypoints from the
 * {@code RoboticMowerWaypointErrorFrequencyAnalysisCase}. The
 * {@code ScatterPlotANalysisCase} then produces the data needed to generate the
 * scatter plot of the waypoint errors.
 * 
 * @author ModelerOne
 */
public class RoboticMowerWaypointErrorPlotAnalysisCase extends ScatterPlotAnalysisCase
{
	/**
	 * ID of this plot in the scatter plot display
	 */
	static String plotID = "Robotic Mower Waypoint Errors";
	/**
	 * Definition of the X axis for the scatter plot
	 */
	static Axis xAxis = new Axis("Waypoint Error X", "centimeters", Optional.of(-20.0), Optional.of(20.0), 10.0, 2);
	/**
	 * Definition of the Y axis for the scatter plot
	 */
	static Axis yAxis = new Axis("Waypoint Error Y", "centimeters", Optional.of(-20.0), Optional.of(20.0), 10.0, 2);
	/**
	 * Complete definition of the scatter plot
	 */
	static ScatterPlotDefinition plotDefinition = new ScatterPlotDefinition(plotID, yAxis, xAxis);
	/**
	 * Constraint block that contains the waypoint error constraint parameter that
	 * is retrieved for the error plot
	 */
	RoboticMowerWaypointErrorFrequencyAnalysisCase errorFrequency;

	/**
	 * Constructor
	 * 
	 * @param errorFrequency constraint block that contains the waypoint error
	 *                       constraint parameter that will be added to the scatter
	 *                       plot display
	 * @param udpPort        UDP port at which the scatter plot display receives the
	 *                       plot data
	 */
	public RoboticMowerWaypointErrorPlotAnalysisCase(RoboticMowerWaypointErrorFrequencyAnalysisCase errorFrequency, int udpPort)
	{
		super(plotDefinition, udpPort, true);
		this.errorFrequency = errorFrequency;
		errorFrequency.waypointError2D.addAttributeObserver(this);
	}

	/**
	 * Sets the next point to scatter plot from the waypoint error value set in the
	 * {@code RoboticMowerWaypointErrorFrequencyConstraintBlock}
	 * 
	 * @param paramID ID of the constraint parameter for the waypoint error.
	 */
	@Override
	public void onParameterChange(String paramID)
	{
		point.setValue(errorFrequency.waypointError2D.scaled(100));
	}

	@Override
	protected void createResult()
	{
		

	}

	@Override
	protected void createResultEvaluation()
	{
		// TODO Auto-generated method stub

	}

	@Override
	protected void createAnalysisCases()
	{
		// TODO Auto-generated method stub

	}

	@Override
	protected void createSubject()
	{
		// TODO Auto-generated method stub

	}

	@Override
	protected void createObjective()
	{
		// TODO Auto-generated method stub

	}

	@Override
	protected void createActors()
	{
		// TODO Auto-generated method stub

	}
}
