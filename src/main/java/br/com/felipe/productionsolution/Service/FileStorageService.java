package br.com.felipe.productionsolution.Service;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    public String readyFile(MultipartFile file);
}
