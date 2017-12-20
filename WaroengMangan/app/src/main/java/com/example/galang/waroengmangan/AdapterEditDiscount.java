package com.example.galang.waroengmangan;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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

/**
 * Created by galang on 12/14/17.
 */

public class AdapterEditDiscount extends RecyclerView.Adapter<AdapterEditDiscount.TampilDiscountViewHolder> {
    // kelima list
    List<TampilDiscount> tampilDiscounts;
    Context ctx;
    private int PiCK_IMAGE_REQUEST = 1;
    // ke sebelas contructor klik kanan

    public AdapterEditDiscount(Context ct, List<TampilDiscount> tampilDiscounts) {
        ctx = ct;
        this.tampilDiscounts = tampilDiscounts;
    }

    @Override
    public AdapterEditDiscount.TampilDiscountViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.edit_discount, parent, false);
        AdapterEditDiscount.TampilDiscountViewHolder holder = new AdapterEditDiscount.TampilDiscountViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final AdapterEditDiscount.TampilDiscountViewHolder holder, int position) {
        final TampilDiscount mydiscount = tampilDiscounts.get(position);
        holder.txtmJudul.setText(mydiscount.getmNama() + " Disc " +mydiscount.getmDiscount() +"%" );
        holder.txtmDiscount.setText("Discount:  " +  mydiscount.getmDiscount() +"%");
        Picasso.with(ctx)
                .load(mydiscount.getMimageURL())
                .into(holder.gambar);
        holder.gambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showImage(mydiscount.getMimageURL());
            }

        });

        //tombol Hapus
        holder.hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ctx)
                        .setMessage("Apakah Anda Yakin Menghapus Menu Ini?")
                        .setCancelable(false)
                        .setPositiveButton("YA", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                hapus(mydiscount.getMimageURL(), mydiscount.getmNama());
                            }
                        })
                        .setNegativeButton("TIDAk", null)
                        .show();


            }
        });



    }






    @Override
    public int getItemCount() {

        return tampilDiscounts.size();
    }


    //Holder pengganti id
    public static class TampilDiscountViewHolder extends RecyclerView.ViewHolder {

        // ke dua TextView txtmNama;
        TextView txtmJudul, txtmDiscount;
        CircleImageView gambar;
        Button edit;
        Button hapus;

        public TampilDiscountViewHolder(View itemView) {
            super(itemView);

            txtmJudul = (TextView) itemView.findViewById(R.id.judul_edit_discount);
            txtmDiscount = (TextView) itemView.findViewById(R.id.discount_edit);
            gambar = (CircleImageView) itemView.findViewById(R.id.foto_edit_discount);
            hapus = (Button) itemView.findViewById(R.id.btn_hapus_discount);

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

    //Hapus file gambar dan database
    public void hapus(String URL, String Child) {
        ///Hapus Database
        DatabaseReference rev = FirebaseDatabase.getInstance().getReference();
        DatabaseReference menu = rev.child("Discount");
        menu.child(Child).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(ctx, "Hapus Menu dari Database Sukses..", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ctx, "Hapus Menu dari Database Gagal..", Toast.LENGTH_SHORT).show();
            }
        });

        StorageReference mStorageRef;

        ///Hapus File
        mStorageRef = FirebaseStorage.getInstance().getReferenceFromUrl(URL);
        mStorageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(ctx, "Hapus Gambar Dari Storege Sukses..", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ctx, "Hapus Gambar Dari Storege Gagal..", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
