package com.example.secret_santa.service.event

import com.example.secret_santa.storage.ServiceLocator

class EventService {
    fun distributeInPairs(id: String) {
        val e = ServiceLocator.eventStorage.getById(id) ?: return
        val participants = e.participants
        val shuffledParticipants = participants.shuffled()
        for (i in shuffledParticipants.indices) {
            val giver = shuffledParticipants[i]
            val receiver = shuffledParticipants[(i + 1) % shuffledParticipants.size]
            if (giver != receiver) {
                val user = ServiceLocator.userStorage.getById(giver) ?: return
                user.recipientId = receiver
                ServiceLocator.userStorage.update(user)
            }
        }
    }
}