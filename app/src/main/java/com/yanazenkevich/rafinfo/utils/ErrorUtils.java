package com.yanazenkevich.rafinfo.utils;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Toast;

import com.yanazenkevich.rafinfo.R;
import com.yanazenkevich.rafinfo.entities.User;

import java.io.IOException;

import retrofit2.HttpException;

public class ErrorUtils {

    public static void errorHandling(Context context, Throwable e){
        if (e instanceof IOException) {
            Toast.makeText(context, context.getResources()
                    .getString(R.string.network_error), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, context.getResources()
                    .getString(R.string.error), Toast.LENGTH_LONG).show();
        }
    }

    public static void errorHandlingLogin(Context context, Throwable e){
        if(e instanceof HttpException){
            Toast.makeText(context, context.getResources()
                    .getString(R.string.log_in_invalid), Toast.LENGTH_LONG).show();
        }else {
            errorHandling(context, e);
        }
    }
}
