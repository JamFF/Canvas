package com.ff.canvas.splash;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ff.canvas.R;

/**
 * description:
 * author: FF
 * time: 2019/4/7 14:03
 */
public class SplashFragment extends Fragment {

    private SplashView mSplashView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_demo, container, false);
        mSplashView = view.findViewById(R.id.splash);
        return view;
    }

    @Override
    public void onDestroyView() {
        if (mSplashView != null) {
            mSplashView.stopAnimator();
        }
        super.onDestroyView();
    }
}
