package com.intern.calculator.goods.data

import kotlinx.coroutines.flow.Flow

class OfflineItemsRepository(private val itemDao: ItemDAO) : ItemsRepository {
    override fun getAllItemsForListSteam(id: Int): Flow<List<Item>> = itemDao.getAllItemsForList(id)

    override fun getAllItemsStream(): Flow<List<Item>> = itemDao.getAllItems()

    override suspend fun insertItem(item: Item) = itemDao.insert(item)

    override suspend fun deleteItem(item: Item) = itemDao.delete(item)

    override suspend fun updateItem(item: Item) = itemDao.update(item)
}