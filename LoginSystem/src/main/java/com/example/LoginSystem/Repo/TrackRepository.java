package com.example.LoginSystem.Repo;

import com.example.LoginSystem.Model.Track.TrackEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrackRepository extends JpaRepository<TrackEntity,String> {
}
