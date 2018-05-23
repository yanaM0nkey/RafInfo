package com.yanazenkevich.rafinfo.interactions;

import com.yanazenkevich.rafinfo.base.UseCase;
import com.yanazenkevich.rafinfo.entities.Relation;
import com.yanazenkevich.rafinfo.net.RestService;

import io.reactivex.Observable;

public class VacancyRelationUseCase extends UseCase<Relation, Integer> {

    @Override
    protected Observable<Integer> buildUseCase(String objectId, Relation relation) {
        return RestService.getInstance().addRelation(objectId, relation);
    }

    @Override
    protected Observable<Integer> buildUseCase(Relation relation) {
        return null;
    }
}
