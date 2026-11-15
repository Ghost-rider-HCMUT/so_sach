package com.sotraubo.app.core.data;

import com.sotraubo.app.core.common.dispatcher.AppDispatchers;
import com.sotraubo.app.core.common.format.ReportPeriodType;
import com.sotraubo.app.core.data.repository.OfflineCattleBusinessRepository;
import com.sotraubo.app.core.data.repository.OperationOutcome;
import com.sotraubo.app.core.database.dao.CashTransactionQueryProjection;
import com.sotraubo.app.core.database.dao.CattleBusinessDao;
import com.sotraubo.app.core.database.dao.TradeOrderQueryProjection;
import com.sotraubo.app.core.database.entity.AttachmentEntity;
import com.sotraubo.app.core.database.entity.CashTransactionEntity;
import com.sotraubo.app.core.database.entity.ContactEntity;
import com.sotraubo.app.core.database.entity.TradeOrderEntity;
import com.sotraubo.app.core.model.CashFlowType;
import com.sotraubo.app.core.model.DebtSortOption;
import com.sotraubo.app.core.model.PaymentMethod;
import com.sotraubo.app.core.model.TradeOrderType;
import kotlinx.coroutines.CoroutineDispatcher;
import kotlinx.coroutines.ExperimentalCoroutinesApi;
import kotlinx.coroutines.flow.Flow;
import org.junit.Before;
import org.junit.Test;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\f\u0010\t\u001a\u00060\nj\u0002`\u000bH\u0007J\f\u0010\f\u001a\u00060\nj\u0002`\u000bH\u0007J\f\u0010\r\u001a\u00060\nj\u0002`\u000bH\u0007J\f\u0010\u000e\u001a\u00060\nj\u0002`\u000bH\u0007J\b\u0010\u000f\u001a\u00020\nH\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0010"}, d2 = {"Lcom/sotraubo/app/core/data/CattleBusinessRepositoryTest;", "", "()V", "dao", "Lcom/sotraubo/app/core/data/InMemoryCattleBusinessDao;", "repository", "Lcom/sotraubo/app/core/data/repository/OfflineCattleBusinessRepository;", "testDispatcher", "Lkotlinx/coroutines/CoroutineDispatcher;", "TC-01 and TC-02 Purchase with partial payment and subsequent payment updates debt accurately", "", "Lkotlinx/coroutines/test/TestResult;", "TC-03 and TC-04 Sale with partial receipt and subsequent receipt updates receivable debt", "TC-05 Bidirectional debt for a single person is kept strictly separate without offsetting", "TC-06 TC-07 TC-11 TC-12 TC-14 Standalone cash, full settlement, edit and delete recalculation and overpayment block", "setUp", "app_debugUnitTest"})
@kotlin.OptIn(markerClass = {kotlinx.coroutines.ExperimentalCoroutinesApi.class})
public final class CattleBusinessRepositoryTest {
    private com.sotraubo.app.core.data.InMemoryCattleBusinessDao dao;
    private com.sotraubo.app.core.data.repository.OfflineCattleBusinessRepository repository;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.CoroutineDispatcher testDispatcher = null;
    
    public CattleBusinessRepositoryTest() {
        super();
    }
    
    @org.junit.Before()
    public final void setUp() {
    }
}