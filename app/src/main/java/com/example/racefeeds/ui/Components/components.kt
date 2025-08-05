package com.example.racefeeds.ui.Components

import FarmTool
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.racefeeds.R
import com.example.racefeeds.data.BottomNavItem
import com.example.racefeeds.ui.screens.Cart.CartScreen
import com.example.racefeeds.ui.screens.Cart.CartViewModel
import com.example.racefeeds.ui.screens.Animal.FarmPage
import androidx.compose.material3.*
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.compose.runtime.getValue
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import com.example.racefeeds.ui.screens.Checkout.CheckoutScreen
import com.example.racefeeds.ui.screens.Checkout.CheckoutViewModel
import com.example.racefeeds.ui.screens.Checkout.OrderSuccessScreen
import com.example.racefeeds.ui.screens.Farm.FarmScreen
import com.example.racefeeds.ui.screens.Farm.FarmToolDetailsScreen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearchTriggered: () -> Unit = {},
    placeholder: String = "Search...",
    modifier: Modifier = Modifier,
){
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
           if (query.isNotEmpty()){
               IconButton(onClick = { onQueryChange("") }){
                   Icon(Icons.Default.Clear, contentDescription = "Clear Icon")
               }
           }
        } ,
        keyboardActions = KeyboardActions(onSearch = { onSearchTriggered() }),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search)
    )

}

@Composable
fun HeadWithSeeAll(
    title: String,
    modifier: Modifier = Modifier,
){
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(
            text = title,
            style = MaterialTheme.typography.headlineLarge
        )

        Text(
            text = "See All",
            style = MaterialTheme.typography.bodyLarge,
            modifier = modifier.clickable {  },
        )
    }
}

@Composable
fun CartIconWithBadge(cartCount: Int, onCartClick: () -> Unit){
    Box{
        IconButton(onClick = onCartClick) {
            Icon(
                painter = painterResource(R.drawable.cart),
                contentDescription = "Cart"
            )

        }
        if (cartCount > 0){
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 4.dp, end = 4.dp)
                    .size(16.dp)
                    .background(MaterialTheme.colorScheme.primary, CircleShape),
                contentAlignment = Alignment.Center
            ){
                Text(
                    text = cartCount.toString(),
                    color = Color.Yellow,
                    fontSize = 10.sp
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
    onNavigateToCheckout: () -> Unit
) {
    NavHost(navController = navController, startDestination = BottomNavItem.Feed.route) {

        composable(BottomNavItem.Feed.route) {
            FarmPage(
                cartViewModel = cartViewModel,
                onNavigateToCart = { navController.navigate(BottomNavItem.Cart.route) }
            )
        }

        composable(BottomNavItem.Cart.route) {
            CartScreen(
                cartViewModel = cartViewModel,
                onBackClick = { navController.popBackStack() },
                onNavigateToCheckout = { navController.navigate("checkout") } ,
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
                cartViewModel = cartViewModel
            )
        }

        composable("farmToolDetails/{toolId}") { backStackEntry ->
            val toolId = backStackEntry.arguments?.getString("toolId") ?: ""
            FarmToolDetailsScreen(
                toolId = toolId,
                cartViewModel = cartViewModel,
                navController = navController
                )
        }

        composable("order_success") {
            OrderSuccessScreen()
        }
    }
}





@Composable
fun BottomBar(
    navController: NavController,
    currentDestination: NavDestination?
) {
    val items = listOf(
        BottomNavItem.Feed,
        BottomNavItem.Farm,
        BottomNavItem.Cart
    )
    val cartViewModel: CartViewModel = viewModel()
    val cartCount by cartViewModel.cartCount.collectAsState()

    NavigationBar {
        items.forEach { item ->
            val isSelected = currentDestination?.route == item.route

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    if (!isSelected) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    if (item is BottomNavItem.Cart && cartCount > 0){
                        BadgedBox(
                            badge = {
                                Badge{ Text(cartCount.toString()) }
                            }
                        ){
                            Icon(
                                painter = painterResource(id = item.iconRes),
                                contentDescription = item.label
                            )
                        }
                    }else{
                        Icon(
                            painter = painterResource(id = item.iconRes),
                            contentDescription = item.label
                        )
                    }
                },
                label = { Text(item.label) }
            )
        }
    }
}

@Composable
fun ToolCard(
    tool: FarmTool,
    onClick: () -> Unit,
    cartViewModel: CartViewModel,
    navController: NavController
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
                       tool.name,
                        tool.price
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
fun TopBarWithCart(
    cartCount: Int,
    onCartClick: () -> Unit,
    innerPaddingValues: PaddingValues
) {
    Surface(tonalElevation = 4.dp) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPaddingValues)
                .background(MaterialTheme.colorScheme.primary)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Farm Supplies",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White
            )

            CartIconWithBadge(
                cartCount = cartCount,
                onCartClick = onCartClick
            )
        }
    }
}

@Composable
fun ScreenTitleWithCart(
    title: String,
    cartCount: Int,
    onNavigateToCart: () -> Unit
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(
            text = title,
            style = MaterialTheme.typography.headlineLarge
        )
        CartIconWithBadge(
            cartCount = cartCount,
            onCartClick = { onNavigateToCart() }
        )
    }
}



















