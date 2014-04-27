package de.ackstorm.converter;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import android.content.Context;

public class UnitConverter {
	protected Map<String, Integer> mCategoryMap;
	protected static Map<String, LinkedHashMap<String, Double>> mConverterMap;
	
	public UnitConverter(Context c) {
    	mCategoryMap = new HashMap<String, Integer>();
    	mCategoryMap.put(c.getString(R.string.cat_torque), R.array.torque_array);
    	mCategoryMap.put(c.getString(R.string.cat_mass),   R.array.mass_array);
    	mCategoryMap.put(c.getString(R.string.cat_length), R.array.length_array);
    	mCategoryMap.put(c.getString(R.string.cat_volume), R.array.volume_array);
		
    	mConverterMap = new HashMap<String, LinkedHashMap<String, Double>>();
    	
    	LinkedHashMap<String, Double> unitMap;
    	unitMap = new LinkedHashMap<String, Double>();
    	
    	unitMap.put(c.getString(R.string.unit_m),     1.0);
    	unitMap.put(c.getString(R.string.unit_km), 1000.0);
    	unitMap.put(c.getString(R.string.unit_yd),    0.9144);
    	unitMap.put(c.getString(R.string.unit_in),    0.0254);
    	unitMap.put(c.getString(R.string.unit_ft),    0.3048);
    	unitMap.put(c.getString(R.string.unit_mi), 1609.344);
    	unitMap.put(c.getString(R.string.unit_nmi),1853.184);
    	mConverterMap.put(c.getString(R.string.cat_length), unitMap);
    	
    	unitMap = new LinkedHashMap<String, Double>();
    	unitMap.put(c.getString(R.string.unit_nm),    1.0);
    	unitMap.put(c.getString(R.string.unit_inlbf), 0.1129848290276167);
    	mConverterMap.put(c.getString(R.string.cat_torque), unitMap);
    	
    	unitMap = new LinkedHashMap<String, Double>();
    	unitMap.put(c.getString(R.string.unit_m3), 1.0);
    	unitMap.put(c.getString(R.string.unit_l),  0.001);
    	unitMap.put(c.getString(R.string.unit_gal_us), 0.003785411784);
    	unitMap.put(c.getString(R.string.unit_gal_imp), 0.00454609);
    	mConverterMap.put(c.getString(R.string.cat_volume), unitMap);

    	unitMap = new LinkedHashMap<String, Double>();
    	unitMap.put(c.getString(R.string.unit_kg), 1.0);
    	unitMap.put(c.getString(R.string.unit_lb),  0.45359237);
    	unitMap.put(c.getString(R.string.unit_oz),  0.028);
    	mConverterMap.put(c.getString(R.string.cat_mass), unitMap);

	}

	public Boolean containsCategory(String category){
		return mCategoryMap.containsKey(category);
	}
	
	public Integer getCategoryId(String category) {
		return mCategoryMap.get(category);
	}
	
	public String convert(String category, String unit, Double value){
		String output = "";
		double inputFactor;
		double result = 0;

		if (mCategoryMap.containsKey(category)) {
			LinkedHashMap<String, Double> unitMap;
			unitMap = mConverterMap.get(category);
			inputFactor = unitMap.get(unit);
			
			for (Map.Entry<String, Double> entry : unitMap.entrySet()) {
				String k = entry.getKey();
				double v = entry.getValue();
				if (!k.equals(unit)) {
					result = value / Double.valueOf(v) * inputFactor;
					output += Double.toString(result) + " " + k + "\n";
				}
			}
		}
		return output;
	}
}
