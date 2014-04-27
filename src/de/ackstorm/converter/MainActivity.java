package de.ackstorm.converter;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import de.ackstorm.converter.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {
	protected static Map<String, LinkedHashMap<String, Double>> mConverterMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new ConverterFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * converter fragment containing a simple view.
     */
    public static class ConverterFragment extends Fragment implements OnItemSelectedListener {

    	Button mConvertButton;
    	
    	protected Map<String, Integer> categoryMap;

    	public ConverterFragment() {
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
                        
        	categoryMap = new HashMap<String, Integer>();
        	categoryMap.put(getString(R.string.cat_torque), R.array.torque_array);
        	categoryMap.put(getString(R.string.cat_mass),   R.array.mass_array);
        	categoryMap.put(getString(R.string.cat_length), R.array.length_array);
        	categoryMap.put(getString(R.string.cat_volume), R.array.volume_array);
        	
        	mConverterMap = new HashMap<String, LinkedHashMap<String, Double>>();
        	
        	LinkedHashMap<String, Double> unitMap;
        	unitMap = new LinkedHashMap<String, Double>();
        	
        	unitMap.put(getString(R.string.unit_m),     1.0);
        	unitMap.put(getString(R.string.unit_km), 1000.0);
        	unitMap.put(getString(R.string.unit_yd),    0.9144);
        	unitMap.put(getString(R.string.unit_in),    0.0254);
        	unitMap.put(getString(R.string.unit_ft),    0.3048);
        	unitMap.put(getString(R.string.unit_mi), 1609.344);
        	unitMap.put(getString(R.string.unit_nmi),1853.184);
        	mConverterMap.put(getString(R.string.cat_length), unitMap);
        	
        	unitMap = new LinkedHashMap<String, Double>();
        	unitMap.put(getString(R.string.unit_nm),    1.0);
        	unitMap.put(getString(R.string.unit_inlbf), 0.1129848290276167);
        	mConverterMap.put(getString(R.string.cat_torque), unitMap);
        	
        	unitMap = new LinkedHashMap<String, Double>();
        	unitMap.put(getString(R.string.unit_m3), 1.0);
        	unitMap.put(getString(R.string.unit_l),  0.001);
        	unitMap.put(getString(R.string.unit_gal_us), 0.003785411784);
        	unitMap.put(getString(R.string.unit_gal_imp), 0.00454609);
        	mConverterMap.put(getString(R.string.cat_volume), unitMap);

        	unitMap = new LinkedHashMap<String, Double>();
        	unitMap.put(getString(R.string.unit_kg), 1.0);
        	unitMap.put(getString(R.string.unit_lb),  0.45359237);
        	unitMap.put(getString(R.string.unit_oz),  0.028);
        	mConverterMap.put(getString(R.string.cat_mass), unitMap);
        	
            mConvertButton = (Button) getActivity().findViewById(R.id.convert_button);
            mConvertButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
				}
			});
        	
        	populateSpinner(R.id.category_spinner, R.array.cat_array);
        	((Spinner) getActivity().findViewById(R.id.category_spinner)).setOnItemSelectedListener(this);
        }
        
        
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            
            return rootView;
        }
        
        public void populateSpinner(int spinnerId, int aryId) {
    		Spinner spinner = (Spinner) getActivity().findViewById(spinnerId);
    		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
    			aryId, android.R.layout.simple_spinner_item);
    		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    		spinner.setAdapter(adapter);
        }
        
        public void onItemSelected(AdapterView<?> parent, View view, 
                int pos, long id) {
        	String item = (String) parent.getItemAtPosition(pos);
        	
        	if (categoryMap.containsKey(item)) {
        		populateSpinner(R.id.unit_spinner, categoryMap.get(item));
        	}
        }
        
        public void onNothingSelected(AdapterView<?> parent) {
            // Another interface callback
        }
            
    }

    /** Called when the user clicks the Send button */
    public void sendMessage(View view) {
    	
		Spinner unitSpinner = (Spinner) findViewById(R.id.unit_spinner);
		Spinner catSpinner = (Spinner) findViewById(R.id.category_spinner);
    	EditText editText = (EditText) findViewById(R.id.edit_message);
    	TextView outputText = (TextView) findViewById(R.id.output_text);
    	
		String unit = (String) unitSpinner.getSelectedItem();
		String cat = (String) catSpinner.getSelectedItem();
		
		
		String valueString = editText.getText().toString();
		String output = "";
		double value;
		double result = 0;
		double inputFactor;
		
		value = Float.valueOf(valueString.trim()).floatValue();

		if (mConverterMap.containsKey(cat)) {
			LinkedHashMap<String, Double> unitMap;
			unitMap = mConverterMap.get(cat);
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
				
		outputText.setText(output);
    }
    
}
