package ru.novik.tggptbot;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.novik.openai.ApiException;
import ru.novik.openai.model.ImageData;
import ru.novik.openai.model.ImageGenerationRequest;
import ru.novik.openai.model.ImageGenerationResponse;
import ru.novik.openai.services.DefaultApi;

@Service
@AllArgsConstructor
@Slf4j
public class ImageGenerationService {

    private final DefaultApi defaultApi;

    public String call(Long userId, String text) {

        ImageGenerationRequest imageGenerationRequest = new ImageGenerationRequest();
        imageGenerationRequest.setPrompt(text);
        imageGenerationRequest.setModel(ImageGenerationRequest.ModelEnum._3);
        imageGenerationRequest.n(1);
        imageGenerationRequest.quality(ImageGenerationRequest.QualityEnum.HD);
        imageGenerationRequest.responseFormat(ImageGenerationRequest.ResponseFormatEnum.B64_JSON);
        imageGenerationRequest.setSize(ImageGenerationRequest.SizeEnum._1792X1024);
        imageGenerationRequest.setStyle(ImageGenerationRequest.StyleEnum.VIVID);
        imageGenerationRequest.setUser(userId.toString());

        try {
            ImageGenerationResponse imageGenerationResponse = defaultApi.imageGeneration(imageGenerationRequest);
            if (imageGenerationResponse.getData() != null && !imageGenerationResponse.getData().isEmpty()) {
                ImageData imageData = imageGenerationResponse.getData().get(0);
                log.info("Image generated: {}", imageData.getRevisedPrompt());
                return imageData.getB64Json();
            }
            return "No response from AI";
        } catch (ApiException e) {
            log.error("Error calling OpenAI", e);
            return "Error calling OpenAI";
        }

    }

}
