package com.example.neerajvishwakarma.smartmed1;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class MedicineDB extends SQLiteAssetHelper
{
    private static final String databaseName = "medicine.db";
    private static final int dbVersion = 2;

    public MedicineDB(Context context)
    {
        super(context, databaseName, null, dbVersion);
    }
}
