package br.com.felipe.productionsolution.Service.Impl;

import br.com.felipe.productionsolution.Service.BussinessLayerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
class BussinessLayerServiceimplTest {

    @Autowired
    BussinessLayerService bussinessLayerService;

    @Test
    void searchTimeStringIndex() {
        String[] words = "Pieces washing 45min \n".split(" ");
        assertEquals(2, bussinessLayerService.searchTimeStringIndex(words));

        words = "Axis calibration 30min 3d\n ".split(" ");
        assertEquals(2, bussinessLayerService.searchTimeStringIndex(words));

        words = "Steel bearing assembly 45min\n".split(" ");
        assertEquals(3, bussinessLayerService.searchTimeStringIndex(words));

        words = "Assembly line cooling - maintenance\n".split(" ");
        assertEquals(4,bussinessLayerService.searchTimeStringIndex(words));

        words = "Assembly line cooling tests\n".split(" ");
        assertNull(bussinessLayerService.searchTimeStringIndex(words));

    }

    @Test
    void safeConversionTime() {
        String time = "45min\n";
        assertEquals(45, bussinessLayerService.safeConversionTime(time));

        time = "30minm\t\n\rtest";
        assertEquals(30, bussinessLayerService.safeConversionTime(time));

        time = "\r\t50minutes\n";
        assertEquals(50, bussinessLayerService.safeConversionTime(time));

        time = "maintenance\n";
        assertNull(bussinessLayerService.safeConversionTime(time));

        time = "        ";
        assertNull(bussinessLayerService.safeConversionTime(time));
    }
}