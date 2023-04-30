package com.example.my_photo_day.ui.picturemars

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.snackbar.Snackbar
import com.example.my_photo_day.MainActivity
import com.example.my_photo_day.R
import com.example.my_photo_day.databinding.FragmentMarsBinding
import com.example.my_photo_day.domain.repository.nasa.NasaRepositoryImpl
import com.example.my_photo_day.ui.extensions.visible
import com.example.my_photo_day.viewBinding

class MarsFragment : Fragment(R.layout.fragment_mars) {

    companion object {
        const val MAX_LINES = 5
    }

    private val adapter by lazy { MarsAdapter() }

    private val viewBinding: FragmentMarsBinding by viewBinding(
        FragmentMarsBinding::bind
    )

    private val marsViewModel: MarsViewModel by viewModels {
        MarsViewModelFactory(NasaRepositoryImpl)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()

        (activity as MainActivity).findViewById<CollapsingToolbarLayout>(R.id.toolbarLayout)
            ?.apply {
                title = getString(R.string.mars_title)
                val img: AppCompatImageView = findViewById(R.id.toolbar_image)
                img.setImageResource(R.drawable.img_mars)
            }

        (activity as MainActivity).findViewById<AppBarLayout>(R.id.app_layout_bar)?.apply {
            setExpanded(true, true)
        }
    }

    private fun init() {
        val marsRV: RecyclerView = viewBinding.marsList
        marsRV.adapter = adapter

        marsViewModel.errorLiveData.observe(viewLifecycleOwner) {
            val error = it ?: return@observe
            viewBinding.progress.visible { false }

            Snackbar
                .make(
                    viewBinding.root,
                    error,
                    Snackbar.LENGTH_INDEFINITE
                )
                .setAction(getString(R.string.repeat_text)) { marsViewModel.getPhoto() }
                .also {
                    it.view.also {
                        (it.findViewById(com.google.android.material.R.id.snackbar_text) as TextView?)?.maxLines =
                            MAX_LINES
                    }
                }
                .show()
        }

        marsViewModel.loadingLiveData.observe(viewLifecycleOwner) {
            viewBinding.progress.visible { it }
        }

        marsViewModel.marsLiveData.observe(viewLifecycleOwner) {
            with(adapter) {
                clear()
                val marses = it ?: return@observe
                addItems(marses)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            marsViewModel.getPhoto()
        }
    }
}