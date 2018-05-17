package com.yanazenkevich.rafinfo.interactions;

import com.yanazenkevich.rafinfo.base.UseCase;
import com.yanazenkevich.rafinfo.entities.Staff;
import com.yanazenkevich.rafinfo.net.RestService;

import java.util.List;

import io.reactivex.Observable;

public class StaffInfoBySurnameUseCase extends UseCase<String, List<Staff>> {

    @Override
    protected Observable<List<Staff>> buildUseCase(String surname) {
        return RestService.getInstance().getStaffInfoBySurname(generateLink(String.valueOf(surname)));
    }

    private String generateLink(String surname) {
        return "data/StaffInfo?where=surname%3D'" +
                surname +
                "'";
    }
}
