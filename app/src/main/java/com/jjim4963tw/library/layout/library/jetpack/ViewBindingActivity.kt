package com.jjim4963tw.library.layout.library.jetpack

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.jjim4963tw.library.R
import com.jjim4963tw.library.databinding.ActivityViewBindingBinding
import com.jjim4963tw.library.databinding.FragmentViewBindingBinding
import java.lang.RuntimeException

class ViewBindingActivity : AppCompatActivity(R.layout.activity_view_binding) {

    private lateinit var binding: ActivityViewBindingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivityViewBindingBinding.inflate(layoutInflater)
        setContentView(this.binding.root)

        this.binding.tvTest0.text = "Hello World"
    }
}


class ViewBindingFragment : Fragment(R.layout.fragment_view_binding) {

    private var _binding: FragmentViewBindingBinding? = null
    private val binding get() = _binding ?: throw RuntimeException("error")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentViewBindingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}