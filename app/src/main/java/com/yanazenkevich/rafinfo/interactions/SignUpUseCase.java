package com.yanazenkevich.rafinfo.interactions;

import com.yanazenkevich.rafinfo.base.UseCase;
import com.yanazenkevich.rafinfo.entities.User;
import com.yanazenkevich.rafinfo.net.RestService;

import io.reactivex.Observable;

public class SignUpUseCase extends UseCase<User, User> {

    @Override
    protected Observable<User> buildUseCase(User user) {
        return RestService.getInstance().signUp(user);
    }
}