package com.yanazenkevich.rafinfo.interactions;

import com.yanazenkevich.rafinfo.base.UseCase;
import com.yanazenkevich.rafinfo.entities.Announcement;
import com.yanazenkevich.rafinfo.net.RestService;

import io.reactivex.Observable;

public class AnnouncementDeleteUseCase extends UseCase<String, Announcement> {

    @Override
    protected Observable<Announcement> buildUseCase(String id) {
        return RestService.getInstance().deleteAnnouncement(id);
    }
}

