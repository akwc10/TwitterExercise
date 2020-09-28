package sequencing

import junit.framework.Assert.assertEquals
import org.junit.Test

class SequenceUtilsKtTest {

//    private fun <T, U> Sequence<T>.distinctUntilChangedBy(selector: (T) -> U): Sequence<T> {
//        val mutableList: MutableList<T> = this.toMutableList()
//        if (mutableList.count() <= 1) return this
//        val listIterator = mutableList.iterator()
//        var previousItem: T = listIterator.next()
//        var currentItem: T
//
//        while (listIterator.hasNext()) {
//            currentItem = listIterator.next()
//            if (selector(previousItem) == selector(currentItem)) listIterator.remove()
//            previousItem = currentItem
//        }
//        return mutableList.asSequence()
//    }

    private fun <T, U> Sequence<T>.distinctUntilChangedBy(selector: (T) -> U): Sequence<T> {
        val sequenceIterator = this.iterator()
        var previousSelector: U? = if (sequenceIterator.hasNext()) selector(sequenceIterator.next()) else null
        var currentSelector: U?
        val newSequence: Sequence<T>

        while (sequenceIterator.hasNext()) {
            currentSelector = selector(sequenceIterator.next())
//            TODO()
//            if (previousSelector == currentSelector) sequenceIterator.remove()
            previousSelector = currentSelector
        }
        return this
    }

    @Test
    fun distinctUntilChangedBy_filtersSubsequentElementsWhenSame() {
        assertEquals(
            listOf(1, 22, 3, 14, 5, 3),
            sequenceOf(1, 22, 12, 3, 14, 4, 5, 5, 3)
                .distinctUntilChangedBy { it % 10 }
                .toList()
        )
    }

    @Test
    fun distinctUntilChangedBy_outputsAllElementsIfDifferent() {
        assertEquals(
            listOf(1, 2, 3),
            sequenceOf(1, 2, 3)
                .distinctUntilChangedBy { it % 10 }
                .toList()
        )
    }

    @Test
    fun distinctUntilChangedBy_supportsConstrainOnceSequences() {
        assertEquals(
            listOf(1, 2, 3),
            sequenceOf(1, 2, 3)
                .constrainOnce()
                .distinctUntilChangedBy { it % 10 }
                .toList()
        )
    }

    @Test
    fun distinctUntilChangedBy_supportsSelectorNulls() {
        assertEquals(
            listOf(11, 2, 4),
            sequenceOf(11, 15, 2, 2, 4)
                .distinctUntilChangedBy {
                    if (it > 10) null
                    else it
                }
                .toList()
        )
    }
}