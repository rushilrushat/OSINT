package com.example.testapp

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.widget.Toast
import com.itextpdf.text.*
import com.itextpdf.text.BaseColor
import com.itextpdf.text.pdf.BaseFont
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import com.itextpdf.text.pdf.draw.LineSeparator
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*


class createPDF(context: Context) {
    var  context1:Context=context
    var pdfFile: File? =null
    init {

    }

    fun createPdf( key: Array<String>, value: ArrayList<String>) {



        //FIle name
        val mFileName = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis())+".pdf"
        //pdf file path
        val path = File(Environment.getExternalStorageDirectory().toString() + "/")
        pdfFile = File(path.getAbsolutePath(), mFileName)
        // create a new document
        val mDoc = Document(PageSize.A4)
        val output: OutputStream = FileOutputStream(pdfFile)
        //val mFilePath = Environment.getExternalStorageDirectory().toString() + "/" + mFileName
        try {
            //create instance of PdfWriter class
            PdfWriter.getInstance(mDoc,output)

            //open the document for writing
            mDoc.open()



            //get text from EditText i.e. textEt
            val fontName =
                BaseFont.createFont("assets/fonts/BrandonText-Medium.otf", "UTF-8", BaseFont.EMBEDDED)
            //add author of the document (metadata)
            mDoc.addAuthor("Anonymous")
            var titleStyle = Font(fontName, 36.0f, Font.NORMAL, BaseColor.BLACK)
            addNewItem(mDoc, "IP Information",Element.ALIGN_TOP, titleStyle)

            val lineSeparator = LineSeparator()
            lineSeparator.setLineColor(BaseColor(0, 0, 0, 68))


            mDoc.add(Chunk(lineSeparator))
            var pdftable = PdfPTable(2)

            var cell = PdfPCell(Phrase("Data Fatch From keycdn.com"))
            cell.setColspan(2)
            pdftable.addCell(cell)

            for (j in key) {
                var phrase = Phrase(j);
                pdftable.addCell(PdfPCell(phrase));


            }
            for (i in value) {
                var phrase = Phrase(i);
                pdftable.addCell(PdfPCell(phrase));
                //mDoc.add(Paragraph(i))

            }
            pdftable.completeRow()

            mDoc.add(pdftable)
            //close document
            mDoc.close()

            //show file saved message with file name and path
            Toast.makeText(context1, "$mFileName.pdf\nis saved to\n$pdfFile", Toast.LENGTH_SHORT)
                .show()
            Log.d("File save", Uri.fromFile( pdfFile).toString())

            previewPdf()

        } catch (e: Exception) {
            //if anything goes wrong causing exception, get and show exception message
            Toast.makeText(context1, e.message, Toast.LENGTH_LONG).show()
            Log.e("File Error",e.message.toString())

        }

    }

    private fun addNewItem(mDoc: Document, text: String, align: Int, style: Font) {

        val chunk = Chunk(text, style)
        val p = Paragraph(chunk)
        p.alignment = align
        mDoc.add(p)

    }
    private fun previewPdf() {

        var intent=Intent(context1,pdfView::class.java)
        intent.putExtra("FileUrl", Uri.fromFile( pdfFile).toString())
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK)
        context1.startActivity(intent)


    }

}
