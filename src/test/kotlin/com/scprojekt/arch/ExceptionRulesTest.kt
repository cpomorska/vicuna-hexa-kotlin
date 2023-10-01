package com.scprojekt.arch;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import org.junit.jupiter.api.Test;

class ExceptionRulesTest {

    private val classes: JavaClasses = ClassFileImporter().importPackages("com.scprojekt");

    @Test
    fun exceptions_must_reside_in_domain_and_exception_package() {
        classes().that().areAssignableTo(Exception::class.java).should()
                .resideInAPackage("..domain..")
                .andShould().resideInAPackage("..exception..")
                .andShould().beAssignableTo(RuntimeException::class.java)
                .`as`("Exceptions should reside in package '..domain..' and then in '..exception..'")
                .allowEmptyShould(true)
                .check(classes)
    }

    @Test
    fun exceptions_must_extend_runtimeexception() {
        classes().that().haveNameMatching(".*Exception")
                .and().resideOutsideOfPackage("..shared..")
                .and().resideInAPackage("..domain..")
                .and().resideInAPackage("..exception..")
                .should().beAssignableTo(RuntimeException::class.java)
                .`as`("Custom exceptions should implement RuntimeException")
                .check(classes)
    }
}
