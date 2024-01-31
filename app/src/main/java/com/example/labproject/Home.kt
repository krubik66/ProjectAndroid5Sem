package com.example.labproject


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun Home(navController: NavController, mainViewModel: MainViewModel){
    val top by mainViewModel.topText.collectAsState(initial = "I am on top!")
    val under1 by mainViewModel.textUnder1.collectAsState(initial = "I am Under1")
    val under2 by mainViewModel.textUnder2.collectAsState(initial = "I am Under2")
    val profilePicture by mainViewModel.profilePicture.collectAsState(initial = "1")

    Column(
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()

    ){
        Text(text = top)
        Spacer(modifier = Modifier.height(10.dp))
        Image(
            painter = when (profilePicture.toInt() + 1) {
                1 -> painterResource(id = R.drawable.orcicon)
                2 -> painterResource(id = R.drawable.playericon)
                3 -> painterResource(id = R.drawable.skeletonicon)
                else -> painterResource(id = R.drawable.orcicon)
            },
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .height(200.dp)
                .width(200.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = under1)
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = under2)
        }

}
