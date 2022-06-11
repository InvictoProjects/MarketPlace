package com.invictoprojects.marketplace.dto

import javax.validation.constraints.NotNull

class RegisterRequest {
    @NotNull
    var email: String? = null

    @NotNull
    var username: String? = null

    @NotNull
    var password: String? = null
}
