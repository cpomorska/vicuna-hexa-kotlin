package com.scprojekt.arch;

import com.scprojekt.domain.shared.database.BaseRepository
import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.theClass
import org.junit.jupiter.api.Test


class RepositoryRulesTest {

    private val classes: JavaClasses = ClassFileImporter().importPackages("com.scprojekt.domain");

    @Test
    fun repositories_must_reside_in_repository_package() {
        classes().that().haveNameMatching(".*Repository")
                .and().resideOutsideOfPackage("..shared..")
                .should().resideInAPackage("..repository..")
                .`as`("Repositories should reside in a package '..repository..'")
                .check(classes);
    }

    @Test
    fun repositories_must_extend_baserepository_interface() {
        classes().that().haveNameMatching(".*Repository")
                .and().resideOutsideOfPackage("..shared..")
                .and().resideInAPackage("..repository..")
                .should().beAssignableTo(BaseRepository::class.java)
                .`as`("Repositories should implement BaseRepository")
                .check(classes)
    }

    @Test
    fun only_one_baserepository_interface() {
        theClass(BaseRepository::class.java)
                .should().resideInAPackage("..shared..")
                .andShould().resideInAPackage("..database..")
                .`as`("BaseRepository Interface in shared .. database")
                .check(classes)
    }
}
