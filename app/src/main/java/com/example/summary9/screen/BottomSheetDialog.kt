package com.example.summary9.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.summary9.databinding.FragmentBottomSheetDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetDialog
    : BottomSheetDialogFragment() {

    private var _binding: FragmentBottomSheetDialogBinding? = null
    private val binding get() = _binding!!

    interface OptionSelected {
        fun onOptionSelected(option: String)
    }

    private var listener: OptionSelected? = null

    fun setListener(listener: OptionSelected) {
        this.listener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBottomSheetDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonTakePhoto.setOnClickListener {
            listener?.onOptionSelected("TAKE_PICTURE")
            dismiss()
        }

        binding.buttonChoosePhotoFromGallery.setOnClickListener {
            listener?.onOptionSelected("CHOOSE_GALLERY")
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}