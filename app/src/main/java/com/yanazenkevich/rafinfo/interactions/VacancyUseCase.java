package com.yanazenkevich.rafinfo.interactions;

import com.yanazenkevich.rafinfo.base.UseCase;
import com.yanazenkevich.rafinfo.entities.Vacancy;
import com.yanazenkevich.rafinfo.net.RestService;

import java.util.List;

import io.reactivex.Observable;

public class VacancyUseCase extends UseCase<Void, List<Vacancy>> {
    @Override
    protected Observable<List<Vacancy>> buildUseCase(Void param) {
        return RestService.getInstance().getVacancies();
    }
}
