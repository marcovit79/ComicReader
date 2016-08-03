package it.marcovit79.comicreader.view.data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.JsonReader;
import android.util.JsonToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by mvit on 30-7-16.
 */
public class ComicsDataFactory {

    public static ComicsData buildFromZip(ZipFile comicFile) {
        ComicsData data;

        List<ZipEntry> zipPages = new ArrayList<>();
        InputStream jsonStream = null;

        Enumeration<ZipEntry> zipEntryEnumeration = (Enumeration<ZipEntry>) comicFile.entries();
        while(zipEntryEnumeration.hasMoreElements() && jsonStream == null) {
            ZipEntry entry = zipEntryEnumeration.nextElement();
            if(entry.getName().matches(".*\\.(jpg|jpeg|png)")) {
                zipPages.add(entry);
            }
            else if(entry.getName().matches(".*read_order.json")) {
                try {
                    jsonStream = comicFile.getInputStream(entry);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if(jsonStream != null) {
            try {
                data = parseJson(comicFile, jsonStream);
            } catch (IOException e) {
                e.printStackTrace();
                data = mkDefaultData(comicFile, zipPages);
            }
        }
        else {
            data = mkDefaultData(comicFile, zipPages);
        }

        System.out.println("COMIC DATA: " + data);
        return data;
    }


    public static ComicsData parseJson(ZipFile comicFile, InputStream inStrm) throws IOException {
        ComicsData data = new ComicsData();
        List<String> entryNames = new ArrayList<>();
        Enumeration<ZipEntry> zipEntryEnumeration = (Enumeration<ZipEntry>) comicFile.entries();
        while(zipEntryEnumeration.hasMoreElements()) {
            entryNames.add(zipEntryEnumeration.nextElement().getName());
        }


        try(JsonReader jsonReader = new JsonReader( new InputStreamReader(inStrm) )) {

            // { "comicbook" : { "pages" : [
            jsonReader.beginObject(); // {
            jsonReader.nextName();
            jsonReader.beginObject();
            jsonReader.nextName();
            jsonReader.beginArray();

            Page page;
            while( (page = parseJsonPage(jsonReader)) != null) {
                data.getPages().add(page);
            };

            // ] } }
            jsonReader.endArray();
            jsonReader.endObject();
            jsonReader.endObject();
        };

        // - replace image name with image position inside zip
        for(Page p: data.getPages()) {
            String fullImagePath = null;
            for(String entryName: entryNames ) {
                if(entryName.endsWith( p.getImage())) {
                    fullImagePath = entryName;
                }
            }
            if(fullImagePath != null) {
                p.setImage(fullImagePath);
            }
            else {
                throw new RuntimeException("Image not found " + p.getImage());
            }
        }

        return data;

    }

    private static Page parseJsonPage(JsonReader jsonReader) throws IOException {
        Page page = null;

        if(JsonToken.BEGIN_OBJECT.equals( jsonReader.peek() )) {
            jsonReader.beginObject();

            // "img" : "005.jpg",
            jsonReader.nextName();
            String img = jsonReader.nextString();

            Zone z = parseJsonZoneData(jsonReader);
            page = new Page(z.getToX(), z.getToY(), img);

            // "vignettes": [
            jsonReader.nextName();
            jsonReader.beginArray();

                while(JsonToken.BEGIN_OBJECT.equals( jsonReader.peek() )) {
                    jsonReader.beginObject();
                    Zone vignette = parseJsonZoneData(jsonReader);
                    page.getChildren().add(vignette);
                    jsonReader.endObject();
                }

            // ] }
            jsonReader.endArray();
            jsonReader.endObject();
        }

        return page;
    }

    private static Zone parseJsonZoneData(JsonReader jsonReader) throws IOException {
        // "from" : { "x" : 0, "y" : 0 },  "to" : { "x" : 1161, "y" : 1606 }
        jsonReader.nextName();
        jsonReader.beginObject();
        jsonReader.nextName();
        int fromX = jsonReader.nextInt();
        jsonReader.nextName();
        int fromY = jsonReader.nextInt();
        jsonReader.endObject();

        jsonReader.nextName();
        jsonReader.beginObject();
        jsonReader.nextName();
        int toX = jsonReader.nextInt();
        jsonReader.nextName();
        int toY = jsonReader.nextInt();
        jsonReader.endObject();

        return new Zone(fromX, fromY, toX, toY);
    }


    public static ComicsData mkDefaultData(ZipFile comicFile, List<ZipEntry> pages) {

        ComicsData data = new ComicsData();

        for(ZipEntry zipPage : pages) {
            try {
                InputStream inStrm = comicFile.getInputStream( zipPage );
                Bitmap pageImg = BitmapFactory.decodeStream( inStrm );
                int width = pageImg.getWidth();
                int height = pageImg.getHeight();

                data.getPages().add(new Page(width, height, zipPage.getName()));
            } catch (IOException exc) {
                exc.printStackTrace();
            }
        }

        return data;
    }

}
