package com.shamardn.android.inventory.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class OutfitContract {

    public static final String CONTENT_AUTHORITY = "com.shamardn.android.inventory";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_OUTFITS = "outfits";

    private OutfitContract() {
    }

    public final static class OutfitEntry implements BaseColumns {

        /** The content URI to access the outfit data in the provider */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_OUTFITS);

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of outfits.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_OUTFITS;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single outfit.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_OUTFITS;


        public final static String TABLE_NAME = "outfits";

        public final static String _ID = BaseColumns._ID;

        public final static String COLUMN_OUTFIT_NAME = "name";
        public final static String COLUMN_OUTFIT_SUPPLIER = "supplier";
        public final static String COLUMN_OUTFIT_QUANTITY = "quantity";
        public final static String COLUMN_OUTFIT_PRICE = "price";
        public final static String COLUMN_OUTFIT_COLOR = "COLOR";
        public final static String COLUMN_OUTFIT_SIZE = "size";
        public final static String COLUMN_OUTFIT_GENDER_CATEGORY = "gender_category";
        public final static String COLUMN_OUTFIT_AGE_CATEGORY = "age_category";
        public final static String COLUMN_OUTFIT_IMAGE = "image";


        public final static int GENDER_UNKNOWN = 0;
        public final static int GENDER_MALE = 1;
        public final static int GENDER_FEMALE = 2;

        public final static int SIZE_SMALL = 0;
        public final static int SIZE_MEDIUM = 1;
        public final static int SIZE_LARGE = 2;
        public final static int SIZE_XLARGE = 3;

        public final static int AGE_KID = 0;
        public final static int AGE_ADULT = 1;

        public static boolean isValidGender(int gender) {
            if (gender == GENDER_UNKNOWN || gender == GENDER_MALE || gender == GENDER_FEMALE) {
                return true;
            }
            return false;
        }

        public static boolean isValidSize(int size) {
            if (size == SIZE_SMALL || size == SIZE_MEDIUM || size == SIZE_LARGE || size == SIZE_XLARGE) {
                return true;
            }
            return false;
        }

        public static boolean isValidAge(int age) {
            if (age == AGE_KID || age == AGE_ADULT) {
                return true;
            }
            return false;
        }

        public static boolean isRealQuantity(int quantity){
            if (quantity >= 0 ){
                return true;
            }
            return false;
        }
    }
}
