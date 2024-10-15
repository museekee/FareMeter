package kr.museekee.faremeter.transportationCalc

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf

object UnknownCalc: CommonCalc {
    override val fare: MutableState<Int> = mutableIntStateOf(0)
    override val counter: MutableState<Int> = mutableIntStateOf(0)
    override val distance: MutableState<Double> = mutableDoubleStateOf(0.0) // m
}