package electriccircuit;

/**
 * Enumeration of the constraint parameters of the constraint blocks for each of
 * the components in the {@code ElectricalCircuit}. Enumerated names are the
 * same as those used in the electrical circuit model described in "SysML
 * Extension for Physical Interaction and Signal Flow Simulation", Object
 * Management Group, Inc., 2018.
 * 
 * @see <a href="https://www.omg.org/spec/SysPhS/1.0/PDF">SysML Extension for
 *      Physical Interaction and Signal Flow Simulation</a>
 * 
 * @author ModelerOne
 *
 */
public enum ParamsEnum
{
	/**
	 * amplitude of voltage in voltage sourc
	 */
	amp,
	/**
	 * ground voltage
	 */
	g,
	/**
	 * resistance of resistor in the RL circuit
	 */
	rl,
	/**
	 * resistance of resistor in the RC circuit
	 */
	rc,
	/**
	 * inductance of inductor in the RL circuit
	 */
	l,
	/**
	 * capacitance of capacitor in the RC circuit
	 */
	c,
	/**
	 * CurrentAmps thru a component
	 */
	i,
	/**
	 * CurrentAmps flowing through the negative pin/port of a component
	 */
	negI,
	/**
	 * CurrentAmps flowing through the positive pin/port of a component
	 */
	posI,
	/**
	 * voltage across a component
	 */
	v,
	/**
	 * voltage at the negative pin/port of a component
	 */
	negV,
	/**
	 * voltage at the positive pin/port of a component
	 */
	posV,
	/**
	 * time of the state of a component in the model execution/simulation
	 */
	time;
}