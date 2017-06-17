package com.example.user.zippo;

import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.greendao.ZI_BlogData;
import com.example.greendao.ZI_EmailData;
import com.example.user.zippo.custom.BaseDialogFragment;
import com.example.user.zippo.custom.CustomDialog;
import com.example.user.zippo.custom.ErrorDialog;
import com.example.user.zippo.custom.RegexUtils;
import com.example.user.zippo.fragment.BackHandledFragment;
import com.example.user.zippo.store.BlogDataStore;
import com.example.user.zippo.store.EmailDataStore;

import java.util.ArrayList;
import java.util.List;

/**
 * 編輯 聯絡資料
 */
public class EditConnectionFragment extends BackHandledFragment implements View.OnClickListener
{
    private ImageView mImageViewClose;
    private BaseDialogFragment mDialogFragmentC; // Close
    private BaseDialogFragment mDialogFragmentM; // Mistake
    private TextView mAddBlog, mAddEmail, mTxtSave;
    private EditText mEdtBlog, mEdtEmail;
    private LinearLayout mLayoutMail, mLayoutBlog; // 動態 LinearLayout
    private boolean hadIntercept; // 監聽返回參數
    private boolean _confirmOpenEmail = false; // Email 是否打開
    private boolean _confirmOpenBlog = false; // Blog 是否打開
    private EditText edtBlog, edtEmail; // 動態新增
    private ArrayList emailArray = new ArrayList();
    private ArrayList blogArray = new ArrayList();
    private ArrayList misTakeArray = new ArrayList(); // 錯誤訊息
    private ArrayList emptyArray = new ArrayList(); // 空白訊息
    private Dialog errorDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_edit_connection, container, false);

        mImageViewClose = (ImageView) view.findViewById(R.id.imageViewClose);
        mImageViewClose.setOnClickListener(this);

        mAddEmail = (TextView) view.findViewById(R.id.addEmail);
        mAddBlog = (TextView) view.findViewById(R.id.addBlog);
        mTxtSave = (TextView) view.findViewById(R.id.txtSave);

        mAddEmail.setOnClickListener(this);
        mAddBlog.setOnClickListener(this);
        mTxtSave.setOnClickListener(this);

        mEdtEmail = (EditText) view.findViewById(R.id.edtEmail);
        mEdtBlog = (EditText) view.findViewById(R.id.edtBlog);

        // 取得 LinearLayout 動態 物件
        mLayoutMail = (LinearLayout)view.findViewById(R.id.linearLayoutMail);
        mLayoutBlog = (LinearLayout)view.findViewById(R.id.linearLayoutBlog);

        EmailDataStore emailDataStore = new EmailDataStore(getActivity()); // 資料庫 有資料 帶入
        List<ZI_EmailData> data = emailDataStore.getList();

        BlogDataStore blogDataStore = new BlogDataStore(getActivity()); // 若資料庫 有資料 帶入
        List<ZI_BlogData> data1 = blogDataStore.getList();

        if(data.size() != 0)
        {
            mEdtEmail.setText(data.get(0).getEmail().toString()); // main
            if(data.size() == 2)
            {
                showLayoutEmail();
                edtEmail.setText(data.get(1).getEmail().toString()); // backup
            }
        }

        if(data1.size() != 0)
        {
            mEdtBlog.setText(data1.get(0).getBlog().toString());
            if(data1.size() == 2)
            {
                showLayoutBlog();
                edtBlog.setText(data1.get(1).getBlog().toString());
            }
        }

        return view;
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.imageViewClose: // 關閉

                closeFragment1();

                break;
            case R.id.addEmail: // 新增

                if(_confirmOpenEmail == false)
                {
                    showLayoutEmail(); // 加入動態 LinearLayout
                } else {
                    _confirmOpenEmail = false;

                    mAddEmail.setText(getString(R.string.ADD));
                    mAddEmail.setTextColor(getResources().getColor(R.color.colorPrimary));
                    mAddEmail.setTextSize(15);
                    Drawable drawable= getResources().getDrawable(R.drawable.ic_add_black_24dp);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    mAddEmail.setCompoundDrawables(drawable,null,null,null);

                    mLayoutMail.removeAllViews();
                }

                break;

            case R.id.addBlog: // 新增

                if(_confirmOpenBlog == false)
                {
                    showLayoutBlog(); // 加入動態 LinearLayout
                } else {
                    _confirmOpenBlog = false;

                    mAddBlog.setText(getString(R.string.ADD));
                    mAddBlog.setTextColor(getResources().getColor(R.color.colorPrimary));
                    mAddBlog.setTextSize(15);
                    Drawable drawable= getResources().getDrawable(R.drawable.ic_add_black_24dp);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    mAddBlog.setCompoundDrawables(drawable,null,null,null);

                    mLayoutBlog.removeAllViews();
                }

                break;
            case R.id.txtSave: // 儲存

                misTakeArray.clear(); // 要先清除陣列中資料
                emailArray.clear(); // 儲存使用
                blogArray.clear(); // 儲存使用
                emptyArray.clear(); // 未使用

                if(mEdtEmail.getText().toString().trim().equals(""))
                {
                    emptyArray.add(getString(R.string.MAIL_TABLE)); // 輸入欄位為空
                } else {
                    emailArray.add(mEdtEmail.getText().toString()); // 加入陣列
                }

                if(_confirmOpenEmail == true)
                {
                    if(edtEmail.getText().toString().trim().equals(""))
                    {
                        emptyArray.add(getString(R.string.BACKUP_MAIL_TABLE));
                    } else {
                        emailArray.add(edtEmail.getText().toString()); // 加入陣列
                    }
                }

                if(mEdtBlog.getText().toString().trim().equals(""))
                {
                    emptyArray.add(getString(R.string.BLOG_ADDRESS_TABLE));
                } else {
                    blogArray.add(mEdtBlog.getText().toString());
                }

                if(_confirmOpenBlog == true)
                {
                    if(!edtBlog.getText().toString().trim().equals(""))
                    {
                        blogArray.add(edtBlog.getText().toString());
                    } else {
                        emptyArray.add(getString(R.string.BACKUP_BLOG_ADDRESS_TABLE));
                    }
                }

                if(checkInputType()) // 檢查格式有誤
                {
                    if(!emptyArray.isEmpty())
                    {
                        misTakeFragment(emptyArray);
                    }

                    if(!emailArray.isEmpty() && !blogArray.isEmpty() && emptyArray.isEmpty())
                    {
                        storeEmail(emailArray); // 存入資料庫
                        storeBlog(blogArray);
                        dialogShow();
                    }
                }

                break;
            default:
                break;
        }
    }

    /**
     * 檢查格式
     */
    private boolean checkInputType()
    {
        boolean aInputBoolean = true;

        if(!RegexUtils.isValidEmailAddress(mEdtEmail.getText().toString()))
        {
            aInputBoolean = false;
            mEdtEmail.setError(getString(R.string.ERROR_INPUTTYPE));
        }

        if(_confirmOpenEmail == true)
        {
            if(!RegexUtils.isValidEmailAddress(edtEmail.getText().toString()))
            {
                aInputBoolean = false;
                edtEmail.setError(getString(R.string.ERROR_INPUTTYPE));
            }
        }

        if (!URLUtil.isValidUrl(mEdtBlog.getText().toString())) {
            aInputBoolean = false;
            mEdtBlog.setError(getString(R.string.ERROR_INPUTTYPE));
        }

        if(_confirmOpenBlog == true) {
            if (!URLUtil.isValidUrl(edtBlog.getText().toString())) {
                aInputBoolean = false;
                edtBlog.setError(getString(R.string.ERROR_INPUTTYPE));
            }
        }

        return aInputBoolean;
    }

    public void showLayoutEmail()
    {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams
                (
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );

        TextView txtMail = new TextView(getActivity());
        txtMail.setText(getString(R.string.BACKUP_MAIL));
        txtMail.setTextSize(15);
        txtMail.setTextColor(getResources().getColor(R.color.BLACK));
        txtMail.setPadding(0, 10, 0 ,0);

        edtEmail = new EditText(getActivity());
        edtEmail.setTextSize(15);

        mLayoutMail.addView(txtMail, layoutParams);
        mLayoutMail.addView(edtEmail, layoutParams);

        _confirmOpenEmail = true;

        mAddEmail.setText(getString(R.string.REMOVE));
        mAddEmail.setTextColor(getResources().getColor(R.color.colorPrimary));
        mAddEmail.setTextSize(15);
        Drawable drawable= getResources().getDrawable(R.drawable.ic_remove_black_24dp);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        mAddEmail.setCompoundDrawables(drawable,null,null,null);
    }

    public void showLayoutBlog()
    {
        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams
                (
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );

        TextView txtBlog = new TextView(getActivity());
        txtBlog.setText(getString(R.string.BACKUP_BLOG_ADDRESS));
        txtBlog.setTextSize(15);
        txtBlog.setTextColor(getResources().getColor(R.color.BLACK));
        txtBlog.setPadding(0, 10, 0 ,0);

        edtBlog = new EditText(getActivity());
        edtBlog.setTextSize(15);

        mLayoutBlog.addView(txtBlog, layoutParams1);
        mLayoutBlog.addView(edtBlog, layoutParams1);

        _confirmOpenBlog = true;

        mAddBlog.setText(getString(R.string.REMOVE));
        mAddBlog.setTextColor(getResources().getColor(R.color.colorPrimary));
        mAddBlog.setTextSize(15);
        Drawable drawable= getResources().getDrawable(R.drawable.ic_remove_black_24dp);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        mAddBlog.setCompoundDrawables(drawable,null,null,null);
    }

    /**
     * 儲存資料
     */
    public void storeEmail(ArrayList mArray)
    {
        EmailDataStore emailDataStore = new EmailDataStore(getActivity());

        ArrayList data = new ArrayList<ZI_EmailData>();
        for (int i = 0; i < mArray.size() ; i++)
        {
            data.add(
                    new ZI_EmailData(
                            null,
                            String.valueOf(i+1),
                            mArray.get(i).toString()
                    )
            );
        }
        emailDataStore.storeEmailList(data);
    }

    public void storeBlog(ArrayList mArray)
    {
        BlogDataStore blogDataStore = new BlogDataStore(getActivity());

        ArrayList data = new ArrayList<ZI_BlogData>();
        for (int i = 0; i < mArray.size() ; i++)
        {
            data.add(
                    new ZI_BlogData(
                            null,
                            String.valueOf(i+1),
                            mArray.get(i).toString()
                    )
            );
        }
        blogDataStore.storeBlogList(data);

    }

    public void misTakeFragment(ArrayList misTakeArray)
    {
        String misTakeString = "";

        for (int i = 0; i < misTakeArray.size() ; i++)
        {
            misTakeString += misTakeArray.get(i).toString() + " ";
        }

        mDialogFragmentM = CustomDialog.newInstance(R.layout.custom_dialog, getString(R.string.ERROR),
                misTakeString + getString(R.string.NULL), getResources().getColor(R.color.RED));

        if (!mDialogFragmentM.isAdded())
        {
            mDialogFragmentM.setOnBtnClickListener(new BaseDialogFragment.OnBtnClickListener()
            {
                @Override
                public void onOkClick()
                {

                }

                @Override
                public void onCancelClick()
                {

                }
            });

            mDialogFragmentM.setCancelable(false);
            mDialogFragmentM.show(getActivity().getSupportFragmentManager(), "dialogMistake");
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

    public void closeFragment1()
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
            closeFragment1();
            return true;
        }
    }
}
