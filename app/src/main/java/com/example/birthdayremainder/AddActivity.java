package com.example.birthdayremainder;

import static android.view.View.GONE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddActivity extends AppCompatActivity {
    EditText etname;
    DatePicker etbirthday;
    ImageButton btimage;
    ImageView imageView;
    Button saveButton;
    int count;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        count = getIntent().getIntExtra("count", 0);
        etname = findViewById(R.id.etname);
        etbirthday = findViewById(R.id.etbirthday);
        btimage = findViewById(R.id.btimage);
        imageView = findViewById(R.id.imageView);
        saveButton = findViewById(R.id.btsave);

        btimage.setOnClickListener(view -> {
            openGallery();
        });
        saveButton.setOnClickListener(view -> saveData());

    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            // Persist URI permission for future access
            getContentResolver().takePersistableUriPermission(data.getData(), Intent.FLAG_GRANT_READ_URI_PERMISSION);
            selectedImageUri = data.getData();
            imageView.setImageURI(selectedImageUri);
            imageView.setVisibility(View.VISIBLE);
            btimage.setVisibility(GONE);
        }
    }

    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("BirthdayData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (etname.getText().toString().isEmpty()){
            Toast.makeText(this,"Name can't be empty", Toast.LENGTH_SHORT).show();
        }
        else {
            editor.putString("name" + count, etname.getText().toString());
            editor.putString("birthday" + count, dateToString());
            String imagePath = selectedImageUri != null ? selectedImageUri.toString() : "No Image";
            Log.e("imagePath", imagePath);
            editor.putString("imagePath" + count, imagePath);
            editor.putInt("count", ++count);
            editor.apply();
            switchActivity();
        }
    }

    private void switchActivity() {
        Intent intent = new Intent(AddActivity.this, MainActivity.class);
        intent.putExtra("count", count);
        startActivity(intent);
        finish();
    }

    public String dateToString() {
        DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        Date date = new Date(etbirthday.getYear()-1900, etbirthday.getMonth(), etbirthday.getDayOfMonth());
        return dateFormat.format(date);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        switchActivity();
    }
}