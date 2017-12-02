package com.example.neerajvishwakarma.smartmed1;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MedicineAccess
{
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static MedicineAccess instance;

    /**
     * Private constructor to aboid object creation from outside classes.
     *
     * @param context
     */
    private MedicineAccess(Context context) {
        this.openHelper = new MedicineDB(context);
    }

    /**
     * Return a singleton instance of DatabaseAccess.
     *
     * @param context the Context
     * @return the instance of DabaseAccess
     */
    public static MedicineAccess getInstance(Context context) {
        if (instance == null) {
            instance = new MedicineAccess(context);
        }
        return instance;
    }

    /**
     * Open the database connection.
     */
    public void open() {
        this.database = openHelper.getWritableDatabase();
    }

    /**
     * Close the database connection.
     */
    public void close() {
        if (database != null) {
            this.database.close();
        }
    }

    /**
     * Read all quotes from the database.
     *
     * @return a List of quotes
     */
    public ArrayList<String> getFullData(String index)
    {
        ArrayList<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT description, price, symptoms, name FROM medicinedata where abstractName = '" + index + "'", null);

        list.add(cursor.getString(cursor.getColumnIndex("description")));
        list.add(cursor.getString(cursor.getColumnIndex("price")));
        list.add(cursor.getString(cursor.getColumnIndex("symptoms")));
        list.add(cursor.getString(cursor.getColumnIndex("name")));

        cursor.close();
        return list;
    }
}