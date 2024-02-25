package com.intern.calculator.goods.data.Repository.Offline

import com.intern.calculator.goods.data.Classes.QuantityUnit
import com.intern.calculator.goods.data.DAO.QuantityUnitDAO
import com.intern.calculator.goods.data.Repository.Online.QuantityUnitRepository
import kotlinx.coroutines.flow.Flow

class OfflineQuantityUnitRepository(private val quantityUnitDAO: QuantityUnitDAO) :
    QuantityUnitRepository {
    override fun getAllQuantityUnitStream(): Flow<List<QuantityUnit>> =
        quantityUnitDAO.getAllQuantityUnit()

    override fun getSpecificQuantityUnitStream(name: Int): Flow<QuantityUnit?> =
        quantityUnitDAO.getSpecificQuantityUnit(name)

    override suspend fun insertQuantityUnit(quantityUnit: QuantityUnit) =
        quantityUnitDAO.insert(quantityUnit)

    override suspend fun deleteQuantityUnit(quantityUnit: QuantityUnit) =
        quantityUnitDAO.delete(quantityUnit)

    override suspend fun update(quantityUnit: QuantityUnit) = quantityUnitDAO.update(quantityUnit)
}