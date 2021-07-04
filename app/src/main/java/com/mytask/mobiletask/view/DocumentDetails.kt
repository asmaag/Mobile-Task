package com.mytask.mobiletask.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.mindorks.framework.mvvm.utils.Constants
import com.mytask.mobiletask.adapter.IspnAdapter
import com.mytask.mobiletask.R
import com.mytask.mobiletask.data.model.Document
import com.mytask.mobiletask.databinding.ActivityDocumentDetailsBinding


class DocumentDetails : AppCompatActivity() {
    private lateinit var adapter: IspnAdapter
    lateinit var document: Document
    private lateinit var databinding: ActivityDocumentDetailsBinding
    var authersNames = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        databinding = DataBindingUtil.setContentView(this, R.layout.activity_document_details)
        setRecycleView()
        getDataFromIntent()
        setDocTitle()
        setDocAuthor()
        setIsbnsOfDocument()
        setListenners()
    }

    /*
         get Data From Parcelable object from intent to
     */
    private fun getDataFromIntent() {
        document = intent.getParcelableExtra(Constants.DOCUMENTNME)!!

    }

    /*
    set title of document
     */
    private fun setDocTitle() {
        databinding.docTitleTV.text = document.title

    }

    /*
       set authors of document
       if document has more than author
       represent it all separated by comma
       if Doc return with no authors
       hide this field and title of it
      */
    private fun setDocAuthor() {
        if (document.authors != null) {
            authersNames = document.authors?.joinToString(separator = " , ").toString()
            databinding.docAuthorsTV.text = authersNames

        } else {
            databinding.titleAuthorsTV.visibility = View.GONE
            databinding.docAuthorsTV.visibility = View.GONE

        }
    }

    /* set Isbn list if exist
     */
    private fun setIsbnsOfDocument() {
        if (document.isbn != null) {
            adapter.setIsbnsList(document.isbn!!)
        }
    }

// to retrun to document list to start serch by author name or title
    fun setListenners() {
        databinding.docTitleTV.setOnClickListener {
            val intent = Intent()
            intent.putExtra(Constants.Search_KEY, databinding.docTitleTV.text.toString())
            intent.putExtra(Constants.TYPE_SEARCH, Constants.TYPE_SEARCH_TITLE)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
        databinding.docAuthorsTV.setOnClickListener {
            val intent = Intent()
            intent.putExtra(Constants.Search_KEY, databinding.docAuthorsTV.text.toString())
            intent.putExtra(Constants.TYPE_SEARCH, Constants.TYPE_SEARCH_Author)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
    fun setRecycleView() {
        adapter = IspnAdapter();
        databinding.isbnResultRV.addItemDecoration(
            DividerItemDecoration(
                databinding.isbnResultRV.context,
                (databinding.isbnResultRV.layoutManager as LinearLayoutManager).orientation
            )
        )
        databinding.isbnResultRV.adapter = adapter

    }
}