package br.com.felipe.productionsolution.Service.Impl;

import br.com.felipe.productionsolution.Service.FileStorageService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Service
public class FileStorageServiceImpl implements FileStorageService {
    @Override
    public String readyFile(MultipartFile file) {
        if (file.isEmpty())
            return "File not uploaded or not exists";
        if (!FilenameUtils.getExtension(file.getOriginalFilename()).equals("txt"))
            return null;
        try {
            return new String(file.getBytes());
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
