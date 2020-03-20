package dev.htm.ncovid.screen;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import dev.htm.ncovid.R;
import dev.htm.ncovid.adapter.NCVidCasesAdapter;
import dev.htm.ncovid.model.CoronaVirus;


public class VietNamesFragment extends Fragment implements SearchView.OnQueryTextListener, NCVidCasesAdapter.onListener {
    private View root;
    private TextView countryVn;
    private SwipeRefreshLayout swipeRefreshLayoutVn;
    private RecyclerView recyclerViewVn;
    private SearchView searchViewVn;
    private ProgressBar progressBarVn;

    private List<CoronaVirus> coronaVirusList;
    private NCVidCasesAdapter mAdapter;
    private String today = null;

    public VietNamesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_vietnames, container, false);
        return root;
    }

    private void initView() {
        searchViewVn = root.findViewById(R.id.search);
        countryVn = root.findViewById(R.id.country);
        swipeRefreshLayoutVn = root.findViewById(R.id.swipe_refresh);
        recyclerViewVn = root.findViewById(R.id.recyclerCase);
        progressBarVn = root.findViewById(R.id.progress_circular_country);

        searchViewVn.setOnQueryTextListener(this);

        swipeRefreshLayoutVn.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                prepareDataNCoVId();
                //loadCountriesDataNCoVid();
            }
        });
    }

    private void setUpRecyclerView(Collection<CoronaVirus> coronaViri) {
        coronaVirusList = new ArrayList<>();
        coronaVirusList.addAll(coronaViri);
        mAdapter = new NCVidCasesAdapter(getActivity(), coronaVirusList, this);
        mAdapter.setHasStableIds(true);
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
    public void onItemSelected(CoronaVirus cases) {

    }
}
