package de.ackstorm.converter;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;

public class UnitConverter {
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

        cat = new Category(R.string.cat_pressure);
        cat.add(new Unit(R.string.unit_pa, R.string.desc_pa,        1.000));
        cat.add(new Unit(R.string.unit_hpa, R.string.desc_hpa,    100.000));
        cat.add(new Unit(R.string.unit_kpa, R.string.desc_kpa,   1000.000));
        cat.add(new Unit(R.string.unit_bar, R.string.desc_bar, 100000.0));
        cat.add(new Unit(R.string.unit_mbar, R.string.desc_mbar,  100.0));
        cat.add(new Unit(R.string.unit_psi, R.string.desc_psi,   6894.7573));
        mList.add(cat);

        cat = new Category(R.string.cat_energy);
        cat.add(new Unit(R.string.unit_joule, R.string.desc_joule,                       1.000));
        cat.add(new Unit(R.string.unit_kilojoule, R.string.desc_kilojoule,            1000.000));
        cat.add(new Unit(R.string.unit_cal, R.string.desc_cal,                           4.184));
        cat.add(new Unit(R.string.unit_kcal, R.string.desc_kcal,                      4184.000));
        cat.add(new Unit(R.string.unit_ws, R.string.desc_ws,                             1.000));
        cat.add(new Unit(R.string.unit_wh, R.string.desc_wh,                          3600.000));
        cat.add(new Unit(R.string.unit_kwh, R.string.desc_kwh,                     3600000.000));
        mList.add(cat);

        cat = new Category(R.string.cat_currency);
        cat.add(new Unit(R.string.unit_euro,  R.string.desc_euro,             1.000));
        cat.add(new Unit(R.string.unit_dollar,  R.string.desc_dollar,           0.900));
        cat.add(new Unit(R.string.unit_rupee,   R.string.desc_rupee,           0.015));
        cat.add(new Unit(R.string.unit_sfr,     R.string.desc_sfr, 0.960));
        cat.add(new Unit(R.string.unit_gbp,     R.string.desc_gbp,         1.380));
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
}
