package com.foo.umbrella.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.foo.umbrella.R;
import com.foo.umbrella.data.AppData;

/**
 * Created by gollaba on 9/17/17.
 */

public class SettingsFragment extends android.support.v4.app.Fragment implements FragmentManager.OnBackStackChangedListener {

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    private static String[] units = new String[]{"Centigrade", "Farenheit"};


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        LinearLayout l = (LinearLayout)view.findViewById(R.id.zipCodeLayout);
        l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                ZipCodeSelector zipCodeSelector = ZipCodeSelector.newInstance("");
                zipCodeSelector.setDismissListener(new DialogFragmentDismissListener() {
                    @Override
                    public void onDimiss(boolean toUpdate) {
                        if(toUpdate)
                            updateUI();
                    }
                });
                zipCodeSelector.show(fm, "zipcode");

            }
        });

        LinearLayout l1 = (LinearLayout)view.findViewById(R.id.temperaturesettingslayout);
        l1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                MetricsSelector metricsSelector = MetricsSelector.newInstance("");
                metricsSelector.setDismissListener(new DialogFragmentDismissListener() {
                    @Override
                    public void onDimiss(boolean toUpdate) {
                        if(toUpdate)
                            updateUI();
                    }
                });
                metricsSelector.show(fm, "metrics");

            }
        });
        getActivity().getSupportFragmentManager().addOnBackStackChangedListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Toolbar myToolbar = (Toolbar) getView().findViewById(R.id.fragmenttoolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(myToolbar);


    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        View rootView = getView();
        TextView zipCodeView = (TextView)rootView.findViewById(R.id.zipCodeView);
        zipCodeView.setText(AppData.instance(getActivity()).getZipCode());
        TextView tempView = (TextView)rootView.findViewById(R.id.tempMetricsView);
        tempView.setText(units[AppData.instance(getActivity()).getMetricsSetting()]);
    }

    @Override
    public void onBackStackChanged() {
        if(getActivity() != null) {
            boolean b = ((AppCompatActivity) getActivity()).getSupportFragmentManager().getBackStackEntryCount() > 0;
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(b);
        }
    }


}
