package com.example.user.zippo.custom;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.user.zippo.R;

/**
 * 根據 Layout 建立
 */
public abstract class BaseDialogFragment extends DialogFragment
{
    protected int mLayout, mColor;
    protected String mTitle, mMessage;
    protected static final String TAG_ARG = "layout";
    protected static final int DEFAULT_COLOR = -1; //default

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        //Step 1 build Dialog
        if (mLayout == 0)
        {
            throw new RuntimeException("no correct layout found.");
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(mLayout, null);

        TextView txtTitle = (TextView) view.findViewById(R.id.txtTitle);
        TextView txtMessage = (TextView) view.findViewById(R.id.txtMessage);

        txtTitle.setText(mTitle);
        txtTitle.setTextColor(mColor);
        txtMessage.setText(mMessage);

        builder.setView(view).setPositiveButton(getOkText(), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                if (mOnBtnClickListener != null)
                    mOnBtnClickListener.onOkClick();
            }
        }).setNegativeButton(getCancelText(), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                if (mOnBtnClickListener != null)
                    mOnBtnClickListener.onCancelClick();
            }
        }).setCancelable(false); // 關閉

        AlertDialog alertDialog = builder.create(); // 建立

        //Step 2 custom Button
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener()
        {
            @Override
            public void onShow(DialogInterface dialog) //只有在 Show 之后才能getButton
            {
                Button okButton = ((AlertDialog) getDialog()).getButton(DialogInterface.BUTTON_POSITIVE);
                Button cancelButton = ((AlertDialog) getDialog()).getButton(DialogInterface.BUTTON_NEGATIVE);

                if (getOkTextColorRes() != DEFAULT_COLOR)
                {
                    okButton.setTextColor(getResources().getColor(getOkTextColorRes()));
                }

                if (getCancelTextColorRes() != DEFAULT_COLOR)
                {
                    cancelButton.setTextColor(getResources().getColor(getCancelTextColorRes()));
                }

                if (getOkBgColorRes() != DEFAULT_COLOR)
                {
                    okButton.setBackgroundResource(getOkBgColorRes());
                }

                if (getCancelBgColorRes() != DEFAULT_COLOR)
                {
                    cancelButton.setBackgroundResource(getCancelBgColorRes());
                }
            }
        });

        //Step 3 request feature
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        return alertDialog;
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
    }

    public interface OnBtnClickListener
    {
        public void onOkClick();
        public void onCancelClick();
    }

    private OnBtnClickListener mOnBtnClickListener;

    /**
     * 添加Button的Click监听
     *
     * @param onBtnClickListener
     */
    public void setOnBtnClickListener(OnBtnClickListener onBtnClickListener)
    {
        mOnBtnClickListener = onBtnClickListener;
    }

    /**
     * 用于设定Positive Button的Text颜色
     * @return Color的Resource Id
     */
    protected abstract int getOkTextColorRes(); //text 只能是color

    /**
     * 用于设定Positive Button的背景颜色
     * @return Color或者Drawable的Resource Id
     */
    protected abstract int getOkBgColorRes();

    /**
     * 用于设定Negative Button的Text颜色
     * @return Color的Resource Id
     */
    protected abstract int getCancelTextColorRes();

    /**
     * 用于设定Negative Button的背景颜色
     * @return Color或者Drawable的Resource Id
     */
    protected abstract int getCancelBgColorRes();

    protected abstract String getOkText();

    protected abstract String getCancelText();
}
