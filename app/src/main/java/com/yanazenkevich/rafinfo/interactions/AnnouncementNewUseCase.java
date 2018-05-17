package com.yanazenkevich.rafinfo.interactions;

import com.yanazenkevich.rafinfo.base.UseCase;
import com.yanazenkevich.rafinfo.entities.Announcement;
import com.yanazenkevich.rafinfo.net.RestService;

import io.reactivex.Observable;

public class AnnouncementNewUseCase extends UseCase<Announcement, Announcement> {

    @Override
    protected Observable<Announcement> buildUseCase(Announcement announcement) {
        return RestService.getInstance().newAnnouncement(announcement);
    }
}
