package dev.htm.ncovid.screen;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.List;

import dev.htm.ncovid.R;
import dev.htm.ncovid.adapter.NCVidCasesAdapter;
import dev.htm.ncovid.model.CoronaVirus;
import dev.htm.ncovid.util.NetworkHelper;
import dev.htm.ncovid.util.ViewUtil;
import dev.htm.ncovid.viewmodel.CoronaVirusViewModel;


public class WorldwideFragment extends Fragment implements NCVidCasesAdapter.onListener, SearchView.OnQueryTextListener, SearchView.OnCloseListener {
    // TODO: Rename parameter arguments, choose names that match
    private LinearLayout root_layout;
    private TextView tv_country;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private SearchView searchView;
    private ProgressBar progressBar;
    private NCVidCasesAdapter mAdapter;

    private CoronaVirusViewModel mCoronaViewModel;

    private View root;

    public WorldwideFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_worldwide, container, false);
        initView();
        initViewModel();
        return root;
    }

    private void initView() {
        root_layout = root.findViewById(R.id.root_layout);
        searchView = root.findViewById(R.id.search);
        tv_country = root.findViewById(R.id.country);
        swipeRefreshLayout = root.findViewById(R.id.swipe_refresh);
        recyclerView = root.findViewById(R.id.recyclerCase);
        progressBar = root.findViewById(R.id.progress_circular_country);

        searchView.setOnQueryTextListener(this);
        searchView.setOnCloseListener(this);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initViewModel();
            }
        });
        if (searchView != null) {
            ViewUtil.show(getActivity());
            searchView.clearFocus();
        }

    }

    private void initViewModel() {
        mCoronaViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(CoronaVirusViewModel.class);
        if (!NetworkHelper.CheckNetwork()) {
            Toast.makeText(getActivity(), R.string.check_connection, Toast.LENGTH_SHORT).show();
            swipeRefreshLayout.setRefreshing(false);
            progressBar.setVisibility(View.GONE);
        } else {
            mCoronaViewModel.getCoronaCompleteInformation();
            mCoronaViewModel.mutableCompleteLiveData.observe(getViewLifecycleOwner(), new Observer<List<CoronaVirus>>() {
                @Override
                public void onChanged(List<CoronaVirus> coronaVirus) {
                    UpdateTotalRegions(coronaVirus.size());
                    setUpRecyclerView(coronaVirus);
                    swipeRefreshLayout.setRefreshing(false);
                    progressBar.setVisibility(View.GONE);
                }
            });
        }


    }


    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    private void UpdateTotalRegions(int size) {
        tv_country.setText(String.format("• Total %d Regions  at of today %s", size, ViewUtil.showDatetime(false, System.currentTimeMillis())));
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
        if (searchView != null) {
            ViewUtil.hide(getActivity(), searchView);
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();

        if (searchView != null) {
            ViewUtil.hide(getActivity(), searchView);
        }
    }

    @Override
    public void onStop() {
        swipeRefreshLayout.setRefreshing(false);
        progressBar.setVisibility(View.GONE);
        super.onStop();
    }

    private void setUpRecyclerView(List<CoronaVirus> virusList) {
        mAdapter = new NCVidCasesAdapter(getActivity(), virusList, this);
        runAnimationAgain();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

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
    public boolean onQueryTextSubmit(String query) {
        // filter recycler view when query submitted
        mAdapter.getFilter().filter(query);
        searchView.clearFocus();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        // filter recycler view when text is changed
        mAdapter.getFilter().filter(newText);

        UpdateTotalRegions(mAdapter.getItemCount());
        return false;
    }

    @Override
    public boolean onClose() {
        hideKeyboard();
        return false;
    }


    /**
     * Hide Soft KeyBoard When Click on crossIcon of SearchView
     */

    private boolean hideKeyboard() {
        try {
            if (searchView.hasFocus()) {
                Log.d("alert", "hiding and removing focus");
                searchView.clearFocus();

                searchView.setIconified(true);
                //   used to again closed searchview  after clear text from click on cross button
            }

            InputMethodManager imm =
                    (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

            imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
