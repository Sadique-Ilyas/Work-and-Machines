package com.example.workmachines;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private Spinner spinner;
    private Calendar calendar;

    TextView time, date;
    TextView machine_name;
    TextView up_time;
    TextView machine_start_time;
    TextView machine_end_time;
    TextView machine_running_time;
    TextView job_start_time;
    TextView job_end_time;
    TextView job_time;
    TextView time_difference;

    long machineStartTime = 0; // 2-2-2021 23:59
    long machineEndTime = 0; // 3-2-2021 00:04
    long runningTime = 0;
    long jobStartTime = 0; // 2-2-2021 23:57
    long jobEndTime = 0; // 3-2-2021 00:06
    long jobTime = 0;
    long upTime = 0;
    long timeDifference = 0;

    boolean machineHasStarted = false;
    boolean jobHasStarted = false;

    SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
//    long myUpTime = 0;
//    int upTime_hour = 0;
//    int upTime_min = 0;
//    static int machine_hours = 0;
//    static int machine_min = 0;
//    static int job_hours = 0;
//    static int job_min = 0;
//    boolean machineHasStarted = false;
//    boolean jobHasStarted = false;
//    long myDate1 = 0, myDate2 = 0;
//    long myDate3 = 0, myDate4 = 0;
//    long machine_difference = 0, job_difference = 0, time_diff = 0;
//    String machine_start_time;
//    String machine_start_time_12hr;
//    String machine_end_time;
//    String machine_end_time_12hr;
//    String job_start_time;
//    String job_start_time_12hr;
//    String job_end_time;
//    String job_end_time_12hr;
//    String time_difference = "";
//    String my_time_difference = "";
//    private TextView machineName, upTime, machineStartTime, machineEndTime, machineRunningTime,
//            jobStartTime, jobEndTime, jobTime, timeDifference;

    private DatabaseReference sensor1Reference = FirebaseDatabase.getInstance().getReference("sensor_f1");
    private DatabaseReference sensor2Reference = FirebaseDatabase.getInstance().getReference("sensor_f2");
    private DatabaseReference upTimeReference = FirebaseDatabase.getInstance().getReference("upTime");

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.DarkTheme);
        setContentView(R.layout.activity_main);

        //Initializing Views
        spinner = findViewById(R.id.machineSpinner);
        time = findViewById(R.id.text_view_time);
        date = findViewById(R.id.text_view_date);

        up_time = findViewById(R.id.up_time);

        machine_name = findViewById(R.id.text_view_machineName);
        machine_start_time = findViewById(R.id.machine_start_time);
        machine_end_time = findViewById(R.id.machine_end_time);
        machine_running_time = findViewById(R.id.machine_running_time);

        job_start_time = findViewById(R.id.job_start_time);
        job_end_time = findViewById(R.id.job_end_time);
        job_time = findViewById(R.id.job_time);

        time_difference = findViewById(R.id.time_difference);

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
                                String myTime = convertMillisTo12hr(getCurrentTime()).replace("a.m.", "AM").replace("p.m.", "PM");
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

        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.Spinner_Items,
                R.layout.color_spinner_layout
        );
        arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        spinner.setAdapter(arrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                machine_name.setText(parent.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Fetching data from sensor and displaying time
        sensor1Reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int value = snapshot.getValue(int.class);

//                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
//                SimpleDateFormat dateFormat12hr = new SimpleDateFormat("hh:mm", Locale.getDefault());


//                Date date = null, date1, date2, date3 = null;

                if (value == 1) {
//                    machine_start_time = dateFormat.format(new Date());
//                    try {
//                        date = dateFormat12hr.parse(machine_start_time);
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//                    machine_start_time_12hr = dateFormat12hr.format(date);
//
//                    if (machine_start_time_12hr.equals(machine_start_time)) {
//                        machine_start_time_12hr = machine_start_time_12hr + " AM";
//                    } else {
//                        machine_start_time_12hr = machine_start_time_12hr + " PM";
//                    }
                    machineStartTime = getCurrentTime();
                    System.out.println("=========MACHINE START TIME===========");
                    System.out.println(machineStartTime);
                    System.out.println("==================================");
                    machine_start_time.setText(convertMillisTo12hr(machineStartTime).replace("a.m.", "AM").replace("p.m.", "PM"));
                    machineHasStarted = true;

                    // Start Uptime
//                    startUptimeChronometer();

//                    try {
//                        date1 = dateFormat.parse(machine_start_time);
//                        myDate1 = date1.getTime();
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
                } else if (value == 0 && machineHasStarted) {
//                    machine_end_time = dateFormat.format(new Date());
//                    try {
//                        date3 = dateFormat12hr.parse(machine_end_time);
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//
//                    machine_end_time_12hr = dateFormat12hr.format(date3);
//
//                    if (machine_end_time_12hr.equals(machine_end_time)) {
//                        machine_end_time_12hr = machine_end_time_12hr + " AM";
//                    } else {
//                        machine_end_time_12hr = machine_end_time_12hr + " PM";
//                    }
                    machineEndTime = getCurrentTime();
                    System.out.println("=========MACHINE END TIME===========");
                    System.out.println(machineEndTime);
                    System.out.println("==================================");
                    machine_end_time.setText(convertMillisTo12hr(machineEndTime).replace("a.m.", "AM").replace("p.m.", "PM"));

                    runningTime = differenceInTime(machineStartTime, machineEndTime);
                    System.out.println("=========RUNNING TIME===========");
                    System.out.println(runningTime);
                    System.out.println("==================================");
                    machine_running_time.setText(convertDurationToHms(runningTime));

                    // Stop Uptime
//                    pauseUptimeChronometer();

//                    try {
//                        date2 = dateFormat.parse(machine_end_time);
//                        myDate2 = date2.getTime();
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//                    machine_difference = myDate2 - myDate1;
//                    System.out.println("=========machine_difference==========");
//                    System.out.println("==========================");
//                    System.out.println(machine_difference);
//                    System.out.println("==========================");
//                    System.out.println("==========================");
//                    machine_hours = (int) (machine_difference / (60 * 60 * 1000) % 24);
//                    machine_min = (int) (machine_difference / (60 * 1000) % 60);
//                    machine_hours = (machine_hours < 0 ? -machine_hours : machine_hours);
//                    machine_min = (machine_min < 0 ? -machine_min : machine_min);
//                    machineRunningTime.setText(machine_hours + " hr " + " : " + machine_min + " min");

                    Upload upload = new Upload(runningTime);
                    FirebaseDatabase.getInstance().getReference("upTime/").push()
                            .setValue(upload);
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

//                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
//                SimpleDateFormat dateFormat12hr = new SimpleDateFormat("hh:mm", Locale.getDefault());

//                Date date1, date2, date3 = null, date4 = null;

                if (value == 0) {
//                    job_start_time = dateFormat.format(new Date());
//                    try {
//                        date3 = dateFormat12hr.parse(job_start_time);
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }

//                    job_start_time_12hr = dateFormat12hr.format(date3);
//
//                    if (job_start_time_12hr.equals(job_start_time)) {
//                        job_start_time_12hr = job_start_time_12hr + " AM";
//                    } else {
//                        job_start_time_12hr = job_start_time_12hr + " PM";
//                    }

                    jobStartTime = getCurrentTime();
                    System.out.println("=========JOB START TIME===========");
                    System.out.println(jobStartTime);
                    System.out.println("==================================");
                    job_start_time.setText(convertMillisTo12hr(jobStartTime).replace("a.m.", "AM").replace("p.m.", "PM"));
                    jobHasStarted = true;
//                    try {
//                        date1 = dateFormat.parse(job_start_time);
//                        myDate3 = date1.getTime();
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
                } else if (value == 1 && jobHasStarted) {
//                    job_end_time = dateFormat.format(new Date());
//
//                    try {
//                        date4 = dateFormat12hr.parse(job_end_time);
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//
//                    job_end_time_12hr = dateFormat12hr.format(date4);
//
//                    if (job_end_time_12hr.equals(job_end_time)) {
//                        job_end_time_12hr = job_end_time_12hr + " AM";
//                    } else {
//                        job_end_time_12hr = job_end_time_12hr + " PM";
//                    }

                    jobEndTime = getCurrentTime();
                    System.out.println("=========JOB END TIME===========");
                    System.out.println(jobEndTime);
                    System.out.println("==================================");
                    job_end_time.setText(convertMillisTo12hr(jobEndTime).replace("a.m.", "AM").replace("p.m.", "PM"));
//                    try {
//                        date2 = dateFormat.parse(job_end_time);
//                        myDate4 = date2.getTime();
//
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//                    job_difference = myDate4 - myDate3;
//                    System.out.println("=========job_difference==========");
//                    System.out.println("==========================");
//                    System.out.println(job_difference);
//                    System.out.println("==========================");
//                    System.out.println("==========================");
//                    job_hours = (int) (job_difference / (60 * 60 * 1000) % 24);
//                    job_min = (int) (job_difference / (60 * 1000) % 60);
//                    job_hours = (job_hours < 0 ? -job_hours : job_hours);
//                    job_min = (job_min < 0 ? -job_min : job_min);
                    jobTime = differenceInTime(jobStartTime, jobEndTime);
                    System.out.println("=========JOB TIME===========");
                    System.out.println(jobTime);
                    System.out.println("==================================");
                    job_time.setText(convertDurationToHms(jobTime));
                }

                if (runningTime != 0 && jobTime != 0 && jobHasStarted) {
                    timeDifference();
                    jobHasStarted = false;
                }
//                if ((job_min != 0 || machine_min != 0) || (job_hours != 0 || machine_hours != 0)) {
//                    timeDifference();
//                    job_min = 0;
//                    machine_min = 0;
//                    job_hours = 0;
//                    machine_hours = 0;
//                    machineHasStarted = false;
//                    jobHasStarted = false;
//
//                    time_difference = "";
//                    my_time_difference = "";
//                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    void timeDifference() {
//        job_hours = (int) (job_difference / (60 * 60 * 1000) % 24);
//        job_min = (int) (job_difference / (60 * 1000) % 60);
//        job_hours = (job_hours < 0 ? -job_hours : job_hours);
//        job_min = (job_min < 0 ? -job_min : job_min);
//        time_diff = job_difference - machine_difference;
//        System.out.println("========time_diff===========");
//        System.out.println("==========================");
//        System.out.println(time_diff);
//        System.out.println("==========================");
//        System.out.println("==========================");
//        int hours = (int) (time_diff / (60 * 60 * 1000) % 24);
//        int mins = (int) (time_diff / (60 * 1000) % 60);
//        hours = (hours < 0 ? -hours : hours);
//        mins = (mins < 0 ? -mins : mins);
//        System.out.println("==========================");
//        System.out.println("==========================");
//        System.out.println(hours+ " hrs ");
//        System.out.println(mins+ " mins");
//        System.out.println("==========================");
//        System.out.println("==========================");
//        time_difference = time_difference + hours + " hr " + " : " + mins + " min\n";

        timeDifference = differenceInTime(runningTime, jobTime);
        System.out.println("=========TIME DIFFERENCE===========");
        System.out.println(timeDifference);
        System.out.println("==================================");
        String saveTimeDifference = convertDurationToHms(timeDifference) + "\n";
        Upload upload = new Upload(saveTimeDifference);
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
                        String my_time_difference = "";
                        int i = 1;
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Upload value = dataSnapshot.getValue(Upload.class);
                            if (value != null) {
                                my_time_difference = my_time_difference + "JOB " + i + ": " + value.getTime_difference();
                                time_difference.setText(my_time_difference);
                                i++;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setUpTime() {
        upTimeReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long myUpTime = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Upload value = dataSnapshot.getValue(Upload.class);
                    if (value != null) {
                        myUpTime = myUpTime + value.getUpTime();
                    }
                }
//                upTime_hour = (int) (myUpTime / (60 * 60 * 1000) % 24);
//                upTime_min = (int) (myUpTime / (60 * 1000) % 60);
//                upTime_hour = (upTime_hour < 0 ? -upTime_hour : upTime_hour);
//                upTime_min = (upTime_min < 0 ? -upTime_min : upTime_min);

                up_time.setText("UpTime: " + convertDurationToHm(myUpTime));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    Long getCurrentTime() {
        return System.currentTimeMillis();
    }

    String convertMillisTo12hr(long millis) {
        return timeFormat.format(millis);
    }

    String convertDurationToHm(long millis) {
        @SuppressLint("DefaultLocale") String hm = String.format("%02d hr %02d min", TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis))
//                TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
        );
        return hm;
    }

    String convertDurationToHms(long millis) {
        @SuppressLint("DefaultLocale") String hm = String.format("%02d hr %02d min %02d sec", TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
        );
        return hm;
    }

    Long differenceInTime(long startTime, long endTime) {
        return endTime - startTime;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTimeDifference();
        setUpTime();
    }
}