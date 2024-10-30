package com.example.bookYourSeat.common.service

import com.example.bookYourSeat.common.entity.color.Color

interface SlackService {
    fun setMessage(title: String, data: LinkedHashMap<String, String>, color: Color)
}