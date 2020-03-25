package com.example.testapp

import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.github.barteksc.pdfviewer.listener.OnErrorListener
import kotlinx.android.synthetic.main.activity_pdf_view.*

class pdfView : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf_view)

            val pdf:Uri= Uri.parse(intent.getStringExtra("FileUrl"))
            pdfview.fromUri(pdf)
                .password(null)
                .defaultPage(0)
                .enableSwipe(true)
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .onDraw { canvas, pageWidth, pageHeight, displayedPage ->  
                    //dsd
                }.onDraw { canvas, pageWidth, pageHeight, displayedPage ->
                    //jk
                }.onPageChange { page, pageCount ->
                    //j
                }.onPageError { page, t ->
                    Toast.makeText(this,"Error while openingpage"+page,Toast.LENGTH_LONG).show()
                    Log.d("ERROR",""+t.localizedMessage)
                }.onTap{false}
                .onRender { nbPages, pageWidth, pageHeight ->
                    pdfview.fitToWidth()
                }
                .enableAnnotationRendering(true)
                .invalidPageColor(Color.RED)
                .load()
    }
}
