package ru.otus.homework;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.model.Message;
import ru.otus.processor.homework.ProcessorThrowsExceptionEveryTwoSecond;
import java.time.LocalDateTime;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ProcessorThrowsExceptionEveryTwoSecondTest {

    @Test
    @DisplayName("errorCall method should be called every even second")
    void errorCallTest(){
        //given
        var message = new Message.Builder(1L).field8("field8").build();
        var errorTime = LocalDateTime.of(2021,12,10,10,10,2);
        var errorSecond = errorTime.getSecond();
        var processor = new ProcessorThrowsExceptionEveryTwoSecond(errorTime);

        //then
        assertThatThrownBy(() -> processor.process(message)).isInstanceOf(RuntimeException.class)
                .hasMessageContaining(String.format("### error call on %s second! ###%n", errorSecond));
    }
}
