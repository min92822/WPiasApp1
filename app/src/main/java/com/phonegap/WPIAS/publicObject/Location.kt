package com.phonegap.WPIAS.publicObject

import com.phonegap.WPIAS.dataClass.CityInfo
import com.phonegap.WPIAS.dataClass.DistrictInfo

object Location {

    var cityInfo = ArrayList<CityInfo>()
    var districtInfo = ArrayList<DistrictInfo>()

    fun init(){

        cityInfo = ArrayList()
        districtInfo = ArrayList()

    }

}