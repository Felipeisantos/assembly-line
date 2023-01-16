package br.com.felipe.productionsolution.Controller;

import br.com.felipe.productionsolution.Model.Task;
import br.com.felipe.productionsolution.Service.BussinessLayerService;
import br.com.felipe.productionsolution.Service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
public class UploadController {

    @Autowired
    private BussinessLayerService bussinessLayerService;

    @GetMapping("/")
    @ResponseBody
    public String index(@RequestParam("file") MultipartFile file) {

        return  bussinessLayerService.applyRulesOnFile(file);
    }
}
