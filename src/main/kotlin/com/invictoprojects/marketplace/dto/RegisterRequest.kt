package com.invictoprojects.marketplace.dto

import javax.validation.constraints.NotNull

class RegisterRequest(
    @NotNull var email: String,
    @NotNull var username: String,
    @NotNull var password: String
)
