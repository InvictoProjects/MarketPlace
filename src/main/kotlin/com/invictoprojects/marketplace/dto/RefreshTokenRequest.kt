package com.invictoprojects.marketplace.dto

import javax.validation.constraints.NotNull

class RefreshTokenRequest(
    @NotNull var refreshToken: String,
    @NotNull var email: String
)
