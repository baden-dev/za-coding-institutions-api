package com.baden;

import com.baden.controller.InstitutionController;
import com.baden.customResponse.SuccessResponse;
import com.baden.customResponse.exception.NullFieldException;
import com.baden.entity.InstitutionEntity;
import com.baden.service.InstitutionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InstitutionController.class)
class InstitutionControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InstitutionService institutionService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Nested
    class saveMethodTests {

        @Test
        void save_whenRequestBodyContainsAllCompulsoryFields_shouldReturnCreatedEntity() throws Exception {
            //Fields set to not null are institution_name, institution_email and institution_website.
            InstitutionEntity institutionEntity = new InstitutionEntity();
            institutionEntity.setInstitution_name("The IT School");
            institutionEntity.setInstitution_email("help@itschool.com");
            institutionEntity.setInstitution_website("itschool.co.za");

            when(institutionService.saveInstitution(institutionEntity)).thenReturn(institutionEntity);

            mockMvc.perform(post("/api/v1/institution")
                            .content(objectMapper.writeValueAsString(institutionEntity))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$").isNotEmpty())
                    .andExpect(jsonPath("$.institution_name", is("The IT School")))
                    .andExpect(jsonPath("$.institution_description", is(nullValue())))
                    .andExpect(jsonPath("$.institution_email", is("help@itschool.com")))
                    .andExpect(jsonPath("$.institution_phone", is(nullValue())))
                    .andExpect(jsonPath("$.institution_website", is("itschool.co.za")));

            verify(institutionService).saveInstitution(institutionEntity);
        }

        @Test
        void save_whenRequestBodyIsMissingCompulsoryFields_shouldReturnExceptionResponse() throws Exception {
            //Fields set to not null are institution_name, institution_email and institution_website.
            InstitutionEntity institutionEntity = new InstitutionEntity(
                    "The IT School",
                    "Just like the name says it's the IT school to be at for IT",
                    null,
                    "+27 33 333 3333",
                    "itschool.co.za");

            when(institutionService.saveInstitution(institutionEntity)).thenReturn(institutionEntity);

            when(institutionService.saveInstitution(institutionEntity))
                    .thenThrow(new NullFieldException("The following compulsory field(s) are null: institution_email"));

            mockMvc.perform(post("/api/v1/institution")
                            .content(objectMapper.writeValueAsString(institutionEntity))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$").isNotEmpty())
                    .andExpect(jsonPath("$.statusCode", is(400)))
                    .andExpect(jsonPath("$.error", is("The following compulsory field(s) are null: institution_email")))
                    .andExpect(jsonPath("$.path", is("uri=/api/v1/institution")));

            verify(institutionService).saveInstitution(institutionEntity);
        }

    }

    @Nested
    class getAllMethodTests {

        @Test
        void getAll_whenTableIsPopulated_shouldReturnListOfInstitutions() throws Exception {
            List<InstitutionEntity> institutionList = new ArrayList<>();

            institutionList.add(new InstitutionEntity(5,
                    "Binary Academy",
                    "The school of 1's and 0's",
                    "info@binaryacdemy.com",
                    "011 111 1111",
                    "binaryacdemy.com"));

            institutionList.add(new InstitutionEntity(7,
                    "The Code Hub",
                    "Where the greatest programing minds are created",
                    "info@codehub.com",
                    "+27 22 222 2222",
                    "codehub.edu"));

            when(institutionService.getAllInstitutions()).thenReturn(institutionList);

            mockMvc.perform(get("/api/v1/institutions"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$").isNotEmpty())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$", hasSize(2)));
        }

        @Test
        void getAll_whenTableIsEmpty_shouldReturnEmptyArray() throws Exception {
            List<InstitutionEntity> institutionList = new ArrayList<>();

            when(institutionService.getAllInstitutions()).thenReturn(institutionList);

            mockMvc.perform(get("/api/v1/institutions"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$").isEmpty())
                    .andExpect(jsonPath("$").isArray());
        }
    }

    @Nested
    class getByIdMethodTest {

        @Test
        void getById_whenEntityExists_shouldReturnEntity() throws Exception {
            Integer institution_id = 5;
            InstitutionEntity institutionEntity = new InstitutionEntity(5,
                    "Binary Academy",
                    "The school of 1's and 0's",
                    "info@binaryacdemy.com",
                    "011 111 1111",
                    "binaryacdemy.com");

            when(institutionService.getInstitutionById(institution_id)).thenReturn(institutionEntity);

            mockMvc.perform(get("/api/v1/institution/5"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$").isNotEmpty())
                    .andExpect(jsonPath("$.institution_id", is(5)))
                    .andExpect(jsonPath("$.institution_name", is("Binary Academy")))
                    .andExpect(jsonPath("$.institution_description", is("The school of 1's and 0's")))
                    .andExpect(jsonPath("$.institution_email", is("info@binaryacdemy.com")))
                    .andExpect(jsonPath("$.institution_phone", is("011 111 1111")))
                    .andExpect(jsonPath("$.institution_website", is("binaryacdemy.com")));

            verify(institutionService).getInstitutionById(institution_id);
        }

        @Test
        void getById_whenEntityDoesNotExist_shouldReturnExceptionResponse() throws Exception {
            Integer institution_id = 10;

            when(institutionService.getInstitutionById(institution_id))
                    .thenThrow(new NoSuchElementException("Error fetching, no such institution present with ID = 10"));

            mockMvc.perform(get("/api/v1/institution/10"))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$").isNotEmpty())
                    .andExpect(jsonPath("$.statusCode", is(404)))
                    .andExpect(jsonPath("$.error", is("Error fetching, no such institution present with ID = 10")))
                    .andExpect(jsonPath("$.path", is("uri=/api/v1/institution/10")));

            verify(institutionService).getInstitutionById(institution_id);
        }
    }

    @Nested
    class updateMethodTest {

        @Test
        void update_whenEntityExists_shouldReturnUpdatedEntity() throws Exception {
            //Updating the institution_description field
            Integer institution_id = 5;

            InstitutionEntity providedUpdate = new InstitutionEntity();
            providedUpdate.setInstitution_description("The school of one's and zero's");

            InstitutionEntity updatedInstitution = new InstitutionEntity(
                    "Binary Academy",
                    "The school of one's and zero's",
                    "info@binaryacdemy.com",
                    "011 111 1111",
                    "binaryacdemy.com");

            when(institutionService.updateInstitution(institution_id, providedUpdate)).thenReturn(updatedInstitution);

            mockMvc.perform(put("/api/v1//institution/5")
                            .content(objectMapper.writeValueAsString(providedUpdate))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$").isNotEmpty())
                    .andExpect(jsonPath("$.institution_description", is("The school of one's and zero's")));

            verify(institutionService).updateInstitution(5, providedUpdate);
        }

        @Test
        void update_whenEntityDoesNotExist_shouldReturnExceptionResponse() throws Exception {
            Integer institution_id = 100;

            InstitutionEntity providedUpdate = new InstitutionEntity();
            providedUpdate.setInstitution_description("The school of one's and zero's");

            when(institutionService.updateInstitution(institution_id, providedUpdate))
                    .thenThrow(new NoSuchElementException("Error updating, no such institution present with ID = 100"));

            mockMvc.perform(put("/api/v1//institution/100")
                            .content(objectMapper.writeValueAsString(providedUpdate))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$").isNotEmpty())
                    .andExpect(jsonPath("$.statusCode", is(404)))
                    .andExpect(jsonPath("$.error", is("Error updating, no such institution present with ID = 100")))
                    .andExpect(jsonPath("$.path", is("uri=/api/v1/institution/100")));

            verify(institutionService).updateInstitution(institution_id, providedUpdate);
        }
    }

    @Nested
    class deleteMethodTest {

        @Test
        void delete_whenEntityExists_shouldReturnSuccessResponse() throws Exception {
            Integer institution_id = 5;

            SuccessResponse successResponse = new SuccessResponse(200, "Successfully deleted Institution with ID = 5");

            when(institutionService.deleteInstitution(institution_id)).thenReturn(successResponse);

            mockMvc.perform(delete("/api/v1//institution/5"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$").isNotEmpty())
                    .andExpect(jsonPath("$.statusCode", is(200)))
                    .andExpect(jsonPath("$.message", is("Successfully deleted Institution with ID = 5")));

            verify(institutionService).deleteInstitution(institution_id);
        }

        @Test
        void delete_whenEntityDoesNotExist_shouldReturnExceptionResponse() throws Exception {
            Integer institution_id = 100;

            when(institutionService.deleteInstitution(institution_id))
                    .thenThrow(new NoSuchElementException("Error deleting, no such institution present with ID = 100"));

            mockMvc.perform(delete("/api/v1/institution/100"))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$").isNotEmpty())
                    .andExpect(jsonPath("$.statusCode", is(404)))
                    .andExpect(jsonPath("$.error", is("Error deleting, no such institution present with ID = 100")))
                    .andExpect(jsonPath("$.path", is("uri=/api/v1/institution/100")));

            verify(institutionService).deleteInstitution(institution_id);
        }
    }

}