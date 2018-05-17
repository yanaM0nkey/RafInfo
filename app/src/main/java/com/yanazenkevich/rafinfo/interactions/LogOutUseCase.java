package com.yanazenkevich.rafinfo.interactions;

import com.yanazenkevich.rafinfo.base.UseCase;
import com.yanazenkevich.rafinfo.net.RestService;

import io.reactivex.Observable;
import retrofit2.Response;


public class LogOutUseCase extends UseCase<String, Response<Void>> {
    @Override
    protected Observable<Response<Void>> buildUseCase(String token) {
        return RestService.getInstance().logOut(token);
    }
}
