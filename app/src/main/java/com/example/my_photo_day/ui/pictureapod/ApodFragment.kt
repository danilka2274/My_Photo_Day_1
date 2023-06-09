package com.example.my_photo_day.ui.pictureapod

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.transition.ChangeBounds
import androidx.transition.ChangeImageTransform
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.example.my_photo_day.MainActivity
import com.example.my_photo_day.R
import com.example.my_photo_day.databinding.FragmentApodBinding
import com.example.my_photo_day.domain.repository.nasa.NasaRepositoryImpl
import com.example.my_photo_day.ui.extensions.visible
import com.example.my_photo_day.viewBinding

class ApodFragment : Fragment(R.layout.fragment_apod_start) {

    companion object {
        const val MAX_LINES = 5
    }

    var isZoomed = false

    private val viewBinding: FragmentApodBinding by viewBinding(
        FragmentApodBinding::bind
    )

    private val apodViewModel: ApodViewModel by viewModels {
        ApodViewModelFactory(NasaRepositoryImpl)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()

        (activity as MainActivity).findViewById<CollapsingToolbarLayout>(R.id.toolbarLayout)
            ?.apply {
                val spannable = SpannableString(getString(R.string.apod_title))
                spannable.setSpan(
                    ForegroundColorSpan(Color.RED),
                    0,
                    4,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannable.setSpan(
                    StyleSpan(Typeface.BOLD),
                    0,
                    4,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannable.setSpan(
                    ForegroundColorSpan(Color.GREEN),
                    5,
                    8,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannable.setSpan(
                    StyleSpan(Typeface.BOLD),
                    5,
                    8,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannable.setSpan(RelativeSizeSpan(1.5f), 5, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                title = spannable
                val img: AppCompatImageView = findViewById(R.id.toolbar_image)
                img.setImageResource(R.drawable.img_picture_day)
            }

        (activity as MainActivity).findViewById<AppBarLayout>(R.id.app_layout_bar)?.apply {
            setExpanded(true, true)
        }

        (activity as MainActivity).findViewById<FloatingActionButton>(R.id.fab)
            ?.apply {
                visible { false }
            }
    }

    private fun init() {
        apodViewModel.errorLiveData.observe(viewLifecycleOwner) {
            val error = it ?: return@observe
            viewBinding.progressLoading.visible { false }

            Snackbar
                .make(
                    viewBinding.root,
                    error,
                    Snackbar.LENGTH_INDEFINITE
                )
                .setAction(getString(R.string.repeat_text)) { apodViewModel.getPhoto() }
                .also {
                    it.view.also {
                        (it.findViewById(com.google.android.material.R.id.snackbar_text) as TextView?)?.maxLines =
                            MAX_LINES
                    }
                }
                .show()
        }

        apodViewModel.loadingLiveData.observe(viewLifecycleOwner) {
            viewBinding.progressLoading.visible { it }
        }

        apodViewModel.apodLiveData.observe(viewLifecycleOwner) {
            it?.title?.let {
                viewBinding.title.text = it
            }

            it?.photoUrl?.let { url ->
                Glide.with(viewBinding.photo)
                    .load(url)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .error(R.drawable.ic_no_image)
                    .into(viewBinding.photo)
            }

            it?.explanation?.let {
                val spannable = SpannableStringBuilder("          $it")
                spannable.forEachIndexed { index, c ->
                    if (c.isDigit()) {
                        spannable.setSpan(
                            ForegroundColorSpan(Color.RED),
                            index,
                            index + 1,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                    }
                    if (c.isUpperCase()) {
                        spannable.setSpan(
                            ForegroundColorSpan(Color.MAGENTA),
                            index,
                            index + 1,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                        spannable.setSpan(
                            RelativeSizeSpan(1.2f),
                            index,
                            index + 1,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                        spannable.setSpan(
                            StyleSpan(Typeface.BOLD),
                            index,
                            index + 1,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                    }
                }
                viewBinding.description.text = spannable
            }

            it?.copyright?.let {
                "${getString(R.string.copyright)} $it".also { viewBinding.copyright.text = it }
            }
        }

        //Анимация увеличения картинки при нажатии на нее
        viewBinding.photo.setOnClickListener {
            isZoomed = !isZoomed
            TransitionManager.beginDelayedTransition(
                viewBinding.root,
                TransitionSet()
                    .addTransition(ChangeBounds())
                    .addTransition(ChangeImageTransform())
            )

            val params: ViewGroup.LayoutParams = viewBinding.photo.layoutParams
            params.height =
                if (isZoomed) ViewGroup.LayoutParams.MATCH_PARENT else ViewGroup.LayoutParams.WRAP_CONTENT
            viewBinding.photo.apply {
                layoutParams = params
                scaleType =
                    if (isZoomed) ImageView.ScaleType.CENTER_CROP else ImageView.ScaleType.FIT_XY
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            apodViewModel.getPhoto()
        }
    }
}