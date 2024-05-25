package com.ddf.ingestion_ddf.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.GenerationType;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "request_status_details")
public class RequestStatusDetails extends BaseModel{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requestStatusId;

    @ManyToOne
    @JoinColumn(name = "ingestion_request_id")
    @JsonIgnore
    private IngestionRequestDetails ingestionRequest;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private Status status;

    private String decisionByName;
    private String decisionByMudid;
    private String decisionByEmail;
    private Date decisionDate;
    private String decisionComments;
    private String rejectionReason;
    private Boolean activeFlag;

}