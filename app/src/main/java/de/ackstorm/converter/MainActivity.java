package de.ackstorm.converter;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import de.ackstorm.converter.UnitConverter.Unit;
import de.ackstorm.converter.UnitConverter.UnitValue;

public class MainActivity extends Activity {

	private static final String TAG = "Converter";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setSubtitle(getString(R.string.app_tagline));
        }

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

        switch (item.getItemId()) {

        case R.id.action_settings:
            return true;

        case R.id.action_about:
            showAbout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void showAbout() {
        View messageView;
        LayoutInflater li = LayoutInflater.from(this);
        messageView = li.inflate(R.layout.about, null);

        try {
            PackageInfo pi = getPackageManager()
                    .getPackageInfo(getApplicationContext()
                        .getPackageName(), 0);
            ((TextView) messageView.findViewById(R.id.version))
                    .setText(pi.versionName);
        } catch (Exception ignored) {

        }

        AlertDialog alert = new AlertDialog.Builder(this).create();
        alert.setIcon(R.drawable.ic_launcher);
        alert.setTitle(R.string.app_name);
        alert.setView(messageView);
        alert.setButton(AlertDialog.BUTTON_NEUTRAL,
                getString(R.string.about_link),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int whichButton) {
                        Uri uri = Uri.parse("https://bitbucket.org/ralfh/converter");
                        startActivity(new Intent(Intent.ACTION_VIEW, uri));
                    }
                });
        alert.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.about_ok),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
        alert.show();
    }

    /**
     * converter fragment containing the main view.
     */
    public static class ConverterFragment extends Fragment {
    	
    	UnitConverter mUnitConverter;
    	Spinner mUnitSpinner;
    	Spinner mCategorySpinner;
    	EditText mEditText;
    	ListView mOutputList;
    	Integer mSavedCategory = null;
    	
    	private static final String KEY_CATEGORY = "category";
    	private static final String KEY_UNIT = "unit";
    	
        @Override
        public void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
            outState.putInt(KEY_CATEGORY, mCategorySpinner.getSelectedItemPosition());
            outState.putInt(KEY_UNIT, mUnitSpinner.getSelectedItemPosition());
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
                        	mSavedCategory = null;
                        	populateUnitSpinner(category);
        				}
        			} else {
                    	populateUnitSpinner(category);
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
        	
        	adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, strings);
    		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    		spinner.setAdapter(adapter);
        }
        
        public void populateCategorySpinner() {
        	ArrayList<Integer> categoryIds;
           	ArrayList<CharSequence> categoryStrings;

           	categoryIds = mUnitConverter.getCategories();
        	categoryStrings = new ArrayList<>();

        	for (Integer i : categoryIds) {
        		categoryStrings.add(getActivity().getApplicationContext().getString(i));
        	}
        	populateSpinner(mCategorySpinner, categoryStrings);
        }

        public void populateUnitSpinner(int category) {
        	ArrayList<Unit> units;
           	ArrayList<CharSequence> unitStrings;

           	units = mUnitConverter.getUnits(category);
        	unitStrings = new ArrayList<>();

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

			result = new ArrayList<>();
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
				value = Float.valueOf(valueString.trim());
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
        	
        	@SuppressLint("InflateParams")
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
