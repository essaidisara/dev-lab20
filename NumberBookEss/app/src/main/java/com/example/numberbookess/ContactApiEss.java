package com.example.numberbookess;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ContactApiEss {

    @POST("api/insertContact.php") // Vérifie bien le chemin vers ton insert
    Call<ApiResponseEss> insertContact(@retrofit2.http.Body ContactEss contact);

    @GET("api/searchContact.php")
    Call<List<ContactEss>> searchContacts(@Query("keyword") String keyword);

    // --- AJOUT : Supprimer un contact par son ID ---
    @GET("api/deleteContact.php")
    Call<ApiResponseEss> deleteContact(@Query("id") int idServer);

    // --- AJOUT : Mettre à jour un contact ---
    @FormUrlEncoded
    @POST("api/updateContact.php")
    Call<ApiResponseEss> updateContact(
            @Field("id") int id,
            @Field("name") String name,
            @Field("phone") String phone
    );
}