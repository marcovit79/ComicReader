package it.marcovit79.comicreader.view;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;

import it.marcovit79.comicreader.R;
import it.marcovit79.comicreader.view.data.ComicBook;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class ViewComicActivity extends AppCompatActivity {

    public static final String COMIC_PATH = "comics";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_comic);

        View controls = findViewById(R.id.fullscreen_content_controls);
        View content= findViewById(R.id.fullscreen_content);
        ImageView imgView = (ImageView) findViewById(R.id.img_viewer);


        this.toggler = new ViewToggler(this, content, controls);


        this.comicBook = new ComicBook( new ImageViewerHandler(imgView));


        // Set up the user interaction to manually show or hide the system UI.
        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewComicActivity.this.toggler.scheduleShowToolbar(100);
                ViewComicActivity.this.toggler.scheduleHideToolbar(5 *1000);
            }
        });

        findViewById(R.id.nextButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewComicActivity.this.toggler.scheduleHideToolbar(5 *1000);
                ViewComicActivity.this.nextVignette();
            }
        });
        findViewById(R.id.prevButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewComicActivity.this.toggler.scheduleHideToolbar(5 *1000);
                ViewComicActivity.this.prevVignette();
            }
        });

    }

    private void loadComicBook() {
        Intent intent = getIntent();
        String path = intent.getStringExtra(COMIC_PATH);
        System.out.println("Set comic book " + path);

        try {
            comicBook.setComicBook(new File(path));
            comicBook.updateView();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void prevVignette() {
        this.comicBook.prevVignette();
        comicBook.updateView();
    }

    private void nextVignette() {
        this.comicBook.nextVignette();
        comicBook.updateView();
    }




    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        this.toggler.hideToolbar();

        loadComicBook();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                comicBook.relayoutCurrentZoneIfOnZoneAndUpdateView();
            }
        }, 100);

    }


    private ComicBook comicBook;

    private ViewToggler toggler;

}
