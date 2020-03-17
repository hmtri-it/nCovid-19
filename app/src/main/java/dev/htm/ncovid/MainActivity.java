package dev.htm.ncovid;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import dev.htm.ncovid.adapter.NCVidCasesAdapter;
import dev.htm.ncovid.model.NCovidLiveData;
import dev.htm.ncovid.model.TotalCase;
import dev.htm.ncovid.service.FetchAsyncData;
import dev.htm.ncovid.service.OnCallback;
import dev.htm.ncovid.util.GetDataUtil;

public class MainActivity extends AppCompatActivity implements OnCallback, NCVidCasesAdapter.onListener {


    private TextView tv_today, tv_totConfirmedCases, tv_totalDeaths, tv_totalRecovered, version;

    private RecyclerView recyclerView;
    private List<NCovidLiveData> casesList;
    private NCVidCasesAdapter mAdapter;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        prepareDataNCoVId();
    }

    private void initView() {
        tv_today = findViewById(R.id.today);
        tv_totConfirmedCases = findViewById(R.id.tot_cnfm_cases);
        tv_totalDeaths = findViewById(R.id.tot_deaths);
        tv_totalRecovered = findViewById(R.id.cases_recovered);
        recyclerView = findViewById(R.id.recyclerCase);
        version = findViewById(R.id.version);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle(R.string.action_search);

        version.setText(getString(R.string.app_name) + " v" + BuildConfig.VERSION_NAME);


    }

    private void prepareDataNCoVId() {
        GetDataUtil.totalCase(this, FetchAsyncData.ALL, this, FetchAsyncData.ALL);
        GetDataUtil.totalCase(this, FetchAsyncData.COUNTRIES, this, FetchAsyncData.COUNTRIES);
    }

    @Override
    public void onSuccess(String type, String data) {
        Gson gson = new GsonBuilder().create();
        switch (type) {
            case FetchAsyncData.ALL:
                TotalCase totalCase = gson.fromJson(data, TotalCase.class);
                DateTime dt = new DateTime(totalCase.getUpdated());
                DateTimeFormatter fmt = DateTimeFormat.forPattern("dd/MM/yyyy");
                tv_today.setText("• Today " + fmt.print(dt));
                tv_totConfirmedCases.setText(String.valueOf(totalCase.getCases()));
                tv_totalDeaths.setText(String.valueOf(totalCase.getDeaths()));
                tv_totalRecovered.setText(String.valueOf(totalCase.getRecovered()));
                break;
            case FetchAsyncData.COUNTRIES:
                Type collectionType = new TypeToken<Collection<NCovidLiveData>>() {
                }.getType();
                Collection<NCovidLiveData> nCovidLiveDatas = gson.fromJson(data, collectionType);
                casesList = new ArrayList<>();
                casesList.addAll(nCovidLiveDatas);
                mAdapter = new NCVidCasesAdapter(this, casesList, this);

                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(mAdapter);
                break;
        }

    }

    @Override
    public void onError(String error) {

    }

    @Override
    public void onItemSelected(NCovidLiveData cases) {

    }

    @Override
    public void onBackPressed() {
        // close search view on back button pressed
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                mAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                mAdapter.getFilter().filter(query);
                return false;
            }
        });
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

        return super.onOptionsItemSelected(item);
    }

}
