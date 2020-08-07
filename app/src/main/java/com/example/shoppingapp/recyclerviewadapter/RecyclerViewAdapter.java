package com.example.shoppingapp.recyclerviewadapter;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppingapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<JSONObject> productList;

    public RecyclerViewAdapter(Context context, ArrayList<JSONObject> productList) {
        this.context = context;
        this.productList = productList;
    }

    // Where to get the single card as viewholder Object
    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
        return new ViewHolder(view);
    }

    // What will happen after we create the viewholder object
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        String product = null;
        String date = null;
        try {
            product = productList.get(position).getString("productName");
            date = productList.get(position).getString("orderDate");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        holder.product.setText(product);
        holder.date.setText("Ordered on : " + date);
        if(product.equals("Razer Blade Stealth")){
           holder.iconButton.setImageResource(R.drawable.razer);
        }else if(product.equals("Dell XPS 15 9560")){
            holder.iconButton.setImageResource(R.drawable.dellxps);
        }else if(product.equals("Predator Gaming")){
            holder.iconButton.setImageResource(R.drawable.predator);
        }else if(product.equals("HP Light")){
            holder.iconButton.setImageResource(R.drawable.hplight);
        }else if(product.equals("Asus Zephyrus")){
            holder.iconButton.setImageResource(R.drawable.asus);
        }else if(product.equals("Legion Gaming")){
            holder.iconButton.setImageResource(R.drawable.legion);
        }else{
            holder.iconButton.setImageResource(R.drawable.macairoriginal);
        }


    }

    // How many items?
    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView product;
        public TextView date;
        public ImageView iconButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            product = itemView.findViewById(R.id.product);
            date = itemView.findViewById(R.id.date);
            iconButton = itemView.findViewById(R.id.icon_button);

            iconButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Log.d("info", "Clicked");

        }
    }
}
