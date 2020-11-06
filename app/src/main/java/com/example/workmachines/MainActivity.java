package com.example.workmachines;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private Spinner spinner;
    private Switch darkSwitch;
    private TextView time, date;
    private Calendar calendar;
    static int machine_hours = 0;
    static int machine_min = 0;
    static int job_hours = 0;
    static int job_min = 0;
    boolean machineHasStarted = false;
    boolean jobHasStarted = false;
    long myDate1 = 0, myDate2 = 0;
    long myDate3 = 0, myDate4 = 0;
    String machine_start_time;
    String machine_start_time_12hr;
    String machine_end_time;
    String machine_end_time_12hr;
    String job_start_time;
    String job_start_time_12hr;
    String job_end_time;
    String job_end_time_12hr;
    String time_difference = "";
    String my_time_difference = "";
    private TextView upTime, machineName, machineStartTime, machineEndTime, machineRunningTime,
            jobStartTime, jobEndTime, jobTime, timeDifference;
    private DatabaseReference sensor1Reference = FirebaseDatabase.getInstance().getReference("sensor_f1");
    private DatabaseReference sensor2Reference = FirebaseDatabase.getInstance().getReference("sensor_f2");

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.DarkTheme);
        } else setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initializing Views
        darkSwitch = findViewById(R.id.darkSwitch);
        spinner = findViewById(R.id.machineSpinner);
        time = findViewById(R.id.text_view_time);
        date = findViewById(R.id.text_view_date);

        upTime = findViewById(R.id.up_time);

        machineName = findViewById(R.id.text_view_machineName);
        machineStartTime = findViewById(R.id.machine_start_time);
        machineEndTime = findViewById(R.id.machine_end_time);
        machineRunningTime = findViewById(R.id.machine_running_time);

        jobStartTime = findViewById(R.id.job_start_time);
        jobEndTime = findViewById(R.id.job_end_time);
        jobTime = findViewById(R.id.job_time);

        timeDifference = findViewById(R.id.time_difference);

        //Checking Dark mode on startup
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            darkSwitch.setChecked(true);
        }

        //Date & Time
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                calendar = Calendar.getInstance();
                                String myTime = DateFormat.getTimeInstance(DateFormat.DATE_FIELD).format(calendar.getTime());
                                String myDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
                                time.setText(myTime);
                                date.setText(myDate);
                            }
                        });
                    }
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        };
        thread.start();

        //Machine Spinner
        final String[] spinnerValues = {"Welding Gantry", "Beam Maker Gantry - 1"
//                , "Beam Maker Gantry - 2", "CNC Flame Cutting Gantry",
//                "PLC Flame Cutting Gantry", "ESAB CNC Cutting", "VMC Machine", "Beam Rotator", "Iron Worker Machine", "Shot Blasting Machine"
        };

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, spinnerValues);
        spinner.setAdapter(arrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

//                int up_time_hr = 2;
//                int up_time_min = 48;
//                int up_time_sec = 19;
//
//                int machine_start_hr = 10;
//                int machine_start_min = 15;
//                int machine_end_hr = 11;
//                int machine_end_min = 0;
//                int machine_running_hr = 0;
//                int machine_running_min = 45;
//
//                int job_start_hr = 10;
//                int job_start_min = 0;
//                int job_end_hr = 12;
//                int job_end_min = 0;
//                int job_hr = 2;
//                int job_min = 0;
//
//                int time_difference_hr = 1;
//                int time_difference_min = 15;
//
                machineName.setText(spinnerValues[position]);
//                switch (position){
//                    case 0:
//                        upTime.setText("Up Time : 0" +up_time_hr+ ":" +up_time_min+ ":" +up_time_sec);
//                        machineStartTime.setText(machine_start_hr +":"+ machine_start_min +" AM");
//                        machineEndTime.setText(machine_end_hr +":0"+ machine_end_min +" AM");
//                        machineRunningTime.setText(machine_running_hr +"0 hr "+ machine_running_min +" min");
//                        jobStartTime.setText(job_start_hr +":0"+ job_start_min +" AM");
//                        jobEndTime.setText(job_end_hr +":0"+ job_end_min +" PM");
//                        jobTime.setText(job_hr +" hr "+ job_min +"0 min");
//                        timeDifference.setText("JOB 001: "+ time_difference_hr +" hr "+ time_difference_min +" min");
//                        break;
//                    case 1:
//                        upTime.setText("Up Time : 0" +(up_time_hr+1)+ ":" +(up_time_min+5)+ ":" +(up_time_sec+4));
//                        machineStartTime.setText(machine_start_hr +":"+ (machine_start_min+5) +" AM");
//                        machineEndTime.setText(machine_end_hr +":0"+ (machine_end_min+5) +" AM");
//                        machineRunningTime.setText(machine_running_hr +"0 hr "+ (machine_running_min+5) +" min");
//                        jobStartTime.setText(job_start_hr +":0"+ (job_start_min+5) +" AM");
//                        jobEndTime.setText(job_end_hr +":0"+ (job_end_min+5) +" PM");
//                        jobTime.setText(job_hr +" hr 0"+ (job_min+5) +" min");
//                        timeDifference.setText("JOB 001: "+ time_difference_hr +" hr "+ (time_difference_min+5) +" min");
//                        break;
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        // Switch for Dark Mode
        darkSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    getDelegate().applyDayNight();
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    getDelegate().applyDayNight();
                }
            }
        });

        // Fetching data from sensor and displaying time
        sensor1Reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int value = snapshot.getValue(int.class);

                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                SimpleDateFormat dateFormat12hr = new SimpleDateFormat("hh:mm", Locale.getDefault());


                Date date = null, date1, date2, date3 = null;

                if (value == 1) {
                    machine_start_time = dateFormat.format(new Date());
                    try {
                        date = dateFormat12hr.parse(machine_start_time);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    machine_start_time_12hr = dateFormat12hr.format(date);

                    if (machine_start_time_12hr.equals(machine_start_time)) {
                        machine_start_time_12hr = machine_start_time_12hr + " AM";
                    } else {
                        machine_start_time_12hr = machine_start_time_12hr + " PM";
                    }

                    machineStartTime.setText(machine_start_time_12hr);
                    machineHasStarted = true;
                    try {
                        date1 = dateFormat.parse(machine_start_time);
                        myDate1 = date1.getTime();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else if (value == 0 && machineHasStarted) {
                    machine_end_time = dateFormat.format(new Date());
                    try {
                        date3 = dateFormat12hr.parse(machine_end_time);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    machine_end_time_12hr = dateFormat12hr.format(date3);

                    if (machine_end_time_12hr.equals(machine_end_time)) {
                        machine_end_time_12hr = machine_end_time_12hr + " AM";
                    } else {
                        machine_end_time_12hr = machine_end_time_12hr + " PM";
                    }

                    machineEndTime.setText(machine_end_time_12hr);
                    try {
                        date2 = dateFormat.parse(machine_end_time);
                        myDate2 = date2.getTime();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    long difference = myDate2 - myDate1;
                    machine_hours = (int) (difference / (60 * 60 * 1000) % 24);
                    machine_min = (int) (difference / (60 * 1000) % 60);
                    machine_hours = (machine_hours < 0 ? -machine_hours : machine_hours);
                    machine_min = (machine_min < 0 ? -machine_min : machine_min);
                    machineRunningTime.setText(machine_hours + " hr " + " : " + machine_min + " min");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        sensor2Reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int value = snapshot.getValue(int.class);

                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                SimpleDateFormat dateFormat12hr = new SimpleDateFormat("hh:mm", Locale.getDefault());

                Date date1, date2, date3 = null, date4 = null;

                if (value == 0) {
                    job_start_time = dateFormat.format(new Date());
                    try {
                        date3 = dateFormat12hr.parse(job_start_time);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    job_start_time_12hr = dateFormat12hr.format(date3);

                    if (job_start_time_12hr.equals(job_start_time)) {
                        job_start_time_12hr = job_start_time_12hr + " AM";
                    } else {
                        job_start_time_12hr = job_start_time_12hr + " PM";
                    }

                    jobStartTime.setText(job_start_time_12hr);
                    jobHasStarted = true;
                    try {
                        date1 = dateFormat.parse(job_start_time);
                        myDate3 = date1.getTime();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else if (value == 1 && jobHasStarted) {
                    job_end_time = dateFormat.format(new Date());

                    try {
                        date4 = dateFormat12hr.parse(job_end_time);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    job_end_time_12hr = dateFormat12hr.format(date4);

                    if (job_end_time_12hr.equals(job_end_time)) {
                        job_end_time_12hr = job_end_time_12hr + " AM";
                    } else {
                        job_end_time_12hr = job_end_time_12hr + " PM";
                    }

                    jobEndTime.setText(job_end_time_12hr);
                    try {
                        date2 = dateFormat.parse(job_end_time);
                        myDate4 = date2.getTime();

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    long difference = myDate4 - myDate3;
                    job_hours = (int) (difference / (60 * 60 * 1000) % 24);
                    job_min = (int) (difference / (60 * 1000) % 60);
                    job_hours = (job_hours < 0 ? -job_hours : job_hours);
                    job_min = (job_min < 0 ? -job_min : job_min);
                    jobTime.setText(job_hours + " hr " + " : " + job_min + " min");
                }
                System.out.println("--------------------------------");
                System.out.println("--------------------------------");
                System.out.println("--------------------------------");
                System.out.println("--------------CALLED------------");
                System.out.println("----job_min----" + job_min);
                System.out.println("----machine_min---" + machine_min);
                System.out.println("---job_hours----" + job_hours);
                System.out.println("---machine_hours----" + machine_hours);
                if ((job_min != 0 || machine_min != 0) || (job_hours != 0 || machine_hours != 0)) {
                    timeDifference();
                    job_min = 0;
                    machine_min = 0;
                    job_hours = 0;
                    machine_hours = 0;
                    machineHasStarted = false;
                    jobHasStarted = false;

                    time_difference = "";
                    my_time_difference = "";
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    void timeDifference() {
        int hours = job_hours - machine_hours;
        int mins = job_min - machine_min;
        hours = (hours < 0 ? -hours : hours);
        mins = (mins < 0 ? -mins : mins);
        time_difference = time_difference + hours + " hr " + " : " + mins + " min\n";

        Upload upload = new Upload(time_difference);
        FirebaseDatabase.getInstance().getReference("time_difference").push()
                .setValue(upload).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(MainActivity.this, "Data Saved !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void setTimeDifference() {
        FirebaseDatabase.getInstance().getReference("time_difference")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        my_time_difference = "";
                        int i = 1;
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Upload value = dataSnapshot.getValue(Upload.class);
                            my_time_difference = my_time_difference + "JOB " + i + ": " + value.getTime_difference();
                            timeDifference.setText(my_time_difference);
                            i++;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTimeDifference();
    }
}