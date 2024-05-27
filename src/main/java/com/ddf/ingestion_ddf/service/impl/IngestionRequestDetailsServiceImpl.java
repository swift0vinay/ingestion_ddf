package com.ddf.ingestion_ddf.service.impl;

import com.ddf.ingestion_ddf.entity.*;
import com.ddf.ingestion_ddf.enums.*;
import com.ddf.ingestion_ddf.exceptions.ApiException;
import com.ddf.ingestion_ddf.repository.*;
import com.ddf.ingestion_ddf.request.mappers.DecisionRequestDTO;
import com.ddf.ingestion_ddf.request.mappers.IngestionRequest;
import com.ddf.ingestion_ddf.response.mappers.IngestionRequestDetailsDTO;
import com.ddf.ingestion_ddf.response.mappers.IngestionRequestSummaryDTO;
import com.ddf.ingestion_ddf.response.mappers.ResponseDtoConverter;
import com.ddf.ingestion_ddf.service.IngestionRequestDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;


/**
 * Implementation of the {@link IngestionRequestDetailsService} interface.
 * This service provides methods to handle ingestion request details.
 * It interacts with various repositories to access and manipulate data.
 *
 * @author Brijesh Rajput
 * @version 1.0
 * @since 20/05/24
 */
@Service
public class IngestionRequestDetailsServiceImpl implements IngestionRequestDetailsService {

    private ValidationNotesRepository validationNotesRepository;

    private StatusRepository statusRepository;

    private IngestionRequestDetailsRepository ingestionRequestDetailsRepository;

    private RequestStatusDetailsRepository requestStatusRepository;

    private TechnicalDetailsRepository technicalDetailsRepository;

    @Autowired
    private ResponseDtoConverter dtoConverter;

    /**
     * Constructs a new {@code IngestionRequestDetailsServiceImpl} with the specified repositories.
     *
     * @param ingestionRequestDetailsRepository the repository for ingestion request details
     * @param statusRepository                  the repository for status data
     * @param validationNotesRepository         the repository for validation notes data
     * @param requestStatusRepository           the repository for request status details
     * @param technicalDetailsRepository        the repository for technical details
     */
    public IngestionRequestDetailsServiceImpl(IngestionRequestDetailsRepository ingestionRequestDetailsRepository, StatusRepository statusRepository,
                                              ValidationNotesRepository validationNotesRepository, RequestStatusDetailsRepository requestStatusRepository,
                                              TechnicalDetailsRepository technicalDetailsRepository) {
        this.ingestionRequestDetailsRepository = ingestionRequestDetailsRepository;
        this.statusRepository = statusRepository;
        this.validationNotesRepository = validationNotesRepository;
        this.requestStatusRepository = requestStatusRepository;
        this.technicalDetailsRepository = technicalDetailsRepository;
    }

    /**
     * Creates an ingestion request with the provided details.
     *
     * @param ingestionRequestId the ID of the ingestion request (optional) only required when ingestionRequest is updating
     * @param ingestionRequest   the ingestion request details to addOrUpdate the IngestionRequestDetails and there related Data
     * @param submit             a flag false to only create the request in DRAFT status, true to create the request in TRIAGE PENDING APPROVAL status
     * @return the DTO representation of the created ingestion request details
     */
    @Override
    public IngestionRequestDetailsDTO createOrUpdateIngestionRequest(Long ingestionRequestId, IngestionRequest ingestionRequest, boolean submit) {
        IngestionRequestDetails ingestionRequestDetails = new IngestionRequestDetails();

        // As logged-in user details are not available, using static emails for createdBy and modifiedBy
        String createdBy = "test@gmail.com";  // Update with logged-in user email
        String modifiedBy = "testModifiy@gmail.com";  // Update with logged-in user email

        // Check if ingestionRequestId is provided and exists in the repository if exists then operation is Update Operation
        if (ingestionRequestId != null && ingestionRequestId != 0 && ingestionRequestDetailsRepository.findById(ingestionRequestId).isPresent()) {
            IngestionRequestDetails details = ingestionRequestDetailsRepository.findById(ingestionRequestId).get();
            ingestionRequestDetails.setIngestionRequestId(ingestionRequestId);
            ingestionRequestDetails.setCreatedBy(details.getCreatedBy());
            ingestionRequestDetails.setCreatedDate(details.getCreatedDate());
        } else {
            ingestionRequestDetails.setCreatedBy(createdBy);
        }
        ingestionRequestDetails.setModifiedBy(modifiedBy);

        ingestionRequestDetails.setRequesterName(ingestionRequest.getRequesterName());
        ingestionRequestDetails.setRequesterMudid(ingestionRequest.getRequesterMudid());
        ingestionRequestDetails.setRequesterEmail(ingestionRequest.getRequesterEmail());
        ingestionRequestDetails.setRequestedByName(ingestionRequest.getRequesterName());
        ingestionRequestDetails.setRequestedByMudid(ingestionRequest.getRequesterMudid());
        ingestionRequestDetails.setRequestedByEmail(ingestionRequest.getRequesterEmail());
        ingestionRequestDetails.setRequestRationaleReason(ingestionRequest.getRequestRationaleReason());

        DatasetDetails datasetDetails = new DatasetDetails();
        if (ingestionRequestDetails.getDatasetDetails() == null) {
            datasetDetails.setCreatedBy(createdBy);
        }
        datasetDetails.setModifiedBy(modifiedBy);
        datasetDetails.setDatasetName(ingestionRequest.getDatasetName());
        datasetDetails.setDatasetOriginSource(ingestionRequest.getDatasetOriginSource());
        datasetDetails.setCurrentDataLocationRef(ingestionRequest.getCurrentDataLocationRef());
        datasetDetails.setMeteorSpaceDominoUsageFlag(ingestionRequest.getMeteorSpaceDominoUsageFlag());
        datasetDetails.setIhdFlag(ingestionRequest.getIhdFlag());
        datasetDetails.setEstimatedDataVolumeRef(ingestionRequest.getEstimatedDataVolumeRef());

        datasetDetails.setAnalysisInitDt(ingestionRequest.getAnalysisInitDt());
        datasetDetails.setAnalysisEndDt(ingestionRequest.getAnalysisEndDt());
        datasetDetails.setDtaContractCompleteFlag(ingestionRequest.getDtaContractCompleteFlag());
        if (!ingestionRequest.getDtaContractCompleteFlag()) {
            datasetDetails.setDtaExpectedCompletionDate(ingestionRequest.getDtaExpectedCompletionDate());
        }

        datasetDetails.setDatasetTypeRef(ingestionRequest.getDatasetTypeRef());

        List<DatasetRoleDetails> datasetRoleDetails = new ArrayList<>();
        // Check the datasetRequiredForRef to determine further dataset details
        if (ingestionRequest.getDatasetRequiredForRef().equalsIgnoreCase("Exploration")) {
            // Populate dataset details for exploration type
            datasetDetails.setContractPartner(ingestionRequest.getContractPartner());
            datasetDetails.setRetentionRules(ingestionRequest.getRetentionRules());
            datasetDetails.setRetentionRulesContractDate(ingestionRequest.getRetentionRulesContractDate());
            datasetDetails.setInformationClassificationTypeRef(ingestionRequest.getInformationClassificationTypeRef());
            datasetDetails.setPiiTypeRef(ingestionRequest.getPiiTypeRef());

            // Populate dataset user usage restrictions
            List<DatasetUserUsageRestriction> datasetUserUsageRestrictionList = new ArrayList<>();
            if (ingestionRequest.getUsageRestrictions() != null && !ingestionRequest.getUsageRestrictions().isEmpty()) {
                for (String usage : ingestionRequest.getUsageRestrictions()) {
                    DatasetUserUsageRestriction datasetUserUsageRestriction = new DatasetUserUsageRestriction();
                    datasetUserUsageRestriction.setDatasetId(datasetDetails);
                    datasetUserUsageRestriction.setRestrictionTypeRef("usage_restrictions");
                    datasetUserUsageRestriction.setRestrictionRef(usage);
                    if (datasetDetails.getDatasetUserUsageRestriction() == null) {
                        datasetUserUsageRestriction.setCreatedBy(createdBy);
                    }
                    datasetUserUsageRestriction.setModifiedBy(modifiedBy);
                    datasetUserUsageRestrictionList.add(datasetUserUsageRestriction);
                }
            }

            // Populate dataset user restrictions
            if (ingestionRequest.getUserRestrictions() != null && !ingestionRequest.getUserRestrictions().isEmpty()) {
                for (String usage : ingestionRequest.getUserRestrictions()) {
                    DatasetUserUsageRestriction datasetUserUsageRestriction = new DatasetUserUsageRestriction();
                    datasetUserUsageRestriction.setDatasetId(datasetDetails);
                    datasetUserUsageRestriction.setRestrictionTypeRef("user_restrictions");
                    datasetUserUsageRestriction.setRestrictionRef(usage);
                    if (datasetDetails.getDatasetUserUsageRestriction() == null) {
                        datasetUserUsageRestriction.setCreatedBy(createdBy);
                    }
                    datasetUserUsageRestriction.setModifiedBy(modifiedBy);
                    datasetUserUsageRestrictionList.add(datasetUserUsageRestriction);
                }
            }
            if (!datasetUserUsageRestrictionList.isEmpty()) {
                datasetDetails.setDatasetUserUsageRestriction(datasetUserUsageRestrictionList);
            }

            // Populate dataset role details for data owner role
            DatasetRoleDetails datasetOwnerDetails = new DatasetRoleDetails();
            datasetOwnerDetails.setRole("data owner");
            datasetOwnerDetails.setName(ingestionRequest.getDatasetOwnerName());
            datasetOwnerDetails.setMudid(ingestionRequest.getDatasetOwnerMudid());
            datasetOwnerDetails.setEmail(ingestionRequest.getDatasetOwnerEmail());
            datasetOwnerDetails.setDatasetId(datasetDetails);
            datasetRoleDetails.add(datasetOwnerDetails);

            // Populate dataset role details for data steward role
            DatasetRoleDetails datasetStewardDetails = new DatasetRoleDetails();
            datasetStewardDetails.setRole("data steward");
            datasetStewardDetails.setName(ingestionRequest.getDatasetStewardName());
            datasetStewardDetails.setMudid(ingestionRequest.getDatasetStewardMudid());
            datasetStewardDetails.setEmail(ingestionRequest.getDatasetStewardEmail());
            datasetStewardDetails.setDatasetId(datasetDetails);
            datasetRoleDetails.add(datasetStewardDetails);

        } else {
            // If dataset is not for exploration or is industrialization, set therapy areas, techniques & assays, and indications
            List<DatasetTherapy> datasetTherapies = new ArrayList<>();
            List<DatasetTechAndAssay> datasetTechAndAssays = new ArrayList<>();
            List<DatasetIndication> datasetIndications = new ArrayList<>();
            if (ingestionRequest.getTherapyAreas() != null && !ingestionRequest.getTherapyAreas().isEmpty()) {
                for (String therapy : ingestionRequest.getTherapyAreas()) {
                    DatasetTherapy datasetTherapy = new DatasetTherapy();
                    datasetTherapy.setDatasetId(datasetDetails);
                    datasetTherapy.setTherapy(therapy);
                    datasetTherapies.add(datasetTherapy);
                }
                datasetDetails.setDatasetTherapies(datasetTherapies);
            }

            if (ingestionRequest.getTechniqueAndAssays() != null && !ingestionRequest.getTechniqueAndAssays().isEmpty()) {
                for (String technique : ingestionRequest.getTechniqueAndAssays()) {
                    DatasetTechAndAssay datasetTechAndAssay = new DatasetTechAndAssay();
                    datasetTechAndAssay.setDatasetId(datasetDetails);
                    datasetTechAndAssay.setTechniqueAndAssay(technique);
                    datasetTechAndAssays.add(datasetTechAndAssay);
                }
                datasetDetails.setDatasetTechAndAssays(datasetTechAndAssays);
            }

            if (ingestionRequest.getIndications() != null && !ingestionRequest.getIndications().isEmpty()) {
                for (String indication : ingestionRequest.getIndications()) {
                    DatasetIndication datasetIndication = new DatasetIndication();
                    datasetIndication.setDatasetId(datasetDetails);
                    datasetIndication.setIndication(indication);
                    datasetIndications.add(datasetIndication);
                }
                datasetDetails.setDatasetIndications(datasetIndications);
            }

        }

        List<DatasetStudy> datasetStudies = new ArrayList<>();
        if (ingestionRequest.getStudyIds() != null && !ingestionRequest.getStudyIds().isEmpty()) {
            for (String studyId : ingestionRequest.getStudyIds()) {
                DatasetStudy datasetStudy = new DatasetStudy();
                datasetStudy.setStudyId(studyId);
                datasetStudy.setDatasetId(datasetDetails);
                datasetStudies.add(datasetStudy);
            }
            datasetDetails.setDatasetStudies(datasetStudies);
        }

        List<DatasetDataCategory> datasetDataCategories = new ArrayList<>();
        if (ingestionRequest.getDataCategoryRefs() != null && !ingestionRequest.getDataCategoryRefs().isEmpty()) {
            for (String dataCategory : ingestionRequest.getDataCategoryRefs()) {
                DatasetDataCategory datasetDataCategory = new DatasetDataCategory();
                datasetDataCategory.setDatasetId(datasetDetails);
                datasetDataCategory.setDataCategoryRef(dataCategory);
                datasetDataCategories.add(datasetDataCategory);
            }
            datasetDetails.setDatasetDataCategories(datasetDataCategories);
        }

        // Populate dataset role details for data SME role
        DatasetRoleDetails datasetRoleDetail = new DatasetRoleDetails();
        datasetRoleDetail.setDatasetId(datasetDetails);
        datasetRoleDetail.setName(ingestionRequest.getDatasetSmeName());
        datasetRoleDetail.setMudid(ingestionRequest.getDatasetSmeMudid());
        datasetRoleDetail.setEmail(ingestionRequest.getDatasetSmeEmail());
        datasetRoleDetail.setRole("data SME");
        datasetRoleDetails.add(datasetRoleDetail);

        datasetDetails.setDatasetRoleDetails(datasetRoleDetails);
        datasetDetails.setIngestionRequest(ingestionRequestDetails);

        ingestionRequestDetails.setDatasetDetails(datasetDetails);

        // Set technical details properties from ingestionRequest object
        TechnicalDetails technicalDetails = new TechnicalDetails();
        if (ingestionRequestDetails.getTechnicalDetails() == null) {
            technicalDetails.setCreatedBy(createdBy);
        }
        technicalDetails.setModifiedBy(modifiedBy);
        technicalDetails.setDataLocationPath(ingestionRequest.getDataLocationPath());
        technicalDetails.setDataRefreshFrequency(ingestionRequest.getDataRefreshFrequency());
        technicalDetails.setTargetIngestionStartDate(ingestionRequest.getTargetIngestionStartDate());
        technicalDetails.setTargetIngestionEndDate(ingestionRequest.getTargetIngestionEndDate());
        technicalDetails.setTargetPath(ingestionRequest.getTargetPath());
        technicalDetails.setDatasetTypeIngestionRef(ingestionRequest.getDatasetTypeIngestionRef());
        // Set guest users email if provided
        if (ingestionRequest.getGuestUsersEmail() != null && !ingestionRequest.getGuestUsersEmail().isEmpty()) {
            String email = String.join(",", ingestionRequest.getGuestUsersEmail());
            technicalDetails.setGuestUsersEmail(email);
        }
        // Set whitelist IP addresses if provided
        if (ingestionRequest.getWhitelistIpAddresses() != null && !ingestionRequest.getWhitelistIpAddresses().isEmpty()) {
            String ipAddress = String.join(",", ingestionRequest.getWhitelistIpAddresses());
            technicalDetails.setWhitelistIpAddresses(ipAddress);
        }
        technicalDetails.setExternalStagingContainerName(ingestionRequest.getExternalStagingContainerName());
        technicalDetails.setDomainRequestId(ingestionRequest.getDomainRequestId());
        technicalDetails.setExternalDataSourceLocation(ingestionRequest.getExternalDataSourceLocation());
        technicalDetails.setGskAccessSourceLocationRef(ingestionRequest.getGskAccessSourceLocationRef());
        technicalDetails.setExternalSourceSecretKeyName(ingestionRequest.getExternalSourceSecretKeyName());
        technicalDetails.setIngestionRequest(ingestionRequestDetails);

        ingestionRequestDetails.setTechnicalDetails(technicalDetails);

        // Set request status details
        List<RequestStatusDetails> requestStatusDetailsList = new ArrayList<>();
        RequestStatusDetails requestStatusDetails = new RequestStatusDetails();
        Status status;
        if (submit) {
            status = statusRepository.findByStatusNameIgnoreCase(IngestionStatus.TRIAGE_PENDING_APPROVAL.toString());
        } else {
            status = statusRepository.findByStatusNameIgnoreCase(IngestionStatus.DRAFT.toString());
        }
        requestStatusDetails.setStatus(status);
        requestStatusDetails.setIngestionRequest(ingestionRequestDetails);
        requestStatusDetails.setActiveFlag(Boolean.TRUE);
        requestStatusDetails.setCreatedBy(createdBy);
        requestStatusDetails.setModifiedBy(modifiedBy);
        requestStatusDetailsList.add(requestStatusDetails);
        ingestionRequestDetails.setRequestStatusDetails(requestStatusDetailsList);

        return dtoConverter.toDto(ingestionRequestDetailsRepository.save(ingestionRequestDetails));
    }

    /**
     * Retrieves the details of an ingestion request by its ID.
     *
     * @param ingestionRequestId The ID of the ingestion request to retrieve.
     * @return The details of the ingestion request as a Data Transfer Object (DTO), or null if the request does not exist.
     */
    @Override
    public IngestionRequestDetailsDTO getIngestionRequestDetail(Long ingestionRequestId) {
        // Retrieve the ingestion request details from the repository
        Optional<IngestionRequestDetails> requestDetails = ingestionRequestDetailsRepository.findById(ingestionRequestId);
        if (requestDetails.isEmpty()) {
            // Handling case when requestId is not found.
            throw new ApiException(HttpStatus.NOT_FOUND, ErrorCode.REQUEST_ID_NOT_FOUND);
        }
        return dtoConverter.toDto(requestDetails.get());
    }

    /**
     * Updates the status of an ingestion request and optionally adds decision details.
     *
     * @param ingestionRequestId The ID of the ingestion request to update.
     * @param newStatus          The new status to set for the ingestion request.
     * @param decisionRequestDTO The decision details including comments, rejection reason, etc based on the required conditions.
     * @return The updated details of the ingestion request as a Data Transfer Object (DTO), or null if the request does not exist.
     */
    @Override
    public IngestionRequestDetailsDTO updateIngestionRequestStatus(Long ingestionRequestId, IngestionStatus newStatus, DecisionRequestDTO decisionRequestDTO) {
        Optional<IngestionRequestDetails> requestDetailsOptional = ingestionRequestDetailsRepository.findById(ingestionRequestId);
        if (requestDetailsOptional.isEmpty()) {
            // Handling case when requestId is not found.
            throw new ApiException(HttpStatus.NOT_FOUND, ErrorCode.REQUEST_ID_NOT_FOUND);
        }
        IngestionRequestDetails requestDetails = requestDetailsOptional.get();
        // As logged-in user details are not available, using static emails for createdBy and modifiedBy
        String createdBy = "testStatusCreated@gamil.com"; // Update with logged-in user email
        String modifyBy = "testStatusModify@gamil.com";  // Update with logged-in user email
        List<RequestStatusDetails> requestStatusDetailsList = requestDetails.getRequestStatusDetails();
        boolean statusUpdated = false;

        // Map defining valid previous statuses for each new status
        Map<IngestionStatus, String> validPreviousStatusOfRequest = Map.of(
                IngestionStatus.APPROVED, IngestionStatus.TRIAGE_PENDING_APPROVAL.toString(),
                IngestionStatus.REJECTED, IngestionStatus.TRIAGE_PENDING_APPROVAL.toString(),
                IngestionStatus.INGESTION_IN_PROGRESS, IngestionStatus.APPROVED.toString(),
                IngestionStatus.INGESTION_COMPLETED, IngestionStatus.INGESTION_IN_PROGRESS.toString(),
                IngestionStatus.INGESTION_FAILURE, IngestionStatus.INGESTION_IN_PROGRESS.toString(),
                IngestionStatus.TRIAGE_PENDING_APPROVAL, IngestionStatus.DRAFT.toString()
        );

        if (requestStatusDetailsList != null && !requestStatusDetailsList.isEmpty()) {
            // Loop through existing status details and deactivate the current active status
            for (RequestStatusDetails requestStatus : requestStatusDetailsList) {
                if (requestStatus.getActiveFlag() && requestStatus.getStatus().getStatusName().equalsIgnoreCase(validPreviousStatusOfRequest.getOrDefault(newStatus, null))) {
                    requestStatus.setActiveFlag(Boolean.FALSE);
                    requestStatus.setModifiedBy(modifyBy);
                    statusUpdated = true;
                }
            }

            if (statusUpdated) {
                // Create a new status detail record for the new status
                RequestStatusDetails newRequestStatusDetails = new RequestStatusDetails();
                newRequestStatusDetails.setIngestionRequest(requestDetails);
                newRequestStatusDetails.setActiveFlag(Boolean.TRUE);
                newRequestStatusDetails.setCreatedBy(createdBy);
                newRequestStatusDetails.setModifiedBy(modifyBy);
                newRequestStatusDetails.setStatus(statusRepository.findByStatusNameIgnoreCase(newStatus.toString()));
                // If decision details are provided, set them in the new status detail
                if (decisionRequestDTO != null) {
                    if (decisionRequestDTO.getDecisionComments() != null) {
                        newRequestStatusDetails.setDecisionComments(decisionRequestDTO.getDecisionComments());
                        newRequestStatusDetails.setRejectionReason(decisionRequestDTO.getRejectionReason());
                        newRequestStatusDetails.setDecisionDate(new Date());
                        // For decisionByName, decisionByMudid, decisionByEmail values are not stored as user login functionality is not included as Authentication or Authorization is not included
                        // For notify_through_email value nothing is provided for Email Template Data for Email Sending so not working with this value
                    }
                    // Update technical details if provided
                    if (decisionRequestDTO.getExistingDataLocationIdentified() != null && !decisionRequestDTO.getExistingDataLocationIdentified().isEmpty()) {
                        TechnicalDetails technicalDetails = requestDetails.getTechnicalDetails();
                        technicalDetails.setExistingDataLocationIdentified(decisionRequestDTO.getExistingDataLocationIdentified());
                        technicalDetails.setIngestionRequest(requestDetails);
                        technicalDetails.setModifiedBy(modifyBy);
                        technicalDetailsRepository.save(technicalDetails);
                    }
                }
                requestStatusDetailsList.add(newRequestStatusDetails);
                requestStatusRepository.saveAll(requestStatusDetailsList);
            }
        }
        return dtoConverter.toDto(requestDetails);
    }


    /**
     * Searches for ingestion requests based on specified criteria.
     *
     * @param myApprovals    Flag indicating whether to search for requests are in Non-Draft status.
     * @param mySubmissions  Flag indicating whether to search for requests submitted by the logged-in user.
     * @param status         Status of the ingestion requests to search for.
     * @param page           Page number of the search results.
     * @param perPage        Number of items per page in the search results.
     * @param orderBy        Field to order the results by.
     * @param orderDirection Direction of the ordering (ascending or descending).
     * @return An IngestionRequestSummaryDTO containing the search results.
     */
    @Override
    public IngestionRequestSummaryDTO searchIngestionRequests(boolean myApprovals, boolean mySubmissions, IngestionRequestStatus status,
                                                              int page, int perPage, OrderByField orderBy, OrderDirection orderDirection) {
        // Create a sort object based on the provided field and direction
        Sort sort = orderDirection == OrderDirection.ASC ? Sort.by(orderBy.getFieldName()).ascending() : Sort.by(orderBy.getFieldName()).descending();
        // Create a pageable object for pagination
        Pageable pageable = PageRequest.of(page - 1, perPage, sort);

        IngestionRequestSummaryDTO summary = new IngestionRequestSummaryDTO();
        Page<IngestionRequestDetails> requestDetails = null;
        Boolean activeFlag = Boolean.TRUE;
        if (!myApprovals) {
            if (mySubmissions) {
                // Filter requests based on status
                if (status == IngestionRequestStatus.All) {
                    requestDetails = ingestionRequestDetailsRepository.findByRequestStatusDetailsStatusStatusNameInAndRequestStatusDetailsActiveFlag(Arrays.asList(IngestionStatus.TRIAGE_PENDING_APPROVAL.toString(),
                            IngestionStatus.APPROVED.toString(), IngestionStatus.REJECTED.toString(), IngestionStatus.INGESTION_IN_PROGRESS.toString(), IngestionStatus.INGESTION_COMPLETED.toString()
                            , IngestionStatus.INGESTION_FAILURE.toString()), activeFlag, pageable);
                } else if (status == IngestionRequestStatus.PendingApproval) {
                    requestDetails = ingestionRequestDetailsRepository.findByRequestStatusDetailsStatusStatusNameInAndRequestStatusDetailsActiveFlag(Arrays.asList(IngestionStatus.TRIAGE_PENDING_APPROVAL.toString(), IngestionStatus.INGESTION_IN_PROGRESS.toString()), activeFlag, pageable);
                } else if (status == IngestionRequestStatus.CompletedRequest) {
                    requestDetails = ingestionRequestDetailsRepository.findByRequestStatusDetailsStatusStatusNameInAndRequestStatusDetailsActiveFlag(Arrays.asList(IngestionStatus.INGESTION_COMPLETED.toString()), activeFlag, pageable);
                } else if (status == IngestionRequestStatus.Rejected) {
                    requestDetails = ingestionRequestDetailsRepository.findByRequestStatusDetailsStatusStatusNameInAndRequestStatusDetailsActiveFlag(Arrays.asList(IngestionStatus.REJECTED.toString()), activeFlag, pageable);
                }
                // Set summary totals based on filtered results
                summary.setTotalPendingApproval(ingestionRequestDetailsRepository.countByRequestStatusDetailsStatusStatusNameInAndRequestStatusDetailsActiveFlag(Arrays.asList(IngestionStatus.TRIAGE_PENDING_APPROVAL.toString(), IngestionStatus.INGESTION_IN_PROGRESS.toString()), activeFlag));
                summary.setTotalCompletedRequest(ingestionRequestDetailsRepository.countByRequestStatusDetailsStatusStatusNameInAndRequestStatusDetailsActiveFlag(Arrays.asList(IngestionStatus.INGESTION_COMPLETED.toString()), activeFlag));
                summary.setTotalRejected(ingestionRequestDetailsRepository.countByRequestStatusDetailsStatusStatusNameInAndRequestStatusDetailsActiveFlag(Arrays.asList(IngestionStatus.REJECTED.toString()), activeFlag));
            }
        } else {
            requestDetails = ingestionRequestDetailsRepository.findByRequestStatusDetailsStatusStatusNameInAndRequestStatusDetailsActiveFlag(Arrays.asList(IngestionStatus.DRAFT.toString()), activeFlag, pageable);
        }
        // Process the search results
        if (requestDetails != null) {
            // Convert request details to DTOs and set them in the summary
            List<IngestionRequestDetailsDTO> requestDetailsDTOs = requestDetails.getContent().stream()
                    .map(item -> dtoConverter.toDto(item))
                    .toList();
            summary.setItems(requestDetailsDTOs);
            summary.setTotalAll((int) requestDetails.getTotalElements());
            return summary;
        }
        return null; // Return null if no results were found
    }


}
