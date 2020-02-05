package com.technology.circles.apps.testahil.services;


import com.technology.circles.apps.testahil.models.AppDataModel;
import com.technology.circles.apps.testahil.models.CategoryDataModel;
import com.technology.circles.apps.testahil.models.CityDataModel;
import com.technology.circles.apps.testahil.models.PlaceGeocodeData;
import com.technology.circles.apps.testahil.models.PlaceMapDetailsData;
import com.technology.circles.apps.testahil.models.ProductDataModel;
import com.technology.circles.apps.testahil.models.UserModel;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Service {

    @GET("place/findplacefromtext/json")
    Call<PlaceMapDetailsData> searchOnMap(@Query(value = "inputtype") String inputtype,
                                          @Query(value = "input") String input,
                                          @Query(value = "fields") String fields,
                                          @Query(value = "language") String language,
                                          @Query(value = "key") String key
    );

    @GET("geocode/json")
    Call<PlaceGeocodeData> getGeoData(@Query(value = "latlng") String latlng,
                                      @Query(value = "language") String language,
                                      @Query(value = "key") String key);


    @GET("api/all-categories")
    Call<CategoryDataModel> getCategories(@Header("Authorization") String toke,
                                          @Header("lang") String lang
    );

    @FormUrlEncoded
    @POST("api/all-products")
    Call<ProductDataModel> getProductByCategory(@Header("Authorization") String toke,
                                                @Header("lang") String lang,
                                                @Field("category_id") int category_id
    );


    @GET("api/all-cities")
    Call<CityDataModel> getCities(@Header("Authorization") String toke,
                                  @Header("lang") String lang
    );

    @FormUrlEncoded
    @POST("api/my-products")
    Call<ProductDataModel> getMyFavoriteProduct(@Header("Authorization") String toke,
                                                @Header("lang") String lang,
                                                @Field("action_type") String action_type
    );

    @GET("api/app/info")
    Call<AppDataModel> getAppInfo(@Header("Authorization") String toke,
                                  @Header("lang") String lang
    );


    @FormUrlEncoded
    @POST("api/login")
    Call<UserModel> login(@Field("email") String email,
                          @Field("password") String password

    );


    @FormUrlEncoded
    @POST("api/logout")
    Call<ResponseBody> logout(@Header("Authorization") String toke,
                              @Field("user_id") int user_id
    );


}


