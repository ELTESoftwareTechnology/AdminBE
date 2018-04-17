package com.app.controller;

import com.app.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This class will handle the encrypted data management in chunks
 */
@RestController
public class ChunkController {

    public final static String CHUNK_QUERY_URL = "/api/chunk/query";

    @GetMapping(CHUNK_QUERY_URL)
    public ResponseEntity queryChunk(){
        String dummyJson = "{\n" +
                "  \"measurements\": [\n" +
                "    {\n" +
                "      \"type\": \"heartrate\",\n" +
                "      \"unit\": \"bpm\",\n" +
                "      \"value\": 128,\n" +
                "      \"createdAt\": \"2018-04-16T21:50:43.515Z\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"type\": \"heartrate\",\n" +
                "      \"unit\": \"bpm\",\n" +
                "      \"value\": 100,\n" +
                "      \"createdAt\": \"2018-04-16T21:51:13.132Z\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"type\": \"heartrate\",\n" +
                "      \"unit\": \"bpm\",\n" +
                "      \"value\": 140,\n" +
                "      \"createdAt\": \"2018-04-16T21:51:23.770Z\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        return ResponseEntity.ok(dummyJson);
    }

}
