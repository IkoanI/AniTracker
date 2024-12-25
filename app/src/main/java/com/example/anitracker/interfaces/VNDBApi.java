package com.example.anitracker.interfaces;

import com.example.anitracker.vnObjects.VNCharPage;
import com.example.anitracker.vnObjects.VNRequestBody;
import com.example.anitracker.vnObjects.VNPage;
import com.example.anitracker.vnObjects.VNResponse;
import com.example.anitracker.vnObjects.VNStaff;
import com.example.anitracker.vnObjects.VNStaffPage;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface VNDBApi {
    @Headers({"Content-Type: application/json"})
    @POST("vn")
    Call<VNPage> fetchVNPage(@Body VNRequestBody body);

    @Headers({"Content-Type: application/json"})
    @POST("character")
    Call<VNCharPage> fetchVNChars(@Body VNRequestBody body);

    @Headers({"Content-Type: application/json"})
    @POST("staff")
    Call<VNStaffPage> fetchVNStaffs(@Body VNRequestBody body);
}
