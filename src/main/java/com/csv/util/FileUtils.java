package com.csv.util;


import com.csv.exception.FileProcessingException;
import com.csv.exception.InvalidFileFormatException;
import com.csv.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

public class FileUtils {

    // Initialize the logger
    private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);

    // Method to validate the file format (CSV)
    public static String validateFile(String inputCsvFile) throws InvalidFileFormatException {
        String fileName = inputCsvFile.toLowerCase();

        logger.info("Validating file format for: {}", inputCsvFile);

        if (!fileName.endsWith(Constants.CSV_FILE_EXTENSION)) {
            logger.error("Invalid file format detected for file: {}", inputCsvFile);
            throw new InvalidFileFormatException(Constants.INVALID_FILE_FORMAT_ERROR);
        }

        logger.info("File format is valid for: {}", inputCsvFile);
        return "csv";
    }

    // Method to process the CSV file and convert it to a list of strings
    public static List<String> processCsvRecordsToList(String inputCsvFilePath) throws FileProcessingException {
        try {
            logger.info("Processing CSV file: {}", inputCsvFilePath);

            ClassLoader classLoader = FileUtils.class.getClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream(inputCsvFilePath);

            if (inputStream == null) {
                logger.error("File not found: {}", inputCsvFilePath);
                throw new FileProcessingException(Constants.FILE_NOT_FOUND_ERROR + inputCsvFilePath, null);
            }

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            List<String> csvRecords = bufferedReader.lines().collect(Collectors.toList());

            logger.info("Successfully processed file: {} with {} records", inputCsvFilePath, csvRecords.size());

            return csvRecords;
        } catch (Exception e) {
            logger.error("Error occurred while processing file: {}", inputCsvFilePath, e);
            throw new FileProcessingException("Error while processing the file: " + inputCsvFilePath, e);
        }
    }
}
