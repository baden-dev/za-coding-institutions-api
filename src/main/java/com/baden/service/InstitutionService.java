package com.baden.service;

import com.baden.customResponse.SuccessResponse;
import com.baden.customResponse.exception.NullFieldException;
import com.baden.entity.InstitutionEntity;
import com.baden.repository.InstitutionRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class InstitutionService {

    private final InstitutionRepository institutionRepository;

    public InstitutionService(InstitutionRepository institutionRepository) {
        this.institutionRepository = institutionRepository;
    }

    // Save new institution to database
    public InstitutionEntity saveInstitution(InstitutionEntity institutionEntity) {
        //Method to check if any of the compulsory fields are null and if true it throws an exception
        checkMissingCompulsoryFields(institutionEntity);
        return institutionRepository.save(institutionEntity);
    }

    // Get all institutions
    public List<InstitutionEntity> getAllInstitutions() {
        return institutionRepository.findAll();
    }

    // Get institution by id
    public InstitutionEntity getInstitutionById(Integer institution_id) {
        Optional<InstitutionEntity> institutionEntity = institutionRepository.findById(institution_id);
        if (institutionEntity.isPresent()) {
            return institutionEntity.get();
        } else {
            throw new NoSuchElementException("Error fetching, no such institution present with ID = " + institution_id);
        }
    }

    // Update institution details by id
    public InstitutionEntity updateInstitution(Integer institution_id, InstitutionEntity updatedInstitution) {
        // Retrieve the entity that needs to be updated
        InstitutionEntity existingInstitutionEntity;
        try {
            existingInstitutionEntity = institutionRepository.findById(institution_id).get();
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Error updating, no such institution present with ID = " + institution_id);
        }

        // Update the entities information
        if (updatedInstitution.getInstitution_name() != null) {
            existingInstitutionEntity.setInstitution_name(updatedInstitution.getInstitution_name());
        }
        if (updatedInstitution.getInstitution_description() != null) {
            existingInstitutionEntity.setInstitution_description(updatedInstitution.getInstitution_description());
        }
        if (updatedInstitution.getInstitution_email() != null) {
            existingInstitutionEntity.setInstitution_email(updatedInstitution.getInstitution_email());
        }
        if (updatedInstitution.getInstitution_phone() != null) {
            existingInstitutionEntity.setInstitution_phone(updatedInstitution.getInstitution_phone());
        }
        if (updatedInstitution.getInstitution_website() != null) {
            existingInstitutionEntity.setInstitution_website(updatedInstitution.getInstitution_website());
        }

        // Save updated entity
        return institutionRepository.save(existingInstitutionEntity);
    }

    // Delete institution by id
    public SuccessResponse deleteInstitution(Integer institution_id) {
        if (institutionRepository.findById(institution_id).isEmpty()) {
            throw new NoSuchElementException("Error deleting, no such institution present with ID = " + institution_id);
        }
        institutionRepository.deleteById(institution_id);
        return new SuccessResponse(200, "Institution deleted successfully!");
    }

    public static void checkMissingCompulsoryFields(InstitutionEntity institutionEntity) {
        // I am avoiding the use of a try-catch block for PropertyValueException here because it is thrown at a lower level
        // by Hibernate when database constraints are violated, making it challenging to provide a custom error message.
        // Instead, I am validating and checking for required fields before attempting to save the entity,
        // and I throw a CustomException with a meaningful error message if any required field (institution_name, institution_email or institution_website) is missing.
        List<String> missingFields = new ArrayList<>();

        if (institutionEntity.getInstitution_name() == null || institutionEntity.getInstitution_email() == null || institutionEntity.getInstitution_website() == null) {

            if (institutionEntity.getInstitution_name() == null) {
                missingFields.add("institution_name");
            }
            if (institutionEntity.getInstitution_email() == null) {
                missingFields.add("institution_email");
            }
            if (institutionEntity.getInstitution_website() == null) {
                missingFields.add("institution_website");
            }

            throw new NullFieldException("The following compulsory field(s) are null: " + String.join(", ", missingFields));
        }
    }

}
