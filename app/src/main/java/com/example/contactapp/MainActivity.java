package com.example.contactapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.contactapp.adapter.RecyclerViewAdapter;
import com.example.contactapp.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

import model.Contact;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private ArrayList<Contact> contactArrayList;
    private ArrayAdapter<String> arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Binding Performs
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // finding recyclerView By ID and its settings
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Creating a dataBase
        dataBaseHelper db = new dataBaseHelper(MainActivity.this);

        // Creating a ContactList taking All contacts from function getAllContacts()
        contactArrayList = new ArrayList<>();

        List<Contact> contactList =db.getAllContacts();
        for(Contact contact: contactList){
            Contact c1 = new Contact();
            c1.setFname(contact.getFname());
            c1.setLname(contact.getLname());
            c1.setPhone(contact.getPhone());
            c1.setEmail(contact.getEmail());
            c1.setAddress(contact.getAddress());
            c1.setImage(contact.getImage());
//            Log.d("dbsunny","\nId" + contact.getId() + "\n" +
//                    "Name" + contact.getFname() + contact.getLname() + "\n" + "Phone Number" + contact.getPhone() + "\n"
//                    + "Email" + contact.getEmail() + "\n" + "Address" + contact.getAddress() + "\n" + "Image" + contact.getImage() + "\n");
            contactArrayList.add(c1);
        }

        // Making a RecyclerView and Giving It List
            recyclerViewAdapter = new RecyclerViewAdapter(MainActivity.this, contactArrayList);
            recyclerView.setAdapter(recyclerViewAdapter);

        //Adding click listener to AddContact
        binding.AddContact.setOnClickListener(this::onClick);
    }
        // Going to Create contact Activity
         public void onClick(View view){
         int id = view.getId();
         if(id == binding.AddContact.getId()){
            Intent intent = new Intent(this,CreateContact.class);
            startActivity(intent);
        }
    }
}