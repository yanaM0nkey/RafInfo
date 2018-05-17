package com.yanazenkevich.rafinfo.interactions;

import com.yanazenkevich.rafinfo.base.UseCase;
import com.yanazenkevich.rafinfo.entities.Announcement;
import com.yanazenkevich.rafinfo.net.RestService;

import java.util.List;

import io.reactivex.Observable;

public class AnnouncementUseCase extends UseCase<Void, List<Announcement>> {
    @Override
    protected Observable<List<Announcement>> buildUseCase(Void param) {
        return RestService.getInstance().getAnnouncements();
    }
}
