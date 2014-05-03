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
    	/* protected TextView mOutputText; */
    	protected ListView mOutputList;
    	
    	private static final String KEY_TEXT = "text";
    	private static final String KEY_CATEGORY = "category";
    	private static final String KEY_UNIT = "unit";
    	
        @Override
        public void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
            /* outState.putString(KEY_TEXT, mOutputText.getText().toString()); */
            outState.putInt(KEY_CATEGORY, mCategorySpinner.getSelectedItemPosition());
            outState.putInt(KEY_UNIT, mUnitSpinner.getSelectedItemPosition());
            Log.d(TAG, "onSaveInstanceState cat=" + mCategorySpinner.getSelectedItemPosition());
            Log.d(TAG, "onSaveInstanceState uni=" + mUnitSpinner.getSelectedItemPosition());
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
	    	/* mOutputText      = (TextView) rootView.findViewById(R.id.output_text); */
	    	mOutputList      = (ListView) rootView.findViewById(R.id.output_list);

            Log.d(TAG, "onCreateView");
        	if (savedInstanceState != null)	{
                Log.d(TAG, "onCreateView: text="+ savedInstanceState.getString(KEY_TEXT));
                Log.d(TAG, "onCreateView: cat="+ savedInstanceState.getInt(KEY_CATEGORY));
                Log.d(TAG, "onCreateView: uni="+ savedInstanceState.getInt(KEY_UNIT));
        		
        		/* mOutputText.setText(savedInstanceState.getString(KEY_TEXT)); */
            	populateCategorySpinner();
            	// TODO this doesn't seem to work
            	populateUnitSpinner(savedInstanceState.getInt(KEY_CATEGORY));
            	mUnitSpinner.setSelection(savedInstanceState.getInt(KEY_UNIT));
        	}
        	populateCategorySpinner();

        	mCategorySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
        		
        		@Override
                public void onItemSelected(AdapterView<?> parent, View view, 
                        int category, long id) {
            		populateUnitSpinner(category);
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

        public void populateSpinner(Spinner spinner, ArrayList<CharSequence> strings) {
        	ArrayAdapter<CharSequence> adapter;
        	
        	adapter = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item, strings);
    		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    		spinner.setAdapter(adapter);
        }
        
        public void populateCategorySpinner() {
        	ArrayList<Integer> categoryIds;
           	ArrayList<CharSequence> categoryStrings;

        	Log.d(TAG, "populateCategorySpinner");
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

        	Log.d(TAG, "populateUnitSpinner(" + category + ")");
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
			ArrayList<UnitValue> result = new ArrayList<UnitValue>();
			double value;
			String valueString;
			int unitIndex = mUnitSpinner.getSelectedItemPosition();
			int categoryIndex = mCategorySpinner.getSelectedItemPosition();

			Log.d(TAG, "updateResult unitIndex=" + unitIndex + " categoryIndex=" + categoryIndex);
			
			if (unitIndex < 0 || categoryIndex < 0) {
				// might happen when TextEdit is modified
				// before Spinners are populated (seen when
				// switching between landscape and portrait
				/* mOutputText.setText(""); */
	            Log.d(TAG, "updateResult: invalid index");
	            return;
			}
			
			valueString = mEditText.getText().toString().trim();

			if (valueString.isEmpty()) {
				/* mOutputText.setText(""); */
	            Log.d(TAG, "updateResult: empty valueString");
			} else { 
				try {
					value = Float.valueOf(valueString.trim()).floatValue();
					result = mUnitConverter.convert(categoryIndex, unitIndex, value);
				/*
				for (int k: result.keySet()) {
					String unit = getActivity().getApplicationContext().getString(k);
					output += result.get(k).toString() + " " + unit + "\n";
				}
				*/
				} catch (NumberFormatException e) {
					Log.d(TAG, "updateResult: invalid number format");
				}
			}
			/*
			ArrayAdapter<UnitValue> adapter =
					new ArrayAdapter<UnitValue>(getActivity(),
							android.R.layout.simple_list_item_1,
							result);
			*/
			ResultAdapter adapter = new ResultAdapter(result);
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
