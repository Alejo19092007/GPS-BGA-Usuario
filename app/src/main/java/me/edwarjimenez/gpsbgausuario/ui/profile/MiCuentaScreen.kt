package me.edwarjimenez.gpsbgausuario.ui.profile

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import me.edwarjimenez.gpsbgausuario.ui.theme.*

@Composable
fun MiCuentaScreen(onBackClick: () -> Unit) {
    val auth = remember { FirebaseAuth.getInstance() }
    val db = remember { FirebaseDatabase.getInstance() }
    val context = LocalContext.current
    val uid = auth.currentUser?.uid ?: ""

    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf(auth.currentUser?.email ?: "") }
    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        db.getReference("usuarios").child(uid).get()
            .addOnSuccessListener { snapshot ->
                nombre = snapshot.child("nombreCompleto").getValue(String::class.java) ?: ""
            }
    }

    Surface(modifier = Modifier.fillMaxSize(), color = BgDark) {
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = GreenAccent)
                }
                Text("Mi Cuenta", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Nombre completo", fontSize = 12.sp, color = TextMuted)
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Tu nombre", color = TextMuted) },
                    leadingIcon = { Icon(Icons.Default.Person, null, tint = TextMuted) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GreenPrimary,
                        unfocusedBorderColor = GreenDark,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Correo electrónico", fontSize = 12.sp, color = TextMuted)
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = {},
                    modifier = Modifier.fillMaxWidth(),
                    enabled = false,
                    leadingIcon = { Icon(Icons.Default.Email, null, tint = TextMuted) },
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledBorderColor = GreenDark,
                        disabledTextColor = TextMuted
                    )
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        if (nombre.isNotEmpty()) {
                            isLoading = true
                            db.getReference("usuarios").child(uid)
                                .child("nombreCompleto").setValue(nombre)
                                .addOnCompleteListener {
                                    isLoading = false
                                    Toast.makeText(context, "Perfil actualizado", Toast.LENGTH_SHORT).show()
                                    onBackClick()
                                }
                        }
                    },
                    enabled = !isLoading,
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    } else {
                        Text("Guardar cambios", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    }
}