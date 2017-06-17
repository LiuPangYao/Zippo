package com.example.user.zippo;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.greendao.ZI_NoteList;
import com.example.user.zippo.custom.BaseDialogFragment;
import com.example.user.zippo.custom.CustomDialog;
import com.example.user.zippo.custom.SharedPreferencesUtil;
import com.example.user.zippo.data.DataNoteInformation;
import com.example.user.zippo.fragment.BackHandledFragment;
import com.example.user.zippo.store.BlogDataStore;
import com.example.user.zippo.store.EmailDataStore;
import com.example.user.zippo.store.NoteListStore;

import java.util.ArrayList;

import info.hoang8f.android.segmented.SegmentedGroup;

public class SettingFragment extends BackHandledFragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener
{
    private TextView mTxtVersion;
    private TextView mTxtPrivacy;
    private TextView mTxtReset;
    private TextView mTxtDelete;
    private BaseDialogFragment mDialogFragmentR; // Reset
    private BaseDialogFragment mDialogFragmentD; // Delete
    private RadioButton mRadioSmall, mRadioMedium, mRadioLarge; // Segment
    SharedPreferencesUtil spUtil, spUtil1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        mTxtPrivacy = (TextView)view.findViewById(R.id.txtPrivacy);
        mTxtReset = (TextView)view.findViewById(R.id.txtReset);
        mTxtDelete = (TextView)view.findViewById(R.id.txtDelete);
        mTxtPrivacy.setOnClickListener(this);
        mTxtReset.setOnClickListener(this);
        mTxtDelete.setOnClickListener(this);

        mTxtVersion = (TextView)view.findViewById(R.id.txtVersion);
        mTxtVersion.setText(getString(R.string.VERSION_) + getVersionName());

        SegmentedGroup mSegment = (SegmentedGroup) view.findViewById(R.id.segmented2);
        mSegment.setTintColor(Color.BLACK);
        mSegment.setOnCheckedChangeListener(this);

        mRadioLarge = (RadioButton)view.findViewById(R.id.buttonLarge);
        mRadioMedium = (RadioButton)view.findViewById(R.id.buttonMedium) ;
        mRadioSmall = (RadioButton)view.findViewById(R.id.buttonSmall) ;

        spUtil = new SharedPreferencesUtil(getActivity(), "shared_data");
        spUtil1 = new SharedPreferencesUtil(getActivity(), "introduction_data");
        String choiceClick = spUtil.getString("radioButtonClick", "");

        if(choiceClick.equals(getString(R.string.SMALL)))
        {
            mRadioSmall.setChecked(true);
        } else if (choiceClick.equals(getString(R.string.MEDIUM))) {
            mRadioMedium.setChecked(true);
        } else if (choiceClick.equals(getString(R.string.LARGE))) {
            mRadioLarge.setChecked(true);
        } else {
            mRadioMedium.setChecked(true);
        }

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * 取得 APP 版號
     * @return version 版本
     */
    private String getVersionName()
    {
        String version = "";
        try {
            PackageManager packageManager = getContext().getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(getContext().getPackageName(), 0);
            version = packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.txtPrivacy: // 隱私權政策
                PrivacyFragment privacyFragment = new PrivacyFragment();
                FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                ft.add(R.id.fragment_container_setting, privacyFragment);
                ft.addToBackStack("privacyFragment");
                ft.commit();
                break;

            case R.id.txtReset: // 重置資料
                if (mDialogFragmentR == null)
                {
                    mDialogFragmentR = CustomDialog.newInstance(R.layout.custom_dialog, getString(R.string.OK), getString(R.string.OK_RESET), getResources().getColor(R.color.colorPrimary));
                    mDialogFragmentR.setOnBtnClickListener(new BaseDialogFragment.OnBtnClickListener()
                    {
                        @Override
                        public void onOkClick()
                        {
                            NoteListStore noteListStore = new NoteListStore(getActivity());
                            ArrayList data = new ArrayList<ZI_NoteList>();
                            for (int i = 0; i < DataNoteInformation.textArray.length; i++)
                            {
                                data.add(
                                        new ZI_NoteList(
                                                null,
                                                DataNoteInformation.textArray[i],
                                                DataNoteInformation.dateArray[i],
                                                DataNoteInformation.urlArray[i]
                                        )
                                );
                            }
                            noteListStore.storeNoteList(data);
                        }

                        @Override
                        public void onCancelClick()
                        {
                            // to do nothing
                        }
                    });
                }
                mDialogFragmentR.setCancelable(false);
                mDialogFragmentR.show(getActivity().getSupportFragmentManager(), "dialogReset");

                break;
            case R.id.txtDelete: // 清除資料
                if (mDialogFragmentD == null)
                {
                    mDialogFragmentD = CustomDialog.newInstance(R.layout.custom_dialog, getString(R.string.OK), getString(R.string.OK_DELETE), getResources().getColor(R.color.colorPrimary));
                    mDialogFragmentD.setOnBtnClickListener(new BaseDialogFragment.OnBtnClickListener()
                    {
                        @Override
                        public void onOkClick()
                        {
                            NoteListStore noteListDelete = new NoteListStore(getActivity()); // 清除列表資料
                            noteListDelete.deleteAll();

                            EmailDataStore emailDataStore = new EmailDataStore(getActivity()); //清除 Email 資料
                            emailDataStore.deleteAll();

                            BlogDataStore blogDataStore = new BlogDataStore(getActivity()); //清除 Blog 資料
                            blogDataStore.deleteAll();

                            spUtil.clear();
                            spUtil1.clear();
                        }

                        @Override
                        public void onCancelClick()
                        {
                            // to do nothing
                        }
                    });
                }
                mDialogFragmentD.setCancelable(false);
                mDialogFragmentD.show(getActivity().getSupportFragmentManager(), "dialogDelete");

                break;
            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId)
    {
        switch (checkedId)
        {
            case R.id.buttonSmall:
                spUtil.clear();
                spUtil.putString("fontType", getString(R.string.SMALL));
                spUtil.putString("radioButtonClick", getString(R.string.SMALL));
                break;
            case R.id.buttonMedium:
                spUtil.clear();
                spUtil.putString("fontType", getString(R.string.MEDIUM));
                spUtil.putString("radioButtonClick", getString(R.string.MEDIUM));
                break;
            case R.id.buttonLarge:
                spUtil.clear();
                spUtil.putString("fontType", getString(R.string.LARGE));
                spUtil.putString("radioButtonClick", getString(R.string.LARGE));
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onBackPressed()
    {
        return false;
    }
}
