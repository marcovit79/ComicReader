package it.marcovit79.comicreader.download;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.util.ArrayMap;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.Manifest;

/**
 * Created by mvit on 28-7-16.
 */
public class DownloadEndedBrodcastReceiver extends BroadcastReceiver {

    private static final String LOG_TAG = "DownloadReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d(LOG_TAG, "Received broadcast action " + action);

        try {
            String[] localUriAndFileName = retrieveInfo(context, intent);

            if( localUriAndFileName != null ) {

                File comicDir = context.getDir(CopyFileUtil.COMICS_DIR_STR, Context.MODE_PRIVATE);
                String localUriStr = localUriAndFileName[0];
                String destFileName= localUriAndFileName[1];

                Log.d(LOG_TAG, "COPY from " + localUriStr + " to " + destFileName );
                CopyFileUtil.copyFileToLocalDir(context, localUriStr, comicDir, destFileName);
            }

        } catch (IOException exc) {
            exc.printStackTrace();
            Toast.makeText(context, exc.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private String[] retrieveInfo(Context ctx, Intent intent) throws UnsupportedEncodingException {
        long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
        DownloadManager dm = (DownloadManager) ctx.getSystemService(Context.DOWNLOAD_SERVICE);

        Cursor cursor = dm.query(
                new DownloadManager.Query()
                        .setFilterById(downloadId)
            );

        String[] info;
        if(cursor.moveToFirst()) {
            String remoteUri = getStringValueByColumnName(cursor, DownloadManager.COLUMN_URI);
            String localUri = getStringValueByColumnName(cursor, DownloadManager.COLUMN_LOCAL_URI);

            String  destFileName = URLDecoder.decode(remoteUri.replaceFirst(".*/", ""), "UTF-8");

            info = new String[] { localUri, destFileName};
        }
        else {
            info = null;
        }
        return info;
    }

    private String getStringValueByColumnName(Cursor downloadCursor, String columnName) {
        return downloadCursor.getString(downloadCursor.getColumnIndex( columnName ));
    }
}

