package eikona.utils.json

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import kotlin.reflect.KClass

abstract class JsonServiceTest<T : Any>(
    private val element: T,
    private val clazz: KClass<T>
) {

    @Test
    open fun cycle() {
        Assertions.assertEquals(element, element.toJson().toModel(clazz))
        Assertions.assertEquals(element, element.toJson().toModel(clazz.java))
    }

    @Test
    open fun repeatable() =
        Assertions.assertEquals(element.toJson(), element.toJson())

    @Test
    open fun byteArrayCycle() =
        Assertions.assertEquals(element, String(element.toJson().toByteArray()).toModel(clazz))
}
