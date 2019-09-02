package pl.draciel.bookish.preference

sealed class TypedKey<T, R> {
    abstract val key: String
    abstract val default: T

    class IntKey(override val key: String, override val default: Int) : TypedKey<Int, Int>()
    class BooleanKey(override val key: String, override val default: Boolean) : TypedKey<Boolean, Boolean>()
    class FloatKey(override val key: String, override val default: Float) : TypedKey<Float, Float>()
    class LongKey(override val key: String, override val default: Long) : TypedKey<Long, Long>()
    class StringKey(override val key: String, override val default: String) : TypedKey<String, String>()
    class NullableStringKey(override val key: String, override val default: String?) : TypedKey<String?, String?>()

    abstract class ObjectKey<T>(override val key: String, override val default: T) : TypedKey<T, String>() {
        internal val serializedDefault: String by lazy { serialize(default) }
        abstract fun serialize(obj: T): String
        abstract fun deserialize(data: String): T
    }

    abstract class NullableObjectKey<T>(override val key: String, override val default: T) : TypedKey<T, String?>() {
        internal val serializedDefault: String? by lazy { serialize(default) }
        abstract fun serialize(obj: T): String?
        abstract fun deserialize(data: String?): T
    }
}
