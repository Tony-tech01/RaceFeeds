import com.example.racefeeds.ui.screens.Cart.CartItem
import com.google.firebase.Timestamp

data class Order(
    val orderId: String = "",
    val userId: String = "",
    val userEmail: String = "",
    val items: List<CartItem> = emptyList(),
    val status: OrderStatus = OrderStatus.PENDING,
    val date: Timestamp? = null
,
    val trackingInfo: TrackingInfo? = null
)

enum class OrderStatus {
    PENDING, COMPLETED, CANCELLED
}

data class TrackingInfo(
    val currentLocation: String = "",
    val estimatedDelivery: Timestamp? = null,
    val progress: Float = 0f
)