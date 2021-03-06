package com.yanazenkevich.rafinfo.net;

import com.yanazenkevich.rafinfo.entities.Announcement;
import com.yanazenkevich.rafinfo.entities.Department;
import com.yanazenkevich.rafinfo.entities.Employer;
import com.yanazenkevich.rafinfo.entities.Relation;
import com.yanazenkevich.rafinfo.entities.Staff;
import com.yanazenkevich.rafinfo.entities.User;
import com.yanazenkevich.rafinfo.entities.Vacancy;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
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

    @GET("data/Announcements?pageSize=100&sortBy=started%20desc")
    Observable<List<Announcement>> getAnnouncements();

    @GET("data/Vacancies?pageSize=100&sortBy=created%20desc%2Cupdated%20desc")
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

    @PUT("data/Announcements/{objectId}")
    Observable<Announcement> editAnnouncement(@Path("objectId") String id, @Body Announcement announcement);

    @DELETE("data/Announcements/{objectId}")
    Observable<Announcement> deleteAnnouncement(@Path("objectId") String id);

    @POST("data/Vacancies")
    Observable<Vacancy> newVacancy(@Body Vacancy vacancy);

    @POST("data/Vacancies/{objectId}/employer")
    Observable<Integer> addRelation(@Path("objectId") String id, @Body Relation relation);

    @GET("data/Employer")
    Observable<List<Employer>> getEmployers();

    @PUT("data/Vacancies/{objectId}")
    Observable<Vacancy> editVacancy(@Path("objectId") String id, @Body Vacancy vacancy);

    @DELETE("data/Vacancies/{objectId}")
    Observable<Vacancy> deleteVacancy(@Path("objectId") String id);

    @POST("data/StaffInfo")
    Observable<Staff> newStaff(@Body Staff staff);

    @PUT("data/StaffInfo/{objectId}")
    Observable<Staff> editStaff(@Path("objectId") String id, @Body Staff staff);

    @DELETE("data/StaffInfo/{objectId}")
    Observable<Staff> deleteStaff(@Path("objectId") String id);

    @POST("data/Employer")
    Observable<Employer> newEmployer(@Body Employer employer);

    @PUT("data/Employer/{objectId}")
    Observable<Employer> editEmployer(@Path("objectId") String id, @Body Employer employer);

    @DELETE("data/Employer/{objectId}")
    Observable<Employer> deleteEmployer(@Path("objectId") String id);

    @GET("users/restorepassword/{login}")
    Observable<Response<Void>> newPassword(@Path("login") String login);

}
