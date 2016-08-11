package it.marcovit79.comicreader.download;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import it.marcovit79.comicreader.R;

public class ReceiveFileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_file);

        String localUriStr = getIntent().getDataString();
        final String destinationFilePath = CopyFileUtil.getRealPathFromURI(getApplicationContext(), localUriStr);

        if(destinationFilePath != null ) {
            TextView txtView = (TextView) findViewById(R.id.url_text);
            txtView.setText( "Vuoi aggiungere " + destinationFilePath +" ?");

            Button okBtn = (Button) findViewById(R.id.ok_btn);
            okBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CopyFileUtil.requestPermissions(ReceiveFileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
                    try {
                        File comicDir = getApplicationContext().getDir(CopyFileUtil.COMICS_DIR_STR, Context.MODE_PRIVATE);
                        CopyFileUtil.copyFileToLocalDir(
                                getApplicationContext(),
                                new File(destinationFilePath),
                                comicDir,
                                null
                        );
                    } catch (IOException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                    finally {
                        finish();
                    }
                }
            });

            Button cancelBtn = (Button) findViewById(R.id.cancel_btn);
            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        else {
            this.finish();
        }
    }

}
