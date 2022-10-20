package com.lhs94.pictonote.ui.main

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.lhs94.pictonote.R
import com.lhs94.pictonote.databinding.FragmentMainBinding

class MainFragment: Fragment(R.layout.fragment_main) {
    private val viewModel: MainViewModel by viewModels()
    private var binding: FragmentMainBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding?.root ?: super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(binding == null) {
            binding = DataBindingUtil.bind(view)
            binding?.viewModel = viewModel
        }
        initializeOptionMenu()
    }

    private fun initializeOptionMenu() {
        activity?.addMenuProvider(object: MenuProvider{
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.actionbar_main, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when(menuItem.itemId) {
                    R.id.menu_add -> {
                        findNavController().navigate(R.id.noteFragment)
                        true
                    }
                    R.id.menu_info -> {
                        true
                    }
                    else -> {
                        true
                    }
                }
            }
        }, viewLifecycleOwner)
    }
}