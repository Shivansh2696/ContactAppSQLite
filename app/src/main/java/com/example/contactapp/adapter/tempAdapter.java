package com.example.contactapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;

import com.androidlover5842.AndroidUtils.Adapter.RecyclerBuilder;
import com.androidlover5842.AndroidUtils.Holder.BaseViewHolder;
import com.example.contactapp.R;
import com.example.contactapp.databinding.RecyclerCardBinding;

import model.Contact;

public class tempAdapter extends RecyclerBuilder<Contact> {
    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position, Contact model, View v) {
        RecyclerCardBinding binding = RecyclerCardBinding.bind(v);
        String FullName = model.getFname() + " " + model.getLname();
        binding.ContactName.setText(FullName);
        binding.ContactNumber.setText(model.getPhone());
        String encodedImage = model.getImage();
        byte[] decodedString = Base64.decode(encodedImage,Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString,0,decodedString.length);
        binding.Image.setImageBitmap(decodedByte);
        binding.Dialer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                int Number = Integer.parseInt(model.getPhone());
                intent.setData(Uri.parse("tel :" + Number));
                getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.recycler_card;
    }
}
