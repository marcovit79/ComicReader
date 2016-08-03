package it.marcovit79.comicreader.download;

import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mvit on 27-7-16.
 */
public class ReadUrlTask extends AsyncTask<String, Integer, List<String>> {

    private ArrayAdapter<String> results;

    public ReadUrlTask(ArrayAdapter<String> results) {
        this.results = results;
    }

    @Override
    protected List<String> doInBackground(String... params) {
        StringBuilder str = new StringBuilder();
        System.out.println("Get content of ...");

        for(String param: params) {
            try(InputStream inStrm = new URL(param).openStream() ) {

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inStrm, "UTF-8"));
                String line;
                while( (line = bufferedReader.readLine()) != null){
                    str.append(line).append('\n');
                }
            } catch (IOException exc) {
                exc.printStackTrace();
                str.append(exc.getMessage());
            }
        }
        System.out.println(str.toString());

        List<String> books = new ArrayList<String>();
        for(String line : str.toString().split("\n")) {
            String hrefTo = line.replaceFirst(".*href=\"","").replaceFirst("\".*","");
            if(hrefTo.endsWith(".cbz") || hrefTo.endsWith(".cbr")) {
                books.add(hrefTo);
            }
        }
        return books;
    }


    @Override
    protected void onPostExecute(List<String> s) {
        this.results.clear();
        this.results.addAll(s);
    }
}
