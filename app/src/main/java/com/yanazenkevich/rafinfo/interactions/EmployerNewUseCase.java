package com.yanazenkevich.rafinfo.interactions;

import com.yanazenkevich.rafinfo.base.UseCase;
import com.yanazenkevich.rafinfo.entities.Employer;
import com.yanazenkevich.rafinfo.net.RestService;

import io.reactivex.Observable;

public class EmployerNewUseCase extends UseCase<Employer, Employer> {

    @Override
    protected Observable<Employer> buildUseCase(Employer employer) {
        return RestService.getInstance().newEmployer(employer);
    }
}
