package com.invictoprojects.marketplace.service

interface EmailService {

    fun sendEmail(sender: String, personal: String, receiver: String, subject: String, hyperText: String)

}
