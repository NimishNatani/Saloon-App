package com.practicecoding.sallonapp.data.model

data class ServiceModel(
    val name: String? = "",
    val price: String = "0",
    val isSelected: Boolean = false,
    val id: Int = 0,
    val serviceTypeHeading:String?=""
)

data class ServiceCat(
    val name: String? = "",
    val services: List<ServiceModel> = emptyList()
)