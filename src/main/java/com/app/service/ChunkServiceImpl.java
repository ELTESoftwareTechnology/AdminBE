package com.app.service;

import com.app.entity.ChunkData;
import com.app.entity.ChunkInfo;
import com.app.entity.User;
import com.app.repository.ChunkDataRepository;
import com.app.repository.ChunkInfoRepository;
import com.app.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChunkServiceImpl implements ChunkService {

    private ChunkDataRepository chunkDataRepository;
    private ChunkInfoRepository chunkInfoRepository;

    @Autowired
    public void setChunkDataRepository(ChunkDataRepository chunkDataRepository) {
        this.chunkDataRepository = chunkDataRepository;
    }

    @Autowired
    public void setChunkInfoRepository(ChunkInfoRepository chunkInfoRepository) {
        this.chunkInfoRepository = chunkInfoRepository;
    }

    @Override
    public ChunkInfo saveDataAndInfo(ChunkData data, ChunkInfo info) {
        ChunkData persistedData = chunkDataRepository.save(data);
        info.setData(persistedData);
        return chunkInfoRepository.save(info);
    }

    @Override
    public List<ChunkInfo> findChunksForUser(User user) {
        return chunkInfoRepository.findByTo(user);
    }

}
