package com.example.wishlistapp.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.wishlistapp.R
import com.example.wishlistapp.viewmodel.WishesViewModel

@Composable
fun EditWishScreen(
    viewModel: WishesViewModel,
    navHostController: NavHostController
){
    val wishName by viewModel.wishName.collectAsState()
    val wishDescription by viewModel.wishDescription.collectAsState()
    val editingWish by viewModel.editingWish.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(
        editingWish
    ) {
        viewModel.changeWishName(editingWish!!.name)
        viewModel.changeWishDescription(editingWish!!.description)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = colorResource(R.color.white),
        topBar = {
            WishTopBar(editingWish!!.name, viewModel, navHostController)
        },

    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(it).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                maxLines = 3,
                value = wishName,
                onValueChange = {
                    name ->
                    viewModel.changeWishName(name)
                }
            )
            Spacer(Modifier.height(5.dp))
            OutlinedTextField(

                value = wishDescription,
                onValueChange = {
                    description ->
                    viewModel.changeWishDescription(description)
                }
            )
            Button(
                onClick = {
                    if(wishName.isNotBlank()){
                        viewModel.updateWish()
                        navHostController.popBackStack()
                    }
                    else{
                        Toast.makeText(context, "task field cannot be empty for editing", Toast.LENGTH_LONG).show()
                    }


                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.cakeColor),
                    contentColor = colorResource(R.color.cardTextColor)
                )
            ) {
                Text("Update")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WishTopBar(title: String, viewModel: WishesViewModel, navHostController: NavHostController) {
    TopAppBar(
        title = {
            Text(
                text = ("Edit"),
            )
        },
        navigationIcon = {
            IconButton(
                onClick = {
                    navHostController.popBackStack()
                }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    tint = Color.White,
                    contentDescription = ""
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors().copy(
            containerColor = colorResource(R.color.Cyan),
            titleContentColor = Color.White
        )
    )
}
