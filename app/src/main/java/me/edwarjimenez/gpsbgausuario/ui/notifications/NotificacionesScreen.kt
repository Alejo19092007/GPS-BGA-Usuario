package me.edwarjimenez.gpsbgausuario.ui.notifications

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import me.edwarjimenez.gpsbgausuario.ui.theme.*

data class Notificacion(
    val id: String,
    val titulo: String,
    val mensaje: String,
    val hora: String,
    val tipo: String
)

@Composable
fun NotificacionesScreen() {
    val db = remember { FirebaseDatabase.getInstance() }
    var notificaciones by remember { mutableStateOf<List<Notificacion>>(emptyList()) }

    // Escuchar cambios en buses y generar notificaciones
    LaunchedEffect(Unit) {
        db.getReference("buses").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lista = mutableListOf<Notificacion>()
                snapshot.children.forEach { busSnap ->
                    val rutaId = busSnap.child("rutaId").getValue(String::class.java) ?: return@forEach
                    val estado = busSnap.child("estado").getValue(String::class.java) ?: ""
                    val paradaIndex = busSnap.child("paradaActual").getValue(Int::class.java) ?: 0
                    val vel = busSnap.child("velocidad").getValue(Double::class.java)?.toInt() ?: 0

                    val nombresRutas = mapOf(
                        "7" to "Limoncito",
                        "36" to "Igsabelar 33",
                        "27" to "Caracolí - Centro"
                    )

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

                    if (estado == "EN_SERVICIO") {
                        val paradaNombre = paradas[rutaId]?.getOrNull(paradaIndex) ?: "Parada $paradaIndex"
                        lista.add(
                            Notificacion(
                                id = busSnap.key ?: "",
                                titulo = "🚌 Ruta $rutaId en servicio",
                                mensaje = "El bus va a ${vel}km/h — En: $paradaNombre",
                                hora = "Ahora",
                                tipo = "activo"
                            )
                        )
                        if (paradaIndex > 0) {
                            lista.add(
                                Notificacion(
                                    id = "${busSnap.key}_parada",
                                    titulo = "📍 Ruta $rutaId · ${nombresRutas[rutaId]}",
                                    mensaje = "Llegando a parada: $paradaNombre",
                                    hora = "Hace ${paradaIndex * 2} min",
                                    tipo = "parada"
                                )
                            )
                        }
                    } else {
                        lista.add(
                            Notificacion(
                                id = busSnap.key ?: "",
                                titulo = "⚠️ Ruta $rutaId fuera de servicio",
                                mensaje = "El bus no está en operación",
                                hora = "Hace un momento",
                                tipo = "inactivo"
                            )
                        )
                    }
                }
                notificaciones = lista
            }
            override fun onCancelled(error: DatabaseError) {}
        })
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
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Notifications, null, tint = GreenAccent, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Notificaciones",
                        fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextPrimary
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (notificaciones.isEmpty()) {
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
                            Text(text = "🔔", fontSize = 48.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = "Sin notificaciones", fontSize = 14.sp, color = TextMuted)
                        }
                    }
                }
            } else {
                items(notificaciones) { notif ->
                    NotificacionCard(notif = notif)
                }
            }

            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }
}

@Composable
fun NotificacionCard(notif: Notificacion) {
    val color = when (notif.tipo) {
        "activo" -> GreenAccent
        "parada" -> YellowAccent
        else -> TextMuted
    }

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
                    .size(40.dp)
                    .clip(RoundedCornerShape(50))
                    .background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    when (notif.tipo) {
                        "activo" -> Icons.Default.DirectionsBus
                        "parada" -> Icons.Default.LocationOn
                        else -> Icons.Default.Warning
                    },
                    null,
                    tint = color,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = notif.titulo, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Text(text = notif.mensaje, fontSize = 12.sp, color = TextMuted)
            }
            Text(text = notif.hora, fontSize = 10.sp, color = TextMuted)
        }
    }
}