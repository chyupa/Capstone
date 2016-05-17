package com.udacity.capstone.activities.register;

import android.app.Fragment;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

import com.udacity.capstone.R;
import com.udacity.capstone.activities.register.fragments.BasicInfoFragment;

public class RegisterActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Fragment step1Fragment = new BasicInfoFragment();

        getFragmentManager()
                .beginTransaction()
                .add(R.id.register, step1Fragment)
                .commit();
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }

}
