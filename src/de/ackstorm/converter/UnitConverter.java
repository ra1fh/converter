package de.ackstorm.converter;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;

public class UnitConverter {
	protected ArrayList<Category> mList;
	
	public UnitConverter() {
		Category cat;		

		mList = new ArrayList<Category>();
		
    	cat = new Category(R.string.cat_length);
		cat.add(new Unit(R.string.unit_m,     1.0));
		cat.add(new Unit(R.string.unit_km, 1000.0));
		cat.add(new Unit(R.string.unit_yd,    0.9144));
    	cat.add(new Unit(R.string.unit_in,    0.0254));
    	cat.add(new Unit(R.string.unit_ft,    0.3048));
    	cat.add(new Unit(R.string.unit_mi, 1609.344));
    	cat.add(new Unit(R.string.unit_nmi,1853.184));
    	mList.add(cat);
		
    	cat = new Category(R.string.cat_torque);
    	cat.add(new Unit(R.string.unit_nm,    1.0));
    	cat.add(new Unit(R.string.unit_inlbf, 0.1129848290276167));
    	mList.add(cat);
    	
    	cat = new Category(R.string.cat_mass);
    	cat.add(new Unit(R.string.unit_kg,  1.0));
    	cat.add(new Unit(R.string.unit_lb,  0.45359237));
    	cat.add(new Unit(R.string.unit_oz,  0.028));
    	mList.add(cat);
    	
    	cat = new Category(R.string.cat_volume);
    	cat.add(new Unit(R.string.unit_m3,      1.0));
    	cat.add(new Unit(R.string.unit_l,       0.001));
    	cat.add(new Unit(R.string.unit_gal_us,  0.003785411784));
    	cat.add(new Unit(R.string.unit_gal_imp, 0.00454609));
    	mList.add(cat);
    	
    	cat = new Category(R.string.cat_temperature);
    	cat.add(new Unit(R.string.unit_celsius,    1.0,       0.0));
    	cat.add(new Unit(R.string.unit_fahrenheit, 5.0/9.0, -32.0));
    	mList.add(cat);

	}

	public ArrayList<Integer> getUnits(int pos) {
		ArrayList<Integer> array = new ArrayList<Integer>();
		if (pos == -1) {
			// special case: add resIds for the categories
			for (Category c : mList) {
				array.add(c.getRes());
			}
		} else {
			// return list of units for category at pos
			for (Unit u : mList.get(pos).getList()) {
				array.add(u.getRes());
			}
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
				result = ((inputValue + inputUnit.getOffset())/ Double.valueOf(u.getVal()) * inputUnit.getVal()) - u.getOffset();
				output.add(new UnitValue(result, u.getRes()));
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
		private int mRes;
		private ArrayList<Unit> mList;

		public Category(int res) {
			mRes = res;
			mList = new ArrayList<Unit>();
		}
		
		/**
		 * @return string resource id of the category
		 */
		public int getRes() {
			return mRes;
		}

		public void add(Unit unit) {
			mList.add(unit);
		}
		
		public ArrayList<Unit> getList() {
			return mList;
		}
	}
	
	public static class Unit {
		private int mRes;
		private double mVal;
		private double mOffset;
		
		public int getRes() {
			return mRes;
		}

		public double getVal() {
			return mVal;
		}
		
		public double getOffset() {
			return mOffset;
		}

		public Unit(int res, double val, double offset) {
			mRes = res;
			mVal = val;
			mOffset = offset;
		}

		public Unit(int res, double val) {
			mRes = res;
			mVal = val;
			mOffset = 0;
		}
	}
}
