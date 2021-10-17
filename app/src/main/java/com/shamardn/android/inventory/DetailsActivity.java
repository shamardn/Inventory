package com.shamardn.android.inventory;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.shamardn.android.inventory.data.OutfitContract.OutfitEntry;

public class DetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_ID = 555;
    private EditText mNameAdding, mSupplierAdding,mColorAdding,mPriceAdding;
    private Spinner mGenderSpinner,mAgeSpinner,mSizeSpinner;
    private int mGender, mAge, mSize;
    private Uri currentOutfitUri;
    private boolean isOutfitChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        currentOutfitUri = intent.getData();
        if (currentOutfitUri == null){
            setTitle(getString(R.string.details_activity_title_new_outfit));
            
            invalidateOptionsMenu();
        } else {
            setTitle(getString(R.string.details_activity_title_edit_outfit));

            getSupportLoaderManager().initLoader(LOADER_ID, null, this);

        }
        mNameAdding = findViewById(R.id.adding_name);
        mSupplierAdding = findViewById(R.id.adding_brand);
        mColorAdding = findViewById(R.id.adding_color);
        mPriceAdding  =findViewById(R.id.adding_price);

        mGenderSpinner = findViewById(R.id.adding_gender_spinner);
        mAgeSpinner = findViewById(R.id.adding_age_spinner);
        mSizeSpinner = findViewById(R.id.adding_size_spinner);

        mGender = 0;
        mAge = 0;
        mSize = 0;

        setupGenderSpinner();
        setupAgeSpinner();
        setupSizeSpinner();

        mNameAdding.setOnTouchListener(mTouchListener);
        mSupplierAdding.setOnTouchListener(mTouchListener);
        mColorAdding.setOnTouchListener(mTouchListener);
        mPriceAdding.setOnTouchListener(mTouchListener);
        mGenderSpinner.setOnTouchListener(mTouchListener);
        mAgeSpinner.setOnTouchListener(mTouchListener);
        mSizeSpinner.setOnTouchListener(mTouchListener);

    }

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            isOutfitChanged = true;
            return false;
        }
    };

    @Override
    public void onBackPressed() {
        if (!isOutfitChanged){
            super.onBackPressed();
            return;
        }else {
            new MaterialAlertDialogBuilder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Discard Changes")
                    .setMessage("Do you need discard Changes you made?")
                    .setPositiveButton("Discard", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
        }
    }

    private void setupGenderSpinner() {

        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_gender_options, android.R.layout.simple_spinner_item);

        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        mGenderSpinner.setAdapter(genderSpinnerAdapter);

        mGenderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.gender_male))) {
                        mGender = OutfitEntry.GENDER_MALE; // Male
                    } else if (selection.equals(getString(R.string.gender_female))) {
                        mGender = OutfitEntry.GENDER_FEMALE; // Female
                    } else {
                        mGender = OutfitEntry.GENDER_UNKNOWN; // Unknown
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mGender = OutfitEntry.GENDER_UNKNOWN; // Unknown
            }
        });
    }

    private void setupAgeSpinner() {
        ArrayAdapter ageSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_age_options, android.R.layout.simple_spinner_item);

        ageSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        mAgeSpinner.setAdapter(ageSpinnerAdapter);

         mAgeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.age_Kids))) {
                        mAge = OutfitEntry.AGE_KID;
                    } else {
                        mAge = OutfitEntry.AGE_ADULT;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mAge = OutfitEntry.AGE_KID;
            }
        });
    }

    private void setupSizeSpinner() {
       ArrayAdapter sizeSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_size_options, android.R.layout.simple_spinner_item);

       sizeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        mSizeSpinner.setAdapter(sizeSpinnerAdapter);

        mSizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.size_small))) {
                        mSize = OutfitEntry.SIZE_SMALL;
                    } else if (selection.equals(getString(R.string.size_medium))) {
                        mSize = OutfitEntry.SIZE_MEDIUM;
                    } else if (selection.equals(getString(R.string.size_large))) {
                        mSize = OutfitEntry.SIZE_LARGE;
                    } else {
                        mSize = OutfitEntry.SIZE_XLARGE;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mSize = OutfitEntry.SIZE_SMALL;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (currentOutfitUri == null){
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
            return true;
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.save:
                saveOutfit();
                finish();
                return true;

            case R.id.action_delete:
                showDeleteDialog();
                return true;

            case android.R.id.home:
                if (!isOutfitChanged){
                    NavUtils.navigateUpFromSameTask(this);
                    return true;
                }else {
                    new MaterialAlertDialogBuilder(this)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("Discard")
                            .setMessage("Do you need To discard Changes you made?")
                            .setPositiveButton("Discard", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    NavUtils.navigateUpFromSameTask(DetailsActivity.this);
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
                    return true;
                }

        }
        return super.onOptionsItemSelected(item);
    }

    private void showDeleteDialog() {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Delete Outfit")
                .setMessage("Are You sure ?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delete();
                    }
                })
                .setNegativeButton("Discard", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    private void delete() {
        if (currentOutfitUri != null){
            int rowsDeleted =   getContentResolver().delete(currentOutfitUri,null,null);
            if (rowsDeleted == 0){
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "Successful Deletion", Toast.LENGTH_SHORT).show();
            }
            finish();
        }
    }

    private void saveOutfit() {

        String nameString = mNameAdding.getText().toString().trim();
        String brandString = mSupplierAdding.getText().toString().trim();
        String colorString = mColorAdding.getText().toString().trim();
        String priceString = mPriceAdding.getText().toString().trim();

        if (TextUtils.isEmpty(nameString) || TextUtils.isEmpty(brandString) || TextUtils.isEmpty(colorString) || TextUtils.isEmpty(priceString)) {
            Toast.makeText(getApplicationContext(), "Error:\nEmpty Field", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!OutfitEntry.isValidGender(mGender) || !OutfitEntry.isValidAge(mAge) || !OutfitEntry.isValidSize(mSize)) {
            Toast.makeText(getApplicationContext(), "Error:\nSelect proper gender and age and size ", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues values = new ContentValues();
        values.put(OutfitEntry.COLUMN_OUTFIT_NAME, nameString);
        values.put(OutfitEntry.COLUMN_OUTFIT_SUPPLIER, brandString);
        values.put(OutfitEntry.COLUMN_OUTFIT_COLOR, colorString);
        values.put(OutfitEntry.COLUMN_OUTFIT_PRICE, priceString);
        values.put(OutfitEntry.COLUMN_OUTFIT_GENDER_CATEGORY, mGender);
        values.put(OutfitEntry.COLUMN_OUTFIT_AGE_CATEGORY, mAge);
        values.put(OutfitEntry.COLUMN_OUTFIT_SIZE, mSize);

        if (currentOutfitUri == null) {
            Uri insertedUri = getContentResolver().insert(OutfitEntry.CONTENT_URI, values);

            if (insertedUri == null) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.savedError), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.item_saved), Toast.LENGTH_SHORT).show();
            }
        }
         else {
            int rowsAffected = getContentResolver().update(currentOutfitUri, values, null, null);

            if (rowsAffected == 0) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.editor_update_outfit_failed), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.editor_update_outfit_successful), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String[] projection = {
                OutfitEntry.COLUMN_OUTFIT_NAME,
                OutfitEntry.COLUMN_OUTFIT_SUPPLIER,
                OutfitEntry.COLUMN_OUTFIT_COLOR,
                OutfitEntry.COLUMN_OUTFIT_PRICE,
                OutfitEntry.COLUMN_OUTFIT_GENDER_CATEGORY,
                OutfitEntry.COLUMN_OUTFIT_AGE_CATEGORY,
                OutfitEntry.COLUMN_OUTFIT_SIZE};

        return new CursorLoader(this,
                currentOutfitUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        if (cursor.moveToNext()) {
            int outfitColumnName = cursor.getColumnIndex(OutfitEntry.COLUMN_OUTFIT_NAME);
            int outfitColumnBrand = cursor.getColumnIndex(OutfitEntry.COLUMN_OUTFIT_SUPPLIER);
            int outfitColumnColor = cursor.getColumnIndex(OutfitEntry.COLUMN_OUTFIT_COLOR);
            int outfitColumnPrice = cursor.getColumnIndex(OutfitEntry.COLUMN_OUTFIT_PRICE);
            int outfitColumnGender = cursor.getColumnIndex(OutfitEntry.COLUMN_OUTFIT_GENDER_CATEGORY);
            int outfitColumnAge = cursor.getColumnIndex(OutfitEntry.COLUMN_OUTFIT_AGE_CATEGORY);
            int outfitColumnSize = cursor.getColumnIndex(OutfitEntry.COLUMN_OUTFIT_SIZE);

            String outfitName = cursor.getString(outfitColumnName);
            String outfitBrand = cursor.getString(outfitColumnBrand);
            String outfitColor = cursor.getString(outfitColumnColor);
            String outfitPrice = cursor.getString(outfitColumnPrice);
            mGender = cursor.getInt(outfitColumnGender);
            mAge = cursor.getInt(outfitColumnAge);
            mSize = cursor.getInt(outfitColumnSize);

            mNameAdding.setText(outfitName);
            mSupplierAdding.setText(outfitBrand);
            mColorAdding.setText(outfitColor);
            mPriceAdding.setText(String.valueOf(outfitPrice));
            mGenderSpinner.setSelection(mGender);
            mAgeSpinner.setSelection(mAge);
            mSizeSpinner.setSelection(mSize);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mNameAdding.setText("");
        mSupplierAdding.setText("");
        mColorAdding.setText("");
        mPriceAdding.setText("");
        mGenderSpinner.setSelection(OutfitEntry.GENDER_UNKNOWN);
        mAgeSpinner.setSelection(OutfitEntry.AGE_KID);
        mSizeSpinner.setSelection(OutfitEntry.SIZE_SMALL);
    }
}