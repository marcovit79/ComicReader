package it.marcovit79.comicreader.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.ScaleGestureDetector;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import it.marcovit79.comicreader.view.data.Zone;

/**
 * Created by mvit on 30-7-16.
 */
public class ImageViewerHandler {

    private ImageView imgView;
    private ImageEventListener lsnr;
    private AnimationHandler animator;


    //private Handler h = new Handler();

    public ImageViewerHandler(ImageView imgView) {
        this.imgView = imgView;
        this.imgView.setScaleType(ImageView.ScaleType.MATRIX);
        initImageListeners();

        this.animator = new AnimationHandler(this.imgView);
    }

    public void setImage(Bitmap img) {
        this.imgView.setImageBitmap(img);
        Zone allImage = new Zone(0, 0, img.getWidth(), img.getHeight());
        zoomZone(allImage , false);
    }

    public void zoomZone(Zone zone) {
        zoomZone(zone, true);
    }

    private void zoomZone(Zone zone, boolean animated) {
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


        //System.out.println("view("+ viewWidth + ", " + viewHeight + ")   " +
        //        "  img(" + width + ", " + height + ")");

        final float ratio = Math.min( viewHeight / (float) height, viewWidth / (float) width);
        final int fromX = zone.getFromX();
        final int fromY = zone.getFromY();
        System.out.println("Ratio " + ratio + "  center(" + fromX + ", " + fromY + ")");


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

}
