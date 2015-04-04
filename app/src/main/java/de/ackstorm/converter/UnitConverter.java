package de.ackstorm.converter;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;

class UnitConverter {
	private final ArrayList<Category> mList;
	
	public UnitConverter() {
		Category cat;		

		mList = new ArrayList<>();
    	cat = new Category(R.string.cat_length);
		cat.add(new Unit(R.string.unit_mm, R.string.desc_mm,    0.001));		
		cat.add(new Unit(R.string.unit_m,  R.string.desc_m,     1.0));		
		cat.add(new Unit(R.string.unit_km, R.string.desc_km, 1000.0));
    	cat.add(new Unit(R.string.unit_in, R.string.desc_in,    0.0254));
    	cat.add(new Unit(R.string.unit_ft, R.string.desc_ft,    0.3048));
		cat.add(new Unit(R.string.unit_yd, R.string.desc_yd,    0.9144));
    	cat.add(new Unit(R.string.unit_mi, R.string.desc_mi, 1609.344));
    	cat.add(new Unit(R.string.unit_nmi,R.string.desc_nmi,1853.184));
    	cat.add(new Unit(R.string.unit_ly, R.string.desc_ly,    9.4607304725808e15));
    	mList.add(cat);
    	
    	cat = new Category(R.string.cat_area);
    	cat.add(new Unit(R.string.unit_yd2,  R.string.desc_yd2,      0.83612736));
    	cat.add(new Unit(R.string.unit_m2,  R.string.desc_m2,        1.0));
    	cat.add(new Unit(R.string.unit_ac,  R.string.desc_ac,     4046.8564224));
    	cat.add(new Unit(R.string.unit_ha,  R.string.desc_ha,    10000.0));
    	cat.add(new Unit(R.string.unit_km2, R.string.desc_km2, 1000000.0));
    	cat.add(new Unit(R.string.unit_mi2,  R.string.desc_mi2,  2.589988110336e6));
    	mList.add(cat);
		
    	cat = new Category(R.string.cat_volume);
    	cat.add(new Unit(R.string.unit_m3,      R.string.desc_m3,      1.0));
    	cat.add(new Unit(R.string.unit_l,       R.string.desc_l,       0.001));
    	cat.add(new Unit(R.string.unit_gal_us,  R.string.desc_gal_us,  0.003785411784));
    	cat.add(new Unit(R.string.unit_gal_imp, R.string.desc_gal_imp, 0.00454609));
    	mList.add(cat);
    	
    	cat = new Category(R.string.cat_torque);
    	cat.add(new Unit(R.string.unit_inlbf, R.string.desc_inlbf, 0.1129848290276167));
    	cat.add(new Unit(R.string.unit_nm,    R.string.desc_nm,    1.0));
    	cat.add(new Unit(R.string.unit_ftlbf, R.string.desc_ftlbf, 1.3558179483314004));
    	mList.add(cat);
          	
    	cat = new Category(R.string.cat_mass);
    	cat.add(new Unit(R.string.unit_kg,  R.string.desc_kg, 1.0));
    	cat.add(new Unit(R.string.unit_lb,  R.string.desc_lb, 0.45359237));
    	cat.add(new Unit(R.string.unit_oz,  R.string.desc_oz, 0.028));
    	mList.add(cat);
    	
    	cat = new Category(R.string.cat_temperature);
    	cat.add(new Unit(R.string.unit_celsius,    R.string.desc_celsius,        1.0,    0.0));
    	cat.add(new Unit(R.string.unit_fahrenheit, R.string.desc_fahrenheit, 5.0/9.0,  -32.0));
    	cat.add(new Unit(R.string.unit_kelvin, R.string.desc_kelvin,             1.0, -273.15));
    	mList.add(cat);

    	cat = new Category(R.string.cat_speed);
    	cat.add(new Unit(R.string.unit_mps,    R.string.desc_mps,        1.0));
    	cat.add(new Unit(R.string.unit_kmh,    R.string.desc_kmh,    1.0/3.6));
    	cat.add(new Unit(R.string.unit_mph,    R.string.desc_mph,    0.44704));
    	cat.add(new Unit(R.string.unit_knot,   R.string.desc_knot,  0.514444));
    	mList.add(cat);

        cat = new Category(R.string.cat_currency);
        cat.add(new Unit(R.string.unit_euro,    R.string.desc_euro,        1.0000));
        cat.add(new Unit(R.string.unit_dollar,  R.string.desc_dollar,      0.8861));
        cat.add(new Unit(R.string.unit_rupee,   R.string.desc_rupee,       0.0142));
        mList.add(cat);

	}

	public ArrayList<Integer> getCategories() {
		ArrayList<Integer> array = new ArrayList<>();
		for (Category c : mList) {
			array.add(c.getCategoryId());
		}
		return array;
	}
	
	public ArrayList<Unit> getUnits(int pos) {
		ArrayList<Unit> array = new ArrayList<>();
		for (Unit u : mList.get(pos).getList()) {
			array.add(u);
		}
		return array;
	}
			
	public ArrayList<UnitValue> convert(int category, int unit, Double inputValue){
		ArrayList<UnitValue> output = new ArrayList<>();
		ArrayList<Unit> list;
		Unit inputUnit;
		double result;

		list = mList.get(category).getList();
		inputUnit = list.get(unit);

		for (Unit u : list) {
			if (u != inputUnit) {
				result = ((inputValue + inputUnit.getOffset())/ u.getFactor() * inputUnit.getFactor()) - u.getOffset();
				output.add(new UnitValue(result, u.getUnitId()));
			}
		}
		return output;
	}
	
	public static class UnitValue extends SimpleEntry<Double, Integer> {

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
	
	public static class Category {
		private final int mCategoryId;
		private final ArrayList<Unit> mList;

		public Category(int res) {
			mCategoryId = res;
			mList = new ArrayList<>();
		}
		
		/**
		 * @return string resource id of the category
		 */
		public int getCategoryId() {
			return mCategoryId;
		}

		public void add(Unit unit) {
			mList.add(unit);
		}
		
		public ArrayList<Unit> getList() {
			return mList;
		}
	}
	
	public static class Unit {
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
}
