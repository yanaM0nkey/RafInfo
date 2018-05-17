package com.yanazenkevich.rafinfo.interactions;

import com.yanazenkevich.rafinfo.base.UseCase;
import com.yanazenkevich.rafinfo.entities.Department;
import com.yanazenkevich.rafinfo.net.RestService;

import java.util.List;

import io.reactivex.Observable;

public class DepartmentUseCase extends UseCase<Void, List<Department>> {
    @Override
    protected Observable<List<Department>> buildUseCase(Void param) {
        return RestService.getInstance().getDepartments();
    }
}
