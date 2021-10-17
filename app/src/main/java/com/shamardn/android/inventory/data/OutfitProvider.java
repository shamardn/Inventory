package com.shamardn.android.inventory.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.shamardn.android.inventory.data.OutfitContract.OutfitEntry;


public class OutfitProvider extends ContentProvider {

    public static final String LOG_TAG = OutfitProvider.class.getSimpleName();
    private static final int OUTFITS = 1000;
    private static final int OUTFIT_ID = 2000;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(OutfitContract.CONTENT_AUTHORITY, OutfitContract.PATH_OUTFITS, OUTFITS);
        sUriMatcher.addURI(OutfitContract.CONTENT_AUTHORITY, OutfitContract.PATH_OUTFITS + "/#", OUTFIT_ID);
    }

    private OutfitDbHelper mDbHelper;

    /**
     * Initialize the provider and the database helper object.
     */
    @Override
    public boolean onCreate() {

        mDbHelper = new OutfitDbHelper(getContext());
        return true;
    }

    /**
     * Perform the query for the given URI. Use the given projection, selection, selection arguments, and sort order.
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        Cursor cursor;

        int match = sUriMatcher.match(uri);
        switch (match) {
            case OUTFITS:
                cursor = db.query(OutfitEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;

            case OUTFIT_ID:
                selection = OutfitEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(OutfitEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;

            default:
                throw new IllegalArgumentException("Can't query URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    /**
     * Insert new data into the provider with the given ContentValues.
     */
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case OUTFITS:
                return insertOutfit(uri, contentValues);
            default:
                throw new IllegalArgumentException("insertion is not supported for " + uri);
        }

    }

    private Uri insertOutfit(Uri uri, ContentValues contentValues) {
        // Check that the name is not null
        String name = contentValues.getAsString(OutfitEntry.COLUMN_OUTFIT_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Outfit requires a name");
        }

        String brand = contentValues.getAsString(OutfitEntry.COLUMN_OUTFIT_SUPPLIER);
        if (brand == null) {
            throw new IllegalArgumentException("Outfit requires a brand");
        }

        // Check that the gender is valid
        Integer gender = contentValues.getAsInteger(OutfitEntry.COLUMN_OUTFIT_GENDER_CATEGORY);
        if (gender == null || !OutfitEntry.isValidGender(gender)) {
            throw new IllegalArgumentException("Outfit requires valid gender");
        }

        // If the price is provided?
        Integer price = contentValues.getAsInteger(OutfitEntry.COLUMN_OUTFIT_PRICE);
        if (price != null && price < 0) {
            throw new IllegalArgumentException("Outfit requires valid price");
        }

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        long id = db.insert(OutfitEntry.TABLE_NAME, null, contentValues);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }

    /**
     * Updates the data at the given selection and selection arguments, with the new ContentValues.
     */
    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case OUTFITS:
                return updateOutfit(uri, contentValues, selection, selectionArgs);

            case OUTFIT_ID:
                selection = OutfitEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateOutfit(uri, contentValues, selection, selectionArgs);

            default:
                throw new IllegalArgumentException("updating is not supported for " + uri);
        }

    }

    private int updateOutfit(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        // Check that the name is not null
        if (contentValues.containsKey(OutfitEntry.COLUMN_OUTFIT_NAME)) {
            String name = contentValues.getAsString(OutfitEntry.COLUMN_OUTFIT_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Outfit requires a name");
            }
        }

        // Check that the brand is not null
        if (contentValues.containsKey(OutfitEntry.COLUMN_OUTFIT_SUPPLIER)) {
            String brand = contentValues.getAsString(OutfitEntry.COLUMN_OUTFIT_SUPPLIER);
            if (brand == null) {
                throw new IllegalArgumentException("Outfit requires a brand");
            }
        }

        // Check that the gender is valid
        if (contentValues.containsKey(OutfitEntry.COLUMN_OUTFIT_GENDER_CATEGORY)) {
            Integer gender = contentValues.getAsInteger(OutfitEntry.COLUMN_OUTFIT_GENDER_CATEGORY);
            if (gender == null || !OutfitEntry.isValidGender(gender)) {
                throw new IllegalArgumentException("Outfit requires valid gender");
            }
        }

        // If the color is provided?
        if (contentValues.containsKey(OutfitEntry.COLUMN_OUTFIT_COLOR)) {
            String color = contentValues.getAsString(OutfitEntry.COLUMN_OUTFIT_COLOR);
            if (color == null) {
                throw new IllegalArgumentException("Outfit requires valid color");
            }
        }

        // If the price is provided?
        if (contentValues.containsKey(OutfitEntry.COLUMN_OUTFIT_PRICE)) {
            Integer price = contentValues.getAsInteger(OutfitEntry.COLUMN_OUTFIT_PRICE);
            if (price != null && price < 0) {
                throw new IllegalArgumentException("Outfit requires valid price");
            }
        }

        // Check that the size is valid
        if (contentValues.containsKey(OutfitEntry.COLUMN_OUTFIT_SIZE)) {
            Integer size = contentValues.getAsInteger(OutfitEntry.COLUMN_OUTFIT_SIZE);
            if (size == null || !OutfitEntry.isValidSize(size)) {
                throw new IllegalArgumentException("Outfit requires valid size");
            }
        }

        // Check that the age is valid
        if (contentValues.containsKey(OutfitEntry.COLUMN_OUTFIT_AGE_CATEGORY)) {
            Integer age = contentValues.getAsInteger(OutfitEntry.COLUMN_OUTFIT_AGE_CATEGORY);
            if (age == null || !OutfitEntry.isValidAge(age)) {
                throw new IllegalArgumentException("Outfit requires valid age");
            }
        }

        // If there are no values to update, then don't try to update the database
        if (contentValues.size() == 0) {
            return 0;
        }

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        int rowsUpdated = db.update(OutfitEntry.TABLE_NAME, contentValues, selection, selectionArgs);

        // If 1 or more rows were updated, then notify all listeners that the data at the
        // given URI has changed
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }

    /**
     * Delete the data at the given selection and selection arguments.
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        // Get writeable database
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Track the number of rows that were deleted
        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case OUTFITS:
                rowsDeleted = db.delete(OutfitEntry.TABLE_NAME, selection, selectionArgs);
                break;

            case OUTFIT_ID:
                // Delete a single row given by the ID in the URI
                selection = OutfitEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = db.delete(OutfitEntry.TABLE_NAME, selection, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of rows deleted
        return rowsDeleted;
    }

    /**
     * Returns the MIME type of data for the content URI.
     */
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case OUTFITS:
                return OutfitEntry.CONTENT_LIST_TYPE;
            case OUTFIT_ID:
                return OutfitEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }
}