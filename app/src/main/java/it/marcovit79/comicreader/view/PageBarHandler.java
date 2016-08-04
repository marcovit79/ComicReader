package it.marcovit79.comicreader.view;

import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.fenchtose.tooltip.Tooltip;

import it.marcovit79.comicreader.view.data.ComicBook;

/**
 * Created by mvit on 3-8-16.
 */
public class PageBarHandler {

    private ComicBook comicBook;
    private SeekBar pageBar;

    private Tooltip tooltip;
    private TextView hoverContentView;

    private DelayToolbarHideListener noHide;


    public PageBarHandler(
            DelayToolbarHideListener noHide,
            ComicBook comicBook,
            SeekBar pageBar,
            TextView hover
    ) {
        this.comicBook = comicBook;
        this.pageBar = pageBar;
        this.hoverContentView = hover;
        this.noHide = noHide;

        hidePageNum(false);

        this.pageBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //PageBarHandler.this.toggler.scheduleHideToolbar(TOOLBAR_HIDE_DELAY_MS);
                if(fromUser) {
                    showPageNum(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                showPageNum(seekBar.getProgress());
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                PageBarHandler.this.comicBook.setPage(seekBar.getProgress());
                PageBarHandler.this.comicBook.updateView(false);
                PageBarHandler.this.comicBook.relayoutCurrentZoneIfOnZoneAndUpdateView(false);
                hidePageNum(true);
            }
        });
    }

    private void showPageNum(int progress) {
        int page = progress;
        int numOfPages = this.comicBook.getNumOfPage();

        String txt = (page + 1) + " / " + numOfPages;

        this.hoverContentView.setText( txt );

        this.hoverContentView.setVisibility(View.VISIBLE);
        this.noHide.delayToolbarHide();
    }

    private void hidePageNum(boolean animate) {
        this.hoverContentView.setVisibility(View.GONE);
    }


    public void setProgress(int page) {
        this.pageBar.setProgress(page);
    }

    public void setMax(int i) {
        this.pageBar.setMax( i );
    }
}
