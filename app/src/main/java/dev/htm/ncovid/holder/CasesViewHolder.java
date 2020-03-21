package dev.htm.ncovid.holder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import dev.htm.ncovid.R;

public class CasesViewHolder extends RecyclerView.ViewHolder {
    public TextView country_name, total_case, recovered, critical, deaths, todayCases, todayDeaths, active, tv_fatality;


    public CasesViewHolder(View view) {
        super(view);
        country_name = view.findViewById(R.id.country);
        total_case = view.findViewById(R.id.caseValue);

        recovered = view.findViewById(R.id.recovered);
        critical = view.findViewById(R.id.critical);
        deaths = view.findViewById(R.id.deaths);
        todayCases = view.findViewById(R.id.todayCases);
        todayDeaths = view.findViewById(R.id.todayDeaths);
        active = view.findViewById(R.id.active);
        tv_fatality = view.findViewById(R.id.fatality);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

}
