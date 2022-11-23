package com.example.myapplicationfragment;


import android.animation.ObjectAnimator;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class WaterFragment extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_water, container, false);

        //reading data
        DatabaseReference rootDatabaseref = FirebaseDatabase.getInstance().getReference().child("tankLevel");
        rootDatabaseref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String tankValue = snapshot.getValue().toString();
                updateTankProgressBar(Integer.parseInt(tankValue));

            }

            //Update progress bar function
            private void updateTankProgressBar(int progress) {
                ProgressBar tankProgressBar = v.findViewById(R.id.tank_progress_bar);
                TextView textViewProgress = v.findViewById(R.id.text_view_tank_progress);
                //
                int progressInt = (int) progress;
                int preProgBar = tankProgressBar.getProgress();
                tankProgressBar.setProgress(progressInt);
                textViewProgress.setText(progress + "%");

                ProgressBar progreso;
                ObjectAnimator progressAnimator;
                progreso = (ProgressBar)v.findViewById(R.id.tank_progress_bar);
                progressAnimator = ObjectAnimator.ofInt(progreso, "progress",preProgBar, progressInt);
                progressAnimator.setDuration(1000);
                progressAnimator.start();

                if (progress<=10 && progress>0){
                    Toast.makeText(getActivity(),"Tank is getting empty!",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(),"Failed to get data.",Toast.LENGTH_SHORT).show();
            }
        });

        return v;
    }

}
