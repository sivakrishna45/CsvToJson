package com.csv.service;


import com.csv.exception.FileProcessingException;
import com.csv.exception.InvalidFileFormatException;
import com.csv.util.Constants;
import com.csv.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CsvRecordToDbService {

    private static final Logger logger = LoggerFactory.getLogger(CsvRecordToDbService.class);

    public List<Map<String, Object>> handleCsvRecordUpload(String inputCsvFilePath) throws Exception {

        try {
            // Validate the file format using the utility method
            FileUtils.validateFile(inputCsvFilePath);

            // Process the CSV records using the utility method
            List<String> csvDataList = FileUtils.processCsvRecordsToList(inputCsvFilePath);
            String[] headers = csvDataList.get(0).split(",");
            List<Map<String, Object>> dataRows = new ArrayList<>();

            // Process the data rows
            csvDataList.stream().skip(1).forEach(row -> {
                String[] values = row.split(",");
                Map<String, Object> objectMap = new HashMap<>();

                // Ensure all headers have corresponding values, assign null if missing
                for (int i = 0; i < headers.length; i++) {
                    String value;

                    // Use if-else to check if value exists or is missing
                    if (i < values.length) {
                        value = values[i].trim();  // If value exists, trim and assign it
                    } else {
                        value = null;  // If value is missing, set it to null
                    }

                    objectMap.put(headers[i].trim(), value);  // Insert header with value or null
                }

                dataRows.add(objectMap);  // Add the map to the list
            });

            return dataRows;
        } catch (InvalidFileFormatException e) {
            logger.error(Constants.INVALID_FILE_FORMAT_ERROR);
            throw new Exception(Constants.INVALID_FILE_FORMAT_ERROR);
        } catch (FileProcessingException e) {
            logger.error(Constants.FILE_NOT_FOUND_ERROR + inputCsvFilePath);
            throw new Exception("An error occurred while processing the file.");
        } catch (Exception e) {
            logger.error("Unexpected error: {}", e.getMessage(), e);
            throw new Exception("An unexpected error occurred.");
        }
    }
}