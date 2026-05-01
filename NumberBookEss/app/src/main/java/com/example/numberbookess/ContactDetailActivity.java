package com.example.numberbookess;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactDetailActivity extends AppCompatActivity {

    private TextView tvName, tvPhone;
    private Button btnUpdate, btnDelete;
    private int contactIdServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);

        tvName = findViewById(R.id.detailName);
        tvPhone = findViewById(R.id.detailPhone);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);

        // Récupération des données passées par l'Adapter
        String name = getIntent().getStringExtra("contact_name");
        String phone = getIntent().getStringExtra("contact_phone");

        // CORRECTION : On récupère l'ID. S'il n'existe pas, il vaudra -1.
        contactIdServer = getIntent().getIntExtra("contact_id_server", -1);

        tvName.setText(name);
        tvPhone.setText(phone);

        // --- BOUTON SUPPRIMER ---
        btnDelete.setOnClickListener(v -> {
            if (contactIdServer > 0) {
                // On affiche une confirmation avant de supprimer
                new AlertDialog.Builder(this)
                        .setTitle("Suppression")
                        .setMessage("Voulez-vous supprimer ce contact du serveur ?")
                        .setPositiveButton("Oui", (dialog, which) -> deleteOnServer(contactIdServer))
                        .setNegativeButton("Non", null)
                        .show();
            } else {
                // Si l'ID est toujours 0 ou -1, c'est ici que l'erreur s'affiche
                Toast.makeText(this, "Erreur ID serveur: " + contactIdServer, Toast.LENGTH_SHORT).show();
            }
        });

        // --- BOUTON MODIFIER ---
        btnUpdate.setOnClickListener(v -> showUpdateDialog(tvName.getText().toString(), tvPhone.getText().toString()));
    }

    private void deleteOnServer(int id) {
        ContactApiEss api = RetrofitClientEss.getClient().create(ContactApiEss.class);
        api.deleteContact(id).enqueue(new Callback<ApiResponseEss>() {
            @Override
            public void onResponse(Call<ApiResponseEss> call, Response<ApiResponseEss> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ContactDetailActivity.this, "Supprimé du serveur !", Toast.LENGTH_SHORT).show();
                    finish(); // Ferme l'activité et revient à la liste
                } else {
                    Toast.makeText(ContactDetailActivity.this, "Erreur suppression", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponseEss> call, Throwable t) {
                Toast.makeText(ContactDetailActivity.this, "Erreur réseau : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showUpdateDialog(String oldName, String oldPhone) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Modifier le contact");

        final EditText inputName = new EditText(this);
        inputName.setText(oldName);
        final EditText inputPhone = new EditText(this);
        inputPhone.setText(oldPhone);

        android.widget.LinearLayout layout = new android.widget.LinearLayout(this);
        layout.setOrientation(android.widget.LinearLayout.VERTICAL);
        layout.setPadding(40, 20, 40, 20);
        layout.addView(inputName);
        layout.addView(inputPhone);
        builder.setView(layout);

        builder.setPositiveButton("Enregistrer", (dialog, which) -> {
            String newName = inputName.getText().toString();
            String newPhone = inputPhone.getText().toString();
            updateOnServer(contactIdServer, newName, newPhone);
        });
        builder.setNegativeButton("Annuler", null);
        builder.show();
    }

    private void updateOnServer(int id, String n, String p) {
        ContactApiEss api = RetrofitClientEss.getClient().create(ContactApiEss.class);
        api.updateContact(id, n, p).enqueue(new Callback<ApiResponseEss>() {
            @Override
            public void onResponse(Call<ApiResponseEss> call, Response<ApiResponseEss> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ContactDetailActivity.this, "Mis à jour !", Toast.LENGTH_SHORT).show();
                    tvName.setText(n);
                    tvPhone.setText(p);
                } else {
                    Toast.makeText(ContactDetailActivity.this, "Échec de la mise à jour", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponseEss> call, Throwable t) {
                Toast.makeText(ContactDetailActivity.this, "Erreur réseau", Toast.LENGTH_SHORT).show();
            }
        });
    }
}