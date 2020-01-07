package com.example.notemanager.new_note

import android.app.Application
import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.notemanager.data.CategoryRepository
import com.example.notemanager.data.NoteRepository
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.core.Is.`is`
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class NewNoteViewModelTest {

    private lateinit var viewModel: NewNoteViewModel


    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Application>()
        viewModel = NewNoteViewModel(
            NoteRepository.getInstance(context),
            CategoryRepository.getInstance(context = context)
        )
    }

    @Test
    fun onTimeMenuClick_notNullValue() {
        viewModel.onTimeMenuClicked()

        val value = viewModel.onTimeMenuClick.getOrAwaitValue()
        assertThat(value.getContentIfNotHandled(), `is`(notNullValue()))
    }
}