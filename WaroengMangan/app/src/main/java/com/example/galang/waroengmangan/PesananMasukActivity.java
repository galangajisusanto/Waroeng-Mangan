package com.example.galang.waroengmangan;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class PesananMasukActivity extends AppCompatActivity {

    private static final String PREF_NAME="prefs";
    private static final String PREF_DARK_THEME="dark_theme";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences preferences=getSharedPreferences(PREF_NAME,MODE_PRIVATE);
        boolean useDarkTheme=preferences.getBoolean(PREF_DARK_THEME,false);


        if(useDarkTheme)
        {
            setTheme(R.style.TemaGelap);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesanan_masuk);
    }
}
