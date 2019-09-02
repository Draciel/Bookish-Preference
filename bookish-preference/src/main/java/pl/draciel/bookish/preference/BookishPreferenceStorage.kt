package pl.draciel.bookish.preference

import android.annotation.SuppressLint
import android.content.SharedPreferences
import androidx.annotation.GuardedBy
import androidx.preference.PreferenceDataStore
import pl.draciel.bookish.preference.TypedKey.*

/**
 * Implementation of [BookishStorage] with [PreferenceDataStore] support.
 * By default, all operations happen on current thread due to usage of [SharedPreferences.Editor.commit]
 */
class BookishPreferenceStorage(@GuardedBy("lock") private val sharedPreferences: SharedPreferences) :
    BookishStorage, PreferenceDataStore() {

    private val lock: Any = Any()

    override fun <T, R> put(key: TypedKey<T, R>, value: T) {
        synchronized(lock) {
            sharedPreferences.edit {
                when (key) {
                    is IntKey -> putInt(key.key, value as Int)
                    is BooleanKey -> putBoolean(key.key, value as Boolean)
                    is FloatKey -> putFloat(key.key, value as Float)
                    is LongKey -> putLong(key.key, value as Long)
                    is StringKey -> putString(key.key, value as String)
                    is NullableStringKey -> putString(key.key, value as? String)
                    is ObjectKey<T> -> putString(key.key, key.serialize(value))
                    is NullableObjectKey<T> -> putString(key.key, key.serialize(value))
                }
            }
        }
    }

    override fun bulkPut(list: List<Pair<TypedKey<*, *>, *>>) {
        synchronized(lock) {
            list.forEach { unsafePut(it.first, it.second) }
        }
    }

    @Suppress("UNCHECKED_CAST") // this casts are safe, because we are checking for key type before cast
    override fun <T, R> get(key: TypedKey<T, R>): T {
        return synchronized(lock) {
            unsafeGet(key)
        }
    }

    override fun <T, R> exists(key: TypedKey<T, R>): Boolean {
        synchronized(lock) {
            return sharedPreferences.contains(key.key)
        }
    }

    override fun <T, R> compareAndPut(key: TypedKey<T, R>, expected: T, updated: () -> T): Boolean {
        synchronized(lock) {
            val value = unsafeGet(key)
            var success = false
            if (value == expected) {
                unsafePut(key, updated())
                success = true
            }
            return success
        }
    }

    override fun <T, R> compareAndPutAndGet(key: TypedKey<T, R>, expected: T, updated: () -> T): T {
        synchronized(lock) {
            var value = unsafeGet(key)
            if (value == expected) {
                value = updated()
                unsafePut(key, value)
            }
            return value
        }
    }

    // Bridge for Preferences API

    override fun getBoolean(key: String, defValue: Boolean): Boolean {
        synchronized(lock) {
            return sharedPreferences.getBoolean(key, defValue)
        }
    }

    override fun putLong(key: String, value: Long) {
        synchronized(lock) {
            sharedPreferences.edit { putLong(key, value) }
        }
    }

    override fun putInt(key: String, value: Int) {
        synchronized(lock) {
            sharedPreferences.edit { putInt(key, value) }
        }
    }

    override fun getInt(key: String, defValue: Int): Int {
        synchronized(lock) {
            return sharedPreferences.getInt(key, defValue)
        }
    }

    override fun putBoolean(key: String, value: Boolean) {
        synchronized(lock) {
            sharedPreferences.edit { putBoolean(key, value) }
        }
    }

    override fun putStringSet(key: String, values: MutableSet<String>?) {
        synchronized(lock) {
            sharedPreferences.edit { putStringSet(key, values) }
        }
    }

    override fun getLong(key: String, defValue: Long): Long {
        synchronized(lock) {
            return sharedPreferences.getLong(key, defValue)
        }
    }

    override fun getFloat(key: String, defValue: Float): Float {
        synchronized(lock) {
            return sharedPreferences.getFloat(key, defValue)
        }
    }

    override fun putFloat(key: String, value: Float) {
        synchronized(lock) {
            sharedPreferences.edit { putFloat(key, value) }
        }
    }

    override fun getStringSet(key: String?, defValues: MutableSet<String>?): MutableSet<String>? {
        synchronized(lock) {
            return sharedPreferences.getStringSet(key, defValues)
        }
    }

    override fun getString(key: String?, defValue: String?): String? {
        synchronized(lock) {
            return sharedPreferences.getString(key, defValue)
        }
    }

    override fun putString(key: String?, value: String?) {
        synchronized(lock) {
            sharedPreferences.edit { putString(key, value) }
        }
    }

    @Suppress("UNCHECKED_CAST") // this casts are safe, because we are checking for key type before cast
    private fun <T, R> unsafeGet(key: TypedKey<T, R>): T {
        return when (key) {
            is IntKey -> sharedPreferences.getInt(key.key, key.default) as T
            is BooleanKey -> sharedPreferences.getBoolean(key.key, key.default) as T
            is FloatKey -> sharedPreferences.getFloat(key.key, key.default) as T
            is LongKey -> sharedPreferences.getLong(key.key, key.default) as T
            is StringKey -> sharedPreferences.getString(key.key, key.default)!! as T
            is NullableStringKey -> sharedPreferences.getString(key.key, key.default) as T
            is ObjectKey<T> -> key.deserialize(sharedPreferences.getString(key.key, key.serializedDefault)!!)
            is NullableObjectKey<T> -> key.deserialize(sharedPreferences.getString(key.key, key.serializedDefault))
        }
    }

    @SuppressLint("ApplySharedPref")
    @Suppress("UNCHECKED_CAST") // this casts are safe, because we are checking for key type before cast
    private fun <T, R> unsafePut(key: TypedKey<T, R>, value: Any?) {
        sharedPreferences.edit {
            when (key) {
                is IntKey -> putInt(key.key, value as Int)
                is BooleanKey -> putBoolean(key.key, value as Boolean)
                is FloatKey -> putFloat(key.key, value as Float)
                is LongKey -> putLong(key.key, value as Long)
                is StringKey -> putString(key.key, value as String)
                is NullableStringKey -> putString(key.key, value as? String)
                is ObjectKey<T> -> putString(key.key, key.serialize(value as T))
                is NullableObjectKey<T> -> putString(key.key, key.serialize(value as T))
            }
        }
    }
}
