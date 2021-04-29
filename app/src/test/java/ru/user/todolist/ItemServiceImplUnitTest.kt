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
import ru.user.todolist.repository.MockItemRepository
import ru.user.todolist.service.impl.ItemServiceImpl

@RunWith(BlockJUnit4ClassRunner::class)
@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
class ItemServiceImplUnitTest {
    private lateinit var itemServiceImpl: ItemServiceImpl

    private val repository = MockItemRepository()

    @Before
    fun setUp() {
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


}
