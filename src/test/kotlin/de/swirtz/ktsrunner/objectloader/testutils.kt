package de.swirtz.ktsrunner.objectloader

import org.assertj.core.api.Assertions

inline fun <reified T : Throwable> assertExceptionThrownBy(crossinline op: () -> Unit) =
    Assertions.assertThatExceptionOfType(T::class.java).isThrownBy {
        op()
    }