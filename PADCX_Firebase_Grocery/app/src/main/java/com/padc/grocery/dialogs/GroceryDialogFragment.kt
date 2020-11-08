package com.padc.grocery.dialogs

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.padc.grocery.R
import com.padc.grocery.activities.MainActivity
import com.padc.grocery.mvp.presenters.MainPresenter
import com.padc.grocery.mvp.presenters.impls.MainPresenterImpl
import kotlinx.android.synthetic.main.dialog_add_grocery.*
import kotlinx.android.synthetic.main.dialog_add_grocery.view.*
import java.io.IOException

class GroceryDialogFragment : DialogFragment() {

    companion object {
        const val BUNDLE_NAME = "BUNDLE_NAME"
        const val BUNDLE_DESCRIPTION = "BUNDLE_DESCRIPTION"
        const val BUNDLE_AMOUNT = "BUNDLE_AMOUNT"
        const val TAG_ADD_GROCERY_DIALOG = "TAG_ADD_GROCERY_DIALOG"
        const val BUNDLE_IMAGE = "BUNDLE_IMAGE"
        const val PICK_IMAGE_REQUEST = 1111

        fun newFragment(): GroceryDialogFragment {
            return GroceryDialogFragment()
        }
    }

    private lateinit var mPresenter : MainPresenter
    private var bitmap : Bitmap? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_add_grocery, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpPresenter()

        view.etGroceryName?.setText(arguments?.getString(BUNDLE_NAME))
        view.etDescription?.setText(arguments?.getString(BUNDLE_DESCRIPTION))
        view.etAmount?.setText(arguments?.getString(BUNDLE_AMOUNT))

        Glide.with(view.context)
            .load(arguments?.getString(BUNDLE_IMAGE))
            .into(view.ivGroceryImage)
        bitmap = ivGroceryImage.drawable.toBitmap()

        view.ivGroceryImage.setOnClickListener {
            openGallery()
        }

        view.btnAddGrocery.setOnClickListener {
            if (bitmap==null) {
                mPresenter.onTapAddGrocery(
                    etGroceryName.text.toString(),
                    etDescription.text.toString(),
                    etAmount.text.toString().toInt()
                )
            }else {
                mPresenter.onTapAddGroceryWithImage(
                    etGroceryName.text.toString(),
                    etDescription.text.toString(),
                    etAmount.text.toString().toInt(),
                    bitmap!!
                )
            }
            dismiss()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode== PICK_IMAGE_REQUEST && resultCode== Activity.RESULT_OK) {
            if(data==null || data.data==null) {
                return
            }
            val filePath = data.data
            try {
                filePath?.let {
                    if (Build.VERSION.SDK_INT >= 29) {
                        val source : ImageDecoder.Source = ImageDecoder.createSource(context?.contentResolver!!,filePath)
                        bitmap = ImageDecoder.decodeBitmap(source)
                        view?.context?.let { it1 ->
                            Glide.with(it1)
                                .load(it)
                                .into(ivGroceryImage)
                        }

                    }
                    else {
                        bitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver,filePath)
                        view?.context?.let { it1 ->
                            Glide.with(it1)
                                .load(it)
                                .into(ivGroceryImage)
                        }
                    }
                }
            }catch (e : IOException) {
                e.printStackTrace()
            }
        }
    }
    private fun setUpPresenter() {
        activity?.let {
            mPresenter = ViewModelProviders.of(it).get(MainPresenterImpl::class.java)
        }
    }
    private fun openGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(intent,"Select Chooser"),
            PICK_IMAGE_REQUEST
        )
    }

}