package com.example.konsist.interfaces

import com.example.konsist.KonsistUtils.interfacesProduction
import com.lemonappdev.konsist.api.ext.list.withoutName
import com.lemonappdev.konsist.api.verify.assertTrue
import io.kotest.core.spec.style.BehaviorSpec

class StartsWithI : BehaviorSpec() {

    init {
        Given("An interface") {
            val interfaces = interfacesProduction.withoutName(*baseline)

            Then("It starts with I") {
                interfaces.assertTrue {
                    it.hasNameStartingWith("I")
                }
            }
        }
    }

    private companion object {
        private val baseline = arrayOf(
            "CustomErrorStateFactory"
        )
    }
}
