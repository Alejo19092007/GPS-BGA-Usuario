package me.edwarjimenez.gpsbgausuario.ui.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.edwarjimenez.gpsbgausuario.ui.theme.*

data class PreguntaFrecuente(
    val pregunta: String,
    val respuesta: String
)

@Composable
fun AyudaScreen(onBackClick: () -> Unit) {

    val preguntas = listOf(
        PreguntaFrecuente(
            "¿Cómo veo los buses en tiempo real?",
            "Ve a la pantalla de Inicio o Mapa. Los buses que estén en servicio aparecerán automáticamente con su posición actualizada."
        ),
        PreguntaFrecuente(
            "¿Cómo guardo una ruta favorita?",
            "Ve a la pantalla de Mis Rutas y toca el botón + para agregar una nueva ruta con origen y destino."
        ),
        PreguntaFrecuente(
            "¿Por qué no veo ningún bus?",
            "Los buses solo aparecen cuando el conductor ha iniciado su servicio. Si no hay buses, intenta más tarde."
        ),
        PreguntaFrecuente(
            "¿Cómo recibo notificaciones?",
            "Activa las notificaciones en Perfil. Recibirás alertas cuando un bus esté cerca de tu parada."
        ),
        PreguntaFrecuente(
            "¿Qué rutas están disponibles?",
            "Actualmente están disponibles la Ruta 7 (Limoncito), Ruta 36 (Igsabelar 33) y Ruta 27 (Caracolí - Centro)."
        ),
        PreguntaFrecuente(
            "¿Cómo contacto al soporte?",
            "Puedes escribirnos a soporte@gpsbga.com o llamar al 318-555-0000 en horario de 7am a 7pm."
        )
    )

    var expandida by remember { mutableStateOf<Int?>(null) }

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
                    Text("Ayuda / Soporte", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Preguntas frecuentes",
                    fontSize = 14.sp, color = TextMuted
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            items(preguntas.size) { index ->
                val pregunta = preguntas[index]
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = BgCard),
                    shape = RoundedCornerShape(12.dp),
                    onClick = { expandida = if (expandida == index) null else index }
                ) {
                    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = pregunta.pregunta,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = if (expandida == index) "▲" else "▼",
                                fontSize = 12.sp,
                                color = GreenAccent
                            )
                        }
                        if (expandida == index) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = pregunta.respuesta,
                                fontSize = 12.sp,
                                color = TextMuted
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = GreenDark),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("📞 Contacto directo", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = GreenAccent)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Email: soporte@gpsbga.com", fontSize = 12.sp, color = TextPrimary)
                        Text("Teléfono: 318-555-0000", fontSize = 12.sp, color = TextPrimary)
                        Text("Horario: Lun-Vie 7am - 7pm", fontSize = 12.sp, color = TextMuted)
                    }
                }
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}