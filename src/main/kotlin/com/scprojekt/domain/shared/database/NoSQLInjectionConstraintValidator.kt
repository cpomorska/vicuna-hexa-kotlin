package com.scprojekt.domain.shared.database

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import java.util.regex.Pattern

class NoSQLInjectionConstraintValidator : ConstraintValidator<NoSQLInjection, String> {
    private val replacement: String = ")(\\b)+\\s.*(.*)"

    private val sqlTypes: String  =
            "TABLE, TABLESPACE, PROCEDURE, FUNCTION, TRIGGER, KEY, VIEW, MATERIALIZED VIEW, LIBRARY" +
                    "DATABASE LINK, DBLINK, INDEX, CONSTRAINT, TRIGGER, USER, SCHEMA, DATABASE, PLUGGABLE DATABASE, BUCKET, " +
                    "CLUSTER, COMMENT, SYNONYM, TYPE, JAVA, SESSION, ROLE, PACKAGE, PACKAGE BODY, OPERATOR" +
                    "SEQUENCE, RESTORE POINT, PFILE, CLASS, CURSOR, OBJECT, RULE, USER, DATASET, DATASTORE, " +
                    "COLUMN, FIELD, OPERATOR"

    private val sqlRegexps = arrayOf(
        "(?i)(.*)(\\b)+SELECT(\\b)+\\s.*(\\b)+FROM(\\b)+\\s.*(.*)",
        "(?i)(.*)(\\b)+INSERT(\\b)+\\s.*(\\b)+INTO(\\b)+\\s.*(.*)",
        "(?i)(.*)(\\b)+UPDATE(\\b)+\\s.*(.*)",
        "(?i)(.*)(\\b)+DELETE(\\b)+\\s.*(\\b)+FROM(\\b)+\\s.*(.*)",
        "(?i)(.*)(\\b)+UPSERT(\\b)+\\s.*(.*)",
        "(?i)(.*)(\\b)+SAVEPOINT(\\b)+\\s.*(.*)",
        "(?i)(.*)(\\b)+CALL(\\b)+\\s.*(.*)",
        "(?i)(.*)(\\b)+ROLLBACK(\\b)+\\s.*(.*)",
        "(?i)(.*)(\\b)+KILL(\\b)+\\s.*(.*)",
        "(?i)(.*)(\\b)+DROP(\\b)+\\s.*(.*)",
        "(?i)(.*)(\\b)+CREATE(\\b)+(\\s)*(" + sqlTypes.replace(",", "|") + replacement,
        "(?i)(.*)(\\b)+ALTER(\\b)+(\\s)*(" + sqlTypes.replace(",", "|") + replacement,
        "(?i)(.*)(\\b)+TRUNCATE(\\b)+(\\s)*(" + sqlTypes.replace(",", "|") + replacement,
        "(?i)(.*)(\\b)+LOCK(\\b)+(\\s)*(" + sqlTypes.replace(",", "|") + replacement,
        "(?i)(.*)(\\b)+UNLOCK(\\b)+(\\s)*(" + sqlTypes.replace(",", "|") + replacement,
        "(?i)(.*)(\\b)+RELEASE(\\b)+(\\s)*(" + sqlTypes.replace(",", "|") + replacement,
        "(?i)(.*)(\\b)+DESC(\\b)+(\\w)*\\s.*(.*)",
        "(?i)(.*)(\\b)+DESCRIBE(\\b)+(\\w)*\\s.*(.*)",
        "(.*)(/\\*|\\*/|;){1,}(.*)",
        "(.*)(-){2,}(.*)"
    )

    private val validationPatterns : MutableList<Pattern>
        get() = getAllValidationPatterns()

    override fun initialize(sql : NoSQLInjection){
        //NOSONAR
    }

    override fun isValid(dataString:String, cxt:ConstraintValidatorContext):Boolean {
        return isNoSqlInjection(dataString)
    }

    private fun isNoSqlInjection(dataString:String) :Boolean{
        validationPatterns.forEach { p ->
            if (matches(p, dataString)) {
                return false
            }
        }
        return true
    }

    private fun matches( pattern: Pattern, dataString: String): Boolean{
        val matcher = pattern.matcher(dataString)
        return matcher.matches()
    }

    private fun getAllValidationPatterns(): MutableList<Pattern> {
        val patterns = mutableListOf<Pattern>()
        sqlRegexps.forEach { s -> patterns.add(getPattern(s)) }
        return patterns
    }

    private fun getPattern(regEx:String) : Pattern{
        return Pattern.compile(regEx, Pattern.CASE_INSENSITIVE or Pattern.UNICODE_CASE)
    }
    private fun isEmpty(cs :CharSequence): Boolean {
        return cs.isEmpty()
    }

}
