package com.K214111950;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.K214111950.databinding.ActivityEditBinding;
import com.K214111950.model.Product;

public class EditActivity extends AppCompatActivity {

    ActivityEditBinding binding;

    Intent intent;
    Product p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getData();

        addEvents();
    }

    private void getData() {
        intent = getIntent();
        p = (Product) intent.getSerializableExtra("data");
        binding.edtName.setText(p.getProductName());
        binding.edtPrice.setText(String.valueOf(p.getProductPrice()));
    }

    private void updateData() {
        ContentValues values = new ContentValues();
        values.put(MainActivity.COL_NAME, binding.edtName.getText().toString());
        values.put(MainActivity.COL_PRICE, Double.parseDouble(binding.edtPrice.getText().toString()));

        int numberOfRows = MainActivity.db.update(MainActivity.TBL_NAME, values, MainActivity.COL_ID + "=?", new
                String[]{String.valueOf(p.getProductCode())});

        if(numberOfRows > 0)
            Toast.makeText(this, "Success!", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "Fail!", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void addEvents() {
        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();

            }
        });
        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}