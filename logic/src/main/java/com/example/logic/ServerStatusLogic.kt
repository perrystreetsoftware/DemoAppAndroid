package com.example.logic

import com.example.domainmodels.ServerStatus
import com.example.repositories.ServerStatusPushBasedRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

class ServerStatusLogic(private val repository: ServerStatusPushBasedRepository) {
    val status: Observable<ServerStatus> = repository.status

    fun reload(): Completable {
        return repository.reload()
    }
}