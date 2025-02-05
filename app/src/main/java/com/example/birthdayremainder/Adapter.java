package com.example.birthdayremainder;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ItemViewHolder> {
    private List<DataClass> list;
    public Adapter(List<DataClass> itemList) {
        this.list = itemList;
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvname,tvdate;
        ShapeableImageView imageView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            tvname = itemView.findViewById(R.id.tvname);
            tvdate = itemView.findViewById(R.id.tvdate);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        DataClass item = list.get(position);
        holder.tvname.setText(item.getName());
        holder.tvdate.setText(item.getBirthday());
        holder.imageView.setImageURI(Uri.parse(item.getImagePath()));
    }


    @Override
    public int getItemCount() {
        return list.size();
    }
}

