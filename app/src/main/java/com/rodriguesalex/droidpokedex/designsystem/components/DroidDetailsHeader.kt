package com.rodriguesalex.droidpokedex.designsystem.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.rodriguesalex.droidpokedex.R

@Composable
fun DroidDetailsHeader (
    vo: DroidDetailsHeaderVo,
    modifier: Modifier
) {
    val inPreview = LocalInspectionMode.current

    Column(
        modifier = modifier
            .background(vo.backgroundColor)
            .fillMaxWidth()
            .height(300.dp)
    ) {
        Text(
            text = vo.pokemonName,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(top = 16.dp, start = 16.dp),
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(modifier = Modifier.weight(1f))

        if (inPreview) {
            Image(
                painter = painterResource(id = vo.pokemonResource ?: R.drawable.pokeball),
                contentDescription = vo.pokemonName,
                modifier = Modifier
                    .width(200.dp)
                    .height(200.dp)
                    .align(Alignment.CenterHorizontally)
            )
        } else {
            AsyncImage(
                model = vo.pokemonUrl ?: vo.pokemonResource ?: R.drawable.pokeball,
                contentDescription = vo.pokemonName,
                modifier = Modifier
                    .width(200.dp)
                    .height(200.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }
    }
}

data class DroidDetailsHeaderVo(
    val pokemonName: String,
    val pokemonNumber: Int,
    val backgroundColor: Color,
    val pokemonResource: Int? = null,
    val pokemonUrl: String?,
)

@Preview
@Composable
fun DroidDetailsHeaderPreview() {
    DroidDetailsHeader(
        vo = DroidDetailsHeaderVo(
            pokemonName = "Charizard",
            pokemonNumber = 1,
            backgroundColor = Color(0xFFFD7D24),
            pokemonUrl = null,
//            pokemonUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/5.png",
            pokemonResource = R.drawable.charizard,
        ),
        modifier = Modifier
    )
}