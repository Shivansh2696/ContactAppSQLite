package com.example.contactapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactapp.All_Details;
import com.example.contactapp.R;

import java.util.List;

import model.Contact;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    final private Context context;
    final private List<Contact> contactList;
    //private ItemClickListener myItemListener;

    public RecyclerViewAdapter(Context context, List<Contact> contactList) {
        this.context = context;
        this.contactList = contactList;
    }

    // where to get single card as viewHolder object
    @NonNull @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_card,parent,false);
        return new MyViewHolder(view);
    }

    // what will happen after we create the viewHolder object
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.MyViewHolder holder, int position) {
    Contact contact = contactList.get(position);

    String fullName = contact.getFname() + " " + contact.getLname();
    holder.contactName.setText(fullName);
    holder.contactNumber.setText(contact.getPhone());

    //Changing String image to bitmap
    String encodedImage = contact.getImage();
    byte[] decodedString = Base64.decode(encodedImage,Base64.DEFAULT);
    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString,0,decodedString.length);
    holder.iconImage.setImageBitmap(decodedByte);
    holder.itemView.setOnClickListener( view -> {
//        Bundle bundle = new Bundle();
//        bundle.putInt("rowID",contact.getId());
//        bundle.putString("imageName",contact.getImage());
//        bundle.putString("firstName",contact.getFname());
//        bundle.putString("lastName",contact.getLname());
//        bundle.putString("phoneNumber",contact.getPhone());
//        bundle.putString("email",contact.getEmail());
//        bundle.putString("address",contact.getAddress());
        Intent intent = new Intent(context,All_Details.class);
        intent.putExtra("imageName",contact.getImage());
        intent.putExtra("firstName",contact.getFname());
        intent.putExtra("lastName",contact.getLname());
        intent.putExtra("phoneNumber",contact.getPhone());
        intent.putExtra("email",contact.getEmail());
        intent.putExtra("address",contact.getAddress());
        intent.putExtra("rowId",contact.getId());
//        intent.putExtra("ContactDetails",bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    });
    }

    //how many times?
    @Override
    public int getItemCount() {
        return contactList.size();
    }

//    public interface ItemClickListener{
//        void onItemClick(Contact contact);
//    }
    //Creating View Holder
        class MyViewHolder extends RecyclerView.ViewHolder{
             ImageView iconImage, Dialer, Caller;
             TextView contactName,contactNumber;
            public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            iconImage = itemView.findViewById(R.id.Image);
            Dialer = itemView.findViewById(R.id.Dialer);
            Caller = itemView.findViewById(R.id.Caller);
            contactName = itemView.findViewById(R.id.ContactName);
            contactNumber = itemView.findViewById(R.id.ContactNumber);

        }
    }
}
