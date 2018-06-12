package com.yanazenkevich.rafinfo.interactions;

import com.yanazenkevich.rafinfo.base.UseCase;
import com.yanazenkevich.rafinfo.net.RestService;

import io.reactivex.Observable;
import retrofit2.Response;

public class NewPasswordUseCase extends UseCase<String, Response<Void>> {

    @Override
    protected Observable<Response<Void>> buildUseCase(String login) {
        return RestService.getInstance().newPassword(login);
    }
}
