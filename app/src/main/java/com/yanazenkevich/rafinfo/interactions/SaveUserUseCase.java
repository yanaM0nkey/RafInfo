package com.yanazenkevich.rafinfo.interactions;

import android.content.Context;

import com.yanazenkevich.rafinfo.base.UseCase;
import com.yanazenkevich.rafinfo.entities.User;
import com.yanazenkevich.rafinfo.net.RestService;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

public class SaveUserUseCase extends UseCase<User, User> {

    private AuthService authService;

    public SaveUserUseCase(AuthService authService) {
        this.authService = authService;
    }

    @Override
    protected Observable<User> buildUseCase(User user) {
        return RestService.getInstance().saveUserById(user)
                .doOnNext(new Consumer<User>() {
                    @Override
                    public void accept(User user) throws Exception {
                        authService.saveObjectId(user.getId());
                        authService.saveDepartment(user.getDepartment());
                    }
                });
    }
}
