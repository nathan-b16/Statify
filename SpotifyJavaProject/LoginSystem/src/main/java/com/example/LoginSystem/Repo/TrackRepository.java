package com.example.LoginSystem.Repo;

import com.example.LoginSystem.Model.Track.TrackEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import se.michaelthelin.spotify.model_objects.specification.Track;

import java.util.List;

@Repository
public interface TrackRepository extends JpaRepository<TrackEntity,String> {
    @Query(value = "SELECT spotify_id FROM tracks ORDER BY RAND() LIMIT 5", nativeQuery = true)
    List<String> findRandomSeedIds();
}
