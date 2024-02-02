package com.intern.calculator.goods.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking

class OfflineQuantityUnitRepository(private val quantityUnitDAO: QuantityUnitDAO) : QuantityUnitRepository {
    override fun getAllQuantityUnitStream(): Flow<List<QuantityUnit>> =
        quantityUnitDAO.getAllQuantityUnit()

    override fun getSpecificQuantityUnitStream(name: Int): Flow<QuantityUnit?> =
        quantityUnitDAO.getSpecificQuantityUnit(name)

    override suspend fun insertQuantityUnit(quantityUnit: QuantityUnit) =
        quantityUnitDAO.insert(quantityUnit)

    override suspend fun deleteQuantityUnit(quantityUnit: QuantityUnit) =
        quantityUnitDAO.delete(quantityUnit)

    override suspend fun update(quantityUnit: QuantityUnit) = quantityUnitDAO.update(quantityUnit)

    override suspend fun getAllQuantityUnitsSync(): List<QuantityUnit> {
        val quantityUnitList: MutableList<QuantityUnit> = mutableListOf()
        runBlocking {
            val flowOfList: Flow<List<QuantityUnit>> = quantityUnitDAO.getAllQuantityUnit()
            flowOfList.collect { list ->
                quantityUnitList.addAll(list)
            }

        }
        return quantityUnitList
    }
}