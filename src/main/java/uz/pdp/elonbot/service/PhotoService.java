package uz.pdp.elonbot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.response.GetFileResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import uz.pdp.elonbot.entity.Photo;
import uz.pdp.elonbot.repo.PhotoRepo;
import java.io.InputStream;
import java.net.URL;

@Service
@RequiredArgsConstructor
public class PhotoService {

    private final TelegramBot telegramBot;
    private final PhotoRepo photoRepo;

    public Photo getPhoto(PhotoSize[] photos) {
        PhotoSize largestPhoto = photos[photos.length - 1];
        String fileId = largestPhoto.fileId();
        String filePath = getFilePath(fileId);
        byte[] content = downloadImage(filePath);
        return createPhoto(content);
    }

    private String getFilePath(String fileId) {
        GetFile getFileRequest = new GetFile(fileId);
        GetFileResponse getFileResponse = telegramBot.execute(getFileRequest);
        return getFileResponse.file().filePath();
    }

    @SneakyThrows
    private byte[] downloadImage(String filePath) {
        String fileApi = "https://api.telegram.org/file/bot";
        String url = fileApi + telegramBot.getToken() + "/" + filePath;
        try (InputStream inputStream = new URL(url).openStream()) {
            return inputStream.readAllBytes();
        }
    }

    private Photo createPhoto(byte[] content) {
        Photo photo = new Photo();
        photo.setContent(content);
        return photoRepo.save(photo);
    }

}
