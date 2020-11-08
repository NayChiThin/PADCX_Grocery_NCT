package com.padc.grocery.network

import android.graphics.Bitmap
import com.padc.grocery.data.vos.GroceryVO

interface FirebaseApi {
    fun getGroceries(onSuccess:(groceries:List<GroceryVO>)->Unit,onFailure:(String)->Unit)
    fun addGrocery(name:String,description:String,amount:Int,image:String)
    fun deleteGrocery(name:String)
    fun uploadImageAndEditGrocery(image:Bitmap,grocery:GroceryVO)
    fun addGroceryWithImage(name: String,description: String,amount: Int,image:Bitmap)
}