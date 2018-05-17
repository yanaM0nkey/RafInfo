package com.yanazenkevich.rafinfo.interactions;

import com.yanazenkevich.rafinfo.base.UseCase;
import com.yanazenkevich.rafinfo.net.RestService;

import io.reactivex.Observable;

public class ValidateUseCase extends UseCase<String, Boolean> {
    @Override
    protected Observable<Boolean> buildUseCase(String token) {
        return RestService.getInstance().validate(token);
    }
}
