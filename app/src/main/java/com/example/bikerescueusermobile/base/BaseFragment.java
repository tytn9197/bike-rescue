package com.example.bikerescueusermobile.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.support.DaggerFragment;

public abstract class BaseFragment extends DaggerFragment {

    private Unbinder unbinder;
    private AppCompatActivity activity;
//    public LifecycleRegistry lifecycleRegistry;

    @LayoutRes
    protected abstract int layoutRes();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(layoutRes(),container,false);
        unbinder = ButterKnife.bind(this,view);
//        lifecycleRegistry = new LifecycleRegistry(this);
//        lifecycleRegistry.markState(Lifecycle.State.CREATED);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        lifecycleRegistry.markState(Lifecycle.State.CREATED);
        activity = (AppCompatActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    public AppCompatActivity getBaseActivity(){
        return activity;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if( unbinder != null ){
            unbinder.unbind();
            unbinder = null;
//            lifecycleRegistry.markState(Lifecycle.State.DESTROYED);
        }
    }
}
