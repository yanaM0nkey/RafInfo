package com.yanazenkevich.rafinfo.interactions;

import com.yanazenkevich.rafinfo.base.UseCase;
import com.yanazenkevich.rafinfo.entities.RequestRelation;
import com.yanazenkevich.rafinfo.net.RestService;

import io.reactivex.Observable;

public class VacancyRelationUseCase extends UseCase<RequestRelation, Integer> {

    @Override
    protected Observable<Integer> buildUseCase(RequestRelation requestRelation) {
        return RestService.getInstance().addRelation(requestRelation);
    }
}
