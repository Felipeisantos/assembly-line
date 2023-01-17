package br.com.felipe.productionsolution.Controller;

import br.com.felipe.productionsolution.Service.BussinessLayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class UploadController {

    @Autowired
    private BussinessLayerService bussinessLayerService;


    @GetMapping("/")
    public ResponseEntity<String> index(@RequestParam("file") MultipartFile file) {
        return new ResponseEntity<>(bussinessLayerService.applyRulesOnFile(file), HttpStatus.OK);
    }
}
