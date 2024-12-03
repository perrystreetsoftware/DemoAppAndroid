package com.example.konsist

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.container.KoScope
import com.lemonappdev.konsist.api.ext.list.withAnnotationNamed
import com.lemonappdev.konsist.api.ext.list.withNameEndingWith

object KonsistUtils {
    val resourceIdentifiers = listOf("string.", "strings.", "drawable.", "drawables.")

    val viewModelsProduction
        get() = classesProduction.withNameEndingWith("ViewModel")

    val logicClassesProduction
        get() = classesProduction.withNameEndingWith("Logic")

    val mapperObjectsProduction
        get() = objectsProduction.withNameEndingWith("Mapper")

    val repositoryClassesProduction
        get() = classesProduction.withNameEndingWith("Repository")

    val apiClassesProduction
        get() = classesProduction.withNameEndingWith("Api")

    val interfacesProduction
        get() = productionCode.interfaces()

    val classesProduction
        get() = productionCode.classes()

    val productionFiles
        get() = productionCode.files

    private val objectsProduction
        get() = productionCode.objects()

    val productionCode
        get() = Konsist.scopeFromProduction()

    val testCode
        get() = Konsist.scopeFromTest().excludeKonsistTests()

    val composableFunctions
        get() = productionCode.functions().withAnnotationNamed("Composable")

    private fun KoScope.excludeKonsistTests() = this.slice { !(it.path.contains("/konsist/")) }
}
