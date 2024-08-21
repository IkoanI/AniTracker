package com.example.anitracker.interfaces;

import com.example.anitracker.vnObjects.VNCharPage;
import com.example.anitracker.vnObjects.VNRequestBody;
import com.example.anitracker.vnObjects.VNPage;
import com.example.anitracker.vnObjects.VNResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface VNDBApi {
    @Headers({"Content-Type: application/json"})
    @POST("vn")
    Call<VNPage> fetchVNPage(@Body VNRequestBody body);

    @Headers({"Content-Type: application/json"})
    @POST("vn")
    Call<VNPage> fetchVNDetails(@Body VNRequestBody body);

    @Headers({"Content-Type: application/json"})
    @POST("vn")
    Call<VNPage> fetchVNChars(@Body VNRequestBody body);

    @Headers({"Content-Type: application/json"})
    @POST("character")
    Call<VNCharPage> fetchVNCharsTest(@Body VNRequestBody body);
}
