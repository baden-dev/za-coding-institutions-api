package com.baden.processCSV;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoaderOnStartup implements CommandLineRunner {

    private CSVDataLoaderService dataLoaderService;

    public DataLoaderOnStartup(CSVDataLoaderService dataLoaderService) {
        this.dataLoaderService = dataLoaderService;
    }

    @Override
    public void run(String... args) throws Exception {
        dataLoaderService.loadCSVData();
    }
}