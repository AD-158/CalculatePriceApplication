package com.intern.calculator.goods.data.repository.offline

import com.intern.calculator.goods.data.classes.QuantityUnit
import com.intern.calculator.goods.data.dao.QuantityUnitDAO
import com.intern.calculator.goods.data.repository.online.QuantityUnitRepository
import kotlinx.coroutines.flow.Flow

class OfflineQuantityUnitRepository(private val quantityUnitDAO: QuantityUnitDAO) :
    QuantityUnitRepository {
    // Get a flow of all quantity units from the local database
    override fun getAllQuantityUnitStream(): Flow<List<QuantityUnit>> =
        quantityUnitDAO.getAllQuantityUnit()

    // Get a flow of a specific quantity unit by its name from the local database
    override fun getSpecificQuantityUnitStream(name: Int): Flow<QuantityUnit?> =
        quantityUnitDAO.getSpecificQuantityUnit(name)

    // Insert a new quantity unit into the local database
    override suspend fun insertQuantityUnit(quantityUnit: QuantityUnit) =
        quantityUnitDAO.insert(quantityUnit)

    // Delete a quantity unit from the local database
    override suspend fun deleteQuantityUnit(quantityUnit: QuantityUnit) =
        quantityUnitDAO.delete(quantityUnit)

    // Update an existing quantity unit in the local database
    override suspend fun update(quantityUnit: QuantityUnit) = quantityUnitDAO.update(quantityUnit)
}