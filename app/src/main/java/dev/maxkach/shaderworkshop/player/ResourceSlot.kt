package dev.maxkach.shaderworkshop.player

internal class ResourceSlot<T>(private val release: (T) -> Unit) {
    private var current: T? = null
    private var closed = false
    val isClosed: Boolean get() = closed

    fun replace(next: T): Boolean {
        if (closed) {
            release(next)
            return false
        }
        current?.let(release)
        current = next
        return true
    }

    fun close() {
        closed = true
        current?.let(release)
        current = null
    }
}
