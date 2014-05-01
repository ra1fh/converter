package de.ackstorm.converter;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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

public class MainActivity extends Activity {

	public static String TAG = "Converter";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "MainActivity.onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        ActionBar actionBar = getActionBar();
        actionBar.setSubtitle(getString(R.string.app_tagline));

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new ConverterFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
     * converter fragment containing the main view.
     */
    public static class ConverterFragment extends Fragment {
    	
    	protected UnitConverter mUnitConverter;
    	protected Button mConvertButton;
    	protected Spinner mUnitSpinner;
    	protected Spinner mCategorySpinner;
    	protected EditText mEditText;
    	protected TextView mOutputText;
    	
    	private static final String KEY_TEXT = "text";
    	
        @Override
        public void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
            outState.putString(KEY_TEXT, mOutputText.getText().toString());
        }
    	
    	@Override
    	public void onCreate(Bundle savedInstanceState){
    		super.onCreate(savedInstanceState);
    		mUnitConverter = new UnitConverter();
    	}
    	
        @Override
        public View onCreateView(LayoutInflater inflater,
        						 ViewGroup container,
        						 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
                        
            // mConvertButton   = (Button)   rootView.findViewById(R.id.convert_button);
			mUnitSpinner     = (Spinner)  rootView.findViewById(R.id.unit_spinner);
			mCategorySpinner = (Spinner)  rootView.findViewById(R.id.category_spinner);
	    	mEditText        = (EditText) rootView.findViewById(R.id.edit_message);
	    	mOutputText      = (TextView) rootView.findViewById(R.id.output_text);

        	if (savedInstanceState != null)	{
        		mOutputText.setText(savedInstanceState.getString(KEY_TEXT));
        	}
        	populateSpinner(mCategorySpinner, -1);

        	mCategorySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
        		
        		@Override
                public void onItemSelected(AdapterView<?> parent, View view, 
                        int pos, long id) {
            		populateSpinner(mUnitSpinner, pos);
            		updateResult();
                }
                
        		@Override
                public void onNothingSelected(AdapterView<?> parent) {
        			updateResult();
                }

        	});
        	
        	mUnitSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					updateResult();
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					updateResult();
				}
        		
        	});

//	    	mConvertButton.setOnClickListener(new View.OnClickListener() {
//				
//				@Override
//				public void onClick(View view) {
//					updateResult();
//				}
//				
//			});

        	mEditText.addTextChangedListener(new TextWatcher() {
        		
				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub
					updateResult();
				}
	    		
	    	});
                    	
            return rootView;
        }

        public void populateSpinner(Spinner spinner, int pos) {
        	ArrayList<Integer> unitIds = mUnitConverter.getUnits(pos);
        	ArrayList<CharSequence> unitStrings = new ArrayList<CharSequence>();
        	ArrayAdapter<CharSequence> adapter;

        	for (Integer i : unitIds) {
        		unitStrings.add(getActivity().getApplicationContext().getString(i));
        	}
        	
        	adapter = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item, unitStrings);
    		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    		spinner.setAdapter(adapter);
        }
        
        public void updateResult() {
			int unitIndex = mUnitSpinner.getSelectedItemPosition();
			int categoryIndex = mCategorySpinner.getSelectedItemPosition();
			String valueString = mEditText.getText().toString().trim();
			String output = "";
			LinkedHashMap<Integer, Double> result;
			double value;
			
			if (! valueString.isEmpty() && unitIndex >= 0 && categoryIndex >= 0) {
				value = Float.valueOf(valueString.trim()).floatValue();
				result = mUnitConverter.convert(categoryIndex, unitIndex, value);
				for (int k: result.keySet()) {
					String unit = getActivity().getApplicationContext().getString(k);
					output += result.get(k).toString() + " " + unit + "\n";
				}
			} else {
				output = "";
			}
			mOutputText.setText(output);
        }

    } /* class ConverterFragment */
}
