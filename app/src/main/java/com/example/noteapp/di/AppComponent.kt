package com.example.noteapp.di

import android.app.Application
import com.example.noteapp.activities.AddNoteActivity
import com.example.noteapp.activities.MainActivity
import com.example.noteapp.activities.UpdateNoteActivity
import com.example.noteapp.viewmodel.NoteViewModel
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AppModule::class]
)
interface AppComponent {
    fun noteViewModel(): NoteViewModel

    fun getAuthComponentFactory(): AuthComponent.Factory

    fun inject(activity: MainActivity)
    fun inject(activity: UpdateNoteActivity)
    fun inject(activity: AddNoteActivity)

//    @Component.Builder
//    interface Builder {
//        @BindsInstance
//        fun application(application: Application): Builder
//
//        fun build(): AppComponent
//    }

    @Component.Factory
    interface Factory{
        fun create(@BindsInstance application: Application): AppComponent
    }
}