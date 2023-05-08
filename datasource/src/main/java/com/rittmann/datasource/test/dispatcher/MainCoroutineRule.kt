package com.rittmann.datasource.test.dispatcher

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@ExperimentalCoroutinesApi
class MainCoroutineRule : TestWatcher() {

    val testDispatcher = TestDispatcherProvider()

    override fun starting(description: Description?) {
        super.starting(description)
        Dispatchers.setMain(testDispatcher.main)
    }

    override fun finished(description: Description?) {
        super.finished(description)
        Dispatchers.resetMain()
    }
}