import androidx.annotation.DrawableRes

data class FarmTool(
    val id: String,
    val name: String,
    @DrawableRes val imageRes: Int,
    val price: Double,
    val description: String,
    val category: String
)