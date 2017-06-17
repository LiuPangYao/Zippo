package com.example.user.zippo;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.zippo.custom.BaseDialogFragment;
import com.example.user.zippo.custom.CustomDialog;
import com.example.user.zippo.custom.ErrorDialog;
import com.example.user.zippo.custom.SharedPreferencesUtil;
import com.example.user.zippo.fragment.BackHandledFragment;

/**
 * 編輯 姓名 簡介 國家
 * 待補 : 編輯後資料帶回，未輸入提示
 */
public class IntroductionFragment extends BackHandledFragment implements View.OnClickListener
{
    private EditText mEdtName, mEdtNameFirst, mEdtWorkTitle, mEdtWorkNow, mEdtTeach, mEdtCountry, mEdtIntroduction;
    private TextView mTxtSave, mTxtEdtWork, mTxtEdtTeach;
    private ImageView mImageViewClose;
    private BaseDialogFragment mDialogFragmentC; // Close
    private boolean hadIntercept; // 監聽返回參數
    SharedPreferencesUtil spUtil;
    private Dialog errorDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_introduction, container, false);

        mEdtName = (EditText) view.findViewById(R.id.edtName);
        mEdtNameFirst = (EditText) view.findViewById(R.id.edtNameFirst);
        mEdtWorkTitle = (EditText) view.findViewById(R.id.edtWorkTitle);
        mEdtWorkNow = (EditText) view.findViewById(R.id.edtWorkNow);
        mEdtTeach = (EditText) view.findViewById(R.id.edtTeach);
        mEdtCountry = (EditText) view.findViewById(R.id.edtCountry);
        mEdtIntroduction = (EditText) view.findViewById(R.id.edtIntroduction);

        mTxtSave = (TextView) view.findViewById(R.id.txtSave);
        mImageViewClose = (ImageView) view.findViewById(R.id.imageViewClose);
        mTxtEdtTeach = (TextView) view.findViewById(R.id.txtEdtTech);
        mTxtEdtWork = (TextView) view.findViewById(R.id.txtEdtWork);

        mTxtSave.setOnClickListener(this);
        mImageViewClose.setOnClickListener(this);
        mTxtEdtWork.setOnClickListener(this);
        mTxtEdtTeach.setOnClickListener(this);

        spUtil = new SharedPreferencesUtil(getActivity(), "introduction_data");

        initView();

        return view;
    }

    private void initView()
    {
        if(!spUtil.getString("Name","").toString().trim().equals(""))
        {
           mEdtName.setText(spUtil.getString("Name", "").toString());
        }

        if(!spUtil.getString("NameFirst", "").toString().trim().equals(""))
        {
            mEdtNameFirst.setText(spUtil.getString("NameFirst", ""));
        }

        if(!spUtil.getString("WorkTitle", "").toString().trim().equals(""))
        {
            mEdtWorkTitle.setText(spUtil.getString("WorkTitle", ""));
        }

        if(!spUtil.getString("WorkNow", "").toString().trim().equals(""))
        {
            mEdtWorkNow.setText(spUtil.getString("WorkNow", ""));
        }

        if(!spUtil.getString("WorkTeach", "").toString().trim().equals(""))
        {
            mEdtTeach.setText(spUtil.getString("WorkTeach", ""));
        }

        if(!spUtil.getString("WorkCountry", "").toString().trim().equals(""))
        {
            mEdtCountry.setText(spUtil.getString("WorkCountry", ""));
        }

        if(!spUtil.getString("WorkCountry", "").toString().trim().equals(""))
        {
            mEdtCountry.setText(spUtil.getString("WorkCountry", ""));
        }

        if(!spUtil.getString("Introduction", "").toString().trim().equals(""))
        {
            mEdtIntroduction.setText(spUtil.getString("Introduction", ""));
        }
    }

    @Override
    public void onClick(View v)
    {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();

        switch (v.getId())
        {
            case R.id.txtSave: // 儲存

                if(!mEdtName.getText().toString().trim().equals(""))
                {
                    spUtil.putString("Name", mEdtName.getText().toString());
                }

                if(!mEdtNameFirst.getText().toString().trim().equals(""))
                {
                    spUtil.putString("NameFirst", mEdtNameFirst.getText().toString());
                }

                if(!mEdtWorkTitle.getText().toString().trim().equals(""))
                {
                    spUtil.putString("WorkTitle", mEdtWorkTitle.getText().toString());
                }

                if(!mEdtWorkNow.getText().toString().trim().equals(""))
                {
                    spUtil.putString("WorkNow", mEdtWorkNow.getText().toString());
                }

                if(!mEdtTeach.getText().toString().trim().equals(""))
                {
                    spUtil.putString("WorkTeach", mEdtTeach.getText().toString());
                }

                if(!mEdtCountry.getText().toString().trim().equals(""))
                {
                    spUtil.putString("WorkCountry", mEdtCountry.getText().toString());
                }

                if(!mEdtIntroduction.getText().toString().trim().equals(""))
                {
                    spUtil.putString("Introduction", mEdtIntroduction.getText().toString());
                }

                dialogShow();

                break;
            case R.id.imageViewClose: // 關閉 Fragment

                closeFragment();

                break;
            case R.id.txtEdtWork: // 編輯現職

                WorkHistoryFragment workHistoryFragment = new WorkHistoryFragment();
                ft.add(R.id.fragment_container_introduction, workHistoryFragment);
                ft.addToBackStack("workHistoryFragment");
                ft.commit();

                break;
            case R.id.txtEdtTech: // 編輯學歷

                TeachFragment teachFragment = new TeachFragment();
                ft.add(R.id.fragment_container_introduction, teachFragment);
                ft.addToBackStack("teachFragment");
                ft.commit();

                break;
            default:
                break;
        }
    }

    public void dialogShow() // dialog 建立
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

    public void closeFragment() // dialogFragment 建立
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
    public boolean onBackPressed() // 監聽返回
    {
        if(hadIntercept) {
            return false;
        } else {
            closeFragment();
            return true;
        }
    }
}
