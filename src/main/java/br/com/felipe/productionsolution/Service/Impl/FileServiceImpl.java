package br.com.felipe.productionsolution.Service.Impl;

import br.com.felipe.productionsolution.Service.FileService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileServiceImpl implements FileService {
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
