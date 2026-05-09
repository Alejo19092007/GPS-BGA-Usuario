package me.edwarjimenez.gpsbgausuario.ui.bus

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.database.*
import me.edwarjimenez.gpsbgausuario.ui.theme.*

@Composable
fun BusInfoScreen(
    rutaId: String,
    onBackClick: () -> Unit
) {
    val db = remember { FirebaseDatabase.getInstance() }

    var velocidad by remember { mutableStateOf("--") }
    var distancia by remember { mutableStateOf("--") }
    var tiempo by remember { mutableStateOf("--") }
    var paradaActual by remember { mutableStateOf("--") }
    var proximaParada by remember { mutableStateOf("--") }
    var tiempoLlegada by remember { mutableStateOf("--") }
    var enServicio by remember { mutableStateOf(false) }

    val paradas = mapOf(
        "7" to listOf(
            "Terminal Los Cauchos", "Servientrega", "Carrera 8",
            "Paragüitas", "Bucarica", "Transversal Oriental",
            "CC Cacique", "Megamall", "Plaza Guarín",
            "Autopista Floridablanca", "Retorno Plata Acero"
        ),
        "36" to listOf(
            "González Chaparro", "Barrio La Paz", "Papi Quiero Piña",
            "Miradores San Lorenzo", "CC Cacique", "Viaducto La Flora",
            "Carrera 33", "Megamall", "Plaza Guarín",
            "Plaza Satélite", "Puente Provenza", "Autopista Cañaveral"
        ),
        "27" to listOf(
            "Terminal Caracolí", "Bucarica", "Bellavista",
            "Carretera Antigua", "Viaducto La Flora", "Carrera 33",
            "Calle 34", "Centro - Carrera 10", "Carrera 13", "Cacique Monterrey"
        )
    )

    val nombresRutas = mapOf(
        "7" to "Limoncito",
        "36" to "Igsabelar 33",
        "27" to "Caracolí - Centro"
    )

    LaunchedEffect(rutaId) {
        db.getReference("buses").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach { busSnap ->
                    val rId = busSnap.child("rutaId").getValue(String::class.java) ?: return@forEach
                    val estado = busSnap.child("estado").getValue(String::class.java) ?: ""
                    if (rId != rutaId || estado != "EN_SERVICIO") return@forEach

                    enServicio = true
                    val vel = busSnap.child("velocidad").getValue(Double::class.java)?.toInt() ?: 0
                    val dist = busSnap.child("distanciaRecorrida").getValue(String::class.java) ?: "0"
                    val t = busSnap.child("tiempoEnRuta").getValue(Long::class.java) ?: 0L
                    val paradaIndex = busSnap.child("paradaActual").getValue(Int::class.java) ?: 0
                    val listaParadas = paradas[rutaId] ?: emptyList()

                    velocidad = "$vel km/h"
                    distancia = "$dist km"
                    tiempo = "${t / 60} min"
                    paradaActual = if (paradaIndex < listaParadas.size) listaParadas[paradaIndex] else "Última parada"
                    proximaParada = if (paradaIndex + 1 < listaParadas.size) listaParadas[paradaIndex + 1] else "Final de ruta"
                    tiempoLlegada = "${(paradaIndex + 1) * 3} min"
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    Surface(modifier = Modifier.fillMaxSize(), color = BgDark) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = GreenAccent)
                }
                Column {
                    Text(
                        text = "Ruta $rutaId · ${nombresRutas[rutaId] ?: ""}",
                        fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary
                    )
                    Text(text = "Información en tiempo real", fontSize = 12.sp, color = TextMuted)
                }
            }

            Column(modifier = Modifier.padding(horizontal = 16.dp)) {

                // Estado
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = if (enServicio) GreenDark else BgCard),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = if (enServicio) "🟢" else "🔴", fontSize = 24.sp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = if (enServicio) "Bus en servicio" else "Bus fuera de servicio",
                                fontSize = 16.sp, fontWeight = FontWeight.Bold,
                                color = if (enServicio) GreenAccent else TextMuted
                            )
                            Text(
                                text = if (enServicio) "Datos actualizándose en tiempo real" else "El conductor no ha iniciado la ruta",
                                fontSize = 12.sp, color = TextMuted
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Estadísticas
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    EstadisticaBus("Llegada", tiempoLlegada, Icons.Default.Schedule, Modifier.weight(1f))
                    EstadisticaBus("Distancia", distancia, Icons.Default.LocationOn, Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    EstadisticaBus("Velocidad", velocidad, Icons.Default.Speed, Modifier.weight(1f))
                    EstadisticaBus("Tiempo", tiempo, Icons.Default.Timer, Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Paradas
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = BgCard),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "📍 Paradas", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Spacer(modifier = Modifier.height(12.dp))

                        ParadaItem(
                            titulo = "Parada actual",
                            valor = paradaActual,
                            color = GreenAccent
                        )

                        HorizontalDivider(color = GreenDark, modifier = Modifier.padding(vertical = 8.dp))

                        ParadaItem(
                            titulo = "Próxima parada",
                            valor = proximaParada,
                            color = YellowAccent
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Lista de paradas de la ruta
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = BgCard),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "🗺️ Recorrido completo", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Spacer(modifier = Modifier.height(12.dp))
                        paradas[rutaId]?.forEachIndexed { index, parada ->
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(RoundedCornerShape(50))
                                        .background(
                                            if (parada == paradaActual) GreenPrimary
                                            else GreenDark
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "${index + 1}",
                                        fontSize = 10.sp,
                                        color = if (parada == paradaActual) Color.White else TextMuted
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = parada,
                                    fontSize = 13.sp,
                                    color = if (parada == paradaActual) GreenAccent else TextPrimary,
                                    fontWeight = if (parada == paradaActual) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
fun EstadisticaBus(
    label: String,
    valor: String,
    icono: ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = BgCard),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icono, null, tint = GreenAccent, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = label, fontSize = 10.sp, color = TextMuted)
            Text(text = valor, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        }
    }
}

@Composable
fun ParadaItem(titulo: String, valor: String, color: Color) {
    Column {
        Text(text = titulo, fontSize = 11.sp, color = TextMuted)
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = valor, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = color)
    }
}