package giuseppevetri.cronometro;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by giuseppe on 12/05/17.
 */

public class LapsAdapter extends RecyclerView.Adapter<LapsAdapter.ViewHolder> {
    private List<String> laps_list;

    public LapsAdapter(List<String> laps_list) {
        this.laps_list = laps_list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_laps,tv_number_laps;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_laps = (TextView) itemView.findViewById(R.id.tv_laps_row);
            tv_number_laps = (TextView) itemView.findViewById(R.id.tv_number_lap);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.laps_row, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String string = laps_list.get(position);
        holder.tv_laps.setText(string);
        holder.tv_number_laps.setText(String.format("Lap %d -", position));
    }

    @Override
    public int getItemCount() {
        return laps_list.size();
    }
}
