package com.example.administrator.myviewpageindicator;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/3/24.
 */
public class VpSimplaeFragment extends Fragment {
    private String mTitle;
    public static final String BUNDLE_TITLE = "title";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Bundle bundle=getArguments();
        if (bundle!=null){
            mTitle=bundle.getString(BUNDLE_TITLE);
        }

        TextView mTextView=new TextView(getActivity());
        mTextView.setText(mTitle);
        mTextView.setGravity(Gravity.CENTER);



        return mTextView;
    }

    public static VpSimplaeFragment newInstance(String  title) {

        Bundle args = new Bundle();
        args.putString(BUNDLE_TITLE,title);
        VpSimplaeFragment fragment = new VpSimplaeFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
