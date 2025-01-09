package com.rodriguesalex.domain.model

import androidx.compose.ui.graphics.Color

enum class DroidPokemonTypeColor(val primary: Color, val secondary: Color) {
    BUG(Color(0xFF8CB230), Color(0XFF8BD674)),
    DARK(Color(0xFF58575F), Color(0xFF6F6E78)),
    DRAGON(Color(0xFF0F6AC0), Color(0xFF7383B9)),
    ELECTRIC(Color(0xFFEED535), Color(0xFFF2CB55)),
    FAIRY(Color(0xFFED6EC7), Color(0xFFEBA8C3)),
    FIGHTING(Color(0xFFD04164), Color(0xFFEB4971)),
    FIRE(Color(0xFFFD7D24), Color(0xFFFFA756)),
    FLYING(Color(0xFF748FC9), Color(0xFF83A2E3)),
    GHOST(Color(0xFF556AAE), Color(0xFF8571BE)),
    GRASS(Color(0xFF62B957), Color(0xFF8BBE8A)),
    GROUND(Color(0xFFDD7748), Color(0xFFF78551)),
    ICE(Color(0xFF61CEC0), Color(0xFF91D8DF)),
    NORMAL(Color(0xFF9DA0AA), Color(0xFFB5B9C4)),
    POISON(Color(0xFFA552CC), Color(0xFF9F6E97)),
    PSYCHIC(Color(0xFFEA5D60), Color(0xFFFF6568)),
    ROCK(Color(0xFFBAAB82), Color(0xFFD4C294)),
    STEEL(Color(0xFF417D9A), Color(0xFF4C91B2)),
    WATER(Color(0xFF4A90DA), Color(0xFF58ABF6)),
    SHADOW(Color(0xFF5116A4), Color(0xFF855BBF)),
    STELLAR(Color(0xFF00B7A6), Color(0xFF00C5B0)),
    UNKNOWN(Color(0xFF333D33), Color(0xFF707770));

    companion object {
        fun getPokemonColor(type: String): DroidPokemonTypeColor {
            return entries.firstOrNull { it.name.equals(type, ignoreCase = true) }
                ?: UNKNOWN
        }
    }
}