package com.yanazenkevich.rafinfo.interactions;

import com.yanazenkevich.rafinfo.base.UseCase;
import com.yanazenkevich.rafinfo.entities.Staff;
import com.yanazenkevich.rafinfo.net.RestService;

import java.util.List;

import io.reactivex.Observable;

public class StaffInfoUseCase extends UseCase<String, List<Staff>> {

    @Override
    protected Observable<List<Staff>> buildUseCase(String department) {
        return RestService.getInstance().getStaffInfo(generateLink(String.valueOf(department)));
    }

    private String generateLink(String department) {
        return "data/StaffInfo?pageSize=100&where=department" + "%3D" +
                department +
                "&sortBy=created%20asc";
    }
}

