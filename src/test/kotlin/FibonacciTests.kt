import org.junit.jupiter.api.*
import ru.emkn.kotlin.FibIterable
import ru.emkn.kotlin.FibIterator
import ru.emkn.kotlin.iterativeFib
import ru.emkn.kotlin.slowFib
import java.lang.IllegalArgumentException
import java.time.Duration
import java.util.stream.IntStream
import java.util.stream.Stream
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class FibonacciTests {
    @Test
    fun `simple test`() {
        assertEquals(3, FibIterable(5).last())
        assertEquals(89, FibIterable(12).last())
        assertEquals(233, FibIterable(14).last())
        assertEquals(377, FibIterable(15).last())
        assertEquals(610, FibIterable(16).last())
    }

    @Test
    fun `illegal argument test`() {
        assertFailsWith(IllegalArgumentException::class) {
            slowFib(-2)
        }
    }

    @Test
    fun `timeout test`() {
        val res = assertTimeoutPreemptively(Duration.ofSeconds(5)) {
            iterativeFib(50)
            //slowFib(50)
        }
        assertEquals(20365011074L, res)
    }

    @TestFactory
    fun `multiple test`(): Stream<DynamicTest> {
        val expected: List<Int> = listOf(0, 0, 1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144)
        return IntStream.range(1, 11).mapToObj { n ->
            DynamicTest.dynamicTest("Test fib for $n") {
                assertEquals(expected[n], FibIterable(n).last())
            }
        }
    }
}