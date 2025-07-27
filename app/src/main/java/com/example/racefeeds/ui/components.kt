package com.example.racefeeds.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction

import androidx.compose.ui.unit.dp
import com.example.racefeeds.data.Animal
import com.example.racefeeds.data.Breed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearchTriggered: () -> Unit = {},
    placeholder: String = "Search...",
    modifier: Modifier = Modifier
){
    TextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = { Text(placeholder) },
        modifier = modifier.fillMaxWidth().padding(horizontal = 12.dp),
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
    modifier: Modifier = Modifier
){
    Row(
        modifier = modifier.fillMaxWidth().padding(12.dp),
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
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun BreedBottomSheet(
//    animal: Animal,
//    onBreedSelected: (Breed) -> Unit,
//    onDismiss: () -> Unit,
//    modifier: Modifier = Modifier
//){
//    ModalBottomSheet(
//        onDismissRequest = onDismiss,
//        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
//    ){
//        Column(modifier = Modifier.padding(8.dp)){
//            Text(
//                text = "Select a breed for ${animal.name}",
//                style = MaterialTheme.typography.headlineSmall
//            )
//            Spacer(modifier = Modifier.height(8.dp))
//
//            animal.breeds.forEach { breed ->
//                Text(
//                    text = breed.name,
//                    style = MaterialTheme.typography.bodyLarge,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .clickable { onBreedSelected(breed) }
//                        .padding(vertical = 12.dp)
//                )
//                Divider()
//            }
//            if (animal.breeds.isEmpty()){
//                Text("General Food Info:")
//                animal.generalFoodInfo.forEach { info ->
//                    Text("- $info")
//                }
//            }
//            Spacer(modifier = Modifier.height(8.dp))
//
//            TextButton(onClick = onDismiss){
//                Text("Close")
//            }
//        }
//    }
//}



























