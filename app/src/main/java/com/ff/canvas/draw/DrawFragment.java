package com.ff.canvas.draw;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ff.canvas.R;

/**
 * description: 对比ViewGroup的dispatchDraw和onDraw
 * author: FF
 * time: 2019/4/7 14:03
 */
public class DrawFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_draw, container, false);
    }
}
