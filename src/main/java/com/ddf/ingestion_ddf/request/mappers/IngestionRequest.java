package com.ddf.ingestion_ddf.request.mappers;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.Date;
import java.util.List;

/**
 * Data transfer object (DTO) for ingestion requests.
 */
@Data
public class IngestionRequest {
    /** Name of the requester. */
    @NotNull(message = "Requester name cannot be null")
    @NotEmpty(message = "Requester name cannot be empty")
    private String requesterName;

    /** MUDID of the requester. */
    @NotNull(message = "Requester mudid cannot be null")
    @NotEmpty(message = "Requester mudid cannot be empty")
    private String requesterMudid;

    /** Email of the requester. */
    @NotNull(message = "Requester email cannot be null")
    @NotEmpty(message = "Requester email cannot be empty")
    private String requesterEmail;

    /** Name of the dataset. */
    @NotNull(message = "Dataset name cannot be null")
    @NotEmpty(message = "Dataset name cannot be empty")
    private String datasetName;

    /** Name of the datasetSME for Data SME Role. */
    @NotNull(message = "Dataset sme name cannot be null")
    @NotEmpty(message = "Dataset sme name cannot be empty")
    private String datasetSmeName;

    /** MUDID of the datasetSME for Data SME Role. */
    @NotNull(message = "Dataset sme mudid cannot be null")
    @NotEmpty(message = "Dataset sme mudid cannot be empty")
    private String datasetSmeMudid;

    /** Email of the datasetSME for Data SME Role. */
    @NotNull(message = "Dataset sme email cannot be null")
    @NotEmpty(message = "Dataset sme email cannot be empty")
    private String datasetSmeEmail;

    /** Reason for the ingestion request. */
    @NotNull(message = "Request rationale reason cannot be null")
    @NotEmpty(message = "Request rationale reason cannot be empty")
    private String requestRationaleReason;

    /** Origin source of the dataset. */
    @NotNull(message = "Dataset origin source cannot be null")
    @NotEmpty(message = "Dataset origin source cannot be empty")
    private String datasetOriginSource;

    /** Current data location reference. */
    @NotNull(message = "Current data location ref cannot be null")
    @NotEmpty(message = "Current data location ref cannot be empty")
    private String currentDataLocationRef;

    /** Path of the data location. */
    @NotNull(message = "Data location path cannot be null")
    @NotEmpty(message = "Data location path cannot be empty")
    private String dataLocationPath;

    /** Flag indicating meteor space Domino usage. */
    @NotNull(message = "Meteor space domino usage flag cannot be null")
    private Boolean meteorSpaceDominoUsageFlag;

    /** Flag indicating IHD usage. */
    @NotNull(message = "Ihd flag cannot be null")
    private Boolean ihdFlag;

    /** Reference for dataset requirement. */
    @NotNull(message = "Dataset required for ref cannot be null")
    @NotEmpty(message = "Dataset required for ref cannot be empty")
    private String datasetRequiredForRef;

    /** Reference for estimated data volume. */
    @NotNull(message = "Estimated data volume ref cannot be null")
    @NotEmpty(message = "Estimated data volume ref cannot be empty")
    private String estimatedDataVolumeRef;

    /** Refresh frequency of the data. */
    @NotNull(message = "Data refresh frequency cannot be null")
    @NotEmpty(message = "Data refresh frequency cannot be empty")
    private String dataRefreshFrequency;

    /** Initial analysis date. */
    @NotNull(message = "Analysis init dt cannot be null")
    private Date analysisInitDt;

    /** End analysis date. */
    @NotNull(message = "Analysis end dt cannot be null")
    private Date analysisEndDt;

    /** Flag indicating DTA contract completion. */
    @NotNull(message = "Dta contract complete flag cannot be null")
    private Boolean dtaContractCompleteFlag;

    /** Expected completion date for DTA. */
    @NotNull(message = "Dta expected completion date cannot be null")
    private Date dtaExpectedCompletionDate;

    /** List of data category references. */
    @NotNull(message = "Data category refs cannot be null")
    private List<String> dataCategoryRefs;

    /** Reference for dataset type. */
    @NotNull(message = "Dataset type ref cannot be null")
    @NotEmpty(message = "Dataset type ref cannot be empty")
    private String datasetTypeRef;

    /** List of study IDs. */
    @NotNull(message = "Study ids cannot be null")
    private List<String> studyIds;

    /** Name of the dataset owner for Data owner role. */
    @NotNull(message = "Dataset owner name cannot be null")
    @NotEmpty(message = "Dataset owner name cannot be empty")
    private String datasetOwnerName;

    /** MUDID of the dataset owner for Data owner role. */
    @NotNull(message = "Dataset owner mudid cannot be null")
    @NotEmpty(message = "Dataset owner mudid cannot be empty")
    private String datasetOwnerMudid;

    /** Email of the dataset owner for Data owner role. */
    @NotNull(message = "Dataset owner email cannot be null")
    @NotEmpty(message = "Dataset owner email cannot be empty")
    private String datasetOwnerEmail;

    /** Name of the dataset steward for Data steward role. */
    @NotNull(message = "Dataset steward name cannot be null")
    @NotEmpty(message = "Dataset steward name cannot be empty")
    private String datasetStewardName;

    /** MUDID of the dataset steward for Data steward role. */
    @NotNull(message = "Dataset steward mudid cannot be null")
    @NotEmpty(message = "Dataset steward mudid cannot be empty")
    private String datasetStewardMudid;

    /** Email of the dataset steward for Data steward role. */
    @NotNull(message = "Dataset steward email cannot be null")
    @NotEmpty(message = "Dataset steward email cannot be empty")
    private String datasetStewardEmail;

    /** Contract partner associated with the dataset. */
    @NotNull(message = "Contract partner cannot be null")
    @NotEmpty(message = "Contract partner cannot be empty")
    private String contractPartner;

    /** Retention rules for the dataset. */
    @NotNull(message = "Retention rules cannot be null")
    @NotEmpty(message = "Retention rules cannot be empty")
    private String retentionRules;

    /** Contract date for retention rules. */
    @NotNull(message = "Retention rules contract date cannot be null")
    private Date retentionRulesContractDate;

    /** List of usage restrictions. */
    @NotNull(message = "Usage restrictions cannot be null")
    private List<String> usageRestrictions;

    /** List of user restrictions. */
    @NotNull(message = "User restrictions cannot be null")
    private List<String> userRestrictions;

    /** Reference for information classification type. */
    @NotNull(message = "Information classification type ref cannot be null")
    @NotEmpty(message = "Information classification type ref cannot be empty")
    private String informationClassificationTypeRef;

    /** Reference for PII type. */
    @NotNull(message = "Pii type ref cannot be null")
    @NotEmpty(message = "Pii type ref cannot be empty")
    private String piiTypeRef;

    /** List of therapy areas associated with the dataset. */
    @NotNull(message = "Therapy areas cannot be null")
    private List<String> therapyAreas;

    /** List of techniques and assays used in the dataset. */
    @NotNull(message = "Technique and assays cannot be null")
    private List<String> techniqueAndAssays;

    /** List of indications for the dataset. */
    @NotNull(message = "Indications cannot be null")
    private List<String> indications;

    /** Target ingestion start date. */
    @NotNull(message = "Target ingestion start date cannot be null")
    private Date targetIngestionStartDate;

    /** Target ingestion end date. */
    @NotNull(message = "Target ingestion end date cannot be null")
    private Date targetIngestionEndDate;

    /** Target path for ingestion. */
    @NotNull(message = "Target path cannot be null")
    @NotEmpty(message = "Target path cannot be empty")
    private String targetPath;

    /** Reference for dataset type ingestion. */
    @NotNull(message = "Dataset type ingestion ref cannot be null")
    @NotEmpty(message = "Dataset type ingestion ref cannot be empty")
    private String datasetTypeIngestionRef;

    /** List of guest users' emails. */
    @NotNull(message = "Guest users email cannot be null")
    private List<String> guestUsersEmail;

    /** List of whitelisted IP addresses. */
    @NotNull(message = "Whitelist ip addresses cannot be null")
    private List<String> whitelistIpAddresses;

    /** Name of the external staging container. */
    @NotNull(message = "External staging container name cannot be null")
    @NotEmpty(message = "External staging container name cannot be empty")
    private String externalStagingContainerName;

    /** Domain request ID. */
    @NotNull(message = "Domain request id cannot be null")
    @NotEmpty(message = "Domain request id cannot be empty")
    private String domainRequestId;

    /** Location of the external data source. */
    @NotNull(message = "External data source location cannot be null")
    @NotEmpty(message = "External data source location cannot be empty")
    private String externalDataSourceLocation;

    /** GSK access source location reference. */
    @NotNull(message = "Gsk access source location ref cannot be null")
    @NotEmpty(message = "Gsk access source location ref cannot be empty")
    private String gskAccessSourceLocationRef;

    /** Name of the external source secret key. */
    @NotNull(message = "External source secret key name cannot be null")
    @NotEmpty(message = "External source secret key name cannot be empty")
    private String externalSourceSecretKeyName;

    /** Modified reason. **/
    private String modifiedReason;
}
