package com.scprojekt.domain.model.user.entity

import com.scprojekt.domain.shared.SQLInjectionSafe
import com.scprojekt.mimetidae.domain.shared.BaseEntity
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
class UserHash: BaseEntity() {

    @NotNull
    @SQLInjectionSafe
    @Column(name="benutzerhash")
    var hashField: String? = null

    @NotNull
    @SQLInjectionSafe
    @Column(name="benutzersalz")
    var saltField: String? = null
}
