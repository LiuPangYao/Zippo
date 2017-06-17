package com.example.user.zippo;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * 工作經驗
 */
public class WorkHistoryFragment extends BackHandledFragment implements View.OnClickListener
{
    private TextView mTxtSave;
    private ImageView mImageViewClose;
    private Boolean hadIntercept = false; // 監聽返回參數
    private BaseDialogFragment mDialogFragmentC; // Close
    private Dialog errorDialog;
    private EditText mEdtWorkName, mEdtCompanyName, mEdtLocation, mEdtWorkStart, mEdtWorkEnd, mEdtScribre;
    private View view;
    private Calendar m_Calendar = Calendar.getInstance();
    private DatePickerDialog.OnDateSetListener datepicker, datepicker1;
    private DatePickerDialog dialog, dialog1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_work_history, container, false);

        mTxtSave = (TextView) view.findViewById(R.id.txtSave);
        mImageViewClose = (ImageView) view.findViewById(R.id.imageViewClose);

        mEdtWorkName = (EditText) view.findViewById(R.id.edtWorkName);
        mEdtCompanyName = (EditText) view.findViewById(R.id.edtCompanyName);
        mEdtLocation = (EditText) view.findViewById(R.id.edtLocation);
        mEdtWorkStart = (EditText) view.findViewById(R.id.edtWorkStart);
        mEdtWorkEnd = (EditText) view.findViewById(R.id.edtWorkEnd);
        mEdtScribre = (EditText) view.findViewById(R.id.edtScribe);

        mImageViewClose.setOnClickListener(this);
        mTxtSave.setOnClickListener(this);
        mEdtWorkStart.setOnClickListener(this);
        mEdtWorkEnd.setOnClickListener(this);

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

                mEdtWorkStart.setText(sdf.format(m_Calendar.getTime()));

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

                mEdtWorkEnd.setText(sdf.format(m_Calendar.getTime()));
            }
        };

        return view;
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
            case R.id.edtWorkStart:

                dialog = new DatePickerDialog(getActivity(),
                        datepicker,
                        m_Calendar.get(Calendar.YEAR),
                        m_Calendar.get(Calendar.MONTH),
                        m_Calendar.get(Calendar.DAY_OF_MONTH));
                dialog.show();

                break;
            case R.id.edtWorkEnd:

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

    /**
     * dialog 建立
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
                                hadIntercept = true;
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
                    hadIntercept = true;
                    getActivity().onBackPressed();
                }

                @Override
                public void onCancelClick()
                {
                    // to do nothing
                    hadIntercept = false; // 加入是因為點選取消，下次再點選返回，顯示提示框
                }
            });

            mDialogFragmentC.setCancelable(false);
            mDialogFragmentC.show(getActivity().getSupportFragmentManager(), "dialogClose");
        }
    }

    @Override
    public boolean onBackPressed()
    {
        if(hadIntercept) {
            return false;
        } else {
            closeFragment();
            return true;
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        hadIntercept = false;
    }
}
