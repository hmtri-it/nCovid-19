package dev.htm.ncovid;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
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
    private SearchView searchView;
    private PieChart pieChart;

    private List<NCovidLiveData> nCovidLiveDataList;
    private NCVidCasesAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        prepareDataNCoVId();
        loadCountriesDataNCoVid();
    }

    private void initView() {
        tv_today = findViewById(R.id.today);
        tv_totConfirmedCases = findViewById(R.id.tot_cnfm_cases);
        tv_totalDeaths = findViewById(R.id.tot_deaths);
        tv_totalRecovered = findViewById(R.id.cases_recovered);
        pieChart = findViewById(R.id.reportPieChart);
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
    }

    private void loadCountriesDataNCoVid() {
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
                tv_today.setText("• Today " + fmt.print(dt) + " " + "of Worldwide");
                tv_totConfirmedCases.setText(String.valueOf(totalCase.getCases()));
                tv_totalDeaths.setText(String.valueOf(totalCase.getDeaths()));
                tv_totalRecovered.setText(String.valueOf(totalCase.getRecovered()));

                buildPieChart(totalCase.getDeaths(), totalCase.getRecovered(), totalCase.getCases());
                break;
            case FetchAsyncData.COUNTRIES:

                Type collectionType = new TypeToken<Collection<NCovidLiveData>>() {
                }.getType();
                Collection<NCovidLiveData> nCovidLiveDatas = gson.fromJson(data, collectionType);
                setUpRecyclerView(nCovidLiveDatas);
                break;
        }

    }

    private void setUpRecyclerView(Collection<NCovidLiveData> nCovidLiveDatas) {
        nCovidLiveDataList = new ArrayList<>();
        nCovidLiveDataList.addAll(nCovidLiveDatas);
        mAdapter = new NCVidCasesAdapter(this, nCovidLiveDataList, this);
        runAnimationAgain();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
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

    private void runAnimationAgain() {
        int resId = R.anim.layout_animation_fall_down;
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(this, resId);

        recyclerView.setLayoutAnimation(controller);
        mAdapter.notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();

    }

    private void buildPieChart(long deaths, long recoveredCases, long confirmedCases){
        List<PieEntry> entries = new LinkedList<>();
        entries.add(new PieEntry(deaths, "Deaths"));
        entries.add(new PieEntry(confirmedCases, "Confirmed"));
        entries.add(new PieEntry(recoveredCases, "Recovered"));
        PieDataSet set = new PieDataSet(entries,"");
        set.setColors(new int[] {R.color.red, R.color.yellow, R.color.colorPrimaryDark}, this);
        PieData data = new PieData(set);
        data.setValueTextColor(Color.WHITE);
        data.setValueTextSize(14);
        pieChart.setData(data);
        Description desc = new Description();
        desc.setText(getString(R.string.app_name));
        desc.setTextSize(14.0f);
        pieChart.setDescription(desc);
        pieChart.animateXY(3000,3000, Easing.EaseInQuart);
        pieChart.invalidate();
        pieChart.setDrawEntryLabels(false);
    }

}
