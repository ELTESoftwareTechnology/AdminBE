package com.app.controller;

import com.app.crypto.ChunkUploadRequest;
import com.app.crypto.CryptoManager;
import com.app.entity.ChunkData;
import com.app.entity.ChunkInfo;
import com.app.entity.User;
import com.app.exception.KeyExpiredException;
import com.app.exception.TargetUserNotFoundException;
import com.app.security.auth.JwtUser;
import com.app.service.ChunkService;
import com.app.service.UserService;
import com.virgilsecurity.sdk.crypto.VirgilPrivateKey;
import com.virgilsecurity.sdk.utils.ConvertionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    public ResponseEntity queryChunk(@RequestParam("patientUsername") String patientUsername) {
        JwtUser principal = ((JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        User currentUser = this.userService.findByUsername(principal.getUsername());

        VirgilPrivateKey privateKey = CryptoManager.getPrivateKey();

        if(privateKey == null){
            throw new KeyExpiredException();
        }

        List<ChunkInfo> infosByPatient = chunkService.findChunksForUser(currentUser).stream().filter(i -> i.getFrom().getUsername().equals(patientUsername)).distinct().collect(Collectors.toList());

        ArrayList<String> decryptedData = new ArrayList<>();

        try {
            for (ChunkInfo info : infosByPatient) {
                String encryptedData = info.getData().getEncryptedData();
                CryptoManager manager = new CryptoManager();
                String decryptedText = manager.dataDecryption(ConvertionUtils.toBase64Bytes(encryptedData), privateKey);
                decryptedData.add(decryptedText);
            }
        } catch (Exception ex) {
            LOG.error(ex.getMessage());
            return new ResponseEntity<>(ex, HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(decryptedData);
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
