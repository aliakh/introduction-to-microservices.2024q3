package com.example.resourceservice.service;

import com.example.resourceservice.dto.CreateSongDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "${song-service.url}", path = "/songs")
public interface SongServiceClient2 {

    @PostMapping
    ResponseEntity<Void> createSong(CreateSongDto createSongDto);

    @DeleteMapping
    ResponseEntity<Void> deleteSong(@RequestParam("id") Long id);
}
