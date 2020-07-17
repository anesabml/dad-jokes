package com.anesabml.dadjokes.ui.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.anesabml.dadjokes.R
import com.anesabml.dadjokes.databinding.FragmentHomeBinding
import com.anesabml.dadjokes.extension.viewBinding

class HomeFragment : Fragment(R.layout.fragment_home) {

    private val binding: FragmentHomeBinding by viewBinding(FragmentHomeBinding::bind)
    private val homeViewModel: HomeViewModel by viewModels()

}