package com.example.user.zippo;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.example.user.zippo.custom.AppManager;
import com.example.user.zippo.custom.BaseDialogFragment;
import com.example.user.zippo.custom.CustomDialog;
import com.example.user.zippo.fragment.BackHandledFragment;
import com.example.user.zippo.fragment.BackHandledInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * 主選單
 */
public class MenuActivity extends FragmentActivity implements BackHandledInterface {
    private FragmentTabHost mTabHost;
    private LayoutInflater layoutInflater;
    private Class fragmentArray[] = {ListFragment.class, WorkFragment.class, SettingFragment.class};
    private int mImageViewArray[] = {R.drawable.ic_list_white_24dp, R.drawable.ic_work_white_24dp, R.drawable.ic_settings_white_24dp};
    private int mImageViewArrayV2[] = {R.drawable.ic_list_black_24dp, R.drawable.ic_work_black_24dp, R.drawable.ic_settings_black_24dp};
    private int[] mTextViewArray = {R.string.LIST, R.string.FILE, R.string.SETTING};
    private ViewPager mViewPager;
    private List<Fragment> mList = new ArrayList<Fragment>();
    private TextView mTextView;
    private ImageView mImageView;
    private BaseDialogFragment mDialogFragment; // 監聽返回

    private BackHandledFragment mBackHandedFragment;
    public final static int RUNTIME_PERMISSION_REQUEST_CODE = 0x1; // 运行时权限申请码

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        initView();
        initPager();
    }

    private void initView()
    {
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mViewPager.setOnPageChangeListener(new ViewPagerListener());

        layoutInflater = LayoutInflater.from(MenuActivity.this);

        mTabHost = (FragmentTabHost) findViewById(R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.viewPager);
        mTabHost.setOnTabChangedListener(new TabHostListener());

        int count = fragmentArray.length;

        for (int i = 0; i < count; i++) {
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(getString(mTextViewArray[i])).setIndicator(getTabItemView(i));
            mTabHost.addTab(tabSpec, fragmentArray[i], null);
            mTabHost.setTag(i);
            // mTabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.selector_tab_background);
        }

        // Activity启動時，在的onCreate方法裏面，將該Activity實例添加到AppManager的堆棧
        AppManager.getAppManager().addActivity(this);
    }

    private void initPager() {
        ListFragment listFragment = new ListFragment();
        WorkFragment workFragment = new WorkFragment();
        SettingFragment settingFragment = new SettingFragment();

        mList.add(listFragment);
        mList.add(workFragment);
        mList.add(settingFragment);

        mViewPager.setAdapter(new CustomAdapter(getSupportFragmentManager(), mList));
        mViewPager.setCurrentItem(1); // 預設開啟位置為檔案
        mViewPager.setOffscreenPageLimit(3);
    }

    private View getTabItemView(int index) {
        View view = layoutInflater.inflate(R.layout.tabhost_item, null);
        mImageView = (ImageView) view.findViewById(R.id.imageview);
        mImageView.setImageResource(mImageViewArray[index]);
        mTextView = (TextView) view.findViewById(R.id.textview);
        mTextView.setText(mTextViewArray[index]);

        return view;
    }

    private void updateTab(final TabHost tabHost) {
        for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
            View view = tabHost.getTabWidget().getChildAt(i);
            mTextView = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(R.id.textview);
            mImageView = (ImageView) tabHost.getTabWidget().getChildAt(i).findViewById(R.id.imageview);

            if (tabHost.getCurrentTab() == i) {
                mImageView.setImageResource(mImageViewArrayV2[i]);
                mTextView.setTextColor(getResources().getColor(R.color.BLACK));
            } else {
                mImageView.setImageResource(mImageViewArray[i]);
                mTextView.setTextColor(getResources().getColor(R.color.WHITE));
            }
        }
    }

    private class TabHostListener implements TabHost.OnTabChangeListener
    {
        @Override
        public void onTabChanged(String tabId)
        {
            int position = mTabHost.getCurrentTab();
            mViewPager.setCurrentItem(position);
            updateTab(mTabHost);
        }
    }

    class CustomAdapter extends FragmentPagerAdapter
    {
        public FragmentManager fm;
        public List<Fragment> list;

        public CustomAdapter(FragmentManager fm) {
            super(fm);
            // TODO Auto-generated constructor stub
        }

        public CustomAdapter(FragmentManager fm, List<Fragment> list) {
            super(fm);
            this.fm = fm;
            this.list = list;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            fragment = list.get(position);
            Bundle bundle = new Bundle();
            bundle.putString("id", "" + position);
            fragment.setArguments(bundle);
            return fragment;
            //return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Fragment instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            fm.beginTransaction().show(fragment).commit();
            return fragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // super.destroyItem(container, position, object);
            Fragment fragment = list.get(position);
            fm.beginTransaction().hide(fragment).commit();
        }

    }

    class ViewPagerListener implements ViewPager.OnPageChangeListener
    {
        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int index)
        {
            TabWidget widget = mTabHost.getTabWidget();
            int oldFocusability = widget.getDescendantFocusability();
            widget.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
            mTabHost.setCurrentTab(index);
            widget.setDescendantFocusability(oldFocusability);
            updateTab(mTabHost);
        }
    }

    @Override
    public void setSelectedFragment(BackHandledFragment selectedFragment)
    {
        this.mBackHandedFragment = selectedFragment;
    }

    private String TAG = "BaseFragmentActivity";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        FragmentManager fragmentManager=getSupportFragmentManager();
        for(int indext = 0; indext < fragmentManager.getFragments().size() ; indext++)
        {
            Fragment fragment=fragmentManager.getFragments().get(indext); // 找到第一层Fragment
            if(fragment==null)
                Log.w(TAG, "Activity result no fragment exists for index: 0x" + Integer.toHexString(requestCode));
            else
                handleResult(fragment,requestCode,resultCode,data);
        }
    }

    /**
     * 递归调用，对所有的子Fragment生效
     * @param fragment
     * @param requestCode
     * @param resultCode
     * @param data
     */
    private void handleResult(Fragment fragment,int requestCode,int resultCode,Intent data)
    {
        fragment.onActivityResult(requestCode, resultCode, data); // 调用每个Fragment的onActivityResult
        Log.e(TAG, "MyBaseFragmentActivity");
        List<Fragment> childFragment = fragment.getChildFragmentManager().getFragments(); // 找到第二层Fragment
        if(childFragment!=null)
            for(Fragment f:childFragment)
                if(f!=null)
                {
                    handleResult(f, requestCode, resultCode, data);
                }
        if(childFragment==null)
            Log.e(TAG, "MyBaseFragmentActivity-null");
    }

    @Override
    public void onBackPressed()
    {
        if (mBackHandedFragment == null || !mBackHandedFragment.onBackPressed())
        {
            FragmentManager fm = getSupportFragmentManager();
            for (Fragment frag : fm.getFragments())
            {
                if (frag != null)
                {
                    FragmentManager childFm = frag.getChildFragmentManager();
                    if (childFm.getBackStackEntryCount() > 0)
                    {
                        for (Fragment childfragnested : childFm.getFragments())
                        {
                            FragmentManager childFmNestManager = childfragnested.getFragmentManager();
                            if (childfragnested != null)
                            {
                                childFmNestManager.popBackStack();
                                return;
                            }
                        }
                    }
                }
            }

            if (fm.getBackStackEntryCount() == 0)
                logout();
        }
    }

    /**
     * 登出
     */
    private void logout()
    {
        mDialogFragment = CustomDialog.newInstance(R.layout.custom_dialog, getString(R.string.OK), getString(R.string.CHECK_LOGOUT), getResources().getColor(R.color.colorPrimary));
        mDialogFragment.setOnBtnClickListener(new BaseDialogFragment.OnBtnClickListener() {
            @Override
            public void onOkClick() {
                // 需要退出程序時，調用
                AppManager.getAppManager().AppExit(MenuActivity.this);
            }

            @Override
            public void onCancelClick() {
                // to do nothing
            }
        });

        mDialogFragment.setCancelable(false);
        mDialogFragment.show(getSupportFragmentManager(), "dialog");
    }
}
