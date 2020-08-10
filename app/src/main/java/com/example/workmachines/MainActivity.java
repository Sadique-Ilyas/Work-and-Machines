package com.example.workmachines;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.text.DateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private Spinner spinner;
    private Switch darkSwitch;
    private TextView time, date;
    private Calendar calendar;

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

        //Checking Dark mode on startup
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            darkSwitch.setChecked(true);
            System.out.println("---------------------------------------------------------------");
            System.out.println("------------------------CHECKED----------------------");
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
        final String[] spinnerValues = {"Machine 101", "Machine 102", "Add Machine..."};

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, spinnerValues);
        spinner.setAdapter(arrayAdapter);


        //Switch for Dark Mode
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
    }
}