import Depends.Hilt.implementHilt
import Depends.Core.implementCoreKtx
import Depends.ViewModel.implementViewModel
import Depends.Retrofit.implementRetrofit
import Depends.Test.implementTest

plugins {
    id(Depends.Plugins.HILT)
}

kapt {
    correctErrorTypes = true
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    // =========== Core ==============
    implementCoreKtx()

    // =========== Hilt ==============
    implementHilt()

    // =========== ViewModel ==============
    implementViewModel()

    // =========== Retrofit ==============
    implementRetrofit()

    // =========== Test ==============
    implementTest()

    implementation( "org.jetbrains.kotlin:kotlin-reflect:${Versions.KOTLIN_VERSION}")
}