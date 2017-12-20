package com.example.galang.waroengmangan;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MasukanDiskonActivity extends AppCompatActivity {


    DatabaseReference reference,menu;
    ArrayList<String> spinerArray= new ArrayList<>();

    Spinner spiner;
    MaterialEditText diskon;
    ;
    ImageButton tambahGambarDiskon;
    Button submitDiskon;
    ImageView gambarDiskon;

    Toolbar toolbar;


    private int PiCK_IMAGE_REQUEST=1;
    int colum_index;
    String image_path;
    String namaMenu;
    String mDiskon;
    DatabaseReference databaseReference;
    // Create a storage reference from our app
    StorageReference storageRef;

    ///Creat URI
    Uri filePathUri;
    //creat Progres dialog
    ProgressDialog progresDialog;


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
        setContentView(R.layout.activity_masukan_diskon);
        toolbar=(Toolbar)findViewById(R.id.cutomtoolbar1);
        setSupportActionBar(toolbar);

       getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Sunting Discount");

        spiner=(Spinner)findViewById(R.id.spinner_menus);
        diskon=(MaterialEditText)findViewById(R.id.txt_diskon);
        tambahGambarDiskon=(ImageButton)findViewById(R.id.btn_tambah_gambar_diskon);
        submitDiskon=(Button)findViewById(R.id.btn_submit_diskon);
        gambarDiskon=(ImageView)findViewById(R.id.image_diskon);

        spinerArray.add("Pilih Menu");
        reference = FirebaseDatabase.getInstance().getReference();
        menu=reference.child("Menu");

        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageRef= FirebaseStorage.getInstance().getReference();

        ///progres dialog
        progresDialog=new ProgressDialog(MasukanDiskonActivity.this);

        menu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot :
                        dataSnapshot.getChildren()){
                    TampilMenus data = snapshot.getValue(TampilMenus.class);
                    /// set data pada spiner
                    spinerArray.add(data.getmNama());

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,spinerArray);
        spiner.setAdapter(adapter);
        spiner.setOnItemSelectedListener(new CustomOnItemSelectedListener());



        ///progres dialog
        progresDialog=new ProgressDialog(MasukanDiskonActivity.this);

        tambahGambarDiskon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(i,"Select Picture"),PiCK_IMAGE_REQUEST);

            }
        });


        submitDiskon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isOnline()==true)
                {
                    proses();
                }
                else
                {
                    Toast.makeText(MasukanDiskonActivity.this, "Cek Koneksi Internet Anda !!!" ,
                            Toast.LENGTH_SHORT).show();
                }

            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.item_samping,menu);
        return true;
    }
    //toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id=item.getItemId();
        if (id==R.id.samping1)
        {
            Intent intent=new Intent(MasukanDiskonActivity.this,MainActivity.class);
            startActivity(intent);
            return true;
        }
        else if(id==R.id.samping2)
        {
            Intent intent=new Intent(MasukanDiskonActivity.this,SettingActivity.class);
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

    //aksi setelah item dipilih

    public class CustomOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

        String firstItem = String.valueOf(spiner.getSelectedItem());
        //Memilih spiner yang di klik
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            if (firstItem.equals(String.valueOf(spiner.getSelectedItem()))) {
                // ToDo when first item is selected
            } else {
                Toast.makeText(parent.getContext(),
                        "Kamu memilih Menu " + parent.getItemAtPosition(pos).toString(),
                        Toast.LENGTH_LONG).show();
                //memasukan spiner yang di klik ke dalam variabel namaMenu
                namaMenu=parent.getItemAtPosition(pos).toString();            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }

    }



    public boolean validasi(){
        if (diskon.getText().toString().isEmpty()){
            Toast.makeText(this, "Diskon Belum !!!", Toast.LENGTH_SHORT).show();
            diskon.setError("Eror: Diskon Kosong");
            diskon.requestFocus();
            return false;
        }
        if (namaMenu.isEmpty()){
            Toast.makeText(this, "Nama Menu Belum dipilih !!!", Toast.LENGTH_SHORT).show();
                        return false;
        }

        return true;
    }

    protected void onActivityResult(int requestCode,int resultCode,Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==PiCK_IMAGE_REQUEST&&resultCode==RESULT_OK&&data!=null&&data.getData()!=null)
        {
            filePathUri=data.getData();
            try{
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),filePathUri);
                gambarDiskon.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }
    //get URi PAth
    public String getpath(Uri uri)
    {
        String[] projection={MediaStore.MediaColumns.DATA};
        Cursor cursor=managedQuery(uri,projection,null,null,null);
        colum_index=cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        image_path=cursor.getString(colum_index);
        return cursor.getString(colum_index);
    }

    public void proses()
    {
        if (!validasi()){
            return;
        }
        storeFile();
    }


    public void storeFile(){
        if(filePathUri!=null)
        {
            //Setting progres title
            progresDialog.setTitle("Gambar sedang di Ungah...");
            //Show progres dialog
            progresDialog.show();

            //Create secon Storage refrence
            StorageReference storageReference2nd=storageRef.child("Gambar_diskon/"+System.currentTimeMillis()+"."+getpath(filePathUri));
            // Add sukseslistener
            storageReference2nd.putFile(filePathUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mDiskon=diskon.getText().toString();

                    //Menghilangkan progres dialog
                    progresDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Data terkirim", Toast.LENGTH_SHORT).show();

                    ///Menyimpan data ke database

                    AddDiscount data = new AddDiscount(namaMenu, mDiskon,taskSnapshot.getDownloadUrl().toString());

                    databaseReference.child("Discount").child(namaMenu).setValue(data);
                    Toast.makeText(MasukanDiskonActivity.this, "Data Tersimpan di database ", Toast.LENGTH_SHORT).show();

                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //mengilangkan progres bar
                            progresDialog.dismiss();
                            //tampoilkan pesan eror
                            Toast.makeText(MasukanDiskonActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            progresDialog.setTitle("Gambar Sedang di Unggah...");
                        }
                    });

        }

        else
        {
            Toast.makeText(MasukanDiskonActivity.this,"Pilih Gambar Terlebih Dahuli",Toast.LENGTH_LONG).show();
        }


    }

    //cek koneksi
    public boolean isOnline()
    {
        ConnectivityManager cm=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo=cm.getActiveNetworkInfo();
        if(netinfo!= null && netinfo.isConnectedOrConnecting())
        {
            return true;
        }
        return false;
    }


}
