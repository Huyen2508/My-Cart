package hanu.a2_1801040109.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {
    public static final String DB_NAME= "cart.db";
    public static final int DB_VERSION = 1;

    public DbHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + DbSchema.CartTable.NAME + "(" +
                DbSchema.CartTable.Cols.P_ID +" INTEGER PRIMARY KEY, " +
                DbSchema.CartTable.Cols.P_THUMBNAIL + " TEXT," +
                DbSchema.CartTable.Cols.P_NAME + " TEXT, " +
                DbSchema.CartTable.Cols.P_UNIT_PRICE + " DOUBLE, "+
                DbSchema.CartTable.Cols.P_QUANTITY + " INTEGER" + ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //drop existing table
        Log.w("My Cart", "My Cart: upgrading DB; dropping/recreating tables.");
        db.execSQL("DROP TABLE IF EXISTS " + DbSchema.CartTable.NAME);

        //other table here

        //onCreate again
        onCreate(db);
    }
}

