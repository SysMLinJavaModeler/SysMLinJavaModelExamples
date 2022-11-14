package electriccircuit;

/**
 * Enumeration of the constraint parameters of the constraint blocks for each of
 * the components in the {@code ElectricalCircuit}
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
	 * current thru a component
	 */
	i,
	/**
	 * current flowing through the negative pin/port of a component
	 */
	negI,
	/**
	 * current flowing through the positive pin/port of a component
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