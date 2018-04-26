package com.app.service;

import com.app.entity.ChunkData;
import com.app.entity.ChunkInfo;
import com.app.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ChunkService {
    ChunkInfo saveDataAndInfo(ChunkData data, ChunkInfo info);
    List<ChunkInfo> findChunksForUser(User user);
}