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
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.LinkedList;
import java.util.List;

import dev.htm.ncovid.R;
import dev.htm.ncovid.adapter.NCVidCasesAdapter;
import dev.htm.ncovid.model.CoronaVirus;
import dev.htm.ncovid.model.CoronaVirusResume;
import dev.htm.ncovid.util.NetworkHelper;
import dev.htm.ncovid.util.ViewUtil;
import dev.htm.ncovid.viewmodel.CoronaVirusViewModel;


public class HomeFragment extends Fragment implements NCVidCasesAdapter.onListener {
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

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initViewModel();
            }
        });
    }

    private void initViewModel() {
        mCoronaViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(CoronaVirusViewModel.class);
        if (!NetworkHelper.CheckNetwork()) {
            Toast.makeText(getActivity(), getString(R.string.check_connection), Toast.LENGTH_SHORT).show();
            swipeRefreshLayout.setRefreshing(false);
            progressBar.setVisibility(View.GONE);
        } else {
            mCoronaViewModel.getCoronaResumeInformation();
            mCoronaViewModel.mutableResumeLiveData.observe(getViewLifecycleOwner(), new Observer<CoronaVirusResume>() {
                @Override
                public void onChanged(CoronaVirusResume coronaVirusResume) {
                    today = ViewUtil.showDatetime(true, coronaVirusResume.getUpdated());
                    tv_totConfirmedCases.setText(String.valueOf(coronaVirusResume.getCases()));
                    tv_totalDeaths.setText(String.valueOf(coronaVirusResume.getDeaths()));
                    tv_totalRecovered.setText(String.valueOf(coronaVirusResume.getRecovered()));
                    death = coronaVirusResume.getDeaths();
                    recovered = coronaVirusResume.getRecovered();
                    cases = coronaVirusResume.getCases();
                    buildPieChart(cases, death, recovered);
                    swipeRefreshLayout.setRefreshing(false);
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
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

    @Override
    public void onItemSelected(CoronaVirus cases) {

    }


    private void buildPieChart(long confirmedCases, long deaths, long recoveredCases) {
        List<PieEntry> entries = new LinkedList<>();
        entries.add(new PieEntry(confirmedCases, "Confirmed"));
        entries.add(new PieEntry(deaths, "Deaths"));
        entries.add(new PieEntry(recoveredCases, "Recovered"));
        PieDataSet dataSet = new PieDataSet(entries, "");

        dataSet.setColors(new int[]{R.color.yellow, R.color.red, R.color.colorPrimaryDark}, getActivity());
        dataSet.setSliceSpace(2f);
        dataSet.setSelectionShift(2f);

        PieData data = new PieData(dataSet);
        data.setValueTextColor(Color.WHITE);
        data.setValueTextSize(14f);
        data.setValueFormatter(new PercentFormatter(pieChart));

        // enable hole and configure
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleRadius(20f);
        pieChart.setTransparentCircleRadius(10f);
        pieChart.setUsePercentValues(true);
        pieChart.setHighlightPerTapEnabled(true);
        pieChart.setData(data);

        Description desc = new Description();
        desc.setText(today);
        desc.setTextSize(14.0f);
        pieChart.setDescription(desc);
        pieChart.setDrawRoundedSlices(true);
        pieChart.setDrawSlicesUnderHole(true);
        pieChart.setMinAngleForSlices(15f);
        pieChart.animateXY(1000, 1000, Easing.EaseInQuart);
        pieChart.invalidate();
        pieChart.setDrawEntryLabels(true);
        pieChart.invalidate();
        pieChart.setFitsSystemWindows(true);


        Legend l = pieChart.getLegend();
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);


    }

}
