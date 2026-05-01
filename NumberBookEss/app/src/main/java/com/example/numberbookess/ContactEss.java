package com.example.numberbookess;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "local_contacts")
public class ContactEss {

    @PrimaryKey(autoGenerate = true)
    private int id;

    // Fait le lien avec "id_ess" du PHP
    @SerializedName("id_ess")
    private int idServer;

    @SerializedName("ess_name")
    private String name;

    @SerializedName("ess_phone")
    private String phone;

    // 1. CONSTRUCTEUR VIDE (Obligatoire pour Room et Retrofit)
    public ContactEss() {}

    // 2. CONSTRUCTEUR AVEC ARGUMENTS (Obligatoire pour corriger ton erreur actuelle)
    @Ignore // On dit à Room d'utiliser le constructeur vide, mais MainActivity utilisera celui-ci
    public ContactEss(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    // --- GETTERS ET SETTERS ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdServer() { return idServer; }
    public void setIdServer(int idServer) { this.idServer = idServer; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}