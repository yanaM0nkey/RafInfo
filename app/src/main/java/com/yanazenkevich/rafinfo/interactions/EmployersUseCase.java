package com.yanazenkevich.rafinfo.interactions;

import com.yanazenkevich.rafinfo.base.UseCase;
import com.yanazenkevich.rafinfo.entities.Employer;
import com.yanazenkevich.rafinfo.net.RestService;

import java.util.List;

import io.reactivex.Observable;

public class EmployersUseCase extends UseCase<Void, List<Employer>> {

    @Override
    protected Observable<List<Employer>> buildUseCase(Void param) {
        return RestService.getInstance().getEmployers();
    }
}
