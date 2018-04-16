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
        return ResponseEntity.ok("TEST");
    }

}
