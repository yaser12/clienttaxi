package database.entity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import clienttaxi.CallingTaxiActivity;
import clienttaxi.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TaxisCallAdapter extends RecyclerView.Adapter<TaxisCallAdapter.ViewHolder> {
    private final List<Taxi> allTaxi ;
    private final Context context ;

    public TaxisCallAdapter(List<Taxi> allTaxi, Context context) {
        this.allTaxi = allTaxi;
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater =LayoutInflater .from(parent.getContext());
        View view=inflater.inflate(R.layout.taxi_call_list_item,parent,false);



        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final  Taxi taxi=allTaxi.get(position);
        holder.taxiTextView.setText(taxi.getDriver()+":"+taxi.getDescrip());
        holder.call_this_taxiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CallingTaxiActivity.class);
                intent.putExtra("taxi_id",""+ taxi.getTaxi_id());

                context. startActivity(intent);

                Toast.makeText(context,"يتم طلب التكسي "+taxi.getDriver()+":"+taxi.getDescrip(),Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return allTaxi.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView       (R.id.taxi_textView) TextView taxiTextView;
        @BindView       (R.id.call_this_taxibtn) Button  call_this_taxiButton;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }
    }
}
