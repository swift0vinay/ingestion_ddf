package com.ddf.ingestion_ddf.enums;

/**
 * Enum representing different statuses of ingestion requests.
 */
public enum IngestionRequestStatus {
    All("all"),
    PendingApproval("pendingApproval"),
    CompletedRequest("completedRequest"),
    Rejected("rejected");

    private final String requestStatus;

    IngestionRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    String getRequestStatus() {
        return this.requestStatus;
    }

}
