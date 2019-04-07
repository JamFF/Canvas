package com.ff.canvas.split;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ff.canvas.widget.SplitViewPlus;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * description:
 * author: FF
 * time: 2019/4/6 22:52
 */
public class SplitFragment extends Fragment {

    private SplitViewPlus mSplitView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mSplitView = new SplitViewPlus(getContext());
        return mSplitView;
    }

    @Override
    public void onDestroyView() {
        mSplitView.stopAnimator();
        super.onDestroyView();
    }
}
