package com.app.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.app.ngn.R

/**
 * Test Fragment
 * Fragment for testing new components
 */
class TestFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fg_test, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*val zxingLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            val result = IntentIntegrator.parseActivityResult(it.resultCode, it.data)

            if(result.contents != null){
                Snackbar.make(requireView(), result.contents, Snackbar.LENGTH_SHORT).show()
            }
        }
        zxingLauncher.launch(IntentIntegrator(requireActivity()).createScanIntent())*/
        val wv = view.findViewById<ImageView>(R.id.gif)
    }
}