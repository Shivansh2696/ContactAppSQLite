package com.example.contactapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import model.Contact;


public class dataBaseHelper extends SQLiteOpenHelper {
    private static final String DataBase_Name = "ContactDB.db";
    private static final String Table_Name = "ContactDetails";
    private static final String FName = "fName";
    private static final String LName = "LName";
    private static final String Phone = "phoneNumber";
    private static final String Email = "email";
    private static final String Address = "address";
    private static final String Img = "image";
    private static final String RowID = "rowId";
    SQLiteDatabase sqLiteDatabase;
     final String sqlQuery = "CREATE TABLE "+Table_Name+" ("+RowID+" "+"INTEGER PRIMARY KEY AUTOINCREMENT"+", "+FName+" text"+", "+LName+" text"+", "+Phone+" text"+", "+Email+" text"+", "+Address+" text"+", "+Img+" text"+")";
     public boolean insertData(Contact contact){
         SQLiteDatabase ContactDB = this.getWritableDatabase();
         ContentValues contentValues = new ContentValues();
         contentValues.put(FName,contact.getFname());
         contentValues.put(LName,contact.getLname());
         contentValues.put(Phone,contact.getPhone());
         contentValues.put(Email,contact.getEmail());
         contentValues.put(Address,contact.getAddress());
         contentValues.put(Img,contact.getImage());
         long insertedRow = ContactDB.insert(Table_Name,null,contentValues);
         ContactDB.close();
         if (insertedRow == -1 ) return true;
         else   return false;
     }

    public List<Contact> getAllContacts(){
        List<Contact> contactList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String select =" SELECT * FROM " + Table_Name;
        Cursor cursor = db.rawQuery(select,null);
        if(cursor.moveToFirst()){
            do{
                Contact contact = new Contact();
                contact.setId(cursor.getColumnCount());
                contact.setFname(cursor.getString(1));
                contact.setLname(cursor.getString(2));
                contact.setPhone(cursor.getString(3));
                contact.setEmail(cursor.getString(4));
                contact.setAddress(cursor.getString(5));
                contact.setImage(cursor.getString(6));
                contactList.add(contact);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return contactList;
    }
    public int upDateContact(Contact contact){
         SQLiteDatabase ContactDB = this.getWritableDatabase();
         ContentValues values = new ContentValues();
         values.put(FName,contact.getFname());
         values.put(LName,contact.getLname());
         values.put(Phone,contact.getPhone());
         values.put(Email,contact.getEmail());
         values.put(Address,contact.getAddress());
         values.put(Img,contact.getImage());
         int result = ContactDB.update(Table_Name,values,RowID + "=" + contact.getId(),null);
         return result;
     }

    public int deleteSingleRecord(Contact contact){
         int deletedItem = sqLiteDatabase.delete(Table_Name,RowID+"="+contact.getId(),null);
        return deletedItem;
    }

    public dataBaseHelper( Context context) {
        super(context, DataBase_Name, null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase ContactDB) {
        ContactDB.execSQL(sqlQuery);
    }
    @Override
    public void onUpgrade(SQLiteDatabase ContactDB, int oldVersion, int newVersion) {
        ContactDB.execSQL("DROP TABLE IF EXISTS "+Table_Name);
        onCreate(ContactDB);
    }
}
//"CREATE TABLE "+Table_Name+"(RowID INTEGER PRIMARY KEY AUTOINCREMENT , FNAME TEXT , LNAME TEXT , PHONE INTEGER , EMAIL TEXT , ADDRESS TEXT , IMAGE TEXT)"