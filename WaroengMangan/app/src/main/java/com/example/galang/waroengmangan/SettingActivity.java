package com.example.galang.waroengmangan;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.CompoundButton;
import android.widget.Switch;



public class SettingActivity extends AppCompatActivity {

    private static final String PREF_NAME="prefs";
    private static final String PREF_DARK_THEME="dark_theme";
    Toolbar toolbar;


    private void toogleTheme(Boolean darkTheme)
    {
        SharedPreferences.Editor editor=getSharedPreferences(PREF_NAME,MODE_PRIVATE).edit();
        editor.putBoolean(PREF_DARK_THEME,darkTheme);
        editor.apply();
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);

            }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences preferences=getSharedPreferences(PREF_NAME,MODE_PRIVATE);
        boolean useDarkTheme=preferences.getBoolean(PREF_DARK_THEME,false);

        if(useDarkTheme)
        {
            setTheme(R.style.TemaGelap);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        toolbar=(Toolbar)findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        setTitle("Setting");

        Switch toggle=(Switch)findViewById(R.id.switch1);

        toggle.setChecked(useDarkTheme);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                toogleTheme(isChecked);

            }
        });
    }






}
