package com.ddf.ingestion_ddf.enums;


/**
 * Enum representing different statuses of ingestion processes.
 */
public enum IngestionStatus {
    DRAFT("draft"),
    TRIAGE_PENDING_APPROVAL("triagePendingApproval"),
    APPROVED("approved"),
    REJECTED("rejected"),
    INGESTION_IN_PROGRESS("inProgress"),
    INGESTION_COMPLETED("completed"),
    INGESTION_FAILURE("failure");

    private final String displayName;

    IngestionStatus(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return this.displayName;
    }
}
