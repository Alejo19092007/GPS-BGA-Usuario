package me.edwarjimenez.gpsbgausuario.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.edwarjimenez.gpsbgausuario.ui.theme.*

data class ViajeHistorial(
    val ruta: String,
    val origen: String,
    val destino: String,
    val fecha: String,
    val duracion: String
)

@Composable
fun HistorialScreen(onBackClick: () -> Unit) {

    val historial = listOf(
        ViajeHistorial("Ruta 36", "González Chaparro", "Autopista Cañaveral", "Hoy 08:30", "45 min"),
        ViajeHistorial("Ruta 7", "Terminal Los Cauchos", "CC Cacique", "Ayer 17:15", "30 min"),
        ViajeHistorial("Ruta 27", "Terminal Caracolí", "Centro - Carrera 10", "Ayer 07:00", "55 min"),
        ViajeHistorial("Ruta 36", "CC Cacique", "González Chaparro", "Hace 2 días 18:00", "40 min"),
        ViajeHistorial("Ruta 7", "Megamall", "Terminal Los Cauchos", "Hace 3 días 12:30", "25 min"),
    )

    Surface(modifier = Modifier.fillMaxSize(), color = BgDark) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = GreenAccent)
                    }
                    Text("Historial de Viajes", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            items(historial) { viaje ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = BgCard),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(RoundedCornerShape(50))
                                .background(GreenDark),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.DirectionsBus, null, tint = GreenAccent, modifier = Modifier.size(20.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = viaje.ruta, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                            Text(text = "${viaje.origen} → ${viaje.destino}", fontSize = 12.sp, color = TextMuted)
                            Text(text = viaje.fecha, fontSize = 11.sp, color = TextMuted)
                        }
                        Text(text = viaje.duracion, fontSize = 12.sp, color = GreenAccent, fontWeight = FontWeight.Bold)
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }
}