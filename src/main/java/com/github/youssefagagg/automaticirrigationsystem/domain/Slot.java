package com.github.youssefagagg.automaticirrigationsystem.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.youssefagagg.automaticirrigationsystem.domain.enumeration.Status;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

/**
 * A Slot.
 */
@Document(collection = "slot")
@Data
public class Slot implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("status")
    private Status status;


}
