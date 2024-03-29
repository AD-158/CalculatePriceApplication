package com.intern.calculator.goods.data.repository.online

import com.intern.calculator.goods.data.classes.QuantityUnit
import kotlinx.coroutines.flow.Flow

interface QuantityUnitRepository {
    fun getAllQuantityUnitStream(): Flow<List<QuantityUnit>>

    fun getSpecificQuantityUnitStream(name: Int): Flow<QuantityUnit?>

    suspend fun insertQuantityUnit(quantityUnit: QuantityUnit)

    suspend fun deleteQuantityUnit(quantityUnit: QuantityUnit)

    suspend fun update(quantityUnit: QuantityUnit)
}