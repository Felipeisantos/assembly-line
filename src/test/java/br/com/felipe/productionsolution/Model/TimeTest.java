package br.com.felipe.productionsolution.Model;

import org.junit.jupiter.api.Test;

class TimeTest {

    Time time = new Time();

    @Test
    void addMinutes() {
        time.addMinutes(180);
    }
}