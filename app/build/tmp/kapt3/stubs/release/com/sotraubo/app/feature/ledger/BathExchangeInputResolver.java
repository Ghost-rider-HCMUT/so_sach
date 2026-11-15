package com.sotraubo.app.feature.ledger;

/**
 * Bộ xử lý quy tắc nhập liệu linh động cho Đổi Bath Thái:
 * - Nếu chưa nhập tiền Việt: tự nhân `bahtAmount * rate` (nếu thiếu 1 trong 2 thì báo lỗi rõ ràng).
 * - Nếu đã nhập tiền Việt: không bắt buộc nhập Bath hay Tỉ giá.
 * - Cho phép nhập cả Bath và tiền Việt mà không cần nhập Tỉ giá.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0002\b\u0006\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002JB\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\b2\u0006\u0010\n\u001a\u00020\b2\u0006\u0010\u000b\u001a\u00020\b2\b\b\u0002\u0010\f\u001a\u00020\b2\b\b\u0002\u0010\r\u001a\u00020\b\u00a8\u0006\u000e"}, d2 = {"Lcom/sotraubo/app/feature/ledger/BathExchangeInputResolver;", "", "()V", "resolve", "Lcom/sotraubo/app/feature/ledger/BathExchangeResolution;", "directionCode", "", "bahtAmount", "", "rate", "vndAmount", "theoreticalVndAmount", "costRate", "costVndAmount", "app_release"})
public final class BathExchangeInputResolver {
    @org.jetbrains.annotations.NotNull()
    public static final com.sotraubo.app.feature.ledger.BathExchangeInputResolver INSTANCE = null;
    
    private BathExchangeInputResolver() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.sotraubo.app.feature.ledger.BathExchangeResolution resolve(@org.jetbrains.annotations.NotNull()
    java.lang.String directionCode, long bahtAmount, long rate, long vndAmount, long theoreticalVndAmount, long costRate, long costVndAmount) {
        return null;
    }
}