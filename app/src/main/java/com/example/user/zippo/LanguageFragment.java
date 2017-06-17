package com.example.user.zippo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * 語言能力
 */
public class LanguageFragment extends Fragment
{
    private EditText mEdtLanguage;
    private Spinner mSpinnerSkill;
    private Button mBtnSave;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_language, container, false);

        mEdtLanguage = (EditText) view.findViewById(R.id.edtLanguage);
        mSpinnerSkill = (Spinner) view.findViewById(R.id.spinnerSkill);
        mBtnSave = (Button) view.findViewById(R.id.btnSave);

        ArrayAdapter<CharSequence> lunchList = ArrayAdapter.createFromResource
                (
                    getActivity(),
                    R.array.SPINNER_SKILL,
                    android.R.layout.simple_spinner_dropdown_item
                );

        mSpinnerSkill.setAdapter(lunchList);

        return view;
    }
}
