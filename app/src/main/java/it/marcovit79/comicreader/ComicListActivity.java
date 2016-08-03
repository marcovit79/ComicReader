package it.marcovit79.comicreader;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import it.marcovit79.comicreader.download.ChooseBook;
import it.marcovit79.comicreader.download.CopyFileUtil;
import it.marcovit79.comicreader.view.ViewComicActivity;

public class ComicListActivity extends AppCompatActivity {

    private final BroadcastReceiver newComicReceiver;

    public ComicListActivity() {
        newComicReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                refreshFileList();
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_comic_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent intent = new Intent(ComicListActivity.this, ChooseBook.class);
            startActivity(intent);
            }
        });

        ListView listView = (ListView) findViewById(R.id.comic_list_view);
        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1);
        listView.setAdapter( adapter );
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String fileName = (String) adapter.getItem(position);

                File dir = ComicListActivity.this.getDir("comics", Context.MODE_PRIVATE);
                File f = new File(dir, fileName);

                System.out.println("OPEN " + f);

                Intent intent = new Intent(ComicListActivity.this, ViewComicActivity.class);
                intent.putExtra(ViewComicActivity.COMIC_PATH, f.getAbsolutePath());
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String fileName = (String) adapter.getItem(position);

                File dir = ComicListActivity.this.getDir("comics", Context.MODE_PRIVATE);
                File f = new File(dir, fileName);

                System.out.println("DELETE " + f);
                f.delete();

                ComicListActivity.this.refreshFileList();
                return true;
            }
        });

        refreshFileList();
    }


    @Override
    protected void onStart() {
        super.onStart();

        IntentFilter newComic = new IntentFilter();
        newComic.addAction(CopyFileUtil.NEW_COMIC_ACTION );

        refreshFileList();
        registerReceiver(newComicReceiver, newComic);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(newComicReceiver);
    }

    private void refreshFileList() {
        ListView listView = (ListView) findViewById(R.id.comic_list_view);
        ArrayAdapter adapter = (ArrayAdapter) listView.getAdapter();

        File dir = this.getDir("comics", Context.MODE_PRIVATE);
        System.out.println("Refreshing file list " + dir);

        ArrayList<String> names = new ArrayList<>();
        for(File f : dir.listFiles() ) {
            names.add(f.getName());
        };
        Collections.sort(names);

        adapter.clear();
        for(String name: names ) {
            adapter.add(name);
            System.out.println(name);
        };
    }

}