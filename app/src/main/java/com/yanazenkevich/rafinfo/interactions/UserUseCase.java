package com.yanazenkevich.rafinfo.interactions;

import com.yanazenkevich.rafinfo.base.UseCase;
import com.yanazenkevich.rafinfo.entities.User;
import com.yanazenkevich.rafinfo.net.RestService;

import io.reactivex.Observable;

public class UserUseCase extends UseCase<String, User> {
    @Override
    protected Observable<User> buildUseCase(String id) {
        return RestService.getInstance().getUserById(id);
    }
}
