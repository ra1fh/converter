package de.ackstorm.converter;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;

public class UnitConverter {
	protected ArrayList<Category> mList;
	
	public UnitConverter() {
		Category cat;		

		mList = new ArrayList<Category>();
    	cat = new Category(R.string.cat_length);
		cat.add(new Unit(R.string.unit_m,  R.string.desc_m,     1.0));
		cat.add(new Unit(R.string.unit_km, R.string.desc_km, 1000.0));
		cat.add(new Unit(R.string.unit_yd, R.string.desc_yd,    0.9144));
    	cat.add(new Unit(R.string.unit_in, R.string.desc_in,    0.0254));
    	cat.add(new Unit(R.string.unit_ft, R.string.desc_ft,    0.3048));
    	cat.add(new Unit(R.string.unit_mi, R.string.desc_mi, 1609.344));
    	cat.add(new Unit(R.string.unit_nmi,R.string.desc_nmi,1853.184));
    	mList.add(cat);
		
    	cat = new Category(R.string.cat_torque);
    	cat.add(new Unit(R.string.unit_nm,    R.string.desc_nm,    1.0));
    	cat.add(new Unit(R.string.unit_inlbf, R.string.desc_inlbf, 0.1129848290276167));
    	mList.add(cat);
    	
    	cat = new Category(R.string.cat_mass);
    	cat.add(new Unit(R.string.unit_kg,  R.string.desc_kg, 1.0));
    	cat.add(new Unit(R.string.unit_lb,  R.string.desc_lb, 0.45359237));
    	cat.add(new Unit(R.string.unit_oz,  R.string.desc_oz, 0.028));
    	mList.add(cat);
    	
    	cat = new Category(R.string.cat_volume);
    	cat.add(new Unit(R.string.unit_m3,      R.string.desc_m3,      1.0));
    	cat.add(new Unit(R.string.unit_l,       R.string.desc_l,       0.001));
    	cat.add(new Unit(R.string.unit_gal_us,  R.string.desc_gal_us,  0.003785411784));
    	cat.add(new Unit(R.string.unit_gal_imp, R.string.desc_gal_imp, 0.00454609));
    	mList.add(cat);
    	
    	cat = new Category(R.string.cat_temperature);
    	cat.add(new Unit(R.string.unit_celsius,    R.string.desc_celsius,        1.0,   0.0));
    	cat.add(new Unit(R.string.unit_fahrenheit, R.string.desc_fahrenheit, 5.0/9.0, -32.0));
    	mList.add(cat);

	}

	public ArrayList<Integer> getCategories() {
		ArrayList<Integer> array = new ArrayList<Integer>();
		for (Category c : mList) {
			array.add(c.getCagtoryId());
		}
		return array;
	}
	
	public ArrayList<Unit> getUnits(int pos) {
		ArrayList<Unit> array = new ArrayList<Unit>();
		for (Unit u : mList.get(pos).getList()) {
			array.add(u);
		}
		return array;
	}
			
	public ArrayList<UnitValue> convert(int category, int unit, Double inputValue){
		ArrayList<UnitValue> output = new ArrayList<UnitValue>();
		ArrayList<Unit> list = null;
		Unit inputUnit = null;
		double result = 0;

		list = mList.get(category).getList();
		inputUnit = list.get(unit);

		for (Unit u : list) {
			if (u != inputUnit) {
				result = ((inputValue + inputUnit.getOffset())/ Double.valueOf(u.getFactor()) * inputUnit.getFactor()) - u.getOffset();
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
			this(new SimpleEntry<Double, Integer>(value, unit));
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
		
		private static final long serialVersionUID = 1L;
	}
	
	public static class Category {
		private int mCategoryId;
		private ArrayList<Unit> mList;

		public Category(int res) {
			mCategoryId = res;
			mList = new ArrayList<Unit>();
		}
		
		/**
		 * @return string resource id of the category
		 */
		public int getCagtoryId() {
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
		private int mUnitId;
		private int mDescId;
		private double mFactor;
		private double mOffset;
		
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
