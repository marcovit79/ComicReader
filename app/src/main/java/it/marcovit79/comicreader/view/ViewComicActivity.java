package it.marcovit79.comicreader.view;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

import it.marcovit79.comicreader.R;
import it.marcovit79.comicreader.view.data.ComicBook;
import it.marcovit79.comicreader.vignetting.VignettingIssueCollector;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class ViewComicActivity extends AppCompatActivity implements DelayToolbarHideListener {

    public static final String COMIC_PATH = "comics";
    public static final int TOOLBAR_HIDE_DELAY_MS = 3 * 1000;
    private static final String LOG_TAG = "ViewComicActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_comic);

        View controls = findViewById(R.id.fullscreen_content_controls);
        View content = findViewById(R.id.fullscreen_content);
        ImageView imgView = (ImageView) findViewById(R.id.img_viewer);
        SeekBar pageBar = (SeekBar) findViewById(R.id.pageBar);
        TextView pageTextHover = (TextView)  findViewById(R.id.page_num_hover);
        TextView pageText = (TextView)  findViewById(R.id.page_down_txt);

        ImageViewerHandler imgViewHandler = new ImageViewerHandler(imgView, pageText);

        this.toggler = new ViewToggler(this, content, controls);
        this.comicBook = new ComicBook( imgViewHandler);
        this.pageBarHandler = new PageBarHandler(this, comicBook, pageBar, pageTextHover);

        VignettingIssueCollector vignettingAdv = new VignettingIssueCollector(getApplicationContext(), comicBook);
        imgViewHandler.setVignettingDebugger(vignettingAdv);

        // Set up the user interaction to manually show or hide the system UI.
        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewComicActivity.this.toggler.scheduleShowToolbar(100);
                delayToolbarHide();
            }
        });

        findViewById(R.id.nextButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delayToolbarHide();
                ViewComicActivity.this.nextVignette();
            }
        });
        findViewById(R.id.prevButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delayToolbarHide();
                ViewComicActivity.this.prevVignette();
            }
        });

    }

    private void loadComicBook() {
        Intent intent = getIntent();
        String path = intent.getStringExtra(COMIC_PATH);
        Log.d(LOG_TAG, "Set comic book " + path);

        try {
            comicBook.setComicBook(new File(path));
            comicBook.updateView();
            pageBarHandler.setProgress(0);
            pageBarHandler.setMax(comicBook.getNumOfPage() - 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void delayToolbarHide() {
        this.toggler.scheduleHideToolbar(TOOLBAR_HIDE_DELAY_MS);
    }

    private void prevVignette() {
        this.comicBook.prevVignette();
        comicBook.updateView();
        updatePageBar();
    }

    private void nextVignette() {
        this.comicBook.nextVignette();
        comicBook.updateView();
        updatePageBar();
    }

    private void updatePageBar() {
        int page = this.comicBook.getPage();
        pageBarHandler.setProgress(page);
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
                comicBook.relayoutCurrentZoneIfOnZoneAndUpdateView(false);
            }
        }, 100);

    }


    private ComicBook comicBook;

    private ViewToggler toggler;

    private PageBarHandler pageBarHandler;


}
