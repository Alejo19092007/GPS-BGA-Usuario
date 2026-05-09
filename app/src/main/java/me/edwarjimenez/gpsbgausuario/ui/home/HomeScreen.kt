package me.edwarjimenez.gpsbgausuario.ui.home

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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import me.edwarjimenez.gpsbgausuario.ui.theme.*

data class BusCercano(
    val rutaId: String,
    val nombreRuta: String,
    val empresa: String,
    val distancia: String,
    val tiempoLlegada: String,
    val velocidad: String,
    val paradaActual: String
)

@Composable
fun HomeScreen(onNavigateToBus: (String) -> Unit) {
    val auth = remember { FirebaseAuth.getInstance() }
    val usuario = auth.currentUser
    val db = remember { FirebaseDatabase.getInstance() }

    var buses by remember { mutableStateOf<List<BusCercano>>(emptyList()) }
    var filtroActivo by remember { mutableStateOf("todos") }

    val rutas = mapOf(
        "7" to Triple("Limoncito", "COTRANDER", listOf(
            "Terminal Los Cauchos", "Servientrega", "Carrera 8",
            "Paragüitas", "Bucarica", "Transversal Oriental",
            "CC Cacique", "Megamall", "Plaza Guarín",
            "Autopista Floridablanca", "Retorno Plata Acero"
        )),
        "36" to Triple("Igsabelar 33", "COTRANDER", listOf(
            "González Chaparro", "Barrio La Paz", "Papi Quiero Piña",
            "Miradores San Lorenzo", "CC Cacique", "Viaducto La Flora",
            "Carrera 33", "Megamall", "Plaza Guarín",
            "Plaza Satélite", "Puente Provenza", "Autopista Cañaveral"
        )),
        "27" to Triple("Caracolí - Centro", "LUSITANIA S.A.", listOf(
            "Terminal Caracolí", "Bucarica", "Bellavista",
            "Carretera Antigua", "Viaducto La Flora", "Carrera 33",
            "Calle 34", "Centro - Carrera 10", "Carrera 13", "Cacique Monterrey"
        ))
    )

    // Escuchar buses en Firebase
    LaunchedEffect(Unit) {
        db.getReference("buses").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lista = mutableListOf<BusCercano>()
                snapshot.children.forEach { busSnap ->
                    val rutaId = busSnap.child("rutaId").getValue(String::class.java) ?: return@forEach
                    val estado = busSnap.child("estado").getValue(String::class.java) ?: ""
                    if (estado != "EN_SERVICIO") return@forEach
                    val vel = busSnap.child("velocidad").getValue(Double::class.java)?.toInt() ?: 0
                    val paradaIndex = busSnap.child("paradaActual").getValue(Int::class.java) ?: 0
                    val rutaInfo = rutas[rutaId] ?: return@forEach
                    val paradas = rutaInfo.third
                    val paradaNombre = if (paradaIndex < paradas.size) paradas[paradaIndex] else "Última parada"
                    val tiempoEstimado = "${(paradaIndex + 1) * 3} min"
                    lista.add(
                        BusCercano(
                            rutaId = rutaId,
                            nombreRuta = "Ruta $rutaId · ${rutaInfo.first}",
                            empresa = rutaInfo.second,
                            distancia = "${String.format("%.1f", (paradaIndex + 1) * 0.8)} km",
                            tiempoLlegada = tiempoEstimado,
                            velocidad = "$vel km/h",
                            paradaActual = paradaNombre
                        )
                    )
                }
                buses = lista
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    Surface(modifier = Modifier.fillMaxSize(), color = BgDark) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "Buenos días 👋", fontSize = 13.sp, color = TextMuted)
                        Text(
                            text = usuario?.email?.substringBefore("@") ?: "Pasajero",
                            fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(50))
                            .background(GreenPrimary.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = usuario?.email?.take(1)?.uppercase() ?: "P",
                            fontSize = 18.sp, fontWeight = FontWeight.Bold, color = GreenAccent
                        )
                    }
                }
            }

            item {
                // Botones Casa / Trabajo / Favoritos
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AccesoRapidoBtn(
                        icono = Icons.Default.Home,
                        texto = "Casa",
                        activo = filtroActivo == "casa",
                        onClick = { filtroActivo = if (filtroActivo == "casa") "todos" else "casa" },
                        modifier = Modifier.weight(1f)
                    )
                    AccesoRapidoBtn(
                        icono = Icons.Default.Work,
                        texto = "Trabajo",
                        activo = filtroActivo == "trabajo",
                        onClick = { filtroActivo = if (filtroActivo == "trabajo") "todos" else "trabajo" },
                        modifier = Modifier.weight(1f)
                    )
                    AccesoRapidoBtn(
                        icono = Icons.Default.Star,
                        texto = "Favoritos",
                        activo = filtroActivo == "favoritos",
                        onClick = { filtroActivo = if (filtroActivo == "favoritos") "todos" else "favoritos" },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                Text(
                    text = if (buses.isEmpty()) "🔍 Buscando buses cercanos..." else "🚌 Buses en servicio (${buses.size})",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }

            if (buses.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = BgCard),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "🚌", fontSize = 48.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "No hay buses en servicio",
                                fontSize = 14.sp, color = TextMuted
                            )
                            Text(
                                text = "El conductor debe iniciar la ruta",
                                fontSize = 12.sp, color = TextMuted
                            )
                        }
                    }
                }
            } else {
                items(buses) { bus ->
                    BusCard(bus = bus, onClick = { onNavigateToBus(bus.rutaId) })
                }
            }

            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }
}

@Composable
fun AccesoRapidoBtn(
    icono: ImageVector,
    texto: String,
    activo: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = if (activo) GreenPrimary else BgCard
        ),
        shape = RoundedCornerShape(12.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                icono, null,
                tint = if (activo) Color.White else GreenAccent,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = texto,
                fontSize = 11.sp,
                color = if (activo) Color.White else TextMuted
            )
        }
    }
}

@Composable
fun BusCard(bus: BusCercano, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = BgCard),
        shape = RoundedCornerShape(16.dp),
        onClick = onClick
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = bus.nombreRuta, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Text(text = bus.empresa, fontSize = 11.sp, color = TextMuted)
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(GreenDark)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(text = "EN VIVO", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = GreenAccent)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                InfoChip(icono = Icons.Default.Schedule, texto = bus.tiempoLlegada, modifier = Modifier.weight(1f))
                InfoChip(icono = Icons.Default.LocationOn, texto = bus.distancia, modifier = Modifier.weight(1f))
                InfoChip(icono = Icons.Default.Speed, texto = bus.velocidad, modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(RoundedCornerShape(50))
                        .background(GreenAccent)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "En: ${bus.paradaActual}", fontSize = 12.sp, color = TextSecondary)
            }
        }
    }
}

@Composable
fun InfoChip(icono: ImageVector, texto: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(BgDark)
            .padding(horizontal = 8.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(icono, null, tint = GreenAccent, modifier = Modifier.size(14.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = texto, fontSize = 11.sp, color = TextPrimary)
    }
}