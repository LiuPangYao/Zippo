package com.example.user.zippo;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.zippo.custom.BaseDialogFragment;
import com.example.user.zippo.custom.CustomDialog;
import com.example.user.zippo.custom.ErrorDialog;
import com.example.user.zippo.fragment.BackHandledFragment;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 教育背景
 */
public class TeachFragment extends BackHandledFragment implements View.OnClickListener
{
    private TextView mTxtSave;
    private ImageView mImageViewClose;
    private boolean hadInterceptTeach = false; // 監聽返回參數
    private BaseDialogFragment mDialogFragmentC; // Close
    private Dialog errorDialog;
    private EditText mEdtSchoolName, mEdtSchoolDegree, mEdtSchoolMain, mEdtSchoolIn, mEdtSchoolOut, mEdtGrade;
    private TextInputLayout mSchoolNameWrapper, mSchoolDegreeWrapper, mSchoolMainWrapper, mSchoolInWrapper, mSchoolOutWrapper, mGradeWrapper;
    private View view;
    private static final String TAG = MenuActivity.class.getSimpleName();
    private DatePickerDialog.OnDateSetListener datepicker, datepicker1;
    private DatePickerDialog dialog, dialog1;
    private Calendar m_Calendar = Calendar.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_teach, container, false);

        initView();

        mEdtSchoolOut.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d(TAG, "beforeTextChanged: "+s);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d(TAG, "afterTextChanged: "+s);
            }
        });

        return view;
    }

    /**
     * 初始化
     */
    public void initView() {
        mTxtSave = (TextView) view.findViewById(R.id.txtSave);
        mImageViewClose = (ImageView) view.findViewById(R.id.imageViewClose);

        mEdtSchoolName = (EditText) view.findViewById(R.id.edtSchoolName);
        mEdtSchoolDegree = (EditText) view.findViewById(R.id.edtSchoolDegree);
        mEdtSchoolMain = (EditText) view.findViewById(R.id.edtSchoolMain);
        mEdtSchoolIn = (EditText) view.findViewById(R.id.edtSchoolIn);
        mEdtSchoolOut = (EditText) view.findViewById(R.id.edtSchoolOut);
        mEdtGrade = (EditText) view.findViewById(R.id.edtGrade);

        mImageViewClose.setOnClickListener(this);
        mTxtSave.setOnClickListener(this);
        mEdtSchoolIn.setOnClickListener(this);
        mEdtSchoolOut.setOnClickListener(this);

        datepicker = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                m_Calendar.set(Calendar.YEAR, year);
                m_Calendar.set(Calendar.MONTH, monthOfYear);
                m_Calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "yyyy/MM/dd"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.TAIWAN);

                mEdtSchoolIn.setText(sdf.format(m_Calendar.getTime()));

            }
        };

        datepicker1 = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                m_Calendar.set(Calendar.YEAR, year);
                m_Calendar.set(Calendar.MONTH, monthOfYear);
                m_Calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "yyyy/MM/dd"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.TAIWAN);

                mEdtSchoolOut.setText(sdf.format(m_Calendar.getTime()));
            }
        };

        mSchoolInWrapper = (TextInputLayout) view.findViewById(R.id.SchoolInWrapper);
        mSchoolOutWrapper = (TextInputLayout) view.findViewById(R.id.SchoolOutWrapper);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.txtSave:
                dialogShow();
                break;
            case R.id.imageViewClose:
                closeFragment();
                break;
            case R.id.edtSchoolIn:

                dialog = new DatePickerDialog(getActivity(),
                        datepicker,
                        m_Calendar.get(Calendar.YEAR),
                        m_Calendar.get(Calendar.MONTH),
                        m_Calendar.get(Calendar.DAY_OF_MONTH));
                dialog.show();
                break;

            case R.id.edtSchoolOut:

                dialog1 = new DatePickerDialog(getActivity(),
                        datepicker1,
                        m_Calendar.get(Calendar.YEAR),
                        m_Calendar.get(Calendar.MONTH),
                        m_Calendar.get(Calendar.DAY_OF_MONTH));
                dialog1.show();

                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        hadInterceptTeach = false;
    }

    @Override
        public void onResume()
    {
        super.onResume();
        hadInterceptTeach = false;
    }

    /**
     * 顯示完成提示
     */
    public void dialogShow()
    {
        ErrorDialog.Builder builder = new ErrorDialog.Builder(getActivity());
        errorDialog =
                builder.cancelTouchout(false)
                        .view(R.layout.dialog_custom)
                        .widthdp(200)
                        .heightdp(200)
                        .style(R.style.Dialog)
                        .addViewOnclick(R.id.btn_cancel, new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View view)
                            {
                                hadInterceptTeach = true;
                                getActivity().onBackPressed();
                                errorDialog.dismiss();
                            }
                        })
                        .build();
        errorDialog.show();
    }

    /**
     * 關閉 DialogFragment
     */
    public void closeFragment()
    {
        mDialogFragmentC = CustomDialog.newInstance(R.layout.custom_dialog, getString(R.string.OK),
                getString(R.string.UPDATE_NO_SAVE), getResources().getColor(R.color.colorPrimary));

        if (!mDialogFragmentC.isAdded())
        {
            mDialogFragmentC.setOnBtnClickListener(new BaseDialogFragment.OnBtnClickListener()
            {
                @Override
                public void onOkClick()
                {
                    hadInterceptTeach = true;
                    getActivity().onBackPressed();
                }

                @Override
                public void onCancelClick()
                {
                    // to do nothing
                    hadInterceptTeach = false; // 加入是因為點選取消，下次再點選返回，顯示提示框
                }
            });

            mDialogFragmentC.setCancelable(false);
            mDialogFragmentC.show(getActivity().getSupportFragmentManager(), "dialogClose");
        }
    }

    @Override
    public boolean onBackPressed()
    {
        if(hadInterceptTeach) {
            return false;
        } else {
            closeFragment();
            return true;
        }
    }
}
