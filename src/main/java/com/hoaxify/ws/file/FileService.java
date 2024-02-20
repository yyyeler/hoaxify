package com.hoaxify.ws.file;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.UUID;

import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hoaxify.ws.configuration.HoaxifyProperties;

@Service
public class FileService {
    @Autowired
    HoaxifyProperties hoaxifyProperties;

    Tika tika = new Tika();

    public String saveBase64StringAsFile(String image) {
        String filename = UUID.randomUUID().toString();
        Path path = getProfileImage(filename);

        try 
        {
            OutputStream outputStream = new FileOutputStream(path.toFile());
            outputStream.write(decodedImage(image));
            outputStream.close();
            return filename;
        } 
        catch (IOException e) {
            e.printStackTrace();
        }

        return null;
        
    }

    public String detectType(String value) {
        return tika.detect(decodedImage(value));
    }

    private byte[] decodedImage(String encodedImage)
    {
        return Base64.getDecoder().decode(encodedImage.split(",")[1]);
    }

    public void deleteOldProfileImage(String image) {
        if(image == null) return;

        Path path = getProfileImage(image);
        try 
        {
            Files.deleteIfExists(path);
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }

    private Path getProfileImage(String filename)
    {
        return Paths.get(hoaxifyProperties.getStorage().getRoot(),hoaxifyProperties.getStorage().getProfile(),filename);
    }
    
}
