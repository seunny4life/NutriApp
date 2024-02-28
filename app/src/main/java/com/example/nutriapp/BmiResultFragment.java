
package com.example.nutriapp;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class BmiResultFragment extends Fragment {

    private TextView mbmidisplay, mbmicategory, mgender;
    private Button mgotomain;
    private ImageView mimageview;
    // No need for a separate RelativeLayout for the background

    public BmiResultFragment() {
        // Required empty public constructor
    }

    public static BmiResultFragment newInstance(float height, float weight, String gender) {
        BmiResultFragment fragment = new BmiResultFragment();
        Bundle args = new Bundle();
        args.putFloat("height", height);
        args.putFloat("weight", weight);
        args.putString("gender", gender);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Directly use the ConstraintLayout for background changes
        View view = inflater.inflate(R.layout.fragment_bmi_result, container, false);

        mbmidisplay = view.findViewById(R.id.bmidisplay);
        mbmicategory = view.findViewById(R.id.bmicategorydispaly);
        mgender = view.findViewById(R.id.genderdisplay);
        mgotomain = view.findViewById(R.id.gotomain);
        mimageview = view.findViewById(R.id.imageview);

        if (getArguments() != null) {
            float height = getArguments().getFloat("height");
            float weight = getArguments().getFloat("weight");
            String gender = getArguments().getString("gender");

            calculateAndDisplayBMI(view, height, weight, gender); // Pass the root view for background color changes
        }

        mgotomain.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        return view;
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    private void calculateAndDisplayBMI(View rootView, float height, float weight, String gender) {
        float heightInMeters = height / 100;
        float bmi = weight / (heightInMeters * heightInMeters);
        mbmidisplay.setText(String.format("%.2f", bmi));
        mgender.setText(gender);

        int backgroundColor;
        if (bmi < 16) {
            mbmicategory.setText("Severely underweight");
            backgroundColor = Color.RED;
            mimageview.setImageResource(R.drawable.cross);
        } else if (bmi < 16.9) {
            mbmicategory.setText("Moderately underweight");
            backgroundColor = Color.YELLOW;
            mimageview.setImageResource(R.drawable.warning);
        } else if (bmi < 18.4) {
            mbmicategory.setText("Slightly underweight");
            backgroundColor = Color.YELLOW;
            mimageview.setImageResource(R.drawable.warning);
        } else if (bmi < 24.9) {
            mbmicategory.setText("Normal");
            backgroundColor = Color.GREEN;
            mimageview.setImageResource(R.drawable.ok);
        } else if (bmi < 29.9) {
            mbmicategory.setText("Overweight");
            backgroundColor = Color.YELLOW;
            mimageview.setImageResource(R.drawable.warning);
        } else if (bmi < 34.9) {
            mbmicategory.setText("Obese Class I");
            backgroundColor = Color.parseColor("#FF5722");
            mimageview.setImageResource(R.drawable.warning);
        } else {
            mbmicategory.setText("Obese Class II");
            backgroundColor = Color.RED;
            mimageview.setImageResource(R.drawable.cross);
        }

        rootView.setBackgroundColor(backgroundColor); // Change the background color of the root view
    }
}
