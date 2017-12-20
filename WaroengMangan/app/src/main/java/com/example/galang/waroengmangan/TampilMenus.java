package com.example.galang.waroengmangan;

/**
 * Created by galang on 12/3/17.
 */

public class TampilMenus {
    String mNama, mDeskripsi, mHarga,mimageURL;
    public TampilMenus()
    {

    }
    public TampilMenus(String mNama, String mDeskripsi, String mHarga, String mimageURL ) {
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
