package dev.htm.ncovid.screen;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.lang.reflect.Type;
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
import dev.htm.ncovid.viewmodel.CoronaVirusViewModel;


public class HomeFragment extends Fragment implements OnCallback, NCVidCasesAdapter.onListener, View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    private TextView tv_statistics, tv_totConfirmedCases, tv_totalDeaths, tv_totalRecovered, country, version;
    private SwipeRefreshLayout swipeRefreshLayout;
    private PieChart pieChart;
    private CardView pt_cases, pt_deaths, pt_recovered;
    private String today = null;
    private long death = 0;
    private long recovered = 0;
    private long cases = 0;
    private CoronaVirusViewModel mCoronaViewModel;
    private ProgressBar progressBar;
    private View root;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_home, container, false);
        initView();
        initViewModel();
        swipeRefreshLayout.setRefreshing(true);
        return root;
    }

    private void initView() {

        tv_statistics = root.findViewById(R.id.tv_statistics);
        tv_totConfirmedCases = root.findViewById(R.id.tot_cnfm_cases);
        tv_totalDeaths = root.findViewById(R.id.tot_deaths);
        tv_totalRecovered = root.findViewById(R.id.cases_recovered);
        pieChart = root.findViewById(R.id.reportPieChart);
        country = root.findViewById(R.id.country);
        swipeRefreshLayout = root.findViewById(R.id.swipe_refresh);
        progressBar = root.findViewById(R.id.progress_circular_country);

        pt_cases = root.findViewById(R.id.pt_cases);
        pt_deaths = root.findViewById(R.id.pt_deaths);
        pt_recovered = root.findViewById(R.id.pt_recovered);


        tv_statistics.setText("• Statistics of Worldwide");

        //onclick
        pt_cases.setOnClickListener(this);
        pt_deaths.setOnClickListener(this);
        pt_recovered.setOnClickListener(this);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //prepareDataNCoVId();
                //loadCountriesDataNCoVid();
                initViewModel();
            }
        });
    }

    private void initViewModel() {
        mCoronaViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(CoronaVirusViewModel.class);

        mCoronaViewModel.getCoronaResumeInformation();
        mCoronaViewModel.mutableResumeLiveData.observe(getViewLifecycleOwner(), new Observer<CoronaVirusResume>() {
            @Override
            public void onChanged(CoronaVirusResume coronaVirusResume) {
                DateTime dt = new DateTime(coronaVirusResume.getUpdated());
                DateTimeFormatter fmt = DateTimeFormat.forPattern("dd/MM/yyyy");
                today = "• " + fmt.print(dt);
                tv_totConfirmedCases.setText(String.valueOf(coronaVirusResume.getCases()));
                tv_totalDeaths.setText(String.valueOf(coronaVirusResume.getDeaths()));
                tv_totalRecovered.setText(String.valueOf(coronaVirusResume.getRecovered()));
                death = coronaVirusResume.getDeaths();
                recovered = coronaVirusResume.getRecovered();
                cases = coronaVirusResume.getCases();
                buildPieChart(death, recovered, cases);
                swipeRefreshLayout.setRefreshing(false);
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onStart() {
        swipeRefreshLayout.setRefreshing(true);
        super.onStart();
    }

    @Override
    public void onResume() {
        swipeRefreshLayout.setRefreshing(false);
        super.onResume();
    }

    @Override
    public void onStop() {
        swipeRefreshLayout.setRefreshing(false);
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
            case FetchAsyncData.ALL:
                CoronaVirusResume coronaVirusResume = gson.fromJson(data, CoronaVirusResume.class);
                DateTime dt = new DateTime(coronaVirusResume.getUpdated());
                DateTimeFormatter fmt = DateTimeFormat.forPattern("dd/MM/yyyy");
                today = "• " + fmt.print(dt);
                tv_totConfirmedCases.setText(String.valueOf(coronaVirusResume.getCases()));
                tv_totalDeaths.setText(String.valueOf(coronaVirusResume.getDeaths()));
                tv_totalRecovered.setText(String.valueOf(coronaVirusResume.getRecovered()));
                death = coronaVirusResume.getDeaths();
                recovered = coronaVirusResume.getRecovered();
                cases = coronaVirusResume.getCases();
                buildPieChart(death, recovered, cases);
                break;
            case FetchAsyncData.COUNTRIES:
            case FetchAsyncData.COUNTRIES_SORT:
                swipeRefreshLayout.setRefreshing(false);
                Type collectionType = new TypeToken<Collection<CoronaVirus>>() {
                }.getType();
                Collection<CoronaVirus> coronaViri = gson.fromJson(data, collectionType);
                country.setText("• Total " + coronaViri.size() + " Regions");
                break;
            default:
                swipeRefreshLayout.setRefreshing(false);
                break;
        }

    }


    @Override
    public void onError(String error) {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onItemSelected(CoronaVirus cases) {

    }


    private void buildPieChart(long deaths, long recoveredCases, long confirmedCases) {
        List<PieEntry> entries = new LinkedList<>();
        entries.add(new PieEntry(deaths, "Deaths"));
        entries.add(new PieEntry(confirmedCases, "Confirmed"));
        entries.add(new PieEntry(recoveredCases, "Recovered"));
        PieDataSet set = new PieDataSet(entries, "");
        set.setColors(new int[]{R.color.red, R.color.yellow, R.color.colorPrimaryDark}, getActivity());
        PieData data = new PieData(set);
        data.setValueTextColor(Color.WHITE);
        data.setValueTextSize(14);
        pieChart.setData(data);
        Description desc = new Description();
        desc.setText(today);
        desc.setTextSize(14.0f);
        pieChart.setDescription(desc);
        pieChart.animateXY(3000, 3000, Easing.EaseInQuart);
        pieChart.invalidate();
        pieChart.setDrawEntryLabels(false);
        pieChart.setFitsSystemWindows(true);
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

}
