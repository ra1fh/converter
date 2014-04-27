package de.ackstorm.converter;

import android.content.Context;
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
	private static UnitConverter mUnitConverter = null;

	public static UnitConverter getUnitConverter(Context context) {
		if (mUnitConverter == null){
	        mUnitConverter = new UnitConverter(context);
		}
		return mUnitConverter;
	}
	
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
    public static class ConverterFragment extends Fragment {
    	
    	public ConverterFragment() {
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
                                	
            Button convertButton = (Button) getActivity().findViewById(R.id.convert_button);
            convertButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View view) {
					Spinner unitSpinner = (Spinner) getActivity().findViewById(R.id.unit_spinner);
					Spinner catSpinner = (Spinner) getActivity().findViewById(R.id.category_spinner);
			    	EditText editText = (EditText) getActivity().findViewById(R.id.edit_message);
			    	TextView outputText = (TextView) getActivity().findViewById(R.id.output_text);
			    	
					String unit = (String) unitSpinner.getSelectedItem();
					String cat = (String) catSpinner.getSelectedItem();
					
					String valueString = editText.getText().toString();
					double value = Float.valueOf(valueString.trim()).floatValue();
					
					UnitConverter unitConverter = getUnitConverter(getActivity());
					String output = unitConverter.convert(cat, unit, value);
							
					outputText.setText(output);
				}
				
			});
        	
        	populateSpinner(R.id.category_spinner, R.array.cat_array);

        	(((Spinner) getActivity().findViewById(R.id.category_spinner))).setOnItemSelectedListener(new OnItemSelectedListener() {
        		
        		@Override
                public void onItemSelected(AdapterView<?> parent, View view, 
                        int pos, long id) {
                	String item = (String) parent.getItemAtPosition(pos);
                	UnitConverter unitConverter = getUnitConverter(getActivity());
                	if (unitConverter.containsCategory(item)) {
                		populateSpinner(R.id.unit_spinner, unitConverter.getCategoryId(item));
                	}
                }
                
        		@Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // TODO Another interface callback
                }

        	});

        }
        
        public void populateSpinner(int spinnerId, int aryId) {
    		Spinner spinner = (Spinner) getActivity().findViewById(spinnerId);
    		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
    			aryId, android.R.layout.simple_spinner_item);
    		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    		spinner.setAdapter(adapter);
        }
       
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            
            return rootView;
        }
        
    }
}
