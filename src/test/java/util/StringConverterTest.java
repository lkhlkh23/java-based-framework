package util;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class StringConverterTest {

    @Test
    public void extractSetterNameTest() {
        String str = "setUserId";
        assertThat(StringConverter.extractSetterName(str)).isEqualTo("userId");
    }
}
