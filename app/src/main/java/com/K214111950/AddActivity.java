package com.K214111950;

import static com.K214111950.MainActivity.TBL_NAME;

import android.content.ContentValues;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.K214111950.databinding.ActivityAddBinding;

public class AddActivity extends AppCompatActivity {
    ActivityAddBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        addEvents();
    }

    private void addEvents() {
        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // insert data
                ContentValues values = new ContentValues();
                values.put(MainActivity.COL_NAME, binding.edtName.getText().toString());
                // chuyển price sang số thực
                values.put(MainActivity.COL_PRICE, Double.parseDouble(binding.edtPrice.getText().toString()));

                long numOfRows = MainActivity.db.insert(MainActivity.TBL_NAME, null, values);
                if(numOfRows > 0)
                    Toast.makeText(AddActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(AddActivity.this, "Fail!", Toast.LENGTH_SHORT).show();
                finish(); // close activity
            }
        });
    }
}
