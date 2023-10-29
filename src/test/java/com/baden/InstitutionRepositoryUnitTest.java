package com.baden;

import com.baden.entity.InstitutionEntity;
import com.baden.repository.InstitutionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DataJpaTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@TestPropertySource(properties = "spring.sql.init.mode=never")
class InstitutionRepositoryUnitTest {

    @Autowired
    InstitutionRepository institutionRepository;


    @BeforeEach
    void setUp() {
        institutionRepository.save(new InstitutionEntity(
                "Binary Academy",
                "The school of 1's and 0's",
                "info@binaryacdemy.com",
                "011 111 1111",
                "binaryacdemy.com"));

        institutionRepository.save(new InstitutionEntity(
                "The Code Hub",
                "Where the greatest programing minds are created",
                "info@codehub.com",
                "+27 22 222 2222",
                "codehub.edu"));
    }

    @AfterEach
    void destroy() {
        institutionRepository.deleteAll();
    }

    @Test
    void saveInstitution() {
        InstitutionEntity institutionEntity = new InstitutionEntity(
                "Programming Wiz",
                "The place to be to become the Harry Potter of code.",
                "help@programmingwiz.com",
                null,
                "programmingwiz.co.za");

        institutionRepository.save(institutionEntity);
        List<InstitutionEntity> institutionEntityList = institutionRepository.findAll();

        assertNotNull(institutionEntity);
        assertTrue(institutionEntityList.size() == 3);
        assertEquals(institutionEntityList.get(2).getInstitution_id(), 3);
        assertEquals(institutionEntityList.get(2).getInstitution_name(), "Programming Wiz");
    }

    @Test
    void getAllInstitutions() {
        List<InstitutionEntity> institutionEntityList = institutionRepository.findAll();

        assertNotNull(institutionEntityList);
        assertTrue(institutionEntityList.size() == 2);
        assertEquals(institutionEntityList.get(0).getInstitution_id(), 1);
        assertEquals(institutionEntityList.get(0).getInstitution_name(), "Binary Academy");
        assertEquals(institutionEntityList.get(1).getInstitution_id(), 2);
        assertEquals(institutionEntityList.get(1).getInstitution_name(), "The Code Hub");
    }

    @Test
    void getInstitutionById() {
        Integer institution_id = 2;
        InstitutionEntity institutionEntity = institutionRepository.findById(institution_id).get();

        assertNotNull(institutionEntity);
        assertEquals(institutionEntity.getInstitution_name(), "The Code Hub");
        assertEquals(institutionEntity.getInstitution_description(), "Where the greatest programing minds are created");
        assertEquals(institutionEntity.getInstitution_email(), "info@codehub.com");
        assertEquals(institutionEntity.getInstitution_phone(), "+27 22 222 2222");
        assertEquals(institutionEntity.getInstitution_website(), "codehub.edu");
    }

    @Test
    void getInvalidInstitutionId() {
        Integer institution_id = 100;

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            institutionRepository.findById(institution_id).get();
        });

        assertNotNull(exception);
        assertEquals(exception.getMessage(), "No value present");
    }

    @Test
    void updateInstitutionId() {
        Integer institution_id = 1;

        InstitutionEntity newUpdates = new InstitutionEntity();
        newUpdates.setInstitution_description("The school of one's and zero's");

        InstitutionEntity existingInstitution = institutionRepository.findById(institution_id).get();

        //For the sake of the test I'm first checking what the description original
        assertNotNull(existingInstitution);
        assertEquals(existingInstitution.getInstitution_description(), "The school of 1's and 0's");

        existingInstitution.setInstitution_description(newUpdates.getInstitution_description());

        InstitutionEntity updatedInstitution = institutionRepository.save(existingInstitution);

        //Now I'm confirming if the update was applied
        assertNotNull(updatedInstitution);
        assertEquals(existingInstitution.getInstitution_description(), "The school of one's and zero's");
    }

    @Test
    void deleteInstitution() {
        Integer institution_id = 2;

        //Delete the institution
        institutionRepository.deleteById(institution_id);

        //To test if successfully deleted I query for the deleted institution and because it doesn't exist anymore it should throw an exception
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            institutionRepository.findById(institution_id).get();
        });

        assertNotNull(exception);
        assertEquals(exception.getMessage(), "No value present");
    }

}