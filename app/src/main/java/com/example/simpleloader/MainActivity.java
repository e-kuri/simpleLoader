package com.example.simpleloader;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "MainActivityTAG_";

    private static final String SMS_CONTENT_URI = "content://sms/";

    private static final int LIST_ID = 10;

    private static final int SMS_CALL_LOG = 69;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_SMS)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS}, SMS_CALL_LOG);
            }else{
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS}, SMS_CALL_LOG);
            }
        }else {
            getSupportLoaderManager().initLoader(LIST_ID, null, this);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

            return new CursorLoader(getApplicationContext(),
                    Uri.parse(SMS_CONTENT_URI),
                    null,
                    null,
                    null,
                    null);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == SMS_CALL_LOG){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getSupportLoaderManager().initLoader(LIST_ID, null, this);
            }else{
                Toast.makeText(this, "no use for this app", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                final String messageBody = cursor.getString(cursor.getColumnIndex("body"));
                final String messageAddress = cursor.getString(cursor.getColumnIndex("address"));
                Log.d(TAG, "onLoadFinished: " + messageAddress + " " + messageBody);
            } while (cursor.moveToNext());
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d(TAG, "onLoaderReset: ");
    }
}
