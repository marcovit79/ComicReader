package it.marcovit79.comicreader.view;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import it.marcovit79.comicreader.view.data.Zone;
import it.marcovit79.comicreader.vignetting.VignettingIssueCollector;

/**
 * Created by mvit on 30-7-16.
 */
public class ImageViewerHandler {

    private static final String LOG_TAG = "ImageViewHandler";
    private ImageView imgView;
    private TextView pageTxt;
    private ImageEventListener lsnr;
    private AnimationHandler animator;


    //private Handler h = new Handler();

    public ImageViewerHandler(ImageView imgView, TextView pageTxt) {
        this.pageTxt = pageTxt;
        this.imgView = imgView;
        this.imgView.setScaleType(ImageView.ScaleType.MATRIX);
        initImageListeners();

        this.animator = new AnimationHandler(this.imgView);
    }

    public void setPage(int page, int numPage, Bitmap img) {
        this.imgView.setImageBitmap(img);
        this.pageTxt.setText("Page " + (page + 1) + " of " + numPage );

        Zone allImage = new Zone(0, 0, img.getWidth(), img.getHeight());
        zoomZone(allImage , false);
    }

    public void zoomZone(Zone zone) {
        zoomZone(zone, true);
    }

    public void zoomZone(Zone zone, boolean animated) {
        //this.imgView.forceLayout();
        int viewWidth = this.imgView.getWidth();
        int viewHeight = this.imgView.getHeight();

        int width = zone.getWidth();
        int height = zone.getHeight();

        // - A bug of the view
        if(viewWidth == 0) {
            viewWidth = width;
        }
        if(viewHeight== 0) {
            viewHeight = height;
        }


        Log.d(LOG_TAG, "view("+ viewWidth + ", " + viewHeight + ")   " +
                                                        "  img(" + width + ", " + height + ")");

        final float ratio = Math.min( viewHeight / (float) height, viewWidth / (float) width);
        final int fromX = zone.getFromX();
        final int fromY = zone.getFromY();
        Log.d(LOG_TAG, "Ratio " + ratio + "  center(" + fromX + ", " + fromY + ")");


        Matrix newMatrix = new Matrix();
        newMatrix.setTranslate( -fromX, -fromY);
        newMatrix.postScale( ratio, ratio);

        if(animated) {
            this.animator.animateZoomAndPan(newMatrix);
        }
        else {
            this.imgView.setImageMatrix( newMatrix );
        }
    }

    private void initImageListeners() {
        lsnr = new ImageEventListener(this.imgView);
    }

    public void setViewportMovedLsnr(UserViewportMovedListener viewportMovedLsnr) {
        lsnr.setViewportMovedLsnr(viewportMovedLsnr);
    }

    public void setVignettingDebugger(VignettingIssueCollector vignettingDebugger) {
        lsnr.setVignettingDebugger(vignettingDebugger);
    }
}
