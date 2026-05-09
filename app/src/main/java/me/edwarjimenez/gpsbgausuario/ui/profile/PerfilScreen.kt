package me.edwarjimenez.gpsbgausuario.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
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
import com.google.firebase.database.FirebaseDatabase
import me.edwarjimenez.gpsbgausuario.ui.theme.*

@Composable
fun PerfilScreen(onLogout: () -> Unit) {
    val auth = remember { FirebaseAuth.getInstance() }
    val usuario = auth.currentUser
    val db = remember { FirebaseDatabase.getInstance() }

    var nombreCompleto by remember { mutableStateOf("") }
    var notificaciones by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        val uid = usuario?.uid ?: return@LaunchedEffect
        db.getReference("usuarios").child(uid).get()
            .addOnSuccessListener { snapshot ->
                nombreCompleto = snapshot.child("nombreCompleto").getValue(String::class.java) ?: ""
            }
    }

    val redPrimary = Color(0xFFFF3B57)
    val redBg = Color(0xFF3A0005)

    Surface(modifier = Modifier.fillMaxSize(), color = BgDark) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Header perfil
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(50))
                        .background(GreenPrimary.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = (nombreCompleto.ifEmpty { usuario?.email ?: "P" }).take(2).uppercase(),
                        fontSize = 28.sp, fontWeight = FontWeight.Bold, color = GreenAccent
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = nombreCompleto.ifEmpty { usuario?.email?.substringBefore("@") ?: "Pasajero" },
                    fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextPrimary
                )
                Text(text = usuario?.email ?: "", fontSize = 12.sp, color = TextMuted)
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(GreenDark)
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(text = "🐆 Pasajero GPSBGA", fontSize = 12.sp, color = GreenAccent)
                }
            }

            HorizontalDivider(color = GreenDark, modifier = Modifier.padding(horizontal = 16.dp))
            Spacer(modifier = Modifier.height(8.dp))

            Column(modifier = Modifier.padding(horizontal = 16.dp)) {

                MenuItemPerfil(Icons.Default.Person, "Mi Cuenta", TextPrimary, TextMuted, BgCard) {}
                Spacer(modifier = Modifier.height(8.dp))
                MenuItemPerfil(Icons.Default.History, "Historial de Viajes", TextPrimary, TextMuted, BgCard) {}
                Spacer(modifier = Modifier.height(8.dp))
                MenuItemPerfil(Icons.Default.Star, "Mis Favoritos", TextPrimary, TextMuted, BgCard) {}
                Spacer(modifier = Modifier.height(8.dp))

                // Notificaciones
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = BgCard),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier.size(36.dp).clip(RoundedCornerShape(8.dp)).background(BgDark),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Notifications, null, tint = TextMuted, modifier = Modifier.size(18.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Notificaciones", fontSize = 14.sp, color = TextPrimary, modifier = Modifier.weight(1f))
                        Switch(
                            checked = notificaciones,
                            onCheckedChange = { notificaciones = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = GreenPrimary
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                MenuItemPerfil(Icons.Default.Help, "Ayuda / Soporte", TextPrimary, TextMuted, BgCard) {}
                Spacer(modifier = Modifier.height(8.dp))
                MenuItemPerfil(Icons.Default.Info, "Acerca de GPSBGA", TextPrimary, TextMuted, BgCard) {}
                Spacer(modifier = Modifier.height(8.dp))

                // Cerrar sesión
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = redBg),
                    shape = RoundedCornerShape(12.dp),
                    onClick = { auth.signOut(); onLogout() }
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ExitToApp, null, tint = redPrimary, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Cerrar Sesión", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = redPrimary)
                    }
                }

                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
fun MenuItemPerfil(
    icono: ImageVector,
    texto: String,
    textPrimary: Color,
    textMuted: Color,
    bgCard: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = bgCard),
        shape = RoundedCornerShape(12.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(36.dp).clip(RoundedCornerShape(8.dp)).background(BgDark),
                contentAlignment = Alignment.Center
            ) {
                Icon(icono, null, tint = TextMuted, modifier = Modifier.size(18.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = texto, fontSize = 14.sp, color = textPrimary, modifier = Modifier.weight(1f))
            Text(text = "›", fontSize = 18.sp, color = textMuted)
        }
    }
}