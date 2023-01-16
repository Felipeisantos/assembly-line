package br.com.felipe.productionsolution.Service;

import org.jetbrains.annotations.NotNull;
import org.springframework.web.multipart.MultipartFile;

public interface BussinessLayerService {
    public String applyRulesOnFile(MultipartFile file);
    public Integer searchTimeStringIndex(@NotNull String words[]);
    public Integer safeConversionTime(@NotNull String str);
}
