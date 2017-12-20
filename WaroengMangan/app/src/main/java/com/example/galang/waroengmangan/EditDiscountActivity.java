package com.example.galang.waroengmangan;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EditDiscountActivity extends AppCompatActivity {


    RecyclerView rv;
    // menampung data dari firebase
    List<TampilDiscount> tampilDiscount;
    AdapterEditDiscount adapter;

    Activity context=this;
    DatabaseReference reference,menu;

    private static final String PREF_NAME="prefs";
    private static final String PREF_DARK_THEME="dark_theme";
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences preferences=getSharedPreferences(PREF_NAME,MODE_PRIVATE);
        boolean useDarkTheme=preferences.getBoolean(PREF_DARK_THEME,false);


        if(useDarkTheme)
        {
            setTheme(R.style.TemaGelap);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_discount);
        toolbar=(Toolbar)findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Sunting Discount");



        //recylerViwe
        rv = (RecyclerView) findViewById(R.id.recycler_edit_discount);
        //Set layout
        rv.setLayoutManager(new LinearLayoutManager(this));
        //Membuat ArryList
        tampilDiscount = new ArrayList<>();
        //deklarasi database firebase
        reference = FirebaseDatabase.getInstance().getReference();
        menu=reference.child("Discount");
        adapter = new AdapterEditDiscount(context,tampilDiscount);
        rv.setAdapter(adapter);



        menu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                tampilDiscount.removeAll(tampilDiscount);

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    TampilDiscount dataku = snapshot.getValue(TampilDiscount.class);
                    tampilDiscount.add(dataku);

                }
                // ke 18
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.item_samping,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id=item.getItemId();
        if (id==R.id.samping1)
        {
            Intent intent=new Intent(EditDiscountActivity.this,MainActivity.class);
            startActivity(intent);
            return true;
        }
        else if(id==R.id.samping2)
        {
            Intent intent=new Intent(EditDiscountActivity.this,SettingActivity.class);
            startActivity(intent);
            return true;
        }
        else if(id==R.id.samping3)
        {
            final Dialog dialog=new Dialog(this);
            dialog.setContentView(R.layout.about);
            dialog.setTitle("Masukan Menu Baru");
            dialog.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
