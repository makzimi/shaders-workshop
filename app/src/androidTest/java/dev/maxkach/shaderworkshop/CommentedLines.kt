package dev.maxkach.shaderworkshop

/** Exercise 1 style steps: a `// Explanation` line, then the step's code commented out line by line with `//`. */
object CommentedLines {
    private val step = Regex("(?m)(?:^[ \\t]*//(?! [A-Z]).*\\n)+")
    private val line = Regex("(?m)^([ \\t]*)//(?! [A-Z])")

    fun count(source: String): Int = step.findAll(source).count()

    /** Uncomments the first remaining step, or returns null when every step is already live. */
    fun uncommentFirst(source: String): String? {
        val first = step.find(source) ?: return null
        return source.replaceRange(first.range, line.replace(first.value, "$1"))
    }

    fun uncommentAll(source: String): String = line.replace(source, "$1")
}
