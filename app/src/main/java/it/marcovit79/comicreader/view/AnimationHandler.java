package it.marcovit79.comicreader.view;

import android.graphics.Matrix;
import android.os.Handler;
import android.widget.ImageView;

/**
 * Created by mvit on 3-8-16.
 */
public class AnimationHandler {

    private float maxTranslationStep = 30;
    private float maxScaleStep = 0.1f;
    private int framePeriodMs = 20;

    private ImageView img;
    private Handler h;

    public AnimationHandler(ImageView img) {
        this.img = img;
        this.h = new Handler();
    }


    public void animateZoomAndPan(Matrix toMatrix) {
        Matrix fromMatrix = img.getImageMatrix();

        ZoomAndPan from = matrixDecomposition(fromMatrix);
        ZoomAndPan to = matrixDecomposition(toMatrix);

        ZoomAndPan delta = to.minus( from );
        ZoomAndPan deltaAbs = delta.abs();

        int numOfSteps = Math.round(Math.max(
                Math.max(
                        deltaAbs.getPanX() / maxTranslationStep,
                        deltaAbs.getPanY() / maxTranslationStep
                ),
                deltaAbs.getZoom() / maxScaleStep
        ));


        h.removeCallbacksAndMessages(null);
        scheduleZoomPanStep(from, to, delta, 0, numOfSteps);
    }


    private void scheduleZoomPanStep(
            ZoomAndPan from,
            ZoomAndPan to,
            ZoomAndPan delta,
            int stepNum,
            int totalSteps
    ) {
        h.postDelayed(new AnimationStep(from, to, delta, stepNum, totalSteps), framePeriodMs);
    }

    private class AnimationStep implements Runnable {

        private ZoomAndPan from;
        private ZoomAndPan to;
        private ZoomAndPan delta;
        private int stepNum;
        private int totalSteps;

        private AnimationStep(
                ZoomAndPan from,
                ZoomAndPan to,
                ZoomAndPan delta,
                int stepNum,
                int totalSteps
        ) {
            this.from = from;
            this.to = to;
            this.delta = delta;
            this.stepNum = stepNum;
            this.totalSteps = totalSteps;
        }

        @Override
        public void run() {
            ZoomAndPan frame = delta.mult(stepNum).mult(1 / (float)totalSteps).sum( from );
            Matrix frameMatrix = frame.getMatrix();

            if( stepNum < totalSteps ) {
                AnimationHandler.this.img.setImageMatrix( frameMatrix );
                AnimationHandler.this.scheduleZoomPanStep(from, to, delta, stepNum + 1, totalSteps);
            }
            else {
                AnimationHandler.this.img.setImageMatrix( to.getMatrix() );
            }
        }
    }

    private static ZoomAndPan matrixDecomposition(Matrix m) {
        float[] matrix = new float[9];
        m.getValues(matrix);

        float zoom = matrix[0];
        float panX = matrix[2] / matrix[0];
        float panY = matrix[5] / matrix[4];

        return new ZoomAndPan(zoom, panX, panY);
    }


    private static class ZoomAndPan {
        private final float zoom;
        private final float panX;
        private final float panY;

        public ZoomAndPan(float zoom, float panX, float panY) {
            this.zoom = zoom;
            this.panX = panX;
            this.panY = panY;
        }

        public float getZoom() {
            return zoom;
        }

        public float getPanX() {
            return panX;
        }

        public float getPanY() {
            return panY;
        }

        public ZoomAndPan sum(ZoomAndPan b) {
            return new ZoomAndPan(
                    this.zoom + b.getZoom(),
                    this.panX + b.getPanX(),
                    this.panY + b.getPanY()
                );
        }

        public ZoomAndPan minus(ZoomAndPan b) {
            return new ZoomAndPan(
                    this.zoom - b.getZoom(),
                    this.panX - b.getPanX(),
                    this.panY - b.getPanY()
            );
        }

        public ZoomAndPan abs() {
            return new ZoomAndPan(
                    Math.abs( this.zoom ),
                    Math.abs( this.panX ),
                    Math.abs( this.panY )
                );
        }

        public ZoomAndPan mult( float f) {
            return new ZoomAndPan(
                    f * this.zoom,
                    f * this.panX,
                    f * this.panY
                );
        }

        public Matrix getMatrix() {
            Matrix m = new Matrix();
            m.setTranslate( panX, panY);
            m.postScale( zoom, zoom);
            return m;
        }
    }

}
