package com.example.myapplicationfragment;

import android.animation.ObjectAnimator;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        Button on = (Button) v.findViewById(R.id.on);
        Button off= (Button) v.findViewById(R.id.off);
        TextView textView = (TextView) v.findViewById(R.id.textView);
        SwitchCompat switchCompat = v.findViewById(R.id.autoSwitch);
        ImageView motorImageView = v.findViewById(R.id.motor_on);


        //switch state save
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("save", Context.MODE_PRIVATE);
        switchCompat.setChecked(sharedPreferences.getBoolean("value",true));
        switchCompat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(switchCompat.isChecked()){
                    SharedPreferences.Editor editor = getActivity().getSharedPreferences("save",Context.MODE_PRIVATE).edit();
                    editor.putBoolean("value",true);
                    editor.apply();
                    switchCompat.setChecked(true);
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("Automate");



                    on.setEnabled(false);
                    off.setEnabled(false);

                    Toast.makeText(getActivity(),"Take a break! Automation is on",Toast.LENGTH_SHORT).show();

                    myRef.setValue("ON");
                    motorImageView.setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
                }
                else{
                    SharedPreferences.Editor editor = getActivity().getSharedPreferences("save",Context.MODE_PRIVATE).edit();
                    editor.putBoolean("value",false);
                    editor.apply();
                    switchCompat.setChecked(false);
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("Automate");

                    on.setEnabled(true);
                    off.setEnabled(true);


                    myRef.setValue("OFF");
                    motorImageView.setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);

                }
            }
        });
        //Button pressed
        on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("Switch State");

                myRef.setValue("ON");
                motorImageView.setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);

            }
        });
        off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("Switch State");

                myRef.setValue("OFF");
                motorImageView.setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
            }
        });


        //reading data
        DatabaseReference rootDatabaseref = FirebaseDatabase.getInstance().getReference().child("MOIST VALUE");
        rootDatabaseref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue().toString();
                //textView.setText("Water level: "+value);
                if(Float.parseFloat(value)<=20 && Float.parseFloat(value)>0 ) {
                    ((MainActivity)getActivity()).createNoti();
                }
                updateProgressBar(Float.parseFloat(value));

            }

   //Update progress bar function
            private void updateProgressBar(float progress) {
                ProgressBar progressBar = v.findViewById(R.id.progress_bar);
                TextView textViewProgress = v.findViewById(R.id.text_view_progress);
                //
                int progressInt = (int) progress;
                int preProgBar = progressBar.getProgress();
                progressBar.setProgress(progressInt);
                textViewProgress.setText(progress + "%");

                ProgressBar progreso;
                ObjectAnimator progressAnimator;
                progreso = (ProgressBar)v.findViewById(R.id.progress_bar);
                progressAnimator = ObjectAnimator.ofInt(progreso, "progress",preProgBar, progressInt);
                progressAnimator.setDuration(700);
                progressAnimator.start();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(),"Failed to get data.",Toast.LENGTH_SHORT).show();
            }
        });

        return v;
    }


}