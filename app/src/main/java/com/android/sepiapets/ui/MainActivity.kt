package com.android.sepiapets.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.android.sepiapets.R
import com.android.sepiapets.adapters.PetListAdapter
import com.android.sepiapets.dataSource.ConfigLocalDataSourceImpl
import com.android.sepiapets.dataSource.PetLocalDataSourceImpl
import com.android.sepiapets.databinding.ActivityMainBinding
import com.android.sepiapets.models.petsData.Pet
import com.android.sepiapets.models.result.ErrorData
import com.android.sepiapets.repositories.ConfigRepositoryImpl
import com.android.sepiapets.repositories.PetsRepositoryImpl
import com.android.sepiapets.utils.Constants.PET_DATA
import com.android.sepiapets.utils.FileOperations
import com.android.sepiapets.viewmodels.MainActivityViewModel
import com.android.sepiapets.viewmodels.ViewModelFactory
import kotlinx.coroutines.Dispatchers
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var mainActivityViewModel: MainActivityViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var petListAdapter: PetListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViewModel()

        binding.apply {
            petListAdapter = PetListAdapter(object : PetListAdapter.OnClickListener {
                override fun onItemClick(pet: Pet) {
                    val intent = Intent(this@MainActivity, PetDetailWebView::class.java)
                    intent.putExtra(PET_DATA, pet)
                    startActivity(intent)
                }
            })
            petRecyclerView.adapter = petListAdapter
        }

        mainActivityViewModel.petList.observe(this) { petList ->
            petList?.pets?.let { petListAdapter.setPetList(it) }
            setUIState()
        }

        mainActivityViewModel.petErrorData.observe(this) {
            setUIState(it)
        }

        mainActivityViewModel.configData.observe(this) { config ->
            config?.let {
                val calendar = Calendar.getInstance()
                val currentDay = calendar.get(Calendar.DAY_OF_WEEK)
                val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
                val isInWorkingHours =
                    mainActivityViewModel.checkWorkingHours(it, currentDay, currentHour)
                if (isInWorkingHours) {
                    mainActivityViewModel.getPetsList()
                } else {
                    showWorkingHourDialog(getString(R.string.not_in_working_hour_msg))
                }
            }
        }

        mainActivityViewModel.configErrorData.observe(this) {
            showWorkingHourDialog(getString(R.string.not_in_working_hour_error))
        }

        mainActivityViewModel.getConfigData()
    }

    private fun showWorkingHourDialog(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(message)
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        builder.setPositiveButton(getString(R.string.exit)) { _, _ ->
            finish()
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    private fun setUIState(errorData: ErrorData? = null) {
        when (errorData == null) {
            true -> {
                if (petListAdapter.itemCount == 0) {
                    binding.petRecyclerView.visibility = View.GONE
                    binding.emptyState.visibility = View.VISIBLE
                } else {
                    binding.petRecyclerView.visibility = View.VISIBLE
                    binding.emptyState.visibility = View.GONE
                }
            }
            false -> {
                binding.errorState.visibility = View.VISIBLE
                binding.petRecyclerView.visibility = View.GONE
                binding.errorMsg.apply {
                    text = errorData.message
                }
            }
        }
    }

    private fun initViewModel() {
        mainActivityViewModel = ViewModelProvider(
            this, ViewModelFactory(
                MainActivityViewModel(
                    petsRepository = PetsRepositoryImpl(
                        PetLocalDataSourceImpl(
                            FileOperations(applicationContext)
                        )
                    ), configRepository = ConfigRepositoryImpl(
                        ConfigLocalDataSourceImpl(
                            FileOperations(applicationContext)
                        )
                    ), ioDispatcher = Dispatchers.IO
                )
            )
        )[MainActivityViewModel::class.java]
    }
}