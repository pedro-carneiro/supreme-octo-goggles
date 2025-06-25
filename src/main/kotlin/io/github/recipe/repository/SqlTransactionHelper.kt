package io.github.recipe.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Component
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.TransactionDefinition
import org.springframework.transaction.support.TransactionTemplate

@Component
class SqlTransactionHelper(
    private val transactionManager: PlatformTransactionManager,
) {
    companion object {
        private const val DEFAULT_QUERY_TIMEOUT = 30
    }

    suspend fun <T> execute(
        isolationLevel: Int = TransactionDefinition.ISOLATION_DEFAULT,
        timeout: Int = DEFAULT_QUERY_TIMEOUT,
        isReadOnly: Boolean = false,
        block: () -> T,
    ): T = withContext(Dispatchers.IO) {
        executeBlocking(isolationLevel, timeout, isReadOnly, block)
    }

    fun <T> executeBlocking(
        isolationLevel: Int = TransactionDefinition.ISOLATION_DEFAULT,
        timeout: Int = DEFAULT_QUERY_TIMEOUT,
        isReadOnly: Boolean = false,
        block: () -> T,
    ): T {
        val template = TransactionTemplate(transactionManager)
        template.isolationLevel = isolationLevel
        template.isReadOnly = isReadOnly
        template.timeout = timeout
        return template.execute { block() }!!
    }
}
