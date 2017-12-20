package com.example.galang.waroengmanganuser;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {
    ViewPager viewPager;

    List<TampilDiscount> gambar= new ArrayList<>();

    MyCustomPagerAdapter myCustomPagerAdapter;

    RecyclerView rv;
    // menampung data dari firebase
    List<TampilMenus> tampilMenu;

    AdapterMenu adapter;
    ProgressDialog progresDialog;

    Activity context=this;
    DatabaseReference reference1,reference2,menu,diskon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        rv = (RecyclerView) findViewById(R.id.recycler_tampil_menu);
        //Set layout

        setTitle("Waroeng Mangan User");


               //Membuat ArryList
        tampilMenu = new ArrayList<>();

        reference1 = FirebaseDatabase.getInstance().getReference();
        reference2 = FirebaseDatabase.getInstance().getReference();
        menu=reference1.child("Menu");
        diskon=reference2.child("Discount");


        diskon.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot :dataSnapshot.getChildren()){
                    TampilDiscount dataku2 = snapshot.getValue(TampilDiscount.class);
                    gambar.add(dataku2);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        menu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                tampilMenu.removeAll(tampilMenu);
                for (DataSnapshot snapshot :
                        dataSnapshot.getChildren()){
                    TampilMenus dataku = snapshot.getValue(TampilMenus.class);
                    tampilMenu.add(dataku);

                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        //recylerViwe
                rv.setLayoutManager(new LinearLayoutManager(this));


          //  Timer timer=new Timer();
        //    timer.scheduleAtFixedRate(new MyTimerTask(),2000,4000);
        adapter = new AdapterMenu(context,tampilMenu);
        rv.setAdapter(adapter);
        myCustomPagerAdapter = new MyCustomPagerAdapter(context, gambar);
        viewPager.setAdapter(myCustomPagerAdapter);










    }

/*

    public class MyTimerTask extends TimerTask{
        @Override
        public void run()
        {
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(viewPager.getCurrentItem()<gambar.size()-1)
                    {
                        viewPager.setCurrentItem(viewPager.getCurrentItem()+1);

                    }

                    else viewPager.setCurrentItem(0);
                }
            });
        }
    } */
}