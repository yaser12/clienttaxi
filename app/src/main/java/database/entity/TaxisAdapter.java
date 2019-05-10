package database.entity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import clienttaxi.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TaxisAdapter extends RecyclerView.Adapter<TaxisAdapter.ViewHolder> {
    private final List<Taxi> allTaxi ;
    private final Context context ;

    public TaxisAdapter(List<Taxi> allTaxi, Context context) {
        this.allTaxi = allTaxi;
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater =LayoutInflater .from(parent.getContext());
        View view=inflater.inflate(R.layout.taxi_list_item,parent,false);



        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final  Taxi taxi=allTaxi.get(position);
        holder.taxiTextView.setText(taxi.getDriver()+":"+taxi.getDescrip());
        holder.taxi_deletButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,"deleting "+taxi.getDriver()+":"+taxi.getDescrip(),Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return allTaxi.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView       (R.id.taxi_textView) TextView taxiTextView;
        @BindView       (R.id.taxi_deletbtn) Button  taxi_deletButton;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }
    }
}
