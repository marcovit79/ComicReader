package it.marcovit79.comicreader.view.data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by mvit on 30-7-16.
 */
public class ComicsData {

    private List<Page> pages = new ArrayList<>();

    public List<Page> getPages() {
        return pages;
    }

    @Override
    public String toString() {
        return "ComicsData{" +
                "pages=" + pages +
                '}';
    }

    public Zone getZone(int page, int vignette) {
        Zone z = this.pages.get(page);
        if(vignette >= 0) {
            z = z.getChildren().get(vignette);
        }
        return z;
    }

    public int getNumVignetteInPage(int page) {
        int numVignettes = this.getPages().get(page).getChildren().size();
        return numVignettes;
    }
}
