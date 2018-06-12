package com.yanazenkevich.rafinfo.interactions;

import com.yanazenkevich.rafinfo.base.UseCase;
import com.yanazenkevich.rafinfo.entities.Staff;
import com.yanazenkevich.rafinfo.net.RestService;

import io.reactivex.Observable;

public class StaffDeleteUseCase extends UseCase<String, Staff> {

    @Override
    protected Observable<Staff> buildUseCase(String id) {
        return RestService.getInstance().deleteStaff(id);
    }
}
