@file:Suppress("DEPRECATION")

package com.example.my_photo_day.ui.settings

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.my_photo_day.MainActivity
import com.example.my_photo_day.R
import com.example.my_photo_day.databinding.FragmentSettingsBinding
import com.example.my_photo_day.domain.storage.AppThemeStorage
import com.example.my_photo_day.ui.extensions.visible
import com.example.my_photo_day.viewBinding
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton

class SettingsFragment : Fragment(R.layout.fragment_settings), View.OnClickListener {

    val THEME_GRAY = 0
    val THEME_ORANGE = 1

    private val viewBinding: FragmentSettingsBinding by viewBinding(
        FragmentSettingsBinding::bind
    )

    private val settingsViewModel: SettingsViewModel by viewModels {
        SettingsViewModelFactory(
            AppThemeStorage(requireActivity().application)
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()

        (activity as MainActivity).findViewById<CollapsingToolbarLayout>(R.id.toolbarLayout)
            ?.apply {
                title = getString(R.string.settings_title)
                val img: AppCompatImageView = findViewById(R.id.toolbar_image)
                img.setImageResource(R.color.transparent)
            }

        (activity as MainActivity).findViewById<FloatingActionButton>(R.id.fab)
            ?.apply {
                visible { false }
            }

        (activity as MainActivity).findViewById<AppBarLayout>(R.id.app_layout_bar)?.apply {
            setExpanded(false, true)
        }
    }

    private fun init() {
        //Навешали ClickListener на RadioButton-ы
        viewBinding.optionGreyTheme.setOnClickListener(this)
        viewBinding.optionOrangeTheme.setOnClickListener(this)

        /**
         * Подписались на изменение ID темы, для установки Checked
         * сохраненной темы при создании вьюхи
         */
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            settingsViewModel.theme.collect {
                when (it) {
                    THEME_GRAY -> {
                        viewBinding.optionGreyTheme.isChecked = true
                    }
                    THEME_ORANGE -> {
                        viewBinding.optionOrangeTheme.isChecked = true
                    }
                }
            }
        }
    }

    /**
     * Обработка клика по RadioButton-у для установки
     * выбранной темы
     */
    override fun onClick(v: View?) {
        v?.let {
            when (it.id) {
                R.id.option_grey_theme -> {
                    settingsViewModel.setTheme(THEME_GRAY)
                    requireActivity().recreate()
                }
                R.id.option_orange_theme -> {
                    settingsViewModel.setTheme(THEME_ORANGE)
                    requireActivity().recreate()
                }
            }
        }
    }
}