package com.sotraubo.app.core.common.format;

/**
 * Formats and appends clear installment payment history lines to a record's note field.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0006\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J@\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u00072\u0006\u0010\t\u001a\u00020\u00072\u0006\u0010\n\u001a\u00020\u00072\u0006\u0010\u000b\u001a\u00020\u00042\b\b\u0002\u0010\f\u001a\u00020\u0004\u00a8\u0006\r"}, d2 = {"Lcom/sotraubo/app/core/common/format/InstallmentNoteFormatter;", "", "()V", "appendInstallmentNote", "", "existingNote", "dateMillis", "", "addedAmount", "newTotalPaid", "remainingAfter", "actionLabel", "installmentNote", "app_release"})
public final class InstallmentNoteFormatter {
    @org.jetbrains.annotations.NotNull()
    public static final com.sotraubo.app.core.common.format.InstallmentNoteFormatter INSTANCE = null;
    
    private InstallmentNoteFormatter() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String appendInstallmentNote(@org.jetbrains.annotations.NotNull()
    java.lang.String existingNote, long dateMillis, long addedAmount, long newTotalPaid, long remainingAfter, @org.jetbrains.annotations.NotNull()
    java.lang.String actionLabel, @org.jetbrains.annotations.NotNull()
    java.lang.String installmentNote) {
        return null;
    }
}