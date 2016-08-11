package it.marcovit79.comicreader.download;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * Created by mvit on 1-8-16.
 */
public class CopyFileUtil {

    public static final String NEW_COMIC_ACTION = "it.mvit79.comicreader.NEW_COMIC";

    public static final String COMICS_DIR_STR = "comics";
    private static final String LOG_TAG = "DownloadReceiver";

    public static void copyFileToLocalDir(Context ctx, String localUri, File localDir, String localName) throws IOException {
        String destFileName;
        String localPath = getRealPathFromURI(ctx, localUri);
        File srcFile = new File(localPath);

        copyFileToLocalDir(ctx, srcFile, localDir, localName);
    }

    public static void copyFileToLocalDir(Context ctx, File srcFile, File localDir, String localNameOrNull) throws IOException {
        String localName;
        if(localNameOrNull == null) {
            localName = srcFile.getName();
        }
        else {
            localName = localNameOrNull;
        }

        File dstFile = new File(localDir, localName);

        Log.d(LOG_TAG, "CP \"" + srcFile+ "\"  \"" + dstFile + "\"");
        copy(srcFile, dstFile);

        ctx.sendBroadcast( new Intent( NEW_COMIC_ACTION ));
    }


    public static String getRealPathFromURI(Context context, String contentUriStr) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Uri contentUri = Uri.parse(contentUriStr);

        Cursor cursor = null;
        String path;
        try {
            cursor = context.getContentResolver().query(contentUri,  null, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            if(cursor.moveToFirst()) {
                path =  cursor.getString(column_index);
            }
            else {
                path = null;
            }
        }
        catch (Exception exc) {
            exc.printStackTrace();
            path = null;
            Toast.makeText(context, exc.getMessage(), Toast.LENGTH_LONG).show();
        }
        finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return path;
    }

    public static void copy(File src, File dst) throws IOException {
        FileInputStream inStream = new FileInputStream(src);
        FileOutputStream outStream = new FileOutputStream(dst);
        FileChannel inChannel = inStream.getChannel();
        FileChannel outChannel = outStream.getChannel();
        inChannel.transferTo(0, inChannel.size(), outChannel);
        inStream.close();
        outStream.close();
    }

    public static boolean requestPermissions(Activity activity, String permissionStr) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, permissionStr);

        boolean result;
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE },
                    1
            );
            result = false;
        }
        else {
            result = true;
        }
        return result;
    }

}
