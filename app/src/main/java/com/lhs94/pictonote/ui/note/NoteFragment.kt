package com.lhs94.pictonote.ui.note

import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.lhs94.pictonote.R
import com.lhs94.pictonote.databinding.FragmentNoteBinding
import com.lhs94.pictonote.databinding.ToolbarTitleBinding
import com.lhs94.pictonote.room.entity.Note
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoteFragment: Fragment(R.layout.fragment_note) {
    private val viewModel: NoteViewModel by viewModels()
    private var binding: FragmentNoteBinding? = null
    private lateinit var getContentLauncher: ActivityResultLauncher<String>

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
        getContentLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {

        }
        initializeOptionMenu()
        viewModel.currentNote = if (Build.VERSION.SDK_INT >= 33) {
            arguments?.getSerializable("note", Note::class.java)
        } else {
            arguments?.getSerializable("note") as? Note
        }
        (activity as? AppCompatActivity)?.supportActionBar?.apply {
            title = ""
            setDisplayShowCustomEnabled(true)
            DataBindingUtil.bind<ToolbarTitleBinding>(customView)?.viewModel = viewModel
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as? AppCompatActivity)?.supportActionBar?.apply {
            title = getString(R.string.app_name)
            setDisplayShowCustomEnabled(false)
            DataBindingUtil.getBinding<ToolbarTitleBinding>(customView)?.viewModel = null
        }
        // save note
    }

    override fun onPause() {
        super.onPause()
        viewModel.saveNote()
    }

    private fun initializeOptionMenu() {
        activity?.addMenuProvider(object: MenuProvider{
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.actionbar_note, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when(menuItem.itemId) {
                    /*R.id.menu_edit -> {
                        viewModel.onClickEditOptionMenu()
                        true
                    }
                    R.id.menu_delete -> {
                        viewModel.onClickDeleteOptionMenu()
                        true
                    }*/
                    R.id.menu_gallery -> {
                        getContentLauncher.launch("image/*")
                        true
                    }
                    else -> {
                        // do nothing
                        true
                    }
                }
            }
        }, viewLifecycleOwner)
    }
}