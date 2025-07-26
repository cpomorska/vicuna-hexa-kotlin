package com.scprojekt.arch;

import com.scprojekt.domain.shared.service.BaseService
import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.theClass
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test


class ServiceRulesTest {

    private val classes: JavaClasses  = ClassFileImporter().importPackages("com.scprojekt")

    @Test
   fun services_must_reside_in_service_package() {
        classes().that().haveNameMatching(".*Service")
                .and().resideOutsideOfPackage("..shared..")
                .and().resideOutsideOfPackage("..example..")
                .and().resideOutsideOfPackage("..stereotype..")
                .should().resideInAPackage("..service..")
                .`as`("Services should reside in a package '..service..'")
                .check(classes)
    }

    @Disabled
    @Test
    fun services_must_extend_baseservice_interface() {
        classes().that().haveNameMatching(".*Service")
                .and().haveNameNotMatching(".*Management")
                .or().haveNameNotMatching(".*Abstract")
                .and().haveSimpleNameNotContaining("Test")
                .and().resideOutsideOfPackage("..shared..")
                .and().resideInAPackage("..service..")
                .should().beAssignableTo(BaseService::class.java)
                .`as`("Services should implement BaseService")
                .check(classes)
    }

    @Test
    fun only_one_baseaervice_interface() {
        theClass(BaseService::class.java)
                .should().resideInAPackage("..shared..")
                .andShould().resideInAPackage("..service..")
                .`as`("BaseService Interface in shared .. service")
                .check(classes)
    }
}
