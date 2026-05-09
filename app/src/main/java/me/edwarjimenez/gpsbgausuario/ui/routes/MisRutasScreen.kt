package me.edwarjimenez.gpsbgausuario.ui.routes

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.edwarjimenez.gpsbgausuario.ui.theme.*

data class RutaGuardada(
    val id: String,
    val nombre: String,
    val origen: String,
    val destino: String,
    val rutaId: String,
    val emoji: String
)

@Composable
fun MisRutasScreen() {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("gpsbga_usuario_prefs", Context.MODE_PRIVATE) }

    var rutas by remember { mutableStateOf<List<RutaGuardada>>(emptyList()) }
    var mostrarDialogo by remember { mutableStateOf(false) }
    var nombreRuta by remember { mutableStateOf("") }
    var origen by remember { mutableStateOf("") }
    var destino by remember { mutableStateOf("") }
    var rutaSeleccionada by remember { mutableStateOf("7") }

    val rutasDisponibles = listOf(
        Triple("7", "Limoncito", "COTRANDER"),
        Triple("36", "Igsabelar 33", "COTRANDER"),
        Triple("27", "Caracolí - Centro", "LUSITANIA S.A.")
    )

    // Rutas por defecto
    LaunchedEffect(Unit) {
        rutas = listOf(
            RutaGuardada("1", "Casa → Trabajo", "Mi Casa", "Oficina Centro", "36", "🏠"),
            RutaGuardada("2", "Trabajo → Casa", "Oficina Centro", "Mi Casa", "36", "💼"),
        )
    }

    Surface(modifier = Modifier.fillMaxSize(), color = BgDark) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Route, null, tint = GreenAccent, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Mis Rutas", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    }
                    FloatingActionButton(
                        onClick = { mostrarDialogo = true },
                        containerColor = GreenPrimary,
                        modifier = Modifier.size(42.dp)
                    ) {
                        Icon(Icons.Default.Add, null, tint = Color.White, modifier = Modifier.size(20.dp))
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                // Accesos rápidos
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AccesoCard(emoji = "🏠", titulo = "Casa", subtitulo = "Ruta 36", modifier = Modifier.weight(1f))
                    AccesoCard(emoji = "💼", titulo = "Trabajo", subtitulo = "Ruta 36", modifier = Modifier.weight(1f))
                    AccesoCard(emoji = "⭐", titulo = "Favoritos", subtitulo = "${rutas.size} rutas", modifier = Modifier.weight(1f))
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                Text(
                    text = "Rutas guardadas (${rutas.size})",
                    fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary
                )
            }

            items(rutas) { ruta ->
                RutaCard(
                    ruta = ruta,
                    onEliminar = { rutas = rutas.filter { it.id != ruta.id } }
                )
            }

            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }

    // Dialogo agregar ruta
    if (mostrarDialogo) {
        AlertDialog(
            onDismissRequest = { mostrarDialogo = false },
            containerColor = BgCard,
            title = {
                Text("Agregar Ruta", fontWeight = FontWeight.Bold, color = TextPrimary)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = nombreRuta,
                        onValueChange = { nombreRuta = it },
                        label = { Text("Nombre de la ruta", color = TextMuted) },
                        placeholder = { Text("Ej: Casa → Trabajo", color = TextMuted) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = GreenPrimary,
                            unfocusedBorderColor = GreenDark,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        )
                    )
                    OutlinedTextField(
                        value = origen,
                        onValueChange = { origen = it },
                        label = { Text("Origen", color = TextMuted) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = GreenPrimary,
                            unfocusedBorderColor = GreenDark,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        )
                    )
                    OutlinedTextField(
                        value = destino,
                        onValueChange = { destino = it },
                        label = { Text("Destino", color = TextMuted) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = GreenPrimary,
                            unfocusedBorderColor = GreenDark,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        )
                    )
                    Text("Selecciona la ruta:", fontSize = 12.sp, color = TextMuted)
                    rutasDisponibles.forEach { (id, nombre, empresa) ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = rutaSeleccionada == id,
                                onClick = { rutaSeleccionada = id },
                                colors = RadioButtonDefaults.colors(selectedColor = GreenAccent)
                            )
                            Column {
                                Text("Ruta $id · $nombre", fontSize = 13.sp, color = TextPrimary)
                                Text(empresa, fontSize = 11.sp, color = TextMuted)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (nombreRuta.isNotEmpty() && origen.isNotEmpty() && destino.isNotEmpty()) {
                            val nueva = RutaGuardada(
                                id = System.currentTimeMillis().toString(),
                                nombre = nombreRuta,
                                origen = origen,
                                destino = destino,
                                rutaId = rutaSeleccionada,
                                emoji = "📍"
                            )
                            rutas = rutas + nueva
                            nombreRuta = ""
                            origen = ""
                            destino = ""
                            mostrarDialogo = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
                ) {
                    Text("Guardar", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogo = false }) {
                    Text("Cancelar", color = TextMuted)
                }
            }
        )
    }
}

@Composable
fun AccesoCard(emoji: String, titulo: String, subtitulo: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = BgCard),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = emoji, fontSize = 24.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = titulo, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Text(text = subtitulo, fontSize = 10.sp, color = TextMuted)
        }
    }
}

@Composable
fun RutaCard(ruta: RutaGuardada, onEliminar: () -> Unit) {
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
                Text(text = ruta.emoji, fontSize = 20.sp)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = ruta.nombre, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Text(text = "${ruta.origen} → ${ruta.destino}", fontSize = 12.sp, color = TextMuted)
                Text(text = "Ruta ${ruta.rutaId}", fontSize = 11.sp, color = GreenAccent)
            }
            IconButton(onClick = onEliminar) {
                Icon(Icons.Default.Delete, null, tint = TextMuted, modifier = Modifier.size(18.dp))
            }
        }
    }
}