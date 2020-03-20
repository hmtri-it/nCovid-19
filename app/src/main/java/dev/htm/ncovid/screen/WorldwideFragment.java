package dev.htm.ncovid.screen;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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

import dev.htm.ncovid.BuildConfig;
import dev.htm.ncovid.R;
import dev.htm.ncovid.adapter.NCVidCasesAdapter;
import dev.htm.ncovid.model.CoronaVirus;
import dev.htm.ncovid.model.CoronaVirusResume;
import dev.htm.ncovid.service.FetchAsyncData;
import dev.htm.ncovid.service.OnCallback;
import dev.htm.ncovid.util.GetDataUtil;
import dev.htm.ncovid.util.NetworkHelper;


public class WorldwideFragment extends Fragment implements OnCallback, NCVidCasesAdapter.onListener, View.OnClickListener, android.widget.SearchView.OnQueryTextListener {
    // TODO: Rename parameter arguments, choose names that match
    private TextView country, version;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private SearchView searchView;
    private ProgressBar progressBar;

    private List<CoronaVirus> coronaVirusList;
    private NCVidCasesAdapter mAdapter;
    private String today = null;

    private View root;

    public WorldwideFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Toast.makeText(getActivity(), "TASTASTA", Toast.LENGTH_SHORT).show();
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_worldwide, container, false);
        initView();
        loadCountriesDataNCoVid();
        return root;
    }

    private void initView() {
        searchView = root.findViewById(R.id.search);
        country = root.findViewById(R.id.country);
        swipeRefreshLayout = root.findViewById(R.id.swipe_refresh);
        recyclerView = root.findViewById(R.id.recyclerCase);
        progressBar = root.findViewById(R.id.progress_circular_country);

        searchView.setOnQueryTextListener(this);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                prepareDataNCoVId();
                //loadCountriesDataNCoVid();
            }
        });
    }

    @Override
    public void onStart() {
        swipeRefreshLayout.setRefreshing(true);
        progressBar.setVisibility(View.VISIBLE);
        super.onStart();
    }

    @Override
    public void onResume() {
        swipeRefreshLayout.setRefreshing(false);
        progressBar.setVisibility(View.VISIBLE);
        super.onResume();
    }

    @Override
    public void onStop() {
        swipeRefreshLayout.setRefreshing(false);
        progressBar.setVisibility(View.GONE);
        super.onStop();
    }

    private void prepareDataNCoVId() {
        if (!NetworkHelper.CheckNetwork()) {
            Toast.makeText(getActivity(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
            swipeRefreshLayout.setRefreshing(false);
        } else {
            GetDataUtil.totalCase(getActivity(), FetchAsyncData.ALL, this, FetchAsyncData.ALL);
        }
    }

    private void loadCountriesDataNCoVid() {
        if (!NetworkHelper.CheckNetwork()) {
            Toast.makeText(getActivity(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
            swipeRefreshLayout.setRefreshing(false);
            progressBar.setVisibility(View.GONE);
        } else {
            GetDataUtil.totalCase(getActivity(), FetchAsyncData.COUNTRIES, this, FetchAsyncData.COUNTRIES);
        }
    }

    private void loadCountriesDataWithSortNCoVid(String property) {
        if (!NetworkHelper.CheckNetwork()) {
            Toast.makeText(getActivity(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
            swipeRefreshLayout.setRefreshing(false);
        } else {
            GetDataUtil.totalCase(getActivity(), FetchAsyncData.COUNTRIES_SORT + property, this, FetchAsyncData.COUNTRIES_SORT);
        }
    }

    @Override
    public void onSuccess(String type, String data) {

        Gson gson = new GsonBuilder().create();
        switch (type) {
            case FetchAsyncData.COUNTRIES:
            case FetchAsyncData.COUNTRIES_SORT:
                swipeRefreshLayout.setRefreshing(false);
                Type collectionType = new TypeToken<Collection<CoronaVirus>>() {
                }.getType();
                Collection<CoronaVirus> coronaViri = gson.fromJson(data, collectionType);
                country.setText("• Total " + coronaViri.size() + " Regions");
                setUpRecyclerView(coronaViri);
                progressBar.setVisibility(View.GONE);

                break;
            default:
                swipeRefreshLayout.setRefreshing(false);
                break;
        }

    }

    private void setUpRecyclerView(Collection<CoronaVirus> coronaViri) {
        coronaVirusList = new ArrayList<>();
        coronaVirusList.addAll(coronaViri);
        mAdapter = new NCVidCasesAdapter(getActivity(), coronaVirusList, this);
        mAdapter.setHasStableIds(true);
        runAnimationAgain();
        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onError(String error) {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onItemSelected(CoronaVirus cases) {

    }

    private void runAnimationAgain() {
        int resId = R.anim.layout_animation_fall_down;
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(getActivity(), resId);

        recyclerView.setLayoutAnimation(controller);
        mAdapter.notifyDataSetChanged();
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.scheduleLayoutAnimation();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pt_cases:
                loadCountriesDataWithSortNCoVid("cases");
                break;
            case R.id.pt_deaths:
                loadCountriesDataWithSortNCoVid("deaths");
                break;
            case R.id.pt_recovered:
                loadCountriesDataWithSortNCoVid("recovered");
                break;
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        // filter recycler view when query submitted
        mAdapter.getFilter().filter(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        // filter recycler view when text is changed
        mAdapter.getFilter().filter(newText);
        return false;
    }

}
