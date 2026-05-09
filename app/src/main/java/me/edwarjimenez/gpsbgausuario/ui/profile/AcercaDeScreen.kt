package me.edwarjimenez.gpsbgausuario.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.edwarjimenez.gpsbgausuario.ui.theme.*

@Composable
fun AcercaDeScreen(onBackClick: () -> Unit) {
    Surface(modifier = Modifier.fillMaxSize(), color = BgDark) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = GreenAccent)
                }
                Text("Acerca de GPSBGA", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            }

            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "🐆", fontSize = 80.sp)
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "GPSBGA",
                    fontSize = 32.sp, fontWeight = FontWeight.Bold, color = GreenAccent,
                    letterSpacing = 4.sp
                )
                Text(text = "Versión 1.0.0", fontSize = 12.sp, color = TextMuted)
                Spacer(modifier = Modifier.height(24.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = BgCard),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Sistema de monitoreo de transporte público de Bucaramanga en tiempo real.",
                            fontSize = 14.sp, color = TextPrimary, textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Características
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = BgCard),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("✨ Características", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = GreenAccent)
                        Spacer(modifier = Modifier.height(12.dp))
                        CaracteristicaItem("🗺️", "Mapa en tiempo real de buses")
                        CaracteristicaItem("🔔", "Notificaciones de llegada")
                        CaracteristicaItem("📍", "Seguimiento de paradas")
                        CaracteristicaItem("⭐", "Rutas favoritas")
                        CaracteristicaItem("🚌", "3 rutas de Bucaramanga")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Rutas disponibles
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = BgCard),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("🚌 Rutas disponibles", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = GreenAccent)
                        Spacer(modifier = Modifier.height(12.dp))
                        RutaItem("7", "Limoncito", "COTRANDER · 23km")
                        Spacer(modifier = Modifier.height(8.dp))
                        RutaItem("36", "Igsabelar 33", "COTRANDER · 24km")
                        Spacer(modifier = Modifier.height(8.dp))
                        RutaItem("27", "Caracolí - Centro", "LUSITANIA S.A. · 27km")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Desarrolladores
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = BgCard),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("👨‍💻 Desarrollado por", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = GreenAccent)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Equipo GPSBGA", fontSize = 13.sp, color = TextPrimary)
                        Text("Universidad · Proyecto Scrum 2026", fontSize = 12.sp, color = TextMuted)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Bucaramanga, Colombia 🇨🇴", fontSize = 12.sp, color = TextMuted)
                    }
                }

                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
fun CaracteristicaItem(emoji: String, texto: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = emoji, fontSize = 16.sp)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = texto, fontSize = 13.sp, color = TextPrimary)
    }
}

@Composable
fun RutaItem(numero: String, nombre: String, info: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(GreenDark),
            contentAlignment = Alignment.Center
        ) {
            Text(text = numero, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = GreenAccent)
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(text = nombre, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Text(text = info, fontSize = 11.sp, color = TextMuted)
        }
    }
}