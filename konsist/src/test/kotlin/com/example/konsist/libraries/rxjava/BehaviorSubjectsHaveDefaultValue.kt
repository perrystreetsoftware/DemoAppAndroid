package com.example.konsist.libraries.rxjava

import com.example.konsist.KonsistUtils.classesProduction
import com.lemonappdev.konsist.api.ext.list.withText
import com.lemonappdev.konsist.api.verify.assertFalse
import io.kotest.core.spec.style.BehaviorSpec

class BehaviorSubjectsHaveDefaultValue : BehaviorSpec() {

    init {
        Given("A class in production") {
            val classes = classesProduction

            When("It has a BehaviorSubject") {
                val classesWithBehaviorSubject = classes.withText("BehaviorSubject")

                Then("It has a default value") {
                    classesWithBehaviorSubject.assertFalse(additionalMessage = MESSAGE) {
                        it.text.contains("BehaviorSubject.create()")
                    }
                }
            }
        }
    }

    private companion object {
        private const val MESSAGE =
            "All BehaviorSubjects should have a default value. Consider adding an initial state, or empty list, or an Optional<value>."
    }
}
