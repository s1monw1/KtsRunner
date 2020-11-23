package de.swirtz.sample

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SampleAppTest
{
    @Test
    fun `can get random value from script`(){
        assertThat(readRandomValueFromScript()).isEqualTo(10)
    }
}