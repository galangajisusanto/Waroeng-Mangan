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
import android.nfc.Tag;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MasukanMenuActivity extends AppCompatActivity {


    MaterialEditText txtNama, txtDeskripsi, txtHarga;
    Button btnSubmit;
    ImageButton btnTambah;
    ImageView image;
    TextView path;
    private int PiCK_IMAGE_REQUEST=1;
    int colum_index;
    String image_path;

    Toolbar toolbar;

    String Nama, Deskripsi, Harga;
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
        setContentView(R.layout.activity_masukan_menu);
        toolbar=(Toolbar)findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitleColor(R.color.white);
        setTitle("Masukan Menu");



        txtNama = (MaterialEditText)findViewById(R.id.txt_nama);
        txtDeskripsi = (MaterialEditText)findViewById(R.id.txt_deskripsi);
        txtHarga = (MaterialEditText)findViewById(R.id.txt_harga);
        btnSubmit = (Button) findViewById(R.id.btn_submit);
        btnTambah=(ImageButton)findViewById(R.id.btn_tambah_gambar);
        path=(TextView)findViewById(R.id.txt_path);
        image=(ImageView)findViewById(R.id.image);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageRef=FirebaseStorage.getInstance().getReference();

        ///progres dialog
        progresDialog=new ProgressDialog(MasukanMenuActivity.this);

        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(i,"Select Picture"),PiCK_IMAGE_REQUEST);

            }
        });


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(isOnline()==true)
               {
                   proses();
               }
               else
               {
                   Toast.makeText(MasukanMenuActivity.this, "Cek Koneksi Internet Anda !!!" ,
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
            Intent intent=new Intent(MasukanMenuActivity.this,MainActivity.class);
            startActivity(intent);
            return true;
        }
        else if(id==R.id.samping2)
        {
            Intent intent=new Intent(MasukanMenuActivity.this,SettingActivity.class);
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




    public boolean validasi(){
        if (txtNama.getText().toString().isEmpty()){
            Toast.makeText(this, "Nama Belum Diisi !!!", Toast.LENGTH_SHORT).show();
            txtNama.setError("Eror: masukan nama");
            txtNama.requestFocus();
            return false;
        }
        if (txtHarga.getText().toString().isEmpty()){
            Toast.makeText(this, "Harga Belum Diisi !!!", Toast.LENGTH_SHORT).show();
            txtHarga.setError("Eror: masukan harga");
            txtHarga.requestFocus();
            return false;
        }
        if (txtDeskripsi.getText().toString().isEmpty()){
            Toast.makeText(this, "Deskripsi diisi", Toast.LENGTH_SHORT).show();
            txtDeskripsi.setError("Error: masukan deskripsi");
            txtDeskripsi.requestFocus();
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
             Bitmap bitmap=MediaStore.Images.Media.getBitmap(getContentResolver(),filePathUri);
             image.setImageBitmap(bitmap);

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
            StorageReference storageReference2nd=storageRef.child("Gambar_menu/"+System.currentTimeMillis()+"."+getpath(filePathUri));
            // Add sukseslistener
            storageReference2nd.putFile(filePathUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Nama=txtNama.getText().toString();
                    Deskripsi= txtDeskripsi.getText().toString();
                    Harga = txtHarga.getText().toString();
                    //Menghilangkan progres dialog
                    progresDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Data terkirim", Toast.LENGTH_SHORT).show();

                    ///Menyimpan data ke database

                    AddMenus addData = new AddMenus(Nama, Deskripsi, Harga,taskSnapshot.getDownloadUrl().toString());

                    databaseReference.child("Menu").child(Nama).setValue(addData);
                    Toast.makeText(MasukanMenuActivity.this, "Data Tersimpan di database ", Toast.LENGTH_SHORT).show();

                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //mengilangkan progres bar
                            progresDialog.dismiss();
                            //tampoilkan pesan eror
                            Toast.makeText(MasukanMenuActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
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
            Toast.makeText(MasukanMenuActivity.this,"Pilih Gambar Terlebih Dahuli",Toast.LENGTH_LONG).show();
        }


        // Create a storage reference from our app
      //  StorageReference storageRef = storageRef.getReference();

// Create a reference to "mountains.jpg"
        //StorageReference mountainsRef = storageRef.child("mountains.jpg");

// Create a reference to 'images/mountains.jpg'
        //StorageReference mountainImagesRef = storageRef.child("images/mountains.jpg");

// While the file names are the same, the references point to different files
        /*
        mountainsRef.getName().equals(mountainImagesRef.getName());    // true
        mountainsRef.getPath().equals(mountainImagesRef.getPath());    // false

        // Get the data from an ImageView as bytes
        image.setDrawingCacheEnabled(true);
        image.buildDrawingCache();
        Bitmap bitmap = image.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
            }
        });

*/
        //Nama = txtNama.getText().toString();


    // Create a reference to "mountains.jpg"
       // StorageReference mountainsRef = storageRef.child(Nama +".jpg");
        // Create a reference to 'images/mountains.jpg'
        //StorageReference mountainImagesRef = storageRef.child("images/"+Nama +".jpg");
        //Apabila nama sudah ada dan path sama
    //    if(mountainsRef.getName().equals(mountainImagesRef.getName())==true&&mountainsRef.getPath().equals(mountainImagesRef.getPath())==true){
          //  Toast.makeText(this,"Gambar Sudah Ada ",Toast.LENGTH_SHORT).show();
      //  }
  //      else{
            /*
            Uri file = Uri.fromFile(new File("path/to/images/rivers.jpg"));

            StorageReference riversRef = storageRef.child("images/"+file.getLastPathSegment());
            UploadTask uploadTask = riversRef.putFile(file);

// Register observers to listen for when the download is done or if it fails
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                }
            });
*/
        //}

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



