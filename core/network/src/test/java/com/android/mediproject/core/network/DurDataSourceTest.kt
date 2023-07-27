package com.android.mediproject.core.network

import com.android.mediproject.core.model.dur.DurItemWrapperFactory
import com.android.mediproject.core.model.dur.DurType
import com.android.mediproject.core.model.dur.durproduct.capacity.DurProductCapacityAttentionResponse
import com.android.mediproject.core.network.datasource.dur.DurProductDataSourceImpl
import com.android.mediproject.core.network.module.datagokr.DurProductInfoNetworkApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import kotlin.reflect.KClass
import kotlin.reflect.jvm.javaType


class DurDataSourceTest {

    @Mock private lateinit var durProductInfoNetworkApi: DurProductInfoNetworkApi


    private lateinit var durProductDataSourceImpl: DurProductDataSourceImpl

    private val durProductCapacityAttentionResponse: DurProductCapacityAttentionResponse = DurProductCapacityAttentionResponse()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        durProductDataSourceImpl = DurProductDataSourceImpl(durProductInfoNetworkApi)
    }

    @Test
    fun testMethods() = runBlocking {
        val ktype = DurProductDataSourceImpl::getCapacityAttentionInfo.returnType
        val kclass = ktype.classifier as KClass<out Any>
        val type = ktype.javaType
        val args = ktype.arguments
        val kfunction = durProductDataSourceImpl::getCapacityAttentionInfo
    }

    @Test
    fun testGetDurList() = runBlocking {
        // 임의의 itemSeq 정의
        val itemSeq = "testItemSeq"

    }

    @Test
    fun convertDurProductToDurItem() = runBlocking {
        val durType = DurType.CAPACITY_ATTENTION
        val result = DurItemWrapperFactory.createForDurProduct(durType, durProductCapacityAttentionResponse).convert()
    }

}
