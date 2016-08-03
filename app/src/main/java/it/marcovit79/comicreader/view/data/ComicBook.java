package it.marcovit79.comicreader.view.data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import it.marcovit79.comicreader.view.ImageViewerHandler;
import it.marcovit79.comicreader.view.UserViewportMovedListener;

/**
 * Created by mvit on 30-7-16.
 */
public class ComicBook implements UserViewportMovedListener {

    private ImageViewerHandler view;

    private ZipFile comicFile;
    private ComicsData data;


    private int page = -1;
    private boolean pageDirty;

    private int vignette = -1;
    private boolean vignetteDirty;
    private boolean viewportModified;


    public ComicBook(ImageViewerHandler view) {
        this.viewportModified = false;
        this.comicFile = null;
        this.view = view;
        this.view.setViewportMovedLsnr(this);
    }


    public void setComicBook(File f) throws IOException {

        this.comicFile = new ZipFile(f);
        this.data = ComicsDataFactory.buildFromZip(comicFile);


        setPage(-1);
        setPage(0);
    }

    public void updateView() {
        if(pageDirty) {
            System.out.println("Update page");

            Bitmap img = this.getImage(page);
            this.view.setImage(img);
            pageDirty = false;
        }

        if(vignetteDirty) {
            System.out.println("Update vignette");
            Zone zone = this.data.getZone(page, vignette);
            this.view.zoomZone(zone);
            vignetteDirty = false;
        }

    }

    public void setPage(int page) {
        if(this.page != page) {
            this.page = page;
            this.pageDirty = true;

            this.vignette = -1;
            this.vignetteDirty = true;
        }
    }

    public void setVignette(int vignette) {
        if(this.vignette != vignette) {
            this.vignette = vignette;
            this.vignetteDirty = true;
        }
    }

    public int getNumOfPage() {
        return this.data.getPages().size();
    }

    public Bitmap getImage(int page) {
        Bitmap img;
        if(page >= 0 && page < this.getNumOfPage() ) {
            try {
                Page p = this.data.getPages().get(page);
                ZipEntry imgEntry = comicFile.getEntry(p.getImage());

                InputStream imgInStrm = comicFile.getInputStream( imgEntry );
                img = BitmapFactory.decodeStream(  imgInStrm );
            } catch (IOException e) {
                e.printStackTrace();
                img = null;
            }
        }
        else {
            img = null;
        }
        return img;
    }

    public void nextVignette() {
        if(viewportModified) {
            this.vignetteDirty = true;
            viewportModified = false;
        }
        else {
            realNextVignette();
        }
    }

    public void prevVignette() {
        if(viewportModified) {
            this.vignetteDirty = true;
            viewportModified = false;
        }
        else {
            realPrevVignette();
        }
    }

    protected void realNextVignette() {

        int vignettes_in_this_page = this.data.getNumVignetteInPage(page);
        if( vignette + 1 < vignettes_in_this_page ) {
            setVignette( getVignette() + 1);
        }
        else if( this.page + 1 < this.getNumOfPage() ) {
            this.setPage( this.getPage() + 1);
        }
    }

    protected void realPrevVignette() {
        if( vignette > -1) {
            setVignette( getVignette() -1 );
        }
        else {

            if(getPage() > 0) {
                setPage( getPage() -1 );

                int vignettes_in_prev_page = this.data.getNumVignetteInPage( getPage());
                setVignette( vignettes_in_prev_page -1);
            }
        }
    }


    public int getPage() {
        return page;
    }

    public int getVignette() {
        return vignette;
    }

    @Override
    public void viewportModified() {
        this.viewportModified = true;
    }

    public void relayoutCurrentZoneIfOnZoneAndUpdateView() {
        System.out.println("Relayout");
        if(!viewportModified) {
            this.pageDirty = true;
            this.vignetteDirty = true;
            this.updateView();
        }
    }
}
