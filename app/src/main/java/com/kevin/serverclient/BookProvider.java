package com.kevin.serverclient;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

/**
 * Created by Kevin on 2019/4/10<br/>
 * Blog:https://blog.csdn.net/student9128<br/>
 * Describe:<br/>
 */
public class BookProvider extends ContentProvider {
    private static final String TAG = "BookProvider";
    public static final String AUTHORITY = "com.kevin.server.provider";
    public static final Uri BOOK_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/book");
    public static final Uri USER_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/user");
    public static final int BOOK_URI_CODE = 0;
    public static final int USER_URI_CODE = 1;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private Context mContext;
    private SQLiteDatabase mDb;

    static {
        sUriMatcher.addURI(AUTHORITY, "book", BOOK_URI_CODE);
        sUriMatcher.addURI(AUTHORITY, "user", USER_URI_CODE);
    }

    @Override
    public boolean onCreate() {
        Log.d(TAG, "onCreate, current thread: " + Thread.currentThread().getName());
        mContext = getContext();
        initProviderData();
        return true;
    }

    private void initProviderData() {
        mDb = new DbOpenHelper(mContext, DbOpenHelper.DB_NAME, null, DbOpenHelper.DB_VERSION).getWritableDatabase();
        mDb.execSQL("delete from " + DbOpenHelper.BOOK_TABLE_NAME);
        mDb.execSQL("delete from " + DbOpenHelper.USER_TABLE_NAME);
        mDb.execSQL("insert into book values(3,'Android');");
        mDb.execSQL("insert into book values(4,'iOS');");
        mDb.execSQL("insert into book values(5,'Html5');");
        mDb.execSQL("insert into user values(1,'Jack', 1);");
        mDb.execSQL("insert into user values(2,'LiLy',0);");

    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Log.d(TAG, "query,current thread:" + Thread.currentThread().getName());
        String tableName = getTableName(uri);
        if (tableName == null) {
            throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        return mDb.query(tableName, projection, selection, selectionArgs, null, null, sortOrder, null);
    }

    @Override
    public String getType(Uri uri) {
        Log.d(TAG, "getType");
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.d(TAG, "insert");
        String tableName = getTableName(uri);
        if (tableName == null) {
            throw new IllegalArgumentException("Unsupported URI: " + uri + " for insert.");
        }
        mDb.insert(tableName, null, values);
        mContext.getContentResolver().notifyChange(uri, null);
        return uri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.d(TAG, "delete");
        String tableName = getTableName(uri);
        if (tableName == null) {
            throw new IllegalArgumentException("Unsupported URI: " + uri + " for delete.");
        }
        int count = mDb.delete(tableName, selection, selectionArgs);
        if (count > 0) {
            mContext.getContentResolver().notifyChange(uri, null);
        }
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        Log.d(TAG, "update");
        String tableName = getTableName(uri);
        if (tableName == null) {
            throw new IllegalArgumentException("Unsupported Uri: " + uri + " for update.");
        }
        int row = mDb.update(tableName, values, selection, selectionArgs);
        if (row > 0) {
            mContext.getContentResolver().notifyChange(uri, null);
        }
        return row;
    }

    private String getTableName(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case BOOK_URI_CODE:
                return DbOpenHelper.BOOK_TABLE_NAME;
            case USER_URI_CODE:
                return DbOpenHelper.USER_TABLE_NAME;
        }
        return null;
    }
}
