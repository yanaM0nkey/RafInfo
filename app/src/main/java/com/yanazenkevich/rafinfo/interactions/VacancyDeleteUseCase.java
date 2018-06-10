package com.yanazenkevich.rafinfo.interactions;

import com.yanazenkevich.rafinfo.base.UseCase;
import com.yanazenkevich.rafinfo.entities.Vacancy;
import com.yanazenkevich.rafinfo.net.RestService;

import io.reactivex.Observable;

public class VacancyDeleteUseCase extends UseCase<String, Vacancy> {

    @Override
    protected Observable<Vacancy> buildUseCase(String id) {
        return RestService.getInstance().deleteVacancy(id);
    }
}
