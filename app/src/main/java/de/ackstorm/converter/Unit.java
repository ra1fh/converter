package de.ackstorm.converter;

public class Unit {
    private final int mUnitId;
    private final int mDescId;
    private final double mFactor;
    private final double mOffset;

    public int getUnitId() {
        return mUnitId;
    }

    public int getDescId() {
        return mDescId;
    }

    public double getFactor() {
        return mFactor;
    }

    public double getOffset() {
        return mOffset;
    }

    public Unit(int unitId, int descId, double factor, double offset) {
        mUnitId = unitId;
        mDescId = descId;
        mFactor = factor;
        mOffset = offset;
    }

    public Unit(int unitId, int descId, double factor) {
        this(unitId, descId, factor, 0);
    }
}
