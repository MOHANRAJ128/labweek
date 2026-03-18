package com.codeinsight.indexing.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name="method_calls")
public class MethodCallEntity {

    @Id
    @GeneratedValue
    private UUID id;

    private UUID projectId;

    private String callerMethod;

    private String calleeMethod;

    private String callerClass;
}