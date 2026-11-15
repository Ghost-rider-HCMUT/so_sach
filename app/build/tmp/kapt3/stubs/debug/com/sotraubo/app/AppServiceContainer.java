package com.sotraubo.app;

import android.app.Application;
import com.sotraubo.app.core.common.dispatcher.AppDispatchers;
import com.sotraubo.app.core.common.dispatcher.StandardAppDispatchers;
import com.sotraubo.app.core.data.repository.CattleBusinessRepository;
import com.sotraubo.app.core.data.repository.OfflineCattleBusinessRepository;
import com.sotraubo.app.core.database.CattleBusinessDatabase;
import com.sotraubo.app.feature.backupreport.BackupAndPdfManager;
import com.sotraubo.app.feature.security.PinSecurityManager;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\t\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\r\u001a\u00020\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0011\u0010\u0011\u001a\u00020\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0011\u0010\u0015\u001a\u00020\u0016\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018R\u0011\u0010\u0019\u001a\u00020\u001a\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u001cR\u0011\u0010\u001d\u001a\u00020\u001e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010 \u00a8\u0006!"}, d2 = {"Lcom/sotraubo/app/AppServiceContainer;", "", "application", "Landroid/app/Application;", "(Landroid/app/Application;)V", "backupAndPdfManager", "Lcom/sotraubo/app/feature/backupreport/BackupAndPdfManager;", "getBackupAndPdfManager", "()Lcom/sotraubo/app/feature/backupreport/BackupAndPdfManager;", "dao", "Lcom/sotraubo/app/core/database/dao/CattleBusinessDao;", "getDao", "()Lcom/sotraubo/app/core/database/dao/CattleBusinessDao;", "database", "Lcom/sotraubo/app/core/database/CattleBusinessDatabase;", "getDatabase", "()Lcom/sotraubo/app/core/database/CattleBusinessDatabase;", "dispatchers", "Lcom/sotraubo/app/core/common/dispatcher/AppDispatchers;", "getDispatchers", "()Lcom/sotraubo/app/core/common/dispatcher/AppDispatchers;", "ledgerDao", "Lcom/sotraubo/app/core/database/dao/LedgerDao;", "getLedgerDao", "()Lcom/sotraubo/app/core/database/dao/LedgerDao;", "pinSecurityManager", "Lcom/sotraubo/app/feature/security/PinSecurityManager;", "getPinSecurityManager", "()Lcom/sotraubo/app/feature/security/PinSecurityManager;", "repository", "Lcom/sotraubo/app/core/data/repository/CattleBusinessRepository;", "getRepository", "()Lcom/sotraubo/app/core/data/repository/CattleBusinessRepository;", "app_debug"})
public final class AppServiceContainer {
    @org.jetbrains.annotations.NotNull()
    private final com.sotraubo.app.core.common.dispatcher.AppDispatchers dispatchers = null;
    @org.jetbrains.annotations.NotNull()
    private final com.sotraubo.app.core.database.CattleBusinessDatabase database = null;
    @org.jetbrains.annotations.NotNull()
    private final com.sotraubo.app.core.database.dao.CattleBusinessDao dao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.sotraubo.app.core.database.dao.LedgerDao ledgerDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.sotraubo.app.core.data.repository.CattleBusinessRepository repository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.sotraubo.app.feature.backupreport.BackupAndPdfManager backupAndPdfManager = null;
    @org.jetbrains.annotations.NotNull()
    private final com.sotraubo.app.feature.security.PinSecurityManager pinSecurityManager = null;
    
    public AppServiceContainer(@org.jetbrains.annotations.NotNull()
    android.app.Application application) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.sotraubo.app.core.common.dispatcher.AppDispatchers getDispatchers() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.sotraubo.app.core.database.CattleBusinessDatabase getDatabase() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.sotraubo.app.core.database.dao.CattleBusinessDao getDao() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.sotraubo.app.core.database.dao.LedgerDao getLedgerDao() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.sotraubo.app.core.data.repository.CattleBusinessRepository getRepository() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.sotraubo.app.feature.backupreport.BackupAndPdfManager getBackupAndPdfManager() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.sotraubo.app.feature.security.PinSecurityManager getPinSecurityManager() {
        return null;
    }
}