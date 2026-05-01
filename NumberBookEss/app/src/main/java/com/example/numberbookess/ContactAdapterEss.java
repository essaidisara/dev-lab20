package com.example.numberbookess;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast; // IMPORT AJOUTÉ POUR LE TOAST DE DEBUG
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

/**
 * Adapter pour la liste des contacts avec navigation vers les détails
 */
public class ContactAdapterEss extends RecyclerView.Adapter<ContactAdapterEss.ContactViewHolder> {

    private List<ContactEss> contacts;

    public ContactAdapterEss(List<ContactEss> contacts) {
        this.contacts = contacts;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_2, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        ContactEss contact = contacts.get(position);
        holder.tvName.setText(contact.getName());
        holder.tvPhone.setText(contact.getPhone());

        // NOUVEAU BLOC AVEC LE DEBUG TOAST
        holder.itemView.setOnClickListener(v -> {
            int idAAfficher = contact.getIdServer();

            // DEBUG : Si ce Toast affiche 0, c'est que la base locale est corrompue
            Toast.makeText(v.getContext(), "Envoi de l'ID : " + idAAfficher, Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(v.getContext(), ContactDetailActivity.class);
            intent.putExtra("contact_name", contact.getName());
            intent.putExtra("contact_phone", contact.getPhone());
            intent.putExtra("contact_id_server", idAAfficher);

            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return contacts != null ? contacts.size() : 0;
    }

    public void updateData(List<ContactEss> newContacts) {
        this.contacts = newContacts;
        notifyDataSetChanged();
    }

    static class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPhone;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(android.R.id.text1);
            tvPhone = itemView.findViewById(android.R.id.text2);
        }
    }
}