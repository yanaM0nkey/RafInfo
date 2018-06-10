package com.yanazenkevich.rafinfo.interactions;

import com.yanazenkevich.rafinfo.base.UseCase;
import com.yanazenkevich.rafinfo.entities.Vacancy;
import com.yanazenkevich.rafinfo.net.RestService;

import io.reactivex.Observable;

public class VacancyEditUseCase extends UseCase<Vacancy, Vacancy> {

    @Override
    protected Observable<Vacancy> buildUseCase(Vacancy vacancy) {
        return RestService.getInstance().editVacancy(vacancy);
    }
}