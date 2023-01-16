package br.com.felipe.productionsolution.Model;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class TimeTest {

    Time time = new Time();
    @Test
    void addMinutes() {
        time.addMinutes(180);
    }
}