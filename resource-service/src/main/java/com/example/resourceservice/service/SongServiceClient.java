package com.example.resourceservice.service;

import com.example.resourceservice.dto.CreateSongDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "songServiceClient", path = "${song-service.url}")
public interface SongServiceClient {

    @PostMapping("/songs")
    void createSong(CreateSongDto createSongDto);

    @DeleteMapping("/songs")
    void deleteSong(@RequestParam("id") Long id);
}
