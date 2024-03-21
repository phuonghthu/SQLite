package com.K214111950;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.K214111950.databinding.ActivityMainBinding;
import com.K214111950.model.Product;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    public static final String DB_NAME = "product_db.db";
    public static final String DB_FOLDER = "/databases";

    public static SQLiteDatabase db = null;

    public static final String TBL_NAME = "Product";
    public static final String COL_ID = "ProductId";
    public static final String COL_NAME = "ProductName";
    public static final String COL_PRICE = "ProductPrice";
    //    private File DbFile;
    ArrayAdapter<Product> adapter;

    Product selectedProduct = null; // toàn cục product

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        copyDb();
        initAdapter();

        registerForContextMenu(binding.lvProduct); //
        addEvents();


    }

    private void addEvents() {
        binding.lvProduct.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                selectedProduct = adapter.getItem(position);
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDb();
    }

    private void initAdapter() {
        adapter = new ArrayAdapter<Product>(MainActivity.this, android.R.layout.simple_list_item_1);
        binding.lvProduct.setAdapter(adapter);
    }

    private void loadDb() {
        db = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);
//        Cursor cursor = db.rawQuery("SELECT * FROM " + TBL_NAME, null);
//        Cursor cursor = db.rawQuery("SELECT * FROM " + TBL_NAME + " WHERE " + COL_ID + ">?", new String[]{"2"});
        //SELECT * FROM Product WHERE ProductId > 2
//
        //SELECT * FROM Product WHERE
        Cursor cursor = db.query(TBL_NAME, null, null,null, null, null, null);

        // Reset data
        adapter.clear();
        Product p;
        while (cursor.moveToNext()) {
            p = new Product(cursor.getInt(0), cursor.getString((1)), cursor.getDouble(2));

            adapter.add(p);
        }
    }

    @SuppressLint("SuspiciousIndentation")
    private void copyDb() {
        File dbFile = getDatabasePath(DB_NAME);
        if (!dbFile.exists()) {
            if (copyDbFromAssets())
                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
                //copy
            else
                Toast.makeText(this, "Fail", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean copyDbFromAssets() {
        String dbPath = getApplicationInfo().dataDir + "/" + DB_FOLDER + "/" + DB_NAME;
        //truy xuất vo thư mục data/data/pakageName/databases/product_db.db
        try {
            InputStream inputStream = getAssets().open(DB_NAME);
            File f = new File(getApplicationInfo().dataDir + "/" + DB_FOLDER + "/");
            if (!f.exists()) {
                f.mkdir();
            }
            OutputStream outputStream = new FileOutputStream(dbPath);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }
    // ======== Menu ======
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.mnAdd) {
            //opening
            Intent intent = new Intent(MainActivity.this, AddActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.context_menu, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.mnEdit) {
            // mở màn hình edit
            Intent intent = new Intent(MainActivity.this, EditActivity.class);
            // đính kèm dữ lieuej đang được chọn gửi đi

            if(selectedProduct != null) {
                intent.putExtra("data", selectedProduct);
                startActivity(intent);
            }
        }
        if (item.getItemId() == R.id.mnDelete){


            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Xác nhận xóa!");
            builder.setIcon(android.R.drawable.ic_input_delete);
            builder.setMessage("Bạn thật sự muốn xóa " +selectedProduct.getProductName() + "?");

            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // xóa sản phẩm
                    int deletedRows = MainActivity.db.delete(TBL_NAME, COL_ID + "=?", new String[]{String.valueOf(selectedProduct.getProductCode())});
                    if (deletedRows > 0) {
                        Toast.makeText(MainActivity.this, "Sucess!", Toast.LENGTH_SHORT).show();
                        loadDb();
                    }

                    else
                        Toast.makeText(MainActivity.this, "Fail!", Toast.LENGTH_SHORT).show();
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });

            Dialog dialog = builder.create();
            dialog.show(); //hiển thị dialog




        }
        return super.onContextItemSelected(item);
    }
}