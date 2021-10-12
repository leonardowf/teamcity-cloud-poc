package com.example.application

import android.widget.TextView
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockito_kotlin.spy
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@RunWith(AndroidJUnit4::class)
class ExampleAndroidTest {

    @Mock
    lateinit var textView: TextView

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun testTextView() {
        val text = "Some value"
        `when`(textView.text).thenReturn(text)
        assertEquals(text, textView.text)
    }
}