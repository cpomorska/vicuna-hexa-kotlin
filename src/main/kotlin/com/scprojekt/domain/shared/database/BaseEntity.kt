package com.scprojekt.domain.shared.database

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.Column
import jakarta.persistence.MappedSuperclass
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.Instant

@MappedSuperclass
open class BaseEntity {

    @JsonIgnore
    @CreationTimestamp
    @Column(nullable = false, name = "created_at")
    open var createdAt: Instant? = null

    @JsonIgnore
    @UpdateTimestamp
    @Column(nullable = false, name = "modified_at")
    open val modifiedAt: Instant? = null

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