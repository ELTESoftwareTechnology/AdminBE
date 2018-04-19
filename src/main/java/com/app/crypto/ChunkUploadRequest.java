package com.app.crypto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChunkUploadRequest implements Serializable {

    private String targetUsername;
    private String encryptedData;
    private String comment;

}


