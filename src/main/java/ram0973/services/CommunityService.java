package ram0973.services;

import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ram0973.dto.ImageDTO;
import ram0973.dto.MessageDTO;

import java.util.Arrays;
import java.util.List;

@Service
public class CommunityService {
    public List<MessageDTO> getCommunityMessages(int page) {
        return Arrays.asList(new MessageDTO(1L, "First message"), new MessageDTO(2L, "Second message"));
    }

    public List<ImageDTO> getCommunityImages(int page) {
        return Arrays.asList(
            new ImageDTO(1L, "First title", null),
            new ImageDTO(2L, "Second title", null)
        );
    }

    public MessageDTO postMessage(MessageDTO messageDTO) {
        return new MessageDTO(3L, "Third message");
    }

    public ImageDTO postImage(
        @RequestParam MultipartFile file, @RequestParam(value = "title") String title) {
        return new ImageDTO(3L, "Third title", null);
    }
}
