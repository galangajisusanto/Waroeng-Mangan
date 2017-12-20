package com.example.galang.waroengmangan;

import android.media.Image;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

public class ContohFloatingButtonActivity extends AppCompatActivity {
Toolbar toolbar;
ImageButton FAB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contoh_floating_button);
    toolbar=(Toolbar)findViewById(R.id.cutomtoolbar1);
        setSupportActionBar(toolbar);
   FAB=(ImageButton)findViewById(R.id.imagebutton);
   FAB.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View v) {
           Toast.makeText(ContohFloatingButtonActivity.this,"Hello Word",Toast.LENGTH_SHORT).show();
       }
   });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.item_navigasi,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id=item.getItemId();
        if (id==R.id.navigation1)
        {Toast.makeText(ContohFloatingButtonActivity.this,"Anda memilih navigasi 1",Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
