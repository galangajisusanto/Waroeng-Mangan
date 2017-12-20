package com.example.galang.waroengmanganuser;

/**
 * Created by galang on 12/14/17.
 */

public class TampilDiscount {

    String mNama, mDiscount,mimageURL;
    //Integer mGambar;

    public TampilDiscount()
    {

    }

    public TampilDiscount(String mNama, String mDiscount, String mimageURL) {
        this.mNama = mNama;
        this.mDiscount = mDiscount;

        this.mimageURL=mimageURL;
    }

    public String getmNama() {
        return mNama;
    }

    public String getmDiscount() {
        return mDiscount;
    }


    public String getMimageURL(){
        return mimageURL;
    }
}
