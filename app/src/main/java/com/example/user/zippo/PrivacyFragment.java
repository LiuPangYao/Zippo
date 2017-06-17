package com.example.user.zippo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.user.zippo.fragment.BackHandledFragment;

/**
 * 隱私權政策
 */
public class PrivacyFragment extends BackHandledFragment implements View.OnClickListener
{
    private ImageView mImageViewBack;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_privacy, container, false);
        mImageViewBack = (ImageView)view.findViewById(R.id.imageViewBack);
        mImageViewBack.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.imageViewBack:
                getActivity().onBackPressed();
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
