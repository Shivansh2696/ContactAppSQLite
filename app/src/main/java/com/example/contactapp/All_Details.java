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
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.example.contactapp.databinding.ActivityAllDetailsBinding;

import java.io.ByteArrayOutputStream;

import model.Contact;

public class All_Details extends AppCompatActivity {
    ActivityAllDetailsBinding binding;
    dataBaseHelper DB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAllDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        DB = new dataBaseHelper(this);
        Bundle bundle = getIntent().getExtras();

        // getting Image through intent in the form of  string and changing it to bitmap and showing it in imageView
        String encodedImage = getIntent().getStringExtra("imageName");
        byte[] decodedString = Base64.decode(encodedImage,Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString,0,decodedString.length);
        binding.EditImage.setImageBitmap(decodedByte);

        // getting all other string attributes and putting it to editText
        binding.EditFname.setText(getIntent().getStringExtra("firstName"));
        binding.EditLName.setText(getIntent().getStringExtra("lastName"));
        binding.EditPhone.setText(getIntent().getStringExtra("phoneNumber"));
        binding.EditEmail.setText(getIntent().getStringExtra("email"));
        binding.EditAddress.setText(getIntent().getStringExtra("address"));
        binding.EditImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseProfilePicture();  // It Shows a dialog box of camera and gallery
            }
        });

        binding.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Contact contact = new Contact();
                contact.setImage(convertImageToString(binding.EditImage));
                contact.setFname(binding.EditFname.getText().toString());
                contact.setLname(binding.EditLName.getText().toString());
                contact.setPhone(binding.EditPhone.getText().toString());
                contact.setEmail(binding.EditEmail.getText().toString());
                contact.setAddress(binding.EditAddress.getText().toString());
                contact.setId(Integer.parseInt(getIntent().getStringExtra("rowID")));
                 int checkUpdate = DB.upDateContact(contact);
                if(checkUpdate > 0)
                    Toast.makeText(All_Details.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                else Toast.makeText(All_Details.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                binding.btnEdit.setOnClickListener(this);
                Intent i = new Intent(All_Details.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        });
    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId())
//        {
//            case R.id.Delete:
//
//        }
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Bundle bundle = getIntent().getExtras();
    int id = item.getItemId();
    if(id == R.id.Delete){
        Contact contact = new Contact();
        contact.setId(bundle.getInt("rowID"));
        DB.deleteSingleRecord(contact);
    }
        return true;
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
        AlertDialog.Builder builder = new AlertDialog.Builder(All_Details.this);
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
                    Uri selectedImage = data.getData();
                    binding.EditImage.setImageURI(selectedImage);
                }
                break;
            case 102:
                if(resultCode == RESULT_OK)
                {
                    Bundle bundle = data.getExtras();
                    Bitmap bitmapImage = (Bitmap) bundle.get("data");
                    binding.EditImage.setImageBitmap(bitmapImage);
                }
        }
    }

}
