package com.david.chatapp.repository;

import com.david.chatapp.model.Chat;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatRepository extends MongoRepository<Chat, String> {
    List<Chat> findAllByOrderByTimestampAsc();
}

