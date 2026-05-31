package com.rodriguesalex.droidpokedex.details

import junit.framework.TestCase.assertEquals
import org.junit.Test

class DetailsFormattingTest {
    @Test
    fun formatHeight_formats_meters_with_one_decimal() {
        assertEquals("0.7 M", formatHeight(0.7))
        assertEquals("1.0 M", formatHeight(1.0))
    }

    @Test
    fun formatWeight_formats_kilograms_with_one_decimal() {
        assertEquals("6.9 KG", formatWeight(6.9))
        assertEquals("10.0 KG", formatWeight(10.0))
    }
}
