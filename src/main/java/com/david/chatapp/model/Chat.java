package com.david.chatapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Generated;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "messages")
@Data
@AllArgsConstructor
@Builder
public class Chat {
    @Id
    @Generated
    @JsonIgnore
    private String id;
    private String sender;
    private String content;
    private String timestamp;
}
