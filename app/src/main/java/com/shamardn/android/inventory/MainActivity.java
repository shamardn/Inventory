package com.shamardn.android.inventory;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.shamardn.android.inventory.data.OutfitContract.OutfitEntry;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private final static int LOADER_ID = 0;
    private ListView outfitListView;
    private OutfitCursorAdapter adapter;
    private FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        outfitListView = findViewById(R.id.list);
        adapter = new OutfitCursorAdapter(this,null);
        outfitListView.setAdapter(adapter);

        View emptyView = findViewById(R.id.empty_view);
        outfitListView.setEmptyView(emptyView);

        outfitListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this,DetailsActivity.class);

                Uri currentOutfitUri = ContentUris.withAppendedId(OutfitEntry.CONTENT_URI,id);

                intent.setData(currentOutfitUri);

                startActivity(intent);
            }
        });
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,DetailsActivity.class);
                startActivity(intent);
            }
        });


        getSupportLoaderManager().initLoader(LOADER_ID,null,this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_outfit:
                addOutfit();
                return true;

            case R.id.delete_all:
                showDeleteDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDeleteDialog() {
        if (adapter.isEmpty()){
            Toast.makeText(getApplicationContext(), "Can't free an Empty Store", Toast.LENGTH_SHORT).show();
            return;
        }
        new MaterialAlertDialogBuilder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Delete All Outfits")
                .setMessage("Are you sure?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteAll();
                    }
                })
                .setNegativeButton("Discard", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    private void deleteAll() {
        int rowsDeleted =  getContentResolver().delete(OutfitEntry.CONTENT_URI,null,null);

        if (rowsDeleted == 0){
            Toast.makeText(this, "there is no Outfit in the Inventory", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(this, "there are "+rowsDeleted+" deleted Outfits in the Inventory", Toast.LENGTH_LONG).show();

    }

    private void addOutfit() {
        ContentValues values = new ContentValues();
        values.put(OutfitEntry.COLUMN_OUTFIT_NAME,"T-Shirt");
        values.put(OutfitEntry.COLUMN_OUTFIT_SUPPLIER,"rivin");
        values.put(OutfitEntry.COLUMN_OUTFIT_GENDER_CATEGORY,OutfitEntry.GENDER_MALE);
        values.put(OutfitEntry.COLUMN_OUTFIT_AGE_CATEGORY,OutfitEntry.AGE_ADULT);
        values.put(OutfitEntry.COLUMN_OUTFIT_PRICE,399);
        values.put(OutfitEntry.COLUMN_OUTFIT_COLOR,"Red");
        values.put(OutfitEntry.COLUMN_OUTFIT_SIZE,OutfitEntry.SIZE_LARGE);

        Uri newUri = getContentResolver().insert(OutfitEntry.CONTENT_URI,values);

        Log.v("MainActivity","New Row Id : " + newUri);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {

        String[] projection = {OutfitEntry._ID,OutfitEntry.COLUMN_OUTFIT_NAME,OutfitEntry.COLUMN_OUTFIT_SUPPLIER,OutfitEntry.COLUMN_OUTFIT_PRICE,OutfitEntry.COLUMN_OUTFIT_GENDER_CATEGORY,OutfitEntry.COLUMN_OUTFIT_AGE_CATEGORY,OutfitEntry.COLUMN_OUTFIT_COLOR,OutfitEntry.COLUMN_OUTFIT_SIZE};

        return new CursorLoader(this,OutfitEntry.CONTENT_URI,projection,null,null,null);

    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}