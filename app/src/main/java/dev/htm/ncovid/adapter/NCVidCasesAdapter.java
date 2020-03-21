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
import dev.htm.ncovid.model.CoronaVirus;
import dev.htm.ncovid.screen.HomeFragment;

public class NCVidCasesAdapter extends RecyclerView.Adapter<CasesViewHolder>
        implements Filterable {

    private Context context;
    private List<CoronaVirus> coronaVirusList;
    private List<CoronaVirus> coronaVirusListFiltered;
    private onListener listener;

    public NCVidCasesAdapter(Context context, List<CoronaVirus> coronaVirusList, onListener listener) {
        this.context = context;
        this.listener = listener;
        this.coronaVirusList = coronaVirusList;
        this.coronaVirusListFiltered = coronaVirusList;
    }

    public List<CoronaVirus> getCoronaVirusListFiltered() {
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
                    List<CoronaVirus> filteredList = new ArrayList<>();
                    for (CoronaVirus row : coronaVirusList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getCountry().toLowerCase().contains(charString.toLowerCase()) || row.getCases().contains(charSequence)) {
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
                coronaVirusListFiltered = (ArrayList<CoronaVirus>) filterResults.values;
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
        final CoronaVirus coronaVirus = coronaVirusListFiltered.get(position);
        holder.country_name.setText(coronaVirus.getCountry());
        holder.total_case.setText(String.valueOf(coronaVirus.getCases()));
        holder.todayCases.setText(String.valueOf(coronaVirus.getTodayCases()));
        holder.deaths.setText(String.valueOf(coronaVirus.getDeaths()));
        holder.todayDeaths.setText(String.valueOf(coronaVirus.getTodayDeaths()));
        holder.recovered.setText(String.valueOf(coronaVirus.getRecovered()));
        holder.critical.setText(String.valueOf(coronaVirus.getCritical()));
        holder.active.setText(String.valueOf(coronaVirus.getActive()));

        double percentage = 100 * Double.parseDouble(coronaVirus.getDeaths())/HomeFragment.cases;
        String percentageText = String.format("%2.02f", percentage) +"%";
        holder.tv_fatality.setText(percentageText);

    }

    @Override
    public int getItemCount() {
        return coronaVirusListFiltered.size();
    }

    public interface onListener {
        void onItemSelected(CoronaVirus cases);
    }
}
