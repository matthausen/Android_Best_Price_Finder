package com.matthausen.pricecomparison;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;
import android.widget.Toast;
import com.androdocs.httprequest.HttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.MessageFormat;
import java.util.ArrayList;

// - myDataset must be an array of Product
// - Start new activity on recycler view item click
// - Create searchview layout
// - Create recyclerview layout
// - Create detail view layout
// - Add Amazon API
// - Add search filters
// - Store in environment variables sensitive data


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Main Activity";

    String COUNTRY = "EBAY-GB";
    String APP_NAME = "MatteoFu-dashboar-PRD-61979435f-f81d2e44";
    String FORMAT = "&RESPONSE-DATA-FORMAT=JSON&keywords=";
    String GLOBAL_ID = "&X-EBAY-SOA-GLOBAL-ID=";
    String EBAY_API = "https://svcs.ebay.com/services/search/FindingService/v1?OPERATION-NAME=findItemsByKeywords&SERVICE-VERSION=1.0.0&SECURITY-APPNAME=";


    String[] myDataset = {"item1", "item2", "item3"};

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SearchView searchView = findViewById(R.id.item_search);
        final CharSequence query = searchView.getQuery();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                fetchItems(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new ItemAdapter(myDataset);
        recyclerView.setAdapter(mAdapter);
    }

    public void fetchItems(String q) {
        new EbayTask(q).execute();
    }

    class EbayTask extends AsyncTask<String, Void, String> {

        String SEARCH;

        public EbayTask(String q){
            super();
            SEARCH = q;
        }

        @Override
        protected void onPreExecute() {
        }


        @Override
        protected String doInBackground(String... strings) {
            String response = HttpRequest.excuteGet(MessageFormat.format(
                    "https://svcs.ebay.com/services/search/FindingService/v1?OPERATION-NAME=findItemsByKeywords&SERVICE-VERSION=1.0.0&SECURITY-APPNAME={0}{1}{2}{3}{4}",
                    APP_NAME, FORMAT, SEARCH, GLOBAL_ID, COUNTRY));
            return response;
        }

        @Override
        protected void onPostExecute(String result) {

            try {
                JSONObject jsonObj = new JSONObject(result);

                JSONArray product = jsonObj.getJSONArray("findItemsByKeywordsResponse")
                        .getJSONObject(0).getJSONArray("searchResult")
                        .getJSONObject(0).getJSONArray("item");

                // Product[] items = new Product[product.length()];

                ArrayList<Product> items = new ArrayList<Product>();

                for (int i = 0; i < product.length(); i++) {
                    JSONObject object = product.getJSONObject(i);

                    String image = object.getJSONArray("galleryURL").getString(0);
                    String title = object.getJSONArray("title").getString(0);
                    String price = object.getJSONArray("sellingStatus").getJSONObject(0).getJSONArray("currentPrice").getJSONObject(0).getString("__value__");
                    String condition = object.getJSONArray("condition").getJSONObject(0).getJSONArray("conditionDisplayName").getString(0);
                    String shipping = object.getJSONArray("shippingInfo").getJSONObject(0).getJSONArray("shippingServiceCost").getJSONObject(0).getString("__value__");
                    String itemURL = object.getJSONArray("viewItemURL").getString(0);

                    items.add(new Product(image, title, price, condition, shipping, itemURL));
                    System.out.println(items);
                }

            } catch(JSONException e) {
                Log.d(TAG, e.toString());
                Toast.makeText(MainActivity.this, "Could not fetch a response from the API", Toast.LENGTH_LONG).show();
            }
        }
    }
}
