package com.example.nutriapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

public class BMIFragment extends Fragment {

    private TextView mcurrentheight;
    private TextView mcurrentweight, mcurrentage;
    private ImageView mincrementage, mdecrementage, mincrementweight, mdecrementweight;
    private SeekBar mseekbarforheight;
    private Button mcalculatebmi;
    private RelativeLayout mmale, mfemale;

    private int intweight = 55;
    private int intage = 22;
    private int currentprogress;
    private String mintprogress = "170";
    private String typerofuser = "0";
    private String weight2 = "55";
    private String age2 = "22";

    public BMIFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bmi, container, false);

        mcurrentheight = view.findViewById(R.id.currentheight);
        mcurrentweight = view.findViewById(R.id.currentweight);
        mcurrentage = view.findViewById(R.id.currentage);
        mincrementage = view.findViewById(R.id.incrementage);
        mdecrementage = view.findViewById(R.id.decrementage);
        mincrementweight = view.findViewById(R.id.incremetweight);
        mdecrementweight = view.findViewById(R.id.decrementweight);
        mcalculatebmi = view.findViewById(R.id.calculatebmi);
        mseekbarforheight = view.findViewById(R.id.seekbarforheight);
        mmale = view.findViewById(R.id.male);
        mfemale = view.findViewById(R.id.female);

        mmale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mmale.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.malefemalefocus));
                mfemale.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.malefemalenotfocus));
                typerofuser = "Male";
            }
        });

        mfemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mfemale.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.malefemalefocus));
                mmale.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.malefemalenotfocus));
                typerofuser = "Female";
            }
        });

        mseekbarforheight.setMax(300);
        mseekbarforheight.setProgress(170);
        mseekbarforheight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                currentprogress = progress;
                mintprogress = String.valueOf(currentprogress);
                mcurrentheight.setText(mintprogress + " cm");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        mincrementweight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intweight++;
                weight2 = String.valueOf(intweight);
                mcurrentweight.setText(weight2 + " kg");
            }
        });

        mincrementage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intage++;
                age2 = String.valueOf(intage);
                mcurrentage.setText(age2 + " years");
            }
        });

        mdecrementage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intage--;
                age2 = String.valueOf(intage);
                mcurrentage.setText(age2 + " years");
            }
        });

        mdecrementweight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intweight--;
                weight2 = String.valueOf(intweight);
                mcurrentweight.setText(weight2 + " kg");
            }
        });

        mcalculatebmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (typerofuser.equals("0")) {
                    Toast.makeText(requireContext(), "Select Your Gender First", Toast.LENGTH_SHORT).show();
                } else if (mintprogress.equals("0")) {
                    Toast.makeText(requireContext(), "Select Your Height First", Toast.LENGTH_SHORT).show();
                } else if (intage == 0 || intage < 0) {
                    Toast.makeText(requireContext(), "Age is Incorrect", Toast.LENGTH_SHORT).show();
                } else if (intweight == 0 || intweight < 0) {
                    Toast.makeText(requireContext(), "Weight Is Incorrect", Toast.LENGTH_SHORT).show();
                } else {
                    navigateToBmiResultFragment();
                }
            }
        });

        return view;
    }

    private void navigateToBmiResultFragment() {
        double height = Double.parseDouble(mintprogress);
        double weight = Double.parseDouble(weight2);
        BmiResultFragment bmiResultFragment = BmiResultFragment.newInstance((float) height, (float) weight, typerofuser);
        FragmentManager fragmentManager = getChildFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, bmiResultFragment)
                .addToBackStack(null)
                .commit();
    }
}
