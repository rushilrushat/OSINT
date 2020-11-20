package com.example.testapp.util

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.testapp.BuildConfig
import com.example.testapp.R
import kotlinx.android.synthetic.main.activity_pdf_view.*
import java.io.File

class pdfView : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf_view)
        val pdf: Uri = Uri.parse(intent.getStringExtra("FileUrl"))
        val pdf1: Uri =  FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID +".provider",File(pdf.getPath()))

        fab.setOnClickListener {

            val intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_STREAM, pdf1)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.setDataAndType(pdf1,"*/*")
            startActivity(Intent.createChooser(intent, "Share via"))


        }
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
                    Toast.makeText(this, "Error while openingpage" + page, Toast.LENGTH_LONG).show()
                    Log.d("ERROR", "" + t.localizedMessage)
                }.onTap { false }
                .onRender { nbPages, pageWidth, pageHeight ->
                    pdfview.fitToWidth()
                }
                .enableAnnotationRendering(true)
                .invalidPageColor(Color.RED)
                .load()
    }
}
