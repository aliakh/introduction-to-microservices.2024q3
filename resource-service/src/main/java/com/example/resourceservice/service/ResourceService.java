package com.example.resourceservice.service;

import com.example.resourceservice.entity.Resource;
import com.example.resourceservice.exception.InvalidMp3FileException;
import com.example.resourceservice.exception.ResourceNotFoundException;
import com.example.resourceservice.exception.SongAlreadyExistsException;
import com.example.resourceservice.repository.ResourceRepository;
import com.example.resourceservice.service.validation.Mp3Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ResourceService {

    @Autowired
    private ResourceRepository resourceRepository;
    @Autowired
    private MetadataService metadataService;
    @Autowired
    private SongServiceClient songServiceClient;
    @Autowired
    private Mp3Validator mp3Validator;

    @Transactional
    public Long uploadResource(byte[] audio) {
        if (!mp3Validator.valid(audio)) {
            throw new InvalidMp3FileException("The request body is invalid MP3");
        }

        var resource = new Resource();
        resource.setAudio(audio);

        var createdResource = resourceRepository.save(resource);
        var createdId = createdResource.getId();

        try {
            var createSongDto = metadataService.extractSongMetadata(audio, createdId);
            songServiceClient.createSong(createSongDto);
        } catch (SongAlreadyExistsException e) {
            throw e;
        } catch (RuntimeException e) {
            throw new RuntimeException(String.format("Failed to save song metadata for ID=%d", createdId));
        }

        return createdId;
    }

    @Transactional(readOnly = true)
    public Resource getResource(Long id) {
        return resourceRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @Transactional
    public List<Long> deleteResources(String csvIds) {
        return parse(csvIds)
            .stream()
            .filter(resourceRepository::existsById)
            .peek(id -> resourceRepository.deleteById(id))
            .peek(id -> {
                try {
                    songServiceClient.deleteSongMetadata(id);
                } catch (RuntimeException e) {
                    throw new RuntimeException(String.format("Failed to delete song metadata for ID=%d", id));
                }
            })
            .collect(Collectors.toList());
    }

    private List<Long> parse(String csvIds) {
        return Arrays.stream(csvIds.split(","))
            .map(String::trim)
            .map(id -> {
                try {
                    return Long.parseLong(id);
                } catch (NumberFormatException e) {
                    throw new RuntimeException(e);
                }
            })
            .collect(Collectors.toList());
    }
}
