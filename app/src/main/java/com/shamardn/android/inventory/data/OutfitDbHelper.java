package com.shamardn.android.inventory.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.jetbrains.annotations.Nullable;

import com.shamardn.android.inventory.data.OutfitContract.OutfitEntry;
public class OutfitDbHelper extends SQLiteOpenHelper {

    public final static String DATABASE_NAME = "inventory.db";
    public final static int DATABASE_VERSION = 1;

    public OutfitDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String SQL_CREATE_OUTFITS_TABLE =  "CREATE TABLE " + OutfitEntry.TABLE_NAME + " ("
                + OutfitEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + OutfitEntry.COLUMN_OUTFIT_NAME + " TEXT NOT NULL, "
                + OutfitEntry.COLUMN_OUTFIT_SUPPLIER + " TEXT, "
                + OutfitEntry.COLUMN_OUTFIT_QUANTITY + " INTEGER, "
                + OutfitEntry.COLUMN_OUTFIT_GENDER_CATEGORY + " INTEGER, "
                + OutfitEntry.COLUMN_OUTFIT_COLOR + " TEXT, "
                + OutfitEntry.COLUMN_OUTFIT_SIZE + " INTEGER, "
                + OutfitEntry.COLUMN_OUTFIT_AGE_CATEGORY + " INTEGER, "
                + OutfitEntry.COLUMN_OUTFIT_IMAGE + " BOLB, "
                + OutfitEntry.COLUMN_OUTFIT_PRICE + " INTEGER DEFAULT 0);";
        // Execute the SQL statement
        db.execSQL(SQL_CREATE_OUTFITS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
