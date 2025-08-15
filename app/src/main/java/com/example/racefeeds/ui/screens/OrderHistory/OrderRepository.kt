package com.example.racefeeds.data.repository

import Order
import OrderStatus
import TrackingInfo
import android.util.Log
import com.example.racefeeds.ui.screens.Cart.CartItem
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class OrderRepository(private val firestore: FirebaseFirestore) {

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders

    fun fetchOrdersForUser(userId: String) {
        firestore.collection("orders").whereEqualTo("userId", userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) {
                    Log.e("OrderRepository", "Error fetching user orders", error)
                    _orders.value = emptyList()
                    return@addSnapshotListener
                }

                val orders = snapshot.documents.mapNotNull { doc -> parseOrderDocument(doc) }
                _orders.value = orders
            }
    }

    fun loadAllOrdersForAdmin() {
        firestore.collection("orders").addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) {
                    Log.e("OrderRepository", "Error fetching all orders", error)
                    _orders.value = emptyList()
                    return@addSnapshotListener
                }

                val orders = snapshot.documents.mapNotNull { doc -> parseOrderDocument(doc) }
                _orders.value = orders
            }
    }

    fun addOrder(order: Order) {
        firestore.collection("orders").document(order.orderId).set(order).addOnSuccessListener {
                Log.d("OrderRepository", "Order added: ${order.orderId}")
            }.addOnFailureListener { e ->
                Log.e("OrderRepository", "Failed to add order", e)
            }
    }

    fun updateOrderStatus(orderId: String, newStatus: OrderStatus) {
        firestore.collection("orders").document(orderId).update("status", newStatus.name)
    }

    fun updateTracking(orderId: String, trackingInfo: TrackingInfo) {
        firestore.collection("orders").document(orderId).update("trackingInfo", trackingInfo)
    }

    private fun parseOrderDocument(doc: DocumentSnapshot): Order? {
        return try {
            val date = doc.getTimestamp("date") ?: return null
            val items = doc.get("items") as? List<Map<String, Any>> ?: emptyList()
            val cartItems = items.mapNotNull { CartItem.fromMap(it) }

            Order(
                orderId = doc.id,
                userId = doc.getString("userId") ?: "",
                userEmail = doc.getString("userEmail") ?: "",
                items = cartItems,
                status = OrderStatus.valueOf(doc.getString("status") ?: "PENDING"),
                date = date,
                trackingInfo = doc.toObject(TrackingInfo::class.java)
            )
        } catch (e: Exception) {
            Log.e("OrderRepository", "Failed to parse order ${doc.id}", e)
            null
        }
    }
}