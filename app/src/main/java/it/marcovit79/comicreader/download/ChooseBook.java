package it.marcovit79.comicreader.download;

import android.app.DownloadManager;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import it.marcovit79.comicreader.R;
import it.marcovit79.comicreader.download.DownloadEndedBrodcastReceiver;
import it.marcovit79.comicreader.download.ReadUrlTask;

public class ChooseBook extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_book);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final EditText urlField = (EditText)findViewById(R.id.url_field);

        ListView listView = (ListView) findViewById(R.id.comics_list_view);
        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1);
        listView.setAdapter( adapter );

        final Button button = (Button) findViewById(R.id.search_btn);
        assert button != null;
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("Search CLICK");
                String urlString = urlField.getText().toString();
                new ReadUrlTask(adapter).execute(urlString);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String urlString = urlField.getText().toString();
                Object item = adapter.getItem(position);
                String downloadUrl = urlString + "/" + String.valueOf(item);

                System.out.println(downloadUrl);
                mkRequest(downloadUrl);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_choose_book, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }



    public void mkRequest(String urlString) {
        DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(urlString));
        long reqId = dm.enqueue(request);
        System.out.println("Download Request Id " + reqId);
    }
}
