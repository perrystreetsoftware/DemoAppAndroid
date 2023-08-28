package com.example.viewmodels.spec

import io.kotest.core.listeners.BeforeSpecListener
import io.kotest.core.spec.Spec
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.Schedulers

class RxSchedulersSpecExtension : BeforeSpecListener {

    override suspend fun beforeSpec(spec: Spec) {
        super.beforeSpec(spec)

        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setNewThreadSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setSingleSchedulerHandler { Schedulers.trampoline() }
    }
}
