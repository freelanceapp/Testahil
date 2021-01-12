package com.technology.circles.apps.testahil.services;


import com.technology.circles.apps.testahil.models.AppDataModel;
import com.technology.circles.apps.testahil.models.CategoryDataModel;
import com.technology.circles.apps.testahil.models.CityDataModel;
import com.technology.circles.apps.testahil.models.PlaceGeocodeData;
import com.technology.circles.apps.testahil.models.PlaceMapDetailsData;
import com.technology.circles.apps.testahil.models.ProductDataModel;
import com.technology.circles.apps.testahil.models.ProductModel;
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
    Call<CategoryDataModel> getCategories(@Header("Authorization") String token,
                                          @Header("lang") String lang,
                                          @Query("page") int page
    );

    @FormUrlEncoded
    @POST("api/all-products")
    Call<ProductDataModel> getProductByCategory(@Header("Authorization") String token,
                                                @Header("lang") String lang,
                                                @Field("category_id") String category_id,
                                                @Field("page") int page
    );


    @GET("api/all-cities")
    Call<CityDataModel> getCities(@Header("Authorization") String token,
                                  @Header("lang") String lang
    );

    @FormUrlEncoded
    @POST("api/my-products")
    Call<ProductDataModel> getMyFavoriteProduct(@Header("Authorization") String token,
                                                @Header("lang") String lang,
                                                @Field("action_type") String action_type
    );

    @GET("api/app/info")
    Call<AppDataModel> getAppInfo(@Header("Authorization") String token,
                                  @Header("lang") String lang
    );


    @FormUrlEncoded
    @POST("api/login")
    Call<UserModel> login(@Field("email") String email,
                          @Field("password") String password

    );

    @FormUrlEncoded
    @POST("api/register")
    Call<UserModel> signUp(@Field("name") String name,
                           @Field("email") String email,
                           @Field("phone") String phone,
                           @Field("password") String password,
                           @Field("software_type") String software_type
    );


    @FormUrlEncoded
    @POST("api/logout")
    Call<ResponseBody> logout(@Header("Authorization") String token,
                              @Field("user_id") int user_id
    );


    @FormUrlEncoded
    @POST("api/contactUs")
    Call<ResponseBody> contactUs(@Header("lang") String lang,
                                 @Header("Authorization") String token,
                                 @Field("name") String name,
                                 @Field("email") String email,
                                 @Field("phone") String phone,
                                 @Field("message") String message
    );

    @FormUrlEncoded
    @POST("api/productsByName")
    Call<ProductDataModel> searchByName(@Header("lang") String lang,
                                        @Header("Authorization") String token,
                                        @Field("product_name") String product_name

    );

    @FormUrlEncoded
    @POST("api/new-offer-suggestion")
    Call<ResponseBody> makeOffer(@Header("lang") String lang,
                                 @Header("Authorization") String token,
                                 @Field("work_department_name") String work_department_name,
                                 @Field("cat_id") String cat_id,
                                 @Field("manager_name") String manager_name,
                                 @Field("phone") String phone,
                                 @Field("email") String email
    );

    @FormUrlEncoded
    @POST("api/single-product")
    Call<ProductModel> singleProduct(@Header("lang") String lang,
                                     @Header("Authorization") String token,
                                     @Field("product_id") int product_id

    );

    @FormUrlEncoded
    @POST("api/product/love")
    Call<ResponseBody> like_dislike(@Header("lang") String lang,
                                    @Header("Authorization") String token,
                                    @Field("product_id") int product_id

    );


    @FormUrlEncoded
    @POST("api/products-filter")
    Call<ProductDataModel> filter(@Header("lang") String lang,
                                  @Header("Authorization") String token,
                                  @Field("special_filter") String special_filter,
                                  @Field("most_view_filter") String most_view_filter,
                                  @Field("distance_filter") String distance_filter,
                                  @Field("city_id") int city_id,
                                  @Field("category_filter") String category_filter,
                                  @Field("company_filter") String company_filter,
                                  @Field("discount_percentage_filter") String discount_percentage_filter

    );

    @FormUrlEncoded
    @POST("api/confirmCodeCheck")
    Call<UserModel> checkCode(@Field("user_id") int user_id,
                              @Field("code") String code
    );


    @FormUrlEncoded
    @POST("api/confirmCodeResend")
    Call<ResponseBody> reSendCode(@Field("user_id") int user_id
    );


    @FormUrlEncoded
    @POST("api/profile/update")
    Call<UserModel> updateProfile(@Header("Authorization") String token,
                                  @Field("name") String name,
                                  @Field("email") String email,
                                  @Field("phone") String phone
    );

}


