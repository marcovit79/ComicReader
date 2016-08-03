package it.marcovit79.comicreader.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by mvit on 30-7-16.
 */
public class ViewToggler {

    private View content;
    private View toolbar;
    private AppCompatActivity activity;
    private boolean toolbarVisible;

    private Handler taskQueue;

    private boolean go_fullscreen_on_hide = true;


    public ViewToggler(AppCompatActivity activity, View content, View toolbar) {
        this.taskQueue = new Handler();

        this.content = content;
        this.toolbar = toolbar;
        this.activity = activity;
    }


    public void goFullScreen() {
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        content.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                //| View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }


    public void scheduleHideToolbar(int delayMs) {
        taskQueue.removeCallbacks(CALL_HIDE);
        taskQueue.postDelayed(CALL_HIDE, delayMs);
    }

    public void scheduleShowToolbar(int delayMs) {
        taskQueue.removeCallbacks(CALL_SHOW);
        taskQueue.postDelayed(CALL_SHOW, delayMs);
    }


    public void hideToolbar() {
        if(go_fullscreen_on_hide) {
            this.goFullScreen();
        }
        toolbar.setVisibility(View.GONE);
        toolbarVisible = false;
    }

    public void showToolbar() {
        toolbar.setVisibility(View.VISIBLE);
        toolbarVisible = true;
    }



    private final Runnable CALL_HIDE = new Runnable() {
        @Override
        public void run() {
            ViewToggler.this.hideToolbar();
        }
    };

    private final Runnable CALL_SHOW = new Runnable() {
        @Override
        public void run() {
            ViewToggler.this.showToolbar();
        }
    };
}
