package dev.htm.ncovid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dev.htm.ncovid.R;
import dev.htm.ncovid.holder.CasesVietNameViewHolder;
import dev.htm.ncovid.holder.CasesViewHolder;
import dev.htm.ncovid.model.CoronaVirus;
import dev.htm.ncovid.model.CoronaVirusVietNam;

public class NCVidCasesVietNamAdapter extends RecyclerView.Adapter<CasesVietNameViewHolder> {

    private Context context;
    private List<CoronaVirusVietNam> coronaVirusList;
    private onListener listener;

    public NCVidCasesVietNamAdapter(Context context, List<CoronaVirusVietNam> coronaVirusList, onListener listener) {
        this.context = context;
        this.listener = listener;
        this.coronaVirusList = coronaVirusList;
    }


    @NonNull
    @Override
    public CasesVietNameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recycler_cases_shimmer_vn, parent, false);

        return new CasesVietNameViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CasesVietNameViewHolder holder, int position) {
        final CoronaVirusVietNam coronaVirus = coronaVirusList.get(position);
        holder.country_name.setText(coronaVirus.getAddress());
        holder.date.setText(String.valueOf(coronaVirus.getDate()));
        holder.total_case.setText(String.valueOf(coronaVirus.getNumber()));
        holder.recovered.setText(String.valueOf(coronaVirus.getRecovered()));
        holder.deaths.setText(String.valueOf(coronaVirus.getDeaths()));
        holder.critical.setText(String.valueOf(coronaVirus.getDoubt()));
    }

    @Override
    public int getItemCount() {
        return coronaVirusList.size();
    }

    public interface onListener {
        void onItemSelected(CoronaVirusVietNam cases);
    }
}
