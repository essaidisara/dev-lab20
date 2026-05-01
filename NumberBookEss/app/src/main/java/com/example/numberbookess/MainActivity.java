package com.example.numberbookess;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log; // FIX : Importation nécessaire pour Log
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private EditText essEtKeyword;
    private ContactAdapterEss adapter;
    private final List<ContactEss> contactListLocal = new ArrayList<>();
    private ContactApiEss contactApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        essEtKeyword = findViewById(R.id.ess_et_keyword);
        RecyclerView rv = findViewById(R.id.ess_recycler_view);
        Button btnLoad = findViewById(R.id.ess_btn_load);
        Button btnSync = findViewById(R.id.ess_btn_sync);
        Button btnSearch = findViewById(R.id.ess_btn_search);

        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ContactAdapterEss(new ArrayList<>());
        rv.setAdapter(adapter);

        contactApi = RetrofitClientEss.getClient().create(ContactApiEss.class);

        btnLoad.setOnClickListener(v -> checkPermission());
        btnSync.setOnClickListener(v -> syncToServer());
        btnSearch.setOnClickListener(v -> searchOnServer());

        essEtKeyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().isEmpty()) {
                    adapter.updateData(contactListLocal);
                }
            }
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            loadLocalContacts();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 100);
        }
    }

    private void loadLocalContacts() {
        contactListLocal.clear();
        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String n = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String p = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
                // Utilise maintenant le constructeur (String, String) corrigé
                contactListLocal.add(new ContactEss(n, p));
            }
            cursor.close();
        }
        adapter.updateData(contactListLocal);
    }

    private void syncToServer() {
        if (contactListLocal.isEmpty()) {
            Toast.makeText(this, "Chargez d'abord les contacts", Toast.LENGTH_SHORT).show();
            return;
        }
        for (ContactEss c : contactListLocal) {
            contactApi.insertContact(c).enqueue(new Callback<ApiResponseEss>() {
                @Override
                public void onResponse(Call<ApiResponseEss> call, Response<ApiResponseEss> response) {
                    if (response.isSuccessful()) {
                        Log.d("SYNC", "Contact synchronisé : " + c.getName());
                    }
                }
                @Override
                public void onFailure(Call<ApiResponseEss> call, Throwable t) {
                    Log.e("SYNC_ERROR", t.getMessage());
                }
            });
        }
        Toast.makeText(this, "Synchronisation lancée vers MySQL...", Toast.LENGTH_SHORT).show();
    }

    private void searchOnServer() {
        String kw = essEtKeyword.getText().toString().trim();
        if (kw.isEmpty()) {
            adapter.updateData(contactListLocal);
            return;
        }

        contactApi.searchContacts(kw).enqueue(new Callback<List<ContactEss>>() {
            @Override
            public void onResponse(Call<List<ContactEss>> call, Response<List<ContactEss>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter.updateData(response.body());
                }
            }
            @Override
            public void onFailure(Call<List<ContactEss>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Erreur serveur", Toast.LENGTH_SHORT).show();
            }
        });
    }
}