package com.yanazenkevich.rafinfo.interactions;

import com.yanazenkevich.rafinfo.base.UseCase;
import com.yanazenkevich.rafinfo.entities.Employer;
import com.yanazenkevich.rafinfo.net.RestService;

import io.reactivex.Observable;

public class EmployerDeleteUseCase extends UseCase<String, Employer> {

    @Override
    protected Observable<Employer> buildUseCase(String id) {
        return RestService.getInstance().deleteEmployer(id);
    }
}
