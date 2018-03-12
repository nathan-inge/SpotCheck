package com.ucsb.cs48.spotcheck.Utilities;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.text.NumberFormat;

public class MoneyTextWatcher implements TextWatcher {
    private final EditText editTextWeakReference;

    public MoneyTextWatcher(EditText editText) {
        editTextWeakReference = editText;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    private String current = "";
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(!s.toString().equals(current)){
            editTextWeakReference.removeTextChangedListener(this);

            String cleanString = s.toString().replaceAll("[$+,+.+]", "");

            double parsed = Double.parseDouble(cleanString);
            String formatted = NumberFormat.getCurrencyInstance().format((parsed/100));

            current = formatted;
            editTextWeakReference.setText(formatted);
            editTextWeakReference.setSelection(formatted.length());

            editTextWeakReference.addTextChangedListener(this);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
