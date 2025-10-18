package com.example.resourceservice.service;

import com.example.resourceservice.dto.CreateSongDto;
import com.example.resourceservice.exception.SongAlreadyExistsException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class SongServiceClient {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${song-service.url}")
    private String songServiceUrl;

    public void createSong(CreateSongDto createSongDto) {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        var requestEntity = new HttpEntity<>(createSongDto, headers);
        restTemplate.postForEntity(songServiceUrl + "/songs", requestEntity, Void.class);
    }

    public void deleteSong(Long id) {
        restTemplate.delete(songServiceUrl + "/songs?id=" + id);
    }
}
