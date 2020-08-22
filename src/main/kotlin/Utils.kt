fun appendNotEnd(index: Int, size: Int, string: String = ", ") = if (index < size - 1) string else ""

fun appendEnd(index: Int, size: Int, string: String = "]\n") = if (index >= size - 1) string else ""