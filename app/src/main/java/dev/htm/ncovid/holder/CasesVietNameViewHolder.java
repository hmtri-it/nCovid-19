package dev.htm.ncovid.holder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import dev.htm.ncovid.R;

public class CasesVietNameViewHolder extends RecyclerView.ViewHolder {
    public TextView country_name, total_case, recovered, critical, date, deaths;


    public CasesVietNameViewHolder(View view) {
        super(view);
        country_name = view.findViewById(R.id.country_vn);
        date = view.findViewById(R.id.date);
        total_case = view.findViewById(R.id.caseValue_vn);
        recovered = view.findViewById(R.id.recovered_vn);

        critical = view.findViewById(R.id.critical_vn);
        deaths = view.findViewById(R.id.deaths_vn);


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

}
