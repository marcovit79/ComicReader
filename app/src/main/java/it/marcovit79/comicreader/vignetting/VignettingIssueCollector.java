package it.marcovit79.comicreader.vignetting;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import it.marcovit79.comicreader.download.CopyFileUtil;
import it.marcovit79.comicreader.view.data.ComicBook;

/**
 * Created by mvit on 5-8-16.
 */
public class VignettingIssueCollector {

    public static final String LOG_TAG = "VignettingIssue";
    public static final String ISSUES_DIR_NAME = "issue_files";
    public static final String VIGNETING_ISSUES_FILE_NAME = "vigneting_issues.txt";
    public static final String SUPPORT_EMAIL = "marcovit79@gmail.com";
    private Context ctx;
    private ComicBook comicBook;

    public VignettingIssueCollector(Context ctx, ComicBook comicBook) {
        this.ctx = ctx;
        this.comicBook = comicBook;
    }

    public void addCurrentImage() {
        int page = comicBook.getPage();
        String comicFilePath = comicBook.getComicFilePath();

        if(comicBook.getNumOfPage() > page && comicFilePath!=null) {
            String imagePath = comicBook.getImageName(page);
            String issueLine = "\"" + comicFilePath + "\" __SEP__ \"" + imagePath +"\"";
            writeVignettingIssue( issueLine );

            Toast.makeText(ctx, "Page added to vignetting review list", Toast.LENGTH_SHORT).show();
        }
    }

    public static  File getVignetingIssuesFile(Context ctx) {
        File issueDir = ctx.getDir(ISSUES_DIR_NAME, Context.MODE_PRIVATE);
        File issueFile = new File(issueDir, VIGNETING_ISSUES_FILE_NAME);
        return issueFile;
    }

    private void writeVignettingIssue(String issueLine) {
        File issueDir = ctx.getDir(ISSUES_DIR_NAME, Context.MODE_PRIVATE);

        Log.i(LOG_TAG, issueLine);
        try (Writer writer = new FileWriter(getVignetingIssuesFile(ctx), true)) {
            writer.append(issueLine).append('\n');
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }

}
