package com.app.service;

import com.app.entity.ChunkData;
import com.app.entity.ChunkInfo;
import com.app.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface ChunkService {
    ChunkInfo saveDataAndInfo(ChunkData data, ChunkInfo info);
    ChunkInfo findChunksForUser(User user);
}