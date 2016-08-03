package it.marcovit79.comicreader.view;

import android.graphics.Matrix;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by mvit on 1-8-16.
 */
public class ImageEventListener implements ScaleGestureDetector.OnScaleGestureListener, GestureDetector.OnGestureListener, View.OnTouchListener {

    private ImageView imgView;
    private ScaleGestureDetector scaleDetector;
    private GestureDetector gestureDetector;

    private UserViewportMovedListener viewportMovedLsnr;

    public ImageEventListener(ImageView imgView) {
        this.imgView = imgView;

        this.scaleDetector = new ScaleGestureDetector(this.imgView.getContext(), this);
        this.gestureDetector = new GestureDetector(this.imgView.getContext(), this);

        this.imgView.setOnTouchListener(this);
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        //System.out.println("SCALE BEGIN");
        return true;
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {

        float x = detector.getFocusX();
        float y = detector.getFocusY();
        float ratio = detector.getScaleFactor();

        //System.out.println("SCALE " + ratio );

        Matrix m = this.imgView.getImageMatrix();
        m.postScale( ratio, ratio, x, y);
        this.imgView.setImageMatrix(m);
        this.imgView.invalidate();

        fireViewportMove();
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
        //System.out.println("SCALE END");
    }

    @Override
    public boolean onDown(MotionEvent e) {
        //System.out.println("GESTURE DOWN");
        return true;
        }

    @Override
    public void onShowPress(MotionEvent e) {
        //System.out.println("GESTURE SHOW PRESS");
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        this.imgView.performClick();
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        //System.out.println("SCROLL " + distanceX + " " + distanceY);

        Matrix m = this.imgView.getImageMatrix();
        m.postTranslate(-distanceX, -distanceY);
        this.imgView.setImageMatrix(m);
        this.imgView.invalidate();

        fireViewportMove();
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {

        this.scaleDetector.onTouchEvent(event);
        if(!this.scaleDetector.isInProgress()) {
            this.gestureDetector.onTouchEvent(event);
        }
        return true;
    }

    protected void fireViewportMove() {
        UserViewportMovedListener lsnr = getViewportMovedLsnr();
        if(lsnr != null) {
            lsnr.viewportModified();
        }
    }

    public UserViewportMovedListener getViewportMovedLsnr() {
        return viewportMovedLsnr;
    }

    public void setViewportMovedLsnr(UserViewportMovedListener viewportMovedLsnr) {
        this.viewportMovedLsnr = viewportMovedLsnr;
    }
}
