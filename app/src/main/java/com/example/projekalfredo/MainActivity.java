package com.example.projekalfredo;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.example.projekalfredo.R;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    EditText editTextName, editTextHarga, editTextDeskripsi;
    ListView listView;
    ArrayAdapter<String> adapter;
    List<DataModel> contactList = new ArrayList<>();
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextName = findViewById(R.id.editTextName);
        editTextHarga = findViewById(R.id.editTextHarga);
        editTextDeskripsi = findViewById(R.id.editTextDeskripsi);
        db = new DatabaseHelper(this);

        findViewById(R.id.buttonAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString();
                String harga = editTextHarga.getText().toString();
                String deskripsi = editTextDeskripsi.getText().toString();

                if (!name.isEmpty()) {
                    DataModel newData = new DataModel(name, harga, deskripsi);
                    db.addContact(newData);
                    updateListView();
                    editTextName.setText("");
                    editTextHarga.setText("");
                    editTextDeskripsi.setText("");
                } else {
                    Toast.makeText(MainActivity.this, "Menu tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        listView = findViewById(R.id.list_view);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showEditOrDeleteDialog(position);
                return true;
            }
        });

        updateListView();
    }

    private void showEditOrDeleteDialog(final int position) {
        final DataModel contact = contactList.get(position);

        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Pilih Aksi")
                .setItems(new CharSequence[]{"Ubah", "Detail", "Hapus"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                showEditDialog(contact);
                                break;
                            case 1:
                                showMenuDetail(contact);
                                break;
                            case 2:
                                deleteContact(contact);
                                break;
                        }
                    }
                })
                .show();
    }

    private void showEditDialog(final DataModel contact) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Ubah Menu");

        View viewInflated = LayoutInflater.from(this).inflate(R.layout.activity_main, null);

        final EditText inputName = viewInflated.findViewById(R.id.editTextName);
        final EditText inputHarga = viewInflated.findViewById(R.id.editTextHarga);
        final EditText inputDeskripsi = viewInflated.findViewById(R.id.editTextDeskripsi);

        inputName.setText(contact.getName());
        inputHarga.setText(contact.getHarga());
        inputDeskripsi.setText(contact.getDeskripsi());

        builder.setView(viewInflated);

        builder.setPositiveButton("Ubah", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newName = inputName.getText().toString().trim();
                String newHarga = inputHarga.getText().toString().trim();
                String newDeskripsi = inputDeskripsi.getText().toString().trim();

                if (!newName.isEmpty()) {
                    contact.setName(newName);
                    contact.setHarga(newHarga);
                    contact.setDeskripsi(newDeskripsi);
                    db.updateContact(contact);
                    updateListView(); // Ensure ListView is updated after editing a menu
                    Toast.makeText(MainActivity.this, "Menu berhasil diubah", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Menu tidak boleh kosong", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        

        builder.create().show();
    }

    private void deleteContact(final DataModel contact) {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Konfirmasi")
                .setMessage("Apakah Anda ingin menghapus menu ini?")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.deleteContact(contact);
                        updateListView();
                        Toast.makeText(MainActivity.this, "Menu berhasil dihapus", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Tidak", null)
                .show();
    }

    private void showMenuDetail(final DataModel contact) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Detail Menu");

        String detail = "Nama: " + contact.getName() + "\n" +
                "Harga: " + contact.getHarga() + "\n" +
                "Deskripsi: " + contact.getDeskripsi();

        builder.setMessage(detail);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }

    private void updateListView() {
        contactList.clear();
        contactList.addAll(db.getAllContacts());

        List<String> details = new ArrayList<>();
        for (DataModel contact : contactList) {
            String detail = contact.getName() + "\n" +
                    "Harga: " + contact.getHarga() + "\n" +
                    "Deskripsi: " + contact.getDeskripsi();
            details.add(detail);
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, details);
        listView.setAdapter(adapter);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main_menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()){
//            case R.id.menu_logout:
//                showLogoutDialog();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
//
//    private void showLogoutDialog(){
//        new AlertDialog.Builder(this)
//                .setTitle("Logout")
//                .setMessage("Apakah anda yakin ingin keluar?")
//                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        performLogout();
//                    }
//                })
//                .setNegativeButton("Tidak", null)
//                .show();
//    }
//
//    private void performLogout() {
//
//        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//        startActivity(intent);
//        finish();
//    }
}
