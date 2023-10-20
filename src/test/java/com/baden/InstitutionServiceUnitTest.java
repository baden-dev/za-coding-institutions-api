package com.baden;

import com.baden.customResponse.SuccessResponse;
import com.baden.customResponse.exception.NullFieldException;
import com.baden.entity.InstitutionEntity;
import com.baden.repository.InstitutionRepository;
import com.baden.service.InstitutionService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class InstitutionServiceUnitTest {

    @InjectMocks
    private InstitutionService institutionService;

    @Mock
    private InstitutionRepository institutionRepository;

    @Nested
    class saveInstitutionMethodTests {

        @Test
        void saveInstitution_whenEntityContainsAllCompulsoryFields_shouldReturnEntity() {
            InstitutionEntity institutionEntity = new InstitutionEntity(
                    "The Code Hub",
                    "Where the greatest programing minds are created",
                    "info@codehub.com",
                    "+27 22 222 2222",
                    "codehub.edu");

            when(institutionRepository.save(any(InstitutionEntity.class))).thenReturn(institutionEntity);

            InstitutionEntity result = institutionService.saveInstitution(institutionEntity);

            assertEquals("The Code Hub", result.getInstitution_name());
            assertEquals("Where the greatest programing minds are created", result.getInstitution_description());
            assertEquals("info@codehub.com", result.getInstitution_email());
            assertEquals("+27 22 222 2222", result.getInstitution_phone());
            assertEquals("codehub.edu", result.getInstitution_website());

            verify(institutionRepository).save(institutionEntity);
        }

        @Test
        void saveInstitution_whenEntityIsMissingCompulsoryFields_shouldReturnException() {
            //Fields that cannot be null are institution_name, institution_email and institution_website
            InstitutionEntity institutionEntity = new InstitutionEntity("The Code Hub",
                    "Where the greatest programing minds are created",
                    null,
                    "+27 22 222 2222",
                    null);

            when(institutionRepository.save(any(InstitutionEntity.class))).thenThrow(NullFieldException.class);

            NullFieldException nullFieldException = assertThrows(NullFieldException.class, () -> {
                institutionService.saveInstitution(institutionEntity);
            });

            assertEquals("The following compulsory field(s) are null: institution_email, institution_website",
                    nullFieldException.getMessage());
        }

        @Test
        void saveInstitution_whenEmptyEntityGiven_ShouldThrowException() {
            InstitutionEntity emptyEntity = new InstitutionEntity();

            NullFieldException nullFieldException = assertThrows(NullFieldException.class, () -> {
                institutionService.saveInstitution(emptyEntity);
            });

            assertEquals("The following compulsory field(s) are null: institution_name, institution_email, institution_website",
                    nullFieldException.getMessage());
        }
    }

    @Nested
    class getAllInstitutionsMethodTests {

        @Test
        void getAllInstitutions_whenTableHasRecords_shouldReturnPopulatedList() {
            List<InstitutionEntity> institutionList = new ArrayList<>();
            institutionList.add(new InstitutionEntity(
                    "Binary Academy",
                    "The school of 1's and 0's",
                    "info@binaryacdemy.com",
                    "011 111 1111",
                    "binaryacdemy.com"));

            institutionList.add(new InstitutionEntity(
                    "The Code Hub",
                    "Where the greatest programing minds are created",
                    "info@codehub.com",
                    "+27 22 222 2222",
                    "codehub.edu"));

            when(institutionRepository.findAll()).thenReturn(institutionList);

            List<InstitutionEntity> result = institutionService.getAllInstitutions();

            assertEquals(2, result.size());
            assertThat(result, hasItem(institutionList.get(0)));
            assertThat(result, hasItem(institutionList.get(1)));
        }

        @Test
        void getAllInstitutions_whenTableHasNoRecords_shouldReturnEmptyList() {
            List<InstitutionEntity> institutionList = new ArrayList<>();

            when(institutionRepository.findAll()).thenReturn(institutionList);

            List<InstitutionEntity> result = institutionService.getAllInstitutions();

            assertEquals(0, result.size());
        }
    }

    @Nested
    class getInstitutionByIdMethodTests {

        @Test
        void getInstitutionById_whenInstitutionExists_shouldReturnEntity() {
            Integer institution_id = 1;
            InstitutionEntity institutionEntity = new InstitutionEntity(
                    "Binary Academy",
                    "The school of 1's and 0's",
                    "info@binaryacdemy.com",
                    "011 111 1111",
                    "binaryacdemy.com");

            when(institutionRepository.findById(institution_id)).thenReturn(Optional.of(institutionEntity));

            InstitutionEntity result = institutionService.getInstitutionById(institution_id);

            assertEquals(institutionEntity, result);

            verify(institutionRepository).findById(institution_id);
        }

        @Test
        void getInstitutionById_whenInstitutionDoesNotExist_shouldReturnException() {
            Integer institution_id = 100;

            NoSuchElementException noSuchElementException = assertThrows(NoSuchElementException.class, () -> {
                institutionService.getInstitutionById(institution_id);
            });

            assertEquals("Error fetching, no such institution present with ID = 100",
                    noSuchElementException.getMessage());
        }
    }

    @Nested
    class updateInstitutionMethodTest {

        @Test
        void updateInstitution_whenInstitutionExists_shouldReturnEntity() {
            Integer institution_id = 1;
            InstitutionEntity existingInstitutionEntity = new InstitutionEntity(
                    "Binary Academy",
                    "The school of 1's and 0's",
                    "info@binaryacdemy.com",
                    "011 111 1111",
                    "binaryacdemy.com");

            InstitutionEntity newUpdates = new InstitutionEntity();
            newUpdates.setInstitution_description("The school of one's and zero's");

            when(institutionRepository.findById(institution_id)).thenReturn(Optional.of(existingInstitutionEntity));
            when(institutionRepository.save(any(InstitutionEntity.class))).thenReturn(existingInstitutionEntity);

            InstitutionEntity result = institutionService.updateInstitution(institution_id, newUpdates);

            assertEquals("The school of one's and zero's", result.getInstitution_description());

            verify(institutionRepository).findById(institution_id);
            verify(institutionRepository).save(existingInstitutionEntity);
        }

        @Test
        void updateInstitution_whenUpdateHasNoChanges_shouldReturnEntity() {
            Integer institution_id = 1;
            InstitutionEntity existingInstitutionEntity = new InstitutionEntity();
            existingInstitutionEntity.setInstitution_name("Same Name");
            existingInstitutionEntity.setInstitution_description("Same Description");

            InstitutionEntity newUpdates = new InstitutionEntity();
            newUpdates.setInstitution_name("Same Name");
            newUpdates.setInstitution_description("Same Description");

            when(institutionRepository.findById(institution_id)).thenReturn(Optional.of(existingInstitutionEntity));
            when(institutionRepository.save(any(InstitutionEntity.class))).thenReturn(existingInstitutionEntity);

            InstitutionEntity result = institutionService.updateInstitution(institution_id, newUpdates);

            assertEquals("Same Name", result.getInstitution_name());
            assertEquals("Same Description", result.getInstitution_description());

            verify(institutionRepository).findById(institution_id);
            verify(institutionRepository).save(existingInstitutionEntity);
        }

        @Test
        void updateInstitution_whenInstitutionDoesNotExists_shouldReturnException() {
            Integer institution_id = 100;
            InstitutionEntity newUpdates = new InstitutionEntity();

            newUpdates.setInstitution_name("Updated Name");
            newUpdates.setInstitution_description("Updated Description");

            NoSuchElementException noSuchElementException = assertThrows(NoSuchElementException.class, () -> {
                institutionService.updateInstitution(institution_id, newUpdates);
            });

            assertEquals("Error updating, no such institution present with ID = 100",
                    noSuchElementException.getMessage());
        }
    }

    @Nested
    class deleteInstitutionMethodTest {

        @Test
        void deleteInstitution_whenInstitutionExists_shouldReturnSuccessResponse() {
            Integer institutionId = 1;
            when(institutionRepository.findById(institutionId)).thenReturn(Optional.of(new InstitutionEntity()));
            doNothing().when(institutionRepository).deleteById(institutionId);

            SuccessResponse result = institutionService.deleteInstitution(institutionId);

            assertEquals(200, result.getStatusCode());
            assertEquals("Successfully deleted Institution with ID = 1", result.getMessage());

            verify(institutionRepository).findById(institutionId);
            verify(institutionRepository).deleteById(institutionId);
        }

        @Test
        void deleteInstitution_whenInstitutionNotFound_ShouldReturnException() {
            Integer institutionId = 2;

            NoSuchElementException noSuchElementException = assertThrows(NoSuchElementException.class, () -> {
                institutionService.deleteInstitution(institutionId);
            });

            assertEquals("Error deleting, no such institution present with ID = 2",
                    noSuchElementException.getMessage());

            verify(institutionRepository).findById(institutionId);
        }
    }

}