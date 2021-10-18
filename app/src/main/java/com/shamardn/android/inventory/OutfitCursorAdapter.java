package com.shamardn.android.inventory;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.shamardn.android.inventory.data.OutfitContract.OutfitEntry;

public class OutfitCursorAdapter extends CursorAdapter {
    public OutfitCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_card,parent,false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView tvName = view.findViewById(R.id.name);
        TextView tvsupplier = view.findViewById(R.id.supplier);
        TextView tvQuantity = view.findViewById(R.id.quantity);
        TextView tvPrice = view.findViewById(R.id.price);

        int nameColumn = cursor.getColumnIndex(OutfitEntry.COLUMN_OUTFIT_NAME);
        int supplierColumn = cursor.getColumnIndex(OutfitEntry.COLUMN_OUTFIT_SUPPLIER);
        int quantityColumn = cursor.getColumnIndex(OutfitEntry.COLUMN_OUTFIT_QUANTITY);
        int priceColumn = cursor.getColumnIndex(OutfitEntry.COLUMN_OUTFIT_PRICE);

        String name = cursor.getString(nameColumn);
        String supplier = cursor.getString(supplierColumn);
        int quantity = cursor.getInt(quantityColumn);
        int price = cursor.getInt(priceColumn);

        tvName.setText(name);
        tvsupplier.setText(supplier.toUpperCase());
        tvQuantity.setText(quantity+ " pcs");
        tvPrice.setText("$"+price);
    }
}
