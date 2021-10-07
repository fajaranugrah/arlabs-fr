package com.example.baseproject.presenter.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.baseproject.databinding.DialogFragmentProgressBinding

/**
 * A simple [Fragment] subclass.
 * Use the [ProgressDialogFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProgressDialogFragment : DialogFragment() {

    private var _binding: DialogFragmentProgressBinding? = null
    private val binding get() = _binding!!

    private var mFragmentManager: FragmentManager? = null
    private var isShowing = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = DialogFragmentProgressBinding.inflate(inflater, container, false)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        isCancelable = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun show(fragmentManager: FragmentManager) {
        this.mFragmentManager = fragmentManager

        if (!(this.dialog?.isShowing == true && !this.isRemoving)) {
            if (!isShowing) {
                show(fragmentManager, TAG)
                isShowing = true
            }
        }
    }

    fun hide() {
        if (mFragmentManager?.findFragmentByTag(TAG) != null && this.dialog?.isShowing == true) {
            dismiss()
            isShowing = false
        }
    }

    companion object {
        const val TAG = "progress_dialog"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment ProgressDialogFragment.
         */
        @JvmStatic
        fun newInstance() = ProgressDialogFragment()
    }
}