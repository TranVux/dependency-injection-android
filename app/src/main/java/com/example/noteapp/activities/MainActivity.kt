package com.example.noteapp.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.noteapp.R
import com.example.noteapp.adapter.NoteAdapter
import com.example.noteapp.databinding.ActivityMainBinding
import com.example.noteapp.model.Note
import com.example.noteapp.viewmodel.NoteViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Qualifier
import javax.inject.Singleton

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val TAG = "NOTE_VIEW_MODEL"

    private val noteViewModel: NoteViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding

//    @Inject
//    lateinit var myServiceImpl: MyService
    @Inject
    lateinit var myCombineService: MyCombineService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)


        initControls()
        initEvents()
        Log.d(TAG, "onCreate: ${myCombineService.doSomeThing()}")
    }

    private fun initEvents() {
        binding.btnOpenAddActivity.setOnClickListener {
            val intent = Intent(this, AddNoteActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initControls() {
        val adapter = NoteAdapter(this@MainActivity, onItemClick, onItemDelete)

        binding.rvNote.setHasFixedSize(true)
        binding.rvNote.layoutManager = LinearLayoutManager(this)
        binding.rvNote.adapter = adapter

        noteViewModel.getAllNote().observe(this) {
            adapter.setNotes(it)
        }

    }

    private val onItemClick: (Note) -> Unit = {
        val intent = Intent(this, UpdateNoteActivity::class.java)
        intent.putExtra("UPDATE_NOTE", it)
        startActivity(intent)

    }
    private val onItemDelete: (Note) -> Unit = {
        noteViewModel.deleteNote(it)
    }
}

//@InstallIn(SingletonComponent::class)
//@Module
//abstract class MyServiceModule {
//    @Binds
//    @Singleton
//    abstract fun bindMyService(myServiceImpl: MyServiceImpl): MyService
//}

@InstallIn(SingletonComponent::class)
@Module
object MyServiceModule{
    @Provides
    @Singleton
    @AnoMyService
    fun provideMyService(): MyService = MyServiceImpl()

    @Provides
    @Singleton
    @AnoOtherMyService
    fun provideOtherMyService(): MyService = OtherMyServiceImpl()
}

class MyCombineService @Inject constructor(
    @AnoMyService private val myServiceImpl: MyService,
    @AnoOtherMyService private val otherMyServiceImpl: MyService
){
    fun doSomeThing() = "Hey yoo ${myServiceImpl.getSomeThing()}, ${otherMyServiceImpl.getSomeThing()}"
}

interface MyService {
    fun getSomeThing(): String
}

class MyServiceImpl @Inject constructor() : MyService {
    override fun getSomeThing(): String {
        return "This is something"
    }

}

class OtherMyServiceImpl @Inject constructor() : MyService {
    override fun getSomeThing(): String {
        return "This is something from other service"
    }
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AnoMyService

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AnoOtherMyService