package com.padc.grocery.network

import android.graphics.Bitmap
import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.padc.grocery.data.vos.GroceryVO
import java.io.ByteArrayOutputStream
import java.util.*

object CloudFirestoreFirebaseApiImpl : FirebaseApi {

    val db = Firebase.firestore
    private val storage = FirebaseStorage.getInstance()
    private val storageRef = storage.reference

    override fun getGroceries(
        onSuccess: (groceries: List<GroceryVO>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        // one time data
/*        db.collection("groceries")
            .get()
            .addOnSuccessListener { result ->
                val groceriesList : MutableList<GroceryVO> = arrayListOf()
                for(document in result) {
                    val data = document.data
                    var grocery = GroceryVO()
                    grocery.name = data["name"] as String
                    grocery.description = data["description"] as String
                    grocery.amount = (data["amount"] as Long).toInt()
                    groceriesList.add(grocery)
                }
                onSuccess(groceriesList)
            }
            .addOnFailureListener{exception ->
                onFailure(exception.message?:"Please check connection")
            }*/
        // real time syncing data
        db.collection("groceries")
            .addSnapshotListener{value,error ->
                error?.let {
                    onFailure(it.message?:"Please check connection")
                }?: run {
                    val groceriesList : MutableList<GroceryVO> = arrayListOf()
                    val result = value?.documents ?: arrayListOf()
                    for(document in result) {
                        val data = document.data
                        val grocery = GroceryVO()
                        grocery.name = data?.get("name") as String
                        grocery.description = data["description"] as String
                        grocery.amount = (data["amount"] as Long).toInt()
                        grocery.image = data["image"] as String?
                        groceriesList.add(grocery)
                    }
                    onSuccess(groceriesList)
                }
            }
    }


    override fun addGrocery(name: String, description: String, amount: Int,image:String) {
        val groceryMap = hashMapOf(
            "name" to name,
            "description" to description,
            "amount" to amount.toLong(),
            "image" to image
        )
        db.collection("groceries")
            .document(name)
            .set(groceryMap)
            .addOnSuccessListener { Log.d("Success","Successfully added grocery.") }
            .addOnFailureListener { Log.d("Failure","Failed to add grocery.") }
    }

    override fun deleteGrocery(name: String) {
        db.collection("groceries")
            .document(name)
            .delete()
            .addOnSuccessListener { Log.d("Success","Successfully deleted grocery") }
            .addOnFailureListener { Log.d("Failure","Failed to delete grocery") }
    }

    override fun uploadImageAndEditGrocery(image: Bitmap, grocery: GroceryVO) {
        val baos = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG,100,baos)
        val data = baos.toByteArray()

        val imageRef = storageRef.child("images/${UUID.randomUUID()}")
        val uploadTask = imageRef.putBytes(data)
        uploadTask.addOnFailureListener{

        }.addOnSuccessListener { taskSnapshot ->  }

        val urlTask = uploadTask.continueWithTask{
            return@continueWithTask imageRef.downloadUrl
        }.addOnCompleteListener { task->
            val imageUrl = task.result?.toString()
            addGrocery(
                grocery.name?:"",
                grocery.description?:"",
                grocery.amount?:0,
                imageUrl?:""
            )
        }
    }

    override fun addGroceryWithImage(
        name: String,
        description: String,
        amount: Int,
        image: Bitmap
    ) {
        val baos = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG,100,baos)
        val data = baos.toByteArray()

        val imageRef = storageRef.child("images/${UUID.randomUUID()}")
        val uploadTask = imageRef.putBytes(data)
        uploadTask.addOnFailureListener{

        }.addOnSuccessListener { taskSnapshot ->  }

        val urlTask = uploadTask.continueWithTask{
            return@continueWithTask imageRef.downloadUrl
        }.addOnCompleteListener { task->
            val imageUrl = task.result?.toString()
            addGrocery(
                name?:"",
                description?:"",
                amount?:0,
                imageUrl?:""
            )
        }
    }
}