package com.invictoprojects.marketplace.dto

import javax.validation.constraints.NotNull

class LoginRequest(
    @NotNull var email: String,
    @NotNull var password: String
)
