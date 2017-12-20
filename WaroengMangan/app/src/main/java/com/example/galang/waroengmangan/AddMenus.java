package com.example.galang.waroengmangan;

/**
 * Created by galang on 12/3/17.
 */

public class AddMenus {


    String mNama, mDeskripsi,mHarga,mimageURL;
    //Integer mGambar;

    public AddMenus(String mNama, String mDeskripsi, String mHarga,String mimageURL) {
        this.mNama = mNama;
        this.mDeskripsi = mDeskripsi;
        this.mHarga = mHarga;
        this.mimageURL=mimageURL;
    }



    public String getmNama() {
        return mNama;
    }

    public String getmDeskripsi() {
        return mDeskripsi;
    }

    public String getmHarga(){
        return mHarga;
    }

    public String getMimageURL(){
        return mimageURL;
    }

}
