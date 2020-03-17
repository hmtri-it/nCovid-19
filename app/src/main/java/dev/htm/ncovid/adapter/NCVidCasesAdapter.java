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
import java.util.List;

import dev.htm.ncovid.R;
import dev.htm.ncovid.holder.CasesViewHolder;
import dev.htm.ncovid.model.NCovidLiveData;

public class NCVidCasesAdapter extends RecyclerView.Adapter<CasesViewHolder>
        implements Filterable {

    private Context context;
    private List<NCovidLiveData> nCovidLiveDataList;
    private List<NCovidLiveData> nCovidLiveDataListFiltered;
    private onListener listener;

    public NCVidCasesAdapter(Context context, List<NCovidLiveData> nCovidLiveDataList, onListener listener) {
        this.context = context;
        this.listener = listener;
        this.nCovidLiveDataList = nCovidLiveDataList;
        this.nCovidLiveDataListFiltered = nCovidLiveDataList;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    nCovidLiveDataListFiltered = nCovidLiveDataList;
                } else {
                    List<NCovidLiveData> filteredList = new ArrayList<>();
                    for (NCovidLiveData row : nCovidLiveDataList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getCountry().toLowerCase().contains(charString.toLowerCase()) || row.getCases().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    nCovidLiveDataListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = nCovidLiveDataListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                nCovidLiveDataListFiltered = (ArrayList<NCovidLiveData>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    @NonNull
    @Override
    public CasesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recycler_cases_shimmer, parent, false);

        return new CasesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CasesViewHolder holder, int position) {
        final NCovidLiveData nCovidLiveData = nCovidLiveDataListFiltered.get(position);
        holder.country_name.setText(nCovidLiveData.getCountry());
        holder.total_case.setText(String.valueOf(nCovidLiveData.getCases()));
        holder.recovered.setText(String.valueOf(nCovidLiveData.getRecovered()));
        holder.critical.setText(String.valueOf(nCovidLiveData.getCritical()));
        holder.deaths.setText(String.valueOf(nCovidLiveData.getDeaths()));
        holder.todayCases.setText(String.valueOf(nCovidLiveData.getTodayCases()));
    }

    @Override
    public int getItemCount() {
        return nCovidLiveDataListFiltered.size();
    }

    public interface onListener {
        void onItemSelected(NCovidLiveData cases);
    }
}
