package com.intern.calculator.goods.data.repository.offline

import com.intern.calculator.goods.data.classes.Item
import com.intern.calculator.goods.data.dao.ItemDAO
import com.intern.calculator.goods.data.repository.online.ItemsRepository
import kotlinx.coroutines.flow.Flow

class OfflineItemsRepository(private val itemDao: ItemDAO) : ItemsRepository {
    // Get a flow of all items for a given category ID from the local database
    override fun getAllItemsForListStream(id: Int): Flow<List<Item>> = itemDao.getAllItemsForList(id)

    // Get a flow of all items from the local database
    override fun getAllItemsStream(): Flow<List<Item>> = itemDao.getAllItems()

    // Get a flow of a specific item by its ID from the local database
    override fun getItemStream(id: Int): Flow<Item?> = itemDao.getItem(id)

    // Insert a new item into the local database
    override suspend fun insertItem(item: Item) = itemDao.insert(item)

    // Delete an item from the local database
    override suspend fun deleteItem(item: Item) = itemDao.delete(item)

    // Update an existing item in the local database
    override suspend fun updateItem(item: Item) = itemDao.update(item)
}