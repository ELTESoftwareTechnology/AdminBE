package com.app.controller;

import com.app.crypto.ChunkUploadRequest;
import com.app.entity.ChunkData;
import com.app.entity.ChunkInfo;
import com.app.entity.User;
import com.app.exception.TargetUserNotFoundException;
import com.app.security.auth.JwtUser;
import com.app.service.ChunkService;
import com.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * This class will handle the encrypted data management in chunks
 */
@RestController
public class ChunkController extends BaseController {

    public final static String CHUNK_QUERY_URL = "/api/chunk/query";
    public final static String CHUNK_UPLOAD_URL = "/api/chunk/upload";

    private ChunkService chunkService;
    private UserService userService;

    /**
     * Injects ChunkService instance
     * @param chunkService to inject
     */
    @Autowired
    public void setChunkService(ChunkService chunkService) {
        this.chunkService = chunkService;
    }
    /**
     * Injects UserService instance
     * @param userService to inject
     */
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }


    @GetMapping(CHUNK_QUERY_URL)
    public ResponseEntity queryChunk() {
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


    @PostMapping(CHUNK_UPLOAD_URL)
    public ResponseEntity uploadChunk(@RequestBody ChunkUploadRequest chunkUploadRequest){
        User fromUser = this.userService.findByUsername(((JwtUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
        User toUser = this.userService.findByUsername(chunkUploadRequest.getTargetUsername());

        if(toUser == null){
            throw new TargetUserNotFoundException();
        }

        ChunkData data = new ChunkData(0L, chunkUploadRequest.getEncryptedData());
        ChunkInfo info = new ChunkInfo(0L, fromUser, toUser, null, chunkUploadRequest.getComment());
        info = chunkService.saveDataAndInfo(data, info);

        return new ResponseEntity(HttpStatus.OK);
    }
}
