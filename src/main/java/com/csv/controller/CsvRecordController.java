package com.csv.controller;

import com.csv.service.CsvRecordToDbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
public class CsvRecordController {

    @Autowired
    private CsvRecordToDbService csvRecordToDbService;


    @PostMapping("/importCsv")
    public ResponseEntity<Map<String, Object>> importCsvToDb(@RequestParam("filePath") String filePath) throws Exception {
        List<Map<String, Object>> jsonObjectList = csvRecordToDbService.handleCsvRecordUpload(filePath);
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("result", "File Converted to JSON Data");
        responseMap.put("data", jsonObjectList);
        return ResponseEntity.status(HttpStatus.OK).body(responseMap);
    }




}
