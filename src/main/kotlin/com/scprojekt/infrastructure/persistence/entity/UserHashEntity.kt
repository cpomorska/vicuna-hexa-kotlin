package com.scprojekt.infrastructure.persistence.entity

import com.scprojekt.domain.shared.database.BaseEntity
import com.scprojekt.domain.shared.database.NoSQLInjection
import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.validation.constraints.NotNull
import lombok.AllArgsConstructor
import lombok.Getter
import lombok.NoArgsConstructor

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
class UserHashEntity : BaseEntity(){

    @NotNull
    @NoSQLInjection
    @Column(name="benutzerhash")
    var hashField: String? = null;

    @NotNull
    @NoSQLInjection
    @Column(name="benutzersalz")
    var saltField: String? = null;
}