package de.ackstorm.converter;

import java.util.ArrayList;

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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import de.ackstorm.converter.UnitConverter.Unit;
import de.ackstorm.converter.UnitConverter.UnitValue;

public class MainActivity extends Activity {

	public static String TAG = "Converter";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
    	protected ListView mOutputList;
    	protected Integer mSavedCategory = null;
    	
    	private static final String KEY_CATEGORY = "category";
    	private static final String KEY_UNIT = "unit";
    	
        @Override
        public void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
            outState.putInt(KEY_CATEGORY, Integer.valueOf(mCategorySpinner.getSelectedItemPosition()));
            outState.putInt(KEY_UNIT, Integer.valueOf(mUnitSpinner.getSelectedItemPosition()));
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
                        
			mUnitSpinner     = (Spinner)  rootView.findViewById(R.id.unit_spinner);
			mCategorySpinner = (Spinner)  rootView.findViewById(R.id.category_spinner);
	    	mEditText        = (EditText) rootView.findViewById(R.id.edit_message);
	    	mOutputList      = (ListView) rootView.findViewById(R.id.output_list);

        	if (savedInstanceState != null)	{
        		int unit = savedInstanceState.getInt(KEY_UNIT);
        		int category = savedInstanceState.getInt(KEY_CATEGORY);

        		mSavedCategory = category;
            	populateCategorySpinner();
            	mCategorySpinner.setSelection(category);
            	populateUnitSpinner(category);
            	mUnitSpinner.setSelection(unit);
        	} else {
            	populateCategorySpinner();
            	populateUnitSpinner(0);
        	}

        	mCategorySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
        		
        		@Override
                public void onItemSelected(AdapterView<?> parent, View view, 
                        int category, long id) {
            		/*
            		 * The onItemSelectedListener gets called after saved state has been
            		 * applied, resulting in the unit spinner to be reset to default.
            		 * Prevent this by only changing the unit spinner if the current pos
            		 * is different from what has been set from saved state.
            		 */
                    if (mSavedCategory != null) {
                    	if (mSavedCategory != category) {
                        	populateUnitSpinner(category);
                        	mSavedCategory = null;
                    	}
        			}
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

        	mEditText.addTextChangedListener(new TextWatcher() {
        		
				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
				}

				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
				}

				@Override
				public void afterTextChanged(Editable s) {
					updateResult();
				}
	    		
	    	});
                    	
            return rootView;
        }

        public void populateSpinner(Spinner spinner, ArrayList<CharSequence> strings) {
        	ArrayAdapter<CharSequence> adapter;
        	
        	adapter = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item, strings);
    		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    		spinner.setAdapter(adapter);
        }
        
        public void populateCategorySpinner() {
        	ArrayList<Integer> categoryIds;
           	ArrayList<CharSequence> categoryStrings;

           	categoryIds = mUnitConverter.getCategories();
        	categoryStrings = new ArrayList<CharSequence>();

        	for (Integer i : categoryIds) {
        		categoryStrings.add(getActivity().getApplicationContext().getString(i));
        	}
        	populateSpinner(mCategorySpinner, categoryStrings);
        }

        public void populateUnitSpinner(int category) {
        	ArrayList<Unit> units;
           	ArrayList<CharSequence> unitStrings;

           	units = mUnitConverter.getUnits(category);
        	unitStrings = new ArrayList<CharSequence>();

        	for (Unit u : units) {
        		String unit = getActivity().getApplicationContext().getString(u.getUnitId());
        		String desc = getActivity().getApplicationContext().getString(u.getDescId());
        		if (! desc.trim().isEmpty()) {
            		unit += " (";
            		unit += desc;
            		unit += ")";
        		}
        		unitStrings.add(unit);
        	}
        	populateSpinner(mUnitSpinner, unitStrings);
        }

        public void updateResult() {
			ResultAdapter adapter;
			ArrayList<UnitValue> result;
			String valueString;
			double value;
			int unitIndex;
			int categoryIndex;

			result = new ArrayList<UnitValue>();
			unitIndex = mUnitSpinner.getSelectedItemPosition();
			categoryIndex = mCategorySpinner.getSelectedItemPosition();
			
			if (unitIndex < 0 || categoryIndex < 0) {
				adapter = new ResultAdapter(result);
				mOutputList.setAdapter(adapter);
	            return;
			}
			
			valueString = mEditText.getText().toString().trim();

			if (valueString.isEmpty()) {
				adapter = new ResultAdapter(result);
				mOutputList.setAdapter(adapter);
				return;
			}
	            
			try {
				value = Float.valueOf(valueString.trim()).floatValue();
				result = mUnitConverter.convert(categoryIndex, unitIndex, value);
			} catch (NumberFormatException e) {
				Log.d(TAG, "updateResult: invalid number format");
			}
			adapter = new ResultAdapter(result);
			mOutputList.setAdapter(adapter);
        }
        
        private class ResultAdapter extends ArrayAdapter<UnitValue> {

        	public ResultAdapter(ArrayList<UnitValue> values) {
        		super(getActivity(), 0, values);
        	}
        	
        	@Override
        	public View getView(int position, View convertView, ViewGroup parent) {
        		if (convertView == null) {
        			convertView = getActivity().getLayoutInflater()
        					.inflate(R.layout.list_item_result, null);
        		}
        		
        		UnitValue v = getItem(position);
        		
        		TextView valueTextView = (TextView) convertView.findViewById(R.id.list_item_value);
        		valueTextView.setText(Float.valueOf(v.getVal().floatValue()).toString());
        		
        		TextView unitTextView = (TextView) convertView.findViewById(R.id.list_item_unit);
        		unitTextView.setText(convertView.getContext().getString(v.getUnit()));
        		return convertView;
        	}
        }
        
    } /* class ConverterFragment */
}
