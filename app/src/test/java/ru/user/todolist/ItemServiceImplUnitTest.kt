package ru.user.todolist

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.BlockJUnit4ClassRunner
import org.junit.runners.MethodSorters
import ru.user.todolist.domain.Item
import ru.user.todolist.exception.EntryExistsException
import ru.user.todolist.exception.EntryNotFoundException
import ru.user.todolist.exception.TextValidationException
import ru.user.todolist.repository.MockItemRepository
import ru.user.todolist.service.impl.ItemServiceImpl

@RunWith(BlockJUnit4ClassRunner::class)
@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
class ItemServiceImplUnitTest {
    private lateinit var itemServiceImpl: ItemServiceImpl

    private val repository = MockItemRepository()

    @Before
    fun setUp() {
        println("Running tests")
        itemServiceImpl =
            ItemServiceImpl(repository)
    }

    @Test
    @Throws(Exception::class)
    fun test001_whenInsertThenCorrect() {
        val expected = Item(
            text = "new item"
        )
        itemServiceImpl.insert(expected)
        val actual = repository.getById(repository.counter.get())
        assertNotNull(actual)

        assertNotNull(actual.id)
        assertEquals(expected.text, actual.text)
    }


    @Test
    @Throws(Exception::class)
    fun test002_whenUpdateThenCorrect() {
        val expected = Item(
            text = "new new item",
        )
        itemServiceImpl.insert(expected)

        val id = repository.counter.get()

        expected.id = id
        itemServiceImpl.update(expected)
        val actual = repository.getById(id)

        assertNotNull(actual)
        assertEquals(expected.text, actual.text)
    }

    @Test(expected = EntryExistsException::class)
    fun test003_whenInsertThenThrowEntryExistsException() {
        val toPersist = Item(
            id = 15,
            text = "new actual"
        )
        itemServiceImpl.insert(toPersist)
        itemServiceImpl.insert(toPersist)
    }


    @Test(expected = TextValidationException::class)
    fun test007_whenInsertWithTooLongNameThenThrowNameValidationException() {
        val tooLongName = StringBuilder()
        for (s: Int in 0..102) tooLongName.append("a")
        val toPersist = Item(text = tooLongName.toString())
        itemServiceImpl.insert(toPersist)
    }

    @Test(expected = TextValidationException::class)
    fun test008_whenInsertWithEmptyNameThenThrowNameValidationException() {
        val toPersist = Item(text = "")
        itemServiceImpl.insert(toPersist)
    }

    @Test
    fun test009_whenSelectThenExpectOnlyFirstTen() {
        for (i in 1..11) {
            val toPersist = Item(id = i, text = "$i")
            itemServiceImpl.insert(toPersist)
        }
        val all = itemServiceImpl.getAll()
        assertEquals(10, all.size)
    }

    @Test
    fun test010_whenRemoveThenExpectNothing() {
        val toPersist = Item(text = "ssss")
        itemServiceImpl.insert(toPersist)
        itemServiceImpl.remove(repository.counter.get())
    }

    @Test(expected = EntryNotFoundException::class)
    fun test011_whenRemoveNotExistingThenExpectException() {
        itemServiceImpl.remove(32)
    }
}
