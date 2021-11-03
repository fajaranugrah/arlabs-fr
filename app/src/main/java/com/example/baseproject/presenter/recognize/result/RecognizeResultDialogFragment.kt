package com.example.baseproject.presenter.recognize.result

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import coil.load
import com.example.baseproject.R
import com.example.baseproject.databinding.DialogFragmentRecognizeResultBinding
import com.example.baseproject.domain.model.CheckIn
import com.example.baseproject.domain.model.CheckOut
import com.example.baseproject.domain.model.Thermal
import com.example.baseproject.util.AppEnvironment
import com.example.baseproject.util.ext.setTextColorRes


/**
 * A simple [Fragment] subclass.
 * Use the [RecognizeResultDialogFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RecognizeResultDialogFragment : DialogFragment() {

    var onDismiss: (() -> Unit)? = null

    private var _binding: DialogFragmentRecognizeResultBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(STYLE_NORMAL, R.style.AppTheme_NoActionBar_FullScreenDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = DialogFragmentRecognizeResultBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {
        arguments?.getParcelable<Thermal>(ARG_THERMAL)?.let {
            binding.tvTemperature.text = it.temperature
            binding.tvTemperature.setTextColorRes(if (it.isUnusual == true) R.color.colorRed else R.color.colorGreen)
        }

        arguments?.getString(ARG_TYPE)?.let {
            when (it) {
                AppEnvironment.VerifyTypes.CHECKIN -> showCheckInData()
                AppEnvironment.VerifyTypes.CHECKOUT -> showCheckoutData()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showCheckInData() {
        //binding.tvPoweredBy.setTextColorRes(android.R.color.white)
        binding.tvSupportedBy.setTextColorRes(android.R.color.white)
        binding.tvRisetAiAndDigitalBuana.setTextColorRes(android.R.color.white)
        binding.imgLogoPeduliLingdungi.load(AppEnvironment.Logo.PEDULI_LINDUNGI_WHITE) {
            placeholder(R.drawable.logo_peduli_lindungi_white)
        }
        /*binding.imgLogoRisetAi.load(AppEnvironment.Logo.RISET_AI_WHITE) {
            placeholder(R.drawable.logo_riset_ai_white)
        }
        binding.imgLogoDigitalBuana.load(AppEnvironment.Logo.DIGITAL_BUANA_WHITE) {
            placeholder(R.drawable.logo_digital_buana_white)
        }
        binding.imgLogoSentuh.load(AppEnvironment.Logo.SENTUH_WHITE) {
            placeholder(R.drawable.logo_sentuh_colored)
        }*/
        /*binding.imgLogoAsriLiving.load(AppEnvironment.Logo.ASRI_LIVING) {
            placeholder(R.drawable.logo_asri_living)
        }
        binding.imgLogoAgungSedayuGroup.load(AppEnvironment.Logo.AGUNG_SEDAYU) {
            placeholder(R.drawable.logo_agung_sedayu_group)
        }*/
        binding.imgLogoXplorin.load(AppEnvironment.Logo.XPLORIN_WHITE) {
            placeholder(R.drawable.logo_xplorin_white)
        }
        binding.imgLogoSbkmotul.load(AppEnvironment.Logo.SBKMOTUL) {
            placeholder(R.drawable.logo_sbkmotul)
        }
        binding.imgLogoItdc.load(AppEnvironment.Logo.ITDC_WHITE) {
            placeholder(R.drawable.logo_itdc)
        }

        binding.tvHintCheckLocation.text = getString(R.string.title_check_in_location)
        binding.tvHintCheckDate.text = getString(R.string.title_check_in_date)

        binding.tvColorArea.text = "Red Area"
        binding.tvNameArea.text = "(Premier Grand Stand)"

        arguments?.getParcelable<CheckIn>(ARG_CHECKIN)?.let {
            binding.tvFullName.text = it.user?.name
            binding.tvNik.text = "${it.user?.id?.take(11)}*****"
            //binding.tvCheckInLocation.text = it.place?.name
            binding.tvCheckInLocation.text = "Mandalika - Sircuit"
            binding.tvCheckDate.text = it.date
            binding.tvCheckTime.text = it.time
            binding.tvCrowd.text = it.place?.crowd.toString()
            binding.tvCapacity.text = "/${it.place?.maxCapacity.toString()}"

            showStatusData(it.user?.status)
        }

        /*Handler().postDelayed({
            onDismiss?.invoke()
            dismiss()
        }, 3000)*/
        Handler().postDelayed({
            onDismiss?.invoke()
            dismiss()
        }, 7000)
    }

    @SuppressLint("SetTextI18n")
    private fun showCheckoutData() {
        //binding.tvPoweredBy.setTextColorRes(R.color.colorPrimaryText)
        binding.tvSupportedBy.setTextColorRes(R.color.colorPrimaryText)
        binding.tvRisetAiAndDigitalBuana.setTextColorRes(R.color.colorPrimaryText)
        binding.imgLogoPeduliLingdungi.load(AppEnvironment.Logo.PEDULI_LINDUNGI_COLORED) {
            placeholder(R.drawable.logo_peduli_lindungi_colored)
        }
        /*binding.imgLogoRisetAi.load(AppEnvironment.Logo.RISET_AI_COLORED) {
            placeholder(R.drawable.logo_riset_ai_colored)
        }
        binding.imgLogoDigitalBuana.load(AppEnvironment.Logo.DIGITAL_BUANA_COLORED) {
            placeholder(R.drawable.logo_digital_buana_colored)
        }
        binding.imgLogoSentuh.load(AppEnvironment.Logo.SENTUH_COLORED) {
            placeholder(R.drawable.logo_sentuh_colored)
        }*/
        /*binding.imgLogoAsriLiving.load(AppEnvironment.Logo.ASRI_LIVING) {
            placeholder(R.drawable.logo_asri_living)
        }
        binding.imgLogoAgungSedayuGroup.load(AppEnvironment.Logo.AGUNG_SEDAYU) {
            placeholder(R.drawable.logo_agung_sedayu_group)
        }*/
        binding.imgLogoXplorin.load(AppEnvironment.Logo.XPLORIN_COLORED) {
            placeholder(R.drawable.logo_xplorin_white)
        }
        binding.imgLogoSbkmotul.load(AppEnvironment.Logo.SBKMOTUL) {
            placeholder(R.drawable.logo_sbkmotul)
        }
        binding.imgLogoItdc.load(AppEnvironment.Logo.ITDC_COLORED) {
            placeholder(R.drawable.logo_itdc_color)
        }

        binding.tvHintCheckLocation.text = getString(R.string.title_check_out_location)
        binding.tvHintCheckDate.text = getString(R.string.title_check_out_date)

        arguments?.getParcelable<CheckOut>(ARG_CHECKOUT)?.let {
            binding.tvFullName.text = it.user?.name
            binding.tvNik.text = "${it.user?.id?.take(11)}*****"
            //binding.tvCheckInLocation.text = it.place?.name
            binding.tvCheckInLocation.text = "Mandalika - Sircuit"
            binding.tvCheckDate.text = it.date
            binding.tvCheckTime.text = it.time
            binding.tvCrowd.text = it.place?.crowd.toString()
            binding.tvCapacity.text = "/${it.place?.maxCapacity.toString()}"

            binding.root.setBackgroundResource(R.drawable.bg_result_clean)
            binding.imgCheck.setImageResource(R.drawable.ic_check_green)
            binding.tvStatus.apply {
                setText(R.string.message_checkout_success)
                setTextColorRes(R.color.colorGreen)
            }
        }

        /*Handler().postDelayed({
            onDismiss?.invoke()
            dismiss()
        }, 3000)*/
        Handler().postDelayed({
            onDismiss?.invoke()
            dismiss()
        }, 7000)
    }

    private fun showStatusData(status: String?) {
        when (status) {
            "green" -> {
                binding.root.setBackgroundResource(R.drawable.bg_result_green)
                binding.imgCheck.setImageResource(R.drawable.ic_check_green)
                binding.tvStatus.apply {
                    setText(R.string.message_checkin_success)
                    setTextColorRes(R.color.colorGreen)
                }
            }
            "yellow" -> {
                binding.root.setBackgroundResource(R.drawable.bg_result_yellow)
                binding.imgCheck.setImageResource(R.drawable.ic_check_yellow)
                binding.tvStatus.apply {
                    setText(R.string.message_checkin_success)
                    setTextColorRes(R.color.colorYellow)
                }
            }
            "red" -> {
                binding.root.setBackgroundResource(R.drawable.bg_result_red)
                binding.imgCheck.setImageResource(R.drawable.ic_uncheck_red)
                binding.tvStatus.apply {
                    setText(R.string.message_checkin_rejected)
                    setTextColorRes(R.color.colorRed)
                }
            }
            "black" -> {
                binding.root.setBackgroundResource(R.drawable.bg_result_black)
                binding.imgCheck.setImageResource(R.drawable.ic_uncheck_black)
                binding.tvStatus.apply {
                    setText(R.string.message_checkin_rejected)
                    setTextColorRes(android.R.color.black)
                }
            }
        }
    }

    companion object {
        const val TAG = "recognize_result"
        const val ARG_TYPE = "type"
        const val ARG_CHECKIN = "checkin"
        const val ARG_CHECKOUT = "checkout"
        const val ARG_THERMAL = "thermal"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment RecognizeResultFragment.
         */
        @JvmStatic
        fun newInstance(
            type: String,
            checkIn: CheckIn? = null,
            checkOut: CheckOut? = null,
            thermal: Thermal? = null,
        ) = RecognizeResultDialogFragment().apply {
            arguments = bundleOf().apply {
                putString(ARG_TYPE, type)
                putParcelable(ARG_CHECKIN, checkIn)
                putParcelable(ARG_CHECKOUT, checkOut)
                putParcelable(ARG_THERMAL, thermal)
            }
        }
    }
}