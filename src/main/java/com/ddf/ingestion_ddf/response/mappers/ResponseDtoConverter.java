package com.ddf.ingestion_ddf.response.mappers;

import com.ddf.ingestion_ddf.entity.*;
import com.ddf.ingestion_ddf.repository.ValidationNotesRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ResponseDtoConverter {

    @Autowired
    private ValidationNotesRepository validationNotesRepository;

    /**
     * Converts a ValidationNotes entity to its DTO representation.
     *
     * @param validationNotes The ValidationNotes entity to convert.
     * @return The DTO representation of the validation notes.
     */
    public ValidationNotesDTO toDto(ValidationNotes validationNotes) {
        ValidationNotesDTO dto = new ValidationNotesDTO();
        BeanUtils.copyProperties(validationNotes, dto);
        return dto;
    }

    /**
     * Maps the active request status details to DTO.
     *
     * @param requestStatusDetails the list of request status details
     * @return the DTO representation of the active request status
     */
    public RequestStatusDetailsDTO toDto(List<RequestStatusDetails> requestStatusDetails) {
        // Check if the list is null or empty
        if (requestStatusDetails == null || requestStatusDetails.isEmpty()) {
            return null;
        }

        // Find the active request status from the list
        RequestStatusDetails activeRequestStatus = requestStatusDetails.stream()
                .filter(status -> status.getActiveFlag() != null && status.getActiveFlag())
                .findFirst()
                .orElse(null);

        // If no active request status found, return null
        if (activeRequestStatus == null) {
            return null;
        }

        // Map details of the active request status to a RequestStatusDetailsDTO
        RequestStatusDetailsDTO activeRequestStatusDTO = new RequestStatusDetailsDTO();
        activeRequestStatusDTO.setRequestStatusId(activeRequestStatus.getRequestStatusId());
        activeRequestStatusDTO.setIngestionRequestId(activeRequestStatus.getIngestionRequest().getIngestionRequestId());
        activeRequestStatusDTO.setDecisionByName(activeRequestStatus.getDecisionByName());
        activeRequestStatusDTO.setDecisionByMudid(activeRequestStatus.getDecisionByMudid());
        activeRequestStatusDTO.setDecisionByEmail(activeRequestStatus.getDecisionByEmail());
        activeRequestStatusDTO.setDecisionDate(activeRequestStatus.getDecisionDate());
        activeRequestStatusDTO.setDecisionComments(activeRequestStatus.getDecisionComments());
        activeRequestStatusDTO.setRejectionReason(activeRequestStatus.getRejectionReason());
        activeRequestStatusDTO.setActiveFlag(activeRequestStatus.getActiveFlag());
        activeRequestStatusDTO.setStatus(activeRequestStatus.getStatus());

        return activeRequestStatusDTO;
    }

    /**
     * Converts a comma-separated string to a list of strings.
     *
     * @param commaSeparatedString the comma-separated string
     * @return the list of strings
     */
    private List<String> convertStringToList(String commaSeparatedString) {
        // Check if the input string is null or empty
        if (commaSeparatedString == null || commaSeparatedString.isEmpty()) {
            return Collections.emptyList();
        }
        // Split the input string at commas and return as a list
        return Arrays.asList(commaSeparatedString.split(","));
    }

    /**
     * Converts an IngestionRequestDetails entity to its corresponding Response Data Transfer Object (DTO).
     *
     * @param ingestionRequestDetails The IngestionRequestDetails entity to convert.
     * @return The converted IngestionRequestDetailsDTO containing details of the ingestion request.
     */
    public IngestionRequestDetailsDTO toDto(IngestionRequestDetails ingestionRequestDetails) {
        IngestionRequestDetailsDTO ingestRequestDetailsDTO = new IngestionRequestDetailsDTO();
        ingestRequestDetailsDTO.setIngestionRequestId(ingestionRequestDetails.getIngestionRequestId());
        ingestRequestDetailsDTO.setRequesterName(ingestionRequestDetails.getRequesterName());
        ingestRequestDetailsDTO.setRequesterMudid(ingestionRequestDetails.getRequesterMudid());
        ingestRequestDetailsDTO.setRequesterEmail(ingestionRequestDetails.getRequesterEmail());

        // Map dataset details from the entity to the DTO
        if (ingestionRequestDetails.getDatasetDetails() != null) {
            DatasetDetails datasetDetails = ingestionRequestDetails.getDatasetDetails();
            ingestRequestDetailsDTO.setDatasetName(datasetDetails.getDatasetName());

            if (datasetDetails.getDatasetRoleDetails() != null) {
                for (DatasetRoleDetails details : datasetDetails.getDatasetRoleDetails()) {
                    if (details.getRole().equalsIgnoreCase("Data SME")) {
                        // Map Data SME role details
                        ingestRequestDetailsDTO.setDatasetSmeName(details.getName());
                        ingestRequestDetailsDTO.setDatasetSmeMudid(details.getMudid());
                        ingestRequestDetailsDTO.setDatasetSmeEmail(details.getEmail());
                    } else if (details.getRole().equalsIgnoreCase("Data owner")) {
                        // Map Data owner role details
                        ingestRequestDetailsDTO.setDatasetOwnerName(details.getName());
                        ingestRequestDetailsDTO.setDatasetOwnerMudid(details.getMudid());
                        ingestRequestDetailsDTO.setDatasetOwnerEmail(details.getEmail());
                    } else if (details.getRole().equalsIgnoreCase("Data steward")) {
                        // Map Data steward role details
                        ingestRequestDetailsDTO.setDatasetStewardName(details.getName());
                        ingestRequestDetailsDTO.setDatasetStewardMudid(details.getMudid());
                        ingestRequestDetailsDTO.setDatasetStewardEmail(details.getEmail());
                    }
                }
            }

            ingestRequestDetailsDTO.setDatasetOriginSource(datasetDetails.getDatasetOriginSource());
            ingestRequestDetailsDTO.setCurrentDataLocationRef(datasetDetails.getCurrentDataLocationRef());
            ingestRequestDetailsDTO.setMeteorSpaceDominoUsageFlag(datasetDetails.getMeteorSpaceDominoUsageFlag());
            ingestRequestDetailsDTO.setIhdFlag(datasetDetails.getIhdFlag());
            ingestRequestDetailsDTO.setDatasetRequiredForRef(datasetDetails.getDatasetRequiredForRef());
            ingestRequestDetailsDTO.setEstimatedDataVolumeRef(datasetDetails.getEstimatedDataVolumeRef());
            ingestRequestDetailsDTO.setAnalysisInitDt(datasetDetails.getAnalysisInitDt());
            ingestRequestDetailsDTO.setAnalysisEndDt(datasetDetails.getAnalysisEndDt());
            ingestRequestDetailsDTO.setDtaContractCompleteFlag(datasetDetails.getDtaContractCompleteFlag());
            ingestRequestDetailsDTO.setDtaExpectedCompletionDate(datasetDetails.getDtaExpectedCompletionDate());
            ingestRequestDetailsDTO.setDatasetTypeRef(datasetDetails.getDatasetTypeRef());
            ingestRequestDetailsDTO.setContractPartner(datasetDetails.getContractPartner());
            ingestRequestDetailsDTO.setRetentionRules(datasetDetails.getRetentionRules());
            ingestRequestDetailsDTO.setRetentionRulesContractDate(datasetDetails.getRetentionRulesContractDate());
            ingestRequestDetailsDTO.setInformationClassificationTypeRef(datasetDetails.getInformationClassificationTypeRef());
            ingestRequestDetailsDTO.setPiiTypeRef(datasetDetails.getPiiTypeRef());

            if (datasetDetails.getDatasetDataCategories() != null) {
                List<String> datasetDataCategories = new ArrayList<>();
                for (DatasetDataCategory category : datasetDetails.getDatasetDataCategories()) {
                    datasetDataCategories.add(category.getDataCategoryRef());
                }
                ingestRequestDetailsDTO.setDataCategoryRefs(datasetDataCategories);
            }

            if (datasetDetails.getDatasetStudies() != null) {
                List<String> studyIds = new ArrayList<>();
                for (DatasetStudy study : datasetDetails.getDatasetStudies()) {
                    studyIds.add(study.getStudyId());
                }
                ingestRequestDetailsDTO.setStudyIds(studyIds);
            }

            if (datasetDetails.getDatasetUserUsageRestriction() != null) {
                List<String> userRestrictions = new ArrayList<>();
                List<String> usageRestrictions = new ArrayList<>();
                for (DatasetUserUsageRestriction user : datasetDetails.getDatasetUserUsageRestriction()) {
                    if (user.getRestrictionTypeRef().equalsIgnoreCase("user_restrictions")) {
                        userRestrictions.add(user.getRestrictionRef());
                    } else if (user.getRestrictionTypeRef().equalsIgnoreCase("usage_restrictions")) {
                        usageRestrictions.add(user.getRestrictionRef());
                    }
                }
                ingestRequestDetailsDTO.setUserRestrictions(userRestrictions);
                ingestRequestDetailsDTO.setUsageRestrictions(usageRestrictions);
            }

            if (datasetDetails.getDatasetTherapies() != null) {
                List<String> therapies = new ArrayList<>();
                for (DatasetTherapy therapy : datasetDetails.getDatasetTherapies()) {
                    therapies.add(therapy.getTherapy());
                }
                ingestRequestDetailsDTO.setTherapyAreas(therapies);
            }

            if (datasetDetails.getDatasetTechAndAssays() != null) {
                List<String> techAndAssayList = new ArrayList<>();
                for (DatasetTechAndAssay techAndAssay : datasetDetails.getDatasetTechAndAssays()) {
                    techAndAssayList.add(techAndAssay.getTechniqueAndAssay());
                }
                ingestRequestDetailsDTO.setTechniqueAndAssays(techAndAssayList);
            }

            if (datasetDetails.getDatasetIndications() != null) {
                List<String> indicationList = new ArrayList<>();
                for (DatasetIndication indication : datasetDetails.getDatasetIndications()) {
                    indicationList.add(indication.getIndication());
                }
                ingestRequestDetailsDTO.setIndications(indicationList);
            }


        }

        if (ingestionRequestDetails.getTechnicalDetails() != null) {
            TechnicalDetails technicalDetails = ingestionRequestDetails.getTechnicalDetails();
            ingestRequestDetailsDTO.setDataLocationPath(technicalDetails.getDataLocationPath());
            ingestRequestDetailsDTO.setDataRefreshFrequency(technicalDetails.getDataRefreshFrequency());
            ingestRequestDetailsDTO.setTargetIngestionStartDate(technicalDetails.getTargetIngestionStartDate());
            ingestRequestDetailsDTO.setTargetIngestionEndDate(technicalDetails.getTargetIngestionEndDate());
            ingestRequestDetailsDTO.setTargetPath(technicalDetails.getTargetPath());
            ingestRequestDetailsDTO.setDatasetTypeIngestionRef(technicalDetails.getDatasetTypeIngestionRef());
            ingestRequestDetailsDTO.setGuestUsersEmail(convertStringToList(technicalDetails.getGuestUsersEmail()));
            ingestRequestDetailsDTO.setWhitelistIpAddresses(convertStringToList(technicalDetails.getWhitelistIpAddresses()));
            ingestRequestDetailsDTO.setExternalStagingContainerName(technicalDetails.getExternalStagingContainerName());
            ingestRequestDetailsDTO.setDomainRequestId(technicalDetails.getDomainRequestId());
            ingestRequestDetailsDTO.setExternalDataSourceLocation(technicalDetails.getExternalDataSourceLocation());
            ingestRequestDetailsDTO.setGskAccessSourceLocationRef(technicalDetails.getGskAccessSourceLocationRef());
            ingestRequestDetailsDTO.setExternalSourceSecretKeyName(technicalDetails.getExternalSourceSecretKeyName());
            ingestRequestDetailsDTO.setExistingDataLocationIdentified(technicalDetails.getExistingDataLocationIdentified());

        }
        ingestRequestDetailsDTO.setRequestRationaleReason(ingestionRequestDetails.getRequestRationaleReason());
        ingestRequestDetailsDTO.setModifiedReason(ingestionRequestDetails.getModifiedReason());
        ingestRequestDetailsDTO.setActiveRequestStatus(toDto(ingestionRequestDetails.getRequestStatusDetails()));
        List<ValidationNotes> validationNotes = validationNotesRepository.findByIngestionRequest(ingestionRequestDetails);
        if (validationNotes != null) {
            ingestRequestDetailsDTO.setNotes(validationNotes.stream().map(this::toDto).collect(Collectors.toList()));
        }
        ingestRequestDetailsDTO.setCreatedBy(ingestionRequestDetails.getCreatedBy());
        ingestRequestDetailsDTO.setCreatedDate(ingestionRequestDetails.getCreatedDate());
        ingestRequestDetailsDTO.setModifiedBy(ingestionRequestDetails.getModifiedBy());
        ingestRequestDetailsDTO.setModifiedDate(ingestionRequestDetails.getModifiedDate());
        return ingestRequestDetailsDTO;
    }

}
