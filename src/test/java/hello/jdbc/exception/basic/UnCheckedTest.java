package hello.jdbc.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
public class UnCheckedTest {

    @Test
    void unChecked_catch() {
        Service service = new Service();
        service.callCatch();
    }

    @Test
    void unChecked_throw() {
        Service service = new Service();
        Assertions.assertThatThrownBy(() -> service.callThrow())
                        .isInstanceOf(MyUncheckedException.class);
    }

    /**
     * RuntimeException을 상속받은 예외는 언체크 예외가 된다.
     */
    static class MyUncheckedException extends RuntimeException {
        public MyUncheckedException(String message) {
            super(message);
        }
    }

    /**
     * UnChecked 예외는
     * 예외를 잡거나, 던지지 안항도 된다.
     * 예외를 잡지 않으면 자동으로 밖으로 던진다.
     * 그리고 main() 순수자바와
     * was 웹어플리케이션
     * 일때 차이점이 있다. main() 순수자바코드일경우 자바단 끝까지 예외가 던져지면 cpu까지 가서 프로그램이 멈추는데
     * was 웹어플리케이션의 경우 was내부에서 자바의 예외를 처리해 에러페이지 응답으로 보내주는 부분이 있어 프로그램이 멈추지 않는다
     */
    static class Service {
        Repository repository = new Repository();

        /**
         * 필요한 경우 예외를 잡아서 처리하면 된다.
         */
        public void callCatch() {
            try {
                repository.call();
            } catch (MyUncheckedException e) {
                //예외 처리 로직
                log.info("예외처리, message={}",e.getMessage(), e);
            }
        }

        /**
         * 예외를 잡지 않아도 된다. 자연스럽게 상위 콜스택으로 넘어간다
         * 체크 예외와 다르게 throws 예외 선언을 하지 않아도 된다.
         */
        public void callThrow() {
            repository.call();
        }
    }

    static class Repository {
        public void call() {
            //throws 선언 안해도 가능
            throw new MyUncheckedException("ex");
        }
    }
}
