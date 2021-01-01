package de.ackstorm.converter;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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

public class MainActivity extends AppCompatActivity {

    Spinner mUnitSpinner;
    Spinner mCategorySpinner;
    UnitConverter mUnitConverter;
    EditText mEditText;
    ListView mOutputList;

    Integer mSavedCategory = null;

    private static final String KEY_CATEGORY = "category";
    private static final String KEY_UNIT = "unit";
	private static final String TAG = "Converter";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUnitSpinner     = (Spinner)  findViewById(R.id.unit_spinner);
        mCategorySpinner = (Spinner)  findViewById(R.id.category_spinner);
        mEditText        = (EditText) findViewById(R.id.edit_message);
        mOutputList      = (ListView) findViewById(R.id.output_list);

        mUnitConverter = new UnitConverter();

        if (savedInstanceState != null) {
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

        mUnitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
    }

    public void populateSpinner(Spinner spinner, ArrayList<CharSequence> strings) {
        ArrayAdapter<CharSequence> adapter;
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, strings);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public void populateCategorySpinner() {
        ArrayList<Integer> categoryIds;
        ArrayList<CharSequence> categoryStrings;

        categoryIds = mUnitConverter.getCategories();
        categoryStrings = new ArrayList<>();

        for (Integer i : categoryIds) {
            categoryStrings.add(getString(i));
        }
        populateSpinner(mCategorySpinner, categoryStrings);
    }

    public void populateUnitSpinner(int category) {
        ArrayList<Unit> units;
        ArrayList<CharSequence> unitStrings;

        units = mUnitConverter.getUnits(category);
        unitStrings = new ArrayList<>();

        for (Unit u : units) {
            String unit = getString(u.getUnitId());
            String desc = getString(u.getDescId());
            if (!desc.trim().isEmpty()) {
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
            adapter = new ResultAdapter(this, result);
            mOutputList.setAdapter(adapter);
            return;
        }

        valueString = mEditText.getText().toString().trim();

        if (valueString.isEmpty()) {
            adapter = new ResultAdapter(this, result);
            mOutputList.setAdapter(adapter);
            return;
        }

        try {
            value = Float.valueOf(valueString.trim());
            result = mUnitConverter.convert(categoryIndex, unitIndex, value);
        } catch (NumberFormatException e) {
            Log.d(TAG, "updateResult: invalid number format");
        }
        adapter = new ResultAdapter(this, result);
        mOutputList.setAdapter(adapter);
    }

    private class ResultAdapter extends ArrayAdapter<UnitValue> {

        public ResultAdapter(AppCompatActivity activity, ArrayList<UnitValue> values) {
            super(activity, 0, values);
        }

        @SuppressLint("InflateParams")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater()
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
}