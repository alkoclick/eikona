package eikona.utils.json

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.datatype.jsr310.deser.InstantDeserializer
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import java.io.IOException
import java.time.Instant
import kotlin.reflect.KClass

/** This class should be used for unmarshalling POJOs out of Json files.
 *
 * Full Documentation is available at:
 */
object JsonService {
    private val OBJECT_MAPPER: ObjectMapper =
        ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .registerKotlinModule()

    init {
        OBJECT_MAPPER.registerModule(JavaTimeModule().apply { addDeserializer(Instant::class.java, InstantDeserializer.INSTANT) })
    }

    /**
     * @see Any.toJson()
     */
    @Throws(IOException::class)
    fun <T : Any> fromJson(content: String, valueType: Class<T>): T {
        return OBJECT_MAPPER.readValue(content, valueType)
    }

    @Throws(IOException::class)
    internal fun <T : Any> fromJson(content: String, valueType: TypeReference<T>): T {
        return OBJECT_MAPPER.readValue(content, valueType)
    }

    @Throws(IOException::class)
    fun <T : Any> toJson(value: T): String {
        return OBJECT_MAPPER.writeValueAsString(value)
    }
}


/**
 * @param this Object that you want to convert to JSON
 * @param <T> the object type
 * @return a JSON String, never null
 * @throws IOException If there was a low level I/O problem, the JSON has invalid content */
@Throws(IOException::class)
fun Any.toJson(): String =
    JsonService.toJson(this)

/**
 * Like [toJson], but wraps the object in an external named object, so your result will be:
 * ```
 * { key: { value.toJson() } }
 * ```
 *
 * @param this object that you want to convert to JSON
 * @param key the name of the external
 * @param <T> the object type
 * @return a JSON String, never null
 * @throws IOException If there was a low level I/O problem, the JSON has invalid content */
@Throws(IOException::class)
fun Any.toWrappedJson(key: String): String =
    JsonService.toJson(mapOf(key to this))

/**
 * @param this Json formatted string with a structure that matches the `valueType`
 * @param valueType a POJO usually, whose structure matches the Json formatted `content`
 * @param <T> the object type that you want to end up with
 * @return a T object, never null
 * @throws IOException If there was a low level I/O problem, the JSON has invalid content. or the
 * JSON structure does not match the expected `valueType`. */
@Throws(IOException::class)
fun <T : Any> String.toModel(valueType: Class<T>): T =
    JsonService.fromJson(this, valueType)

/**
 * Convenience method to avoid additional casts when using this method from Kotlin
 *
 * @param content Json formatted string with a structure that matches the `valueType`
 * @param valueType a POKO usually, whose structure matches the Json formatted `content`
 * @param <T> the object type that you want to end up with
 * @return a T object, never null
 * @throws IOException If there was a low level I/O problem, the JSON has invalid content. or the
 * JSON structure does not match the expected `valueType`. */
@Throws(IOException::class)
fun <T : Any> String.toModel(valueType: KClass<T>): T =
    toModel(valueType.java)

/**
 * @param content Json formatted string with a structure that matches the `valueType`
 * @param valueType a POJO usually, whose structure matches the Json formatted `content`
 * @param <T> the object type that you want your list to end up with
 * @return a T object, never null
 * @throws IOException If there was a low level I/O problem, the JSON has invalid content. or the
 * JSON structure does not match the expected `valueType`. */
@Throws(IOException::class)
fun <T : Any> String.toModel(valueType: TypeReference<T>): T =
    JsonService.fromJson(this, valueType)

const val NULL_JSON = "null"
val NULL_JSON_ARRAY = NULL_JSON.toByteArray()