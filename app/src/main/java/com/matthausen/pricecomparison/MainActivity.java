package com.matthausen.pricecomparison;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
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


// - Create detail view layout:
    /*
    * Avg price for new
    * Avg price for used
    * Avg price on Ebay and Amazon
    * Min - Max price on Ebay
    * Min -Max price on Amazon
    *
    * */
// - Drop down menu with options for country
// - String replace space in search with '&' character
// - Filter for price
// - Filter for condition
// - Initial loading page
// - Remove current AppBar and create a better one with a logo

// - Add Amazon API
// - Add search filters
// - Store in environment variables sensitive data
// - Add AliBaba / Aliexpress API


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Main Activity";
    private Context context;
    private IProcess mprocess;

    String COUNTRY = "EBAY-GB";
    String APP_NAME = "MatteoFu-dashboar-PRD-61979435f-f81d2e44";
    String FORMAT = "&RESPONSE-DATA-FORMAT=JSON&keywords=";
    String GLOBAL_ID = "&X-EBAY-SOA-GLOBAL-ID=";
    String EBAY_API = "https://svcs.ebay.com/services/search/FindingService/v1?OPERATION-NAME=findItemsByKeywords&SERVICE-VERSION=1.0.0&SECURITY-APPNAME=";

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SearchView searchView = findViewById(R.id.item_search);

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
        context = this;

        ArrayList<Product> myDataset = new ArrayList<>();
        mprocess = new IProcess() {
            @Override
            public void updateAdapter(ArrayList<Product> result) {
                recyclerView.setAdapter(new ItemAdapter(context, result));
                mAdapter.notifyDataSetChanged();
            }
        };

        mAdapter = new ItemAdapter(this, myDataset);
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


                ArrayList<Product> items = new ArrayList<Product>();

                for (int i = 0; i < product.length(); i++) {
                    JSONObject object = product.getJSONObject(i);

                    String image = object.getJSONArray("galleryURL").getString(0);
                    String title = object.getJSONArray("title").getString(0);
                    String currency = object.getJSONArray("sellingStatus").getJSONObject(0).getJSONArray("currentPrice").getJSONObject(0).getString("@currencyId");
                    String price = object.getJSONArray("sellingStatus").getJSONObject(0).getJSONArray("currentPrice").getJSONObject(0).getString("__value__");
                    String condition = object.getJSONArray("condition").getJSONObject(0).getJSONArray("conditionDisplayName").getString(0);
                    String shipping = object.getJSONArray("shippingInfo").getJSONObject(0).getJSONArray("shippingServiceCost").getJSONObject(0).getString("__value__");
                    String itemURL = object.getJSONArray("viewItemURL").getString(0);

                    items.add(new Product(image, title, currency, price, condition, shipping, itemURL));
                }

                if (items.size() > 0 ) {
                    mprocess.updateAdapter(items);
                } else {
                    Log.d(TAG, "No product found");
                }

            } catch(JSONException e) {
                Log.d(TAG, e.toString());
                Toast.makeText(MainActivity.this, "Could not fetch a response from the API", Toast.LENGTH_LONG).show();
            }
        }
    }
}
