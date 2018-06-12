package com.yanazenkevich.rafinfo.interactions;

import com.yanazenkevich.rafinfo.base.UseCase;
import com.yanazenkevich.rafinfo.entities.Staff;
import com.yanazenkevich.rafinfo.net.RestService;

import io.reactivex.Observable;

public class StaffNewUseCase extends UseCase<Staff, Staff> {

    @Override
    protected Observable<Staff> buildUseCase(Staff staff) {
        return RestService.getInstance().newStaff(staff);
    }
}
