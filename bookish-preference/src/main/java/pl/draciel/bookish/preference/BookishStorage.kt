package pl.draciel.bookish.preference

interface BookishStorage {

    /**
     * Atomically puts all given values
     */
    fun bulkPut(list: List<Pair<TypedKey<*, *>, *>>)

    /**
     * Puts [value]
     */
    fun <T, R> put(key: TypedKey<T, R>, value: T)

    /**
     * @return currently stored value
     */
    fun <T, R> get(key: TypedKey<T, R>): T

    /**
     * Atomically puts [updated] value if current value equals [expected]. If not, [updated] is not invoked at all.
     * @return true if operation was successful
     */
    fun <T, R> compareAndPut(key: TypedKey<T, R>, expected: T, updated: () -> T): Boolean

    /**
     * Atomically puts [updated] value if current value equals [expected]. If not, [updated] is not invoked at all.
     * @return [updated] value if operation was successful, if not it returns currently stored value
     */
    fun <T, R> compareAndPutAndGet(key: TypedKey<T, R>, expected: T, updated: () -> T): T

    /**
     * Checks if any value exists for the given [key]
     */
    fun <T, R> exists(key: TypedKey<T, R>): Boolean

}
