package com.scprojekt.arch;

import com.scprojekt.domain.shared.database.BaseEntity
import com.scprojekt.domain.shared.database.NoSQLInjection
import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*
import jakarta.persistence.Embeddable
import jakarta.persistence.Entity
import jakarta.persistence.EntityManager
import jakarta.persistence.Table
import org.junit.jupiter.api.Test
import java.sql.SQLException


class EntityRulesTest {

    private val classes: JavaClasses = ClassFileImporter().importPackages("com.scprojekt");

    @Test
    fun entities_must_reside_in_domain_and_entity_package() {
        classes().that().areAnnotatedWith(Entity::class.java)
            .should()
                .resideInAPackage("..domain..")
                .andShould().resideInAPackage("..entity..")
                .`as`("Entities should reside in package '..domain..' and then in '..entity..'")
                .allowEmptyShould(true)
                .check(classes)
    }

    @Test
    fun entities_must_extend_baseentity() {
        classes().that().areAnnotatedWith(Entity::class.java)
                .should().beAssignableTo(BaseEntity::class.java)
                .`as`("Entities should extend BaseEntity")
                .allowEmptyShould(true)
                .check(classes)
    }

    @Test
    fun entities_must_be_annotated_with_entity_and_table_or_embeddable() {
        classes().that().resideInAPackage("..entity..")
                .should().beAnnotatedWith(Entity::class.java)
                .andShould().beAnnotatedWith(Table::class.java)
                .orShould().beAnnotatedWith(Embeddable::class.java)
                .`as`("Entities use @Entity and @Table or @Embeddable")
                .allowEmptyShould(true)
                .check(classes)
    }

    @Test
    fun string_members_must_annotated_with_nosqlinjection() {
        fields().that().arePublic().or()
                .arePackagePrivate()
                .and().areDeclaredInClassesThat().resideInAPackage("..entity..")
                .and().haveRawType(String::class.simpleName)
                .should().beAnnotatedWith(NoSQLInjection::class.java)
                .`as`("Fields with String type should be SQLInjection safe")
                .allowEmptyShould(true)
                .check(classes)
    }

    @Test
    fun only_repositories_use_the_EntityManager_directly() {
        noClasses().that().resideInAPackage("..entity..")
            .should().accessClassesThat().areAssignableTo(EntityManager::class.java)
            .`as`("Only Repositories may use the " + EntityManager::class.simpleName)
            .allowEmptyShould(true)
            .check(classes)

    }

    @Test
    fun Entity_must_not_throw_SQLException() {
        noMethods().that().areDeclaredInClassesThat().resideInAPackage("..entity..")
                .should().declareThrowableOfType(SQLException::class.java)
                .allowEmptyShould(true)
                .check(classes)
    }
}
