package com.example.networking;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayAdapter<String> adapter;
    private ArrayList<String>mountainName=new ArrayList<String>();
    private ArrayList<String>mountainLocation=new ArrayList<String>();
    private ArrayList<Integer>mountainHeight=new ArrayList<Integer>();
    private ArrayList<Mountain> mountainArrayList=new ArrayList<>();
    TextView textView;
    String s = new String("{\"ID\":" +
            "\"mobilprog_k2\",\"name\":\"K2\"," +
            "\"type\":\"a19emigr\",\"company\":\"\"," +
            "\"location\":\"The Karakoram range\"," +
            "\"category\":\"\",\"size\":8611,\"cost\":28251," +
            "\"auxdata\":{\"wiki\":\"https://en.wikipedia.org/wiki/K2\"," +
            "\"img\":\"https://upload.wikimedia.org/wikipedia/commons/thumb/1/12/K2_2006b.jpg/640px-K2_2006b.jpg\"}}," +
            "\" +\"{\"ID\":\"mobilprog_kilimanjaro\",\"name\":\"Kilimanjaro\",\"type\":\"a19emigr\"," +
            "\"company\":\"\",\"location\":\"Tanzania\",\"category\":\"\",\"size\":5885," +
            "\"cost\":19308,\"auxdata\":{\"wiki\":\"https://en.wikipedia.org/wiki/Mount_Kilimanjaro\"," +
            "\"img\":\"https://upload.wikimedia.org/wikipedia/commons/thumb/9/91/Mount_Kilimanjaro.jpg/640px-Mount_Kilimanjaro.jpg\"}}," +
            "{\"ID\":\"mobilprog_matterhorn\",\"name\":\"Matterhorn\",\"type\":\"a19emigr\"," +
            "\"company\":\"\",\"location\":\"The Alps\",\"category\":\"\",\"size\":4478," +
            "\"cost\":14692,\"auxdata\":{\"wiki\":\"https://en.wikipedia.org/wiki/Matterhorn\"," +
            "\"img\":\"https://upload.wikimedia.org/wikipedia/commons/thumb/6/60/Matterhorn_from_Domhütte_-_2.jpg/640px-Matterhorn_from_Domhütte_-_2.jpg\"}}," +
            "{\"ID\":\"mobilprog_mount_everest\",\"name\":\"Mount Everest\",\"type\":\"a19emigr\"," +
            "\"company\":\"\",\"location\":\"The Mahalangur Himal sub-range of the Himalayas\"," +
            "\"category\":\"\",\"size\":8884,\"cost\":29029,\"auxdata\":{\"wiki\":\"https://en.wikipedia.org/wiki/Mount_Everest\"," +
            "\"img\":\"https://upload.wikimedia.org/wikipedia/commons/thumb/f/f6/Everest_kalapatthar.jpg/512px-Everest_kalapatthar.jpg\"}}");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapter=new ArrayAdapter<String>(MainActivity.this,R.layout.list_item_textview,R.id.list_item_textview_xml,mountainName);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id){
                Toast.makeText(getApplicationContext(),mountainName.get(i) + " ligger i " + mountainLocation.get(i) + " och är " + mountainHeight.get(i) + " hög.", Toast.LENGTH_LONG)
                        .show();
            }
        });
        textView=(TextView)findViewById(R.id.textView);
        new JsonTask().execute("https://wwwlab.iit.his.se/brom/kurser/mobilprog/dbservice/admin/getdataasjson.php?type=brom");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id=item.getItemId();

        if (id==R.id.action_settings){
            return true;
        }
        if (id==R.id.action_refresh){
            new JsonTask().execute();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @SuppressLint("StaticFieldLeak")
    private class JsonTask extends AsyncTask<String, String, String> {

        private HttpURLConnection connection = null;
        private BufferedReader reader = null;

        protected String doInBackground(String... params) {
            try {
                URL url = new URL("https://wwwlab.iit.his.se/brom/kurser/mobilprog/dbservice/admin/getdataasjson.php?type=brom");
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null && !isCancelled()) {
                    builder.append(line).append("\n");
                }
                return builder.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            return null;
        }

        @Override
        protected void onPostExecute(String json) {
            try {
                JSONArray jsonAr = new JSONArray(json);
                for (int i = 0; i<jsonAr.length(); i++){
                    JSONObject jsonObj = jsonAr.getJSONObject(i);
                    String name = jsonObj.getString("name");
                    String location = jsonObj.getString("location");
                    int height = jsonObj.getInt("size");
                    mountainName.add(name);
                    mountainLocation.add(location);
                    mountainHeight.add(height);
                }
                adapter.notifyDataSetChanged();

            }
            catch (JSONException e){
                Log.d("a19emigr",e.getLocalizedMessage());
            }


        }

    }
}
