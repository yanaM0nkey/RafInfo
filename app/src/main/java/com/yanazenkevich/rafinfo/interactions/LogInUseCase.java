package com.yanazenkevich.rafinfo.interactions;

import com.yanazenkevich.rafinfo.base.UseCase;
import com.yanazenkevich.rafinfo.entities.User;
import com.yanazenkevich.rafinfo.net.RestService;

import io.reactivex.Observable;

import io.reactivex.functions.Consumer;



public class LogInUseCase extends UseCase<User, User> {

    private AuthService authService;

    public LogInUseCase(AuthService authService) {
        this.authService = authService;
    }

    @Override
    protected Observable<User> buildUseCase(User user) {
        return RestService.getInstance().logIn(user)
                .doOnNext(new Consumer<User>() {
                    @Override
                    public void accept(User user) throws Exception {
                        authService.saveAccessToken(user.getToken());
                        authService.saveObjectId(user.getId());
                        authService.saveDepartment(user.getDepartment());
                        authService.saveAdmin(user.isAdmin());
                    }
                });
    }
}

