package com.example.myapplicationfragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class SettingsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_settings, container, false);


        EditText tankHeight = (EditText) v.findViewById(R.id.tankHeight);
        EditText minMoist = (EditText) v.findViewById(R.id.minMoist);
        EditText maxMoist= (EditText) v.findViewById(R.id.maxMoist);

        Button save= (Button) v.findViewById(R.id.save_reset_button);


        FirebaseDatabase database = FirebaseDatabase.getInstance();



        //save and reset button
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("Reset");

                myRef.setValue("YES");

                //write settings data

                int tankHeightText , minMoistText , maxMoistText;
                try {

                    tankHeightText = Integer.parseInt(tankHeight.getText().toString());
                    DatabaseReference myRefHeight = database.getReference("tankHeight");
                    myRefHeight.setValue(tankHeightText);

                    minMoistText = Integer.parseInt(minMoist.getText().toString());
                    DatabaseReference myRefMin = database.getReference("MinMoist");
                    myRefMin.setValue(minMoistText);

                    maxMoistText = Integer.parseInt(maxMoist.getText().toString());
                    DatabaseReference myRefMax = database.getReference("MaxMoist");
                    myRefMax.setValue(maxMoistText);
                }
                catch (NumberFormatException e){
                    Toast.makeText(getActivity(),"Please enter valid input",Toast.LENGTH_SHORT).show();}

            }
        });
        
        return v;
    }
}