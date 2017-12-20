package com.example.galang.waroengmangan;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    Context ctx= this;
    private static final String PREF_NAME="prefs";
    private static final String PREF_DARK_THEME="dark_theme";


    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences preferences=getSharedPreferences(PREF_NAME,MODE_PRIVATE);
        boolean useDarkTheme=preferences.getBoolean(PREF_DARK_THEME,false);

        if(useDarkTheme)
        {
            setTheme(R.style.TemaGelap);
        }


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this));

        //toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("waroeng Mangan");
        toolbar.setTitleTextColor(R.color.cardview_light_background);
        setSupportActionBar(toolbar);

        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        //Mengatur Navigasi View Item yang akan dipanggil untuk menangani item klik menu navigasi
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                //Memeriksa apakah item tersebut dalam keadaan dicek  atau tidak,
                if(menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);
                //Menutup  drawer item klik
                drawerLayout.closeDrawers();
                //Memeriksa untuk melihat item yang akan dilklik dan melalukan aksi
                switch (menuItem.getItemId()){
                    // pilihan menu item navigasi akan menampilkan pesan toast klik kalian bisa menggantinya
                    //dengan intent activity
                    case R.id.navigation1:


                        return true;
                    case R.id.navigation2:
                        Intent i=new Intent(MainActivity.this,SettingActivity.class);
                        startActivity(i);

                        return true;
                    case R.id.navigation3:
                        final Dialog dialog=new Dialog(ctx);
                        dialog.setContentView(R.layout.about);
                        dialog.setTitle("Masukan Menu Baru");
                        dialog.show();
                        return true;

                    default:
                        Toast.makeText(getApplicationContext(),"Kesalahan Terjadi ",Toast.LENGTH_SHORT).show();
                        return true;
                }
            }
        });





        // Menginisasi Drawer Layout dan ActionBarToggle
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.openDrawer, R.string.closeDrawer){
            @Override
            public void onDrawerClosed(View drawerView) {
                // Kode di sini akan merespons setelah drawer menutup disini kita biarkan kosong
                super.onDrawerClosed(drawerView);
            }
            @Override
            public void onDrawerOpened(View drawerView) {
                //  Kode di sini akan merespons setelah drawer terbuka disini kita biarkan kosong
                super.onDrawerOpened(drawerView);
            }
        };
        //Mensetting actionbarToggle untuk drawer layout
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        //memanggil synstate
        actionBarDrawerToggle.syncState();





        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                if(position==0)
                {
                    Intent intent=new Intent(MainActivity.this,MasukanMenuActivity.class);
                    startActivity(intent);
                }
                else if(position==1)
                {
                    Intent intent=new Intent(MainActivity.this,EditMenuActivity.class);
                    startActivity(intent);
                }
                else if(position==2)
                {
                    Intent intent=new Intent(MainActivity.this,MasukanDiskonActivity.class);
                    startActivity(intent);
                }

                else if(position==3)
                {
                    Intent intent=new Intent(MainActivity.this,EditDiscountActivity.class);
                    startActivity(intent);
                }
                else if (position==4)
                {
                    Intent intent=new Intent(MainActivity.this,PesananMasukActivity.class);
                    startActivity(intent);
                }
                else if (position==5)
                {
                    Intent intent=new Intent(MainActivity.this,LaporanPenjualanActivity.class);
                    startActivity(intent);
                }



            }
        });




    }


}


