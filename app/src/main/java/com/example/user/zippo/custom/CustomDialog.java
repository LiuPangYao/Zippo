package com.example.user.zippo.custom;

import android.os.Bundle;

import com.example.user.zippo.R;

public class CustomDialog extends BaseDialogFragment
{
    public static BaseDialogFragment newInstance(int layout, String Title, String Message, int Color)
    {
        CustomDialog dialogFragment = new CustomDialog();
        Bundle args = new Bundle();
        args.putInt(TAG_ARG, layout);
        args.putInt("Color", Color);
        args.putString("Title", Title);
        args.putString("Message", Message);
        dialogFragment.setArguments(args);

        return dialogFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null)
        {
            mLayout = arguments.getInt(TAG_ARG, 0);
            mColor = arguments.getInt("Color");
            mTitle = arguments.getString("Title");
            mMessage = arguments.getString("Message");
        }
    }

    @Override
    protected int getOkTextColorRes()
    {
        return R.color.BLACK;
    }

    @Override
    protected int getOkBgColorRes()
    {
        return R.drawable.selector_item_bg;
    }

    @Override
    protected int getCancelTextColorRes()
    {
        //return BaseDialogFragment.DEFAULT_COLOR;
        return R.color.BLACK;
    }

    @Override
    protected int getCancelBgColorRes()
    {
        return R.drawable.selector_item_bg;
    }

    @Override
    protected String getOkText()
    {
        return getString(R.string.OK);
    }

    @Override
    protected String getCancelText()
    {
        return getString(R.string.CANCEL);
    }
}
