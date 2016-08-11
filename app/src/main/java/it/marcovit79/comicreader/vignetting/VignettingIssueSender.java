package it.marcovit79.comicreader.vignetting;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.UUID;

import it.marcovit79.comicreader.download.CopyFileUtil;
import it.marcovit79.comicreader.view.data.ComicBook;

/**
 * Created by mvit on 5-8-16.
 */
public class VignettingIssueSender {

    public static final String SUPPORT_EMAIL = "marcovit79@gmail.com";
    private Activity activity;

    public VignettingIssueSender(Activity activity) {
        this.activity = activity;
    }

    public void sendVignetteIssueEmail() {
        boolean hasPermission = CopyFileUtil.requestPermissions(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if(hasPermission) {
            doSendMail();
        }
    }

    private void doSendMail() {
        File attachmentFile = VignettingIssueCollector.getVignetingIssuesFile(activity);
        File destFile = new File( activity.getExternalCacheDir(), UUID.randomUUID().toString() );

        try {
            CopyFileUtil.copy(attachmentFile, destFile);
            Uri attachmentPath = Uri.fromFile( destFile );

            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("vnd.android.cursor.dir/email");
            emailIntent.putExtra(Intent.EXTRA_EMAIL  , new String[]{SUPPORT_EMAIL});
            emailIntent.putExtra(Intent.EXTRA_STREAM, attachmentPath );
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Vignetting ISSUES");
            emailIntent.putExtra(Intent.EXTRA_TEXT   , "Check the attachment");


            activity.startActivityForResult(emailIntent, 2);
        } catch (ActivityNotFoundException ex) {
            Toast.makeText(activity, "I can't send email.", Toast.LENGTH_LONG).show();
        } catch (IOException ex) {
            Toast.makeText(activity, "I can't send email.", Toast.LENGTH_LONG).show();
        }
    }


    public boolean areVignettingIssuesPresent() {
        boolean result = VignettingIssueCollector.getVignetingIssuesFile(activity).exists();
        return result;
    }

    public void clearCacheAndIsues() {
        for(File f: activity.getExternalCacheDir().listFiles()) {
            f.delete();
        }
        VignettingIssueCollector.getVignetingIssuesFile(activity).delete();
        Log.i(VignettingIssueCollector.LOG_TAG, "Clear issues informations");
    }
}
