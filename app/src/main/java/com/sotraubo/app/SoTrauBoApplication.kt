package com.sotraubo.app

import android.app.Application
import com.sotraubo.app.core.common.dispatcher.AppDispatchers
import com.sotraubo.app.core.common.dispatcher.StandardAppDispatchers
import com.sotraubo.app.core.data.repository.CattleBusinessRepository
import com.sotraubo.app.core.data.repository.OfflineCattleBusinessRepository
import com.sotraubo.app.core.database.CattleBusinessDatabase
import com.sotraubo.app.feature.backupreport.BackupAndPdfManager
import com.sotraubo.app.feature.security.PinSecurityManager

class AppServiceContainer(application: Application) {
    val dispatchers: AppDispatchers = StandardAppDispatchers()
    val database: CattleBusinessDatabase = CattleBusinessDatabase.getInstance(application)
    val dao = database.cattleBusinessDao()
    val ledgerDao = database.ledgerDao()
    val repository: CattleBusinessRepository = OfflineCattleBusinessRepository(
        database = database,
        dao = dao,
        dispatchers = dispatchers
    )
    val backupAndPdfManager: BackupAndPdfManager = BackupAndPdfManager(
        dao = dao,
        ledgerDao = ledgerDao,
        dispatchers = dispatchers
    )
    val pinSecurityManager: PinSecurityManager = PinSecurityManager.create(application)
}

class SoTrauBoApplication : Application() {
    lateinit var container: AppServiceContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppServiceContainer(this)
    }
}
