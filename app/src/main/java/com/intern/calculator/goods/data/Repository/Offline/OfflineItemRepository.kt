package com.intern.calculator.goods.data.Repository.Offline

import com.intern.calculator.goods.data.Classes.Item
import com.intern.calculator.goods.data.DAO.ItemDAO
import com.intern.calculator.goods.data.Repository.Online.ItemsRepository
import kotlinx.coroutines.flow.Flow

class OfflineItemsRepository(private val itemDao: ItemDAO) : ItemsRepository {
    override fun getAllItemsForListSteam(id: Int): Flow<List<Item>> = itemDao.getAllItemsForList(id)

    override fun getAllItemsStream(): Flow<List<Item>> = itemDao.getAllItems()

    override fun getItemStream(id: Int): Flow<Item?> = itemDao.getItem(id)

    override suspend fun insertItem(item: Item) = itemDao.insert(item)

    override suspend fun deleteItem(item: Item) = itemDao.delete(item)

    override suspend fun updateItem(item: Item) = itemDao.update(item)
}