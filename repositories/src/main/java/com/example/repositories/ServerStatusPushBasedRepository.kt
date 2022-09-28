package com.example.repositories

import com.example.domainmodels.ServerStatus
import com.example.interfaces.ITravelAdvisoriesApi
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject

class ServerStatusPushBasedRepository(private val travelAdvisoriesApi: ITravelAdvisoriesApi) {
    private var _status: BehaviorSubject<ServerStatus> = BehaviorSubject.createDefault(ServerStatus.EMPTY)
    val status: Observable<ServerStatus> = _status

    fun reload(): Completable {
        return travelAdvisoriesApi.getServerStatus()
            .doOnNext {
                _status.onNext(ServerStatus(it.ok))
            }
            .ignoreElements()
    }
}