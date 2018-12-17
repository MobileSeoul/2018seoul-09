package com.example.ggavi.ahnsungmin.nomal_mode;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;


public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

    ImageView imageView;
    public DownloadImageTask(ImageView imageView){
        this.imageView = imageView;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Bitmap doInBackground(String... url) {
        String path = url[0];
        Bitmap bitmap = null;
        try{
            InputStream inputStream = new URL(path).openStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap){
        imageView.setImageBitmap(bitmap);
        System.out.println("downloading image completed");
    }
}
