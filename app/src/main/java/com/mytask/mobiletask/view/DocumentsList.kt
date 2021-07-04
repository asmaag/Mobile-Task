package com.mytask.mobiletask.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mindorks.framework.mvvm.utils.Constants
import com.mytask.mobiletask.adapter.DocumentsAdapter
import com.mytask.mobiletask.R
import com.mytask.mobiletask.data.model.Document
import com.mytask.mobiletask.data.listenners.OnItemClickListener
import com.mytask.mobiletask.data.repository.DocumentRepository
import com.mytask.mobiletask.databinding.ActivityDocumentListBinding
import com.mytask.mobiletask.network.RetrofitService
import com.mytask.mobiletask.utils.NetworkUtils
import com.mytask.mobiletask.viewmodel.DocumentViewModel
import com.mytask.mobiletask.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.no_network_layout.view.*


class DocumentsList : AppCompatActivity(),OnItemClickListener {
    private lateinit var documentsAdapter: DocumentsAdapter
    private lateinit var viewModel: DocumentViewModel
    private var page = 1
    private var searchType = ""
    private var lastSearchkey = ""
    private var isLoading = false
    private lateinit var launcher: ActivityResultLauncher<Intent>
    private lateinit var databinding: ActivityDocumentListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        databinding = DataBindingUtil.setContentView(this,R.layout.activity_document_list)
        setViewModel()
        setLauncherResult()
        setAdapterToRecycleView()
        observeOnData()
        setListenners()
    }

    private fun setViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(DocumentRepository(RetrofitService.getInstance()))
        ).get(DocumentViewModel::class.java)
    }


/*
     this for set adapter t recycle view and
     to add on scroll in recycle view to handle Pagination add flag "isLoading"
     to prevent scroll view to git another page before first page already loaded
 */
    private fun setAdapterToRecycleView() {
        databinding.searchTV.requestFocus()
        documentsAdapter = DocumentsAdapter(this)
        databinding.searchResultRV.addItemDecoration(
            DividerItemDecoration(
                databinding.searchResultRV.context,
                (databinding.searchResultRV.layoutManager as LinearLayoutManager).orientation
            )
        )
        databinding.searchResultRV.adapter = documentsAdapter
        databinding.searchResultRV.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(0) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (viewModel.loadMorData() && page != 1 && !isLoading) {
                        viewModel.getDocumentsBySearchKey(lastSearchkey, page,searchType)
                        isLoading = true;
                        databinding.moreprogressbar.visibility = View.VISIBLE
                    }
                }
            }

        });
    }
/*
     apply search when click on typing of edit text  type of
     general search for query search typed by user
     another type is (Author ) to search by author another type(Title) to search by title
     after search by title or author name  when clear search area return to General type
     (can change that depend on required  logic)
 */
    private fun applySearch(editable: Editable) {
        page = 1;
        if (editable.toString() != "") {
            showLoading();
            viewModel.getDocumentsBySearchKey(editable.toString(), page,searchType)
            lastSearchkey = editable.toString()
        } else {
            searchType = Constants.TYPE_SEARCH_GENERAL
            hideLoading()
        }
    }


    /*
       to get Data and perform search after retrun from screen document details
        by author name or document's title
     */

    private fun setLauncherResult() {
        launcher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val returnString: String = result.data?.getStringExtra(Constants.Search_KEY)!!
                    searchType = result.data?.getStringExtra(Constants.TYPE_SEARCH)!!
                    databinding.searchTV.setText(returnString)
                }
            }
    }
/*
       to observe on data that get from viewmodel which can be documents list or error msg
       handle error can be change according to error code
       Failure error is general can change to read msg from api if exist ,
       ServerError  and Network error
     */

    private fun observeOnData() {
        viewModel.docsList.observe(this, Observer {
            hideLoading();
            hideKeyboard();
            isLoading = false;
            if (it.isEmpty()) {
                showNoData()
            } else {
                databinding.searchResultRV.visibility = View.VISIBLE
                databinding.noDataLayout.visibility = View.GONE
                databinding.noNetworkLayout.visibility = View.GONE
                documentsAdapter.setDocumentsList(it)
                page++
            }

        })
        viewModel.errorMessage.observe(this, Observer {
            hideLoading()
            hideKeyboard()
            if(NetworkUtils.isNetworkAvailable(this)){
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }else {
            if (page == 1) {
                showNetworkError()
            } else {
                Toast.makeText(this, "Network Error, Please check your network ", Toast.LENGTH_LONG).show()

            }
        }
        })
    }

    private fun setListenners(){
        databinding.searchTV.doAfterTextChanged { editable ->
            editable?.let { applySearch(it) }
        }
        databinding.noNetworkLayout.retry_button.setOnClickListener{
            trySearchAgain()
        }
    }
    private fun showLoading() {
        if (databinding.progressbar.visibility == View.GONE)
            databinding.progressbar.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        databinding.progressbar.visibility = View.GONE
        databinding.moreprogressbar.visibility = View.GONE

    }

    private fun showNoData() {
        databinding.searchResultRV.visibility = View.GONE
        databinding.noDataLayout.visibility = View.VISIBLE

    }
    private fun showNetworkError() {
        databinding.searchResultRV.visibility = View.GONE
        databinding.noNetworkLayout.visibility = View.VISIBLE
    }
    private fun trySearchAgain(){
        showLoading()
        databinding.noNetworkLayout.visibility = View.GONE
        viewModel.getDocumentsBySearchKey(lastSearchkey, page,searchType)

    }

    fun Activity.hideKeyboard() {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }

    override fun onCickItem(document: Document?) {
        val intent = Intent(this, DocumentDetails::class.java)
        intent.putExtra(Constants.DOCUMENTNME, document)
        launcher.launch(intent)
    }
}