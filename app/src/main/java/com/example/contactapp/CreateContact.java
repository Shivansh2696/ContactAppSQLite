package com.example.contactapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.contactapp.databinding.ActivityCreateContactBinding;

import java.io.ByteArrayOutputStream;

import model.Contact;

public class CreateContact extends AppCompatActivity {
    ActivityCreateContactBinding binding;
    dataBaseHelper DB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateContactBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
         DB = new dataBaseHelper(this);
         binding.CapturedImage.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 chooseProfilePicture();  // It Shows a dialog box of camera and gallery
             }
         });
        binding.createContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Contact contact = new Contact();
                contact.setFname(binding.EnfName.getText().toString());
                contact.setLname(binding.EnlName.getText().toString());
                contact.setPhone(binding.EnphoneNumber.getText().toString());
                contact.setEmail(binding.EnEmail.getText().toString());
                contact.setAddress(binding.EnAddress.getText().toString());
                contact.setImage(convertImageToString(binding.CapturedImage));

                if(contact.getFname().equals("")){
                    binding.EnfName.setError("Enter First Name");
                    binding.EnfName.requestFocus();
                    return;
                }
                 if(contact.getLname().equals("")){
                    binding.EnlName.setError("Enter Last Name");
                    binding.EnlName.requestFocus();
                    return;
                }
                if(contact.getPhone().equals("")){
                    binding.EnphoneNumber.setError("Enter Phone Number Here");
                    binding.EnphoneNumber.requestFocus();
                    return;
                }
                 if(contact.getEmail().equals("")){
                    binding.EnEmail.setError("Enter Email Here");
                    binding.EnEmail.requestFocus();
                    return;
                }
                if(contact.getAddress().equals("")){
                    binding.EnAddress.setError("Enter Address Here");
                    binding.EnAddress.requestFocus();
                    return;
                }
//                    String imageInString = convertImageToByteArray(binding.CapturedImage);
                    boolean checkInsertedData = DB.insertData(contact);
                    if(checkInsertedData)
                        Toast.makeText(CreateContact.this, "Data Inserted Successfully", Toast.LENGTH_SHORT).show();
                    else Toast.makeText(CreateContact.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                        binding.createContact.setOnClickListener(this);
                        Intent i = new Intent(CreateContact.this, MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        finish();
            }
        });
    }

    public String convertImageToString(ImageView imageView){
        imageView.buildDrawingCache();
        Bitmap bitmap = imageView.getDrawingCache();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,80,byteArrayOutputStream);
        byte[] b = byteArrayOutputStream.toByteArray();
        String encodedString = Base64.encodeToString(b,Base64.DEFAULT);
        return encodedString ;
    }
    private void chooseProfilePicture() {
        // Creating Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(CreateContact.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_dialog_profile_picture,null);
        builder.setCancelable(true);
        builder.setView(dialogView);
        // finding buttons via ID
        ImageView imageCamera = dialogView.findViewById(R.id.ImageCamera);
        ImageView imageGallery = dialogView.findViewById(R.id.ImageGallery);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
            //  When Clicked On Camera
        imageCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askCameraPermission();
                alertDialog.dismiss();
            }
        });
        // When Clicked On Gallery
        imageGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityIfNeeded(pickPhoto,103);
                alertDialog.dismiss();
            }
        });
    }

    // Asking Permission For Camera in Runtime (if user already gave permission then it will also going to work)
    private void askCameraPermission() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, 101);
        }
        else {
            openCamera();
        }
    }
        // IF User Denied The Permission Request
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        if(requestCode == 101){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                openCamera();
            }
            else Toast.makeText(this, "Camera Permission is Required To Use Camera", Toast.LENGTH_SHORT).show();
        }
    }
    private void openCamera() {
        Intent Camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(Camera.resolveActivity(getPackageManager()) != null) {
            startActivityIfNeeded(Camera, 102);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case 103:
                if(resultCode == RESULT_OK)
                {
                    assert data != null;
                    Uri selectedImage = data.getData();
                    binding.CapturedImage.setImageURI(selectedImage);
                }
                break;
            case 102:
                if(resultCode == RESULT_OK)
                {
                    assert data != null;
                    Bundle bundle = data.getExtras();
                    Bitmap bitmapImage = (Bitmap) bundle.get("data");
                    binding.CapturedImage.setImageBitmap(bitmapImage);
                }
        }
    }


}