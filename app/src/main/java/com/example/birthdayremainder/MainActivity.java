package com.example.birthdayremainder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    List<DataClass> list;
    RecyclerView.Adapter adapter;
    int count=1;
    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        list = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        floatingActionButton = findViewById(R.id.floatingActionButton);
        setAdapter();

        floatingActionButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddActivity.class);
            intent.putExtra("count", count);
            startActivity(intent);
            finish();
        });
        retrieveData();
    }
    private void retrieveData() {
        list.clear();

        SharedPreferences sharedPreferences = getSharedPreferences("BirthdayData", MODE_PRIVATE);
        count = sharedPreferences.getInt("count", 1); // Get the current count (last added item index)

        // Loop through the stored data using the count
        for (int i = 1; i <= count; i++) {
            String name = sharedPreferences.getString("name" + i, null);
            String birthday = sharedPreferences.getString("birthday" + i, null);
            String imagePath = sharedPreferences.getString("imagePath" + i, null);

            if (name != null && birthday != null) {
                list.add(new DataClass(name, birthday, imagePath));
                Log.e("Retrieve", "retrieveData: "+name+birthday+imagePath );
            }
        }

        // Notify the adapter that the data set has changed
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }



    private void setAdapter() {
        adapter = new Adapter(list);
        recyclerView.setAdapter(adapter);
    }
}