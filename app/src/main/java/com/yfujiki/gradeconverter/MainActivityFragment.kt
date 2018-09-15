package com.yfujiki.gradeconverter

import android.graphics.Typeface
import android.support.v4.app.Fragment
import android.os.Bundle
import android.os.Handler
import android.support.v4.provider.FontRequest
import android.support.v4.provider.FontsContractCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_main.*

/**
 * A placeholder fragment containing a simple view.
 */
class MainActivityFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val result = inflater.inflate(R.layout.fragment_main, container, false)

        val request = FontRequest("com.google.android.gms.fonts",
                "com.google.android.gms",
                "Yellowtail",
                R.array.com_google_android_gms_fonts_certs)

        val callback = object: FontsContractCompat.FontRequestCallback() {
            override fun onTypefaceRetrieved(typeface: Typeface) {
                super.onTypefaceRetrieved(typeface)

                titleTv.typeface = typeface;
            }

            override fun onTypefaceRequestFailed(reason: Int) {
                super.onTypefaceRequestFailed(reason)
            }
        }

        activity?.let {
            FontsContractCompat.requestFont(it, request, callback, Handler())
        }

        return result
    }
}
