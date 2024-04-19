package ram0973.controllers;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ram0973.dto.ImageDTO;
import ram0973.dto.MessageDTO;
import ram0973.services.CommunityService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/v1/community")
@RequiredArgsConstructor
public class CommunityController {

    private final CommunityService communityService;

    // GET /v1/community/messages
    @GetMapping("/messages")
    public ResponseEntity<List<MessageDTO>> getCommunityMessages(
        @RequestParam(value = "page", defaultValue = "0") @Min(0) int page) {
        return ResponseEntity.ok(communityService.getCommunityMessages(page));
    }

    @GetMapping("images")
    public ResponseEntity<List<ImageDTO>> getCommunityImages(
        @RequestParam(value = "page", defaultValue = "0") @Min(0) int page) {
        return ResponseEntity.ok(communityService.getCommunityImages(page));
    }

    @PostMapping("/messages")
    public ResponseEntity<MessageDTO> postMessage(@RequestBody MessageDTO messageDTO) {
        return ResponseEntity.created(URI.create("/v1/community/messages")).body(communityService.postMessage(messageDTO));
    }

    @PostMapping("/images")
    public ResponseEntity<ImageDTO> postImage(
        @RequestParam MultipartFile file, @RequestParam(value = "title") String title) {
        return ResponseEntity.created(URI.create("/v1/community/images")).body(communityService.postImage(file, title));
    }
}
