package com.gotti.memoit.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.gotti.memoit.R
import com.gotti.memoit.databinding.FragmentWelcomePageBinding

class WelcomePage : Fragment() {

    private var _binding: FragmentWelcomePageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWelcomePageBinding.inflate(inflater, container, false)
        val view = binding.root
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()


        binding.getStartedButton.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().apply {
                remove(this@WelcomePage).
                commit()
            }

            Toast.makeText(requireActivity(), "done", Toast.LENGTH_LONG).show()
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}