package me.edwarjimenez.gpsbgausuario.ui.map

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.maps.android.compose.*
import me.edwarjimenez.gpsbgausuario.ui.theme.*

data class BusEnMapa(
    val busId: String,
    val rutaId: String,
    val lat: Double,
    val lng: Double,
    val velocidad: Int,
    val paradaActual: Int
)

@Composable
fun MapaScreen() {
    val db = remember { FirebaseDatabase.getInstance() }
    val bucaramanga = LatLng(7.1254, -73.1198)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(bucaramanga, 13f)
    }

    var buses by remember { mutableStateOf<List<BusEnMapa>>(emptyList()) }

    val coordenadasRutas = mapOf(
        "7" to listOf(
            LatLng(7.1390, -73.1180), LatLng(7.1320, -73.1190), LatLng(7.1250, -73.1200),
            LatLng(7.1180, -73.1210), LatLng(7.1100, -73.1190), LatLng(7.1050, -73.1150),
            LatLng(7.0980, -73.1180), LatLng(7.0920, -73.1160), LatLng(7.0850, -73.1140),
            LatLng(7.0780, -73.1120), LatLng(7.0650, -73.1090)
        ),
        "36" to listOf(
            LatLng(7.0550, -73.0980), LatLng(7.0620, -73.1020), LatLng(7.0720, -73.1080),
            LatLng(7.0820, -73.1120), LatLng(7.0920, -73.1160), LatLng(7.1020, -73.1180),
            LatLng(7.1120, -73.1200), LatLng(7.1200, -73.1210), LatLng(7.1150, -73.1190),
            LatLng(7.1050, -73.1170), LatLng(7.0950, -73.1150), LatLng(7.0750, -73.1100)
        ),
        "27" to listOf(
            LatLng(7.0900, -73.0850), LatLng(7.0980, -73.0950), LatLng(7.1050, -73.1050),
            LatLng(7.1100, -73.1150), LatLng(7.1120, -73.1180), LatLng(7.1150, -73.1200),
            LatLng(7.1200, -73.1220), LatLng(7.1250, -73.1230), LatLng(7.1220, -73.1210),
            LatLng(7.1180, -73.1200)
        )
    )

    val coloresRutas = mapOf(
        "7" to android.graphics.Color.parseColor("#4CAF50"),
        "36" to android.graphics.Color.parseColor("#81C784"),
        "27" to android.graphics.Color.parseColor("#FFD700")
    )

    LaunchedEffect(Unit) {
        db.getReference("buses").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lista = mutableListOf<BusEnMapa>()
                snapshot.children.forEach { busSnap ->
                    val estado = busSnap.child("estado").getValue(String::class.java) ?: ""
                    if (estado != "EN_SERVICIO") return@forEach
                    val lat = busSnap.child("latitud").getValue(Double::class.java) ?: return@forEach
                    val lng = busSnap.child("longitud").getValue(Double::class.java) ?: return@forEach
                    val rutaId = busSnap.child("rutaId").getValue(String::class.java) ?: ""
                    val vel = busSnap.child("velocidad").getValue(Double::class.java)?.toInt() ?: 0
                    val parada = busSnap.child("paradaActual").getValue(Int::class.java) ?: 0
                    lista.add(BusEnMapa(busSnap.key ?: "", rutaId, lat, lng, vel, parada))
                }
                buses = lista
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(mapType = MapType.NORMAL),
            uiSettings = MapUiSettings(zoomControlsEnabled = false)
        ) {
            // Dibujar rutas
            coordenadasRutas.forEach { (rutaId, coords) ->
                val color = coloresRutas[rutaId] ?: android.graphics.Color.GREEN
                Polyline(
                    points = coords,
                    color = androidx.compose.ui.graphics.Color(color),
                    width = 6f
                )
            }

            // Marcadores de buses
            buses.forEach { bus ->
                val posicion = LatLng(bus.lat, bus.lng)
                Marker(
                    state = MarkerState(position = posicion),
                    title = "Ruta ${bus.rutaId}",
                    snippet = "Velocidad: ${bus.velocidad} km/h",
                    icon = BitmapDescriptorFactory.defaultMarker(
                        if (bus.rutaId == "7") BitmapDescriptorFactory.HUE_GREEN
                        else if (bus.rutaId == "36") BitmapDescriptorFactory.HUE_CYAN
                        else BitmapDescriptorFactory.HUE_YELLOW
                    )
                )
            }
        }

        // Panel superior
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.TopCenter)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = BgCard.copy(alpha = 0.95f)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Map, null, tint = GreenAccent, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Mapa en vivo — ${buses.size} bus(es) activo(s)",
                        fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary
                    )
                }
            }
        }

        // Leyenda de rutas
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
                .padding(bottom = 80.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = BgCard.copy(alpha = 0.95f)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(text = "Rutas", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        LeyendaItem(color = GreenAccent, texto = "Ruta 7")
                        LeyendaItem(color = androidx.compose.ui.graphics.Color(0xFF81C784), texto = "Ruta 36")
                        LeyendaItem(color = androidx.compose.ui.graphics.Color(0xFFFFD700), texto = "Ruta 27")
                    }
                }
            }
        }
    }
}

@Composable
fun LeyendaItem(color: androidx.compose.ui.graphics.Color, texto: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(RoundedCornerShape(50))
                .background(color)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = texto, fontSize = 11.sp, color = TextMuted)
    }
}