package com.matefoldes.exchangerates.exception

import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class ExceptionHandlerControllerTest {

    private val underTest = ExceptionHandlerController()

    @Test
    fun `test handleException should return properly`() {
        // GIVEN
        val e = Exception("error")
        val expected = ResponseEntity("Unexpected error.", HttpStatus.INTERNAL_SERVER_ERROR)

        // WHEN
        val actual = underTest.handleException(e)

        // THEN
        assert(actual == expected)
    }
}
