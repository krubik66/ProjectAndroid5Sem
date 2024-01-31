package com.example.labproject

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState

@OptIn(ExperimentalPagerApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Swipe(navController: NavController, mainViewModel: MainViewModel){
    var top by remember { mutableStateOf("") }
    var under1 by remember { mutableStateOf("") }
    var under2 by remember { mutableStateOf("") }
    val photos = listOf(
        R.drawable.orcicon,
        R.drawable.playericon,
        R.drawable.skeletonicon
    )
    val pagerState = rememberPagerState()

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
        ) {
            Box(modifier = Modifier.size(250.dp, 250.dp)){
                HorizontalPager(
                    count = photos.size,
                    state = pagerState,
                    key = { photos[it] }
                ) { page ->
                    Image(
                        painter = painterResource(id = photos[page]),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(220.dp, 220.dp)
                    )

                }
            }
            Text(text = "Ustaw tekst nad zdjęciem")
            TextField(value = top, onValueChange = {
                top = it
            })
            Text(text = "Ustaw pierwszy tekst poniżej")
            TextField(value = under1, onValueChange = {
                under1 = it
            })
            Text(text = "Ustaw drugi tekst poniżej")
            TextField(value = under2, onValueChange = {
                under2 = it
            })
            Button(onClick = {
                mainViewModel.updateTopText(top)
                mainViewModel.updateUnderText1(under1)
                mainViewModel.updateUnderText2(under2)
                mainViewModel.updateProfilePicture(pagerState.currentPage.toString())
            }) {
                Text(text = "Zapisz")
            }
        }
    }
}