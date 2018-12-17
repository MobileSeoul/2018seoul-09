package com.example.ggavi.ahnsungmin.nomal_mode;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ggavi.ahnsungmin.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class ProfilePictureUpload extends AppCompatActivity {

    Button UploadImageOnServerButton;
    ImageView GetImageFromGalleryButton;
    ImageView ShowSelectedImage;
    EditText GetImageName;
    Bitmap FixBitmap;
    String ImageTag = "image_tag";
    String ImageName = "image_data";
    String ServerUploadPath = "http://ggavi2000.cafe24.com/uploadProfileImage.php";
    ProgressDialog progressDialog;
    ByteArrayOutputStream byteArrayOutputStream;

    byte[] byteArray;
    String ConvertImage;
    private String userId;
    HttpURLConnection httpURLConnection;

    URL url;
    OutputStream outputStream;
    BufferedWriter bufferedWriter;

    int RC;
    BufferedReader bufferedReader;
    StringBuilder stringBuilder;

    boolean check = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity5_profile_picture_upload);

        ((AppCompatActivity) ProfilePictureUpload.this).getSupportActionBar().setTitle((Html.fromHtml("<font color='#ffffff'>" + "프로필 사진 업로드" + "</font>")));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        GetImageFromGalleryButton = (ImageView) findViewById(R.id.select);
        GetImageFromGalleryButton.setClickable(true);
        UploadImageOnServerButton = (Button) findViewById(R.id.post);
        ShowSelectedImage = (ImageView) findViewById(R.id.selectedPic);
        userId = a_LoginMainActivity.userID.trim();

        byteArrayOutputStream = new ByteArrayOutputStream();
        GetImageFromGalleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image From Gallery"), 1);
            }
        });


        UploadImageOnServerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UploadImageToServer();
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
            Intent intent = new Intent(ProfilePictureUpload.this, ProfilePicture.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish(); // close this activity and return to preview activity (if there is any)
        Intent intent = new Intent(ProfilePictureUpload.this, ProfilePicture.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int RC, int RQC, Intent I) {
        super.onActivityResult(RC, RQC, I);
        if (RC == 1 && RQC == RESULT_OK && I != null && I.getData() != null) {
            Uri uri = I.getData();
            try {
                FixBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                ShowSelectedImage.setImageBitmap(FixBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void UploadImageToServer() {
        System.out.println("debug>>" + byteArrayOutputStream);

        if (FixBitmap == null) {
            Toast.makeText(ProfilePictureUpload.this, "사진을 선택해주세요.", Toast.LENGTH_LONG).show();
        } else {
            FixBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byteArray = byteArrayOutputStream.toByteArray();
            ConvertImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
            class AsyncTaskUploadClass extends AsyncTask<Void, Void, String> {

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    progressDialog = ProgressDialog.show(ProfilePictureUpload.this, "업로드중", "잠시 기다려주세요.", false, false);
                }

                @Override
                protected void onPostExecute(String string1) {
                    super.onPostExecute(string1);
                    progressDialog.dismiss();
                    Toast.makeText(ProfilePictureUpload.this, string1, Toast.LENGTH_LONG).show();
                    finish();
                    Intent intent = new Intent(ProfilePictureUpload.this, ProfilePicture.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }

                @Override
                protected String doInBackground(Void... params) {
                    ImageProcessClass imageProcessClass = new ImageProcessClass();
                    HashMap<String, String> HashMapParams = new HashMap<String, String>();
                    HashMapParams.put(ImageTag, userId);
                    HashMapParams.put(ImageName, ConvertImage);
                    String FinalData = imageProcessClass.ImageHttpRequest(ServerUploadPath, HashMapParams);
                    return FinalData;
                }
            }
            AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();
            AsyncTaskUploadClassOBJ.execute();
        }
        // System.out.println("bitmap>>"+FixBitmap);

    }

    public class ImageProcessClass {
        public String ImageHttpRequest(String requestURL, HashMap<String, String> PData) {
            StringBuilder stringBuilder = new StringBuilder();
            try {
                url = new URL(requestURL);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setReadTimeout(20000);
                httpURLConnection.setConnectTimeout(20000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                outputStream = httpURLConnection.getOutputStream();
                bufferedWriter = new BufferedWriter(
                        new OutputStreamWriter(outputStream, "UTF-8"));
                bufferedWriter.write(bufferedWriterDataFN(PData));
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                RC = httpURLConnection.getResponseCode();
                if (RC == HttpsURLConnection.HTTP_OK) {
                    bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    stringBuilder = new StringBuilder();
                    String RC2;
                    while ((RC2 = bufferedReader.readLine()) != null) {
                        stringBuilder.append(RC2);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return stringBuilder.toString();
        }

        private String bufferedWriterDataFN(HashMap<String, String> HashMapParams) throws UnsupportedEncodingException {
            stringBuilder = new StringBuilder();
            for (Map.Entry<String, String> KEY : HashMapParams.entrySet()) {
                if (check)
                    check = false;
                else
                    stringBuilder.append("&");
                stringBuilder.append(URLEncoder.encode(KEY.getKey(), "UTF-8"));
                stringBuilder.append("=");
                stringBuilder.append(URLEncoder.encode(KEY.getValue(), "UTF-8"));
            }
            return stringBuilder.toString();
        }
    }
}