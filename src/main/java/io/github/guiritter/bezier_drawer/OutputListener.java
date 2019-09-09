package io.github.guiritter.bezier_drawer;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JFormattedTextField;

import io.github.guiritter.fit_linear.FitLinear;

/**
 * Updates the desired coordinate range converter
 * when the minimum and maximum values are changed.
 * @author Guilherme Alan Ritter
 */
public final class OutputListener implements PropertyChangeListener{

    /**
     * Range converter.
     */
    private final Wrapper<FitLinear> fitWrapper;

    /**
     * Field monitored by this listener.
     */
    private final JFormattedTextField formattedTextField;

    private final Wrapper<Double> maximumWrapper;

    private final Wrapper<Double> minimumWrapper;

    /**
     * Will be equal to either the minimum or the maximum.
     */
    private final Wrapper<Integer> outputWrapper;

    private Double valueDouble;

    private Object valueObject;

    private final Wrapper<Double> valueWrapper;

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        valueObject = formattedTextField.getValue();
        if (valueObject instanceof Long) {
            valueDouble = ((Long) valueObject).doubleValue();
        } else {
            valueDouble = (Double) valueObject;
        }
        valueWrapper.value = valueDouble;
        if ((minimumWrapper.value == null) || (maximumWrapper.value == null)) {
            fitWrapper.value = null;
        } else {
            fitWrapper.value = new FitLinear(0, minimumWrapper.value, outputWrapper.value.doubleValue(), maximumWrapper.value);
        }
    }

    public OutputListener(
     JFormattedTextField formattedTextField,
     Wrapper<Double> valueWrapper,
     Wrapper<Double> minumumWrapper,
     Wrapper<Double> maximumWrapper,
     Wrapper<FitLinear> fitWrapper,
     Wrapper<Integer> outputWrapper) {
        this.formattedTextField = formattedTextField;
        this.valueWrapper = valueWrapper;
        this.minimumWrapper = minumumWrapper;
        this.maximumWrapper = maximumWrapper;
        this.fitWrapper = fitWrapper;
        this.outputWrapper = outputWrapper;
    }
}
