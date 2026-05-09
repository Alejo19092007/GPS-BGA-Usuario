package me.edwarjimenez.gpsbgausuario

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import me.edwarjimenez.gpsbgausuario.ui.auth.LoginScreen
import me.edwarjimenez.gpsbgausuario.ui.auth.RegistroScreen
import me.edwarjimenez.gpsbgausuario.ui.auth.RecuperarScreen
import me.edwarjimenez.gpsbgausuario.ui.home.HomeScreen
import me.edwarjimenez.gpsbgausuario.ui.map.MapaScreen
import me.edwarjimenez.gpsbgausuario.ui.notifications.NotificacionesScreen
import me.edwarjimenez.gpsbgausuario.ui.routes.MisRutasScreen
import me.edwarjimenez.gpsbgausuario.ui.profile.PerfilScreen
import me.edwarjimenez.gpsbgausuario.ui.profile.MiCuentaScreen
import me.edwarjimenez.gpsbgausuario.ui.profile.HistorialScreen
import me.edwarjimenez.gpsbgausuario.ui.profile.AyudaScreen
import me.edwarjimenez.gpsbgausuario.ui.profile.AcercaDeScreen
import me.edwarjimenez.gpsbgausuario.ui.bus.BusInfoScreen
import me.edwarjimenez.gpsbgausuario.ui.theme.GpsBGAUsuarioTheme
import me.edwarjimenez.gpsbgausuario.ui.theme.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GpsBGAUsuarioTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                val rutasSinNavBar = listOf("login", "registro", "recuperar")
                val mostrarNavBar = currentRoute !in rutasSinNavBar &&
                        currentRoute != "bus_info/{rutaId}" &&
                        currentRoute != "mi_cuenta" &&
                        currentRoute != "historial" &&
                        currentRoute != "ayuda" &&
                        currentRoute != "acerca_de"

                Scaffold(
                    bottomBar = {
                        if (mostrarNavBar) {
                            NavigationBar(
                                containerColor = BgCard,
                                tonalElevation = 0.dp
                            ) {
                                NavigationBarItem(
                                    selected = currentRoute == "home",
                                    onClick = {
                                        navController.navigate("home") {
                                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    },
                                    icon = { Icon(Icons.Default.Home, null) },
                                    label = { Text("Inicio", fontSize = 10.sp) },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = GreenAccent,
                                        selectedTextColor = GreenAccent,
                                        unselectedIconColor = TextMuted,
                                        unselectedTextColor = TextMuted,
                                        indicatorColor = GreenPrimary.copy(alpha = 0.2f)
                                    )
                                )
                                NavigationBarItem(
                                    selected = currentRoute == "mapa",
                                    onClick = {
                                        navController.navigate("mapa") {
                                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    },
                                    icon = { Icon(Icons.Default.Map, null) },
                                    label = { Text("Mapa", fontSize = 10.sp) },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = GreenAccent,
                                        selectedTextColor = GreenAccent,
                                        unselectedIconColor = TextMuted,
                                        unselectedTextColor = TextMuted,
                                        indicatorColor = GreenPrimary.copy(alpha = 0.2f)
                                    )
                                )
                                NavigationBarItem(
                                    selected = currentRoute == "notificaciones",
                                    onClick = {
                                        navController.navigate("notificaciones") {
                                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    },
                                    icon = { Icon(Icons.Default.Notifications, null) },
                                    label = { Text("Alertas", fontSize = 10.sp) },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = GreenAccent,
                                        selectedTextColor = GreenAccent,
                                        unselectedIconColor = TextMuted,
                                        unselectedTextColor = TextMuted,
                                        indicatorColor = GreenPrimary.copy(alpha = 0.2f)
                                    )
                                )
                                NavigationBarItem(
                                    selected = currentRoute == "mis_rutas",
                                    onClick = {
                                        navController.navigate("mis_rutas") {
                                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    },
                                    icon = { Icon(Icons.Default.Route, null) },
                                    label = { Text("Rutas", fontSize = 10.sp) },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = GreenAccent,
                                        selectedTextColor = GreenAccent,
                                        unselectedIconColor = TextMuted,
                                        unselectedTextColor = TextMuted,
                                        indicatorColor = GreenPrimary.copy(alpha = 0.2f)
                                    )
                                )
                                NavigationBarItem(
                                    selected = currentRoute == "perfil",
                                    onClick = {
                                        navController.navigate("perfil") {
                                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    },
                                    icon = { Icon(Icons.Default.Person, null) },
                                    label = { Text("Perfil", fontSize = 10.sp) },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = GreenAccent,
                                        selectedTextColor = GreenAccent,
                                        unselectedIconColor = TextMuted,
                                        unselectedTextColor = TextMuted,
                                        indicatorColor = GreenPrimary.copy(alpha = 0.2f)
                                    )
                                )
                            }
                        }
                    },
                    containerColor = BgDark
                ) { paddingValues ->
                    NavHost(
                        navController = navController,
                        startDestination = "login",
                        modifier = Modifier.fillMaxSize().padding(paddingValues)
                    ) {
                        composable("login") {
                            LoginScreen(
                                onLoginSuccess = {
                                    navController.navigate("home") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                },
                                onNavigateToRegistro = { navController.navigate("registro") },
                                onNavigateToRecuperar = { navController.navigate("recuperar") }
                            )
                        }
                        composable("registro") {
                            RegistroScreen(
                                onRegistroSuccess = {
                                    navController.navigate("home") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                },
                                onBackClick = { navController.popBackStack() }
                            )
                        }
                        composable("recuperar") {
                            RecuperarScreen(onBackClick = { navController.popBackStack() })
                        }
                        composable("home") {
                            HomeScreen(
                                onNavigateToBus = { rutaId ->
                                    navController.navigate("bus_info/$rutaId")
                                }
                            )
                        }
                        composable("mapa") {
                            MapaScreen()
                        }
                        composable("bus_info/{rutaId}") { backStackEntry ->
                            BusInfoScreen(
                                rutaId = backStackEntry.arguments?.getString("rutaId") ?: "7",
                                onBackClick = { navController.popBackStack() }
                            )
                        }
                        composable("notificaciones") {
                            NotificacionesScreen()
                        }
                        composable("mis_rutas") {
                            MisRutasScreen()
                        }
                        composable("perfil") {
                            PerfilScreen(
                                onLogout = {
                                    navController.navigate("login") {
                                        popUpTo(0) { inclusive = true }
                                    }
                                },
                                onMiCuenta = { navController.navigate("mi_cuenta") },
                                onHistorial = { navController.navigate("historial") },
                                onMisFavoritos = { navController.navigate("mis_rutas") },
                                onAyuda = { navController.navigate("ayuda") },
                                onAcercaDe = { navController.navigate("acerca_de") }
                            )
                        }
                        composable("mi_cuenta") {
                            MiCuentaScreen(onBackClick = { navController.popBackStack() })
                        }
                        composable("historial") {
                            HistorialScreen(onBackClick = { navController.popBackStack() })
                        }
                        composable("ayuda") {
                            AyudaScreen(onBackClick = { navController.popBackStack() })
                        }
                        composable("acerca_de") {
                            AcercaDeScreen(onBackClick = { navController.popBackStack() })
                        }
                    }
                }
            }
        }
    }
}