package dev.htm.ncovid.screen;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import dev.htm.ncovid.R;
import dev.htm.ncovid.adapter.NCVidCasesAdapter;
import dev.htm.ncovid.adapter.NCVidCasesVietNamAdapter;
import dev.htm.ncovid.model.CoronaVirus;
import dev.htm.ncovid.model.CoronaVirusVietNam;
import dev.htm.ncovid.util.NetworkHelper;
import dev.htm.ncovid.viewmodel.CoronaVirusViewModel;


public class VietNamesFragment extends Fragment implements SearchView.OnQueryTextListener, NCVidCasesVietNamAdapter.onListener {
    private View root;
    private TextView countryVn;
    private SwipeRefreshLayout swipeRefreshLayoutVn;
    private RecyclerView recyclerViewVn;
    //private SearchView searchViewVn;
    private ProgressBar progressBarVn;
    private TextView tv_statistics_vn, tv_totConfirmedCases_vn, tv_totalDeaths_vn, tv_totalRecovered_vn, tv_active_vn, version;

    private NCVidCasesVietNamAdapter mAdapter;
    private CoronaVirusViewModel mCoronaViewModel;

    public VietNamesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_vietnames, container, false);
        initView();
        initViewModel();
        return root;
    }

    private void initView() {
        //searchViewVn = root.findViewById(R.id.search_vn);

        tv_statistics_vn = root.findViewById(R.id.tv_statistics_vn);
        tv_totConfirmedCases_vn = root.findViewById(R.id.tot_cnfm_cases_vn);
        tv_totalDeaths_vn = root.findViewById(R.id.tot_deaths_vn);
        tv_totalRecovered_vn = root.findViewById(R.id.cases_recovered_vn);
        tv_active_vn = root.findViewById(R.id.cases_active_vn);
        countryVn = root.findViewById(R.id.tt_country_vn);
        swipeRefreshLayoutVn = root.findViewById(R.id.swipe_refresh_vn);
        recyclerViewVn = root.findViewById(R.id.recyclerCase_vn);
        progressBarVn = root.findViewById(R.id.progress_circular_country_vn);
        countryVn.setText(getString(R.string.country));

        //searchViewVn.setOnQueryTextListener(this);

        swipeRefreshLayoutVn.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initViewModel();
            }
        });
    }

    private void initViewModel() {
        mCoronaViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(CoronaVirusViewModel.class);
        if (!NetworkHelper.CheckNetwork()) {
            Toast.makeText(getActivity(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
            swipeRefreshLayoutVn.setRefreshing(false);
            progressBarVn.setVisibility(View.GONE);
        } else {
            mCoronaViewModel.getCoronaVietNameCompleteInformation();
            mCoronaViewModel.mutableCompleteLiveDataVn.observe(getViewLifecycleOwner(), new Observer<List<CoronaVirusVietNam>>() {
                @Override
                public void onChanged(List<CoronaVirusVietNam> coronaVirus) {
                    setUpRecyclerView(coronaVirus);
                    swipeRefreshLayoutVn.setRefreshing(false);
                    progressBarVn.setVisibility(View.GONE);
                }
            });

            mCoronaViewModel.getCoronaVirusTotalVietName();
            mCoronaViewModel.mutableResumeLiveDataVn.observe(getViewLifecycleOwner(), new Observer<CoronaVirus>() {
                @Override
                public void onChanged(CoronaVirus coronaVirus) {
                    tv_totConfirmedCases_vn.setText(String.valueOf(coronaVirus.getCases()));
                    tv_totalDeaths_vn.setText(String.valueOf(coronaVirus.getDeaths()));
                    tv_totalRecovered_vn.setText(String.valueOf(coronaVirus.getRecovered()));
                    tv_active_vn.setText(String.valueOf(coronaVirus.getActive()));
                }
            });
        }


    }


    private void setUpRecyclerView(List<CoronaVirusVietNam> coronaVirusVietNams) {

        mAdapter = new NCVidCasesVietNamAdapter(getActivity(), coronaVirusVietNams, this);
        runAnimationAgain();
        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewVn.setHasFixedSize(true);
        recyclerViewVn.setLayoutManager(mLayoutManager);
        recyclerViewVn.setItemAnimator(new DefaultItemAnimator());
        recyclerViewVn.setAdapter(mAdapter);

        swipeRefreshLayoutVn.setRefreshing(false);
    }

    private void runAnimationAgain() {
        int resId = R.anim.layout_animation_fall_down;
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(getActivity(), resId);

        recyclerViewVn.setLayoutAnimation(controller);
        mAdapter.notifyDataSetChanged();
        recyclerViewVn.setItemViewCacheSize(20);
        recyclerViewVn.setDrawingCacheEnabled(true);
        recyclerViewVn.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerViewVn.scheduleLayoutAnimation();

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void onItemSelected(CoronaVirusVietNam cases) {

    }
}
