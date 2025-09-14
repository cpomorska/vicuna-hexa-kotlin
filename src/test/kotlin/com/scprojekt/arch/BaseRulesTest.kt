package com.scprojekt.arch;

import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.fields
import com.tngtech.archunit.library.GeneralCodingRules.*
import org.slf4j.Logger

@AnalyzeClasses(packages = ["com.scprojekt"])
public class BaseRulesTest {

    //val classes = ClassFileImporter().importPackages("com.scprojekt")

//    @ArchTest
//    private val no_field_injection = NO_CLASSES_SHOULD_USE_FIELD_INJECTION

    @ArchTest
    private val no_deprecated_api = DEPRECATED_API_SHOULD_NOT_BE_USED

//    @ArchTest
//    private fun check_other_rules(classes: JavaClasses) {
//        no_deprecated_api.check(classes)
//        no_field_injection.check(classes)
//    }

    @ArchTest
    private fun standard_rules_for_all_classes(classes: JavaClasses) {
        ArchRuleDefinition.noClasses()
            .should(ACCESS_STANDARD_STREAMS)
            .andShould(THROW_GENERIC_EXCEPTIONS)
            .andShould(USE_JAVA_UTIL_LOGGING)
            .andShould(USE_JODATIME)
            .check(classes);
    }

    @ArchTest
    private fun loggers_should_be_private_static_final(classes: JavaClasses) {
        fields().that().haveRawType(Logger::class.java)
            .should().bePrivate()
            .andShould().beFinal()
            .because("standard logger convention")
            .allowEmptyShould(true)
            .check(classes)
    }
}
