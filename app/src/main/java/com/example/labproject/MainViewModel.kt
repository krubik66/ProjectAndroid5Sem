package com.example.labproject

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val myPreferencesMain: MyPreferencesMain
): ViewModel(){
    val topText = myPreferencesMain.userFlow.map{
        it.username
    }

    val textUnder1 = myPreferencesMain.userFlow.map{
        it.textUnder1
    }

    val textUnder2 = myPreferencesMain.userFlow.map{
        it.textUnder2
    }

    val profilePicture = myPreferencesMain.userFlow.map{
        it.profilePicture
    }

    fun updateTopText(top: String) {
        viewModelScope.launch {
            myPreferencesMain.updateTopText(top)
        }
    }

    fun updateUnderText1(under1: String) {
        viewModelScope.launch {
            myPreferencesMain.updateUnder1(under1)
        }
    }

    fun updateUnderText2(under2: String) {
        viewModelScope.launch {
            myPreferencesMain.updateUnder2(under2)
        }
    }

    fun updateProfilePicture(profilePicture: String) {
        viewModelScope.launch {
            myPreferencesMain.updateProfilePicture(profilePicture)
        }
    }
}