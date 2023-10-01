package com.scprojekt.domain.shared.database

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.Column
import jakarta.persistence.MappedSuperclass
import jakarta.persistence.Temporal
import jakarta.persistence.TemporalType
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.util.*

@MappedSuperclass
open class BaseEntity {

    @JsonIgnore
    @CreationTimestamp
    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(nullable = false)
    open var createdAt: Date? = null

    @JsonIgnore
    @UpdateTimestamp
    @Temporal(value = TemporalType.TIMESTAMP)
    open val modifiedAt: Date? = null

    @JsonIgnore
    open val modifiedFrom: String? = null

    @JsonIgnore
    @Column(columnDefinition = "boolean default true")
    open var enabled: Boolean = true
}