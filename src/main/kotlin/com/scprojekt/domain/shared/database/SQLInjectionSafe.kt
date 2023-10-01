package com.scprojekt.domain.shared.database;

import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@MustBeDocumented
@Constraint(validatedBy = [SQLInjectionSafeConstraintValidator::class])
@Target(AnnotationTarget.FIELD,AnnotationTarget.TYPE_PARAMETER, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class SQLInjectionSafe (
    val message: String = "{SQLInjectionSafe}",
    val groups: Array<KClass<*>>  = emptyArray<KClass<*>>(),
    val payload: Array<KClass<out Payload>> = emptyArray<KClass<out Payload>>()
)
{
}
