package com.example.fotovideo;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiServices {

    @GET("posts")
    Call<List<PlaceholderPost>> getAllPosts();
}
