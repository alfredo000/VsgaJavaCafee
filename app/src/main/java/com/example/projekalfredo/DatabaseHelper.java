package com.example.projekalfredo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TABLE_CONTACTS = "contacts";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String HARGAMENU = "harga";
    private static final String DESKRIPSI = "deskripsi";
    public DatabaseHelper(@Nullable Context context) {
        super(context, "projekakhiralfredo.db", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase MyDatabase) {
        MyDatabase.execSQL("create Table users(email TEXT primary key, password TEXT)");
        String createContactsTableQuery = "CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT," // Add space after column name
                + HARGAMENU + " TEXT,"
                + DESKRIPSI + " TEXT"
                + ")";
        MyDatabase.execSQL(createContactsTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int i, int i1) {
        MyDB.execSQL("drop Table if exists users");
    }
    public void onUpgrade2(SQLiteDatabase MyDB, int i, int i1) {
        MyDB.execSQL("drop Table if exists users");
    }

    public void addContact(DataModel contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getName());
        values.put(HARGAMENU, contact.getHarga());
        values.put(DESKRIPSI, contact.getDeskripsi());
        db.insert(TABLE_CONTACTS, null, values);
        db.close();
    }
    public List<DataModel> getAllContacts() {
        List<DataModel> contactList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                DataModel contact = new DataModel();
                contact.setId(Integer.parseInt(cursor.getString(0)));
                contact.setName(cursor.getString(1));
                contact.setHarga(cursor.getString(2));
                contact.setDeskripsi(cursor.getString(3));

                contactList.add(contact);
            } while (cursor.moveToNext());
        }
        return contactList;
    }
    public int updateContact(DataModel contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getName());
        values.put(HARGAMENU, contact.getHarga());
        values.put(DESKRIPSI, contact.getDeskripsi());
        return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
                new String[]{String.valueOf(contact.getId())});
    }
    public void deleteContact(DataModel contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
                new String[]{String.valueOf(contact.getId())});
        db.close();
    }
    public Boolean insertData(String email, String password){
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", email);
        contentValues.put("password", password);
        long result = MyDatabase.insert("users", null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public Boolean checkEmail(String email){
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        Cursor cursor = MyDatabase.rawQuery("Select * from users where email = ?", new String[]{email});
        if(cursor.getCount() > 0) {
            return true;
        }else {
            return false;
        }
    }
    public Boolean checkEmailPassword(String email, String password){
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        Cursor cursor = MyDatabase.rawQuery("Select * from users where email = ? and password = ?", new String[]{email, password});
        if (cursor.getCount() > 0) {
            return true;
        }else {
            return false;
        }
    }
}
