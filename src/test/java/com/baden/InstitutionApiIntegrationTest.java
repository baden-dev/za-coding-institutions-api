package com.baden;

import com.baden.customResponse.SuccessResponse;
import com.baden.customResponse.exception.ExceptionResponse;
import com.baden.entity.InstitutionEntity;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = "spring.sql.init.mode=never")
public class InstitutionApiIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private static HttpHeaders headers;

    @BeforeAll
    public static void init() {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    private String urlWithPort() {
        return "http://localhost:" + port + "/api/v1/";
    }

    @Nested
    class postMethodTests {

        @Test
        void post_whenRequestBodyIsValid_shouldReturnCreatedEntity() {
            InstitutionEntity institutionEntity = new InstitutionEntity();
            institutionEntity.setInstitution_name("Test Name");
            institutionEntity.setInstitution_email("Test Email");
            institutionEntity.setInstitution_website("Test Website");

            HttpEntity<InstitutionEntity> entity = new HttpEntity<>(institutionEntity, headers);

            ResponseEntity<InstitutionEntity> response = restTemplate.postForEntity(urlWithPort() + "institution", entity, InstitutionEntity.class);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(1, response.getBody().getInstitution_id());
            assertEquals("Test Name", response.getBody().getInstitution_name());
            assertEquals("Test Email", response.getBody().getInstitution_email());
            assertEquals("Test Website", response.getBody().getInstitution_website());
        }

        @Test
        void post_whenRequestIsInvalid_shouldReturnErrorResponse() {
            InstitutionEntity institutionEntity = new InstitutionEntity();
            institutionEntity.setInstitution_name("Test Name");
            institutionEntity.setInstitution_email("Test Email");
            institutionEntity.setInstitution_website(null);

            HttpEntity<InstitutionEntity> entity = new HttpEntity<>(institutionEntity, headers);

            ResponseEntity<ExceptionResponse> response = restTemplate.postForEntity(urlWithPort() + "institution", entity, ExceptionResponse.class);

            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertEquals("The following compulsory field(s) are null: institution_website", response.getBody().getError());
            assertEquals("uri=/api/v1/institution", response.getBody().getPath());
        }
    }

    @Nested
    class getMethodTests {

        @Test
        void getAll_whenDatabaseIsPopulated_shouldReturnListOfInstitutions() {
            //Adding institutions into table as it is initially empty
            // Add the first institution
            InstitutionEntity institution = new InstitutionEntity();
            institution.setInstitution_name("Test 1 Name");
            institution.setInstitution_email("Test 1 Email");
            institution.setInstitution_website("Test 1 Website");

            HttpEntity<InstitutionEntity> newEntity = new HttpEntity<>(institution, headers);
            ResponseEntity<InstitutionEntity> saveResponse = restTemplate.postForEntity(urlWithPort() + "institution", newEntity, InstitutionEntity.class);

            assertEquals(HttpStatus.OK, saveResponse.getStatusCode());

            // Add the second institution
            institution = new InstitutionEntity();
            institution.setInstitution_name("Test 2 Name");
            institution.setInstitution_email("Test 2 Email");
            institution.setInstitution_website("Test 2 Website");

            newEntity = new HttpEntity<>(institution, headers);
            saveResponse = restTemplate.postForEntity(urlWithPort() + "institution", newEntity, InstitutionEntity.class);

            assertEquals(HttpStatus.OK, saveResponse.getStatusCode());

            //Getting all institutions
            HttpEntity<String> entity = new HttpEntity<>(null, headers);
            ResponseEntity<List<InstitutionEntity>> response = restTemplate.exchange(urlWithPort() + "institutions",
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<>() {
                    });

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(2, response.getBody().size());
            assertEquals(1, response.getBody().get(0).getInstitution_id());
            assertEquals("Test 1 Name", response.getBody().get(0).getInstitution_name());
            assertEquals(2, response.getBody().get(1).getInstitution_id());
            assertEquals("Test 2 Name", response.getBody().get(1).getInstitution_name());
        }

        @Test
        void getAll_whenDatabaseIsEmpty_shouldReturnEmptyList() {
            HttpEntity<String> entity = new HttpEntity<>(null, headers);
            ResponseEntity<List<InstitutionEntity>> response = restTemplate.exchange(urlWithPort() + "institutions",
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<>() {
                    });

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(0, response.getBody().size());
        }

        @Test
        void getOne_whenInstitutionExists_shouldReturnInstitution() {
            InstitutionEntity institution = new InstitutionEntity();
            institution.setInstitution_name("Test Name");
            institution.setInstitution_email("Test Email");
            institution.setInstitution_website("Test Website");

            HttpEntity<InstitutionEntity> newEntity = new HttpEntity<>(institution, headers);
            ResponseEntity<InstitutionEntity> response = restTemplate.postForEntity(urlWithPort() + "institution", newEntity, InstitutionEntity.class);

            assertEquals(HttpStatus.OK, response.getStatusCode());

            //Getting institution by ID
            response = restTemplate.getForEntity(urlWithPort() + "institution/1", InstitutionEntity.class);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(1, response.getBody().getInstitution_id());
            assertEquals("Test Name", response.getBody().getInstitution_name());
            assertEquals("Test Email", response.getBody().getInstitution_email());
            assertEquals("Test Website", response.getBody().getInstitution_website());
        }

        @Test
        void getOne_whenInstitutionNotFound_shouldReturnErrorResponse() {
            ResponseEntity<ExceptionResponse> response = restTemplate.getForEntity(urlWithPort() + "institution/100", ExceptionResponse.class);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
            assertEquals("Error fetching, no such institution present with ID = 100", response.getBody().getError());
            assertEquals("uri=/api/v1/institution/100", response.getBody().getPath());
        }
    }

    @Nested
    class putMethodTests {

        @Test
        void put_whenInstitutionExists_shouldReturnUpdatedEntity() {
            //Creating an institution in database that will be used to test the update method
            InstitutionEntity existingInstitution = new InstitutionEntity();
            existingInstitution.setInstitution_name("Test 1 Name");
            existingInstitution.setInstitution_email("Test 1 Email");
            existingInstitution.setInstitution_website("Test 1 Website");

            HttpEntity<InstitutionEntity> entity = new HttpEntity<>(existingInstitution, headers);
            ResponseEntity<InstitutionEntity> createResponse = restTemplate.postForEntity(urlWithPort() + "institution", entity, InstitutionEntity.class);

            assertEquals(HttpStatus.OK, createResponse.getStatusCode());

            //Updating existing institution
            // Setting up institution entity that will hold the values of th fields that we are updating
            InstitutionEntity update = new InstitutionEntity();
            update.setInstitution_name("Updated Name");

            entity = new HttpEntity<>(update, headers);

            ResponseEntity<InstitutionEntity> response = restTemplate.exchange(
                    urlWithPort() + "institution/1",
                    HttpMethod.PUT,
                    entity,
                    InstitutionEntity.class
            );

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(1, response.getBody().getInstitution_id());
            assertEquals("Updated Name", response.getBody().getInstitution_name());
            assertEquals("Test 1 Email", response.getBody().getInstitution_email());
            assertEquals("Test 1 Website", response.getBody().getInstitution_website());
        }


        @Test
        void put_InstitutionNotFound_shouldReturnErrorResponse() {
            InstitutionEntity update = new InstitutionEntity();
            update.setInstitution_name("Updated Name");

            HttpEntity<InstitutionEntity> entity = new HttpEntity<>(update, headers);

            ResponseEntity<ExceptionResponse> response = restTemplate.exchange(
                    urlWithPort() + "institution/100",
                    HttpMethod.PUT,
                    entity,
                    ExceptionResponse.class
            );

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
            assertEquals("Error updating, no such institution present with ID = 100", response.getBody().getError());
            assertEquals("uri=/api/v1/institution/100", response.getBody().getPath());
        }
    }

    @Nested
    class deleteMethodTests {

        @Test
        void delete_whenInstitutionExists_shouldReturnSuccessResponse() {
            //Creating an institution in database that will be used to test the delete method
            InstitutionEntity existingInstitution = new InstitutionEntity();
            existingInstitution.setInstitution_name("Test Name");
            existingInstitution.setInstitution_email("Test Email");
            existingInstitution.setInstitution_website("Test Website");

            HttpEntity<InstitutionEntity> entity = new HttpEntity<>(existingInstitution, headers);
            ResponseEntity<InstitutionEntity> createResponse = restTemplate.postForEntity(urlWithPort() + "institution", entity, InstitutionEntity.class);

            assertEquals(1, createResponse.getBody().getInstitution_id());
            assertEquals(HttpStatus.OK, createResponse.getStatusCode());

            //Delete existing institution
            ResponseEntity<SuccessResponse> response = restTemplate.exchange(
                    urlWithPort() + "institution/1",
                    HttpMethod.DELETE,
                    entity,
                    SuccessResponse.class
            );

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals("Successfully deleted Institution with ID = 1", response.getBody().getMessage());
        }


        @Test
        void delete_InstitutionNotFound_shouldReturnErrorResponse() {
            ResponseEntity<ExceptionResponse> response = restTemplate.exchange(
                    urlWithPort() + "institution/100",
                    HttpMethod.DELETE,
                    null,
                    ExceptionResponse.class
            );

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
            assertEquals("Error deleting, no such institution present with ID = 100", response.getBody().getError());
            assertEquals("uri=/api/v1/institution/100", response.getBody().getPath());
        }
    }

}
