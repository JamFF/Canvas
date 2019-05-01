package com.ff.canvas;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.ff.canvas.draw.DrawFragment;
import com.ff.canvas.splash.SplashFragment;
import com.ff.canvas.save.SaveRestoreFragment;
import com.ff.canvas.split.SplitFragment;
import com.ff.canvas.transform.TransformFragment;

public class MainActivity extends AppCompatActivity implements MainFragment.OnListItemClickListener {

    private FrameLayout mRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRoot = new FrameLayout(this);
        mRoot.setId(View.generateViewId());
        mRoot.setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        setContentView(mRoot);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(mRoot.getId(), new MainFragment())
                    .commit();
        }
    }

    @Override
    public void onListItemClick(int position) {
        Fragment fragment;
        switch (position) {
            case 0:// Canvas的transform效果
                fragment = new TransformFragment();
                break;
            case 1:// Canvas的save和restore
                fragment = new SaveRestoreFragment();
                break;
            case 2:// 爆裂效果
                fragment = new SplitFragment();
                break;
            case 3:// 自定义Splash
                fragment = new SplashFragment();
                break;
            case 4:// ViewGroup的draw
                fragment = new DrawFragment();
                break;
            default:
                return;

        }
        getSupportFragmentManager().beginTransaction()
                .replace(mRoot.getId(), fragment)
                .addToBackStack(null)
                .commit();
    }
}
