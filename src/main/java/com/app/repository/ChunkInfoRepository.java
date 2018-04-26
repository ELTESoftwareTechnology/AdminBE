package com.app.repository;

import com.app.entity.ChunkData;
import com.app.entity.ChunkInfo;
import com.app.entity.User;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface ChunkInfoRepository extends JpaRepository<ChunkInfo, Long> {
    /**
     * Find chunks that targets a user
     * @param to user to look for
     * @return chunk infos for a targeted user
     */
    List<ChunkInfo> findByTo(User to);
}