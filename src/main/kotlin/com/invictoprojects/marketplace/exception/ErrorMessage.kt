package com.invictoprojects.marketplace.exception

import java.time.Instant

data class ErrorMessage(
    var timestamp: Instant,
    var status: Int,
    var error: String,
    var messages: List<String?>,
    var path: String
)
