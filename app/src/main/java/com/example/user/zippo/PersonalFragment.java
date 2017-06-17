package com.example.user.zippo;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.zippo.custom.BaseDialogFragment;
import com.example.user.zippo.custom.CustomDialog;
import com.example.user.zippo.custom.ErrorDialog;
import com.example.user.zippo.fragment.BackHandledFragment;

import info.hoang8f.android.segmented.SegmentedGroup;

/**
 * 個人成就
 */
public class PersonalFragment extends BackHandledFragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener
{
    private boolean hadInterceptTeach = false; // 監聽返回參數
    private BaseDialogFragment mDialogFragmentC; // Close
    private Dialog errorDialog;
    private RadioButton mRadioLanguageSkill, mRadioPaperSkill; // Segment
    private ImageView mImageViewClose;
    private TextView mTxtSave;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_personal, container, false);

        SegmentedGroup mSegment = (SegmentedGroup) view.findViewById(R.id.segmented2);
        mSegment.setTintColor(Color.BLACK);
        mSegment.setOnCheckedChangeListener(this);

        mRadioLanguageSkill = (RadioButton) view.findViewById(R.id.buttonLanguageSkill);
        mRadioPaperSkill = (RadioButton) view.findViewById(R.id.buttonPaperSkill) ;
        mRadioLanguageSkill.setChecked(true);

        mImageViewClose = (ImageView) view.findViewById(R.id.imageViewClose);
        mTxtSave = (TextView) view.findViewById(R.id.txtSave);

        mImageViewClose.setOnClickListener(this);
        mTxtSave.setOnClickListener(this);

        return view;
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId)
    {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        switch (checkedId)
        {
            case R.id.buttonLanguageSkill: // 語言能力
                Toast.makeText(getActivity(), "語言能力", Toast.LENGTH_SHORT).show();

                LanguageFragment languageFragment = new LanguageFragment();
                ft.replace(R.id.fragment_personal, languageFragment);
                ft.commit();

                break;
            case R.id.buttonPaperSkill: // 證照資格
                Toast.makeText(getActivity(), "證照資格", Toast.LENGTH_SHORT).show();

                PaperSkillFragment paperSkillFragment = new PaperSkillFragment();
                ft.replace(R.id.fragment_personal, paperSkillFragment);
                ft.commit();

                break;
            default:
                break;
        }
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
            default:
                break;
        }
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
