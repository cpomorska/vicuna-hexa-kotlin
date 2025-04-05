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
    @Column(nullable = false, name = "created_at")
    open var createdAt: Date? = null

    @JsonIgnore
    @UpdateTimestamp
    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(nullable = false, name = "modified_at")
    open val modifiedAt: Date? = null

    @JsonIgnore
    @Column(name = "created_from")
    open val createdFrom: String? = null

    @JsonIgnore
    @Column(name = "modified_from")
    open val modifiedFrom: String? = null

    @JsonIgnore
    @Column(columnDefinition = "boolean default true")
    open var enabled: Boolean = true
}