package de.ackstorm.converter;

import java.util.AbstractMap.SimpleEntry;

public class UnitValue extends SimpleEntry<Double, Integer> {

    public UnitValue(SimpleEntry<Double, Integer> value) {
        super(value);
    }

    public UnitValue(Double value, Integer unit) {
        this(new SimpleEntry<>(value, unit));
    }

    public Integer getUnit() {
        return super.getValue();
    }

    public Double getVal() {
        return super.getKey();
    }

    public String toString() {
        return getVal().toString();
    }

    // --Commented out by Inspection (3/25/15 12:49 PM):private static final long serialVersionUID = 1L;
}