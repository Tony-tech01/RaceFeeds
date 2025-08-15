package com.example.racefeeds.ui.Components

import FarmTool
import OrderSuccessScreen
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.racefeeds.R
import com.example.racefeeds.data.BottomNavItem
import com.example.racefeeds.ui.screens.Animal.FarmPage
import com.example.racefeeds.ui.screens.Cart.CartScreen
import com.example.racefeeds.ui.screens.Cart.CartViewModel
import com.example.racefeeds.ui.screens.Checkout.CheckoutScreen
import com.example.racefeeds.ui.screens.Checkout.CheckoutViewModel
import com.example.racefeeds.ui.screens.Farm.FarmScreen
import com.example.racefeeds.ui.screens.Farm.FarmToolDetailsScreen
import com.example.racefeeds.ui.screens.OrderHistory.OrderHistoryScreen
import com.example.racefeeds.ui.screens.OrderHistory.OrderHistoryViewModel
import com.example.racefeeds.ui.screens.admin.AccessDeniedScreen
import com.example.racefeeds.ui.screens.admin.AdminScreen
import com.example.racefeeds.ui.screens.firebase.AuthGateScreen
import com.example.racefeeds.ui.screens.firebase.AuthViewModel
import com.example.racefeeds.ui.screens.firebase.LoginScreen
import com.example.racefeeds.ui.screens.firebase.SignupScreen
import com.example.racefeeds.ui.screens.settings.SettingsScreen
import com.example.racefeeds.ui.screens.settings.SettingsViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearchTriggered: () -> Unit = {},
    placeholder: String = "Search...",
    modifier: Modifier = Modifier,
) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = { Text(placeholder) },
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        singleLine = true,
        shape = RoundedCornerShape(16.dp),
        leadingIcon = {
            Icon(Icons.Default.Search, contentDescription = "Search Icon")
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(Icons.Default.Clear, contentDescription = "Clear Icon")
                }
            }
        },
        keyboardActions = KeyboardActions(onSearch = { onSearchTriggered() }),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search)
    )

}

@Composable
fun HeadWithSeeAll(
    title: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title, style = MaterialTheme.typography.headlineLarge, fontSize = 24.sp
        )

        Text(
            text = "See All",
            style = MaterialTheme.typography.bodyLarge,
            modifier = modifier.clickable { },
        )
    }
}

@Composable
fun CartIconWithBadge(cartCount: Int, onCartClick: () -> Unit) {
    Box {
        IconButton(onClick = onCartClick) {
            Icon(
                painter = painterResource(R.drawable.cart), contentDescription = "Cart"
            )

        }
        if (cartCount > 0) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 4.dp, end = 4.dp)
                    .size(16.dp)
                    .background(MaterialTheme.colorScheme.primary, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = cartCount.toString(), color = Color.Yellow, fontSize = 10.sp
                )
            }
        }
    }
}


@Composable
fun AppNavGraph(
    navController: NavHostController,
    cartViewModel: CartViewModel,
    checkoutViewModel: CheckoutViewModel,
    contentPadding: PaddingValues,
    onNavigateToCheckout: () -> Unit,
    settingsViewModel: SettingsViewModel,
    authViewModel: AuthViewModel,
    orderHistoryViewModel: OrderHistoryViewModel,
) {


    NavHost(
        navController = navController, startDestination = BottomNavItem.Feed.route
    ) {

        composable(BottomNavItem.Feed.route) {
            FarmPage(
                cartViewModel = cartViewModel,
                onNavigateToCart = { navController.navigate(BottomNavItem.Cart.route) },
                navController = navController
            )
        }

        composable(BottomNavItem.Cart.route) {
            CartScreen(
                cartViewModel = cartViewModel,
                onBackClick = { navController.popBackStack() },
                onNavigateToCheckout = { navController.navigate("checkout") },
                navController = navController
            )
        }

        composable(BottomNavItem.Farm.route) {
            FarmScreen(
                cartViewModel = cartViewModel,
                navController = navController,
                onNavigateToCart = { navController.navigate(BottomNavItem.Cart.route) },
            )
        }

        composable("checkout") {
            CheckoutScreen(
                navController = navController,
                checkoutViewModel = checkoutViewModel,
                innerPadding = contentPadding,
                cartViewModel = cartViewModel,
                authViewModel = authViewModel
            )
        }

        composable("farmToolDetails/{toolId}") { backStackEntry ->
            val toolId = backStackEntry.arguments?.getString("toolId") ?: ""
            FarmToolDetailsScreen(
                toolId = toolId, cartViewModel = cartViewModel, navController = navController
            )
        }

        composable("order_success") {
            OrderSuccessScreen(navController = navController, cartViewModel = cartViewModel)
        }
        composable("settings") {
            SettingsScreen(
                settingsViewModel,
                innerPadding = contentPadding,
                authViewModel = authViewModel,
                navController = navController
            )
        }

        composable("order_history") {
            OrderHistoryScreen(
                navController = navController,
                orderHistoryViewModel = orderHistoryViewModel,
                innerPadding = contentPadding,
                authViewModel = authViewModel
            )
        }

        composable(
            route = "login?returnTo={returnTo}", arguments = listOf(navArgument("returnTo") {
                type = NavType.StringType
                defaultValue = BottomNavItem.Feed.route
            })
        ) { backStackEntry ->
            val returnTo =
                backStackEntry.arguments?.getString("returnTo") ?: BottomNavItem.Feed.route
            LoginScreen(
                navController = navController, authViewModel = authViewModel, returnTo = returnTo
            )
        }

        composable(
            route = "signup?returnTo={returnTo}", arguments = listOf(navArgument("returnTo") {
                type = NavType.StringType
                defaultValue = BottomNavItem.Farm.route
            })
        ) { backStackEntry ->
            val returnTo =
                backStackEntry.arguments?.getString("returnTo") ?: BottomNavItem.Farm.route
            SignupScreen(
                navController = navController, authViewModel = authViewModel, returnTo = returnTo
            )
        }

        composable("authGate") {
            AuthGateScreen(
                navController = navController, authViewModel = authViewModel
            )
        }

        composable("admin") {

                AdminScreen(
                    orderHistoryViewModel = orderHistoryViewModel,
                    navController = navController,
                    innerPadding = contentPadding
                )
            }
    }
}

@Composable
fun BottomBar(
    navController: NavController,
    currentDestination: NavDestination?,
) {
    val items = listOf(
        BottomNavItem.Feed, BottomNavItem.Farm, BottomNavItem.Cart
    )
    val cartViewModel: CartViewModel = viewModel()
    val cartCount by cartViewModel.cartCount.collectAsState()

    NavigationBar {
        items.forEach { item ->
            val isSelected = currentDestination?.route == item.route

            NavigationBarItem(selected = isSelected, onClick = {
                if (!isSelected) {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }, icon = {
                if (item is BottomNavItem.Cart && cartCount > 0) {
                    BadgedBox(
                        badge = {
                            Badge { Text(cartCount.toString()) }
                        }) {
                        Icon(
                            painter = painterResource(id = item.iconRes),
                            contentDescription = item.label
                        )
                    }
                } else {
                    Icon(
                        painter = painterResource(id = item.iconRes),
                        contentDescription = item.label
                    )
                }
            }, label = { Text(item.label) })
        }
    }
}

@Composable
fun ToolCard(
    tool: FarmTool,
    onClick: () -> Unit,
    cartViewModel: CartViewModel,
    navController: NavController,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)

    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Image(
                painter = painterResource(id = tool.imageRes),
                contentDescription = tool.name,
                modifier = Modifier
                    .height(100.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = tool.name, fontWeight = FontWeight.Bold)
                    Text(text = "Ksh ${tool.price}", style = MaterialTheme.typography.bodyMedium)
                }
                IconButton(onClick = {
                    cartViewModel.addToCart(
                        tool.name, tool.price
                    )
                }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add to cart",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}


@Composable
fun ScreenTitleWithCart(
    title: String,
    cartCount: Int,
    onNavigateToCart: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title, style = MaterialTheme.typography.headlineLarge, fontSize = 24.sp
        )
        CartIconWithBadge(
            cartCount = cartCount, onCartClick = { onNavigateToCart() })
    }
}



















