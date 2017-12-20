package com.example.galang.waroengmanganuser;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class AdapterMenu extends RecyclerView.Adapter<AdapterMenu.TampilDataViewHolder>  {
    // kelima list
    List<TampilMenus> tampilMenus;
    Context ctx;
    private int PiCK_IMAGE_REQUEST=1;
    // ke sebelas contructor klik kanan

    public AdapterMenu(Context ct, List<TampilMenus> tampilMenus)
    {   ctx=ct;
        this.tampilMenus = tampilMenus;
    }

    @Override
    public TampilDataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item, parent, false);
        TampilDataViewHolder holder = new TampilDataViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final TampilDataViewHolder holder, int position) {
        final TampilMenus mymenu = tampilMenus.get(position);
        holder.txtmJudul.setText(mymenu.getmNama());
        holder.txtmHarga.setText("Rp. "+mymenu.getmHarga() +" ,-");
        Picasso.with(ctx)
                .load(mymenu.getMimageURL())
                .into(holder.gambar);
        holder.gambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showImage(mymenu.getMimageURL());
            }

        });




    }


    View.OnClickListener clickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        //member aksi saat cardview diklik berdasarkan posisi tertentu
            TampilDataViewHolder vholder = (TampilDataViewHolder) v.getTag();

            int position = vholder.getPosition();
            Bundle b = new Bundle();
            Toast.makeText(ctx,"posisi"+position,Toast.LENGTH_SHORT).show();

            /// mengeset gambar ke bundle
          //  b.putInt("gambar",gambar[position]);
           // b.putString("label","Resep "+name[position]);


            //Intent intent=new Intent(context,ResepActivity.class);
            //intent.putExtras(b);
            //context.startActivity(intent);
        }
    };






    @Override
    public int getItemCount() {

        return tampilMenus.size();
    }



//Holder pengganti id
    public static class TampilDataViewHolder extends RecyclerView.ViewHolder{

        // ke dua TextView txtmNama;
        TextView txtmJudul, txtmHarga;
        CircleImageView gambar;
        Button edit;
        Button hapus;
        public TampilDataViewHolder(View itemView) {
            super(itemView);

            txtmJudul = (TextView)itemView.findViewById(R.id.judul_edit_discount);
            txtmHarga = (TextView)itemView.findViewById(R.id.harga_edit);
            gambar=(CircleImageView) itemView.findViewById(R.id.foto_edit_discount);

        }
    }

    // Image POP UP

    public void showImage(String URL) {
        Dialog builder = new Dialog(ctx);
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                //nothing;
            }
        });

        ImageView imageView = new ImageView(ctx);
        Picasso.with(ctx)
                .load(URL)
                .into(imageView);
        builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        builder.show();
    }



}
