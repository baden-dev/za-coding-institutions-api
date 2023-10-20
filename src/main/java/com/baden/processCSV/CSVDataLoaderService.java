package com.baden.processCSV;

import com.baden.entity.InstitutionEntity;
import com.baden.repository.InstitutionRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;

@Service
public class CSVDataLoaderService {

    private final InstitutionRepository institutionRepository;

    public CSVDataLoaderService(InstitutionRepository institutionRepository) {
        this.institutionRepository = institutionRepository;
    }

    public void loadCSVData() {
        String filePath = "src/main/java/com/baden/processCSV/institutionData.csv";

        try (CSVReader lineReader = new CSVReader(new FileReader(filePath))) {

            lineReader.readNext();
            String[] line;

            while ((line = lineReader.readNext()) != null) {
                saveToTable(line, institutionRepository);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }
    }

    // Mapping CSV columns to institution entity fields
    public static void saveToTable(String[] data, InstitutionRepository institutionRepository) {
        InstitutionEntity institution = new InstitutionEntity();

        //Not null fields
        String name = data[1];
        String email = data[3];
        String website = data[5];

        //Null fields
        String description = null;
        if (!data[2].equals("Null")) {
            description = data[2];
        }

        String phone = null;
        if (!data[4].equals("Null")) {
            phone = data[4];
        }

        institution.setInstitution_name(name);
        institution.setInstitution_description(description);
        institution.setInstitution_email(email);
        institution.setInstitution_phone(phone);
        institution.setInstitution_website(website);

        institutionRepository.save(institution);
    }
}
