package com.yanazenkevich.rafinfo.net;

import com.yanazenkevich.rafinfo.entities.Announcement;
import com.yanazenkevich.rafinfo.entities.Department;
import com.yanazenkevich.rafinfo.entities.Staff;
import com.yanazenkevich.rafinfo.entities.User;
import com.yanazenkevich.rafinfo.entities.Vacancy;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface RestApi {

    @POST("users/login")
    Observable<User> logIn(@Body User user);

    @GET("data/Departments?sortBy=id%20asc")
    Observable<List<Department>> getDepartments();

    @POST("users/register")
    Observable<User> signUp(@Body User user);

    @GET("data/Announcements?sortBy=started%20desc")
    Observable<List<Announcement>> getAnnouncements();

    @GET("data/Vacancies?sortBy=updated%20desc%2Ccreated%20desc")
    Observable<List<Vacancy>> getVacancies();

    @GET("data/Users/{objectId}")
    Observable<User> getUserById(@Path("objectId") String id);

    @PUT("data/Users")
    Observable<User> saveUserById(@Body User user);

    @GET("users/logout")
    Observable<Response<Void>> logOut(@Header("user-token") String token);

    @GET("users/isvalidusertoken/{userToken}")
    Observable<Boolean> validate(@Path("userToken") String token);

    @GET
    Observable<List<Staff>> getStaffInfo(@Url String s);

    @GET
    Observable<List<Staff>> getStaffInfoBySurname(@Url String s);

    @POST("data/Announcements")
    Observable<Announcement> newAnnouncement(@Body Announcement announcement);
}