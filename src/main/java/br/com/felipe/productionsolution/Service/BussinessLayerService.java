package br.com.felipe.productionsolution.Service;

import org.jetbrains.annotations.NotNull;
import org.springframework.web.multipart.MultipartFile;

public interface BussinessLayerService {
    String applyRulesOnFile(MultipartFile file);

    Integer searchTimeStringIndex(@NotNull String words[]);

    Integer safeConversionTime(@NotNull String str);
}
