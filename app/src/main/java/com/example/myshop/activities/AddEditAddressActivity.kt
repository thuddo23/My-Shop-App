package com.example.myshop.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.example.myshop.R
import com.example.myshop.databinding.ActivityAddEditAddressBinding
import com.example.myshop.firestore.FireStoreClass
import com.example.myshop.models.Address
import com.example.myshop.utils.Constant
import kotlinx.android.synthetic.main.activity_add_edit_address.*

class AddEditAddressActivity : BaseActivity() {
    private lateinit var binding: ActivityAddEditAddressBinding
    private var mAddressDetails: Address? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_edit_address)
        fullScreen()
        setUpActionBar()
        if (intent.hasExtra(Constant.EXTRA_ADDRESS_DETAILS)) {
            mAddressDetails = intent.getParcelableExtra(Constant.EXTRA_ADDRESS_DETAILS)
        }
        if (mAddressDetails != null) {
            if (mAddressDetails!!.id.isNotEmpty()) {
                binding.titleToolbarEditAddress.text =
                    resources.getString(R.string.title_edit_address)
                binding.btnSubmitAddAddress.text = resources.getString(R.string.update)

                binding.editFullNameAddress.setText(mAddressDetails!!.name)
                binding.editPhoneAddress.setText(mAddressDetails!!.mobileNumber)
                binding.editAddressAddAddress.setText(mAddressDetails!!.address)
                binding.editZipcodeAddAddress.setText(mAddressDetails!!.zipCode)
                binding.editAdditionalNoteAddAddress.setText(mAddressDetails!!.additionalNote)

                when (mAddressDetails!!.type) {
                    Constant.HOME -> binding.btnHomeAddAddress.isChecked = true
                    Constant.OFFICE -> binding.btnOfficeAddAddress.isChecked = true
                    Constant.OTHER -> {
                        binding.btnHomeAddAddress.isChecked = true
                        binding.editOtherDetailsAddress.visibility = View.VISIBLE
                        binding.editOtherDetailsAddress.setText(mAddressDetails!!.othersDetails)
                    }
                }
            }
        }

        binding.btnSubmitAddAddress.setOnClickListener {
            saveAddressToFireStore()
        }
        binding.radioGrAddAddress.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.btn_other_add_address -> binding.editOtherDetailsAddress.visibility =
                    View.VISIBLE
                else -> binding.editOtherDetailsAddress.visibility = View.GONE
            }
        }
    }

    private fun setUpActionBar() {
        setSupportActionBar(binding.toolbarAddEditAddress)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_white_ios_24)
            actionBar.setDisplayShowTitleEnabled(false)
        }
        binding.toolbarAddEditAddress.setNavigationOnClickListener {
            onBackPressed()
        }


    }

    private fun saveAddressToFireStore() {
        val fullName: String = binding.editFullNameAddress.text.toString().trim()
        val phoneNumber: String = binding.editPhoneAddress.text.toString().trim()
        val address: String = binding.editAddressAddAddress.text.toString().trim()
        val zipCode: String = binding.editZipcodeAddAddress.text.toString().trim()
        val additionalNote: String = binding.editAdditionalNoteAddAddress.text.toString().trim()
        val otherDetails: String = binding.editOtherDetailsAddress.text.toString().trim()
        if (validAddressData()) {
            val addressType: String = when {
                binding.btnHomeAddAddress.isChecked -> Constant.HOME
                binding.btnOfficeAddAddress.isChecked -> Constant.OFFICE
                else -> Constant.OTHER
            }
            val address = Address(
                FireStoreClass().getCurrentUserID(),
                fullName,
                phoneNumber,
                address,
                zipCode,
                additionalNote,
                addressType,
                otherDetails
            )

            if (mAddressDetails != null && mAddressDetails!!.id.isNotEmpty()) {
                FireStoreClass().updateAddress(this, mAddressDetails!!.id, address)
            } else {
                FireStoreClass().addAddress(this, address)
            }
        }
    }

    fun successAddAddress() {
        val notify = if(mAddressDetails!=null && mAddressDetails!!.id.isNotEmpty()){
            resources.getString(R.string.your_update_is_successfully)
        } else {resources.getString(R.string.success_add_address)}
        showErrorSnackBar(notify, false)
        setResult(RESULT_OK)
        finish()
    }

    private fun validAddressData(): Boolean {
        return when {
            binding.editFullNameAddress.text.toString().isEmpty() -> {
                showErrorSnackBar(resources.getString(R.string.please_enter_full_name), true)
                false
            }
            binding.editPhoneAddress.text.toString().isEmpty() -> {
                showErrorSnackBar(
                    resources.getString(R.string.please_enter_phone_number),
                    true
                )
                false
            }
            binding.editAddressAddAddress.text.toString().isEmpty() -> {
                showErrorSnackBar(
                    resources.getString(R.string.please_enter_address),
                    true
                )
                false
            }
            binding.editZipcodeAddAddress.text.toString().isEmpty() -> {
                showErrorSnackBar(
                    resources.getString(R.string.please_enter_zip_code),
                    true
                )
                false
            }
            binding.btnOtherAddAddress.isChecked && binding.editOtherDetailsAddress.toString()
                .isEmpty() -> {
                showErrorSnackBar(
                    resources.getString(R.string.please_enter_other_details),
                    true
                )
                false
            }
            else -> true

        }
    }
}