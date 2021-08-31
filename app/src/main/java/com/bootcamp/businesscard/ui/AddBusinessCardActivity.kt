package com.bootcamp.businesscard.ui

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.bootcamp.businesscard.App
import com.bootcamp.businesscard.R
import com.bootcamp.businesscard.data.model.BusinessCard
import com.bootcamp.businesscard.databinding.ActivityAddBusinessCardBinding
import com.github.danielnilsson9.colorpickerview.dialog.ColorPickerDialogFragment
import com.github.rtoshiro.util.format.SimpleMaskFormatter
import com.github.rtoshiro.util.format.text.MaskTextWatcher
import java.util.*

class AddBusinessCardActivity : AppCompatActivity(), ColorPickerDialogFragment.ColorPickerDialogListener {

    private val binding by lazy{ ActivityAddBusinessCardBinding.inflate(layoutInflater)}
    private val mainViewModel: MainViewModel by viewModels {
        MainViewModelFactory((application as App).repository)
    }
    private lateinit var backgroundColor: String
    private lateinit var imageUriString: String
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        phoneFormat(binding.addPhoneEdt)
        initActions()
    }

    private fun initActions() {
        binding.addCloseBtn.setOnClickListener {
            finish()
        }

        binding.addPhotoGallery.setOnClickListener {
            checkPermissionStorage()
        }

        binding.addPhotoCamera.setOnClickListener {
            checkPermissionCamera()
        }

        binding.addCardColorBtn.setOnClickListener {
            val colorPicker = ColorPickerDialogFragment.newInstance(
                DIALOG_ID, null, null, Color.BLACK, true
            )
            colorPicker.show(fragmentManager, "d")
        }

        binding.addCardBtn.setOnClickListener {
            val businessCard = BusinessCard(
                name = binding.addNameLayout.editText?.text.toString(),
                phone = binding.addPhoneLayout.editText?.text.toString(),
                email = binding.addEmailLayout.editText?.text.toString(),
                company = binding.addCompanyNameLayout.editText?.text.toString(),
                background = backgroundColor,
                profilePic = imageUriString
            )
            mainViewModel.insert(businessCard)
            Toast.makeText(this, getString(R.string.msg_add_card_success), Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun checkPermissionStorage() {
        if(ActivityCompat.checkSelfPermission(
                this, Manifest.permission.READ_EXTERNAL_STORAGE)!=
            PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), IMAGE_PICK_CODE)
        } else {
            pickGalleryImage()
        }
    }

    private fun checkPermissionCamera(){
        if(ActivityCompat.checkSelfPermission(
                this, Manifest.permission.CAMERA)!=
            PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!=
            PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE
                    , Manifest.permission.CAMERA ), OPEN_CAMERA_CODE)
        } else {
            openCamera()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            IMAGE_PICK_CODE ->{
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    pickGalleryImage()
            }

            OPEN_CAMERA_CODE -> {
                if(grantResults.size > 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED)
                    openCamera()
            }
        }
    }

    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "IMG-${Date().time}-bootcamp")
        imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)

        startActivityForResult(cameraIntent, OPEN_CAMERA_CODE)
    }

    private fun pickGalleryImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){
            imageUriString = data?.data.toString()
            binding.addImageView.setImageURI(data?.data)
        }
        if(resultCode == Activity.RESULT_OK && requestCode == OPEN_CAMERA_CODE){
            imageUriString = imageUri.toString()
            binding.addImageView.setImageURI(imageUri)
        }
    }

    override fun onColorSelected(dialogId: Int, color: Int) {
        binding.addCardColorBtn.setBackgroundColor(color)
        backgroundColor = colorToHexString(color)
        Toast.makeText(this, "Cor selecionada: " + colorToHexString(color), Toast.LENGTH_SHORT).show();
    }

    override fun onDialogDismissed(dialogId: Int) {}

    private fun colorToHexString(color: Int): String {
        return String.format("#%06X", -0x1 and color)
    }

    private fun phoneFormat(edt: EditText){
        val smf = SimpleMaskFormatter("(NN) NNNNN-NNNN")
        val mtw = MaskTextWatcher(edt, smf)
        edt.addTextChangedListener(mtw)
    }

    companion object{
        const val IMAGE_PICK_CODE = 1001
        const val OPEN_CAMERA_CODE = 1002
        const val DIALOG_ID = 1003
    }
}