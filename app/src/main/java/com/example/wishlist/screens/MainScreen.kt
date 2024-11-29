package com.example.wishlistapp.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.wishlistapp.R
import com.example.wishlistapp.items.WishItem
import com.example.wishlistapp.model.WishModel
import com.example.wishlistapp.utils.Screens
import com.example.wishlistapp.viewmodel.WishesViewModel

@Composable
fun MainScreen(
    wishViewModel: WishesViewModel,
    navigationController: NavHostController
){
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = colorResource(R.color.white),
        topBar = {
            MainTopBar(
                onAddClick = {
                    wishViewModel.changeAddingVision()
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                containerColor = colorResource(R.color.cakeColor),
                shape = RoundedCornerShape(20),

                onClick = {
                    navigationController.navigate(Screens.newScreen)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    tint = Color.White,
                    contentDescription = ""
                )
            }
        }
    ) {
        MainContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            wishViewModel = wishViewModel,
            navigationController
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopBar(
    onAddClick: () -> Unit
) {
    var isOpen by remember {
        mutableStateOf(false)
    }
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "TASK LIST",
                textAlign = TextAlign.Center,
                fontSize = 18.sp
            )
        },
        actions = {
            IconButton(
                onClick = {
                    isOpen = !isOpen
                }
            ) {
               Column {
                   Icon(
                       imageVector = Icons.Default.MoreVert,
                       tint = Color.White,
                       contentDescription = ""
                   )
                   DropdownMenu(
                       expanded = isOpen,
                       onDismissRequest = {
                           isOpen = false
                       }
                   ) {
                       DropdownMenuItem(
                           text = {
                               Text("Task list")
                           },
                           onClick = {
                               isOpen = false
                               onAddClick()
                           }
                       )
                   }

               }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors().copy(
            containerColor = colorResource(R.color.Cyan),
            titleContentColor = Color.White
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContent(modifier: Modifier,
                wishViewModel: WishesViewModel,
                navigationController: NavHostController
) {

    val isAdding by wishViewModel.isAddingWish.collectAsState()
    val listOfWishes by wishViewModel.listOfWishes.collectAsState()

    LaunchedEffect(
        listOfWishes.size
    ) {
        wishViewModel.getAllWishes()
    }

    if (isAdding){
        AddingDialog(wishViewModel)
    }
    LazyColumn(
        modifier = modifier
    ) {
        items(listOfWishes, key = {wish -> wish.id}){
            wish ->
            val dismissState = rememberSwipeToDismissBoxState(
                confirmValueChange = {
                    if (it == SwipeToDismissBoxValue.StartToEnd){
                        wishViewModel.deleteWish(wish)
                        true
                    }

                    else{
                        false
                    }


                }
            )
            SwipeToDismissBox(
                state = dismissState,
                backgroundContent = {}
            ) {
                WishItem(wish, navigationController, wishViewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddingDialog(
    viewModel: WishesViewModel
) {
    val wishDescription by viewModel.wishDescription.collectAsState()
    val wishName by viewModel.wishName.collectAsState()

    val context = LocalContext.current

    BasicAlertDialog(
        onDismissRequest = {
            viewModel.changeAddingVision()
        }
    ) {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .background(
                    brush = Brush.linearGradient(
                        listOf(
                            colorResource(R.color.cakeColor),
                            colorResource(R.color.secondCardColor)
                        )
                    ),
                    shape = CardDefaults.shape
                )
                .padding(21.dp)
        )
        {
            TextField(
                maxLines = 3,
                value = wishName,
                onValueChange = {
                    viewModel.changeWishName(it)
                },
                placeholder = {
                    Text(
                        text = "title",
                        color = Color.Gray.copy(alpha = 1.0f)
                    )
                }
            )
            Spacer(Modifier.height(10.dp))
            TextField(
                value = wishDescription,
                onValueChange = {
                    viewModel.changeWishDescription(it)
                },
                placeholder = {
                    Text(
                        text = "description",
                        color = Color.Gray.copy(alpha = 1.0f)
                    )
                }
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Button(
                    onClick = {
                        viewModel.changeAddingVision()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.cakeColor),
                        contentColor = colorResource(R.color.cardTextColor)
                    )
                ) {
                    Text("Cancel")
                }


                Button(
                    onClick = {
                        if(wishName.isNotBlank()){
                            viewModel.addWish(
                                WishModel(
                                    id = 0L,
                                    name = wishName,
                                    description = wishDescription
                                )
                            )
                            viewModel.changeAddingVision()
                        }
                        else{
                            Toast.makeText(context, "task field cannot be empty", Toast.LENGTH_LONG).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.cakeColor),
                        contentColor = colorResource(R.color.cardTextColor)
                    )
                ) {
                    Text("Add")
                }
            }
        }
    }
}

@Preview
@Composable
fun Preview(){
    AddingDialog(viewModel())
}
