package com.yanazenkevich.rafinfo.net;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yanazenkevich.rafinfo.entities.Announcement;
import com.yanazenkevich.rafinfo.entities.Department;
import com.yanazenkevich.rafinfo.entities.Employer;
import com.yanazenkevich.rafinfo.entities.RequestRelation;
import com.yanazenkevich.rafinfo.entities.Staff;
import com.yanazenkevich.rafinfo.entities.User;
import com.yanazenkevich.rafinfo.entities.Vacancy;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestService {

    private static final RestService instance = new RestService();

    private RestApi restApi;

    private RestService(){
        init();
    }

    public static RestService getInstance() {
        return instance;
    }

    private void init() {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(20, TimeUnit.SECONDS)
                .connectTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build();

        Gson gson =  new GsonBuilder().create();

        final String BASE_URL = "https://api.backendless.com/35E9CDF6-DCB8-3846-FFD1-6CD10ECA4300/0A174A67-7D02-1B2C-FF7F-D37C07161400/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient).build();

        restApi = retrofit.create(RestApi.class);
    }

    public Observable<User> logIn(User user) {
        return restApi.logIn(user);
    }

    public Observable<List<Department>> getDepartments() {
        return restApi.getDepartments();
    }

    public Observable<User> signUp(User user) {
        return restApi.signUp(user);
    }

    public Observable<List<Announcement>> getAnnouncements() {
        return restApi.getAnnouncements();
    }

    public Observable<List<Vacancy>> getVacancies() {
        return restApi.getVacancies();
    }

    public Observable<User> getUserById(String id){
        return restApi.getUserById(id);
    }

    public Observable<User> saveUserById(User user){
        return restApi.saveUserById(user);
    }

    public Observable<Response<Void>> logOut(String token) {
        return restApi.logOut(token);
    }

    public Observable<Boolean> validate(String token) {
        return restApi.validate(token);
    }

    public Observable<List<Staff>> getStaffInfo(String s) {
        return restApi.getStaffInfo(s);
    }

    public Observable<List<Staff>> getStaffInfoBySurname(String s) {
        return restApi.getStaffInfoBySurname(s);
    }

    public Observable<Announcement> newAnnouncement (Announcement announcement) {
        return restApi.newAnnouncement(announcement);
    }

    public Observable<Announcement> editAnnouncement(Announcement announcement){
        return restApi.editAnnouncement(announcement.getId(), announcement);
    }

    public Observable<Announcement> deleteAnnouncement(String id){
        return restApi.deleteAnnouncement(id);
    }

    public Observable<Vacancy> newVacancy (Vacancy vacancy) {
        return restApi.newVacancy(vacancy);
    }

    public Observable<Integer> addRelation (RequestRelation mRelation) {
        return restApi.addRelation(mRelation.getObjectId(), mRelation.getmRelation());
    }

    public Observable<List<Employer>> getEmployers() {
        return restApi.getEmployers();
    }

    public Observable<Vacancy> editVacancy(Vacancy vacancy){
        return restApi.editVacancy(vacancy.getId(), vacancy);
    }

    public Observable<Vacancy> deleteVacancy(String id){
        return restApi.deleteVacancy(id);
    }

    public Observable<Staff> newStaff (Staff staff) {
        return restApi.newStaff(staff);
    }

    public Observable<Staff> editStaff(Staff staff){
        return restApi.editStaff(staff.getId(), staff);
    }

    public Observable<Staff> deleteStaff(String id){
        return restApi.deleteStaff(id);
    }
}
