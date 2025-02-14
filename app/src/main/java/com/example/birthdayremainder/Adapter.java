package com.example.birthdayremainder;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;
import java.util.Objects;

public class Adapter extends RecyclerView.Adapter<Adapter.ItemViewHolder> {
    private List<DataClass> list;
    private Context context;

    public Adapter(Context context, List<DataClass> itemList) {
        this.context = context;
        this.list = itemList;
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvname, tvdate;
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

        holder.itemView.setOnLongClickListener(view -> {
            showPopupMenu(view, position);
            return true;
        });
    }

    private void showPopupMenu(View view, int position) {
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        popupMenu.inflate(R.menu.popupmenu);

        popupMenu.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.optiondelete) {
                deleteData(position);
            } else if (id == R.id.optionedit) {
            } else {
                throw new IllegalStateException("Unexpected value: " + item.getItemId());
            }
            return true;
        });
        popupMenu.show();
    }

    private void deleteData(int position) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("BirthdayData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        int count = sharedPreferences.getInt("count", 0);
        if (count == 0 || position >= count) {
            return;
        }

        editor.remove("name" + position);
        editor.remove("birthday" + position);
        editor.remove("imagePath" + position);

        for (int i = position; i < count - 1; i++) {
            String nextName = sharedPreferences.getString("name" + (i + 1), null);
            String nextBirthday = sharedPreferences.getString("birthday" + (i + 1), null);
            String nextImage = sharedPreferences.getString("imagePath" + (i + 1), null);

            if (nextName != null) {
                editor.putString("name" + i, nextName);
                editor.putString("birthday" + i, nextBirthday);
                editor.putString("imagePath" + i, nextImage);
            }
        }

        editor.remove("name" + (count - 1));
        editor.remove("birthday" + (count - 1));
        editor.remove("imagePath" + (count - 1));

        editor.putInt("count", count - 1);
        editor.apply();

        list.remove(position);
        notifyItemRemoved(position);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }
}
