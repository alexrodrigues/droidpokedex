package com.rodriguesalex.domain.model

import junit.framework.TestCase.assertEquals
import org.junit.Test

class DroidPokemonTypeColorTest {
    @Test
    fun `getPokemonColor returns matching type case insensitively`() {
        assertEquals(DroidPokemonTypeColor.FIRE, DroidPokemonTypeColor.getPokemonColor("fire"))
        assertEquals(DroidPokemonTypeColor.FIRE, DroidPokemonTypeColor.getPokemonColor("FIRE"))
    }

    @Test
    fun `getPokemonColor returns UNKNOWN for unrecognized type`() {
        assertEquals(DroidPokemonTypeColor.UNKNOWN, DroidPokemonTypeColor.getPokemonColor("not-a-type"))
    }
}
