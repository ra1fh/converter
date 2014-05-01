package de.ackstorm.converter;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
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
        actionBar.setSubtitle("Unit Conversion Made Easy");

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
    	
    	@Override
    	public void onCreate(Bundle savedInstanceState){
    		super.onCreate(savedInstanceState);
    		mUnitConverter = new UnitConverter(getActivity().getApplicationContext());
    	}
    	
        @Override
        public View onCreateView(LayoutInflater inflater,
        						 ViewGroup container,
        						 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
                        
            mConvertButton   = (Button)   rootView.findViewById(R.id.convert_button);
			mUnitSpinner     = (Spinner)  rootView.findViewById(R.id.unit_spinner);
			mCategorySpinner = (Spinner)  rootView.findViewById(R.id.category_spinner);
	    	mEditText        = (EditText) rootView.findViewById(R.id.edit_message);
	    	mOutputText      = (TextView) rootView.findViewById(R.id.output_text);
            
            mConvertButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View view) {
					String unit = (String) mUnitSpinner.getSelectedItem();
					String cat = (String) mCategorySpinner.getSelectedItem();
					String valueString = mEditText.getText().toString().trim();
					String output;
					double value;
					
					if (! valueString.isEmpty()) {
						value = Float.valueOf(valueString.trim()).floatValue();
						output = mUnitConverter.convert(cat, unit, value);							
					} else {
						output = "NaN";
					}
					mOutputText.setText(output);
				}
				
			});
        	
        	populateSpinner(mCategorySpinner, R.array.cat_array);
        	mCategorySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
        		
        		@Override
                public void onItemSelected(AdapterView<?> parent, View view, 
                        int pos, long id) {
                	String item = (String) parent.getItemAtPosition(pos);
                	if (mUnitConverter.containsCategory(item)) {
                		populateSpinner(mUnitSpinner, mUnitConverter.getCategoryId(item));
                	}
                }
                
        		@Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // TODO Another interface callback
                }

        	});

            return rootView;
        }

        public void populateSpinner(Spinner spinner, int aryId) {
    		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
    			aryId, android.R.layout.simple_spinner_item);
    		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    		spinner.setAdapter(adapter);
        }

    } /* class ConverterFragment */
}
