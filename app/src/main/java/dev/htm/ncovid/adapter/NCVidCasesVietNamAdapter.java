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

public class NCVidCasesVietNamAdapter extends RecyclerView.Adapter<CasesVietNameViewHolder>
        implements Filterable {

    private Context context;
    private List<CoronaVirusVietNam> coronaVirusList;
    private List<CoronaVirusVietNam> coronaVirusListFiltered;
    private onListener listener;

    public NCVidCasesVietNamAdapter(Context context, List<CoronaVirusVietNam> coronaVirusList, onListener listener) {
        this.context = context;
        this.listener = listener;
        this.coronaVirusList = coronaVirusList;
        this.coronaVirusListFiltered = coronaVirusList;
    }

    public List<CoronaVirusVietNam> getCoronaVirusListFiltered() {
        return coronaVirusListFiltered;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    coronaVirusListFiltered = coronaVirusList;
                } else {
                    List<CoronaVirusVietNam> filteredList = new ArrayList<>();
                    for (CoronaVirusVietNam row : coronaVirusList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getAddress().toLowerCase().contains(charString.toLowerCase()) || row.getDate().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    coronaVirusListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = coronaVirusListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                coronaVirusListFiltered = (ArrayList<CoronaVirusVietNam>) filterResults.values;
                notifyDataSetChanged();
            }
        };
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
        final CoronaVirusVietNam coronaVirus = coronaVirusListFiltered.get(position);
        holder.country_name.setText(coronaVirus.getAddress());
        holder.date.setText(String.valueOf(coronaVirus.getDoubt()));
        holder.total_case.setText(String.valueOf(coronaVirus.getNumber()));
        holder.recovered.setText(String.valueOf(coronaVirus.getRecovered()));
        holder.todayCases.setText(String.valueOf(coronaVirus.getDeaths()));
        holder.critical.setText(String.valueOf(coronaVirus.getDate()));

        //holder.todayDeaths.setText(String.valueOf(coronaVirus.getTodayDeaths()));


        //holder.active.setText(String.valueOf(coronaVirus.getActive()));

    }

    @Override
    public int getItemCount() {
        return coronaVirusListFiltered.size();
    }

    public interface onListener {
        void onItemSelected(CoronaVirusVietNam cases);
    }
}
