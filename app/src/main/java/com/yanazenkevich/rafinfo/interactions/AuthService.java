package com.yanazenkevich.rafinfo.interactions;


import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.yanazenkevich.rafinfo.entities.AuthState;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public class AuthService {

    public static final String KEY_ACCESS_TOKEN = "accessToken";
    public static final String SHARED_PREFS_NAME = "sharedPrefs";
    public static final String KEY_OBJECT_ID = "objectId";
    public static final String KEY_DEPARTMENT = "department";
    public static final String KEY_ADMIN = "admin";
    private Context context;

    private BehaviorSubject<AuthState> state = BehaviorSubject.createDefault(new AuthState(false));

    public AuthService(Context context) {
        this.context = context;
        restoreAccessToken();
    }

    public Observable<AuthState> observeState(){
        return state;
    }

    public void saveAccessToken(String accessToken){
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        preferences.edit().putString(KEY_ACCESS_TOKEN, accessToken).apply();
        state.onNext(new AuthState(true));
    }

    public void removeAccessToken(){
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        preferences.edit().remove(KEY_ACCESS_TOKEN).apply();
        state.onNext(new AuthState(false));
    }

    private void restoreAccessToken(){
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        String token = preferences.getString(KEY_ACCESS_TOKEN, null);
        if(!TextUtils.isEmpty(token)){
            state.onNext(new AuthState(true));
        }else{
            state.onNext(new AuthState(false));
        }
    }

    public void saveObjectId(String objectId){
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        preferences.edit().putString(KEY_OBJECT_ID, objectId).apply();
    }

    public void removeObjectId(){
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        preferences.edit().remove(KEY_OBJECT_ID).apply();
    }

    public void saveDepartment(int department){
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        preferences.edit().putInt(KEY_DEPARTMENT, department).apply();
    }

    public void removeDepartment(){
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        preferences.edit().remove(KEY_DEPARTMENT).apply();
    }

    public void saveAdmin(boolean isAdmin){
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        preferences.edit().putBoolean(KEY_ADMIN, isAdmin).apply();
    }

    public void removeAdmin(){
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        preferences.edit().remove(KEY_ADMIN).apply();
    }
}
