package com.demo.mymovies.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.mymovies.R;
import com.demo.mymovies.data.Trailer;

import java.util.ArrayList;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.trailerviewHolder> {

    private ArrayList<Trailer> trailwers;
    private OnTrailerClickListener onTrailerClickListener;

    public  interface OnTrailerClickListener{
        public void OnTrailerClick(String url);
    }


    public void setOnTrailerClickListener(OnTrailerClickListener onTrailerClickListener) {
        this.onTrailerClickListener = onTrailerClickListener;
    }

    @NonNull
    @Override
    public trailerviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viev= LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_item, parent, false);
        return new trailerviewHolder(viev);
    }

    @Override
    public void onBindViewHolder(@NonNull trailerviewHolder holder, int position) {
        Trailer trailer=trailwers.get(position);
        holder.textViewNameOfVideo.setText(trailer.getName());


    }

    @Override
    public int getItemCount() {
        return trailwers.size();
    }

    public void setTrailwers(ArrayList<Trailer> trailwers) {
        this.trailwers = trailwers;
        notifyDataSetChanged();
    }

    class trailerviewHolder extends RecyclerView.ViewHolder{

        private TextView textViewNameOfVideo;

        public trailerviewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNameOfVideo=itemView.findViewById(R.id.textViewNameOfVideo);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onTrailerClickListener!=null){
                       onTrailerClickListener.OnTrailerClick(trailwers.get(getAdapterPosition()).getKey());
                    }
                }
            });

        }
    }
}
