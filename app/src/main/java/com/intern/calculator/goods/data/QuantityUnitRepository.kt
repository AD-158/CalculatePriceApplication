package com.intern.calculator.goods.data

import kotlinx.coroutines.flow.Flow

/**
 * Repository that provides insert, update, delete, and retrieve of [QuantityUnit] from a given data source.
 */
interface QuantityUnitRepository {
    fun getAllQuantityUnitStream(): Flow<List<QuantityUnit>>

    fun getSpecificQuantityUnitStream(name: Int): Flow<QuantityUnit?>

    suspend fun insertQuantityUnit(quantityUnit: QuantityUnit)

    suspend fun deleteQuantityUnit(quantityUnit: QuantityUnit)

    suspend fun update(quantityUnit: QuantityUnit)

    suspend fun getAllQuantityUnitsSync(): List<QuantityUnit>
}