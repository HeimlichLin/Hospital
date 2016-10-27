package com.example.user_pc.hospital;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

public class BunqiaoActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener {
    ProgressDialog mDialog;
    ListView mListView,sListView;
    BunqiaoAdapter mAdapter;
    SwipeRefreshLayout mSwipeLayout;
    ArrayList<String> mSearchList = new ArrayList<>();
    boolean mIsSearch = false;
    ArrayAdapter mSearchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorSchemeColors(Color.BLUE); //重整的圖示用藍色

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadData();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy 年 MM 月 dd 日 HH:mm:ss");

                Date curDate = new Date(System.currentTimeMillis()) ; // 獲取當前時間

                String str = formatter.format(curDate);
                Snackbar.make(view, "資料更新時間：" +str, Snackbar.LENGTH_LONG)
                        .setAction(str, null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mDialog = new ProgressDialog(this);
        mDialog.setMessage("Loading Data...");

        mListView = (ListView) findViewById(R.id.main_listview);
        mAdapter = new BunqiaoAdapter(this);
        mListView.setAdapter(mAdapter);

        loadData();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        MenuItem menuSearchItem = menu.findItem(R.id.action_search);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menuSearchItem.getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        // 這邊讓icon可以還原到搜尋的icon
        searchView.setIconifiedByDefault(true);

        searchView.setOnQueryTextListener(this);
        sListView = (ListView) findViewById(R.id.main_listview);
        mSearchAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mSearchList);
        searchData();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_introduction) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_paste_Hospital) {
            Intent HospitalIntent = new Intent(this, HospitalActivity.class);
            HospitalIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(HospitalIntent);
            overridePendingTransition(0, 0);

        } else if (id == R.id.nav_paste_Sanchong) {
            Intent SanchongIntent = new Intent(this, SanchongActivity.class);
            SanchongIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(SanchongIntent);
            overridePendingTransition(0, 0);

        } else if (id == R.id.nav_paste_Bunqiao) {
            loadData();

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void loadData() {
        mDialog.show();
        mListView = (ListView) findViewById(R.id.main_listview);
        mAdapter = new BunqiaoAdapter(this);
        mListView.setAdapter(mAdapter);
        String urlString = "http://data.ntpc.gov.tw/api/v1/rest/datastore/382000000A-000000-002";

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(urlString, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                mDialog.dismiss();
                Toast.makeText(getApplicationContext(),
                        "Success!", Toast.LENGTH_SHORT).show();
                Log.d("response Text:", response.toString());

                if (response.has("err") && response.optInt("err") != 0) {
                    Toast.makeText(getApplicationContext(), "Data error", Toast.LENGTH_SHORT).show();
                }

                JSONObject result = response.optJSONObject("result");
                Log.d("result Text:", result.toString());
                JSONArray records = result.optJSONArray("records");
                Log.d("records Text:", records.toString());

                mAdapter.updateData(records);
                mSwipeLayout.setRefreshing(false); //結束更新動畫
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject error) {
                mDialog.dismiss();
                Toast.makeText(getApplicationContext(),
                        "Error: " + statusCode + " " + e.getMessage(),
                        Toast.LENGTH_LONG).show();
                // Log error message
                Log.e("Hot Text:", statusCode + " " + e.getMessage());
                mSwipeLayout.setRefreshing(false); //結束更新動畫
            }
        });
    }

    private void searchData(){
        String urlString = "http://data.ntpc.gov.tw/api/v1/rest/datastore/382000000A-000000-002";

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(urlString, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                if( response.has("err") && response.optInt("err")!=0 ){
                    Toast.makeText(getApplicationContext(),"Data error", Toast.LENGTH_SHORT).show();
                }

                JSONObject result = response.optJSONObject("result");
                JSONArray records = result.optJSONArray("records");
                JSONObject board;
                for(int i=0; i<records.length(); i++){
                    board = records.optJSONObject(i);
                    mSearchList.add(board.optString("publisher") + " " + "板橋院區 候診資訊" + "\n"
                            + board.optString("title"));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject error) {
                Toast.makeText(getApplicationContext(),
                        "Error: " + statusCode + " " + e.getMessage(),
                        Toast.LENGTH_LONG).show();
                // Log error message
                Log.e("response Text:", statusCode + " " + e.getMessage());
            }
        });
    }



    @Override
    public void onRefresh() {
        loadData();
    }
    @Override
    public boolean onQueryTextChange(String newText) {
        if(!mIsSearch && newText.length()!=0) { //搜尋框有值時
            sListView.setAdapter(mSearchAdapter);
            mIsSearch = true;
        }else if(mIsSearch && newText.length()==0){ //搜尋框是空的時
            sListView.setAdapter(null);
            mIsSearch = false;
            loadData();
        }
        if(mIsSearch) { //過濾Adapter的內容
            Filter filter = mSearchAdapter.getFilter();
            filter.filter(newText);
        }
        return true;
    }
    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

}


