package br.com.felipe.productionsolution.Service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    public String readyFile(MultipartFile file);
}
